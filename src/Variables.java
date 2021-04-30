/*
 * Authors: Andrew Hartsfield, Anthony Smith
 * Description: This class is a template for the Variables Object
 * */

public class Variables {

    private double cost;
    private boolean isTrue;
    private String name;

    public Variables(){};

    public Variables(String name, double cost) {
        this.cost = cost;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }
}
