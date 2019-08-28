package com.cyient.iot.ambulance;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitosync.AmazonCognitoSyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Created by Administrator on 24-10-2017.
 */

public class ManagerClass {

    private CognitoCachingCredentialsProvider credentialsProvider = null;
    private CognitoSyncManager syncManager = null;
    private AmazonS3Client s3Client = null;
    private TransferUtility transferUtility = null;
    private AmazonCognitoSyncClient amazonCognitoSyncClient = null;

    public static AmazonDynamoDBClient dynamoDBClient = null;
    public static DynamoDBMapper dynamoDBMapper = null;

    public CognitoCachingCredentialsProvider getCredentials(Context context) {

        // Initialize the Amazon Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(context, "us-east-2:263761a1-38eb-4848-9362-8dfc35979f6b", Regions.US_EAST_2);
        return credentialsProvider;
    }

    public AmazonS3Client initS3Client(Context context) {
        if (credentialsProvider == null) {
            getCredentials(context);
            s3Client = new AmazonS3Client(credentialsProvider);
            s3Client.setRegion(Region.getRegion(Regions.US_EAST_2));
        }
        return s3Client;
    }

    public TransferUtility checkTransferUtility(AmazonS3Client s3Client, Context context) {
        if (transferUtility == null) {
            transferUtility = new TransferUtility(s3Client, context);
            return transferUtility;
        } else {
            return transferUtility;
        }
    }

    public DynamoDBMapper initDynamoClient(CognitoCachingCredentialsProvider credentialsProvider) {

        if (dynamoDBClient == null) {
            dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
            dynamoDBClient.setRegion(Region.getRegion(Regions.US_EAST_2));
            dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
        }

        return dynamoDBMapper;
    }
}