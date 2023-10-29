package com.fiap.postech.fase3.Parquimetro.service;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.fiap.postech.fase3.Parquimetro.model.Condutor;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.core.env.Environment;
import org.springframework.messaging.support.MessageBuilder;
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

    @Autowired
    private Environment env;
    private String envSQS = "https://sqs.us-east-2.amazonaws.com/280054123680/NotificacaoCondutores.fifo";
    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;
    @PostConstruct
    public void notificarUsuarios(){

        TimerTask task = new TimerTask() {
            public void run() {
                PaginatedScanList<Condutor> condutoresAtivos = condutorService.getCondutorByTempoEstacionamento();
                System.out.print("entrei");
                for (Condutor condutor:condutoresAtivos) {
                    LocalDateTime horarioAtual = LocalDateTime.now();
                    System.out.print("horarioAtual" + horarioAtual);
                    System.out.print("horario condutor" + condutor.getInicioEstacionamento());
                    double qtdeHoras = Math.ceil(ChronoUnit.MINUTES.between(condutor.getInicioEstacionamento(), horarioAtual));
                    queueMessagingTemplate.send(envSQS,//env.getProperty("aws-sqs-endpoint"),
                            MessageBuilder.withPayload(horarioAtual + ": Prezado condutor " + qtdeHoras + " " + condutor.getCondutor_CPF())
                                    .setHeader("message-group-id", "groupName")
                                    .build()
                    );
                    if(qtdeHoras <= 24) {

                        if (condutor.isFlagTempoFixoHora()) {
                            if (qtdeHoras - condutor.getDuracaoEstacionamento() >= 0) {
                                queueMessagingTemplate.send(envSQS,//env.getProperty("aws-sqs-endpoint"),
                                        MessageBuilder.withPayload(horarioAtual + ": Prezado condutor " + condutor.getCondutor_CPF() +
                                                        ", tempo fixo delimitado para o estacionamento " +
                                                        "está encerrando. Favor iniciar outro período ou encerrar o" +
                                                        " estacionamento. Caso o horário permitido seja ultrapassado, " +
                                                        "uma nova hora será acrescida.")
                                                .setHeader("message-group-id", "groupName")
                                                .build()
                                );
                                condutor.setDuracaoEstacionamento((int) qtdeHoras);
                                condutorService.update(condutor);
                            }
                        } else if (!condutor.isFlagTempoFixoHora() && qtdeHoras > condutor.getDuracaoEstacionamento()) {

                            queueMessagingTemplate.send(envSQS,//env.getProperty("aws-sqs-endpoint"),
                                    MessageBuilder.withPayload(horarioAtual + ": O tempo variável delimitado para o " +
                                                    "estacionamento do condutor " + condutor.getCondutor_CPF() +
                                                    " encerrou. Será acrescida uma nova hora. Total: " + qtdeHoras)
                                            .setHeader("message-group-id", "groupName")
                                            .build()
                            );
                            condutor.setDuracaoEstacionamento((int) qtdeHoras);
                            condutorService.update(condutor);
                        }
                    }
                }


            }
        };
        Timer timer = new Timer("Timer");
        long delay = 1000L;
        timer.scheduleAtFixedRate(task, delay, 60000);

    }
}
