package com.fiap.postech.fase3.Parquimetro.service;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import com.fiap.postech.fase3.Parquimetro.repository.CondutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CondutorService {

    @Autowired
    public CondutorRepository condutorRepository;


    public Condutor save(Condutor condutor) {
        try{
            return condutorRepository.save(condutor);
        }catch(Exception e){
            throw e;
        }
    }

    public Condutor getCondutorById(String condutorCpf) {

        return condutorRepository.getCondutorById(condutorCpf);
    }

    public PaginatedScanList<Condutor> getCondutorByTempoEstacionamento(){
        return condutorRepository.getCondutorByTempoEstacionamento();
    }

    public List<Condutor> getCondutores() {
        return condutorRepository.getCondutores();
    }

    public String delete(String condutorCpf) {

        return condutorRepository.delete(condutorCpf);
    }

    public String update(Condutor condutor) {
        return condutorRepository.update(condutor);
    }

    public Condutor verificaCondutor(String condutorCpf) throws DataIntegrityViolationException {
        Condutor condutorBusca = getCondutorById(condutorCpf);
        if(condutorBusca==null){
            throw new DataIntegrityViolationException("Condutor " + condutorCpf + " não existe. Realize o cadastro do condutor e tente novamente");
        }else{
            return condutorBusca;
        }
    }
}
