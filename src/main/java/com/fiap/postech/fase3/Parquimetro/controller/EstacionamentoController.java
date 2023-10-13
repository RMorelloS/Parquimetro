package com.fiap.postech.fase3.Parquimetro.controller;

import com.fiap.postech.fase3.Parquimetro.model.Estacionamento;
import com.fiap.postech.fase3.Parquimetro.service.EstacionamentoService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estacionar")
public class EstacionamentoController {

    @Autowired
    private EstacionamentoService estacionamentoService;
    @PostMapping("/iniciar")
    public ResponseEntity estacionarVeiculo(@RequestBody Estacionamento estacionamento){
        this.estacionamentoService.estacionarVeiculo(estacionamento);
        return ResponseEntity.ok("estacionado com sucesso");
    }

    @PostMapping("/retirar/{condutor_CPF}/{placa}")
    public ResponseEntity retirarVeiculo(@PathVariable String condutor_CPF,
                                         @PathVariable String placa){
        this.estacionamentoService.retirarVeiculo(condutor_CPF, placa);
        return ResponseEntity.ok("retirado com sucesso");
    }
}