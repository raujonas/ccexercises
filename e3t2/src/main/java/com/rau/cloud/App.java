package com.rau.cloud;


import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.s3.model.Tag;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        AwsCredentials awsCredentials = AwsBasicCredentials.create(
                System.getenv("AWS_ID"),
                System.getenv("AWS_KEY")
        );

        Ec2Client ec2 = Ec2Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();

        String amiId = "ami-04b9e92b5572fa0d1";

        RunInstancesRequest runInstancesRequest = RunInstancesRequest.builder()
                .imageId(amiId)
                .instanceType(InstanceType.T1_MICRO)
                .maxCount(1)
                .minCount(1)
                .build();

        RunInstancesResponse runInstancesResponse = ec2.runInstances(runInstancesRequest);

        String instanceId = runInstancesResponse.instances().get(0).instanceId();

        String name = "java created";

        Tag tag = Tag.builder()
                .key(name)
                .value(name)
                .build();

        CreateTagsRequest createTagsRequest = CreateTagsRequest.builder()
                .resources(instanceId)
                .build();

        try {
            ec2.createTags(createTagsRequest);
            System.out.printf(
                    "Successfully started EC2 instance %s based on AMI %s", instanceId, amiId);
        }
        catch (Ec2Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
