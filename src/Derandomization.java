import java.util.ArrayList;

public class Derandomization {
    //array of variables
    //read from file
    private static ArrayList<Variables> variables = new ArrayList<>();

    //read from file
    private static ArrayList<Clauses> clauses = new ArrayList<>();


    //function for expected weight
    //x1 = 1
    //return expected weights, choose the value of the higher weight
    public double expectedWeight(ArrayList<Clauses> clauses){
        ArrayList<Double> expectedWeightsFalse = new ArrayList<>();
        ArrayList<Double> expectedWeightsTrue = new ArrayList<>();
        for(Variables variables : variables){
            double i = 0;
            //check clause for variable and determine expected weight
            for(Clauses clauses1 : clauses){
                if (clauses1.getVariablesInClause().contains(variables)) /*and none of the variables are true */{
                    i = (1 -  Math.pow(.5 , (clauses1.getNumberOfVariables() - 1/**/))) * clauses1.getWeight();
                }else {
                    i = (1 -  Math.pow(.5 , (clauses1.getNumberOfVariables()))) * clauses1.getWeight();
                }
                expectedWeightsFalse.add(i);
            }
            double expectedWeightIfFalse = sum(expectedWeightsFalse);





            for(Clauses clauses1 : clauses){
                if (clauses1.getVariablesInClause().contains(variables)){
                    i = clauses1.getWeight();
                }else {
                    i = (1 -  Math.pow(.5 , (clauses1.getNumberOfVariables()))) * clauses1.getWeight();
                }
                expectedWeightsTrue.add(i);
            }
            double expectedWeightIfTrue = sum(expectedWeightsTrue);
            if(expectedWeightIfFalse > expectedWeightIfTrue){
                return expectedWeightIfFalse;
            }else {
                return expectedWeightIfTrue;
            }
        }
        return 0;
    }

    public static double sum(ArrayList<Double> list) {
        double sum = 0;
        for (double i: list) {
            sum += i;
        }
        return sum;
    }
}
