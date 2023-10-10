package com.fiap.postech.fase3.Parquimetro.controller;

import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import com.fiap.postech.fase3.Parquimetro.repository.CondutorRepository;
import com.fiap.postech.fase3.Parquimetro.service.CondutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/condutor")
public class CondutorController {
    @Autowired
    private CondutorService condutorService;
    @PostMapping
    public Condutor save(@RequestBody Condutor condutor){
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
    public String deleteCondutor(@PathVariable("id") String condutor_CPF){
        return condutorService.delete(condutor_CPF);
    }

    @PutMapping("/{id}")
    public String updateEmployee(@PathVariable("id") String condutor_CPF,
                                 @RequestBody Condutor condutor){
        return condutorService.update(condutor_CPF, condutor);
    }
}
