package de.unistuttgart.iaas.cc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.PropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class TextProcessorWorker {

    final static Logger logger = LoggerFactory.getLogger(TextProcessorWorker.class);

    private enum MessagingProvider {
        ActiveMQ, SQS
    }

    ;

    private static String provider;
    private static String workerName;
    private static String connectionFactoryName;
    private static String requestQueueName;
    private static String responseQueueName;

    // properties file containing the AWS API credentials (access key and secret
    // key)
    private static final String awsCredentialsFile = "aws.properties";

    private static String workerProps = "worker.properties";

    public static void main(String[] args) {
        if (args.length > 0) {
            workerProps = args[0];
        }
        initProperties();

        boolean success;
        switch (provider) {
            case "ActiveMQ":
                success = initConnection(MessagingProvider.ActiveMQ);
                break;
            case "SQS":
            default:
                success = initConnection(MessagingProvider.SQS);
                break;
        }

        if (success) {
            while (true) {
                try {
                    Thread.sleep(1000 * 1);
                    System.out.print(".");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void initProperties() {
        Properties prop = new Properties();
        try (InputStream is = new FileInputStream(Paths.get(workerProps).toFile())) {
            prop.load(is);
        } catch (FileNotFoundException e1) {
            System.err.println(workerProps + " not found");
        } catch (IOException e) {
            System.err.println("Error reading " + workerProps);
        }
        provider = prop.getProperty("provider", "SQS");
        workerName = prop.getProperty("worker", UUID.randomUUID().toString());
        connectionFactoryName = prop.getProperty("conFactory", "ConnectionFactory");
        requestQueueName = prop.getProperty("requestQueue", "requests");
        responseQueueName = prop.getProperty("responseQueue", "responses");
    }

    private static boolean initConnection(MessagingProvider provider) {
        try {
            Context jndi = null;
            QueueConnectionFactory conFactory;
            Queue requestQueue;
            Queue responseQueue;
            switch (provider) {
                case ActiveMQ:
                    // reads the configuration from jndi.properties file
                    jndi = new InitialContext();
                    // connect to messaging system
                    conFactory = (QueueConnectionFactory) jndi.lookup(connectionFactoryName);
                    break;
                case SQS:
                    // Create the connection factory using the properties file
                    // credential provider.
                    conFactory = SQSConnectionFactory.builder().withRegion(Region.getRegion(Regions.EU_WEST_1))
                            .withAWSCredentialsProvider(new PropertiesFileCredentialsProvider(awsCredentialsFile)).build();
                    break;
                default:
                    logger.warn("Cannot connect to messaging system.");
                    return false;
            }

            // create connection
            QueueConnection connection = conFactory.createQueueConnection();
            // create session
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            // get queues
            switch (provider) {
                case ActiveMQ:
                    // lookup queue
                    requestQueue = (Queue) jndi.lookup(requestQueueName);
                    responseQueue = (Queue) jndi.lookup(responseQueueName);
                    break;
                case SQS:
                    requestQueue = session.createQueue(requestQueueName);
                    responseQueue = session.createQueue(responseQueueName);
                    break;
                default:
                    logger.warn("Cannot get queues.");
                    return false;
            }

            // create sender and receiver
            QueueSender sender = session.createSender(responseQueue);
            QueueReceiver receiver = session.createReceiver(requestQueue);
            receiver.setMessageListener(new ML(sender, session));
            // start connection (!)
            connection.start();
        } catch (NamingException e) {
            e.printStackTrace();
            return false;
        } catch (JMSException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static class ML implements MessageListener {

        private final QueueSession session;
        private final QueueSender sender;

        public ML(QueueSender sender, QueueSession session) {
            this.sender = sender;
            this.session = session;
        }

        @Override
        public void onMessage(Message message) {
            logger.debug("Receiving request");
            if (message instanceof TextMessage) {
                try {
                    TextMessage request = (TextMessage) message;
                    // build response message
                    TextMessage response = session.createTextMessage();
                    // set text
                    response.setText("[processed by worker " + workerName + "] - " + request.getText());
                    // copy properties from request to response
                    @SuppressWarnings("unchecked")
                    Enumeration<String> props = request.getPropertyNames();
                    while (props.hasMoreElements()) {
                        String pn = props.nextElement();
                        response.setStringProperty(pn, request.getStringProperty(pn));
                    }
                    //response.setStringProperty("id", request.getStringProperty("id"));
                    sender.send(response);
                    logger.debug("Sent response.");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
