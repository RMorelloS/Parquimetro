package com.fiap.postech.fase3.Parquimetro.service;

import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import com.fiap.postech.fase3.Parquimetro.model.FormaPagamento;
import com.fiap.postech.fase3.Parquimetro.model.Veiculo;
import com.fiap.postech.fase3.Parquimetro.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    public VeiculoRepository veiculoRepository;

    public Veiculo getVeiculoByPlaca(ArrayList<Veiculo> veiculos, String placaVeiculoEstacionado) {
        Iterator<Veiculo> i = veiculos.iterator();
        Veiculo veiculoBusca = new Veiculo();
        while (i.hasNext()) {
            Veiculo veiculoIt = i.next();
            if (veiculoIt.getPlaca()
                    .equals(placaVeiculoEstacionado)) {
                veiculoBusca = veiculoIt;
            }
        }
        return veiculoBusca;
    }

    public void validaVeiculosCondutor(List<Veiculo> veiculos, String placa) throws Exception {
        boolean veiculoIdentificadoNaLista = false;
        for (Veiculo veiculo: veiculos){
            if (veiculo.getPlaca().equals(placa)){
                veiculoIdentificadoNaLista = true;
            }
        }
        if (!veiculoIdentificadoNaLista){
            throw new Exception("Veiculo não consta como registrado para o condutor!");
        }
    }
}
