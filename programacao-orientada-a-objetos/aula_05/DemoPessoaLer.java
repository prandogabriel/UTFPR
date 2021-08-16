//Gravacao e Leitura de Objeto em arquivo
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class DemoPessoaLer{

 public static void main(String[] args) {
  
  File arquivo = new File("agenda.dat");
  
  try {
   FileInputStream fin = new FileInputStream(arquivo);
   ObjectInputStream oin = new ObjectInputStream(fin);
  /*Lendo os objetos de um arquivo e fazendo a
   coercao de tipos*/
   
   Pessoa[] agendaArq = (Pessoa[]) oin.readObject();
   oin.close();
   fin.close();
  
  //Uma forma de diferente do for para percorrer vetores
   for (Pessoa p : agendaArq) {
	 p.imprimirDados();
   }
  }catch (Exception ex) {
    System.err.println("erro: " + ex.toString());
  }
 } 
}//fim da classe