#include<stdio.h>
#include<stdlib.h>
#define CLOCKS_PER_SEC 1000000

void troca_2(int vet[] ,int menor, int i){
    int aux;
    aux= vet[i];
    vet[i]=vet[menor];
    vet[menor]=aux;
}

void selecao(int v[], int n){
    int menor, i, j,aux;
    for(i=0;i<n-1;i++){
        menor= i;
        for(j=i+1;j<n;j++){
            if(v[j]<v[menor]){
                menor=j;
            }
            
        }
        troca_2(v, menor, i);
        
    }
}
void imprime(int v[], int n){
    int i;
        for(i=0;i<n;i++)
        printf("%d ", v[i]);
    printf("\n");    

}
void insercao(int vet[],int n)
{
    int i,j,atual;
    for(i=1;i<n;i++)
    {
        atual = vet[i];
        j=i-1;
        while(j>=0 && vet[j]>atual)
        {
            vet[j+1] = vet[j];
            j--;
        }
        vet[j+1] = atual;
    }
}

void bolha(int a[], int n)
{
 int i, j, tmp;
 
  for(i = 0; i < n; i++)
  {
   for(j = i+1; j < n; j++)
   {
    if(a[j] < a[i])
    {
     tmp = a[i];
     a[i] = a[j];
     a[j] = tmp;
    }
  }
 }
}

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
