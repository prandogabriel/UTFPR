#include <stdio.h>

int busca_sequencial(int v[], int n, int chave) {
  int i;
  for(i = 0; i < n; i++)
    if(v[i] == chave)
      return i;
  return -1;
}

int busca_binaria(int v[], int n, int chave) {
  int p = 0;
  int r = n - 1;
  int q;
  while(p <= r) {
    q = (p + r) / 2;
    if(v[q] == chave)
      return q;
    else {
      if(v[q] > chave)
        r = q - 1;
      else
        p = q + 1;
    }
  }
  return -1;
}

int busca_binaria_rec(int v[], int p, int r, int chave) {
  if(p > r)
    return -1;
  else {
    int q = (p + r) / 2;
    if(v[q] == chave)
      return q;
    else {
      if(v[q] > chave)
        busca_binaria_rec(v, p, q - 1, chave);
      else
        busca_binaria_rec(v, q + 1, r, chave);
    }
  }
}

int main(void) {
    int v[] = {2, 4, 6, 8, 9, 20, 34, 26, 60, 90};
    // printf("%d\n", busca_sequencial(v, 10, 60));
    // printf("%d\n", busca_binaria(v, 10, 60));
    printf("%d\n", busca_binaria_rec(v, 0, 9, 60));

    return 0;
}
