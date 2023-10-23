package com.fiap.postech.fase3.Parquimetro.service;

import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import com.fiap.postech.fase3.Parquimetro.model.FormaPagamento;
import jakarta.validation.Path;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

@Service
public class FormaPagamentoService {


    @Autowired
    CondutorService condutorService;

    public String saveFormaPagamento(String condutor_CPF, FormaPagamento formaPagamento) throws Exception {
        Condutor condutorBusca = condutorService.getCondutorById(condutor_CPF);
        if (condutorBusca == null){
            throw new Exception("Condutor não encontrado!");
        }

        try {
            Condutor condutor = condutorService.getCondutorById(condutor_CPF);
            var formasPagamento = condutor.getFormaPagamentos();
            if (formasPagamento == null)
                formasPagamento = new ArrayList<>();
            else{
                formasPagamento = deletaFormaPagamento(condutor_CPF, formaPagamento);
            }
            formasPagamento.add(formaPagamento);
            condutor.setFormaPagamentos(formasPagamento);
            condutorService.update(condutor);
            return "Salvo com sucesso";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    public ArrayList<FormaPagamento> deletaFormaPagamento(String condutor_CPF, FormaPagamento formaPagamento) throws Exception {
        Condutor condutorBusca = condutorService.getCondutorById(condutor_CPF);
        if (condutorBusca == null){
            throw new Exception("Condutor não encontrado!");
        }
        try {

            Condutor condutor = condutorService.getCondutorById(condutor_CPF);

            ArrayList<FormaPagamento> formasPagamento = (ArrayList<FormaPagamento>) condutor.getFormaPagamentos();

            Iterator<FormaPagamento> i = formasPagamento.iterator();
            while (i.hasNext()) {
                FormaPagamento formaPagamentoBusca = i.next();
                if (formaPagamentoBusca.getNomeFormaPagamento()
                        .equals(formaPagamento.getNomeFormaPagamento())) {
                    i.remove();
                }
            }
            condutor.setFormaPagamentos(formasPagamento);
            condutorService.update(condutor);
            return formasPagamento;
        }catch(Exception e){
            throw e;
        }
    }
    public FormaPagamento getFormaPagamentoByNome(ArrayList<FormaPagamento> formasPagamento,
                                                  String nomeFormaPagamento){
        Iterator<FormaPagamento> i = formasPagamento.iterator();
        FormaPagamento formaPagamentoBusca = new FormaPagamento();
        while (i.hasNext()) {
            FormaPagamento formaPagamentoIt = i.next();
            if (formaPagamentoIt.getNomeFormaPagamento()
                    .equals(nomeFormaPagamento)) {
                formaPagamentoBusca = formaPagamentoIt;
            }
        }
        return formaPagamentoBusca;
    }
}
