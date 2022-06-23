#include "trabalho-8-definicoes.h"

P1 j;
P2 p;
int fd[2];

int main()
{

  mkfifo("retirar", 0666);
  mkfifo("servir", 0666);
  fd[0] = open("retirar", O_RDONLY);
  fd[1] = open("servir", O_WRONLY);
  j.javalis = JAVALIS_GRELHADOS;
  printf("Cozinheiro repoe %d javalis\n", j.javalis);

  write(fd[1], &j, sizeof(P1));

  while (1)
  {
    read(fd[0], &p, sizeof(P2));
    if (p.pipe == 'E')
    {
      j.javalis = JAVALIS_GRELHADOS;
      printf("Cozinheiro repoe %d javalis\n", j.javalis);
      write(fd[1], &j, sizeof(P1));
      read(fd[0], &p, sizeof(P2));
    }
    if (p.pipe == 'B')
    {
      j.javalis--;
    }
    write(fd[1], &j, sizeof(P1));
  }
}
