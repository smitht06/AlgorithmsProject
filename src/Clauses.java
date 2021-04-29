import java.util.ArrayList;

public class Clauses {
    private ArrayList<Variables> variablesInClause;
    private double weight;
    private int numberOfVariables;
    private boolean isSatisfied;
    private int clauseNumber;
    private double variablesToSatisfy;

    public Clauses(){};

    public Clauses(ArrayList<Variables> variablesInClause, double weight) {
        this.variablesInClause = variablesInClause;
        this.weight = weight;
        this.numberOfVariables = this.getNumberOfVariables();
        this.variablesToSatisfy = (double) this.getNumberOfVariables()/2;
    }

    public int getClauseNumber() {
        return clauseNumber;
    }

    public void setClauseNumber(int clauseNumber) {
        this.clauseNumber = clauseNumber;
    }

    public ArrayList<Variables> getVariablesInClause() {
        return variablesInClause;
    }

    public void remove(Variables variable){
        this.variablesInClause.remove(variable);
    }

    public double getWeight() {
        return weight;
    }

    public int getNumberOfVariables(){
        return this.getVariablesInClause().size();
    }

    public void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    public boolean isSatisfied() {
        return isSatisfied;
    }

    public void setSatisfied(boolean satisfied) {
        isSatisfied = satisfied;
    }

    public void setVariablesToSatisfy(double variablesToSatisfy) {
        this.variablesToSatisfy = variablesToSatisfy;
    }

    public double getVariablesToSatisfy() {
        return variablesToSatisfy;
    }

    public boolean twoVarTrueOrMore(){
        boolean halfTrue;
        int counter= 0;
        double ratio = 0;
        for(Variables variables: variablesInClause){
            if (variables.isTrue()){
                counter++;
            }
        }
        ratio = (double)variablesInClause.size()/2;
        System.out.println("ratio: " + ratio);
        if (counter >= ratio){
            halfTrue = true;
        }else{
            halfTrue = false;
        }
        return halfTrue;
    }

    public double calculateExpectedWeight(int booleanFlag){
        return (1 -  (Math.pow(.5 , (this.variablesToSatisfy - booleanFlag)))) * this.weight;
    }
}
