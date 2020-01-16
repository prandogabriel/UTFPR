public class Motor{

    private  int[] parafusos;
    private float[] rebimbocas;

    public Motor(int q1, int q2){
        
        parafusos = new int[q1];
        rebimbocas = new float[q2];
    }

    public void acelerar(int i){
        System.out.println("Estou acelerando a "+i+"de instensidade");

    }

    public String toString(){
        return "tenho um motor com "+ parafusos.length+" parafusos e  "+rebimbocas.length+"rebimbobcas";
    }
}