import java.io.Serializable;

public class JogoGeneral implements Serializable {

    private Dado[] dados;
    private int[] jogadas;
    private int aux;
    private int a, b, c;
    private int i, j;

    /**
     * @return construtor da classe JogoGeneral, sem nenhum parâmetro
     */
    public JogoGeneral() {
        dados = new Dado[5];

        jogadas = new int[13];
        for (i = 0; i < dados.length; i++) {
            dados[i] = new Dado();
        }
        for (i = 0; i < jogadas.length; i++) {
            jogadas[i] = 0;
        }
    }
    
    /**
     * @brief metodo para zerar as jogadas
     */
    public void zeraTudo(){
        for(i=0;i<13;i++)
            jogadas[i]=0;
    }

    /**
     * 
     * @param x  jogada a ser pontuada de 1-13
     */
    public void pontua(int x){
        if(jogadas[x-1]==0){
            jogadas[x-1] = pontuarJogada(x);
        }
    }
    
    /**
     * 
     * @param x indice da jogada que se quer saber o valor
     * @return valor da jogada no índice
     */
    public int valorJogada(int x){
        return jogadas[x];
    }

    
   

    /**
     * @brief método para rolar os dados, utilizando Ramdom para definir os valores
     * @return o valor dos 5 dados aleatórios
     */
    public void rolarDados() {
        Dado dadoAux = new Dado();
        // rolar os dados
        for (i = 0; i < dados.length; i++) {
            dados[i] = new Dado();
            System.out.print(dados[i].getSideUp() + "  ");
        }
        System.out.println("");
        // ordenar os dados para as verificações futuras das jogadas
        for (i = 0; i < dados.length; i++) {
            for (j = 0; j < 4; j++) {
                if (dados[j].getSideUp() > dados[j + 1].getSideUp()) {
                    dadoAux = dados[j];
                    dados[j] = dados[j + 1];
                    dados[j + 1] = dadoAux;
                }
            }
        }
    }

    /**
     * @brief método para verificar se a jogado que o usuário informou é valida ou
     *        não
     * @return (1) para verdaeiro e (0) para falso
     */
    public boolean validarJogada(int x) {

        // entrada para validar as jogadas de 1,2,3,4,5,6.
        if (x > 0 && x < 7) {
            aux = 0;
            for (i = 0; i < dados.length; i++) {
                a = dados[i].getSideUp();

                if (a == x)
                    aux++;
            }
            if (x > 0)
                return true;
            else
                return false;
        }

        // entrada para validar a jogada da Trinca
        if (x == 7) {
            aux = 0;
            for (i = 0; i < 3; i++) {
                a = dados[i].getSideUp();
                b = dados[i + 1].getSideUp();
                c = dados[i + 2].getSideUp();
                if (a == b && a == c) {
                    aux++;
                }
            }
            if (aux >= 1)
                return true;
            else
                return false;
        }

        // entrada para validar jogada da quadra
        if (x == 8) {
            aux = 0;
            for (i = 0; i < 3; i++) {
                a = dados[i].getSideUp();
                b = dados[i + 1].getSideUp();
                c = dados[i + 2].getSideUp();
                if (a == b && a == c) {
                    aux++;
                }
            }
            if (aux >= 2)
                return true;
            else
                return false;
        }

        // entrada para validar jogada Full house
        if (x == 9) {
            aux = 0;
            if (dados[0].getSideUp() == dados[1].getSideUp() && dados[0].getSideUp() == dados[2].getSideUp()
                    && dados[3].getSideUp() == dados[4].getSideUp())
                aux++;
            if (dados[0].getSideUp() == dados[1].getSideUp() && dados[2].getSideUp() == dados[3].getSideUp()
                    && dados[2].getSideUp() == dados[4].getSideUp())
                aux++;

            if (aux >= 1)
                return true;
            else
                return false;
        }

        // entrada para validar Jogada Sequência alta (S+):
        if (x == 10) {
            aux = 0;
            for (i = 0; i < dados.length; i++) {
                if (dados[i].getSideUp() == i + 2) {// compara se no indice 0 ele é igual a 2, no inidice 1 igual a
                                                    // 3....
                    aux++;
                }
            }
            // se for igual a 5 quer dizer que a jogada é valida
            if (aux == 5)
                return true;
            else
                return false;
        }

        // entrada pra validar Jogada Sequência baixa (S-):
        if (x == 11) {
            aux = 0;
            for (i = 0; i < dados.length; i++) {
                if (dados[i].getSideUp() == i + 1) {// compara se no indice 0 ele é igual a 1, no inidice 1 igual a
                                                    // 2....
                    aux++;
                }
            }
            // se for igual a 5 quer dizer que a jogada é valida
            if (aux == 5)
                return true;
            else
                return false;
        }

        // entrada para validar a jogada General
        if (x == 12) {
            aux = 0;
            for (i = 0; i < 4; i++) {
                a = dados[i].getSideUp();
                b = dados[i + 1].getSideUp();
                if (a == b) {
                    aux++;
                }
            }
            if (aux == 4)
                return true;
            else
                return false;
        }

        // entrada para validar jogada Jogada aleatória (X):
        if (x == 13) {
            aux = 0;
            for (i = 0; i < 5; i++) {
                a = dados[i].getSideUp();
                if (a > 0 && a < 7) {
                    aux++;
                }
            }
            if (aux == 5)
                return true;
            else
                return false;
        }
        return false;

    }

    /**
     * @brief caso a função validarJogada retorne true, a função pontua a jogada,
     *        caso contrário não pontua.
     * @param x qual jogada irá ser pontuada.
     * @return 0 caso não seja possível pontuar, ou o valor da pontuação.
     */
    public int pontuarJogada(int x) {

        if (validarJogada(x)) {
            aux = 0;

            // pontuar jogada de 1,2,3,4,5,6
            if (x > 0 && x < 7) {
                for (i = 0; i < dados.length; i++) {
                    if (dados[i].getSideUp() == x)
                        aux++;
                }
                return (aux * x);
            }
            // pontuar jogada Trinca, Quadra e aleatória. Que são a soma de todos os dados
            if (x == 7 || x == 8 || x == 13) {
                for (i = 0; i < dados.length; i++)
                    aux += dados[i].getSideUp();

                return aux;
            }

            // pontuar jogada full house
            if (x == 9)
                return 25;

            // pontuar jogada sequência alta
            if (x == 10)
                return 30;

            // pontuar jogada sequência baixa
            if (x == 11)
                return 40;

            // pontuar jogada General, caso não seja nenhum outra, só resta ser essa jogada.
            else
                return 50;

        } else
            return 0;

    }

    public int Ponto(int x){
        return jogadas[x];
    }

    /**
     * @return  informações sobre o jogo
     */
    public String toString() {

        return "o Jogo general é composto por 5 dados, onde o jogador tem direito a 13 lançamentos destes dados";
    }

}
