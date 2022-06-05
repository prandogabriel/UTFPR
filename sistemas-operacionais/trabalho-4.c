#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <time.h>
#include <math.h>

sem_t sem_1, sem_2, sem_3;
int tempo;

void *process_a(void *item)
{
  printf("Iniciando A\n");

  tempo = rand() % 5;
  sleep(tempo);
  sem_post(&sem_1);

  printf("Finalizando processo `A`\n");
  return 0;
}

void *process_b(void *item)
{
  printf("Iniciando B\n");

  tempo = rand() % 5;
  sleep(tempo);

  sem_post(&sem_2);

  printf("Finalizando processo `B`\n");
  return 0;
}

void *process_c(void *item)
{
  sem_wait(&sem_1);
  sem_wait(&sem_2);

  printf("Iniciando C\n");

  tempo = rand() % 5;
  sleep(tempo);

  sem_post(&sem_3);
  sem_post(&sem_3);

  printf("Finalizando processo `C`\n");
  return 0;
}

void *process_d(void *item)
{
  sem_wait(&sem_3);

  printf("Iniciando D\n");

  tempo = rand() % 5;
  sleep(tempo);

  printf("Finalizando processo `D`\n");
  return 0;
}

void *process_e(void *item)
{
  sem_wait(&sem_3);

  printf("Iniciando E\n");

  tempo = rand() % 5;
  sleep(tempo);

  printf("Finalizando processo `E`\n");
  return 0;
}

int main(void)
{

  int i = 0;
  pthread_t thread[5];

  sem_init(&sem_1, 0, 0);
  sem_init(&sem_2, 0, 0);
  sem_init(&sem_3, 0, 0);

  pthread_create(&thread[0], NULL, process_a, NULL);
  pthread_create(&thread[1], NULL, process_b, NULL);
  pthread_create(&thread[2], NULL, process_c, NULL);
  pthread_create(&thread[3], NULL, process_d, NULL);
  pthread_create(&thread[4], NULL, process_e, NULL);

  while (i < 5)
  {
    pthread_join(thread[i], NULL);
    i++;
  }

  sem_destroy(&sem_1);
  sem_destroy(&sem_2);
  sem_destroy(&sem_3);

  return 0;
}
