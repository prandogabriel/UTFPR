public class DieDemo{
    public static void main (String [] args){
        Die a = new Die();
        Die b = new Die(3);
        System.out.println( a + "\n" + b);
        System.out.println("soma das faces superiores");
        System.out.println( a.getSideUp() +b.getSideUp());
        a.roll();
        b.roll();
        System.out.println("apos rolar os dados");
        System.out.println( a + "\n" + b);
        System.out.println("soma das faces superiores");
        System.out.println( a.getSideUp() +b.getSideUp());

    }
}