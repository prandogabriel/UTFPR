#include <stdio.h>

int maior(int n, int v[]) {
    int maior = 0, i;
    for(i = 0; i < n; i++)
        if(v[i] > maior)
            maior = v[i];
    return maior;
}

int maior_rec(int n, int v[]){
    if(n == 1) // considerando somente a primeira posição do vetor, o maior é o primeiro
        return v[0];
    else {
        int maior;
        maior = maior_rec(n - 1, v);
        if(maior > v[n - 1])
            return maior;
        else
            return v[n - 1];
    }
}

main() {
  int v[] = {77, 88, 66, 99};
  printf("Maior elemento do vetor: %d\n", maior(4, v));
  printf("Maior elemento do vetor: %d\n", maior_rec(4, v));
}
