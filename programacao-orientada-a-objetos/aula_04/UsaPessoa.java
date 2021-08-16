public class UsaPessoa {

  public static void main(String[] args) {
    /*
     * Criando um vetor que permite alocar ate 3 objetos da classe Pessoa
     */
    Pessoa[] agenda = new Pessoa[3];

    // Criando 2 objetos da classe Pessoa
    agenda[0] = new Pessoa("joao", "123");
    agenda[1] = new Pessoa("maria", "456");
    agenda[2] = new Pessoa("jose", "789");

    // Uma forma diferente do for para
    // percorrer vetores
    for (Pessoa p : agenda) {
      p.imprimirDados();
    }
  }
}// fim da classe
