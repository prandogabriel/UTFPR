public class UsaPessoa{

	public static void main(String[] Args){
		Pessoa Ana = new Pessoa();
		Pessoa Bete = new Pessoa("Bete");
		Pessoa Carlos = new Pessoa("Carlos", "333.444.555-66",1960);
		
		Ana.imprimirDados();
		Bete.imprimirDados();
		Carlos.imprimirDados();
	}
}