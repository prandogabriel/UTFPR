#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <semaphore.h>
#include <pthread.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <mqueue.h>
#include <time.h>
#include <math.h>

#define JAVALIS_GRELHADOS 20
#define QTD_GAULESES 6

typedef struct mensagemBuff{
    int ct;
}MensagemBuff;

MensagemBuff ac, ret;
MensagemBuff *acorda = &ac, *retira = &ret;
mqd_t fd1, fd2;
int javalis;
sem_t semaforo_1, semaforo_2, semaforo_3;
char nome[QTD_GAULESES] = "WESLEY";
int tempo;

void iniciaSemaforo(){
    sem_init(&semaforo_1, 0, 0);
    sem_init(&semaforo_2, 0, 0);
    sem_init(&semaforo_3, 0, 1);
}

void RetiraJavali(long g)
{
    sem_wait(&semaforo_1);

    if(retira->ct == 0){
        printf("Gaules %c(%d) acordou o cozinheiro\n", nome[g], g);
        mq_send(fd1, (void*)acorda, sizeof(MensagemBuff), 0);
		mq_receive(fd2, (void*)retira, sizeof(MensagemBuff), 0);
    }
    if(retira->ct){
        retira->ct--;
    }
    sem_post(&semaforo_2);
}

void *Gaules(void *thr){
    int g = (int)thr;
    while(1)
    {
        sem_wait(&semaforo_3);
        sem_post(&semaforo_1);

        RetiraJavali(gaules);
        sem_wait(&semaforo_2);

        printf("Gaules %c(%d) come, faltam %d javalis\n", nome[g], g, retira->ct);
        sem_post(&semaforo_3);

        tempo= rand() % 6;
        sleep(tempo);
    }
    exit(0);
}

int main(){

    struct mq_attr attr;
    attr.mq_maxmsg = 1;
	attr.mq_msgsize = sizeof(MensagemBuff);
	attr.mq_flags = 0;

	srand((unsigned int)(time(NULL)));

	fd1 = mq_open("/acorda", O_RDWR);
	fd2 = mq_open("/retira", O_RDWR);

    iniciaSemaforo();
    javalis = JAVALIS_GRELHADOS;
    pthread_t vaga[QTD_GAULESES];
    acorda->ct = 1;
    mq_receive(fd2, (void*)retira, sizeof(MensagemBuff), 0);

    int i=0;
    while(i < QTD_GAULESES){
        pthread_create(&vaga[i], NULL, Gaules, i);
        i++;
    }

    i=0;
    while(i < QTD_GAULESES){
        pthread_join(vaga[i], NULL);
        i++
    }

    sem_destroy(&semaforo_1);
    sem_destroy(&semaforo_2);
    sem_destroy(&semaforo_3);

    exit(0);
}

