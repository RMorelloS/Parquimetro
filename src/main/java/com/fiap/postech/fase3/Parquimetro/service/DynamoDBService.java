package com.fiap.postech.fase3.Parquimetro.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamoDBService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
   
    public List<Map<String, AttributeValue>> scanForTable(String tableName, String value, String column){
        Map<String, AttributeValue> expressionAttributeValues =
                new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", new AttributeValue().withS(value));

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(tableName)
                .withFilterExpression(column + "= :val")
                .withExpressionAttributeValues(expressionAttributeValues);
        ScanResult result = dynamoDBMapper.scan(scanRequest);
        return result.getItems();
    }
}
