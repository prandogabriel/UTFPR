/*
Escreva um trecho de código que uti liza a função fork() e gera uma árvore de busca
binária (ou quase isso) com seu primeiro nome. Para tal siga as seguintes regras:
1. Primeira letra será a raiz da árvore
2. Cada letra seguinte será inserida a direita, se a letra for maior que a raiz, ou a
esquerda, se a letra for menor que a raiz.
3. Esse procedimento (verificar a raiz e inserir a direita ou a esquerda) deve ser realizado recursivamente.
Ex: PRANDO
     P
    / \
    A  R
     \
      N
    /  \
   D    O


Atenção, um processo deve mostrar uma mensagem se idenficando (“proc-P”... “proc-O”)
quando ele criado e quando ele está prestes a morrer
cada processo gerado deve imprimir o seu PID e o PPID você deve garantir que um pai sempre morre depois de seu filho!
*/

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

int main(void)
{

  pid_t pid;
  int status = 0;

  printf("proc-P, pid %d, ppid %d, criado\n", getpid(), getppid());

  pid = fork();
  if (pid == 0)
  { //  filho entra
    printf("proc-R, pid %d, ppid %d, criado\n", getpid(), getppid());
    printf("proc-R, pid %d, ppid %d, morreu\n", getpid(), getppid());
    exit(0);
  }
  waitpid(pid, &status, 0);

  pid = fork();
  if (pid == 0)
  { //  filho entra
    printf("proc-A, pid %d, ppid %d, criado\n", getpid(), getppid());

    pid = fork();
    if (pid == 0)
    { //  filho entra
      printf("proc-N, pid %d, ppid %d, criado\n", getpid(), getppid());
      // antes de se matar, N tem que criar 2 e esperar os 2

      pid = fork();
      if (pid == 0)
      { //  filho entra
        printf("proc-D, pid %d, ppid %d, criado\n", getpid(), getppid());
        printf("proc-D, pid %d, ppid %d, morreu\n", getpid(), getppid());
        exit(0);
      }
      waitpid(pid, &status, 0); // N espera N se matar
      pid = fork();
      if (pid == 0)
      { //  filho entra
        printf("proc-O, pid %d, ppid %d, criado\n", getpid(), getppid());
        printf("proc-O, pid %d, ppid %d, morreu\n", getpid(), getppid());
        exit(0);
      }
      waitpid(pid, &status, 0); // N espera N se matar

      printf("proc-N, pid %d, ppid %d, morreu\n", getpid(), getppid());
      exit(0);
    }

    waitpid(pid, &status, 0); // N espera N se matar

    printf("proc-A, pid %d, ppid %d, morreu\n", getpid(), getppid());
    exit(0);
  }

  waitpid(pid, &status, 0); // A espera N se matar

  printf("proc-P, pid %d, ppid %d, morreu\n", getpid(), getppid());
  // exit(0);
}
