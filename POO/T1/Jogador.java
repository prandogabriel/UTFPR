import java.io.Serializable;
public class Jogador implements Serializable {

    // Atributos encapsulado
    private String nome;
    private JogoGeneral jogoG;

    /**
     * @return construtor default para  classe Jogador
     */
    public Jogador() {
        nome = "Nulo";
        jogoG = new JogoGeneral();
    }

    /**
     * @brief Construtor sobrecarregado, como pedido no trabalho
     * @param name nome a ser atribuido ao jogador
     */
    public Jogador(String name) {
        nome = name;
        jogoG = new JogoGeneral();
    }

    /**
     * 
     * @return String com o nome do jogador
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return o valor dos 5 dados do jogador
     */
    public void jogarDados() {
        jogoG.rolarDados();
    }


    /**
     * 
     * @param n qual jogada o jogador pretende pontuar
     * @return caso jogada válida numero de pontos cujo qual o jogador consegui marcar, caso contrário retorna zero
     */
    public int escolherJogada(int n) {
        return jogoG.pontuarJogada(n);
    }

    /**
     * 
     * @param x indice da jogada a ser retornada
     * @return valor do índice
     */
    public int retornaValorJogada(int x){
    
        return jogoG.valorJogada(x);
    }

    /**
     * @brief função para zera todas as jogadas
     */
    public void zeraJogadas(){
        jogoG.zeraTudo();
    }

    /**
     * 
     * @param x jogada a ser pontuada em jogadas
     */
    public void pontuaJogadas(int x){
        jogoG.pontua(x);
    }

    /**
     * 
     * @param i indice da jogada
     * @return valor de pontuação no índice
     */
    public int retornaPonto(int i){
        return jogoG.Ponto(i);
    }

    /**
     * @return informações sobre o jogador
     */
    public String toString() {
        return nome;
    }

}
