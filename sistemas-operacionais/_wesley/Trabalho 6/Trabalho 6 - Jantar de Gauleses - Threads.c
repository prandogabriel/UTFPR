#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <time.h>
#include <math.h>

#define JAVALIS_GRELHADOS 20
#define QTD_GAULESES 6

sem_t  semaforo_1, semaforo_2, semaforo_3, semaforo_4, semaforo_5;
char nome[QTD_GAULESES] = "WESLEY";
int javalis = JAVALIS_GRELHADOS;
int tempo;


void RetiraJavali(int g){

       sem_wait(&semaforo_1);

       if(!javalis)
       {
         sem_post(&semaforo_2);

         printf("Gaules %c(%d) acorda o cozinheiro\n", nome[g], g);
         sem_wait(&semaforo_4);
       }

       if(javalis)
       {
         javalis--;
       }
        sem_post(&semaforo_5);

}

void *Gaules(void *thr){

    int g = (int)thr;
    while(1)
    {

        sem_post(&semaforo_1);
        sem_wait(&semaforo_3);


        RetiraJavali(g);
        sem_wait(&semaforo_5);

        printf("Gaules %c(%d) come, faltam %d javalis\n", nome[g], g, javalis);
        sem_post(&semaforo_3);

        tempo=rand() % 6;
        sleep(tempo);
    }
   
 exit(0);
}

void *Cozinheiro()
{
    while(1)
    {
        sem_wait(&semaforo_2);

        printf("O Gaules acorda o cozinheiro que repoe os javalis\n");
        javalis = JAVALIS_GRELHADOS;
        sem_post(&semaforo_4);
    }
    exit(0);
}

int main(void)
{

    pthread_t vaga[7];
    int i=0;

    sem_init(&semaforo_2, 0, 0);
    sem_init(&semaforo_1, 0, 0);
    sem_init(&semaforo_3, 0, 1);
    sem_init(&semaforo_4, 0, 0);
    sem_init(&semaforo_5, 0, 0);


    srand((unsigned int)(time(NULL)));

    while(i < 6)
    {
        pthread_create(&vaga[i], NULL, Gaules, i);
		i++;
     }
    pthread_create(&vaga[6], NULL, Cozinheiro, NULL);

    while(i < 7)
    {
        pthread_join(vaga[i], 0);
		i++;
     }

    sem_destroy(&semaforo_1);
    sem_destroy(&semaforo_2);
    sem_destroy(&semaforo_3);
    sem_destroy(&semaforo_4);
    sem_destroy(&semaforo_5);

    exit(0);
}
