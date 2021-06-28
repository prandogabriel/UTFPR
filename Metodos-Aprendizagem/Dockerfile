#Sistema Operacional
FROM ubuntu:16.04

#Dependências do sistema
RUN apt-get update -y
RUN apt-get install -y python3-dev python3-pip build-essential

#Pacotes do Python
COPY requirements.txt /requirements.txt

RUN pip3 install --upgrade pip #Atualização do gerenciador de pacotes
RUN pip3 install numpy
RUN pip3 install matplotlib
RUN pip3 install scipy
RUN pip3 install jupyter #Instalação do pacote jupyter notebook

#Diretório do usuário docker
RUN mkdir -p /home/user
WORKDIR /home/user
