#!/bin/bash

openssl ecparam -genkey -name secp256r1 | openssl ec -out ca.key
openssl req -new -subj "/C=BR/ST=Parana/L=Pato Branco/O=UTFPR/OU=PB/CN=UTFPR-PB-ECC-secp256r1" -x509 -days 3650 -key ca.key -out ca.pem

openssl ecparam -genkey -name secp256r1 | openssl ec -out server.key
openssl req -new -subj "/C=BR/ST=Parana/L=Pato Branco/O=UTFPR/OU=PB/CN=UTFPR-PB-server-ECC-secp256r1" -key server.key -out server.csr
openssl x509 -req -days 3650 -in server.csr -CA ca.pem -CAkey ca.key -set_serial 01 -out server.pem
openssl pkcs8 -topk8 -nocrypt -in server.key -out server_pkcs8.key
openssl pkcs12 -export -out server.p12 -in server.pem -inkey server_pkcs8.key -passout pass:password
cat server_pkcs8.key server.pem > server_cert_and_key.pem

openssl ecparam -genkey -name secp256r1 | openssl ec -out client01.key
openssl req -new -subj "/C=BR/ST=Parana/L=Pato Branco/O=UTFPR/OU=PB/CN=UTFPR-PB-client01-ECC-secp256r1" -key client01.key -out client01.csr
openssl x509 -req -days 3650 -in client01.csr -CA ca.pem -CAkey ca.key -set_serial 02 -out client01.pem
openssl pkcs8 -topk8 -nocrypt -in client01.key -out client01_pkcs8.key
openssl pkcs12 -export -out client01.p12 -in client01.pem -inkey client01_pkcs8.key -passout pass:password
cat client01_pkcs8.key client01.pem > client01_cert_and_key.pem


# client01_cert_and_key para testar no curl
# client01.pem e client01.key para testar em um client como o esp
# outros podem ser utilizado em outras linguagens
