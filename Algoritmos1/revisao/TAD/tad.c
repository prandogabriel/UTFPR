#include <stdio.h>
#include <stdlib.h>
#include "tad.h"

struct item
{
  int chave;
};

Item *criaItem(int y)
{
  Item *x = malloc(sizeof(Item));
  x->chave = y;
  return x;
}

void mudaChave(Item *it)
{
  it->chave = 5;
}

void printaItem(Item *it)
{
  int aux = it->chave;
  printf("%d", &aux);
}