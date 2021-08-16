#include <stdio.h>

void merge(int arr[], int p, int q, int r) {
    int n1 = q - p + 1; // tamanho da metade esquerda
    int n2 =  r - q; // tamanho da metade direita
    // vetores temporários
    int L[n1], R[n2];
    // varíaveis para percorrer L[], R[] e arr[]
    int i, j, k;

    // valorização dos vetores temporários
    for (i = 0; i < n1; i++)
        L[i] = arr[p + i];
    for (j = 0; j < n2; j++)
        R[j] = arr[q + j + 1];

    // intercala os itens de L[] e R[] em arr[]
    i = 0; // indice inicial de L[]
    j = 0; // indice inicial de R[]
    k = p; // indice inicial do vetor final (arr[])
    while (i < n1 && j < n2) {
        // pegar o menor dos dois e colocar em arr[]
        if (L[i] <= R[j]) {
            arr[k] = L[i];
            i++;
        }
        else {
            arr[k] = R[j];
            j++;
        }
        k++;
    }

    // copiar os itens restantes de L[], caso tenha sobrado algum
    while (i < n1) {
        arr[k] = L[i];
        i++;
        k++;
    }

    // copiar os itens restantes de R[], caso tenha sobrado algum
    while (j < n2) {
        arr[k] = R[j];
        j++;
        k++;
    }
}

void merge_sort(int arr[], int p, int r) {
  int q;
  if(p < r) {
    q = (p + r) / 2;
    merge_sort(arr, p, q); // metade esquerda
    merge_sort(arr, q + 1, r); // metade_direita
    merge(arr, p, q, r); // intercala
  }
  return;
}

main() {
  int n = 10;
  int v[] = {2, 90, 5, 4, 60, 1, 19, 22, 35, 15};
  merge_sort(v, 0, 9);
  for(int i = 0; i < n; i++) {
    printf("%d\t", v[i]);
  }
}
