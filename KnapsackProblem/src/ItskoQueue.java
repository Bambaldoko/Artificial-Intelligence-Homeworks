import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.Consumer;

public class ItskoQueue {
    // Helper classes //
    private class Node {
        // Member variables //
        Node parent;
        Node child;
        Bag data;

        // Constructors //
        Node(Bag data) {
            this.data = data;
            child = null;
            parent = null;
        }

        // Methods //
        void addChild(Bag data) {
            Node child = new Node(data);
            child.parent = this;
            this.child = child;
        }
    }
    // Member variables //
    private Node root;
    private Node tail;

    // Constructor //
    ItskoQueue() {
        root = null;
        tail = null;
    }

    ItskoQueue(Bag data) {
        root = new Node(data);
        root.parent = null;
        tail = root;
    }

    // Methods //
    public Bag addAndResize(Bag newBag) {
        if(root == null || newBag.fitness() < tail.data.fitness()) {
            return newBag;
        }
        add(newBag);
        return removeTail();
    }

    public void add(Bag newBag) {
        Node newNode = new Node(newBag);
        if(root == null) {
            root = newNode;
            tail = newNode;
            return;
        }

        Node currNode = root;
        Node lastNode = root;
        while(currNode != null && currNode.data.fitness() > newBag.fitness()) {
            lastNode = currNode;
            currNode = currNode.child;
        }
        if(currNode == null) {
            lastNode.child = newNode;
            newNode.parent = lastNode;
            tail = newNode;
        } else {
            if (currNode.parent == null) {
                root = newNode;
            } else {
                currNode.parent.child = newNode;
                newNode.parent = currNode.parent;
            }
            currNode.parent = newNode;
            newNode.child = currNode;
        }
    }

    private Bag removeTail() {
        Node newTail = tail.parent;
        Bag oldBag = tail.data;
        newTail.child = null;
        tail.parent = null;
        tail = newTail;

        return oldBag;
    }

    public PairOfBags getRandomPair() {
        Random randomGenerator = new Random();
        int index1 = randomGenerator.nextInt(Bag.getCount());
        int index2 = randomGenerator.nextInt(Bag.getCount());
        int counter = 0;

        Node currentNode = root;
        Bag bag1 = null;
        Bag bag2 = null;

        while(bag1 == null || bag2 == null) {
            if(counter == index1) {
                bag1 = currentNode.data;
            }
            if(counter == index2) {
                bag2 = currentNode.data;
            }
            counter++;
            currentNode = currentNode.child;
        }
        return new PairOfBags(bag1, bag2);
    }

    public PairOfBags getBestParents() {
        return new PairOfBags(root.data, root.child.data);
    }

    public Bag bestBag() {
        return root.data;
    }

    public void printStatistics() {
        int[] statistics = new int[Bag.getCount()];
        Node currentNode = root;

        while(currentNode != null) {
            boolean[] currBagAvailability = currentNode.data.getAvailableItems();
            for(int i = 0; i < Bag.getCount(); i++) {
                if(currBagAvailability[i]) {
                    statistics[i]++;
                }
            }
            currentNode = currentNode.child;
        }
        for(int i = 0; i < Bag.getCount(); i++) {
            System.out.print(statistics[i] + " ");
        }
        System.out.println();
    }
}
