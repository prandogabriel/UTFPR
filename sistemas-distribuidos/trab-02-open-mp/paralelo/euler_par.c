#include <stdio.h>
#include <omp.h>
#include <time.h>

#define H 0.000001        // precision
#define num_iteracoes 100 // number of iterations
#define num_threads 4     // number of threads

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

  unsigned int num_iteracoes_thread = num_iteracoes / num_threads;
  printf("num_iteracoes_total: %d\nnum_threads: %d\nnum_iteracoes_threads: %d\n", num_iteracoes, num_threads, num_iteracoes_thread);

  double time_threads[num_threads];
  long double result = 0; // Resultado final

  omp_set_num_threads(num_threads);
#pragma omp parallel for
  for (char threads = 0; threads < num_threads; threads++)
  {
    char thread_id = omp_get_thread_num();

    // P.V.I
    long double xn = 0, xn1 = 0;
    long double zn = 0, zn1 = 0;

    clock_t begin = clock();
    for (int ite = thread_id * num_iteracoes_thread; ite < (thread_id + 1) * num_iteracoes_thread; ite++)
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
      result = zn;
    }
    clock_t end = clock();
    time_threads[thread_id] = (double)(end - begin) / (CLOCKS_PER_SEC * num_threads);
  }

  printf("[THREADS]   [TIME]\n");
  double max_time = -1;
  for (char i = 0; i < num_threads; i++)
  {
    printf("Thread %d   %f\n", i, time_threads[i]);
    if (time_threads[i] > max_time)
    {
      max_time = time_threads[i];
    }
  }

  printf("\nNum_Iteracoes por loop: %d \t\t Tempo Gasto: %.10lf [s]\n", N, max_time);

  printf("[EULER] zn: %.70Lf\n", result);
  printf("[EXATO] yx: %.70Lf\n", ((long double)b / (b * b + 1)));

  return 0;
}

long double equation(long double x, long double y)
{
  return ((1 / (1 + x * x)) - 2 * y * y);
}
