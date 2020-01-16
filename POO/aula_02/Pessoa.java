public class Pessoa{

 private String nome, cpf;
 private int anoNasc;

 // metodo construtor padrao
 public Pessoa(){
	nome = ""; cpf = ""; anoNasc = 0;
 }

 // metodo construtor com 1 parametro
 public Pessoa(String no){
	nome = no; cpf = ""; anoNasc = 0;
 }

 // metodo construtor com 3 parametros
 public Pessoa(String no, String c, int a){
	nome = no; cpf = c; anoNasc = a;
 }
 
 public void imprimirDados(){
	 System.out.println("Nome: " + nome);
	 System.out.println("CPF: " + cpf);
	 System.out.println("Ano: " + anoNasc);
	 
 }	  
}// fim da classe
