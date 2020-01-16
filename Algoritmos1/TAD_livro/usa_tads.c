#include <stdio.h>
#include "Livros.h"

int main(){
  Livro **l;
  int n, i, ano;
  float valor;
  char titulo[30], genero[30];
  scanf("%d", &n);
  l = malloc(n * sizeof(Livro*)); // *l[n];
  for(i = 0; i < n; i++) {
    scanf(" %[^\n]s", &titulo);
    scanf(" %[^\n]s", &genero);
    scanf(" %d", &ano);
    scanf(" %f", &valor);
    l[i] = cadastra_livro(titulo, genero, ano, valor);
    imprime_livro(l[i]);
  }
  printf("Mais caro:\n");
  imprime_livro(retorna_mais_caro(l, n));
  printf("Mais barato:\n");
  imprime_livro(retorna_mais_barato(l, n));

  return 0;
}
