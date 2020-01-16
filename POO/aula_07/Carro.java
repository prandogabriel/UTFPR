public class Carro{
    
    private String marca;
    private Motor propulsor;

    public Carro(String m, Motor mo){
        marca = m;
        propulsor = mo; 
    }

    public void acelerar(int v) {
        propulsor.acelerar(v);
        
    }

    public String toString(){
        return "sou um carro da marca "+marca+" " +propulsor;
    }
}