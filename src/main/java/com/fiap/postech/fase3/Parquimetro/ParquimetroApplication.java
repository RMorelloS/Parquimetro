package com.fiap.postech.fase3.Parquimetro;

import com.fiap.postech.fase3.Parquimetro.service.NotificacaoService;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParquimetroApplication {

	private static NotificacaoService notificacaoService;
	public static void main(String[] args) {

		SpringApplication.run(ParquimetroApplication.class, args);

	}

}
