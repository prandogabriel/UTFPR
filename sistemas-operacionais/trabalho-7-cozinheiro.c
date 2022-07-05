#include "trabalho-7-definicoes.h"

Shared_Memory *sm;

void iniciaSemaforo()
{
  sem_init(&sm->sem_1, 1, 0);
  sem_init(&sm->sem_2, 1, 0);
  sem_init(&sm->sem_3, 1, 0);
  sem_init(&sm->sem_4, 1, 0);
  sem_init(&sm->sem_5, 1, 1);

  sm->javalis = JAVALIS_GRELHADOS;
  printf("Cozinheiro repoe %d javalis\n", sm->javalis);
}

void iniciaSM()
{
  int fd = shm_open(SHARED_M, O_RDWR | O_CREAT, S_IRUSR | S_IWUSR);

  ftruncate(fd, sizeof(Shared_Memory));

  sm = mmap(NULL, sizeof(Shared_Memory), PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
}

void *Produtor()
{
  while (1)
  {
    sem_wait(&sm->sem_1);

    printf("Cozinheiro repoe %d javalis\n", sm->javalis);
    sm->javalis = JAVALIS_GRELHADOS;
    sem_post(&sm->sem_2);
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
