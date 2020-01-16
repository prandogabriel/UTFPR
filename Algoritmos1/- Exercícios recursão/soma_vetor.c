#include <stdio.h>

/*
S(0) = 0
S(1) = v[1 - 1] + S(0)
S(2) = v[2 - 1] + S(1)
S(3) = v[3 - 1] + S(2)
S(4) = V[4 - 1] + S(3)
*/
int soma_rec(int n, int v[]) {
  if(n == 0) 
	  return 0;
  else
    return v[n - 1] + soma_rec(n - 1, v);
}

main() {
  int v[] = {1, 2, 10, 5};
  printf("Soma dos elementos do vetor: %d\n", soma_rec(4, v));

}
