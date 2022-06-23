#include "trabalho-8-definicoes.h"

int main(void){
    pid_t pid;

    pid = fork();
    if(pid > 0){
        system("gcc trabalho-8-cozinheiro.c -o trabalho-8-cozinheiro.o -lpthread");
        execl("./trabalho-8-cozinheiro.o", "", NULL);
    } else if (pid == 0){
        system("gcc trabalho-8-gaules.c -o trabalho-8-gaules.o -lpthread");
        execl("./trabalho-8-gaules.o", "", NULL);
    } else{
        printf("Erro\n");
    }
    exit(0);
}
