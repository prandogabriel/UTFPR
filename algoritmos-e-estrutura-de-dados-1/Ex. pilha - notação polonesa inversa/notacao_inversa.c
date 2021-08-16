#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Pilha.h"

main() {
  Pilha *p = cria_pilha_vazia();
  char eq[50];
  int t, i, resultado;
  Item *ultimo, *penultimo;

  scanf("%s", eq);

  t = strlen(eq);
  for(i = 0; i < t; i++) {
    if(eq[i] >= '0' && eq[i] <= '9')
      empilha(p, eq[i] - '0');
    else {
      ultimo = desempilha(p); // função desempilha foi alterada para retornar o item
      penultimo = desempilha(p);
      if(eq[i] == '+') {
        resultado = soma(penultimo, ultimo);
      }
      else if(eq[i] == '-') {
        resultado = subtrai(penultimo, ultimo);
      }
      else if(eq[i] == '*') {
        resultado = multiplica(penultimo, ultimo);
      }
      else if(eq[i] == '/') {
        resultado = divide(penultimo, ultimo);
      }
      printf("Resultado parcial (%c): %d\n", eq[i], resultado);
      empilha(p, resultado);
    }
    imprime_pilha(p);
  }
  libera_pilha(p);
}
