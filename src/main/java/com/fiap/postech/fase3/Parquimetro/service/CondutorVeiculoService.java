package com.fiap.postech.fase3.Parquimetro.service;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import com.fiap.postech.fase3.Parquimetro.model.CondutorVeiculo;
import com.fiap.postech.fase3.Parquimetro.model.Veiculo;
import com.fiap.postech.fase3.Parquimetro.repository.CondutorVeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CondutorVeiculoService {
    @Autowired
    public CondutorVeiculoRepository condutorVeiculoRepository;

    @Autowired
    public VeiculoService veiculoService;

    public void saveCondutorVeiculo(Veiculo veiculo, Condutor condutorBusca) {
        CondutorVeiculo condutorVeiculo = new CondutorVeiculo(veiculo.getPlaca(), condutorBusca.getCondutor_CPF());
        condutorVeiculoRepository.save(condutorVeiculo);
    }

    public void deleteCondutor(String condutorCpf) {
        PaginatedScanList<CondutorVeiculo> registrosCondutorVeiculo = condutorVeiculoRepository.findByCpf(condutorCpf);
        condutorVeiculoRepository.deleteCondutor(registrosCondutorVeiculo);

        for(CondutorVeiculo condutorVeiculo :  registrosCondutorVeiculo){
            PaginatedScanList<CondutorVeiculo> condutorVeiculoBusca = condutorVeiculoRepository.findByPlaca(condutorVeiculo.getPlaca());
            if(condutorVeiculoBusca == null){
                veiculoService.delete(condutorVeiculo.getPlaca());
            }
        }

    }
}
