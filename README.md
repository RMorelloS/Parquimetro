# Pós Tech - Fase 3

Para a entrega do 3º tech challenge, foi desenvolvido um sistema de parquímetro, simulando um aplicativo de estacionamentos em uma cidade.

Foram desenvolvidas as seguintes funcionalidades:

1. Cadastro de condutores
2. Cadastro de formas de pagamento
3. Estacionamento/Retirada de veículos
4. Serviços de notificação via SQS

Foram utilizadas as seguintes tecnologias/técnicas:
 * **Java + Spring** - recebimento e processamento de requisições
 * **Banco de dados NoSQL DynamoDB** - armazenamento das informações de forma escalável via ambiente serverless
 * **Jakarta** - validação das informações de entrada (não aceitar campos nulos, vazios, ...)
 * **Postman** - geração das requisições e validação da API
 * **Docker** - definição de containers e virtualização
 * **SQS** - recebimento de mensagens para notificação ao usuário quando o período de estacionamento está prestes a vencer
 * **EC2 + Auto Scaling Group** - processamento das requisições (EC2) e escalabilidade via execução de outras instâncias quando as demais instâncias estão sobrecarregadas (auto scaling group)

Ressalta-se como principal desafio a implantação do pacote JAR no ambiente serverless, que foi resolvida via investigação na documentação da AWS.

A arquitetura AWS é apresentada no fluxo a seguir:

![Diagrama-Serviços-AWS](https://github.com/RMorelloS/Parquimetro/assets/32580031/cb1d2be2-6a4e-4b22-a78c-b11b37d2fb60)


# Acesso ao serviço:

## Ambiente Online

## Dockerfile

Para executar o projeto utilizando Docker:

   1. Executar o projeto via DockerHUB

   ```bash
      docker run -p 8080:8080 -d ricardoms98:parquimetro:0.0.2
   ```


```bash
curl --location 'localhost:8080/usuario' \
--header 'Content-Type: application/json' \
--data '{
    "loginUsuario": "ricardoms",
    "nome": "Ricardo Morello"
}'
```
