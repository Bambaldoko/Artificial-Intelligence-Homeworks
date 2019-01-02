import java.util.Scanner;

public class Inputer {
    public static void main(String args[]) {
        Record rec;
        Scanner scanner = new Scanner(System.in);
        Solver solver = new Solver();
        String lastLine = scanner.nextLine();
        while(!lastLine.equals("")) {
            rec = new Record(lastLine);
            solver.recordOpinion(rec);
            lastLine = scanner.nextLine();
        }
        int repeats = 1;
        float sumOfChances = 0;
        float[] results;
        for(int i = 0; i < repeats; i++) {
            results = solver.predictYoself();
            sumOfChances += results[Solver.numberOfFolds];
            for(int j = 0; j < Solver.numberOfFolds; j++) {
                System.out.println("Fold " + j + ": " + results[j]);
            }
        }
        System.out.println("Average accuracy: " + sumOfChances / repeats);
    }
}
