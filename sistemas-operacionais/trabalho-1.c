/*
Escreva um programa que realiza tratamento de sinais POSIX. O programa deve:
Contar quantas vezes o usuário envia o sinal SIGINT (Ctrl-C) para o processo em execução. Quando o sinal receber um SIGTSTP (Ctl-Z), ele deve imprimir o número de sinais SIGINT que ele recebeu.
Depois de ter recebido 10 SIGINT’s, o programa deve “convidar” o usuário a sair (“Really exit (Y/n)?”).
* Se o usuário não responder em 5 seg., o programa finaliza
* Se responder ‘Y’ manda um sinal de termino a ele próprio.
* Se responder ‘n’ reinicia contagem
*/

#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int count=0;

void SIGINT_handler(){
    count = count + 1;
}

void SIGTSTP_handler(){
    printf("O numero de sinais SIGINT recebidos e: %d\n",count);
}

int main(void){
    signal(SIGINT, SIGINT_handler);
    signal(SIGTSTP, SIGTSTP_handler);

    for(;;){

        char resposta;
        if(count == 10){

            alarm(5);

            printf("\nReally exit (Y/n)?");
            scanf(" %c",&resposta);

            if(resposta == 'Y'){
                 kill(getpid(), SIGKILL);
            }
            else{
                alarm(0);
                count=0;
            }
        }
    }

}
