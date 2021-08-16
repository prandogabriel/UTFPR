import java.util.Random;

public class Roda {

  // Atributos
  private boolean calibragempneu;

  // Métodos
  public Roda() {
    Random x = new Random();
    int i = x.nextInt(100);
    if (i % 2 == 0) {
      this.calibragempneu = true;
    } else {
      this.calibragempneu = false;
    }
  }

  public void setCalibragemT() {
    this.calibragempneu = true;
  }

  public void setCalibragemF() {
    this.calibragempneu = false;
  }

  public boolean getCalibragem() {
    return this.calibragempneu;
  }

}
