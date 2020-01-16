import java.util.Scanner;

public class Simulador {

	private static final int max = 20;
	private static Veiculo[] veiculos = new Veiculo[max];
	private static int IdVe;


public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in); // Funcao para leitura de dados
		String aux;
		char tipoVeiculo;
		float comb; //combustivel
		int pneu;

		char opcao; // Variavel para selecionar as opções do simulador

		do { // Inciando MENU de Opções (interativo)
			System.out.println("(A) Incluir veiculo");
			System.out.println("(B) Remover um veiculo");
			System.out.println("(C) Abastecer veiculo");
			System.out.println("(D) Movimentar veiculo");
			System.out.println("(E) Movimentar veiculos por tipo");
			System.out.println("(F) Imprimir todos os dados de todos os veículos");
			System.out.println("(G) Imprimir dados de veículo por tipo");
			System.out.println("(H) Esvaziar um pneu específico");
			System.out.println("(I) Calibrar um pneu específico");
			System.out.println("(J) Calibrar todos os pneus por tipo veiculo");
			System.out.println("(K) Imprimir pista de corrida");
			System.out.println("(L) Sair da aplicacao");

			System.out.print("Opcao: ");
			aux = teclado.next();
			opcao = aux.charAt(0); // convertendo string para char

			switch (opcao) {
			// Incluir veiculo
			case 'A': {

					System.out.println("Bicicleta (B) | Motocicleta (M) | Carro Popular (C) | Ferrari (F)");
					System.out.println("Informe o tipo de veiculo para ser adicionado: ");

					tipoVeiculo = teclado.next().charAt(0);
					System.out.println("Informe o ID do veiculo:");
					IdVe = teclado.nextInt();
					if(valida(IdVe))  adicionaVeiculo(IdVe,tipoVeiculo);
					else System.out.println("Ja existe ID");
				break;
			}

			// Remover um veiculo
			case 'B': {
					System.out.println("Informe o ID do veiculo que deseja remover:");
					IdVe = teclado.nextInt();
					removeV(IdVe);

					break;
			}

			// Abastecer veiculo
			case 'C': {
				System.out.println("Informe o ID do veiculo que deseja abastecer:");
				IdVe = teclado.nextInt();
				System.out.println("Informe a quantidade de litros que deseja repor:");
				comb = teclado.nextFloat();
				abasteceV(IdVe,comb);

				break;
			}
			// Movimentar veiculo
			case 'D': {
				System.out.println("Informe o ID do veiculo que deseja mover:");
				IdVe = teclado.nextInt();
				moveV(IdVe);

				break;
			}
			// Movimentar veiculos por tipo
			case 'E': {
				System.out.println("Bicicleta (B) | Motocicleta (M) | Carro Popular (C) | Ferrari (F)");
				System.out.println("Informe o tipo de veiculo a ser movido: ");
				tipoVeiculo = teclado.next().charAt(0);
				moveTipo(tipoVeiculo);

				break;
			}
			// Imprimir todos os dados de todos os veículos
			case 'F': {
				imprimeinfo();

				break;
			}
			// Imprimir dados de veículo por tipo
			case 'G': {
				System.out.println("Bicicleta (B) | Motocicleta (M) | Carro Popular (C) | Ferrari (F)");
				System.out.println("Informe o tipo de veiculo: ");
				tipoVeiculo = teclado.next().charAt(0);
				imprimeTipo(tipoVeiculo);

				break;
			}
			// Esvaziar um pneu específico
			case 'H': {
				System.out.println("Informe o ID do veiculo:");
				IdVe = teclado.nextInt();
				System.out.println("Informe a posição do pneu:");
				pneu = teclado.nextInt();
				esvaziaEsp(IdVe,pneu);

				break;
			}
			// Calibrar um pneu específico
			case 'I': {
				System.out.println("Informe o ID do veiculo:");
				IdVe = teclado.nextInt();
				System.out.println("Informe a posição do pneu:");
				pneu = teclado.nextInt();
				calibraEsp(IdVe,pneu);
				break;
			}
			// Calibrar todos os pneus por tipo veiculo
			case 'J': {
				System.out.println("Bicicleta (B) | Motocicleta (M) | Carro Popular (C) | Ferrari (F)");
				System.out.println("Informe o tipo de veiculo a ser calibrado os pneus: ");
				tipoVeiculo = teclado.next().charAt(0);
				calibraTipo(tipoVeiculo);
				break;
			}
			// Imprimir pista de corrida
			case 'K': {
				ImprimirPista();

				break;
			}
			// Sair da aplicacao
			case 'L': {

				break;
			}
			// caso opção for diferente do intervalo [a-j]
			default: {
				System.out.println("Opcao invalida!");
			}
			}
			System.out.println("");
		} while (opcao != 'L');
	}


public static void adicionaVeiculo(int ID, char tipo) {

	int i=0;
	while(veiculos[i] != null && i<max) { // vai percorrer o vetor ocupados até achar um false ... o tipo + (indice + 1) desse vetor sera o NICK do Veiculo
		i++;
	}

	if(i==max){

	}

	switch(tipo) {
		case 'B': // CRIANDO OBJETO Bicicleta
		{
			veiculos[i] = new Bicicleta(ID,0);
			break;
		}

		case 'M': // CRIANDO OBJETO Motocicleta
		{
			veiculos[i] = new Motocicleta(ID,0);
			break;
		}

		case 'C': // CRIANDO OBJETO CarroPopular
		{
			veiculos[i] = new CarroPopular(ID,0);
			 break;
		}

		case 'F': // CRIANDO OBJETO Ferrari
		{
			veiculos[i] = new Ferrari(ID,0);
			break;
		}
	}
}

