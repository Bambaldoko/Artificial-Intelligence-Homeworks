public class PairOfBags {
    // Member variables //
    Bag bag1;
    Bag bag2;

    // Constructor //
    PairOfBags() {
    }

    PairOfBags(Bag bag1, Bag bag2) {
        this.bag1 = bag1;
        this.bag2 = bag2;
    }

    // Methods //
    public Bag getBag1() {
        return bag1;
    }

    public Bag getBag2() {
        return bag2;
    }

    public void setBag1(Bag bag1) {
        this.bag1 = bag1;
    }

    public void setBag2(Bag bag2) {
        this.bag2 = bag2;
    }
}
