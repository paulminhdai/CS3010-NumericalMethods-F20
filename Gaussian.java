import java.util.Arrays;

public class Gaussian {

    public static void gaussianElimination(double [][] m) {
        int n = m.length;

        // Find the scale vector and store them in array scale.
        double [] scale = new double[n];
        for(int i=0; i<n; i++) {
            double max = 0;
            for(int j=0; j<n; j++) {
                if(Math.abs(m[i][j]) > max)
                    max = Math.abs(m[i][j]); 
            }
            scale[i] = max;
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

            pivot(m, n, i, scale);

            
            for(int row=i+1; row<n; row++) {
                double div = m[row][i]/m[i][i];
                for(int col=0; col<n+1; col++) {
                    m[row][col] = (m[row][col] - (m[i][col]*(div)));
                }
            }
        }
        /*if(m[n-1][n-1] == 0)
            throw new Exception("There's no unique solution");*/

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
        System.out.print("The result is: ");
        for(int res=0; res<result.length; res++){
            System.out.printf("%5.2f", result[res]);
        }
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

        // Swap rows
        double temp = scale[i];
        scale[i] = scale[maxRow];
        scale[maxRow] = temp;
        
        double [] temp1 = m[i];
        m[i] = m[maxRow];
        m[maxRow] = temp1;
        
    }
    
    
    public static void main(String args []) {
        
        // Test cases
        double [][]m = {{2,3,0,8},{-1,2,-1,0},{3,0,2,9}};
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