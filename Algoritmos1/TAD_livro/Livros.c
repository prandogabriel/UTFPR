#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include "Livros.h"

struct livro {
  char titulo[30];
  char genero[30];
  int ano;
  float valor;
};

Livro * cadastra_livro(char titulo[], char genero[], int ano, float valor) {
  Livro *l;
  l = malloc(sizeof(Livro));
  strcpy(l->titulo, titulo);
  strcpy(l->genero, genero);
  l->ano = ano;
  l->valor = valor;
  return l;
}

void imprime_livro(Livro *l){
  puts(l->titulo);
  puts(l->genero);
  printf("%d\n", l->ano);
  printf("%.2f\n\n", l->valor);
}

Livro * retorna_mais_caro(Livro **l, int n) {
  Livro *mais_caro;
  float maior_valor = l[0]->valor;
  int i;
  for(i = 1; i < n; i++) {
    if(l[i]->valor > maior_valor) {
      maior_valor = l[i]->valor;
      mais_caro = l[i];
    }
  }
  return mais_caro;
}

Livro * retorna_mais_barato(Livro **l, int n) {
  Livro *mais_barato;
  float menor_valor = l[0]->valor;
  int i;
  for(i = 1; i < n; i++) {
    if(l[i]->valor < menor_valor) {
      menor_valor = l[i]->valor;
      mais_barato = l[i];
    }
  }
  return mais_barato;
}

// mostra livros de um determinado genero
// mostra livros que são lançamento
// libera livros
