/** 
 * A classe DataSimples contém campos e métodos que permitem a manipulação de 
 * datas.
 */
class DataSimples // declaração da classe 
  {
  /**
   * Declaração dos campos da classe
   */
  byte dia,mês; // dia e mês são representados por bytes
  short ano; // ano é representado por um short

  /**
   * O método inicializaDataSimples recebe argumentos para inicializar os campos da 
   * classe DataSimples. Este método chama o método dataÉVálida para verificar se os 
   * argumentos são correspondentes a uma data válida: se forem, inicializa os 
   * campos, caso contrário inicializa todos os três campos com o valor zero.
   * @param d o argumento correspondente ao método dia
   * @param m o argumento correspondente ao método mês
   * @param a o argumento correspondente ao método ano
   */
  void inicializaDataSimples(byte d,byte m,short a)
    {
    if (dataÉVálida(d,m,a)) // se a data for válida, inicializa os campos com os 
                            // valores passados como argumentos
      {
      dia = d; mês = m; ano = a;
      }
    else // caso contrário, inicializa os campos com zero
      {
      dia = 0; mês = 0; ano = 0;
      }
    } // fim do método inicializaDataSimples

  /**
   * O método dataÉVálida recebe três valores como argumentos e verifica de maneira 
   * simples se os dados correspondem a uma data válida. Se a data for válida, retorna
   * a constante booleana true, caso contrário, retorna a constante booleana false.
   * Vale a pena notar que este algoritmo é simples e incorreto, um dos exercícios 
   * sugere a implementação do algoritmo correto.
   * @param d o argumento correspondente ao método dia
   * @param m o argumento correspondente ao método mês
   * @param a o argumento correspondente ao método ano
   * @return true se a data for válida, false se não for válida
   */
  boolean dataÉVálida(byte d,byte m,short a)
    {
    if ((d >=1) &&      // se o dia for maior ou igual a 1  E
        (d <= 31) &&    // se o dia for menor ou igual a 31  E
        (m >= 1) &&     // se o mês for maior ou igual a 1  E
        (m <= 12))      // se o mês for menor ou igual a 12 ENTÃO 
      return true;      // a data é válida, retorna true
    else
      return false;     // a data não é válida, retorna false
    } // fim do método dataÉVálida

  /**
   * O método éIgual recebe uma instância da própria classe DataSimples como argumento
   * e verifica se a data representada pela classe e pela instância que foi passada é
   * a mesma. A comparação é feita comparando os campos da data um a um.
   * @param outraDataSimples uma instância da própria classe DataSimples
   * @return true se a data encapsulada for igual à passada, false caso contrário
   */
  boolean éIgual(DataSimples outraDataSimples)
    {
    if ((dia == outraDataSimples.dia) &&    // se os dois dias forem iguais E
        (mês == outraDataSimples.mês) &&    // se os dois meses forem iguais E
        (ano == outraDataSimples.ano))      // se os dois anos forem iguais então
      return true;      // a data é igual, retorna true
    else
      return false;     // a data é diferente, retorna false
    } // fim do método éIgual

  /**
   * O método mostraDataSimples não recebe argumentos nem retorna valores. Este método 
   * somente imprime os valores dos campos, formatados de forma que uma barra ("/") 
   * seja impressa entre eles. Quando o valor do ano for impresso, uma quebra de 
   * linha também será impressa.
   */
  void mostraDataSimples()
    {
    System.out.print(dia);   // O método print do campo out da classe System faz com 
    System.out.print("/");   // que o argumento passado a ele seja transformado em uma
    System.out.print(mês);   // string e impresso no terminal. O método println faz a 
    System.out.print("/");   // mesma coisa, mas adiciona uma quebra de linha ('\n')
    System.out.println(ano); // ao final da string impressa.
    }  // fim do método mostraDataSimples

  } // fim da classe DataSimples
