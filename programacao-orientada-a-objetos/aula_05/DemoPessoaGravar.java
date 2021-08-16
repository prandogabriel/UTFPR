//Gravacao e Leitura de Objeto em arquivo
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class DemoPessoaGravar{

public static void main(String[] args) {
 /*Criando um vetor que permite alocar ate
    3 objetos da classe Pessoa */
 Pessoa[] agenda = new Pessoa[3];

 //Criando 2 objetos da classe Pessoa
  agenda[0] = new Pessoa("Madalena","123");
  agenda[1] = new Pessoa("Jose","456");
  agenda[2] = new Pessoa("Pedro","789");

 /*Gravar em arquivo*/
  File arquivo = new File("agenda.dat");
  try {
   FileOutputStream fout = new FileOutputStream(arquivo);
   ObjectOutputStream oos = new ObjectOutputStream(fout);
   // gravando o vetor de pessoas no arquivo
   oos.writeObject(agenda);
   oos.flush();
   oos.close();
   fout.close();
  }
  catch (Exception ex) {
   System.err.println("erro: " + ex.toString());
  }
 }
}//fim da classe



