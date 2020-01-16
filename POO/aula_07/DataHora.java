/** 
 * A classe DataHora reutiliza as classes Data e Hora através de delegação.
 * A data e hora são representadas por instâncias das respectivas classes que estão
 * embutidas na classe DataHora, e toda a interação entre esta classe e as embutidas
 * é feita através da chamada de métodos das classes embutidas. Esta classe demonstra 
 * o conceito de reutilização de classes através de delegação ou composição.
 */
class DataHora // declaração da classe 
  {
  /**
   * Declaração dos campos da classe. Estes campos são declarados como privados
   * para que não possam ser acessados de fora da classe.
   */
  private Data estaData; // uma referência à instância da classe Data representa
                         // o dia, mês e ano
  private Hora estaHora; // uma referência à instância da classe Hora representa
                         // a hora, minuto e segundo
   
 /**
  * O construtor para a classe DataHora, que recebe argumentos para inicializar 
  * todos os campos que esta classe indiretamente contém, e chama os construtores
  * das classes Data e Hora para inicializar os campos das instâncias destas classes.
  * @param hora a hora
  * @param minuto o minuto
  * @param segundo o segundo
  * @param dia o dia
  * @param mês o mês
  * @param ano o ano
  */
  DataHora(byte hora,byte minuto,byte segundo,byte dia,byte mês,short ano)
    {
    estaData = new Data(dia,mês,ano);
    estaHora = new Hora(hora,minuto,segundo);
    }
  
 /**
  * O construtor para a classe DataHora, que recebe argumentos para inicializar 
  * os campos que representam uma data. O construtor também inicializará os campos
  * que representam uma hora, considerando que todos valem zero (meia-noite).
  * De novo, os construtores das classes embutidas nesta serão chamados.
  * @param dia o dia
  * @param mês o mês
  * @param ano o ano
  */
  DataHora(byte dia,byte mês,short ano)
    {
    estaData = new Data(dia,mês,ano);
    estaHora = new Hora((byte)0,(byte)0,(byte)0); // cast necessário
    }
  
  /**
   * O método toString não recebe argumentos, e retorna uma string contendo os valores 
   * dos campos da classe formatados. Os valores são obtidos através da chamada
   * implícita aos métodos toString das instâncias das classes embutidas.
   * @return uma string com os valores dos campos formatados.
   */
  public String toString()
    {
    return estaData+" "+estaHora;
    }

  } // fim da classe DataHora
