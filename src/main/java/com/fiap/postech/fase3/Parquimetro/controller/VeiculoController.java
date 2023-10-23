package com.fiap.postech.fase3.Parquimetro.controller;

import com.fiap.postech.fase3.Parquimetro.model.Estacionamento;
import com.fiap.postech.fase3.Parquimetro.model.Veiculo;
import com.fiap.postech.fase3.Parquimetro.repository.VeiculoRepository;
import com.fiap.postech.fase3.Parquimetro.service.EstacionamentoService;
import jakarta.validation.Valid;
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
@RequestMapping("/veiculo")
public class VeiculoController {
    @Autowired
    private VeiculoRepository veiculoRepository;
    @PostMapping("/{id}")
    public Veiculo save(@Valid @RequestBody Veiculo veiculo,
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
                                 @Valid @RequestBody Veiculo veiculo){
        return veiculoRepository.update(id, veiculo);
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
