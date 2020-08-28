#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct ItemL{
    char nom[100];
}ItemL;

typedef struct Lista{
    ItemL it[100];
    int tam;
}Lista;



void criar( int n)
{
    Lista *l;
    int i;
    l = (Lista) malloc (sizeof(Lista));
    l->tam = n;

    for(i=0; i<n;i++)
    {
        scanf("%s", &l->it[i].nom);
    }
    int tam;
    for (i=0;i<n;i++)
    {

      tam = bemFormada(l->it[i].nom);
      if(tam==0)
        printf("errada");
      else
        printf("certa");
    }
}


int bemFormada (char s[])
{
   char *pilha; int t;
   int n, i;

   n = strlen (s);
   pilha = malloc(n * sizeof (char));
   t = 0;
   for (i = 0; s[i] != '\0'; ++i) {
      // a pilha está armazenada em pilha[0..t-1]
      switch (s[i]) {
         case ')': if (t != 0 && pilha[t-1] == '(')
                      --t;
                   else return 0;
                   break;
         case ']': if (t != 0 && pilha[t-1] == '[')
                      --t;
                   else return 0;
                   break;
         default:  pilha[t++] = s[i];
      }
   }
   free (pilha);
   if (t == 0) return 1;
   else return 0;
}



int main ()
{
    int n;
    scanf("%d", &n);
    criar(n);
}


