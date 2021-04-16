import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Derandomization {
    //array of variables
    private static ArrayList<Variables> variables = new ArrayList<>();
    private static ArrayList<Clauses> clauses = new ArrayList<>();

    public static void main(String [] args){
         readFromFile("Data/variables.csv");
         readFromFile("Data/clauses.csv");
         Derandomization.expectedWeight(clauses);
         System.out.println("Complete");
    }

    //function for expected weight
    //x1 = 1
    //return expected weights, choose the value of the higher weight
    public static void expectedWeight(ArrayList<Clauses> clauses){
        ArrayList<Double> expectedWeightsFalse = new ArrayList<>();
        ArrayList<Double> expectedWeightsTrue = new ArrayList<>();
        for(Variables variable : variables){
            //check clause for variable and determine expected weight
            double expectedWeightIfFalse = getExpectedWeightIfFalse(clauses, expectedWeightsFalse, variable);
            double expectedWeightIfTrue = getExpectedWeightIfTrue(clauses, expectedWeightsTrue, variable);
            chooseHigherWeight(clauses, variable, expectedWeightIfFalse, expectedWeightIfTrue);
        }
    }

    private static void chooseHigherWeight(ArrayList<Clauses> clauses, Variables variable, double expectedWeightIfFalse, double expectedWeightIfTrue) {
        if(expectedWeightIfFalse > expectedWeightIfTrue){
            variable.setTrue(false);
            for (Clauses clause : clauses){
                if (clause.getVariablesInClause().contains(variable)){
                    clause.setNumberOfVariables(clause.getNumberOfVariables() - 1 );
                }}
        }else {
            variable.setTrue(true);
            for (Clauses clause : clauses){
                if (clause.getVariablesInClause().contains(variable)){
                    clause.setSatisfied(true);
                }
            }
        }
    }

    private static double getExpectedWeightIfTrue(ArrayList<Clauses> clauses, ArrayList<Double> expectedWeightsTrue, Variables variable) {
        double expectedWeight;
        double expectedWeightIfTrue;
        for(Clauses clause : clauses){
            if (clause.getVariablesInClause().contains(variable) || clause.isSatisfied()){
                expectedWeight = clause.getWeight();
            }
            else {
                expectedWeight = (1 -  Math.pow(.5 , (clause.getNumberOfVariables()))) * clause.getWeight();
            }
            expectedWeightsTrue.add(expectedWeight);
        }
        expectedWeightIfTrue = sum(expectedWeightsTrue);
        expectedWeightsTrue.clear();
        System.out.println("Expected weight if "+ variable.getName() + " is true: " + expectedWeightIfTrue);
        return expectedWeightIfTrue;
    }

    private static double getExpectedWeightIfFalse(ArrayList<Clauses> clauses, ArrayList<Double> expectedWeightsFalse, Variables variables) {
        double expectedWeight;
        double expectedWeightIfFalse;
        for(Clauses clause : clauses){

            if (clause.getVariablesInClause().contains(variables) && !clause.isSatisfied()) /*and none of the variables are true */{
                expectedWeight = (1 -  Math.pow(.5 , (clause.getNumberOfVariables() - 1/**/))) * clause.getWeight();
            }else if(clause.isSatisfied()){
                expectedWeight = clause.getWeight();
            }
            else{
                expectedWeight = (1 -  Math.pow(.5 , (clause.getNumberOfVariables()))) * clause.getWeight();
            }
            expectedWeightsFalse.add(expectedWeight);
        }
        expectedWeightIfFalse = sum(expectedWeightsFalse);
        expectedWeightsFalse.clear();
        System.out.println("Expected weight if false: " + expectedWeightIfFalse);
        return expectedWeightIfFalse;
    }

    public static double sum(ArrayList<Double> list) {
        double sum = 0;
        for (double i: list) {
            sum += i;
        }
        return sum;
    }

    public static void readFromFile(String fileName){
      try {
            File file = new File(fileName);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] stringArray = data.split(",");
                if (fileName.contains("variables")){
                    createVariableFromFile(stringArray);
                }
                else {
                    createClauseFromFile(stringArray);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
   }

    public static void createVariableFromFile(String[] record) {
        Variables variable = new Variables(record[0], Integer.parseInt(record[1]));
        variables.add(variable);
    }

    public static void createClauseFromFile(String[] record){
        ArrayList<Variables> variablesInClause = new ArrayList<>();
        for (int i = 1; i < record.length; i++){
            for (Variables var:variables){
                if (record[i].equals(var.getName())){
                    variablesInClause.add(var);
                }
            }
        }
        Clauses clause = new Clauses(variablesInClause, Integer.parseInt(record[0]));
        clauses.add(clause);
    }
}
