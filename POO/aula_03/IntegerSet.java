public class IntegerSet {

    private int i;

    private boolean[] a = new boolean[101];

    IntegerSet() {

        for (int i = 0; i < 101; i++) {

            a[i] = false;

        }

    }

    public IntegerSet union(IntegerSet b) {

        IntegerSet c = new IntegerSet();

        for (i = 0; i < 101; i++) {

            c.a[i] = a[i] || b.a[i];

        }

        return c;

    }

    public IntegerSet intersection(IntegerSet b) {

        IntegerSet c = new IntegerSet();

        for (i = 0; i < 101; i++) {

            c.a[i] = a[i] && b.a[i];

        }

        return c;

    }

    public void insertElement(int x) {

        a[x] = true;

    }

    public void deleteElement(int x) {

        a[x] = false;

    }

    public String toSetString() {

        String s = new String();

        for (i = 0; i < 101; i++) {

            if (a[i] == true) {

                s = s + i + ' ';

            }

        }

        return s;

    }

    public boolean isEqualTo(IntegerSet b) {

        for (i = 0; i < 101; i++) {

            if (a[i] != b.a[i]) {

                return false;

            }

        }

        return true;

    }

}
