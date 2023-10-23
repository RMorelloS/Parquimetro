package com.fiap.postech.fase3.Parquimetro.controller;

import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import com.fiap.postech.fase3.Parquimetro.model.FormaPagamento;
import com.fiap.postech.fase3.Parquimetro.service.CondutorService;
import com.fiap.postech.fase3.Parquimetro.service.FormaPagamentoService;
import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/formaPagamento")
public class FormaPagamentoController {

    @Autowired
    FormaPagamentoService formaPagamentoService;
    @PostMapping("/{condutor_CPF}")
    public ResponseEntity saveFormaPagamento(@PathVariable String condutor_CPF,
                                     @Valid @RequestBody FormaPagamento formaPagamento){
        try {
            String retorno = formaPagamentoService.saveFormaPagamento(condutor_CPF, formaPagamento);
            return new ResponseEntity(retorno, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{condutor_CPF}")
    public ResponseEntity deleteFormaPagamento(@PathVariable String condutor_CPF,
                                                         @Valid @RequestBody FormaPagamento formaPagamento) {
        try {
            var retorno = formaPagamentoService.deletaFormaPagamento(condutor_CPF, formaPagamento);
            return new ResponseEntity(retorno, HttpStatus.OK);
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
