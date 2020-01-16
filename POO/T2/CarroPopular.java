import java.util.Random;

public class CarroPopular extends Veiculo_Motorizado implements IPVA{

  //Atributos
  private String[] carro = {"    ____\n"," __/  |_ \\_\n",
  "|  _     _``-.       Carro popular\n","'-(_)---(_)--'\n\n\n"};
  private boolean IPVA;

  //Método construtor
  public CarroPopular(int ID, int dist){
    super(ID,dist,4);
    Random x = new Random();
    int i =  x.nextInt(100);
    if(i % 2 == 0) {
      this.IPVA = true;
    }
    else{
      this.IPVA = false;
    }
  }

  //Métodos Herdados de Veiculos
  public void desenhar(){
    System.out.print(carro[0]);
    System.out.print(carro[1]);
    System.out.print(carro[2]);
    System.out.print(carro[3]);
  }

  public void mover(){
    String giroC = "          ";

    if(getcombustivel()>0 && IPVA == true && r_cali() == 4){
      for(int i = 0; i< carro.length; i++)
        carro[i] = giroC + carro[i];
        System.out.print(carro[0]);
        System.out.print(carro[1]);
        System.out.print(carro[2]);
        System.out.print(carro[3]);
      setDistancia();
      mudacombustivel('C');
    }else{
    System.out.println("Veja se o veiculo possui IPVA pago e combustivel");
  }
}



  //Métodos Herdados de IPVA
  public double calcularIPVA(){
    return (cte_CarroPopular * valor_Base);
  }

  public String toString(){
    String aux  = super.toString() + " Eu sou um Carro Popular com IPVA:"+IPVA;
    return aux;
  }

}
