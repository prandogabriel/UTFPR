#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <time.h>
#include <math.h>

#define AVALIS_GRELHADOS 20
#define QTD_GAULESES 6
#define SHARED_M "/shm_open"

typedef struct _Shared_Memory
{
    int javalis;
    sem_t semaforo_1, semaforo_2, semaforo_3, semaforo_4, semaforo_5;

}Shared_Memory;

char nome[QTD_GAULESES] = "WESLEY";
int tempo;

Shared_Memory *sm;

void iniciaSM(){
    int fd = shm_open(SHARED_M, O_RDWR|O_CREAT, S_IRUSR|S_IWUSR);

    ftruncate(fd, sizeof(shm_t));

    sm = mmap(NULL, sizeof(shm_t), PROT_READ|PROT_WRITE, MAP_SHARED, fd, 0);
}

void RetiraJavali(int g){
    sem_wait(&sm->semaforo_4);

    if(!sm->javalis)
    {
        sem_post(&sm->semaforo_1);

        printf("Gaules %c(%d) acordoa o cozinheiro\n", nome[g], g);
        sem_wait(&sm->semaforo_2);
    }
    if(sm->javalis)
    {
        sm->javalis--;
    }
    sem_post(&sm->semaforo_3);
}

void *Gaules(void *thr){
    int g = (int)thr;
    while(1){
	sem_wait(&sm->semaforo_5);
        sem_post(&sm->semaforo_4);
        RetiraJavali(g);
        sem_wait(&sm->semaforo_3);
        printf("Gaules %c(%d) come, faltam %d javalis\n", nome[g], g, sm->javalis);
        sem_post(&sm->semaforo_5);

        tempo=rand() % 6;
        sleep(tempo);
    }
    exit(0);
}

int main(void)
{
    iniciaSM();
    pthread_t vaga[QTD_GAULESES];
    int i=0;


    while(i < 6)
    {
        pthread_create(&vaga[i], NULL, Gaules, i);
        i++;
    }

    i=0;
    while(i < 6)
    {
        pthread_join(vaga[i], NULL);
        i++;
    }

    sem_destroy(&semaforo_1);
    sem_destroy(&semaforo_2);
    sem_destroy(&semaforo_3);
    sem_destroy(&semaforo_4);
    sem_destroy(&semaforo_5);

    exit(0);
}

