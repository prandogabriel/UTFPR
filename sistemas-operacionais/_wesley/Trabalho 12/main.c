#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <unistd.h>
#include <mqueue.h>
#include <time.h>
#include <math.h>

int main(){
	pid_t pid;

    pid = fork();
	if(pid > 0)
	{
		system("gcc Produtor.c -o prod -lrt");
		execl("./prod","", NULL);
	} else if (pid == 0) {

		sleep(1);
		system("gcc Consumidor.c -o cons -lpthread -lrt ");
		execl("./cons", "", NULL);
	} else {
		printf("Erro\n");
	}

	exit(0);
}
