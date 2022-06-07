#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <time.h>
#include <math.h>

sem_t sem[7];
int h, j, u;

int x = 1, y = 1, z = 2;

// processo "meio"
void *process_1()
{
  //  inicio sem conflito
  y = y + 2;

  sem_wait(&sem[0]);

  // habilita cima
  sem_post(&sem[1]);
  sem_post(&sem[2]);
  sem_post(&sem[3]);
  sem_post(&sem[4]);
  // habilitou a biforcação do processo do meio

  sem_wait(&sem[5]);
  sem_wait(&sem[6]);

  u = h + j / 3;

  return 0;
}

// processo de cima
void *process_2()
{
  //  inicio sem conflito
  x = x * 2;
  x = x + 1;

  sem_wait(&sem[1]);
  sem_wait(&sem[3]);

  h = y + x;

  sem_post(&sem[5]);

  return 0;
}

// processo de baixo
void *process_3()
{
  //  inicio sem conflito
  z = z / 2;
  sem_wait(&sem[2]);
  sem_wait(&sem[4]);
  j = z + y - 4;
  sem_post(&sem[6]);

  return 0;
}

int main(void)
{
  pthread_t thread[3];

  sem_init(&sem[0], 0, 1);

  int i = 1;
  while (i < 7)
  {
    sem_init(&sem[i], 0, 0);
    i++;
  }

  pthread_create(&thread[0], NULL, process_1, NULL);
  pthread_create(&thread[1], NULL, process_2, NULL);
  pthread_create(&thread[2], NULL, process_3, NULL);

  i = 0;
  while (i < 3)
  {
    pthread_join(thread[i], NULL);
    i++;
  }

  printf("O valor de u eh: %d\n", u);

  sem_destroy(&sem[0]);
  sem_destroy(&sem[1]);
  sem_destroy(&sem[2]);
  sem_destroy(&sem[3]);
  sem_destroy(&sem[4]);
  sem_destroy(&sem[5]);
  sem_destroy(&sem[6]);

  exit(0);
}
