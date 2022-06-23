#include "trabalho-12-definicoes.h"

int main()
{
  pid_t pid;

  pid = fork();
  if (pid > 0)
  {
    system("gcc trabalho-12-cozinheiro.c -o trabalho-12-cozinheiro.o -lrt");
    execl("./trabalho-12-cozinheiro.o", "", NULL);
  }
  else if (pid == 0)
  {

    sleep(1);
    system("gcc trabalho-12-gaules.c -o trabalho-12-gaules.o -lpthread -lrt ");
    execl("./trabalho-12-gaules.o", "", NULL);
  }
  else
  {
    printf("Erro\n");
  }

  exit(0);
}
