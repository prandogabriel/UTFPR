import java.util.Scanner;

import javax.lang.model.util.ElementScanner6;

//Gravacao e Leitura de Objeto em arquivo
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Console;

public class AgendaSimploria {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        int opcao = 0;
        File arquivo = new File("agenda.dat");
        Console console = System.console();
        int i = 0;
        Pessoa[] agenda = new Pessoa[100];

        do {
            System.out.println("Incluir Pessoa Digite 1 ");
            System.out.println("Listar  Pessoas Cadastradas Digite 2 ");
            System.out.println("Excluir Pessoa Digite 3 ");
            System.out.println("Gravar Agenda em um arquivo digite 4 ");
            System.out.println("Ler uma agenda Digite 5 ");
            System.out.println("Sair Digite 6");
            opcao = teclado.nextInt();

            switch (opcao) {
            case 1:
                if (i < 100) {
                    String nome = console.readLine("Entre com o nome: ");
                    String cpf = console.readLine("Entre com o CPF: ");
                    Pessoa aux = new Pessoa(nome, cpf);
                    agenda[i] = aux;
                    i++;
                } else
                    System.out.println("Erro");
                break;
            case 2:
                for (Pessoa p : agenda) {
                    if (p != null)
                        p.imprimirDados();
                }
                break;
            case 3:
                System.out.println("indique o indice a ser Excluido: ");
                int indice = teclado.nextInt();
                if(agenda[indice]!=null){
                    agenda[indice]=null;
                }
                else
                    System.out.println("não existe essa pessoa");

                break;
            case 4:
                try {
                    FileOutputStream fout = new FileOutputStream(arquivo);
                    ObjectOutputStream oos = new ObjectOutputStream(fout);
                    // gravando o vetor de pessoas no arquivo
                    oos.writeObject(agenda);
                    oos.flush();
                    oos.close();
                    fout.close();
                } catch (Exception ex) {
                    System.err.println("erro: " + ex.toString());
                }
                break;
            case 5:
                try {
                    FileInputStream fin = new FileInputStream(arquivo);
                    ObjectInputStream oin = new ObjectInputStream(fin);
                    /*
                     * Lendo os objetos de um arquivo e fazendo a coercao de tipos
                     */

                    Pessoa[] agendaArq = (Pessoa[]) oin.readObject();
                    oin.close();
                    fin.close();

                    // Uma forma de diferente do for para percorrer vetores
                    for (Pessoa p : agendaArq) {
                        if (p != null)
                            p.imprimirDados();
                    }
                } catch (Exception ex) {
                    System.err.println("erro: " + ex.toString());
                }
                break;

            default:
                System.out.println("Opção inválida");
            }
        } while (opcao != 6);

    }
}