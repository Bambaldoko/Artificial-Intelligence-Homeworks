import java.util.Scanner;

public class ControlHub {
    public static void main(String[] args) {
        Player p1 = new Player('X');
        Player p2 = new Player('O');

        GameTable table = new GameTable(p1, p2);
        int counter = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Who is going to play first? Input '1' for Player and '2' for AI");
        int turnDefiner = (scanner.nextInt() + 1) % 2;
        table.print();

        while (true) {
            if (counter % 2 == turnDefiner) {
                int y = scanner.nextInt();
                int x = scanner.nextInt();

                if (!p1.makeMove(y - 1, x - 1)) {
                    System.out.println("Try again");
                    continue;
                }
            } else {
                Coordinates decision = table.generateMaximumPrunedEffortMove(p2);
                p2.makeMove(decision.getY(), decision.getX());
                System.out.println("AI's turn:");
            }

            counter++;

            if (table.isGameOver()) {
                table.print();
                System.out.println("Game over");
                if (table.gameVictorSymbol() != GameTable.emptyField) {
                    System.out.println(table.gameVictorSymbol() + " wins!");
                } else {
                    System.out.println("DRAW!");
                }
//                System.out.println("I did " + table.turns + " calculations");
                break;
            }
            table.print();
            System.out.println();
        }

//        Player currentPlayer;

//        while (true) {
//            if (counter % 2 == 0) {
//                currentPlayer = p1;
//            } else {
//                currentPlayer = p2;
//            }
//
//            int y = scanner.nextInt();
//            int x = scanner.nextInt();
//
//            if (!currentPlayer.makeMove(y - 1, x - 1)) {
//                System.out.printf("Try again!");
//                continue;
//            }
//
//            counter++;
//
//            if (table.gameVictorSymbol() != GameTable.emptyField) {
//                table.print();
//                System.out.println("Game over");
//                break;
//            }
//            table.print();
//        }
    }
}