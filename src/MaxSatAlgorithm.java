import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MaxSatAlgorithm {
    private static final double budget = 17.62;
    private static double cost = 0;
    private static ArrayList<Variables> variables = new ArrayList<>();
    private static ArrayList<Clauses> clauses = new ArrayList<>();


    public static void main(String [] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter 1 to the derandomized algorithm, 2 for randomized:");
        String argument = scanner.nextLine();

        if (argument.equals("1") || argument.equals("2")){
            readFromFile("../Data/variables.csv");
            readFromFile("../Data/clauses.csv");
            algorithm(argument);
        }
        else {
            System.out.println("An incorrect was argument given. Please try running again using 1 or 2 as an argument.");
        }
    }

    private static void algorithm(String algorithm){
        if (algorithm.equals("1")){
            derandomize();
            printResults();
            System.out.println("Complete");
        }
        else if (algorithm.equals("2")) {
            randomization(variables, clauses);
        }
    }

    private static void derandomize(){
        for(Variables variable : variables){
            //check clause for variable and determine expected weight
            System.out.println("Setting each variable to true and false to see expected weight.");
            double expectedWeightIfFalse = getExpectedWeightIfFalse(variable);
            double expectedWeightIfTrue = getExpectedWeightIfTrue(variable);
            chooseHigherWeight(variable, expectedWeightIfFalse, expectedWeightIfTrue);
        }
    }

    private static void chooseHigherWeight(Variables variable, double expectedWeightIfFalse, double expectedWeightIfTrue) {
        if(expectedWeightIfFalse > expectedWeightIfTrue){
            System.out.println(variable.getName() + " is set to false.");
            variable.setTrue(false);
            for (Clauses clause : clauses){
                if (clause.getVariablesInClause().contains(variable)){
                    clause.setNumberOfVariables(clause.getNumberOfVariables() - 1 );
                }
            }
            pop(variable);
        }
        else if ((expectedWeightIfFalse <= expectedWeightIfTrue) && (cost + variable.getCost()) < budget){
            System.out.println(variable.getName() + " is set to true.");
            variable.setTrue(true);
            cost = cost + variable.getCost();
            for (Clauses clause : clauses){
                if (clause.getVariablesInClause().contains(variable)){
                    clause.setSatisfied(true);
                }
            }
        }
        else {
            System.out.println("Adding variable would go over budget. Variable set to false.");
            variable.setTrue(false);
            pop(variable);
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

    private static double sum(ArrayList<Double> list) {
        double sum = 0;
        for (double i: list) {
            sum += i;
        }
        return sum;
    }

    private static void pop(Variables variable){
        for (Clauses clause: clauses){
            if (clause.getVariablesInClause().contains(variable)){
                clause.remove(variable);
            }
        }
    }

    private static void readFromFile(String fileName){
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

    private static void createVariableFromFile(String[] record) {
        Variables variable = new Variables(record[0], Double.parseDouble(record[1]));
        variables.add(variable);
    }

    private static void createClauseFromFile(String[] record){
        ArrayList<Variables> variablesInClause = new ArrayList<>();
        for (int i = 1; i < record.length; i++){
            for (Variables var:variables){
                if (record[i].equals(var.getName())){
                    variablesInClause.add(var);
                }
            }
        }
        Clauses clause = new Clauses(variablesInClause, Double.parseDouble(record[0]));
        clauses.add(clause);
    }

    private static void printResults(){
        double totalWeight = 0;
        System.out.println("The final results are: ");
        System.out.print("Variables ");
        for (Variables variable: variables) {
            if (variable.isTrue()){
                System.out.print(variable.getName() + " ");
            }
        }
        System.out.println(" are true. With a final cost of " + cost);
        System.out.print("The following weight is satisfied: ");
        for (Clauses clause: clauses){
            if (clause.isSatisfied()){
                totalWeight = totalWeight + clause.getWeight();
            }
        }
        System.out.println("" + totalWeight + " in thousands of people.");
    }

    private static double randomization(ArrayList<Variables> variables, ArrayList<Clauses> clauses){
        Random randomBool = new Random();
        double weight = 0;
        double runningCost = 0;
        for(Variables variable : variables) {
            Boolean nextBool = randomBool.nextBoolean();
            if (nextBool && (runningCost + variable.getCost() > budget)){
                nextBool = false;
            }
            variable.setTrue(nextBool);
            System.out.println(variable.getName() + " " + variable.isTrue());
            if(variable.isTrue()){
                runningCost+=variable.getCost();
            }
        }

        for(Variables variables1 : variables) {
            for (Clauses clause : clauses) {
                if (variables1.isTrue() && clause.getVariablesInClause().contains(variables1) && !clause.isSatisfied()) {
                    weight += clause.getWeight();
                    clause.setSatisfied(true);
                }
            }
        }
        System.out.println(weight);
        System.out.println(runningCost);
        return weight;
    }
}
