public class Bag {
    // Static variables //
    private static Item[] items;
    private static int maxWeight;

    public static int getMaxWeight() {
        return maxWeight;
    }

    // Static methods //
    static void initialize(Item[] items, int maxWeight) {
        Bag.items = items;
        Bag.maxWeight = maxWeight;
    }

    static int getCount() {
        return items.length;
    }

    // Member variables //
    private boolean[] availableItems;
    private int cost;
    private int weight;

    // Constructors //
    Bag() {
        availableItems = new boolean[items.length];
    }

    Bag(boolean[] availableItems) {
        this.availableItems = availableItems;
        calculateWeightAndCost();
    }

    Bag(boolean[] availableItems, int weight, int cost) {
        this.availableItems = availableItems;
        this.weight = weight;
        this.cost = cost;
    }

    // Public Methods //
    public int fitness() {
        return cost;
    }

    public boolean addItem(int index) {
        if(weight + items[index].weight <= maxWeight && !availableItems[index]) {
            weight += items[index].weight;
            cost += items[index].cost;
            availableItems[index] = true;
            return true;
        }
        return false;
    }

    public void alternateItem(int index) {
        if(availableItems[index]) {
            removeItem(index);
        } else {
            addItem(index);
        }
        calculateWeightAndCost();
    }

    private void removeItem(int index) {
        if(!availableItems[index]) return;

        availableItems[index] = false;
        weight -= items[index].weight;
        cost -= items[index].weight;
    }

    public int getWeight() {
        return weight;
    }

    public boolean[] getAvailableItems() {
        return availableItems;
    }

    public void print() {
        System.out.println("Hi I'm a bag. I weight " + weight + " grams and cost " + cost + " shekels");
//        for(int i = 0; i < items.length; i++) {
//            if(availableItems[i]) {
//                System.out.print("1 ");
//            } else {
//                System.out.print("0 ");
//            }
//        }

        for(int i = 0; i < items.length; i++) {
            if(availableItems[i]) {
                System.out.println(Bag.items[i].weight + " " + Bag.items[i].cost);
            }
        }
        System.out.println();
    }

    // Private methods //
    public void calculateWeightAndCost() {
        cost = 0;
        weight = 0;
        for(int i = 0; i < items.length; i++) {
            if(availableItems[i]) {
                cost += items[i].cost;
                weight += items[i].weight;
            }
        }
    }
}
