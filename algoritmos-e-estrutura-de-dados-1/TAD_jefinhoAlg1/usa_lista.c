#include <stdio.h>
#include "lista.h"

int main()
{

  Lista *l1, *l2;

  l1 = criar();
  l2 = criar();

  inserir(l1, 1);
  inserir(l1, 2);
  inserir(l1, 3);
  inserir(l1, 4);
  inserir(l1, 5);

  imprimir_lista(l1);

  inserir(l2, 6);
  inserir(l2, 7);
  inserir(l2, 8);
  inserir(l2, 9);
  inserir(l2, 10);

  imprimir_lista(l2);

  Lista *junta = criar();

  printf("lista concatenada\n");
  
  junta = concatenar(l1, l2);

  imprimir_lista(junta);

  liberar_lista(l1);
  liberar_lista(l2);
  liberar_lista(junta);

  return 0;
}