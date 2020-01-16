#include <stdio.h>

static int troca(int *a, int *b){
    int t = *a;
    *a = *b;
    *b = t;
}

static int separa(int v[], int primeiro, int ultimo) {
   int pivo = v[ultimo];
   int esquerda = primeiro;
   int atual;
   for(atual = primeiro; atual < ultimo; atual++) { 
      if (v[atual] <= pivo) { 
         troca(&v[atual], &v[esquerda]);
         esquerda++;
      }
   }
   troca(&v[esquerda], &v[ultimo]);
   return esquerda;
}

void quick_sort(int v[], int p, int u) {
   if(p < u) {
      int j = separa(v, p, u);
      quick_sort(v, p, j - 1);
      quick_sort(v, j + 1, u);
   }
   return;
}


main() {
  int n = 10;
  int v[] = {2, 90, 5, 4, 60, 1, 19, 22, 35, 15};
  quick_sort(v, 0, 9);
  for(int i = 0; i < n; i++) {
    printf("%d\t", v[i]);
  }
}
