import java.util.Random;

public class IntegerSetDemo{
    public static void main(String[] args) {
        IntegerSet x = new IntegerSet();
        IntegerSet y = new IntegerSet();
        IntegerSet c = new IntegerSet();

        c = c.union(x,y);
        c = x.intersection(y);
        x.insertElement(20);
        x.deleteElement(19);
        y.toSetString();
        x.isEqualTo(y);
         
    }
}