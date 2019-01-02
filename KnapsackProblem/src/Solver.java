import java.util.Random;

public class Solver {
    // Member variables //
    private Item[] allItems;
    private ItskoQueue fittestBags;
    private Bag[] unfits;

    // Configuration //
    private final int unfitBagModifier = 2;
    private final int chanceToPickNotunfitParent = 5;
    private final int chanceToSaveToUnfits = 5;
    private final int randomParentChance = 10;

    private final short mutationChance = 100; // in percent
    private final int mutationSeverity = 20; // in percent of number of bags

    private final int generationCount = 2000;
    private final int consecutiveUnchangedBestMax = 480;
    private final int parentPairsPerGeneration = 10;

    // Constructors //
    Solver(int M, Item[] items) {
        Bag.initialize(items, M);
        allItems = items;
        fittestBags = new ItskoQueue();
        unfits = new Bag[items.length / unfitBagModifier];

        generateRandomBags(items.length, items.length / 3);
    }

    private void generateRandomBags(int bagCount, int attemptLimit) {
        Bag newBag;
        Random randomGenerator = new Random();
        int numberOfDenials;

        for(int i = 0; i < bagCount; i++) {
            newBag = new Bag();
            numberOfDenials = 0;
            while (numberOfDenials < attemptLimit && newBag.getWeight() < Bag.getMaxWeight()) {
                int index = randomGenerator.nextInt(allItems.length);
                if (newBag.addItem(index)) {
                    numberOfDenials = 0;
                } else {
                    numberOfDenials++;
                }
            }
            fittestBags.add(newBag);
            if(i % unfitBagModifier == 0) {
                unfits[i / unfitBagModifier] = newBag;
            }
        }
    }

    public int solve() {
        int counter = 0;
        int currMax = 0;
//        int currMaxGeneration = 0;
        int consecutiveUnchangedBest = 0;

        while(counter < generationCount && consecutiveUnchangedBest < consecutiveUnchangedBestMax) {
            PairOfBags[] generationParents = select();
            Bag[] generationChildren = crossover(generationParents);
            Bag[] mutatedChildren = mutate(generationChildren);
            processGeneration(mutatedChildren);
            counter++;
//            System.out.println("Generation " + counter);
            Bag fittestBag = fittestBags.bestBag();
//            fittestBag.print();
//            fittestBags.printStatistics();
            consecutiveUnchangedBest++;
            if(fittestBag.fitness() > currMax) {
                currMax = fittestBag.fitness();
//                currMaxGeneration = counter;
                consecutiveUnchangedBest = 0;
            }
            if(counter == 10 || counter == 20 || counter == 50 || counter == 100) {
                System.out.println(fittestBag.fitness());
            }
        }
//        System.out.println("Fittest solution found at generation №" + currMaxGeneration);
        Bag fittestBag = fittestBags.bestBag();
        System.out.println(fittestBag.fitness());
//        fittestBag.calculateWeightAndCost();
//        fittestBag.print();
//        fittestBags.printStatistics();
        return fittestBag.fitness();
    }

    private void processGeneration(Bag[] generation) {
        Random randomGenerator = new Random();
        for (Bag mutant : generation) {
            if(mutant.getWeight() <= Bag.getMaxWeight()) {
                Bag droppedBag = fittestBags.addAndResize(mutant);
                if(chanceToSaveToUnfits > randomGenerator.nextInt(99)) {
                    unfits[randomGenerator.nextInt(unfits.length)] = droppedBag;
                }
            }
        }
    }

    // Standard Genetic Methods //
    // SELECT //
    private PairOfBags[] select() {
        PairOfBags[] selectedParents = new PairOfBags[parentPairsPerGeneration];

        for(int i = 0; i < parentPairsPerGeneration; i++) {
            selectedParents[i] = fittestBags.getRandomPair();
//            randomize(selectedParents[i]);
        }
        return selectedParents;
    }

    // Side- effect only :/
    private void randomize(PairOfBags selectedParents) {
        Random randomGenerator = new Random();
        if(randomParentChance > randomGenerator.nextInt(100)) {
            Bag randomBag = unfits[randomGenerator.nextInt(unfits.length)];
            selectedParents.setBag1(randomBag);
        }
        if(randomParentChance > randomGenerator.nextInt(100)) {
            Bag randomBag = unfits[randomGenerator.nextInt(unfits.length)];
            selectedParents.setBag2(randomBag);
        }
    }

    // CROSSOVER //
    private Bag[] crossover(PairOfBags[] pairs) {
        Bag[] generation = new Bag[pairs.length * 2];
        int index = 0;
        PairOfBags children;
        for(int i = 0; i < pairs.length; i++) {
            children = cross(pairs[i]);
            generation[index++] = children.getBag1();
            generation[index++] = children.getBag2();
        }
//        mutate(generation);
        return generation;
    }

    private PairOfBags cross(PairOfBags pair){
        Random randomGenerator = new Random();
        int firstCrossPoint = randomGenerator.nextInt(Bag.getCount() - 1);
        int secondCrossPoint = randomGenerator.nextInt(Bag.getCount() - firstCrossPoint) + firstCrossPoint;

        boolean[] firstChildChromosome = pair.getBag1().getAvailableItems().clone();
        boolean[] secondChildChromosome = pair.getBag2().getAvailableItems().clone();

        for(int i = firstCrossPoint; i <= secondCrossPoint; i++) {
            boolean temp = firstChildChromosome[i];
            firstChildChromosome[i] = secondChildChromosome[i];
            secondChildChromosome[i] = temp;
        }
        return new PairOfBags(new Bag(firstChildChromosome), new Bag(secondChildChromosome));
    }

    // MUTATE //
    private Bag[] mutate(Bag[] generation) {
        Random randomGenerator = new Random();
        int index;
        for (Bag bag : generation) {
            if(randomGenerator.nextInt(100) < mutationChance) {
                for(int i = 0; i < (int)(Bag.getCount() * (mutationSeverity / 100.0)); i++) {
                    index = randomGenerator.nextInt(Bag.getCount());
                    bag.alternateItem(index);
                }
            }
        }
        return generation;
    }
}
