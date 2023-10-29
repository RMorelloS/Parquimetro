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

# 1. Cadastro de condutores

## 1.1 Cadastrar novo condutor

Para cadastrar um novo condutor, deve-se realizar uma requisição do tipo POST, passando informações como CPF, nome, rua e outras 4 flags, descritas abaixo:

1. Duração estacionamento: quantidade de horas que o condutor estará estacionado
2. Flag tempo fixo/hora: flag que indica se o condutor estacionará por um tempo fixo ou variável
3. Início do estacionamento: Data/hora de início do estacionamento
4. Status do estacionamento: se o condutor possui algum veículo estacionado

As flags descritas acima são inicialmente configuradas como null ou 0, sendo preenchidas conforme o condutor estacione um veículo.

```bash
curl --location '3.142.36.237:8080/condutor' \
--header 'Content-Type: application/json' \
--data '{
    "condutor_CPF": "92631817052",
    "nome": "Usuário 1",
    "rua": "Rua 1",
    "endereco_numero": 20,
    "cidade": "Cidade 1",
    "bairro": "Bairro 1",
    "estado": "Estado 1",
    "celular": "912345678",
    "veiculos": [
        {
            "placa": "CCC5678",
            "modelo": "Fiat Marea",
            "ano": "1998"
        }
    ],
    "duracaoEstacionamento": null,
    "flagTempoFixoHora": 0,
    "inicioEstacionamento": null,
    "statusEstacionamento": 0 
}'
```

Caso a informação seja válida, o condutor será criado e as informações serão retornadas em uma resposta 200 - OK:

![image](https://github.com/RMorelloS/Parquimetro/assets/32580031/c2fb5da6-40e5-4621-9462-a716bc7e964f)

Caso seja inserida alguma informação inválida, a validação retornará um erro:

![image](https://github.com/RMorelloS/Parquimetro/assets/32580031/1e3905fe-9e78-47be-ad42-9570d72b9d11)

## 1.2 Ler condutor por CPF

Para leitura de um condutor, deve-se utilizar uma requisição do tipo GET, passando-se o CPF como argumento:

```bash
curl --location '3.142.36.237:8080/condutor/92631817052'
```

Caso o CPF seja válido e esteja cadastrado, retorna as informações do condutor:

![image](https://github.com/RMorelloS/Parquimetro/assets/32580031/8504f190-4ac3-4b6c-8ef0-d14a6c967cba)


## 1.3 Ler todos os condutores

Para leitura de todos os condutores, realizar uma requisição do tipo GET:

```bash
curl --location '3.142.36.237:8080/condutor'
```

A requisição retornará todos os condutores cadastrados.

## 1.4 Atualizar um condutor

Para atualizar um condutor, realizar uma requisição do tipo PUT, passando as informações atualizadas do condutor:

```bash
curl --location --request PUT '3.142.36.237:8080/condutor/92631817052' \
--header 'Content-Type: application/json' \
--data '{
    "condutor_CPF": "92631817052",
    "nome": "Usuário 1 - Atualizado",
    "rua": "Rua 1",
    "endereco_numero": 20,
    "cidade": "Cidade 1",
    "bairro": "Bairro 1",
    "estado": "Estado 1",
    "celular": "912345678",
    "veiculos": [
        {
            "placa": "CCC5678",
            "modelo": "Fiat Marea",
            "ano": "1998"
        },

         {
            "placa": "ABC1234",
            "modelo": "Renault Kwid",
            "ano": "2015"
        }

    ],
    "duracaoEstacionamento": null,
    "flagTempoFixoHora": 0,
    "inicioEstacionamento": null,
    "statusEstacionamento": 0
   
}'
```

## 1.5 Excluir um condutor

Para excluir um condutor, realizar uma requisição do tipo DELETE, passando o CPF como argumento:

```bash
curl --location --request DELETE 'localhost:8080/condutor/92631817052'
```


# 2. Cadastro de formas de pagamento

## 2.1 Cadastrar nova forma de pagamento

Para cadastro de uma forma de pagamento, realizar uma requisição do tipo POST, passando informações como apelido do pagamento, número do cartão, CVV, ...:

Importante: caso seja um pagamento via cartão de crédito/débito, as informações referentes ao cartão devem ser preenchidas. Caso seja via PIX, somente a informação de chave pix deverá ser preenchida:

### 2.1.1 Pagamento via cartão de crédito

```bash
curl --location '3.142.36.237:8080/formaPagamento/92631817052' \
--header 'Content-Type: application/json' \
--data '{
 "nomeFormaPagamento": "nome-1",
 "numero_cartao": "12345678",
 "flag_credito_debito": 1,
 "data_validade": "2030-10-29T23:36:58.105903983",
 "cvv": "231",
 "chave_PIX": null
}'
```

### 2.1.2 Pagamento via PIX

```bash
curl --location '3.142.36.237:8080/formaPagamento/92631817052' \
--header 'Content-Type: application/json' \
--data '{
 "nomeFormaPagamento": "nome-2",
 "numero_cartao": null,
 "flag_credito_debito": null,
 "data_validade": null,
 "cvv": null,
 "chave_PIX": "40247194832"
}'
```

## 2.2 Excluir forma de pagamento


# 3. Estacionamento/Retirada de veículos

## 3.1 Estacionar um veículo

## 2.2 Retirar um veículo


# 4. Serviços de notificação via SQS

## 4.1 Horário Fixo

## 4.2 Horário variável

