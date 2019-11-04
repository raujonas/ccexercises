package com.rau.cloud;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.InstanceType;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        Ec2Client ec2 = Ec2Client.create();

        RunInstancesRequest runInstancesRequest = RunInstancesRequest.builder()
                .imageId("ami-04b9e92b5572fa0d1")
                .instanceType(InstanceType.T1_MICRO)
                .maxCount(1)
                .minCount(1)
                .build();

        RunInstancesResponse runInstancesResponse = ec2.runInstances(runInstancesRequest);

        String instanceId = runInstancesResponse.instances().get(0).instanceId();
    }
}
