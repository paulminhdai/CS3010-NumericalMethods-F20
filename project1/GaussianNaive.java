/* ==========================================================
Dai Vuong (Paul)
CS3010.01
Sept 22th, 2020
Gaussian Elimination Naive.
=============================================================*/


import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

public class GaussianNaive {

    public static void gaussianElimination(double [][] matrix) {
        int n = matrix.length;

        for(int i=0; i<n; i++) {

            //print the matrix
            for(int r=0; r<n; r++) {
                for(int c=0; c<n+1; c++) {
                    System.out.printf("%6.2f",matrix[r][c]);
                }
                System.out.printf("%n");
            }

            // Eliminate each row 
            for(int row=i+1; row<n; row++) {
                double div = matrix[row][i]/matrix[i][i];
                for(int col=0; col<n+1; col++) {
                    matrix[row][col] = (matrix[row][col] - (matrix[i][col]*(div)));
                }
            }
        }

        // Detect infinite solutions (no unique solution)
        try {
            if(matrix[n-1][n-1] == 0 && matrix[n-1][n] == 0)
                throw new Exception("There's no unique solution");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        // Detect no solutions
        try {
            if(matrix[n-1][n-1] == 0)
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
                add = add + (matrix[i][j] * result[j]);
                result[i] = (matrix[i][n] - add) / matrix[i][i];  
            }
        }

        // Print out the result
        System.out.println("The result is: ");
        for(int res=0; res<result.length; res++){
            System.out.printf("x%d = %5.2f%n",(res+1), result[res]);
        }
        System.out.printf("%n");
    };

    
    // Function enter info 
    private static double [][] enterMatrix() {
        Scanner kb = new Scanner(System.in);
        int row = 0;

        try {
            System.out.print("Enter the number of equations: ");
            row = kb.nextInt();
            System.out.println();
        }
        catch (Exception e) {
            System.out.println("Enter the invalid value.");
        }

        double [][] matrix = new double [row][row+1]; // declare matrix

        int option;
        
        System.out.println("Do you want to enter the equations in the console or enter a file name? Choose:\n\t1 - enter coefficients equations.\n\t2 - enter file name.");
        option = kb.nextInt();
        while (option < 1 || option > 2) {
            System.out.print("Enter 1 or 2 again to choose options: ");
            option = kb.nextInt();
            System.out.println();
        }

        if (option == 1) { //import equations from console
            try {
                System.out.println("Enter each equation row by row: ");

                for (int r=0; r<row; r++) {
                    for (int c=0; c<row+1; c++) {
                        matrix[r][c] = kb.nextInt();
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Invalid value.");
                System.exit(0);
            }
        }

        
        if (option == 2) { // import equations from text file
            
            System.out.print("Enter the file name: ");
            Scanner read = new Scanner("");
            try {
                read = new Scanner(new File(kb.next()));
                for(int r = 0; r < row; r++) {
                    for(int c = 0; c < row+1; c++) {
                        matrix[r][c] = read.nextDouble();
                    }
                }
            } 
            catch (Exception e) {
                System.out.println("An error occurred. Either the file is not found or invalid values.");
                e.printStackTrace();
                System.exit(0);
            }
            read.close();
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