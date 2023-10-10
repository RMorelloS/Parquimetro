package com.fiap.postech.fase3.Parquimetro.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import com.fiap.postech.fase3.Parquimetro.model.CondutorVeiculo;
import com.fiap.postech.fase3.Parquimetro.model.Veiculo;
import com.fiap.postech.fase3.Parquimetro.service.CondutorVeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CondutorVeiculoRepository {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    public PaginatedScanList<CondutorVeiculo> findByCpf(String condutorCpf) {
        Map<String, AttributeValue> expressionAttributeValues =
                new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", new AttributeValue().withN(condutorCpf));

        PaginatedScanList<CondutorVeiculo> listaCondutores =
                dynamoDBMapper.scan(CondutorVeiculo.class, new DynamoDBScanExpression()
                        .withFilterExpression("condutor_CPF =: val"));
        return listaCondutores;
    }

    public void save(CondutorVeiculo condutorVeiculo) {
        dynamoDBMapper.save(condutorVeiculo);
    }

    public void deleteCondutor(PaginatedScanList<CondutorVeiculo> registrosCondutorVeiculo) {
        dynamoDBMapper.batchDelete(registrosCondutorVeiculo);
    }

    public PaginatedScanList<CondutorVeiculo> findByPlaca(String placa) {
        return dynamoDBMapper.scan(CondutorVeiculo.class, new DynamoDBScanExpression());
    }
}
