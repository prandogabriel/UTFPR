public class ArraydePontos2D{
    private Ponto2D[] array;

    public ArraydePontos2D(int numero) {
        array = new Ponto2D[numero]; // alocamos memoria para o array encapsulado
    }
    
    public int tamanho() {
        return array.length;
    }

    public void modifica(int posicao, Ponto2D novo ) {
        if ((posicao >= 0) && (posicao < array.length))
          array[posicao] = novo;
    }

    public Ponto2D valor(int posicao) {
        if ((posicao >= 0) && (posicao < array.length))
          return array[posicao];
        else
          return null;
    }

    public String toString() {
        String resultado = "O array de Pontos 2D tem " + array.length + " elementos:\n";
        for (int c = 0; c < array.length; c++)
          resultado += array[c] + " ";
        return resultado;
    }
}