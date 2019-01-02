import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        StateTree tree = new StateTree(N);
        tree.solveTask();
    }
}
