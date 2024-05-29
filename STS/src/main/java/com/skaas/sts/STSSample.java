package com.skaas.sts;

import java.util.Scanner;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.regions.Region;


import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;


public class STSSample {

    public static void main(String[] args) {
        // Change all these values
        String roleSessionName = "";
        String roleArn = "";
        Region region = Region.US_WEST_2;

        // Your Access Keys and Secret Keys here
        String accessKey = "";
        String secretKey = "";

        // For Getting Runtime input
        Scanner scanner = new Scanner(System.in);


        System.out.println("Creating STS Client with AWS credentials");
        StsClient sts = StsClient.builder()
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .region(region)
            .build();


        // Assuming EC2 Role
        System.out.println("Assuming EC2 Role");
        AssumeRoleResponse assumeRoleResponse = sts.assumeRole(
            AssumeRoleRequest.builder()
                .roleSessionName(roleSessionName)
                .roleArn(roleArn)
                .durationSeconds(900)
                .build()
        );

        String  tempAccessKey = assumeRoleResponse.credentials().accessKeyId(),
                tempSecretKey = assumeRoleResponse.credentials().secretAccessKey(),
                tempSessionToken = assumeRoleResponse.credentials().sessionToken();

        System.out.println(
                "\n Access Key: \t"    + tempAccessKey +
                "\n Secret Key: \t"     + tempSecretKey +
                "\n Session Token: \t"   + tempSessionToken
        );
        System.out.println("Press ENTER to Continue...");
        scanner.nextLine();


        // Using your Temporary Credentials with Elevated Permissions
        System.out.println("Using your Temporary Credentials with Elevated Permissions");
        Ec2Client ec2 = Ec2Client.builder()
                .credentialsProvider(
                    StaticCredentialsProvider.create(
                        AwsSessionCredentials.create(tempAccessKey, tempSecretKey, tempSessionToken)
                    )
                )
                .region(region)
                .build();
        System.out.println("Press ENTER to Continue...");
        scanner.nextLine();


        // Listing All Instances
        System.out.println("Listing All Instances");
        DescribeInstancesResponse describeInstancesResponse = ec2.describeInstances();
        System.out.println(describeInstancesResponse.reservations());

        scanner.close();
    }

}
