#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <omp.h>

#define ANSI_COLOR_GREEN   "\x1b[32m"
#define ANSI_COLOR_BLUE    "\x1b[34m"

static int turno_d = 0;
static int flag_d[2] = {0, 0};

static int turno_p = 0;
static int flag_p[2] = {0, 0};

static int local_k[2] = {0,0};
static int b_k[2] = {0,0};
static int turno_k[2] = {0,1};


/*Rutina que se encarga de ejecutar la seccion critica*/
void seccion_critica()
{
	int id_hilo = omp_get_thread_num();
	if (id_hilo){
		printf(ANSI_COLOR_GREEN "%d: Ya entre \n \033[0m"  ,id_hilo);
	}else{
		printf(ANSI_COLOR_BLUE "%d: Ya entre \n \033[0m"  ,id_hilo);
	}
	int i = 0;
	//Hace la suma de los primeros 1000 naturales
	while(i<1000){
		i = i + 1;
	}
	if (id_hilo){
		printf(ANSI_COLOR_GREEN "%d: Ya sali \n \033[0m"  ,id_hilo);
	}else{
		printf(ANSI_COLOR_BLUE "%d: Ya sali \n \033[0m"  ,id_hilo);
	}
}

/*Rutina que representa el algoritmo de Dekker*/
void proceso_hilo_dekker()
{
	int id = omp_get_thread_num();
	//Levantamos la bandera de que esta por
	//entrar en sus seccion critica
	flag_d[id] = 1;
	int otro = 1-id;
	//Mientras la bandera del otro esta levantada
	while(flag_d[otro])
	{
		//Si es el turno del otro le damos la
		//oportunidad de salir del ciclo y ejecutar
		//la seccion critica
		if(turno_d == otro)
		{
			flag_d[id] = 0;
			while(turno_d != id){}
			//Cuando es su turno vuelve a levantar su
			//bandera
			flag_d[id] = 1;
		}
	}
	seccion_critica();
	//Le damos el turno al otro hilo
	turno_d = otro;
	//Bajamos la bandera
	flag_d[id] = 0;
}

/*Rutina que representa el algoritmo de Peterson */
void proceso_hilo_petterson()
{
	int id = omp_get_thread_num();
	int otro = (id + 1)%2;
	//Marcamos que va a entrar a la seccion critica
	flag_p[id] = 1;
	turno_p = otro;
	//Espera si no es su turno y la bandera del otro hilo esta levantada
	while(flag_p[otro] && turno_p == otro){}
	seccion_critica();
	//Bajamos la bandera
	flag_p[id] = 0;
}

int main(int argc, char** argv)
{
	omp_set_num_threads(2);
	printf("Proceso con Dekker \n");
	#pragma omp parallel
	{
		int j = 0;
		while(j<20){
			proceso_hilo_dekker();
			j += 1;
		}
	}
	omp_set_num_threads(2);
	printf("Proceso con Petterson \n");
	#pragma omp parallel
	{
		int j = 0;
		while(j<20){
			proceso_hilo_petterson();
			j += 1;
		}
	}
	return 0;
}
