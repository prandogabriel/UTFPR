const { Client } = require('pg')
const faker = require('faker')

process.env.PGUSER='postgres'
process.env.PGHOST="localhost"
process.env.PGPASSWORD="docker"
process.env.PGDATABASE="postgres"
process.env.PGPORT="5432"

const seed = async () => {

  const client = new Client()
  await client.connect()

  await client.query(
    `
    do $$
    declare varEstado varchar [] = '{"PR", "SC", "RS", "SP"}';
    begin for i in 1..1000000 loop for j in 1..4 loop
    INSERT INTO Aluno (Nome,RA,DataNasc,Idade,NomeMae,Cidade,Estado,Curso,periodo) VALUES("${faker.name.firstName() + faker.name.lastName()}","${faker.datatype.number({min:0,max:1000000,precision:0})}",data(),numero(2),"${faker.name.firstName() + faker.name.lastName()}","${faker.address.city()}",varEstado [j],texto(20),numero(1)) on conflict do nothing;
    end loop;
    end loop;
    end;
    $$ language plpgsql;
  `)
  console.log(res.rows[0].message) // Hello world!
  await client.end()

  return 'end seed'
}

seed().then(success => console.log(success)).catch(error => console.log({error}))
