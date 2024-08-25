## Conteúdos

- [Sobre](#sobre)
- [Pré-requisitos](#pre-requisitos)
- [Como usar](#como-usar)
- [Tecnologias](#tecnologias)

</br></br>

## Sobre <a id="sobre"></a>

> Este projeto faz parte de um teste para o processo seletivo do <code>Caju Benefícios</code>.
> O projeto está dividido em dois módulos:
>
> - <code>**_authorization-server_**</code>
> - <code>**_caju-transaction_**</code>

</br></br>

## Pré-requisitos <a id="pre-requisitos"></a>

> - Mysql 8
> - JDK 17
> - Maven 3.9.6

</br></br>

## Como Usar <a id="como-usar"></a>

> Edite o arquivo <code>**_application.yml_**</code> dos módulos <code>**_authorization-server_**</code> e <code>**_caju-transaction_**</code> e informe as seguintes variáveis de ambiente:
>
> - <code>**_${DATASOURCE_USERNAME:usuario_banco}_**</code>
> - <code>**_${DATASOURCE_PASSWORD:senha_banco}_**</code>
>
> Execute o módulo <code>**_authorization-server_**</code> que rodará na porta 9000 (<code>**_http://localhost:9000/api/v1_**</code>). Este módulo é responsável por gerar um token de autenticação.

```sh
Auth Type: Basic Auth
- Username: authorization_code
- Password: 17686e8b-34d2-11ee-8422-581122c7752d

curl

--location 'http://localhost:9100/api/v1/oauth2/token'
--header 'Content-Type: application/x-www-form-urlencoded'
--header 'Authorization: ••••••'
--data-urlencode 'grant_type=custom_password'
--data-urlencode 'card_number=5588623798347322'
--data-urlencode 'password=123456'
```

> Execute o módulo <code>**_caju-transaction_**</code> que rodará na porta 9100 (<code>**_http://localhost:9100/api/v1_**</code>). Este módulo é responsável pelas transações.

```sh
curl

--location 'http://localhost:9100/api/v1/transactions'
--header 'Authorization: Bearer eyJ4NXQjUzI1NiI6ImI1Sl9...'
--header 'Content-Type: application/json'
--data '{
"totalAmount": 10.0,
"mcc": "5411",
"merchant": "MS RESTAURANTE - SAO PAULO BR",
"idEstablishment": "e003f62a-5e8c-11ef-9650-581122c7752d"
}'
```

</br></br>

## Tecnologias <a id="tecnologias"></a>

![Java](https://img.shields.io/static/v1?label=Java&message=17&color=green)
![Spring Boot](https://img.shields.io/static/v1?label=spring-boot&message=3.2.2&color=green)
![MySql](https://img.shields.io/static/v1?label=mysql&message=8.3.0&color=green)
![Lombok](https://img.shields.io/static/v1?label=lombok&message=1.18.30&color=green)
![ModelMapper](https://img.shields.io/static/v1?label=model-mapper&message=3.1.0&color=green)
![Commons](https://img.shields.io/static/v1?label=commons-lang3&message=3.13.0&color=green)
![Flyway](https://img.shields.io/static/v1?label=flywaydb&message=9.22.3&color=green)
![jUnit](https://img.shields.io/static/v1?label=junit&message=5.7.0&color=green)
