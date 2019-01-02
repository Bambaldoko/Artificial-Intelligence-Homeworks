import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Float.POSITIVE_INFINITY;

public class Solver {
    // Member variables //
    private Flower[] trainingRecords;
    private Flower[] testingRecords;
    private final int NUMBER_OF_TESTING_FLOWERS = 20;
    private int COMPARED_NEIGHBOURS_COUNT = 5;

    // Constructor //
    Solver(Flower[] records) {
        this.trainingRecords = new Flower[records.length - NUMBER_OF_TESTING_FLOWERS];
        this.testingRecords = new Flower[NUMBER_OF_TESTING_FLOWERS];
        separateTestingExamples(records);
        Arrays.sort(this.trainingRecords, Comparator.comparing(Flower::evaluate));
    }

    Solver(Flower[] records, int nearestNeighbourCount) {
        this.trainingRecords = new Flower[records.length - NUMBER_OF_TESTING_FLOWERS];
        this.testingRecords = new Flower[NUMBER_OF_TESTING_FLOWERS];
        separateTestingExamples(records);
        Arrays.sort(this.trainingRecords, Comparator.comparing(Flower::evaluate));
        COMPARED_NEIGHBOURS_COUNT = nearestNeighbourCount;
    }

    private void separateTestingExamples(Flower[] records) {
        int[] indexOfTestingFlowers = chooseRandomFlowers();
        int numberOfWrittenTestingFlowers = 0; // for testing
        int numberOfWrittenTrainingFlowers = 0;

        for(int i = 0; i < records.length; i++) {
            if(
                    numberOfWrittenTestingFlowers < testingRecords.length &&
                    indexOfTestingFlowers[numberOfWrittenTestingFlowers] == i
                    ) {
                testingRecords[numberOfWrittenTestingFlowers++] = records[i];
            } else {
                trainingRecords[numberOfWrittenTrainingFlowers++] = records[i];
            }
        }

    }

    private int[] chooseRandomFlowers() {
        int[] ar = new int[trainingRecords.length + testingRecords.length];
        for(int i = 0; i < trainingRecords.length + testingRecords.length; i++) {
            ar[i] = i;
        }

        shuffleArray(ar);
        int[] croppedArray = new int[NUMBER_OF_TESTING_FLOWERS];
        System.arraycopy(ar, 0, croppedArray, 0, NUMBER_OF_TESTING_FLOWERS);
        Arrays.sort(croppedArray);

        return croppedArray;
    }

    private void shuffleArray(int[] ar) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public Flower.FlowerClass classify(Flower flower) {
        Flower[] neighbours = chooseNeighbours(flower);
        Flower.FlowerClass foretoldClass = tellMostNearbyCommonClass(neighbours, flower);
        System.out.println(foretoldClass);
        return foretoldClass;
    }

    public float selfTest() {
        int correctAnswers = 0;

        for(Flower flower : testingRecords) {
            if(flower.getFlowerClass() == classify(flower)) {
                correctAnswers++;
            }
        }

        return correctAnswers / (float)NUMBER_OF_TESTING_FLOWERS;
    }

    private Flower[] chooseNeighbours(Flower flower) {
        Flower[] chosenNeighbours = new Flower[COMPARED_NEIGHBOURS_COUNT];
        int chosenNeighboursCount = 1;
        int closestNeighbourIndex = closestNeighbour(flower);
        chosenNeighbours[0] = trainingRecords[closestNeighbourIndex];

        int leftNeighbourIndex = closestNeighbourIndex - 1;
        int rightNeighbourIndex = closestNeighbourIndex + 1;

        while(chosenNeighboursCount < COMPARED_NEIGHBOURS_COUNT) {
            if(leftNeighbourIndex >= 0) {
                chosenNeighbours[chosenNeighboursCount++] = trainingRecords[leftNeighbourIndex--];
            }

            if(chosenNeighboursCount >= COMPARED_NEIGHBOURS_COUNT) break;

            if(rightNeighbourIndex < trainingRecords.length) {
                chosenNeighbours[chosenNeighboursCount++] = trainingRecords[rightNeighbourIndex++];
            }
        }

        return chosenNeighbours;
    }

    private Flower.FlowerClass tellMostNearbyCommonClass(Flower[] flowers, Flower unclassifiedFlower) {
        float setosa = 0;
        int setosaCount = 0;
        float versi = 0;
        int versiCount = 0;
        float virgin = 0;
        int virginCount = 0;

        for(Flower fl : flowers) {
            switch (fl.getFlowerClass()) {
                case SETOSA:
                    setosa += Math.abs(fl.evaluate() - unclassifiedFlower.evaluate());
                    setosaCount++;
                    break;
                case VERSICOLOUR:
                    versi += Math.abs(fl.evaluate() - unclassifiedFlower.evaluate());
                    versiCount++;
                    break;
                case VIRGINICA:
                    virgin += Math.abs(fl.evaluate() - unclassifiedFlower.evaluate());
                    virginCount++;
            }
        }

        if(setosaCount == 0) {
            setosa = POSITIVE_INFINITY;
        } else {
            setosa = setosa / (float)setosaCount;
        }

        if(versiCount == 0) {
            versi = POSITIVE_INFINITY;
        } else {
            versi = versi / (float)versiCount;
        }

        if(virginCount == 0) {
            virgin = POSITIVE_INFINITY;
        } else {
            virgin = virgin / (float)virginCount;
        }

        return tellMinimum(setosa, versi, virgin);
    }

    private Flower.FlowerClass tellMinimum(float setosa, float versi, float virgin){
        if(setosa < versi && setosa < virgin) {
            return Flower.FlowerClass.SETOSA;
        }
        if(versi < virgin) {
            return Flower.FlowerClass.VERSICOLOUR;
        }
        return Flower.FlowerClass.VIRGINICA;
    }

    private int closestNeighbour(Flower flower) {
        float valueForLookup = flower.evaluate();
        int currentIndex;
        for(currentIndex = 0 ;currentIndex < trainingRecords.length; currentIndex++) {
            if(valueForLookup <= trainingRecords[currentIndex].evaluate()) {
                return currentIndex;
            }
        }
        return currentIndex - 1;
    }

    public void print() {
        for(int i = 0; i < trainingRecords.length; i++) {
            Flower rec = trainingRecords[i];
            System.out.print(i + ": ");
            rec.print();
            System.out.println(":" + rec.evaluate());
        }
    }

    public void statistics(){
        float setosaSum = 0;
        int setosaCount = 0;
        float versiSum = 0;
        int versiCount = 0;
        float virginSum = 0;
        int virginCount = 0;

        for(Flower record : trainingRecords) {
            switch (record.getFlowerClass()) {
                case SETOSA:
                    setosaSum += record.getSepalWidth();
                    setosaCount++;
                    break;
                case VERSICOLOUR:
                    versiSum += record.getSepalWidth();
                    versiCount++;
                    break;
                case VIRGINICA:
                    virginSum += record.getSepalWidth();
                    virginCount++;
                    break;
                default:
                    break;
            }
        }

        System.out.println("SETOSA: " + setosaSum / setosaCount);
        System.out.println("VERS: " + versiSum / versiCount);
        System.out.println("VIRGIN: " + virginSum / virginCount);
    }

}
