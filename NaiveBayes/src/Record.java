import java.util.Arrays;

public class Record {
    // enum stuff //
    enum Party {
        REPUBLICAN,
        DEMOCRAT,
        WHO_CARES
    }

    private Party parseString(String string) {
        String normalizedString = string.toLowerCase();
        switch(normalizedString) {
            case "republican":
                return Party.REPUBLICAN;
            case "democrat":
                return Party.DEMOCRAT;
            default:
                return Party.WHO_CARES;
        }
    }

    // Member variables //
    private Party party;
    private int[] opinions;

    // Constants //
    public static final int numberOfOpinions = 16;
    public static final int numberOfDifferentOpinionValues = 3; //"y", "n", "?"
    public static final int numberOfParties = 2;

    // Constructors //
    Record (Party party, int[] data) {
        this.party = party;
        opinions = Arrays.copyOf(data, numberOfOpinions);
    }

    Record (String unparsedData) {
        if(unparsedData.equals("")) {
            return;
        }
        String[] splitData = unparsedData.split(",");
        party = parseString(splitData[0]);
        opinions = new int[numberOfOpinions];
        for(int i = 0; i < numberOfOpinions; i++) {
            int parsedOpinion = parseOpinion(splitData[i + 1]);
            if(parsedOpinion == 0) {
                continue;
            }
            opinions[i] = parsedOpinion;
        }
    }

    // Methods //
    public Party getParty() {
        return party;
    }

    public int getOpinion(int index) {
        return opinions[index];
    }

    public void print() {
        System.out.print("Hi, I am " + party.toString() + " and these are my gay beliefs: ");
        for(int i = 0; i < numberOfOpinions; i++) {
            System.out.print(opinions[i] + " ");
        }
        System.out.println();
    }

    // This looks barbaric but I think the other options are as barbaric, but would
    // also take me more time to implement....... so yea, sorry to anyone reading this
    private int parseOpinion(String opinion) {
        String normalizedOpinion = opinion.toLowerCase();
        switch(normalizedOpinion) {
            case "y":
                return 0;
            case "n":
                return 1;
            case "?":
                return 2;
            default:
                return 3;
        }
    }
}
