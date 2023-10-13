package com.fiap.postech.fase3.Parquimetro.service;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class NotificacaoService {
    @Autowired
    public CondutorService condutorService;
    @PostConstruct
    public void notificarUsuarios(){
        TimerTask task = new TimerTask() {
            public void run() {
                PaginatedScanList<Condutor> condutoresAtivos = condutorService.getCondutorByTempoEstacionamento();
                for (Condutor condutor:condutoresAtivos) {
                    LocalDateTime horarioAtual = LocalDateTime.now();
                    double qtdeHoras = Math.ceil(ChronoUnit.HOURS.between(condutor.getInicioEstacionamento(), horarioAtual));

                    if (condutor.isFlagTempoFixoHora()){
                        if (qtdeHoras - condutor.getDuracaoEstacionamento() == 0){

                        }
                    }else if(!condutor.isFlagTempoFixoHora() && qtdeHoras > condutor.getDuracaoEstacionamento()){
                        if(qtdeHoras - condutor.getDuracaoEstacionamento() < 0){

                        }
                    }
                    condutor.setDuracaoEstacionamento((int) qtdeHoras);
                    condutorService.update(condutor);
                }

            }
        };
        Timer timer = new Timer("Timer");
        long delay = 1000L;
        timer.scheduleAtFixedRate(task, delay, 10000);

    }
}
