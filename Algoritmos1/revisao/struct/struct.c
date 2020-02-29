#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#define MAXTAM 100

typedef struct
{
  char nome[MAXTAM];
  int ra;
  float peso;
} Aluno;

// void alteraNome(Aluno *a1)
// {
//   //a1->nome[MAXTAM]="jubileu";
//   // Aluno *aux = malloc(sizeof(Aluno));
  
//   // strcpy(aux->nome, "jubileu");
//   // strcpy(a1->nome,aux->nome);
// }

int main(void)
{
  Aluno *a1;
  strcpy(a1->nome, "joao");
  a1->ra = 123;
  a1->peso = 71.5;

  printf("o nome do aluno é %s \n", a1->nome);
  printf("o peso do aluno é %f \n", a1->peso);
  // //alteraNome(a1);
  // printf("Nome alterado para :");
  // printf("%s \n", a1->nome);

  printf("\n");
  return 0;
}
