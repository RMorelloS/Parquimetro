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
 * **EC2 + Auto Scaling Group** - processamento das requisições (EC2) e escalabilidade via execução de outras instâncias quando as demais instâncias estão sobrecarregadas (auto scaling group). Todas as instâncias que venham a ser inicializadas são carregadas com a pré-configuração do docker e da execução do container disponível no Docker HUB, utilizando a opção _user data_ do EC2.

Ressalta-se como principal desafio a implantação do pacote JAR no ambiente serverless, que foi resolvida via investigação da documentação da AWS.

A arquitetura AWS é apresentada no fluxo a seguir:

![Diagrama-Serviços-AWS](https://github.com/RMorelloS/Parquimetro/assets/32580031/63e9351e-ec50-4d63-a8f5-a58c09adb074)


# Acesso ao serviço:

## Ambiente Online

Acesso via endpoint HTTP em instância EC2, disponível pelo [IP da instância](http://3.141.103.105:8080). Vale ressaltar que o endpoint principal não possui controller associado, sendo necessário buscar um endpoint especifico, como:

```bash
http://3.141.103.105:8080/condutor
```

## Dockerfile

Para executar o projeto utilizando Docker:

   1. Executar o projeto via DockerHUB

   ```bash
      docker run -p 8080:8080 -d -e ACCESSKEY='AKIAUCNEGZSQBEV4MFQA' -e SECRETKEY='Gli2/1YlhEN3gmwsZ9U1
rSDsD6J1LjxRCUqu69fB' -e DYNAMODB='dynamodb.us-east-2.amazonaws.com' -e AWSREGION='us-east-2' -e AWSSQSENDPOINT='https://sqs.us-east-2.amazonaws.com/280
054123680/NotificacaoCondutores.fifo' ricardoms98/parquimetro:0.0.3
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
curl --location '3.141.103.105:8080/condutor' \
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

![image](https://github.com/RMorelloS/Parquimetro/assets/32580031/79d06fc4-310e-42d2-8c5e-0f16ea94e757)


Caso seja inserida alguma informação inválida, a validação retornará um erro:

![image](https://github.com/RMorelloS/Parquimetro/assets/32580031/555b7baa-7713-416a-94aa-a8aab2d22b4b)


## 1.2 Ler condutor por CPF

Para leitura de um condutor, deve-se utilizar uma requisição do tipo GET, passando-se o CPF como argumento:

```bash
curl --location '3.141.103.105:8080/condutor/92631817052'
```

Caso o CPF seja válido e esteja cadastrado, retorna as informações do condutor:

![image](https://github.com/RMorelloS/Parquimetro/assets/32580031/a832dee7-398d-48d5-8648-afc47150a08d)

## 1.3 Ler todos os condutores

Para leitura de todos os condutores, realizar uma requisição do tipo GET:

```bash
curl --location '3.141.103.105:8080/condutor'
```

A requisição retornará todos os condutores cadastrados.

## 1.4 Atualizar um condutor

Para atualizar um condutor, realizar uma requisição do tipo PUT, passando as informações atualizadas do condutor:

```bash
curl --location --request PUT '3.141.103.105:8080/condutor/92631817052' \
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
curl --location '3.141.103.105:8080/formaPagamento/92631817052' \
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
curl --location '3.141.103.105:8080/formaPagamento/92631817052' \
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

Para excluir uma forma de pagamento, realizar uma requisição do tipo DELETE, passando informações como CPF do condutor e apelido da forma de pagamento:

```bash

curl --location --request DELETE '3.141.103.105:8080/formaPagamento/92631817052' \
--header 'Content-Type: application/json' \
--data '{
   "nomeFormaPagamento": "nome-1"
}'

```

# 3. Estacionamento/Retirada de veículos

## 3.1 Estacionar um veículo
Para estacionar um veículo, realizar uma requisição do tipo POST para o serviço de estacionamentos, passando informações como CPF do condutor, placa, duração prevista e se o veículo permanecerá por tempo fixo ou variável:
```bash

curl --location '3.141.103.105:8080/estacionar/iniciar' \
--header 'Content-Type: application/json' \
--data '{
    "condutor_CPF": "92631817052",
    "placa": "ABC1234",
    "flagTempoFixoHora": 1,
    "duracaoEstacionamento": 5
}'

```

Retornará uma mensagem de sucesso, caso tenha sido possível registrar o estacionamento do veículo:

![image](https://github.com/RMorelloS/Parquimetro/assets/32580031/fbdfc4c8-303a-454f-958e-a4816b752a74)

Caso o condutor realize um novo estacionamento já tendo um veículo estacionado, o sistema rejeitará a solicitação:

![image](https://github.com/RMorelloS/Parquimetro/assets/32580031/afde3844-575e-4636-b4c5-22eb30eeb327)

Caso o condutor realize um estacionamento de um veículo que não está registrado para seu CPF, o sistema rejeitará a solicitação:

![image](https://github.com/RMorelloS/Parquimetro/assets/32580031/cfc5ddf1-a5d7-4763-8ee1-ca0fe133cff4)



## 3.2 Retirar um veículo

Para retirar um veículo, realizar uma requisição do tipo POST, passando informações como CPF do condutor, placa do veículo e apelido da forma de pagamento selecionada:

```bash
curl --location '3.141.103.105:8080/estacionar/retirar/92631817052/CCC5678/nome-2' \
--header 'Content-Type: application/json' \
--data '{
    "condutor_CPF": "402471938651-fixo",
    "placa": "CCC5678",
    "flagTempoFixoHora": 1,
    "duracaoEstacionamento": 1
}'
```

Retornará uma mensagem em formato JSON, contendo o recibo do estacionamento realizado, com as seguintes informações:

1. CPF do condutor
2. Informações do veículo
3. Horário de início
4. Horário de fim
5. Forma de pagamento
6. Valor total

```json
{
    "condutor_CPF": "92631817052",
    "veiculo": {
        "placa": "CCC5678",
        "modelo": "Fiat Marea",
        "ano": "1998"
    },
    "horaInicio": "2023-11-01T02:57:23.535308043",
    "horaFim": "2023-11-01T07:57:23.535308043",
    "formaPagamento": {
        "nomeFormaPagamento": "nome-2",
        "numero_cartao": null,
        "flag_credito_debito": false,
        "data_validade": null,
        "cvv": null,
        "chave_PIX": "40247194832"
    },
    "valorPagamento": 50
}
```

# 4. Serviços de notificação via SQS

Como serviço de notificação do usuário, realizou-se uma interface com o SQS da AWS. O sistema buscará, de forma assíncrona, todos os condutores com estacionamento ativo na base. 

Após levantar os condutores com estacionamento ativo, o serviço de notificação avalia se o condutor está com o período definido prestes a vencer.

Caso esteja próximo do vencimento, o serviço subirá uma notificação no SQS e aumentará uma hora no tempo total do veículo.

Para fins de simulação, a busca de condutores ativos no DynamoDB é realizada a cada 30 minutos. Também, definiu-se o tempo limite para estacionamento para 5 horas. Por fim, definiu-se a tarifa de R$ 10 por hora estacionada.

## 4.1 Horário Fixo

Caso o condutor tenha optado por realizar um estacionamento na modalidade tempo fixo, o sistema carregará uma mensagem no SQS, notificando-o de que o tempo fixo delimitado está próximo do encerramento. Caso o condutor exceda o tempo definido inicialmente, uma nova hora é computada em seu tempo total. 

## 4.2 Horário variável

Caso o condutor tenha optado por realizar um estacionamento na modalidade tempo variável, o sistema carregará uma mensagem no SQS, notificando-o de que uma nova hora será acrescentada ao tempo total do veiculo.
