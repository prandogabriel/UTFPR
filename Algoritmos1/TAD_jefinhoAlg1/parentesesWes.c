#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAXTAM 100

typedef struct ItemP {
  char chave;
}ItemP;

typedef struct Pilha {
  ItemP item[MAXTAM];
  int topo;
}Pilha;

Pilha * cria_pilha_vazia()
{
  Pilha *p = malloc(sizeof(Pilha));
  p->topo = -1;
  return p;
}

int verifica_pilha_cheia(Pilha *p)
{
  return p->topo == MAXTAM - 1;
}

void empilha(Pilha *p, int chave)
{
  if(verifica_pilha_cheia(p)){
    printf("Erro: a pilha está cheia.\n");
    return;
  }
  else {
    ItemP novo_item;
    novo_item.chave = chave;
    p->topo++;
    p->item[p->topo] = novo_item;
  }
}

int verifica_pilha_vazia(Pilha *p)
{
  return p->topo == -1;
}

void desempilha(Pilha *p)
{
  if (verifica_pilha_vazia(p)) {
    printf("Erro: a pilha está vazia.\n");
    return;
  }
  else {
    p->topo--;
  }
}

void libera_pilha(Pilha *p)
{
  free(p);
}

void verificaParenteses( int n)
{
  char frase[MAXTAM];
  int vetorVerdade[n];

  int i=0;
  while( i < n )
  {
    scanf("%s", &frase);
    vetorVerdade[i] = parenteses(frase);
    i++;
  }

  for (i=0;i<n;i++)
  {
  if(vetorVerdade[i]==0)
    printf("incorrect\n");
  else
    printf("correct\n");
  }
}


int parenteses (char s[])
{
  Pilha *pilha = cria_pilha_vazia();
  int n, i, t;

  n = strlen (s);
  t = 0;
  i = 0;

  for (i = 0; s[i] != '\0'; ++i) {
      switch (s[i]) {
        case ')':
          if (t != 0 && pilha->item[t-1].chave == '(') {
            --t;
            desempilha(pilha);
          }
          else return 0;
          break;
        case '(':
          t++;
          empilha(pilha, s[i]);
          break;
        default:
          break;
      }
   }
  libera_pilha(pilha);
  if (t == 0)
    return 1;
  else
    return 0;
}



int main ()
{
  int n;
  scanf("%d", &n);
  verificaParenteses(n);
}
