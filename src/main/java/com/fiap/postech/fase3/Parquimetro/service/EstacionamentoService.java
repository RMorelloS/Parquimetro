package com.fiap.postech.fase3.Parquimetro.service;

import com.fiap.postech.fase3.Parquimetro.model.*;
import com.fiap.postech.fase3.Parquimetro.repository.CondutorRepository;
import jakarta.validation.Path;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

@Service
public class EstacionamentoService {
    @Autowired
    public CondutorRepository condutorRepository;

    @Autowired
    public FormaPagamentoService formaPagamentoService;

    @Autowired
    private CondutorService condutorService;

    @Autowired
    private VeiculoService veiculoService;

    //TO-DO: validação tepofixohora/tempo desejado
    //validação condutor_CPF existente
    public void estacionarVeiculo(Estacionamento estacionamento) throws Exception {


        Condutor condutorBusca = condutorService.getCondutorById(estacionamento.getCondutor_CPF());

        if (condutorBusca == null){
            throw new Exception("Condutor não encontrado!");
        }
        if (condutorBusca.isStatusEstacionamento()){
            throw new Exception("Estacionamento em aberto para o condutor. " +
                    "Favor solicitar a retirada do veículo antes de estacionar outro " +
                    "veículo");
        }
        veiculoService.validaVeiculosCondutor(condutorBusca.getVeiculos(), estacionamento.getPlaca());
        try {
            String cpfBusca = estacionamento.getCondutor_CPF();
            Condutor condutor = condutorService.getCondutorById(cpfBusca);
            condutor.setDuracaoEstacionamento(estacionamento.getDuracaoEstacionamento());
            condutor.setFlagTempoFixoHora(estacionamento.isFlagTempoFixoHora());
            condutor.setInicioEstacionamento(LocalDateTime.now());
            condutor.setStatusEstacionamento(true);
            condutor.setPlacaVeiculoEstacionado(estacionamento.getPlaca());
            condutorService.update(condutor);
        }catch(Exception e){
            throw e;
        }
    }

    public Recibo retirarVeiculo(String condutor_CPF, String placa, String nomeFormaPagamento) throws Exception {
        Condutor condutorBusca = condutorService.getCondutorById(condutor_CPF);
        if (condutorBusca == null){
            throw new Exception("Condutor não encontrado!");
        }
        if (!condutorBusca.isStatusEstacionamento()){
            throw new Exception("Não constam veículos estacioandos para o condutor. " +
                    "Favor estacionar um veículo antes de solicitar a retirada");
        }
        veiculoService.validaVeiculosCondutor(condutorBusca.getVeiculos(), placa);
        try {
            Condutor condutor = condutorService.getCondutorById(condutor_CPF);

            long horasCobranca = 0;
            horasCobranca = (long) Math.ceil(Double.valueOf(condutor.getDuracaoEstacionamento()));
            LocalDateTime horaFim = condutor.getInicioEstacionamento().plusHours(horasCobranca);

            long valorPagamento = horasCobranca * 10;
            Recibo recibo = emitirRecibo(condutor, valorPagamento, nomeFormaPagamento, horaFim);
            condutor.setStatusEstacionamento(false);
            condutorService.update(condutor);
            return recibo;
        }catch(Exception e){
            throw e;
        }
    }

    private Recibo emitirRecibo(Condutor condutor,
                                long valorPagamento,
                                String nomeFormaPagamento,
                                LocalDateTime horaFim) {

        FormaPagamento formaPagamentoRecibo = formaPagamentoService.getFormaPagamentoByNome(
                (ArrayList<FormaPagamento>) condutor.getFormaPagamentos(),
                nomeFormaPagamento);

        Veiculo veiculoRecibo = veiculoService.getVeiculoByPlaca(
                (ArrayList<Veiculo>) condutor.getVeiculos(),
                condutor.getPlacaVeiculoEstacionado()
        );

        Recibo recibo = new Recibo(condutor.getCondutor_CPF(),
                veiculoRecibo,
                condutor.getInicioEstacionamento(),
                horaFim,
                formaPagamentoRecibo,
                valorPagamento);

        return recibo;

    }


}
