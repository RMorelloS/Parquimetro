package com.fiap.postech.fase3.Parquimetro.controller;

import com.fiap.postech.fase3.Parquimetro.model.Estacionamento;
import com.fiap.postech.fase3.Parquimetro.model.Recibo;
import com.fiap.postech.fase3.Parquimetro.service.EstacionamentoService;
import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/estacionar")
public class EstacionamentoController {

    @Autowired
    private EstacionamentoService estacionamentoService;
    @PostMapping("/iniciar")
    public ResponseEntity estacionarVeiculo(@Valid @RequestBody Estacionamento estacionamento){
        try {
            this.estacionamentoService.estacionarVeiculo(estacionamento);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("estacionado com sucesso");
    }

    @PostMapping("/retirar/{condutor_CPF}/{placa}/{nomeFormaPagamento}")
    public ResponseEntity retirarVeiculo(@PathVariable String condutor_CPF,
                                         @PathVariable String placa,
                                         @PathVariable String nomeFormaPagamento){
        try{
            Recibo recibo = this.estacionamentoService.retirarVeiculo(condutor_CPF, placa, nomeFormaPagamento);
            return ResponseEntity.ok(recibo);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity validationError(MethodArgumentNotValidException ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("motivo", ex.getBindingResult().getFieldError().getDefaultMessage());
        body.put("campo", ex.getBindingResult().getFieldError().getField());
        body.put("valor", ex.getBindingResult().getFieldError().getRejectedValue());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}