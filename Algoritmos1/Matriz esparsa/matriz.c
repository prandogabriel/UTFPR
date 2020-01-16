#include <stdio.h>
#include <stdlib.h>
#include "matriz.h"

struct celula {
  float valor;
  int col;
  Celula *prox;
};

struct matriz {
  Celula **celulas; // lista de ponteiros
  int lin; // número de linhas
  int col; // número de colunas
};

/* Exemplo:
        0       1       2       3
0       0,0     0,0     5,2     0,0
1       0,0     0,0     0,0     0,0
2       0,0     3,0     0,0     4,0
 ----------       ----------       -------------------       ------
| **celulas| --> | linha 1  | --> | 5,2  | 2   | prox | --> | NULL |
 ----------       ----------       -------------------       ------
| 3        |     | NULL     |
 ----------       ----------       -------------------       -------------------
| 4        |     | linha 3  | --> | 3,0  | 1   | prox | --> | 4,0  | 3   | prox | --> NULL
 ----------       ----------       -------------------       -------------------
*/

// semelhante a estrutura usada para tabela hash com tratamento por lista encadeada
Matriz * inicializa_matriz(int lin, int col) {
  int i;
  Matriz *m = malloc(sizeof(Matriz)); // aloca mem. para a struct Matriz
  m->lin = lin;
  m->col = col;
  m->celulas = malloc(lin * sizeof(Celula*)); // aloca mem. para a umas lista de ponteiros
  for(i = 0; i < lin; i++) {
    m->celulas[i] = NULL; // m->celulas[i] é o início de cada lista e as listas começam vazias
  }
  return m;
}

// semelhante a estrutura usada para tabela hash com tratamento por lista encadeada
void libera_matriz(Matriz *m) {
  int i;
  Celula *aux, *remover;
  for(i = 0; i < m->lin; i++) {
     aux = m->celulas[i];
     while(aux != NULL) {
       remover = aux;
       aux = aux->prox;
       free(remover);
     }
  }
  free(m->celulas);
  free(m);
}

void adiciona_celula(Matriz *m, int lin, int col, float valor) {
  if (lin < 0 || lin >= m->lin || col < 0 || col >= m->col) {
    printf("Erro: a posição %d, %d não existe.\n", lin, col);
    return;
  }
  // se a posição é válida, encontrar a célula correspondente e também a anterior
  Celula *atual, *anterior;
  atual = m->celulas[lin]; // início da lista
  anterior = NULL;
  while(atual != NULL && atual->col < col) {
    anterior = atual;
    atual = atual->prox;
  }
  if(atual != NULL && atual->col == col) { // se a célula existe
    if(valor == 0) { // se valor == 0, então remover a célula
      if(atual == m->celulas[lin]) // é a primeira?
        m->celulas[lin] = atual->prox;
      else // é do meio ou do fim?
        anterior->prox = atual->prox;
      free(atual);
    }
    else // valor != 0, então atualizar a célula
      atual->valor = valor;
  }
  else if (valor != 0) { // se a célula não existe e valor != 0, inserir célula
    Celula *nova = malloc(sizeof(Celula));
    nova->valor = valor;
    nova->col = col;
    nova->prox = atual;
    if(anterior == NULL) // inserir no início
      m->celulas[lin] = nova;
    else // inserir no meio ou fim
      anterior->prox = nova;
  }
}

void imprime_matriz(Matriz *m) {
  int i;
  Celula *aux;
  for(i = 0; i < m->lin; i++) {
    aux = m->celulas[i];
    while(aux != NULL) {
      printf("%d, %d: %.2f\t", i, aux->col, aux->valor);
      aux = aux->prox;
    }
    printf("\n");
  }
}

float busca_valor(Matriz *m, int lin, int col) {
  if (lin < 0 || lin >= m->lin || col < 0 || col >= m->col) {
    printf("Erro: a posição %d, %d não existe.\n", lin, col);
    return 0;
  }
  Celula *aux = m->celulas[lin];
  while(aux != NULL && aux->col < col)
    aux = aux->prox;
  if(aux != NULL && aux->col == col)
    return aux->valor;
  return 0;
}
