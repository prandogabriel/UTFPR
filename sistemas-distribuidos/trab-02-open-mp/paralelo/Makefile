all: parallel

parallel:
	gcc euler_par.c -o euler_par -fopenmp
	./euler_par

clean:
	@echo "Limpando projeto"
	@rm -f euler_par *.o

.PHONY: clean