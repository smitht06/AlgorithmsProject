public class Variables {
    private int cost;
    private boolean isTrue;

    public Variables(){};

    public Variables(int cost, boolean isTrue) {
        this.cost = cost;
        this.isTrue = isTrue;
    }

    public int getCost() {
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
