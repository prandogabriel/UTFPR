public class UsaCarro{

    public static void main(String[] argumentos) {
        
        Carro myCar = new Carro("vokisvaguin", new Motor(13,28));

        Motor meuAvo = new Motor(25,32);
        
        Carro carro2 = new Carro("mercedes", meuAvo);

        System.out.println(myCar);
        myCar.acelerar(10);
        System.out.println(carro2);
        myCar.acelerar(30);
    }
}