#include <stdio.h>
#include <stdlib.h>
#include "fila.h"

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
  return f->tamanho == MAXTAM;
}

void enfileira(Fila* f, int chave) {
  if(verifica_fila_cheia(f)) {
		printf("Erro: a fila está cheia.\n");
    return;
  }
	else {
    Item novo_item;
    novo_item.chave = chave;
		f->item[f->ultimo] = novo_item;
		f->ultimo = (f->ultimo + 1) % MAXTAM;
		f->tamanho++;
	}
}

void imprime(Fila* f) {
	int i = f->primeiro;
	int t = f->tamanho;
	while (t > 0) {
		printf("Chave: %d\n", f->item[i].chave);
		t--;
		i = (i+1) % MAXTAM;
	}
}
int desenfilera(Fila *f){
    int aux;
    aux = f->item[f->primeiro].chave;
    f->primeiro = (f->primeiro + 1)% MAXTAM;
    f->tamanho--;
  
  return aux;
}
void remove_compara(Fila* f) {
    int maior, x;
    Fila *aux = cria_fila_vazia();
    while(!verifica_fila_vazia(f) && !verifica_fila_vazia(f)){
      if (!verifica_fila_vazia(f)){
        maior = desenfilera(f);
        printf("\n%d",maior);
        if(!verifica_fila_vazia(f)){
            x = desenfilera(f);
          while(x <= maior && !verifica_fila_vazia(f)){
              enfileira(aux,x);
              x=desenfilera(f);
          }
        if(x>maior)
          printf("  %d \n",x );
        else
          printf("  -1\n");
        enfileira(aux, x);
        while(!verifica_fila_vazia(f) ){
          enfileira(aux, desenfilera(f));
        }
        }
        else
          printf("  -1\n");
    }
    if (!verifica_fila_vazia(aux)){   
        maior = desenfilera(aux);
        printf("\n%d",maior);
        if(!verifica_fila_vazia(aux)){
            x = desenfilera(aux);
          while(x <= maior && !verifica_fila_vazia(aux)){
              enfileira(f,x);
              x=desenfilera(aux);
          }
        if(x>maior)
          printf("  %d \n",x );
        else 
          printf("  -1\n");
        enfileira(f, x);
        while(!verifica_fila_vazia(aux) ){
          enfileira(f, desenfilera(aux));
        }
      }
      else
      {
        printf("  -1\n");
      }
      
    }
    }
}

void libera(Fila *f) {
  free(f);
}