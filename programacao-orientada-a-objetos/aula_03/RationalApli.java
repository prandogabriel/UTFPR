public class RationalApli {

    public static void main(String[] args) {
        Rational a = new Rational(2,2);
        Rational b = new Rational(21,42);
        a.mostrar(a);
        b.mostrar(b);
        a.somar(a,b);
        a.multiplicar(a,b);
        a.dividir(a,b);
        b.mostrarFloat(3);
    } 
}