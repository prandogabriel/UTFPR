#include "trabalho-8-definicoes.h"

P1 j;
P2 p;
int fd[2];

sem_t sem_1, sem_2, sem_3;
int tempo;

void iniciaSemaforo()
{
  sem_init(&sem_1, 0, 0);
  sem_init(&sem_2, 0, 0);
  sem_init(&sem_3, 0, 1);
}

void RetiraJavali(int g)
{
  sem_wait(&sem_1);
  read(fd[1], &j, sizeof(P1));

  if (j.javalis == 0)
  {
    printf("Gaules %c(%d) acordou o cozinheiro\n", nome[g], g);
    p.pipe = 'E';
    write(fd[0], &p, sizeof(P2));
    read(fd[1], &j, sizeof(P1));
  }
  if (j.javalis > 0)
  {
    p.pipe = 'B';
    write(fd[0], &p, sizeof(P2));
  }
  sem_post(&sem_2);
}

void *Gaules(void *thr)
{
  int g = (int)thr;
  while (1)
  {
    sem_wait(&sem_3);
    sem_post(&sem_1);

    RetiraJavali(g);
    sem_wait(&sem_2);

    printf("Gaules %c(%d) come, faltam %d javalis\n", nome[g], g, j.javalis - 1);
    sem_post(&sem_3);

    tempo = rand() % 6;
    sleep(tempo);
  }
  exit(0);
}

int main(void)
{
  int i = 0;

  srand((unsigned int)(time(NULL)));

  mkfifo("retirar", 0666);
  mkfifo("servir", 0666);

  iniciaSemaforo();

  fd[0] = open("retirar", O_WRONLY);
  fd[1] = open("servir", O_RDONLY);

  pthread_t thread[QTD_GAULESES];

  while (i < QTD_GAULESES)
  {
    pthread_create(&thread[i], NULL, Gaules, i);
    i++;
  }

  i = 0;
  while (i < QTD_GAULESES)
  {
    pthread_join(thread[i], NULL);
    i++;
  }

  sem_destroy(&sem_1);
  sem_destroy(&sem_2);
  sem_destroy(&sem_3);

  exit(0);
}
