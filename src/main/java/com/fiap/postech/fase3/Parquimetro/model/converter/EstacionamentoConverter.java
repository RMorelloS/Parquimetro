package com.fiap.postech.fase3.Parquimetro.model.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fiap.postech.fase3.Parquimetro.model.Estacionamento;

import java.time.LocalDateTime;

// Converts the complex type Estacionamento to a string and vice-versa.
public class EstacionamentoConverter implements DynamoDBTypeConverter<String, Estacionamento> {

    @Override
    public String convert(Estacionamento object) {
        Estacionamento novoEstacionamento = (Estacionamento) object;
        String strEstacionamento = null;
        try {
            if (novoEstacionamento != null) {
                strEstacionamento = String.format("%s x %s x %s x %s x %s", novoEstacionamento.getCondutor_CPF(), novoEstacionamento.getPlaca(),
                        novoEstacionamento.isFlagTempoFixoHora(), novoEstacionamento.getDuracaoEstacionamento(), novoEstacionamento.getInicioEstacionamento());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return strEstacionamento;
    }

    @Override
    public Estacionamento unconvert(String s) {

        Estacionamento novoEstacionamento = new Estacionamento();
        try {
            if (s != null && s.length() != 0) {
                String[] data = s.split("x");
                novoEstacionamento.setCondutor_CPF(data[0].trim());
                novoEstacionamento.setPlaca(data[1].trim());
                novoEstacionamento.setFlagTempoFixoHora(Boolean.parseBoolean(data[2].trim()));
                novoEstacionamento.setDuracaoEstacionamento(Integer.parseInt(data[3].trim()));
                novoEstacionamento.setInicioEstacionamento(LocalDateTime.parse(data[4].trim()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return novoEstacionamento;
    }
}