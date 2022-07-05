#include "trabalho-7-definicoes.h"

int tempo;

Shared_Memory *sm;

void iniciaSM()
{
  int fd = shm_open(SHARED_M, O_RDWR | O_CREAT, S_IRUSR | S_IWUSR);

  ftruncate(fd, sizeof(sem_t));

  sm = mmap(NULL, sizeof(sem_t), PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
}

void RetiraJavali(int g)
{
  sem_wait(&sm->sem_4);

  if (!sm->javalis)
  {
    sem_post(&sm->sem_1);

    printf("Gaules %c(%d) acordoa o cozinheiro\n", nome[g], g);
    sem_wait(&sm->sem_2);
  }
  if (sm->javalis)
  {
    sm->javalis--;
  }
  sem_post(&sm->sem_3);
}

void *Gaules(void *thr)
{
  int g = (int)thr;
  while (1)
  {
    sem_wait(&sm->sem_5);
    sem_post(&sm->sem_4);
    RetiraJavali(g);
    sem_wait(&sm->sem_3);
    printf("Gaules %c(%d) come, faltam %d javalis\n", nome[g], g, sm->javalis);
    sem_post(&sm->sem_5);

    tempo = rand() % 6;
    sleep(tempo);
  }
  exit(0);
}

int main()
{
  iniciaSM();
  pthread_t vaga[QTD_GAULESES];
  int i = 0;

  while (i < 6)
  {
    pthread_create(&vaga[i], NULL, Gaules, i);
    i++;
  }

  i = 0;
  while (i < 6)
  {
    pthread_join(vaga[i], NULL);
    i++;
  }

  sem_destroy(&sm->sem_1);
  sem_destroy(&sm->sem_2);
  sem_destroy(&sm->sem_3);
  sem_destroy(&sm->sem_4);
  sem_destroy(&sm->sem_5);

  exit(0);
}
