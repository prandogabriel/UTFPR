#include <stdio.h>

long euclides_rec(long m, long n) {
  if(n == 0)
    return m;
  printf("euclides_rec(%ld, %ld)\n", n, m % n);
  return euclides_rec(n, m % n);
}

main() {
  long m, n, mdc;
  scanf("%ld %ld", &m, &n);
  printf("euclides_rec(%ld, %ld)\n", m, n);
  mdc = euclides_rec(m, n);
  printf("MDC: %ld\n", mdc);
}
