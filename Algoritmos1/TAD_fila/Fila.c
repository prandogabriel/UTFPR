#include <stdio.h>
#include <stdlib.h>
#include "Fila.h"

struct item {
  int chave;
  // demais campos
};

struct fila {
  Item item[MAXTAM];
  int primeiro;
  int ultimo;
  int tamanho;
};

Fila * cria_fila_vazia() {
  Fila *f = malloc(sizeof(Fila));
  f->primeiro = 0;
  f->ultimo = 0;
  f->tamanho = 0;
  return f;
}

int verifica_fila_vazia(Fila *f) {
  return f->tamanho == 0;
}

int verifica_fila_cheia(Fila *f) {
  return f->ultimo == MAXTAM;
}

void enfileira(Fila* f, int chave) {
  Item novo_item;

  if(verifica_fila_cheia(f)) {
		printf("Erro: a fila está cheia.\n");
    return;
  }
	else {
    novo_item.chave = chave;
		f->item[f->ultimo] = novo_item;
		f->ultimo++;
		f->tamanho++;
	}
}

void imprime(Fila* f) {
  int i;
	int p = f->primeiro;
	int u = f->ultimo;
  for(i = p; i < u; i++)
		printf("Chave: %d\n", f->item[i].chave);
}

void desenfileira(Fila* f) {
	if(verifica_fila_vazia(f)) {
    printf("Erro: a fila está vazia.\n");
    return;
  }
	else{
		f->primeiro++;
		f->tamanho--;
	}
}

int tamanho(Fila *f) {
    return f->tamanho;
}

void libera(Fila *f) {
  free(f);
}
