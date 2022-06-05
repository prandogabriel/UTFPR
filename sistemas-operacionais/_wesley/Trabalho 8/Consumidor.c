#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <time.h>
#include <math.h>

#define JAVALIS_GRELHADOS 20
#define QTD_GAULESES 6

typedef struct _P1
{
    int javalis;

}P1;

typedef struct _P2
{
    char pipe;

}P2;

P1 j;
P2 p;
int fd[2];

sem_t semaforo_1, semaforo_2, semaforo_3;
char nome[QTD_GAULESES] = "WESLEY";
int tempo;

void iniciaSemaforo(){
    sem_init(&semaforo_1, 0, 0);
    sem_init(&semaforo_2, 0, 0);
    sem_init(&semaforo_3, 0, 1);
}

void RetiraJavali(int g){
    sem_wait(&semaforo_1);
    read(fd[1], &j, sizeof(P1));

    if(j.javalis == 0)
    {
        printf("Gaules %c(%d) acordou o cozinheiro\n", nome[g], g);
        p.pipe = 'E';
        write(fd[0], &p, sizeof(P2));
        read(fd[1], &j, sizeof(P1));
    }
    if(j.javalis > 0){
        p.pipe = 'B';
        write(fd[0], &p, sizeof(P2));
    }
    sem_post(&semaforo_2);
}

void *Gaules(void *thr){
    int g = (int)thr;
    while(1)
    {
        sem_wait(&semaforo_3);
        sem_post(&semaforo_1);

        RetiraJavali(g);
        sem_wait(&semaforo_2);

        printf("Gaules %c(%d) come, faltam %d javalis\n", nome[g], g, j.javalis - 1);
        sem_post(&semaforo_3);

        tempo= rand() % 6;
        sleep(tempo);
    }
    exit(0);
}

int main(void)
{
    int i= 0;

    srand((unsigned int)(time(NULL)));


    mkfifo("retirar", 0666);
    mkfifo("servir", 0666);



    iniciaSemaforo();

    fd[0] = open("retirar", O_WRONLY);
    fd[1] = open("servir", O_RDONLY);

    pthread_t vaga[QTD_GAULESES];

    while(i < QTD_GAULESES)
    {
        pthread_create(&vaga[i], NULL, Gaules, i);
         i++;
    }


    i=0;
    while(i < QTD_GAULESES)
    {
        pthread_join(vaga[i], NULL);
        i++;
    }

    sem_destroy(&semaforo_1);
    sem_destroy(&semaforo_2);
    sem_destroy(&semaforo_3);


    exit(0);
}
