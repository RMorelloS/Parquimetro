package com.fiap.postech.fase3.Parquimetro.service;

import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import com.fiap.postech.fase3.Parquimetro.model.Veiculo;
import com.fiap.postech.fase3.Parquimetro.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    public VeiculoRepository veiculoRepository;

    public List<Veiculo> getVeiculos() {
        return veiculoRepository.getVeiculos();
    }

    public String delete(String id) {
        return veiculoRepository.delete(id);
    }

    public String update(String id, Veiculo veiculo) {
        return veiculoRepository.update(id, veiculo);
    }
}
