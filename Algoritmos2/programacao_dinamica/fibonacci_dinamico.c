#include <stdio.h>
#include <stdlib.h>
#define MAX 100
#define inf 2147483647

int f[MAX];

int fibPD(int n){
  if (f[n] > -1)
    return f[n];
  else if (n == 0)
    return f[n] = 0;
  else if (n == 1)
    return f[n] = 1;
  else
    return f[n] = fibPD(n - 1) + fibPD(n - 2);
}

int main(){
    int n;

    int i = 0;

    for (i = 0; i < MAX; i++)
      f[i] = -1;

    scanf("%d",&n);

    printf("%d\n", fibPD(n));
    return 0;
}
