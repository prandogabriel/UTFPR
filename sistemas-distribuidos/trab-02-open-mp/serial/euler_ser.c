#include <stdio.h>
#include <time.h>

#define H 0.000001        // precision
#define num_iteracoes 100 // number of iterations 1000

// char          ->  1 bytes --> 0 to 255
// short int     ->  2 bytes --> -32768 to 32767
// unsigned int  ->  4 bytes --> 0 to 4294967295
// int           ->  4 bytes --> -2147483648 to 2147483647
// long double   -> 16 bytes --> -1.7976931348623157E+308 to 1.7976931348623157E+308

long double equation(long double x, long double y);

int main()
{
  char a = 0; // Limite inferior de X
  char b = 5; // Limite superior de X

  unsigned int N = (b - a) / H; // Número de iterações

  // P.V.I
  long double xn = 0, xn1 = 0;
  long double zn = 0, zn1 = 0;

  clock_t begin = clock();
  for (int ite = 0; ite < num_iteracoes; ite++)
  {
    // reset P.V.I
    xn = 0, xn1 = 0;
    zn = 0, zn1 = 0;

    for (int i = 0; i < N; i++)
    {
      xn = xn1;
      zn = zn1;

      xn1 = xn + H;
      zn1 = zn + H * equation(xn, zn);
    }
  }
  clock_t end = clock();

  double time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
  printf("\nNum_Iteracoes por loop: %d \t\t Tempo Gasto: %.10lf [s]\n", N, time_spent);

  printf("[EULER] zn: %.70Lf\n", zn);
  printf("[EXATO] yx: %.70Lf\n", ((long double)b / (b * b + 1)));

  return 0;
}

long double equation(long double x, long double y)
{
  return ((1 / (1 + x * x)) - 2 * y * y);
}
