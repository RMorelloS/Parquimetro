package com.fiap.postech.fase3.Parquimetro.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.fiap.postech.fase3.Parquimetro.model.Veiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VeiculoRepository {
     @Autowired
    private DynamoDBMapper dynamoDBMapper;

     public Veiculo save(Veiculo veiculo, String condutor_CPF){
         try {
             dynamoDBMapper.save(veiculo);
         }catch(Exception e){
             throw e;
         }
         return veiculo;
     }

     public Veiculo getVeiculoById(String id){
         return dynamoDBMapper.load(Veiculo.class, id);
     }


     public String delete(String id){
         Veiculo veiculo = dynamoDBMapper.load(Veiculo.class, id);
         dynamoDBMapper.delete(veiculo);
         return "Veiculo deletado";
     }
     public String update(String id, Veiculo veiculo){
         dynamoDBMapper.save(veiculo, new DynamoDBSaveExpression()
                 .withExpectedEntry("id",
                         new ExpectedAttributeValue(
                                 new AttributeValue().withS(id)
                         )));
         return id;
     }

    public List<Veiculo> getVeiculos() {
         return dynamoDBMapper.scan(Veiculo.class, new DynamoDBScanExpression());
    }
}
