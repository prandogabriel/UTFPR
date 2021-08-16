#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
  int *v; // vetor[] de tamanho n
  int q; // quantidade de itens inseridos em v
  int n; // tamanho (capacidade) de v
} Vetor;

Vetor * iniciar(int n) {
  Vetor *aux = malloc(sizeof(Vetor));

  aux->v = (int*)malloc(n * sizeof(int));
  aux->n = n;
  aux->q = 0;

  return aux;
}

void inserir(Vetor *v, int x) {
  if(v->q < v->n) { // vetor está cheio
    v->v[v->q] = x;
    v->q++;
  }
}

Vetor * concatenar(Vetor *v1, Vetor *v2) {
  Vetor *aux;
  int i=0, j=0;


  aux = iniciar(v1->n+v2->n);

  while (i<v1->n)
  {
    aux->v[i]=v1->v[i];
    aux->q++;
    i++;
  }

  while (i<v1->n+v2->n)
  {
    aux->v[i]=v2->v[j];

    i++;
    j++;
  }

  return aux;
}

Vetor *soma(Vetor *v1, Vetor *v2)
{
  Vetor *vet4;
  int i;
  if(v1->n >= v2->n){
    vet4 = iniciar(v1->n);
  }
  else
    vet4 = iniciar(v2->n);

  for(i=0;i<vet4->n;i++){

    if(i < v1->n && i<v2->n){
      vet4->v[i]= v1->v[i] + v2->v[i];
    }
    else if(i < v1->n && i>=v2->n){
      vet4->v[i]= v1->v[i];
    }
    else{
      vet4->v[i] = v2->v[i];
    }
  }
  return vet4;
}


int main() {
  Vetor *vet1, *vet2, *vet3, *vet4;
  int aux;
  int i = 0;
  scanf("%d",&aux);

  vet1 = iniciar(aux);

  while (i<vet1->n)
  {
    scanf("%d", &aux);

    inserir(vet1, aux);
    i++;
  }


  scanf("%d",&aux);

  vet2 = iniciar(aux);

  i=0;

  while (i<vet2->n)
  {
    scanf("%d", &aux);

    inserir(vet2, aux);

    i++;
  }

  vet3 = concatenar(vet1, vet2);

  i=0;

  while (i < vet2->n+vet1->n)
  {
    printf("%d ", vet3->v[i]);
    i++;
  }
  printf("\n");

  vet4 = soma(vet1, vet2);
  for(i=0; i<vet4->n;i++){
    printf("%d ", vet4->v[i]);
  }

  printf("\n");


  return 0;
}



