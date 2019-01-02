import java.util.Random;
import java.util.Vector;

public class Solver {
    // Member variables //
    private int[][][] statistics; // opinion-number; party; opinion- value[number]
    private Vector<Record> records;

    // Constants //
    public static final int numberOfFolds = 10;

    // Constructors //
    Solver() {
        statistics = new int[Record.numberOfOpinions][Record.numberOfParties][Record.numberOfDifferentOpinionValues];
        records = new Vector<>();
    }

    // Methods //
    public void recordOpinion(Record opinion) {
        records.add(opinion);
    }

    public float[] predictYoself() {
        int correctGuessesThisFold = 0;
        int[] randomIndexes = getRandomRecordIndexes();
        int stepSize = records.size() / numberOfFolds;
        float[] results = new float[numberOfFolds + 1];

        for(int i = 0; i < numberOfFolds; i++) {
            trainWithoutSomeRecords(i, i + stepSize, randomIndexes);
            for (int j = 0; j < stepSize &&  i * numberOfFolds + j < records.size(); j++) {
                Record rec = records.get(randomIndexes[i * numberOfFolds + j]);
                if (rec.getParty() == predictParty(rec)) {
                    correctGuessesThisFold++;
                }
            }
            results[i] = correctGuessesThisFold / (float)stepSize;
            correctGuessesThisFold = 0;
            resetStatistics();
        }
        for(int i = 0; i < numberOfFolds; i++) {
            results[numberOfFolds] += results[i];
        }
        results[numberOfFolds] /= numberOfFolds;
        return results;
    }

    private int[] getRandomRecordIndexes() {
        int[] indexes = new int[records.size()];
        for(int i = 0; i < records.size(); i++) {
            indexes[i] = i;
        }
        shuffleArray(indexes);

        return indexes;
    }

    private void shuffleArray(int[] array) {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private void trainWithoutSomeRecords(int trainUpTo, int trainAfter, int[] arrayOfIndexes) {
        for(int i = 0; i < trainUpTo; i++) {
            trainWithOneRecord(records.get(arrayOfIndexes[i]));
        }
        for(int i = trainAfter; i < records.size(); i++) {
            trainWithOneRecord(records.get(arrayOfIndexes[i]));
        }
    }

    private void trainWithOneRecord(Record rec) {
        int partyIndex = rec.getParty().ordinal();

        for(int topicIndex = 0; topicIndex < Record.numberOfOpinions; topicIndex++) {
            int opinionValueIndex = rec.getOpinion(topicIndex);
            statistics[topicIndex][partyIndex][opinionValueIndex]++;
        }
    }
    private Record.Party predictParty(Record opinion) {
        float probabilityToBeRepublican = 1;
        float probabilityToBeDemocrat = 1;
        for(int topicIndex = 0; topicIndex < Record.numberOfOpinions; topicIndex++) {
            int republicansWithSameDecision =
                    statistics[topicIndex][0][opinion.getOpinion(topicIndex)];
            int democratsWithSameDecision =
                    statistics[topicIndex][1][opinion.getOpinion(topicIndex)];
            int totalOpinions = republicansWithSameDecision + democratsWithSameDecision;
            probabilityToBeRepublican *= republicansWithSameDecision / (float)totalOpinions;
            probabilityToBeDemocrat *= democratsWithSameDecision / (float)totalOpinions;
        }

        if(probabilityToBeRepublican > probabilityToBeDemocrat) {
            return Record.Party.values()[0];
        }
        return Record.Party.values()[1];
    }

    private void resetStatistics() {
        // this is probably slower than manually setting them to "0" but
        // I'm too lazy
        statistics = new int[Record.numberOfOpinions][Record.numberOfParties][Record.numberOfDifferentOpinionValues];
    }
}
