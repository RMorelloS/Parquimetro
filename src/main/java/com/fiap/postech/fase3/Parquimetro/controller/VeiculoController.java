package com.fiap.postech.fase3.Parquimetro.controller;

import com.fiap.postech.fase3.Parquimetro.model.Veiculo;
import com.fiap.postech.fase3.Parquimetro.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculo")
public class VeiculoController {
    @Autowired
    private VeiculoRepository veiculoRepository;
    @PostMapping("/{id}")
    public Veiculo save(@RequestBody Veiculo veiculo,
                        @PathVariable("id") String condutor_CPF){
        return veiculoRepository.save(veiculo, condutor_CPF);
    }
    @GetMapping("/{id}")
    public Veiculo getVeiculo(@PathVariable("id") String id){
        return veiculoRepository.getVeiculoById(id);
    }

    @GetMapping
    public List<Veiculo> getVeiculo(){
        return veiculoRepository.getVeiculos();
    }

    @DeleteMapping("/{id}")
    public String deleteVeiculo(@PathVariable("id") String id){
        return veiculoRepository.delete(id);
    }

    @PutMapping("/{id}")
    public String updateEmployee(@PathVariable("id") String id,
                                 @RequestBody Veiculo veiculo){
        return veiculoRepository.update(id, veiculo);
    }
}
