public class ApliArray{
    public static void main(String[] args) {
        ArraydePontos2D vet = new ArraydePontos2D(2);
        Ponto2D ponto1 = new Ponto2D(2,4);
        Ponto2D ponto2 = new Ponto2D(5,7);
        System.out.println(vet.tamanho());
        vet.modifica(0,ponto1);
        vet.modifica(1,ponto2);
        System.out.println(vet.valor(0));
        System.out.println(vet);
    }
}