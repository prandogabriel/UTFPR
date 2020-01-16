public abstract class Veiculo{

  //Atributos
  private final int ID;
  private int distanciaPercorrida;
  private Roda[] rodas;
  private final int quantidadeRodas;

  //Metodos
  public  Veiculo(int ID, int distancia, int qtdrodas){
    this.ID = ID;
    this.distanciaPercorrida = distancia;
    this.quantidadeRodas = qtdrodas;
    this.rodas = new Roda[quantidadeRodas];
    for(int i = 0; i < rodas.length; i++){
      rodas[i] = new Roda();
    }
  }

  public abstract void mover();
  public abstract void desenhar();

  public void calibraALL(){
    for(int i = 0; i < quantidadeRodas; i++){
      rodas[i].setCalibragemT();
    }
  }

  public void descalibrar(int pos){
    rodas[pos-1].setCalibragemF();
  }

  public void calibra(int pos){
    rodas[pos-1].setCalibragemT();
  }

  public int getDistancia(){
    return distanciaPercorrida;
  }
  public void setDistancia(){
    distanciaPercorrida = distanciaPercorrida + 1;
  }

  public int getID(){
    return ID;
  }

  public int r_cali(){
    int cont = 0;
    for(int i = 0; i<rodas.length; i++){
      if(rodas[i].getCalibragem()) cont++;
    }
    return cont;
  }

  public String toString(){
    return "Sou o veiculo de ID:"+ID+" com "+quantidadeRodas+" rodas e distancia percorrida: "+distanciaPercorrida;
  }
}
