public class IFSTables {
    static double ot = 0.3333333, tt = 0.6666666;
    public static double[][] CARPET = new double[][]{
        new double[]{ot, ot, 0, 0, 0,  0},
        new double[]{ot, ot, 0, 0, ot, 0},
        new double[]{ot, ot, 0, 0, tt, 0},
        new double[]{ot, ot, 0, 0, 0,  ot},
        new double[]{ot, ot, 0, 0, tt, ot},
        new double[]{ot, ot, 0, 0, 0,  tt},
        new double[]{ot, ot, 0, 0, ot, tt},
        new double[]{ot, ot, 0, 0, tt, tt},
    };

    public static double[][] GASKET = new double[][]{
        new double[]{0.5, 0.5, 0, 0, 0, 0},
        new double[]{0.5, 0.5, 0, 0, 0.5, 0},
        new double[]{0.5, 0.5, 0, 0, 0, 0.5}
    };

    public static double[][] GASKET2 = new double[][]{
        new double[]{0.5, 0.5, 0, 0, 0, 0},
        new double[]{0.5, 0.5, 0, 0, 0.5, 0},
        new double[]{0.5, 0.5, 0, 0, 0.25, 0.5}
    };

    public static double[][] GASKET3 = new double[][]{
        new double[]{0.5, 0.5, 0, 0, 0, 0},
        new double[]{0.5, 0.5, 0, 0, 0.5, 0},
        new double[]{0.5, 0.5, -90, -90, 0.5, 0.5}
    };

    public static double[][] KOCH = new double[][]{
        new double[]{ot, ot, 0, 0, 0,  0},
        new double[]{ot, ot, -60, -60, ot, 0},
        new double[]{ot, ot, 60, 60, 0.5, 0.289},
        new double[]{ot, ot, 0, 0, tt,  0}
    };

    public static double[][] SQUAREKOCH = new double[][]{
        new double[]{ot, ot, -90, -90, ot, 0},
        new double[]{ot, ot, 90, 90, tt, ot},
        new double[]{ot, ot, 0, 0, 0, 0},
        new double[]{ot, ot, 0, 0, tt, 0},
        new double[]{ot, ot, 0, 0, ot, ot}
    };

    public static double[][] TREE = new double[][]{
        new double[]{0.05, 0.6, 0, 0, 0.496, 0.0},
        new double[]{0.05, -0.5, 0, 0, 0.496, 0.4},
        new double[]{0.42, 0.42, 42, 42, 0.36, 0.6},
        new double[]{0.42, 0.42, -42, -42, 0.36, 0.31},
        new double[]{0.42, 0.42, 100, 100, 0.55, 0.67},
        new double[]{0.42, 0.42, -100, -100, 0.57, 0.24},
    };

    public static double[][] CHRISTMASTREE = new double[][]{
        new double[]{0.5, 0.5, 0, 0, 0.25, 0.5},
        new double[]{0.5, 0.5, -90, -90, 0.5, 0},
        new double[]{0.5, 0.5, 90, 90, 0.5, 0.5}
    };

    public static double[][] SPIRAL = new double[][]{
        new double[]{0.25, 0.25, 0, 0, 0.1, 0.2},
        new double[]{0.25, 0.25, 0, 0, 0.38, 0.7},
        new double[]{0.25, 0.25, 0, 0, 0.65, 0.2},
        new double[]{0.7, 0.7, -150, -150, 0.98, 0.62}
    };

    public static double[][] TCARPET = new double[][]{
        new double[]{0.5, 0.5, 0, 0, 0.5, 0},
        new double[]{0.5, 0.5, 90, 90, 0, 0.5},
        new double[]{0.5, 0.5, -90, -90, 1, 0.5}
    };

    public static double[][] PROBLEM2 = new double[][]{
        new double[]{0.5, 0.5, 0, 0, 0.5, 0.5},
        new double[]{-0.5, 0.5, 0, 0, 0.5, 0},
        new double[]{-0.5, 0.5, 90, 90, 1, 0.5}
    };

    public static double[][] PROBLEM3 = new double[][]{
        new double[]{-0.5, 0.5, 180, 180, 0.5, 0.5},
        new double[]{-0.5, 0.5, 180, 180, 0, 1},
        new double[]{-0.5, 0.5, 90, 90, 0.5, 0.5}
    };

    public static double[][] CANTORTHIRD = new double[][]{
        new double[]{ot, ot, 0, 0, 0, 0},
        new double[]{ot, ot, 0, 0, tt, 0},
    };

    public static double[][] CANTORHALF = new double[][]{
        new double[]{0.25, 0.25, 0, 0, 0, 0},
        new double[]{0.25, 0.25, 0, 0, 0.75, 0},
        new double[]{0.25, 0.25, 0, 0, 0, 0.75},
        new double[]{0.25, 0.25, 0, 0, 0.75, 0.75},
    };

    public static String getName(double[][] table) {
        if (table == GASKET) return "Sierpinski Gasket";
        else if (table == GASKET2) return "Sierpinski Gasket 2";
        else if (table == GASKET3) return "Sierpinski Gasket 3";
        else if (table == CARPET) return "Sierpinski Carpet";
        else if (table == KOCH) return "Koch Curve";
        else if (table == SQUAREKOCH) return "Square Koch Curve";
        else if (table == TREE) return "Tree Fractal";
        else if (table == CHRISTMASTREE) return "Christmas Tree Fractal";
        else if (table == SPIRAL) return "Spiral Fractal";
        else return "Fractal";
    }

    public static double[][] carpet7 () {
        double[][] carpet = new double[40][6];
        double os = 0.142857142857142857;
        int index = 0;
        for (int i = 0; i < 7; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < 7; j++) {
                    carpet[index] = new double[]{os, os, 0, 0, j * os, i * os};
                    index++;
                }
            } else {
                for (int j = 0; j < 7; j += 2) {
                    carpet[index] = new double[]{os, os, 0, 0, j * os, i * os};
                    index++;
                }
            }
        }
        return carpet;
    }
}
