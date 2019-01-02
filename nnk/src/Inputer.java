import java.util.Scanner;
import java.util.Vector;

public class Inputer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int neighbourCount = Integer.parseInt(scanner.nextLine());

        String currentLine = scanner.nextLine();
        String[] splitCurrLine;
        Vector<Flower> input = new Vector<>();
        float sepalLength;
        float sepalWidth;
        float petalLength;
        float petalWidth;
        Flower.FlowerClass flowerClass;

        while(!currentLine.equals("")) {
            splitCurrLine = currentLine.split(",");
            sepalLength = Float.parseFloat(splitCurrLine[0]);
            sepalWidth = Float.parseFloat(splitCurrLine[1]);
            petalLength = Float.parseFloat(splitCurrLine[2]);
            petalWidth = Float.parseFloat(splitCurrLine[3]);
            flowerClass = Flower.parseEnum(splitCurrLine[4]);

            input.add(new Flower(sepalLength, sepalWidth, petalLength, petalWidth, flowerClass));
            currentLine = scanner.nextLine();
        }

        Flower[] readyInput = new Flower[input.size()];
        input.toArray(readyInput);
        float resultSum = 0;
        int numberOfRetries = 1;

        for(int i = 0; i < numberOfRetries; i++) {
            Solver solver = new Solver(readyInput, neighbourCount);
//            solver.classify(new Flower(4.3f, 3.0f, 1.1f, 0.1f));
//            solver.print();
            resultSum += solver.selfTest();
        }
        System.out.println("Accurracy: " + resultSum / numberOfRetries * 100 + "%");
        //solver.statistics();
    }
}
