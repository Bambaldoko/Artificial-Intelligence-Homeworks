import java.util.Scanner;

public class ControlHub {
    public static void main(String[] args) {
        int N = new Scanner(System.in).nextInt();
        for(int i = 0; i < 10; i++) {
            System.out.println("====================================================");
            System.out.println(N + i);
            GameBoard game = new GameBoard(N + i);
            long start = System.nanoTime();
            game.solve();
//        game.printQueens2();
            System.out.println("Solving the task took: " + (System.nanoTime() - start) / 1000000000.0 + "s");
        }
    }
}
