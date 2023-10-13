package com.fiap.postech.fase3.Parquimetro.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
         Condutor condutor = dynamoDBMapper.load(Condutor.class, condutor_CPF);
         return condutor;
     }


     public String delete(String condutor_CPF){
         Condutor condutor = dynamoDBMapper.load(Condutor.class, condutor_CPF);
         dynamoDBMapper.delete(condutor);
         return "Condutor deletado";
     }
     public String update(Condutor condutor){

         try {
             this.delete(condutor.getCondutor_CPF());
             save(condutor);

         }catch(Exception e){
             throw e;
         }
         return "Atualizado com sucesso!";
     }

    public List<Condutor> getCondutores() {
         return dynamoDBMapper.scan(Condutor.class, new DynamoDBScanExpression());
    }

    public PaginatedScanList<Condutor> getCondutorByTempoEstacionamento() {

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withN("1"));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("statusEstacionamento = :val1").withExpressionAttributeValues(eav);

        PaginatedScanList<Condutor> condutoresAtivos = dynamoDBMapper.scan(Condutor.class, scanExpression);

        for (Condutor condutor : condutoresAtivos) {
            System.out.format("Id=%s", condutor);
        }
        return condutoresAtivos;
    }
}
