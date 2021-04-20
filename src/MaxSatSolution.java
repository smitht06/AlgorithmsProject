import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MaxSatSolution {
    //array of variables
    private static ArrayList<Variables> variables = new ArrayList<>();
    private static ArrayList<Clauses> clauses = new ArrayList<>();

    public static void main(String [] args){
         readFromFile("Data/variables.csv");
         readFromFile("Data/clauses.csv");
//         Derandomization.algorithm();
//         System.out.println("Complete");
           randomization(variables, clauses);
    }

    //function for expected weight
    //x1 = 1
    //return expected weights, choose the value of the higher weight
    public static void algorithm(){
        for(Variables variable : variables){
            //check clause for variable and determine expected weight
            double expectedWeightIfFalse = getExpectedWeightIfFalse(variable);
            double expectedWeightIfTrue = getExpectedWeightIfTrue(variable);
            chooseHigherWeight(variable, expectedWeightIfFalse, expectedWeightIfTrue);
        }
    }

    private static void chooseHigherWeight(Variables variable, double expectedWeightIfFalse, double expectedWeightIfTrue) {
        if(expectedWeightIfFalse > expectedWeightIfTrue){
            System.out.println(variable.getName() + "is set to false.");
            variable.setTrue(false);
            for (Clauses clause : clauses){
                if (clause.getVariablesInClause().contains(variable)){
                    clause.setNumberOfVariables(clause.getNumberOfVariables() - 1 );
                }}
        }else {
            System.out.println(variable.getName() + "is set to true.");
            variable.setTrue(true);
            for (Clauses clause : clauses){
                if (clause.getVariablesInClause().contains(variable)){
                    clause.setSatisfied(true);
                }
            }
        }
    }

    private static double getExpectedWeightIfTrue(Variables variable) {
        double expectedWeight;
        double expectedWeightIfTrue;
        ArrayList<Double> expectedWeightsTrue = new ArrayList<>();

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
        System.out.println("Expected weight if "+ variable.getName() + " is true: " + expectedWeightIfTrue);
        return expectedWeightIfTrue;
    }

    private static double getExpectedWeightIfFalse(Variables variable) {
        double expectedWeight;
        double expectedWeightIfFalse;
        ArrayList<Double> expectedWeightsFalse = new ArrayList<>();

        for(Clauses clause : clauses){

            if (clause.getVariablesInClause().contains(variable) && !clause.isSatisfied()) /*and none of the variables are true */{
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
        System.out.println("Expected weight if " + variable.getName() + " is  false: " + expectedWeightIfFalse);
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

    public static void randomization(ArrayList<Variables> variables, ArrayList<Clauses> clauses){
        Random random = new Random();
        ArrayList<Double> expectedWeights = new ArrayList<>();
        double weight = 0;
        Variables x1 = variables.get(0);
        Variables x2 = variables.get(1);
        Variables x3 = variables.get(2);
        x1.setTrue(false);
        x2.setTrue(false);
        x3.setTrue(true);
//        for(Variables variable : variables){
//            variable.setTrue(random.nextBoolean());
//        }
        for (Variables variable :variables){
            int runCount = 1;
            System.out.println(variable.getName() + " " + variable.isTrue());
            int clauseNumber = 1;
            int cost = 0;
            int resources = 5;
            for (Clauses clause:clauses){
                expectedWeights.clear();
                clause.setNumberOfVariables(clause.getVariablesInClause().size());
                ArrayList<Variables> variablesinclause = clause.getVariablesInClause();
                if (variable.isTrue() && clause.getVariablesInClause().contains(variable)){
                    weight = clause.getWeight();
                    clause.setSatisfied(true);
                    cost += variable.getCost();
                    expectedWeights.add(weight);

                    //System.out.println("Variable " + variable.getName() + " is true, the weight is " + weight);
                }else if(!variable.isTrue() && clause.getVariablesInClause().contains(variable) && !clause.isSatisfied()){
                    clause.setNumberOfVariables(clause.getNumberOfVariables() - 1);
                    //System.out.println("number of variables remaining in clause " + clauseNumber +" "+ clause.getNumberOfVariables());
                    weight = ((1 -  Math.pow(.5 , (clause.getNumberOfVariables()))) * clause.getWeight());
                    expectedWeights.add(weight);
                    cost += variable.getCost();

                    //System.out.println("Variable " + variable.getName() + " is false, the weight is " + weight);
                }
                else{
                    //System.out.println("This variable is not in this clause and it is not satisfied");
                    weight = (1 -  Math.pow(.5 , (clause.getNumberOfVariables()))) * clause.getWeight();
                    expectedWeights.add(weight);

            }
               for(double weights:expectedWeights){
                   System.out.println(clauseNumber + " " +weights);
               }
                clauseNumber++;

            }
        }}



    }

