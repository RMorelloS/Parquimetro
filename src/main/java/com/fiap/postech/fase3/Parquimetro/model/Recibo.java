package com.fiap.postech.fase3.Parquimetro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recibo {
    private String condutor_CPF;
    private Veiculo veiculo;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private FormaPagamento formaPagamento;
    private long valorPagamento;
}
