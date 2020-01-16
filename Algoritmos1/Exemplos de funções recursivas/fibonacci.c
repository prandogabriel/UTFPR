#include <stdio.h>

int fib(int x){
    int i;
    int primeiro = 0, segundo = 1, proximo;
    for(i = 0; i < x-2; i++) { // os dois primeiros são fixos (0 e 1)
        proximo = primeiro + segundo;
        primeiro = segundo;
        segundo = proximo;
    }
    return proximo;
}

int fib_rec(int x){
    if(x == 0)
        return 0;
    if(x == 1)
        return 1;
    else
        return fib_rec(x - 1) + fib_rec(x - 2);
}

main() {
  printf("%do termo de Fibonacci: %d\n", 7, fib(7));
  printf("%do termo de Fibonacci: %d\n", 7, fib_rec(7));
}
