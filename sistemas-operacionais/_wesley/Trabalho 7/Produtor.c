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


#define JAVALIS_GRELHADOS 20
#define QTD_GAULESES 6
#define SHARED_M "/shm_open"

typedef struct _Shared_Memory
{
    int javalis;
    sem_t semaforo_1, semaforo_2, semaforo_3, semaforo_4, semaforo_5;;
}Shared_Memory;

Shared_Memory *sm;

void iniciaSemaforo()
{
    sem_init(&sm->semaforo_1, 1, 0);
    sem_init(&sm->semaforo_2, 1, 0);
    sem_init(&sm->semaforo_3, 1, 0);
    sem_init(&sm->semaforo_4, 1, 0);
    sem_init(&sm->semaforo_5, 1, 1);

    sm->javalis = JAVALIS_GRELHADOS;
    printf("Cozinheiro repoe %d javalis\n", sm->javalis);
}

void iniciaSM()
{
    int fd = shm_open(SHARED_M, O_RDWR|O_CREAT, S_IRUSR|S_IWUSR);

    ftruncate(fd, sizeof(Shared_Memory));

    sm = mmap(NULL, sizeof(Shared_Memory), PROT_READ|PROT_WRITE, MAP_SHARED, fd, 0);
}


void *Produtor()
{
    while(1)
    {
        sem_wait(&sm->semaforo_1);

        printf("Cozinheiro repoe %d javalis\n", sm->javalis);
        sm->javalis = JAVALIS_GRELHADOS;
        sem_post(&sm->semaforo_2);
    }
    exit(0);
}


int main(void)
{
    iniciaSemaforo();
    iniciaSM();
    pthread_t produtor;

    srand((unsigned int)(time(NULL)));

    pthread_create(&produtor, NULL, Produtor, NULL);
    pthread_join(produtor, NULL);

    exit(0);
}

