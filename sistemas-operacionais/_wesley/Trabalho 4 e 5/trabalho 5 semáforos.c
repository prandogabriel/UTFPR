#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <time.h>
#include <math.h>

sem_t semaforo[7];
int h, j, u, x, y, z;


void *Processo_1(void *item)
{
    x=1, y=1, z=2;
	y = y + 2;

	sem_wait(&semaforo[0]);
    sem_post(&semaforo[1]);
    sem_post(&semaforo[2]);
    sem_post(&semaforo[3]);
    sem_post(&semaforo[4]);
    sem_wait(&semaforo[5]);
    sem_wait(&semaforo[6]);

	u = h + j / 3;


    return 0;
}

void *Processo_2(void *item)
{

    x = x * 2;
    x = x + 1;
	h = y + x;

    sem_wait(&semaforo[1]);
	sem_wait(&semaforo[3]);
    sem_post(&semaforo[5]);

    return 0;
}

void *Processo_3(void *item)
{
	z = z / 2;
	j = z + y - 4;

	sem_wait(&semaforo[2]);
    sem_wait(&semaforo[4]);
    sem_post(&semaforo[6]);



    return 0;
}

int main(void)
{
    pthread_t vaga[3];

    int i = 1;
    sem_init(&semaforo[0], 0, 1);

    while(i < 7)
    {
        sem_init(&semaforo[i], 0, 0);
        i++;
    }


    pthread_create(&vaga[0], NULL, Processo_1, NULL);
    pthread_create(&vaga[1], NULL, Processo_2, NULL);
    pthread_create(&vaga[2], NULL, Processo_3, NULL);

    i = 0;
    while(i < 3)
    {
        pthread_join(vaga[i], NULL);
        i++;

    }

    printf("O valor de u eh: %d\n", u);

    sem_destroy(&semaforo[0]);
    sem_destroy(&semaforo[1]);
    sem_destroy(&semaforo[2]);
    sem_destroy(&semaforo[3]);
    sem_destroy(&semaforo[4]);
    sem_destroy(&semaforo[5]);
    sem_destroy(&semaforo[6]);


    exit(0);
}
