#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <time.h>
#include <math.h>

sem_t semaforo_1,semaforo_2,semaforo_3;
int tempo;

void *Processo_A(void *item)
{
    printf("Iniciando A\n");

    tempo = rand() % 5;
    sleep(tempo);
    sem_post(&semaforo_1);

    printf("Finalizando processo A\n");
	return 0;

}

void *Processo_B(void *item)
{
    printf("Iniciando B\n");

    tempo = rand() % 5;
    sleep(tempo);

    sem_post(&semaforo_2);

    printf("Finalizando processo B\n");
	return 0;

}

void *Processo_C(void *item)
{
    sem_wait(&semaforo_1);
    sem_wait(&semaforo_2);

    printf("Iniciando C\n");

    tempo = rand() % 5;
    sleep(tempo);

    sem_post(&semaforo_3);
    sem_post(&semaforo_3);

    printf("Finalizando processo C\n");
	return 0;
}

void *Processo_D(void *item)
{
    sem_wait(&semaforo_3);

    printf("Iniciando D\n");

    tempo = rand() % 5;
    sleep(tempo);

    printf("Finalizando processo D\n");
	return 0;
}

void *Processo_E(void *item)
{
    sem_wait(&semaforo_3);

    printf("Iniciando E\n");

    tempo = rand() % 5;
    sleep(tempo);

    printf("Finalizando processo E\n");
	return 0;
}


int main(void)
{

    int i=0;
    pthread_t vaga[5];

	sem_init(&semaforo_1,0,0);
    sem_init(&semaforo_2,0,0);
    sem_init(&semaforo_3,0,0);

    srand((unsigned int)(time(NULL)));

    pthread_create(&vaga[0], NULL, Processo_A, NULL);
    pthread_create(&vaga[1], NULL, Processo_B, NULL);
    pthread_create(&vaga[2], NULL, Processo_C, NULL);
    pthread_create(&vaga[3], NULL, Processo_D, NULL);
    pthread_create(&vaga[4], NULL, Processo_E, NULL);

    while(i < 5)
    {
        pthread_join(vaga[i], NULL);
        i++;
    }

    sem_destroy(&semaforo_1);
    sem_destroy(&semaforo_2);
    sem_destroy(&semaforo_3);

	exit(0);


}
