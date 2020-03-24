#include <stdio.h>
#include <stdlib.h>

int potenciaInterativa(int a, int n){
  
    int i, aux;

    if (n <= 0)
        return 1;
    else{
        aux = a;

        for (i = 2; i <= n; i++)
            aux = aux * a;

        return aux;
    }

}

int potenciaDivisaoConquista(int a, int n){
    if (n <= 0)
        return 1;
    else if (n == 1)
        return a;
    else
        if (n % 2 == 0)
            return potenciaDivisaoConquista(a, n / 2) * potenciaDivisaoConquista(a, n / 2);
        else
            return a * potenciaDivisaoConquista(a, (n - 1) / 2) * potenciaDivisaoConquista(a, (n - 1) / 2);
}

int main(){
    printf("%d elevado a %d = %d\n", 2, 8, potenciaInterativa(2, 8));
    printf("%d elevado a %d = %d\n", 2, 8, potenciaDivisaoConquista(2, 8));
    return 0;
}