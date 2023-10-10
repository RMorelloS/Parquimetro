package com.fiap.postech.fase3.Parquimetro.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CondutorRepository {
     @Autowired
    private DynamoDBMapper dynamoDBMapper;

     public Condutor save(Condutor condutor){
         try {
             dynamoDBMapper.save(condutor);
         }catch(Exception e){
             throw e;
         }
         return condutor;
     }

     public Condutor getCondutorById(String condutor_CPF){

         return dynamoDBMapper.load(Condutor.class, condutor_CPF);
     }


     public String delete(String condutor_CPF){
         Condutor condutor = dynamoDBMapper.load(Condutor.class, condutor_CPF);
         dynamoDBMapper.delete(condutor);
         return "Condutor deletado";
     }
     public String update(String condutor_CPF, Condutor condutor){
         dynamoDBMapper.save(condutor, new DynamoDBSaveExpression()
                 .withExpectedEntry("condutor_CPF",
                         new ExpectedAttributeValue(
                                 new AttributeValue().withS(condutor_CPF)
                         )));
         return condutor_CPF;
     }

    public List<Condutor> getCondutores() {
         return dynamoDBMapper.scan(Condutor.class, new DynamoDBScanExpression());
    }
}
