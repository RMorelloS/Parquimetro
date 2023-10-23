package com.fiap.postech.fase3.Parquimetro.controller;

import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import com.fiap.postech.fase3.Parquimetro.service.CondutorService;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/condutor")
public class CondutorController {
    @Autowired
    private CondutorService condutorService;

    @PostMapping
    public Condutor save( @Valid @RequestBody Condutor condutor){
        return condutorService.save(condutor);
    }
    @GetMapping("/{id}")
    public Condutor getCondutor(@PathVariable("id") String condutor_CPF){
        return condutorService.getCondutorById(condutor_CPF);
    }

    @GetMapping
    public List<Condutor> getCondutor(){
        return condutorService.getCondutores();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCondutor(@PathVariable("id") String condutor_CPF){

        try {
            String retorno = condutorService.delete(condutor_CPF);
            return new ResponseEntity(retorno, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public String updateEmployee( @RequestBody Condutor condutor){

        return condutorService.update(condutor);
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
