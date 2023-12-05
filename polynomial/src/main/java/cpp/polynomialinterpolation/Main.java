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

                System.out.println("Unformated Table: ");
                printMatrix(differenceTable);
                System.out.println("\nUpper Triangle Form:");
                printLeftTopHalf(differenceTable);

                System.out.println("\n Newton's Form Polynomial: ");
                printNewtonInterpolatingPolynomial(xValues, differenceTable);
                System.out.println();

                System.out.println("\n Lagrange's Form Polynomial: ");
                printLagrangeInterpolationFormula(xValues, differenceTable);
                System.out.println();

                System.out.println("\n Simplified Polynomial: ");
                printSimplifiedPolynomial(xValues, differenceTable);
                System.out.println();

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

    public static void printNewtonInterpolatingPolynomial(double[] xValues, double[][] dividedDifferenceTable)
    {
        System.out.print("Newton(x) = ");
        StringBuilder sb = new StringBuilder();
        int length = xValues.length;
        sb.append(String.valueOf(dividedDifferenceTable[0][0]));

        for(int i=1; i<length;i++ )
        {
            StringBuilder sbTerm = new StringBuilder();
            sbTerm.append(" + " + dividedDifferenceTable[0][i]);
            
            for (int j = 0; j < i; j++) 
                sbTerm.append(" * (x - " + xValues[j] + " )" ); 

            sb.append(sbTerm.toString());
        }
        System.out.print(sb.toString());
    }

    private static void printLagrangeInterpolationFormula(double[] xValues, double[][] dividedDifferenceTable)
    {
        double[] fxValues = new double[dividedDifferenceTable.length];
        for (int i = 0; i < fxValues.length; i++)
            fxValues[i] = dividedDifferenceTable[i][0];    
        
        System.out.print("Lagrange(x) = ");
        for (int i = 0; i < xValues.length; i++) 
        {
            double termCoefficient = fxValues[i];

            for (int j = 0; j < xValues.length; j++) 
                if (j != i) 
                {
                    termCoefficient /= (xValues[i] - xValues[j]);
                    System.out.print("( x - " + xValues[j] + ")");
                }
            
            System.out.print(" * " + termCoefficient);

            if (i < xValues.length - 1) 
                System.out.print(" + ");
        }

    }

    private static void printSimplifiedPolynomial(double[] xValues, double[][] dividedDifferenceTable) 
    {
        System.out.print("Simple(x) = " + dividedDifferenceTable[0][0]);

        for (int i = 1; i < xValues.length; i++) 
            System.out.print(" + " + formatSimplifiedTerm(dividedDifferenceTable[0][i], xValues, i));

        System.out.println();
    }

    private static String formatSimplifiedTerm(double coefficient, double[] xValues, int index) 
    {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < index; i++) 
            result.append("(x - ").append(xValues[i]).append(") ");

        return coefficient == 0 ? "" : coefficient + " * " + result.toString().trim();
    }

}
    


