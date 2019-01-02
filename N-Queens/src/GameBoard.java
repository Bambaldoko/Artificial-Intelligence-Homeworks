import java.util.Arrays;
import java.util.Random;

public class GameBoard {
    // Member variables //
    private int N;
    private int[][] collisionHeuristic;
    //private int[][] horseMoveHeuristic;
    private int[] queenLocation;
    private int lastMove;
    private ItsoVector workPriority;

    // Constructors //
    public GameBoard(int N) {
        this.N = N;
        collisionHeuristic = new int[N][N];
        //horseMoveHeuristic = new int[N][N];
        queenLocation = new int[N];
        lastMove = -1;
        workPriority = new ItsoVector(N);

        initialize();
    }

    // Methods //
    private void initialize() {
        long start = System.nanoTime();
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++) {
                collisionHeuristic[i][j] = 0;
                //horseMoveHeuristic[i][j] = 0;
            }
            queenLocation[i] = i;
        }
        positionQueensHorse();
        System.out.println("Setting up the queens took: " + (System.nanoTime() - start) / 1000000000.0 + "s");
    }

    private void positionQueensHorse() {
        int index = 0;
        for(int i = 0; i < 2; i++) {
            for(int j = i; j < N; j += 2) {
                queenLocation[index] = j;
                changeAllHeuristics(index, 1);
                index++;
            }
        }
    }

    private void positionQueensHorse2() {
        int index = 0;
        for(int i = 1; i >= 0; i--) {
            for(int j = i; j < N; j += 2) {
                queenLocation[index] = j;
                changeAllHeuristics(index, 1);
                index++;
            }
        }
    }

    private void positionQueensRandomly() {
        shuffleQueens();
        for(int i = 0; i < N; i++) {
            changeAllHeuristics(i, 1);
        }
    }

    private int getMaxConflicts() {
        int max = 0;
        for(int i = 0; i < N; i++) {
            int currentCollision = collisionHeuristic[i][queenLocation[i]];
            if(collisionHeuristic[i][queenLocation[i]] > max) {
                max = currentCollision;
            }
        }
        return max;
    }

    public void shuffleQueens() {
        int index, temp;
        Random random = new Random();
        for (int i = queenLocation.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = queenLocation[index];
            queenLocation[index] = queenLocation[i];
            queenLocation[i] = temp;
        }
    }

    private void changeAllHeuristics(int column, int change) {
        changeQueenConflicts(column, change);
//        changeHorseHeuristics(column, change);
    }

    private void changeQueenConflicts(int column, int change) {
        changeSouthEast(column, change);
        changeNorthWest(column, change);
        changeEast(column, change);
        changeWest(column, change);
        changeNorthEast(column, change);
        changeSouthWest(column, change);
    }

    private void changeSouthEast(int column, int change) {
        changeConflictsBoard(column, queenLocation[column], change, 1, 1);
    }

    private void changeNorthWest(int column, int change) {
        changeConflictsBoard(column, queenLocation[column], change, -1, -1);
    }

    private void changeWest(int column, int change) {
        changeConflictsBoard(column, queenLocation[column], change, 0, -1);
    }

    private void changeEast(int column, int change) {
        changeConflictsBoard(column, queenLocation[column], change, 0, 1);
    }

    private void changeNorthEast(int column, int change) {
        changeConflictsBoard(column, queenLocation[column], change, -1, 1);
    }

    private void changeSouthWest(int column, int change) {
        changeConflictsBoard(column, queenLocation[column], change, 1, -1);
    }

    private void changeConflictsBoard(int startingColumn, int startingRow, int change, int dy, int dx) {
        startingRow += dy;
        startingColumn += dx;

        while(startingRow >= 0 && startingRow < N && startingColumn >= 0 && startingColumn < N) {
            collisionHeuristic[startingRow][startingColumn] += change;
            startingRow += dy;
            startingColumn += dx;
        }
    }

    private void changeHorseHeuristics(int queenColumn, int change){
        changeHorseBoard(queenColumn, change, -1, -2);
        changeHorseBoard(queenColumn, change, 1, -2);
        changeHorseBoard(queenColumn, change, -1, 2);
        changeHorseBoard(queenColumn, change, 1, 2);

        changeHorseBoard(queenColumn, change, -2, -1);
        changeHorseBoard(queenColumn, change, 2, -1);
        changeHorseBoard(queenColumn, change, -2, 1);
        changeHorseBoard(queenColumn, change, 2, 1);
    }

    private void changeHorseBoard(int queenColumn, int change, int dy, int dx) {
        if(queenColumn + dx >= N || queenColumn + dx < 0) return;

        int queenRow = queenLocation[queenColumn];
        if(queenRow + dy >= N || queenRow + dy < 0) return;

        //horseMoveHeuristic[queenRow + dy][queenColumn + dx] += change;
    }

    // AI //
    public void solve() {
        solve(1, N / 2);
    }

    private void solve(int retries, int limit) {
        int counter = 0;
        while(counter++ < limit) {
            int column = chooseQueen();
            if (column == -1) {
                System.out.println("Boi we found it");
                System.out.println(counter);
//                printQueens2();
                return;
            }

            int row = chooseNewRow(column);
            moveQueen(column, row);
//            printQueens2();
//            System.out.println();
        }
        System.out.println("Resetting...");
        resetCollisions();

        switch (retries) {
            case 1:
                positionQueensHorse2();
                solve(retries + 1, limit);
                break;
            case 2:
                positionQueensHorse();
                solve(retries + 1, 10000);
                break;
            case 3:
                positionQueensHorse2();
                solve(retries + 1, 10000);
                break;
            default:
                return;
        }
    }

    private void resetCollisions() {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                collisionHeuristic[i][j] = 0;
            }
        }
    }

    private int chooseQueen() {
        int maxHeuristic = 0;
        for(int column = 0; column < N; column++) {
            int currentHeuristic = collisionHeuristic[queenLocation[column]][column];
            if(currentHeuristic == 0 || lastMove == column) continue;

            if(currentHeuristic > maxHeuristic) {
                workPriority.reset();
                workPriority.add(column);
                maxHeuristic = currentHeuristic;
            } else if(currentHeuristic == maxHeuristic) {
                workPriority.add(column);
            }
        }
        if(maxHeuristic == 0){
            return -1;
        }
        return workPriority.give();
    }

    private int chooseNewRow(int column) {
        int minCollision = N;
        int maxHorseHeuristic = 0;

        for(int row = 0; row < N; row++) {
            if(row == queenLocation[column]) continue;

            int currentCollision = collisionHeuristic[row][column];
            //int currentHorseHeuristic = horseMoveHeuristic[row][column];
            if(currentCollision < minCollision) {
                //maxHorseHeuristic = currentHorseHeuristic;
                minCollision = currentCollision;
                workPriority.reset();
                workPriority.add(row);
            } else if(currentCollision == minCollision) {
//                if(currentHorseHeuristic > maxHorseHeuristic) {
//                    maxHorseHeuristic = currentHorseHeuristic;
//                    workPriority.reset();
//                    workPriority.add(row);
//                } else if(currentHorseHeuristic == maxHorseHeuristic) {
                    workPriority.add(row);
//                }
            }
        }
        return workPriority.give();
    }

    // Interface //
    public void moveQueen(int queenColumn, int destinationRow) {
        changeAllHeuristics(queenColumn, -1);

        queenLocation[queenColumn] = destinationRow;

        changeAllHeuristics(queenColumn, 1);
        lastMove = queenColumn;
    }

    // Printers //
    public void printCollisions() {
        printBoard(collisionHeuristic);
    }

//    public void printHorseHeuristics() {
//        printBoard(horseMoveHeuristic);
//    }

    private void printBoard(int[][] arr) {
        for (int[] line : arr) {
            for (int number : line) {
                System.out.format("%3s", number);
            }
            System.out.println();
        }
    }

    public void printQueensAndConflicts() {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if(queenLocation[j] == i) {
                    System.out.format("%3s", 'Q');
                } else {
                    System.out.format("%3s", collisionHeuristic[i][j]);
                }
            }
            System.out.println();
        }
    }

    public void printQueensAndHorses() {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if(queenLocation[j] == i) {
                    System.out.format("%3s", 'Q');
                } else {
//                    System.out.format("%3s", horseMoveHeuristic[i][j]);
                }
            }
            System.out.println();
        }
    }

    public void printQueens2() {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if(queenLocation[j] == i) {
                    System.out.format("%3s", '*');
                } else {
                    System.out.format("%3s", "_");
                }
            }
            System.out.println();
        }
    }
}
