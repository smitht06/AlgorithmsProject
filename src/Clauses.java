import java.util.ArrayList;

public class Clauses {
    private ArrayList<Variables> variablesInClause;
    private int weight;
    private int numberOfVariables;
    private boolean isSatisfied;

    public Clauses(){};

    public Clauses(ArrayList<Variables> variablesInClause, int weight) {
        this.variablesInClause = variablesInClause;
        this.weight = weight;
    }

    public ArrayList<Variables> getVariablesInClause() {
        return variablesInClause;
    }

    public void setVariablesInClause(ArrayList<Variables> variablesInClause) {
        this.variablesInClause = variablesInClause;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getNumberOfVariables(){
        return this.numberOfVariables;
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
}
