package cpp.polynomialinterpolation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Main {    

    public static void main(String[] args) {

        if(args.length > 0)
        {
            System.out.println("Args Error");
            return;
        }

        try 
        {

            System.out.println("Enter the path to the text file with x and f(x) values: ");
            System.out.print(" > ");
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in) );
            String filePath = reader.readLine();
            double[][] data = readDataFromFile(filePath);

            if (data != null)
            {
                double[] xValues = data[0];
                double[] fxValues = data[1];
                double[][] differenceTable = dividedDifferenceTable(xValues, fxValues);

                double[] newtonCoefficients = Arrays.copyOf(differenceTable[0], differenceTable[0].length);
                
                System.out.println();
                printMatrix(differenceTable);
                System.out.println();
                printLeftTopHalf(differenceTable);
                System.out.println();

                printNewtonPolynomial(newtonCoefficients, xValues);
                // System.out.println("\n Newton's Form Polynomial: ");
                // printNewtonPolynomial();

                // System.out.println("\n Lagrange's Form Polynomial: ");
                // printLagrangePolynomial();

                // System.out.println("\n Simplified Polynomial: ");
                // printSimplifiedPolynomial();

            }

        } 
        catch (IOException e) 
        {
            System.err.println();
            System.err.println("Error: " + e.getMessage());
            System.err.println();
        }

    }

    private static void printMatrix(double[][] matrix) 
    {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static void printLeftTopHalf(double[][] matrix) 
    {
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int i = 0; i < rows; i++) 
        {
            // Print elements up to the diagonal for the current row
            for (int j = 0; j < cols - i; j++) 
                System.out.print(matrix[i][j] + " ");
            System.out.println(); // Move to the next line after printing each row
        }
    }

    private static double[][] readDataFromFile(String filePath) throws IOException 
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {
            double[] xVals = null;
            double[] fxVals = null;

            String line;
            String[] splits = null;
            //XVals
            if ( ( line = reader.readLine()) != null ) 
            {    
                splits = line.split("\\s+");
                xVals = Arrays.stream(splits).mapToDouble(Double::parseDouble).toArray();
            }
            else
                throw new IOException("Missing X Values on Row(1)");

            //FxVals
            if ( ( line = reader.readLine()) != null ) 
            {
                splits = line.split("\\s+");
                fxVals = Arrays.stream(splits).mapToDouble(Double::parseDouble).toArray();
            }
            else
                throw new IOException("Missing F(x) Values on Row(2)");

            return new double[][]{xVals,fxVals};
        }
    }

    private static double[][] dividedDifferenceTable(double[] x, double[] fx) 
    {
        int n = x.length;
        double[][] table = new double[n][n];

        for (int i = 0; i < n; i++) 
            table[i][0] = fx[i];
        for (int j = 1; j < n; j++) 
            for (int i = 0; i < n - j; i++) 
                table[i][j] = (table[i + 1][j - 1] - table[i][j - 1]) / (x[i + j] - x[i]);
        
        return table;
    }

    private static void printNewtonPolynomial(double[] Co, double[] Xs)
    {
        int n = Xs.length;
        System.out.print("N(x) = ");
        for (int i = 0; i < n; i++) 
        {
            System.out.print( String.format("%.6f", Co[i]) );
            
            for(int j=0; j<=i-1; j++)
                System.out.print(" * (x - " + String.format("%.6f", Xs[j]) + ")");

            if(i < n-1)
                System.out.print(" + ");
        }
        System.out.println();

    }
    

    

}

