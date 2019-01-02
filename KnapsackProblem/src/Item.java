public class Item {
    // Member variables //
    int cost;
    int weight;

    // Constructors //
    public Item(int weight, int cost) {
        this.weight = weight;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
