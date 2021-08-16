# Primeira prova de Banco 2

Para acessar a descrição da atividade, [clique aqui](./Atividade_Indexes.pdf)



Para o desenvolvimento, foi utilizado docker para rodar o banco e postgres na versão  13.4 (LTS no período de desenvolvimento da atividade)



```bash
# Para iniciar o banco
docker run -d --name p1-banco-2 -e POSTGRES_PASSWORD=docker -p 5432:5432 postgres:13.4

# Após pode ser conectado em alguma interface com o user "postgres", senha "docker", host "localhost", porta "5432"
```



