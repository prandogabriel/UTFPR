--Executar 1 vez so no banco para habilitar o uso do indice bitmap
create extension btree_gin

--Criacao do indice (ex: atributo formado = 'S' ou 'N')
create index idxformado on aluno using gin (formado)
