#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <stdint.h>

#define NUM_THREADS 2

long saldo=0;

int turn = 0; // indica de quem éa vez
int wants[2] = {0, 0}; // indica se a tarefa i quer acessar a seção crítica

void enter(int task){ // task pode valer 0 ou 1
	int other = 1-task; // indica a outra tarefa
	wants[task] = 1; // quando 2 processos chamam enter() ao mesmo tempo ambos indicarão seu interesse colocando a respectiva entrada no vetor wants(TRUE)
	turn = task; // um dos processos deixa a vez (pois altera esta variável por último) e portanto habilita o outro processo a continuar
	while((turn==task)&&wants[other]){ // espera ocupada
	}
}

void leave(int task){
	wants[task] = 0; // task libera a seção crítica
}

void* depositar(void* v){
	int tid = (int)(long)v;

	for(int i=0;i<10000;i++){
		enter(tid);
		saldo += 1;
		leave(tid);
	}
}

int main(){
	pthread_t thread[NUM_THREADS] ;

	for(int i=0;i<NUM_THREADS;i++)
		pthread_create(&thread[i], NULL, &depositar, (void*)(intptr_t)i);

	for(int i=0; i<NUM_THREADS; i++)
		pthread_join (thread[i], NULL);

	printf("Final value of c %ld \n", saldo);

	exit(0);
}


