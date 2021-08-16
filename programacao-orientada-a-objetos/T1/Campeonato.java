import java.util.Scanner;

//import sun.swing.PrintColorUIResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Campeonato {

    public static void main(String[] args) {
        int qtdJogadores = 0; // contador para auxiliar ao adicionar e remover jogadores
        int i, j, k, x; // auxiliares para repetição e para entrada de dados
        Jogador[] jogadores = new Jogador[5]; // vetor de jogadores, limite de jogadore é de 5
        int[][] cartela = new int[13][5]; // cartela para marcar pontuação dos jogadores, 13 é o numero de rodadas, e 5
                                          // o numero máximo de jogadores
        Scanner teclado = new Scanner(System.in); // classe scanner para ler entrada de dados para o jogo
        int opcao = 0; // auxiliar para opções do jogo
        File arquivo = new File("jogo.dat"); // classe para gravar dados em um arquivo, e para ler

        do {

            System.out.println("");

            System.out.println("Incluir Jogador Digite 1 ");
            System.out.println("Excluir Pessoa Digite 2 ");
            System.out.println("Iniciar Campeonato Digite 3 ");
            System.out.println("Mostrar Cartela Digite 4 ");
            System.out.println("Gravar em um arquivo Digite 5 ");
            System.out.println("Ler de um Arquivo Digite 6");
            System.out.println("Sair Digite 7 ");
            System.out.print("Entre com a Opção ");

            opcao = teclado.nextInt();

            switch (opcao) {
            case 1:
                System.out.print("Nome do Jogador(a): ");
                String nome;
                nome = teclado.next();
                Jogador jogAux = new Jogador(nome);
                qtdJogadores = incluirJogador(jogAux, jogadores, qtdJogadores);

                break;
            case 2:
                // mostrar todos os jogadores, para saber quais são os nomes possíveis a ser
                // removido
                motraTodosJogadores(jogadores);
                System.out.print("Escolha o nome do jogador(a) a ser removido: ");
                String nomeRemover;
                nomeRemover = teclado.next();
                qtdJogadores = removerJogador(nomeRemover, jogadores, qtdJogadores, cartela);

                break;

            case 3:
                // iniciar um campeonato
                if (qtdJogadores != 0) {
                    System.out.println("comecando!");
                    zeraCartela(cartela);
                    for (i = 0; i < qtdJogadores; i++)
                        jogadores[i].zeraJogadas();
                    if (jogadores[0] != null) {
                        for (j = 0; j < 13; j++) {
                            for (k = 0; k < qtdJogadores; k++) {
                                System.out.println(
                                        "Rodada: " + (j + 1) + "\n rolando dados para " + jogadores[k].getNome());
                                // rolar dados para o jogador do indice k
                                jogadores[k].jogarDados();
                                System.out
                                        .println("para qual jogada deseja marcar: [1 - 13]  " + jogadores[k].getNome());
                                System.out.println("1\t2\t3\t4\t5\t6\t7(T)\t8(Q)\t9(F)\t10(S+)\t11(S-)\t12(G)\t13(X)");
                                // passar a cartela e o k, que seria o jogador, para mostrar quais jogadas
                                // ele já efetuou
                                mostraJogadasEfetuadas(cartela, k);

                                do {
                                    // numero da jogada a ser pontuada
                                    x = teclado.nextInt();
                                    if (x > 13 || x < 1)
                                        System.out.println("Não existe essa jogada !!\n Informe entre 1-13 !");
                                    else {

                                        // x-1 pq o vetor começa em 0-12 e para o usuário começa em 1-13
                                        if (jogadores[k].retornaValorJogada(x - 1) == 0) {
                                            // se o retorno do escolhe jogada == 0 é pq a jogada é inválida
                                            if (jogadores[k].escolherJogada(x) == 0)
                                                System.out.println(
                                                        "seus valores não cumprem o requisito para esta jogada!");
                                            else {
                                                cartela[x - 1][k] = jogadores[k].escolherJogada(x);
                                                jogadores[k].pontuaJogadas(x);
                                            }
                                        } else
                                            System.out.println("jogada já efetuada !!");
                                    }
                                } while (x > 13 || x < 1);

                            }
                        }
                    }

                } else
                    System.out.println("impossivel iniciar campeonato sem jodadores !");

                break;
            case 4:
                mostrarCartela(cartela, qtdJogadores, jogadores);
                break;
            case 5:
                System.out.println("");
                try {
                    FileOutputStream fout = new FileOutputStream(arquivo);
                    ObjectOutputStream oos = new ObjectOutputStream(fout);

                    oos.writeObject(jogadores);
                    oos.flush();
                    oos.close();
                    fout.close();
                    System.out.println("Arquivo campeonato Gravado com sucesso !!!");
                } catch (Exception ex) {
                    System.err.println("erro: " + ex.toString());

                }

                break;
            case 6:

                System.out.println("");
                try {
                    FileInputStream fin = new FileInputStream(arquivo);
                    ObjectInputStream oin = new ObjectInputStream(fin);

                    jogadores = (Jogador[]) oin.readObject();
                    oin.close();
                    fin.close();

                    i = 0;
                    for (Jogador p : jogadores) {
                        if (p != null) {
                            i++;
                        }
                    }
                    qtdJogadores = i;
                    // recuperando valores da cartela
                    recuperaCartela(cartela, qtdJogadores, jogadores);
                    System.out.println("Leitura dos dados feita com sucesso !!!");
                } catch (Exception ex) {
                    System.err.println("erro: " + ex.toString());
                }
                break;

            default:
                if (opcao != 7)
                    System.out.println("Opção inválida");
            }
        } while (opcao != 7);
        teclado.close();
    }

    /**
     * 
     * @param jog        jogador auxiliar a ser adicionado ao vetor de jogadores
     * @param jogadores  vetor de jogadores
     * @param quantidade quantidade de jogadores antes de adicionar o novo jogador
     * @return se foi adicionado com sucesso ou não, e a quantidade atualizado
     */
    public static int incluirJogador(Jogador jog, Jogador[] jogadores, int quantidade) {

        if (quantidade < 5) {
            jogadores[quantidade] = jog;
            System.out.println("\n" + jog + " adiciondo com sucesso");
            quantidade++;
            return quantidade;
        } else {
            System.out.println("\n" + jog + " não adiciondo número máximo atingido");
            return quantidade;
        }
    }

    /**
     * 
     * @param nome       nome do jogador a ser removido
     * @param jogadores  vetor de jogadores para comparar se o nome a ser removido
     *                   existe
     * @param quantidade auxiliar para remoção, onde armazerna quantos jogadores
     *                   temos inclusos no jogo
     * @return se foi removido com sucesso ou não, e a quantidade de jogadores
     *         atualizado
     */
    public static int removerJogador(String nome, Jogador[] jogadores, int quantidade, int[][] cart) {
        int i = 0;
        while (i < quantidade && !jogadores[i].getNome().equals(nome)) {
            // nome.equals(jogadores[i].getNome()) é equivalente
            i++;
        }
        if (i < quantidade) {
            if (jogadores[i].getNome().equals(nome)) {
                jogadores[i] = null;
                // zerar os pontos deste jogador
                for (int k = 0; k < 13; k++)
                    cart[k][i] = 0;
                quantidade--;
            }
            int j = i;
            while (j < jogadores.length && j < quantidade) {
                if (jogadores[j] == null) {
                    jogadores[j] = jogadores[j + 1];
                    jogadores[j + 1] = null;
                    j++;
                }
            }
            System.out.println("\n" + nome + " removido com sucesso");
            return quantidade;

        } else {

            System.out.println("\nImpossível remover um jogador que não existe!");
            return quantidade;
        }

    }

    /**
     * @brief método para mostrar todos os jogadores adicionados
     * @param jogadores vetor de jogadores para printar seus toString
     */
    public static void motraTodosJogadores(Jogador[] jogadores) {
        int i = 0;
        for (i = 0; i < jogadores.length; i++) {
            if (jogadores[i] != null)
                System.out.println(jogadores[i]);
        }
    }

    /**
     * @brief recebe a cartela e zera todos os seus índices
     * @param cart Cartela de resultador
     */
    public static void zeraCartela(int[][] cart) {
        int i, j;
        for (i = 0; i < 13; i++) {
            for (j = 0; j < 5; j++) {
                cart[i][j] = 0;
            }
        }

    }

    /**
     * @brief recebe a cartela e o numero do jogador e printa as jogadas que já
     *        foram efetuadas
     * @param cart cartela onde é armazenado os pontos
     * @param k    número do jogador no vetor, para mostrar suas jogadas já
     *             efetuadas
     */
    public static void mostraJogadasEfetuadas(int[][] cart, int k) {
        int i;
        for (i = 0; i < 13; i++) {
            if (cart[i][k] == 0) {
                System.out.print("-\t");
            } else
                System.out.print("X\t");
        }
        System.out.println("");
    }

    /**
     * 
     * @param cart   Matriz cartela onde contém todas os pontos dos jogadores
     * @param qtdJog numero de jogadores na partida, para auxiliar na hora de
     *               printar os resultados
     * @param jogs   vetor de jogadores que estão na partida
     */
    public static void mostrarCartela(int[][] cart, int qtdJog, Jogador[] jogs) {
        // se quantidade de jogadores for 0, não tem o que mostrar
        if (qtdJog == 0)
            System.out.println("Não há jogadores para existir uma cartela de Campeonato.");
        else {
            // vetor de string para printar na barra lateral
            String[] s = { "1", "2", "3", "4", "5", "6", "7(T)", "8(Q)", "9(F)", "10(S+)", "11(S-)", "12(G)", "13(X)" };

            System.out.println("-- Cartela de Resultados --");
            System.out.print("\t");

            // imprimir nome dos jogadores no cabeçalho
            for (int i = 0; i < qtdJog; i++)
                System.out.print(jogs[i].getNome() + "\t");
            System.out.println("");

            // imprimir a barra lateral referente a cada jogada de 1-13
            for (int i = 0; i < 13; i++) {
                System.out.print(s[i] + "\t");

                for (int j = 0; j < qtdJog; j++) {
                    // se não tiver pontuado e certa jogada, printa espaço em branco
                    if (cart[i][j] != 0)
                        System.out.print(cart[i][j] + "\t");
                    else
                        System.out.print("\t ");

                }
                System.out.println();
            }
            System.out.println("--------------------------------------------------------");
            int cont;
            // total de pontos para cada jogador
            System.out.print("Total" + "\t");
            for (int i = 0; i < qtdJog; i++) {
                // contador auxiliar para contar os pontos de cada jogador
                cont = 0;
                for (int k = 0; k < 13; k++) {
                    cont += cart[k][i];
                }
                System.out.print(cont + "   \t");

            }
            System.out.println("");

        }

    }

    /**
     * 
     * @param cart   cartela vazia para recuperação de dados
     * @param qtdJog quantidade de jogadores após a leitura de arquivo
     * @param jogs   vetor de jogadores lidos
     */
    public static void recuperaCartela(int[][] cart, int qtdJog, Jogador[] jogs) {

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < qtdJog; j++) {
                cart[i][j] = jogs[j].retornaPonto(i);
            }
        }
    }
}