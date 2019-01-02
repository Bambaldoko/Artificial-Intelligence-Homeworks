public class Topic {
    // Member variables //
    private int[][] opinionsByParty;

    // Constructors //
    Topic(int numberOfParties, int numberOfDifferentOpinionValues) {
        opinionsByParty = new int [numberOfParties][numberOfDifferentOpinionValues];
    }

    // Methods //
    public void recordOpinion(){}
}
