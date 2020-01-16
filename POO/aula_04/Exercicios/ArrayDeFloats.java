public class ArrayDeFloats // declaracao da classe
{
  private float[] array; // o array encapsulado

  public ArrayDeFloats(int numero) {
    array = new float[numero]; // alocamos memoria para o array encapsulado
  }

  public int tamanho() {
    return array.length;
  }

  public void modifica(int posicao, float valor) {
    if ((posicao >= 0) && (posicao < array.length))
      array[posicao] = valor;
  }

  public float valor(int posicao) {
    if ((posicao >= 0) && (posicao < array.length))
      return array[posicao];
    else
      return Float.NaN;
  }

  public float[] paraArray() {
    return array;
  }

  public float menorValor() {
    float menorAteAgora = array[0];
    for (int c = 1; c < array.length; c++)
      if (array[c] < menorAteAgora)
        menorAteAgora = array[c];
    return menorAteAgora;
  }

  public float maiorValor() {
    float maiorAteAgora = array[0];
    for (int c = 1; c < array.length; c++)
      if (array[c] > maiorAteAgora)
        maiorAteAgora = array[c];
    return maiorAteAgora;
  }

  public String toString() {
    String resultado = "O array tem " + array.length + " elementos:\n";
    for (int c = 0; c < array.length; c++)
      resultado += array[c] + " ";
    return resultado;
  }

} // fim da classe ArrayDeFloats
