#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <time.h>
#include <math.h>

#define JAVALIS_GRELHADOS 20
#define QTD_GAULESES 7

sem_t sem_1, sem_2, sem_3, sem_4, sem_5;
char name[QTD_GAULESES] = "GABRIEL";
int javalis = JAVALIS_GRELHADOS;
int tempo;

void RetiraJavali(int g)
{

  sem_wait(&sem_1);

  if (!javalis)
  {
    sem_post(&sem_2);

    printf("Gaules %c(%d) acorda o cozinheiro\n", name[g], g);
    sem_wait(&sem_4);
  }

  if (javalis)
  {
    javalis--;
  }
  sem_post(&sem_5);
}

void *Gaules(void *thr)
{

  int g = (int)thr;
  while (1)
  {

    sem_post(&sem_1);
    sem_wait(&sem_3);
    printf(" javalis %d \n", g);
    RetiraJavali(g);

    sem_wait(&sem_5);

    printf("Gaules %c(%d) come, faltam %d javalis\n", name[g], g, javalis);
    sem_post(&sem_3);

    tempo = rand() % 6;
    sleep(tempo);
  }

  exit(0);
}

void *Cozinheiro()
{
  while (1)
  {
    sem_wait(&sem_2);

    printf("O Gaules acorda o cozinheiro que repoe os javalis\n");
    javalis = JAVALIS_GRELHADOS;
    sem_post(&sem_4);
  }
  exit(0);
}

int main(void)
{


  sem_init(&sem_2, 0, 0);
  sem_init(&sem_1, 0, 0);
  sem_init(&sem_3, 0, 1);
  sem_init(&sem_4, 0, 0);
  sem_init(&sem_5, 0, 0);

  srand((unsigned int)(time(NULL)));

  pthread_t thread[7];

  int i = 0;
  while (i < 6)
  {
    pthread_create(&thread[i], NULL, Gaules, i);
    i++;
  }

  pthread_create(&thread[6], NULL, Cozinheiro, NULL);

  while (i < 7)
  {
    pthread_join(thread[i], 0);
    i++;
  }

  sem_destroy(&sem_1);
  sem_destroy(&sem_2);
  sem_destroy(&sem_3);
  sem_destroy(&sem_4);
  sem_destroy(&sem_5);

  exit(0);
}
