/*
Escolha 2 séries quaisquer para aproximar o número pi. Veja exemplos sequenciais em Pi sequencial. Implemente a
versão paralela dessas séries, ulizando pthreads, sequindo os seguintes requisitos:
* Devem ser calculados pelo menos 1 bilhão (10^9) de termos de cada série.
* Use variáveis reais de dupla precisão (double) nos cálculos;
* O programa deve dividir o espaço de cálculo uniformemente entre as N threads
    - e.x. 1 bilhao de termos com 2 threads = 500 milhoes de termos em cada thread;
    - cada thread efetua uma soma parcial de forma autônoma;
    - Para evitar o uso de mecanismos de sincronização, cada thread T[i] deve depositar seu resultado parcial na posição result[i] de um vetor de resultados parciais.
* Após o término das threads de cálculo, o programa principal soma os resultados
parciais obdos por elas e apresenta o resultado final na tela;
* Execute as threads no seu computador pessoal
* Execute as soluções com N = {1, 2, 4, 8 e 16} threads
* Marque o tempo necessário para calcular Pi para cada N e faça um gráfico de linhas (NxTempo)
 apresentado os resultados obtidos
* Compare o resultado das duas soluções (series) escolhidas, indicando qual série é mais eficiente
 em termos de tempo e qualidade da solução (i.e. valor mais exato de pi)
*/
#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<math.h>
#include<pthread.h>
#include<time.h>

#define N_THREADS 16
#define N_TERMOS_SERIE 1000000000

double parc[8*N_THREADS];
double result = 0.0;
int j=1;

void* calcula_pi_1(void* i){

    long tid = (long)i;
    int termos = N_TERMOS_SERIE/(N_THREADS*j);
    int inicio = tid*termos;
    int fim = tid*termos + termos;

	for(int i = inicio; i < fim; i++){
        parc[8*tid] += 4*((pow(-1,i)/ (2*i+1))); //Serie 1
    }
    return 0;
}


void* calcula_pi_2(void* i){

    long tid = (long)i;
    int termos = N_TERMOS_SERIE/(N_THREADS*j);
    int inicio = tid*termos;
    int fim = tid*termos + termos;
    int numerador = -1/3;
	  for(int i = inicio; i < fim; i++){
        parc[8*tid] += sqrt(12)*(pow(-1,i)/ ((2*i+1)*pow(3,i)));//(1/pow(16,i)) *((4/(8*i+1)) - (2/(8*i+4)) - (1/(8*i+5)) - (1/(8*i+6))); //
    }
    return 0;
}


int main(void){

    pthread_t threads[N_THREADS];
    int i;
    FILE *out1 = fopen("./trabalho-3-plot/out_1.txt","w+");

    for(j=1; j<=N_THREADS; j*=2){
        result = 0.0;
        for(i = 0; i < N_THREADS; i++)
        parc[8*i] = 0;

        struct timespec t1, t2;

        clock_gettime(CLOCK_MONOTONIC, &t1);
        for(i = 0; i < N_THREADS; i++)
            pthread_create(&threads[i], NULL, calcula_pi_1, (void*)(intptr_t)i);

        for(i = 0; i < N_THREADS; i ++){

            pthread_join(threads[i], NULL);
            result += parc[8*i];

        }
        clock_gettime(CLOCK_MONOTONIC, &t2);
        double temp = (double)(t2.tv_sec - t1.tv_sec) + (t2.tv_nsec - t1.tv_nsec)/1E9;
        printf("N threads: %d\tValor de pi: %.50lf\tTempo: %.3f\n", j, (double)result, temp);
        fprintf(out1,"%d %.4f\n",j,temp);
    }
    fclose(out1);


    // Segunda série

    FILE *out2 = fopen("./trabalho-3-plot/out_2.txt","w+");

    for(j=1; j<=N_THREADS; j*=2){
        result = 0.0;
        for(i = 0; i < N_THREADS; i++)
        parc[8*i] = 0;

        struct timespec t1, t2;

        clock_gettime(CLOCK_MONOTONIC, &t1);
        for(i = 0; i < N_THREADS; i++)
            pthread_create(&threads[i], NULL, calcula_pi_2, (void*)(intptr_t)i);

        for(i = 0; i < N_THREADS; i ++){

            pthread_join(threads[i], NULL);
            result += parc[8*i];

        }
        clock_gettime(CLOCK_MONOTONIC, &t2);
        double temp = (double)(t2.tv_sec - t1.tv_sec) + (t2.tv_nsec - t1.tv_nsec)/1E9;
        printf("N threads: %d\tValor de pi: %.50lf\tTempo: %.3f\n", j, (double)result, temp);
        fprintf(out2,"%d %.4f\n",j,temp);
    }
    fclose(out2);
}
