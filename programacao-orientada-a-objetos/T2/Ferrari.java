import java.util.Random;

public class Ferrari extends Veiculo_Motorizado implements IPVA{

  //Atributos
  private String[] ferrari = {"        __         \n","      ~( @\\ \\   \n","   _____]_[_/_>__   \n",
  "  / __ \\<> |  __ \\      \n","=\\_/__\\_\\__|_/__\\_D     Ferrari\n","   (__)      (__)    \n"};
  private boolean IPVA;


  //Método criador
  public Ferrari(int ID, int dist){
    super(ID, dist, 4);
    Random x = new Random();
    int i =  x.nextInt(100);
    if(i % 2 == 0) {
      this.IPVA = true;
    }
    else{
      this.IPVA = false;
    }
  }

  //Métodos Herdadados de Veiculos
  public void desenhar(){
    System.out.print(ferrari[0]);
    System.out.print(ferrari[1]);
    System.out.print(ferrari[2]);
    System.out.print(ferrari[3]);
    System.out.print(ferrari[4]);
  }

  public void mover(){
    String giroF = "                    ";

    if(getcombustivel()>0 && IPVA == true && r_cali() == 4){
      for(int i = 0; i< ferrari.length; i++)
      	ferrari[i] = giroF + ferrari[i];
      System.out.print(ferrari[0]);
      System.out.print(ferrari[1]);
      System.out.print(ferrari[2]);
      System.out.print(ferrari[3]);
      System.out.print(ferrari[4]);
      setDistancia();
      mudacombustivel('F');
    }
    else{
      System.out.println("Veja se o veiculo possui IPVA pago e combustivel");
    }

  }


  //Métodos Herdados de IPVA
  public  double calcularIPVA(){
    return (cte_Ferrari * valor_Base);
  }

  public boolean getIPVA(){
    return IPVA;
  }

  public String toString(){
    String aux  = super.toString() + " Eu sou uma Ferrari com IPVA:"+IPVA;
    return aux;
  }

}