public static  void removeV(int ID){

	int i=0;

	while(veiculos[i].getID()!=ID && i < max){
		i++;
	}

	if(i==max){
		System.out.println("O id não foi encontrado e portanto nenhum veículo removido");
	}
	else{
		veiculos[i] = null;
		System.out.println("Veiculo removido com sucesso");
	}
}

public static void ImprimirPista(){

		for(int i = 0; i<veiculos.length; i++){
			if(veiculos[i]!=null){
				veiculos[i].desenhar();
			}
		}
}

//É pra funcionar
public static void calibraTipo(char tipo){


	for(int i = 0; i < veiculos.length; i++){
		if(veiculos[i]!=null){
			if(tipo == 'B'){
				if(veiculos[i] instanceof Bicicleta){
					veiculos[i].calibraALL();
				}
			}
			else if (tipo == 'F'){
				if(veiculos[i] instanceof Ferrari){
					veiculos[i].calibraALL();
				}
			}
			else if(tipo == 'M'){
				if(veiculos[i] instanceof Motocicleta){
					veiculos[i].calibraALL();
				}
			}
			else{
				if(veiculos[i] instanceof CarroPopular){
					veiculos[i].calibraALL();
				}
			}

		}
		}

}
//Deve funcionar
public static void calibraEsp(int ID, int n_pneu){
	int i=0;

	while(veiculos[i].getID()!=ID && i < max){
		i++;
	}

	if(i==max){
		System.out.println("O id não foi encontrado");
	}
	else{
		if(veiculos[i]!=null){
			veiculos[i].calibra(n_pneu);
			System.out.println("Calibrado com sucesso");
		}

	}
}
//Deve funcionar
public static void esvaziaEsp(int ID, int n_pneu){
	int i=0;

	while(veiculos[i].getID()!=ID && i < max){
		i++;
	}
	if(i==max){
		System.out.println("O id não foi encontrado");
	}
	else{
		veiculos[i].descalibrar(n_pneu);
		System.out.println("Descalibrado com sucesso");
	}
}
//Ta vazio
public static void imprimeTipo(char tipo){
	for(int i = 0; i < veiculos.length; i++){
		if(veiculos[i]!=null){
			//System.out.println("Não é nulo");
			if(tipo == 'B'){
				//System.out.println("Ele sabe o tipo");
				if(veiculos[i] instanceof Bicicleta){
					//System.out.println("É instancia");
					System.out.println(((Bicicleta)veiculos[i]).toString());
				}
			}
			else if (tipo == 'F'){
				if(veiculos[i] instanceof Ferrari){
					System.out.println(((Ferrari)veiculos[i]).toString());
				}
			}
			else if(tipo == 'M'){
				if(veiculos[i] instanceof Motocicleta){
					System.out.println(((Motocicleta)veiculos[i]).toString());
				}
			}
			else {
				if(veiculos[i] instanceof CarroPopular){
					System.out.println(((CarroPopular)veiculos[i]).toString());
				}
			}
		}
	}
}
//Também ta vazio
public static void imprimeinfo(){
	for(int i = 0; i < veiculos.length; i++){
		if(veiculos[i]!=null){
				if(veiculos[i] instanceof Bicicleta){
					System.out.println(((Bicicleta)veiculos[i]).toString());
				}
				else if(veiculos[i] instanceof Ferrari){
					System.out.println(((Ferrari)veiculos[i]).toString());
				}
			else if(veiculos[i] instanceof Motocicleta){
					System.out.println(((Motocicleta)veiculos[i]).toString());
				}
			else if(veiculos[i] instanceof CarroPopular){
					System.out.println(((CarroPopular)veiculos[i]).toString());
				}
		}
	}
}
//Se pa funciona
public static void moveTipo(char tipo){
	for(int i = 0; i < veiculos.length; i++){
		if(veiculos[i]!=null){
			if(tipo == 'B'){
				if(veiculos[i] instanceof Bicicleta){
					veiculos[i].mover();
				}
			}
			else if (tipo == 'F'){
				if(veiculos[i] instanceof Ferrari){
					veiculos[i].mover();
				}
			}
			else if(tipo == 'M'){
				if(veiculos[i] instanceof Motocicleta){
					veiculos[i].mover();
				}
			}
			else{
				if(veiculos[i] instanceof CarroPopular){
					veiculos[i].mover();
				}
			}
		}
	}
}
//É pra funcionar
public static void moveV(int ID){
	int i=0;

	while(veiculos[i].getID()!=ID && i < max){
		i++;
	}

	if(i==max){
		System.out.println("O id não foi encontrado");
	}
	else{
		veiculos[i].mover();
	}
}
//Junges salvou
public static void abasteceV(int ID, float litros){
	int i=0;

	while(veiculos[i].getID()!=ID && i < max){
		i++;
	}

	if(i==max){
		System.out.println("O id não foi encontrado");
	}
	else{
		if(veiculos[i] instanceof Veiculo_Motorizado){

			((Veiculo_Motorizado) veiculos[i]).abastecer(litros);
			System.out.println ("Veiculo abastecido com sucesso");
		}
	}

}

public static boolean valida(int ID){
	for(int i = 0; i <veiculos.length; i++){
		if(veiculos[i]!=null){
			if(veiculos[i].getID() == ID) return false;
		}
	}
	return true;
}
}
