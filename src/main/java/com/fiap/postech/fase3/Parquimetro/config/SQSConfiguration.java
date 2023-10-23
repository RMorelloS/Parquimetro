package com.fiap.postech.fase3.Parquimetro.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Configuration
public class SQSConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(){
        return new QueueMessagingTemplate(amazonSQSAsync());
    }
    @Primary
    @Bean
    public AmazonSQSAsync amazonSQSAsync(){
        return AmazonSQSAsyncClientBuilder.standard().withRegion(
                //env.getProperty("aws-region")
                "us-east-2"
                )
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(
                                "AKIAUCNEGZSQBEV4MFQA",
                                "Gli2/1YlhEN3gmwsZ9U1rSDsD6J1LjxRCUqu69fB"
                                //env.getProperty("aws-accesskey"),
                                //env.getProperty("aws-secretkey")
                                )

                )).build();
    }
}
