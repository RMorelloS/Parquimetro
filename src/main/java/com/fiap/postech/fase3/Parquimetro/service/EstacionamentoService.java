package com.fiap.postech.fase3.Parquimetro.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import com.fiap.postech.fase3.Parquimetro.model.Estacionamento;
import com.fiap.postech.fase3.Parquimetro.repository.CondutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EstacionamentoService {
    @Autowired
    public CondutorRepository condutorRepository;

    @Autowired
    public ReciboService reciboService;
    @Autowired
    public CondutorService condutorService;

    //TO-DO: validação tepofixohora/tempo desejado
    //validação condutor_CPF existente
    public void estacionarVeiculo(Estacionamento estacionamento){
        try {
            String cpfBusca = estacionamento.getCondutor_CPF();
            Condutor condutor = condutorService.getCondutorById(cpfBusca);
            condutor.setDuracaoEstacionamento(estacionamento.getDuracaoEstacionamento());
            condutor.setFlagTempoFixoHora(estacionamento.isFlagTempoFixoHora());
            condutor.setInicioEstacionamento(LocalDateTime.now());
            condutor.setStatusEstacionamento(true);
            condutorService.update(condutor);
        }catch(Exception e){
            throw e;
        }
    }

    public void retirarVeiculo(String condutor_CPF, String placa){
        Condutor condutor = condutorService.getCondutorById(condutor_CPF);

        Double horasCobranca = 0.0;
        horasCobranca = Double.valueOf(condutor.getDuracaoEstacionamento());

        Double valorCobranca = horasCobranca * 10;
        reciboService.emitirRecibo();
        condutor.setStatusEstacionamento(false);
        condutorService.update(condutor);
    }



}
