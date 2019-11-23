# textprocessor-worker

A simple application using JMS for messaging. It listens on a queue for text messages, reads and modifies the text, and then sends it to another queue. All properties of the request message are copied to the response message.

Start the application like this:

```java -jar worker-jar-with-dependencies.jar worker.properties```

If no properties file is given, `worker.properties` is used as default.

The application can connect to ActiveMQ as well as to AWS SQS. When using ActiveMQ, the `src/main/resource/jndi.properties` file may have to be adapted. When using SQS, you have to provide your API credentials in a file named `aws.properties`. Please note that the application currently only works with the `eu-west-1` region.  
__Note: Do not rename__ `aws.properties.example` __to__ `aws.properties` __but rather create a new file named__ `aws.properties` __instead.__  
Otherwise, Git will no longer ignore the file, possibly resulting in your AWS credentials being committed and pushed and available to everyone. 

Worker can be configured with different names and that names then show up in the modified text (which is very useful for debugging as you can easily identify which worker processed your message). For running two worker with different names on the same SQS queues, the two example files `worker1.properties` and `worker2.properties` are given. They assume that the Queues `TextProcessorRequests` and `TextProcessorResponses` have already been created using the AWS console. 
