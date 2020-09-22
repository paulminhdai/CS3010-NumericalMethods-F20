/*
who: [Your Name]
what: CS1400.[section]
when: [date]
why: [project name]
*/

import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

public class test2 {

    public static void gaussianElimination(double [][] m) {
        int n = m.length;

        // Find the scale vector and store them in array scale.
        double [] scale = new double[n];
        for(int r=0; r<n; r++) {
            double max = 0;
            for(int c=0; c<n; c++) {
                if(Math.abs(m[r][c]) > max)
                    max = Math.abs(m[r][c]); 
            }
            scale[r] = max;
        }
        // Print out the scale vector
        System.out.print("The scale vector: ");
        for(int s=0; s<n; s++)
            System.out.printf("%6.2f",scale[s]);
        System.out.printf("%n%n");
        

        for(int i=0; i<n; i++) {

            //print the matrix
            for(int r=0; r<n; r++) {
                for(int c=0; c<n+1; c++) {
                    System.out.printf("%6.2f",m[r][c]);
                }
                System.out.printf("%n");
            }

            
            pivot(m, n, i, scale); // call pivot funtion to calculate scaled pivot and max

            // Eliminate each row 
            for(int row=i+1; row<n; row++) {
                double div = m[row][i]/m[i][i];
                for(int col=0; col<n+1; col++) {
                    m[row][col] = (m[row][col] - (m[i][col]*(div)));
                }
            }
        }

        // Detect infinite solutions (no unique solution)
        try {
            if(m[n-1][n-1] == 0 && m[n-1][n] == 0)
                throw new Exception("There's no unique solution");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        // Detect no solutions
        try {
            if(m[n-1][n-1] == 0)
                throw new Exception("There's no solution");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        // Find x1,x2,x3,... after eliminate.
        double [] result = new double[n];
        for(int i=n-1; i>=0; i--) {
            double add = 0;
            for(int j=i; j<n; j++) {
                add = add + (m[i][j] * result[j]);
                result[i] = (m[i][n] - add) / m[i][i];  
            }
        }

        // Print out the result
        System.out.println("The result is: ");
        for(int res=0; res<result.length; res++){
            System.out.printf("x%d = %5.2f%n",(res+1), result[res]);
        }
        System.out.printf("%n");
    };

    // Pivot function calculate the scaled ratio and swap the rows.
    // Print out the scaled ratio and the selected row.
    public static void pivot(double [][]m, int n, int i, double [] scale) {
        double max = -1e100;
        int maxRow = 0;

        System.out.print("Scaled ratio: (");
        for(int coef=i; coef < n; coef++) {

            //print out the ratio
            System.out.printf("%5.2f",Math.abs(m[coef][i]/scale[coef])); // Print each scaled value

            if(max < Math.abs(m[coef][i]/scale[coef])) {
                maxRow = coef;
                max = Math.abs(m[coef][i]/scale[coef]);
            }
        }
        int rowSelected = maxRow + 1;
        System.out.println(" )");
        System.out.println("row " + rowSelected + " is selected."); // Print the selected row
        System.out.println("\n");

        // Swap the scaled pivot array
        double temp = scale[i];
        scale[i] = scale[maxRow];
        scale[maxRow] = temp;
        // Swap the row of the matrix
        double [] temp1 = m[i];
        m[i] = m[maxRow];
        m[maxRow] = temp1;
        
    }
    
    // Function enter info 
    private static double [][] enterMatrix() {
        Scanner kb = new Scanner(System.in);
        int row = 0;

        try {
            System.out.println("Enter the number of equations: ");
            row = kb.nextInt();
        }
        catch (Exception e) {
            System.out.println("Enter the invalid value.");
        }

        double [][] matrix = new double [row][row+1]; // declare matrix

        int option;
        
        System.out.println("Do you want to enter the equations in the console or enter a file name?\nEnter:\n1 to enter equations.\n2 to enter file name.");
        option = kb.nextInt();
        while (option < 1 || option > 2) {
            System.out.println("Enter 1 or 2 again to choose options: ");
            option = kb.nextInt();
        }

        if (option == 1) { //import equations from console
            try {
                System.out.println("Enter each equation row by row: ");

                for (int i=0; i<row; i++) {
                    for (int j=0; j<row+1; j++) {
                        matrix[i][j] = kb.nextInt();
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Enter the invalid value.");
            }
        }

        
        if (option == 2) { // import equations from text file
            
            System.out.println("Enter a file.");
        
        Scanner readMatrixFile = new Scanner("");
        try {
            readMatrixFile = new Scanner(new File(kb.next()));
            for(int i = 0; i < matrix.length; i++) {
                for(int j = 0; j < matrix[i].length; j++) {
                    double coefficient = readMatrixFile.nextDouble();
                    matrix[i][j] = coefficient;
                    System.out.println(coefficient + " entered.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid file or invalid value detected. Check the file and coefficients and try again.");
            return null;
        }
            /*try {
                Scanner input = new Scanner(new File(fileName));
                for(int r = 0; r < row; r++) {
                    for(int c = 0; c < row+1; c++) {
                        if(input.hasNextInt()) {
                            matrix[r][c] = input.nextInt();
                        }
                    }
                }
                input.close();
            } 
            catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }*/
        }
        return matrix;
    };


    
    public static void main(String args [] ) {
        
        double [][]m = enterMatrix(); // Enter the number of equations and input the coefficients. 
        
        
        // Test cases
        /*double [][]m = {{2,3,0,8},
                          {-1,2,-1,0},
                          {3,0,2,9}};*/

        // No unique solution
        /*double [][]m = {{-3,-5,36,10},
                          {-1,0,7,5},
                          {1,1,-10,-4}};*/
        
        // No solution
        /*double [][]m = {{1,1,1,2},
                        {0,1,-3,1},
                        {2,1,5,0}};*/


        /*double [][]m = {{3,-13,9,3,-19},
                        {-6,4,1,18,-34},
                        {6,-2,2,4,16},
                        {12,-8,6,10,26}};*/

        /*double [][]m = {{1,0,3,0,0},
                        {0,1,3,-1,0},
                        {3,-3,0,6,0},
                        {0,2,4,6,0}};*/

        gaussianElimination(m);

    }
}