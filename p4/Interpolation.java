/**
* program that creates a divided difference table from the given data in a text file and
* uses that to create the interpolating polynomial
*
* @author  Dai (Paul) Vuong
* @since   2020-11-10
* CS 3010.01 - Cal Poly Pomona
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;


public class Interpolation {
    private double[][] divided_diff_table;
    private double[] diagonal;
    
    /**
     * Read data from input file and put in the 2D array table
     * @param filename the file contains x and fx
     * @throws FileNotFoundException
     */
    public void read_data(String filename) throws FileNotFoundException{
        String[] x = null;
        String[] fx = null;
        
        File file = null;

        try {
            file = new File(filename);
        }
        catch (Exception e) {
            System.err.println(e);
        }

        Scanner sc = new Scanner(file);

        try {
            x = sc.nextLine().split(" ");
            fx = sc.nextLine().split(" ");
        }
        catch (Exception e) {
            System.err.println(e);
        }

        sc.close();

        divided_diff_table = new double[x.length][fx.length + 1];

        for (int i = 0; i < x.length; i++)
            divided_diff_table[i][0] = Double.parseDouble(x[i]);
        for (int i = 0; i < fx.length; i++)
            divided_diff_table[i][1] = Double.parseDouble(fx[i]);
    }

    /** Calculate the divided difference table 
     * And store the diagnal of the table to diagonal array.
    */
    public void divided_difference() {
        int n = divided_diff_table[0].length;

        for (int j = 2; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                divided_diff_table[i][j] = 
                (divided_diff_table[i+1][j-1] - divided_diff_table[i][j-1]) / 
                (divided_diff_table[i+(j-1)][0] - divided_diff_table[i][0]);
            }
        }
        diagonal = new double[divided_diff_table.length];
        for (int i = 1; i < divided_diff_table[0].length; i++)
            diagonal[i-1] = divided_diff_table[0][i];
    }


    /** Print out the divided difference table */
    public void print_table(){
        System.out.println("\nThe divided difference table:");
        int n = divided_diff_table[0].length;
        String[] tittle = new String[divided_diff_table.length + 1];
        tittle[0] = "x";
        tittle[1] = "y";
        for (int i = 2; i < tittle.length; i++) {
            String f = "f[";
            for (int j = 1; j < i; j++)
                f = f + ",";
            f = f + "]";
            tittle[i] = f;
        }
        for (int i = 0; i < tittle.length; i++) {
            System.out.printf("%12s", tittle[i]);
        }
        System.out.println();

        for(int i = 0; i < n - 1; i++){
            for(int j = 0; j < n - i; j++)
                System.out.printf("%12.3f", divided_diff_table[i][j]);
            System.out.println();
        }
        System.out.println();
    }

    /** String out the result to the polynomial in Newton's form.
     * @return string polynomial in unsimplified form
     */
    public String newton_form() {
        ArrayList<String> parenthesis_part = new ArrayList<String>();
        for (int i = 0; i < this.divided_diff_table.length - 1; i++) {
            double xValue = this.divided_diff_table[i][0];
            if (xValue == 0)
                parenthesis_part.add("(x)");
            else
                parenthesis_part.add(String.format("(x%+.3f)", -xValue));
        }

        String final_string = String.format("%.3f", diagonal[0]);

        for (int i = 1; i < parenthesis_part.size() + 1; i++) {
            double coef_val = diagonal[i];
            if (coef_val != 0) {
                String combine = "";
                for (int j = 0; j < i; j++)
                    combine += parenthesis_part.get(j);
                final_string += String.format("%+.3f%s", coef_val, combine);
            }
        }
        return final_string;
    }

    /** String out the result to the polynomial in simplified form.
     * @return string polynomial in simplified form
     */
    public String simplified_form() {
        ArrayList<Double> coeff = new ArrayList<Double>();
        ArrayList<ArrayList<Double>> array = new ArrayList<ArrayList<Double>>();

        // The constant term of a polynomial
        for(int i = 0; i < divided_diff_table[0].length - 1; i++)
            coeff.add(0.0);
        coeff.add(0, diagonal[0]);
        array.add(coeff);
        
        for(int i = 1; i < diagonal.length; i++){
            coeff = new ArrayList<Double>();
            for(int j = 0; j < i; j++)
                coeff.add(divided_diff_table[j][0]);
            double diagonal_const = diagonal[i];
            array.add(polynomial_func(diagonal_const, coeff, divided_diff_table[0].length));
        }
        
        coeff = combine_poly(array);
        
        String simplified_string = "";
        for (int  i = coeff.size() - 1; i >= 0; i--) {
            Double f = coeff.get(i);
            if (f != 0) {
                if (i == 0)
                    simplified_string += String.format(" %+.3f", f);
                else if (i == 1)
                    simplified_string += String.format(" %+.3fx", f);
                else 
                    simplified_string += String.format("%+.3f%s", f, String.format("x^%d", i));
            }
        }
        return simplified_string;
    }

    /** Expand the polynomial by distributing
     * @param constant the value from the digonal values
     * @param x_i list of x
     * @param size the size
     * @return  data after expanding and combining.
    */
    private ArrayList<Double> polynomial_func(double diagonal_const, ArrayList<Double> x_i, int size){
        ArrayList<ArrayList<Double>> array = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> storeY = new ArrayList<Double>();

        for(int i = 0; i < x_i.size() + 1; i++)
            storeY.add(0.0);
        storeY.add(0, diagonal_const);

        for(int i = 0; i < x_i.size(); i++){
            array.add(mul_to_x(storeY));
            array.add(mul_xi(storeY, -x_i.get(i)));
            storeY = combine_poly(array);
            array.clear();
        }
        int ysize = storeY.size();

        for(int i = 0; i < size - ysize; i++)
            storeY.add(0.0);

        return storeY;
    }

    /** Multiplying constant to x 
     * @param diagonal_const the constant value from the divided different table diagnal
     * @return The coefficient before x
    */
    private ArrayList<Double> mul_to_x(ArrayList<Double> diagonal_const){
        ArrayList<Double> value = new ArrayList<Double>();
        value.add(0.0);
        for(int i = 0; i < diagonal_const.size() - 1; i++)
            value.add(diagonal_const.get(i));
        return value;
    }

    /** Multiplying the constant
     * @param diagonal_const the constant value from the divided different table diagnal
     * @param x the value of x_i in (x - x_i)
     * @return The coefficients
    */
    private ArrayList<Double> mul_xi(ArrayList<Double> diagonal_const, double x){
        ArrayList<Double> value = new ArrayList<Double>();
        for(double i : diagonal_const)
            value.add(i * x);
        return value;
    }

    /** Combine the like terms 
     * @param array combine like terms after multiply the constant to x and x_i.
     * @return the combined values array.
    */
    private ArrayList<Double> combine_poly(ArrayList<ArrayList<Double>> value){
        ArrayList<Double> combined = new ArrayList<Double>();
        for(int i = 0; i < value.get(0).size(); i++){
            double sum = 0.0;
            for(int j = 0; j < value.size(); j++)
                sum = sum + value.get(j).get(i);
            
            combined.add(sum);
        }
        return combined;
    }
    
    public static void main(String[] args) throws FileNotFoundException, Exception {
        Scanner kb = new Scanner(System.in);
        System.out.print("Enter a filename: ");
        String filename = kb.nextLine();
        kb.close();
        Interpolation interpolation = new Interpolation();
        interpolation.read_data(filename);
        interpolation.divided_difference();
        interpolation.print_table();
        System.out.printf("%s%n%s%n%n", "The interpolating polynomial is:", 
                            interpolation.newton_form());
        System.out.printf("%s%n%s%n%n", "Simplified polynomial is:",
                            interpolation.simplified_form());
    }     
}