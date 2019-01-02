public class Flower {
    enum FlowerClass {
        SETOSA,
        VERSICOLOUR,
        VIRGINICA,
        UNKNOWN
    }

    public static FlowerClass parseEnum(String string){
        switch(string) {
            case "Iris-setosa":
                return FlowerClass.SETOSA;
            case "Iris-versicolor":
                return FlowerClass.VERSICOLOUR;
            case "Iris-virginica":
                return FlowerClass.VIRGINICA;
            default:
                return FlowerClass.UNKNOWN;
        }
    }

    public float getSepalLength() {
        return sepalLength;
    }

    public float getSepalWidth() {
        return sepalWidth;
    }

    public float getPetalLength() {
        return petalLength;
    }

    public float getPetalWidth() {
        return petalWidth;
    }

    // Member variables //
    private float sepalLength;
    private float sepalWidth;
    private float petalLength;
    private float petalWidth;
    private FlowerClass flowerClass;

    // Value modifiers //
    private final float SL_MODIFIER = 0.05f;
    private final float SW_MODIFIER = -0.15f;
    private final float PL_MODIFIER = 1.1f; // 1.5f
    private final float PW_MODIFIER = 1f; // 1.2f

    // Constructors //
    Flower(float sl, float sw, float pl, float pw, FlowerClass flowerClass) {
        this.sepalLength = sl;
        this.sepalWidth = sw;
        this.petalLength = pl;
        this.petalWidth = pw;
        this.flowerClass = flowerClass;
    }

    Flower(float sl, float sw, float pl, float pw) {
        this.sepalLength = sl;
        this.sepalWidth = sw;
        this.petalLength = pl;
        this.petalWidth = pw;
        this.flowerClass = FlowerClass.UNKNOWN;
    }

    // Methods //
    float evaluate() {
        return
                sepalLength * SL_MODIFIER +
                sepalWidth * SW_MODIFIER +
                petalLength * PL_MODIFIER +
                petalWidth * PW_MODIFIER;
    }

    FlowerClass getFlowerClass() {
        return flowerClass;
    }

    public void print() {
        System.out.print(
                getSepalLength() + " " +
                        getSepalWidth() + " " +
                        getPetalLength() + " " +
                        getPetalWidth() + " " +
                        getFlowerClass()
        );
    }
}
