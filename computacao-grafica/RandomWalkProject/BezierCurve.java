import image.Image;

public class BezierCurve {
    static class Point{
        int x; int y;
    }

    //ajustar o tamanho da imagem se precisar
    public static Image image = new Image(400, 400, 1);

    public static void main(String[] args) throws Exception {

        //quantidade de pontos
        Point[] points = new Point[3];
        points[0] = new Point(); points[0].x = 400; points[0].y = 0;
        points[1] = new Point(); points[1].x = 200; points[1].y = 900;
        points[2] = new Point(); points[2].x = 0; points[2].y = 0; //o �ltimo ponto termina em 0,0
        /*
        Point[] points = new Point[4];
        points[0] = new Point(); points[0].x = 400; points[0].y = 0;
        points[1] = new Point(); points[1].x = 318; points[1].y = 350;
        points[2] = new Point(); points[2].x = 92; points[2].y = 350; //o �ltimo ponto termina em 0,0
        points[3] = new Point(); points[2].x = 0; points[2].y = 0; //o �ltimo ponto termina em 0,0
         */

        //desenhar a curva
        bezierCurve(points);

        //mostrar a imagem
        image.showImage();
    }

    public static void bezierCurve(Point[] points){
        final float TOLERANCE = 0.001f;
        for (int k=0; k<points.length; k++){
            for (float t=0; t<=1; t+=TOLERANCE) {
                bezierCurveTime(t, points);
            }
        }
    }

    public static void bezierCurveTime(float t, Point[] points){
        int n = points.length;
        double sumX = 0;
        double sumY = 0;

        for (int i=0; i<n; i++){//etapas do somat�rio
            double bernResult = bernstein(t, n, i);

            sumX += bernResult * points[i].x;
            sumY += bernResult * points[i].y;
        }

        if (sumX > 0 && image.getHeight()-(int)sumY > 0 && sumX < image.getWidth() && image.getHeight()-(int)sumY < image.getHeight())
            image.setPixel((int)sumX, image.getHeight()-(int)sumY, 0, 255);
    }
    public static double bernstein(float t, int n, int i){
        return binomialCoefficient(n, i) * Math.pow(t,i) * Math.pow(1-t, n-i);
    }
    public static double binomialCoefficient(int n, int i){
        return factorial(n)/(factorial(i)*factorial(n-i));
    }
    public static double factorial(int number){
        double result = 1;
        for (int k=2; k<=number; k++){
            result *= k;
        }
        return result;
    }
}