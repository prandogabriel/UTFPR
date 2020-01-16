public class CalculadoraDeLinhaDeComando // declaracao da classe
{
  public static void main(String[] argumentos) {
    if (argumentos.length != 3) {
      System.out.println("Este programa precisa que tres argumentos sejam passados " + "pela linha de comando.");
      System.exit(1); // saimos do programa com o codigo de execu��o n�mero 1
    }
    // Extraimos um valor inteiro da String correspondente ao primeiro argumento
    int primeiroValor = Integer.parseInt(argumentos[0]);
    // Extraimos o primeiro caracter da String correspondente ao segundo argumento
    char operador = argumentos[1].charAt(0);
    // Extraimos um valor inteiro da String correspondente ao terceiro argumento
    int segundoValor = Integer.parseInt(argumentos[2]);
    // Dependendo do caracter operador, efetuamos a opera��o
    int resultado = 0; // deve ser inicializada
    switch (operador) {
    case '+':
      resultado = primeiroValor + segundoValor;
      break;
    case '-':
      resultado = primeiroValor - segundoValor;
      break;
    case 'x':
      resultado = primeiroValor * segundoValor;
      break;
    case '/':
      resultado = primeiroValor / segundoValor;
      break;
    }
    // Imprimimos os argumentos passados com espa�os entre eles
    for (int indice = 0; indice < argumentos.length; indice++)
      System.out.print(argumentos[indice] + " ");
    // Imprimimos o resultado
    System.out.println("= " + resultado);
  } // fim do m�todo main
} // fim da classe
