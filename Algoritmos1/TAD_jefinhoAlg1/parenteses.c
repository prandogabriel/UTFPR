#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAXTAM 100

typedef struct ItemL {
  char frase[MAXTAM];
}ItemL;

typedef struct ItemP {
  char chave;
}ItemP;

typedef struct Lista {
  ItemL item[MAXTAM];
  int tam;
}Lista;


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
  Lista *l;
  int i;
  l = (Lista*) malloc (sizeof(Lista));
  l->tam = n;

  i=0;
  while( i < n )
  {
    scanf("%s", &l->item[i].frase);
    i++;
  }

  int tam;

  for (i=0;i<n;i++)
  {

  tam = parenteses(l->item[i].frase);
  if(tam==0)
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
  while (s[i] != '\0')
  {
    if(s[i] == ')')
    {
      if (t != 0 && pilha->item[t-1].chave == '(')
      {
        --t;
        desempilha(pilha);
      }
      else
        return 0;
    }
    else if(s[i] == '(')
    {
      t++;
      empilha(pilha, s[i]);
    }
    ++i;
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
