import java.util.ArrayList;

public class Derandomization {
    //array of variables
    //read from file
    private static ArrayList<Variables> variables = new ArrayList<>();

    //read from file
    private static ArrayList<Clauses> clauses = new ArrayList<>();

    public static void main(String [] args){
         Variables x1 = new Variables("x1",12);
         variables.add(x1);

         Variables x2 = new Variables("x2", 6);
         variables.add(x2);

         Clauses S1 = new Clauses(variables, 12);
         clauses.add(S1);

         Clauses S2 = new Clauses(variables, 15);

         Derandomization.expectedWeight(clauses);
    }

    //function for expected weight
    //x1 = 1
    //return expected weights, choose the value of the higher weight
    public static double expectedWeight(ArrayList<Clauses> clauses){
        ArrayList<Double> expectedWeightsFalse = new ArrayList<>();
        ArrayList<Double> expectedWeightsTrue = new ArrayList<>();
        for(Variables variables : variables){
            double expectedWeight = 0;
            double expectedWeightIfFalse = 0;
            double expectedWeightIfTrue = 0;
            //check clause for variable and determine expected weight
            expectedWeightIfFalse = getExpectedWeightIfFalse(clauses, expectedWeightsFalse, variables);
            expectedWeightIfTrue = getExpectedWeightIfTrue(clauses, expectedWeightsTrue, variables);
            chooseHigherWeight(clauses, variables, expectedWeightIfFalse, expectedWeightIfTrue);
        }
        return 0;
    }

    private static void chooseHigherWeight(ArrayList<Clauses> clauses, Variables variables, double expectedWeightIfFalse, double expectedWeightIfTrue) {
        if(expectedWeightIfFalse > expectedWeightIfTrue){
            variables.setTrue(false);
            expectedWeightIfFalse = 0;
            for (Clauses clauses2 : clauses){
                if (clauses2.getVariablesInClause().contains(variables)){
                    clauses2.setNumberOfVariables(clauses2.getNumberOfVariables() - 1 );
                }}
            System.out.println(expectedWeightIfFalse);
        }else {
            variables.setTrue(true);
            expectedWeightIfFalse = 0;
            for (Clauses clauses2 : clauses){
                if (clauses2.getVariablesInClause().contains(variables)){
                    clauses2.setSatisfied(true);
                }
            }
            System.out.println("this should print");
            System.out.println(expectedWeightIfFalse);
        }
    }

    private static double getExpectedWeightIfTrue(ArrayList<Clauses> clauses, ArrayList<Double> expectedWeightsTrue, Variables variables) {
        double expectedWeight;
        double expectedWeightIfTrue;
        for(Clauses clauses1 : clauses){
            if (clauses1.getVariablesInClause().contains(variables)){
                expectedWeight = clauses1.getWeight();
            }else if (clauses1.isSatisfied()){
                expectedWeight = clauses1.getWeight();
            }
            else {
                expectedWeight = (1 -  Math.pow(.5 , (clauses1.getNumberOfVariables()))) * clauses1.getWeight();
            }
            expectedWeightsTrue.add(expectedWeight);
        }
        expectedWeightIfTrue = sum(expectedWeightsTrue);
        expectedWeightsTrue.clear();
        System.out.println("Expected weight if "+ variables.getName() + " is true: " + expectedWeightIfTrue);
        return expectedWeightIfTrue;
    }

    private static double getExpectedWeightIfFalse(ArrayList<Clauses> clauses, ArrayList<Double> expectedWeightsFalse, Variables variables) {
        double expectedWeight;
        double expectedWeightIfFalse;
        for(Clauses clauses1 : clauses){
            System.out.println("Back at beginning");
            if (clauses1.getVariablesInClause().contains(variables) && !clauses1.isSatisfied()) /*and none of the variables are true */{
                expectedWeight = (1 -  Math.pow(.5 , (clauses1.getNumberOfVariables() - 1/**/))) * clauses1.getWeight();
            }else if(clauses1.isSatisfied()){
                expectedWeight = clauses1.getWeight();
            }
            else{
                expectedWeight = (1 -  Math.pow(.5 , (clauses1.getNumberOfVariables()))) * clauses1.getWeight();
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

    //public static ArrayList<String> readFromFile(String fileName){

   // }
}
