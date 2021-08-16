// Solução do exercício da potência.
#include <stdio.h>

// não recursiva
int potencia(int b, int e) {
  int resultado = b;
  int i;
  for(i = 0; i < e - 1; i++)
    resultado *= b;
  return resultado;
}

/* recursiva
Considerando o exemplo 3^4, o problema pode ser quebrado
em pedaços cada vez menores:
3^4 = 3 * 3^3
3^3 = 3 * 3^2
3^2 = 3 * 3^1
3^1 = 3
*/
int potencia_rec(int b, int e) {
  if(e == 1)
    return b;
  else
    return b * potencia_rec(b, e - 1);
}

main() {
  int b = 3;
  int e = 4;
  printf("%d^%d = %d\n", b, e, potencia(b, e));
  printf("%d^%d = %d\n", b, e, potencia_rec(b, e));
}
