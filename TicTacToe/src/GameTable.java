public class GameTable {
    // Additional classes //
    class Node {
        Node child;
        char[][] data;

        Node(char[][] data) {
            this.data = data;
            child = null;
        }

        Node addChild(char[][] data) {
            Node child = new Node(data);
            this.child = child;
            return child;
        }
    }

    // Member variables //
    private Player player1;
    private Player player2;
    private char[][] table;
    private int numberOfPlacedTokens;
    int turns = 0;

    // Configuration //
    private static final int tableSize = 3;
    private static final int victoryValue = 1;
    private static final int defeatValue = -1;
    private static final int drawValue = 0;
    public static final char emptyField = '_';

    // Constructors //
    GameTable(Player p1, Player p2) {
        initializeTable();
        this.player1 = p1;
        this.player2 = p2;
        p1.setGameTable(this);
        p2.setGameTable(this);
    }

    private void initializeTable() {
        table = new char[tableSize][tableSize];

        for(int i = 0; i < tableSize; i++) {
            for(int j = 0; j < tableSize; j++) {
                table[i][j] = emptyField;
            }
        }
        numberOfPlacedTokens = 0;

//        table = new char[][] {
//                {'O', '_', 'X'},
//                {'_', 'X', '_'},
//                {'_', '_', '_'}
//        };
//        numberOfPlacedTokens = 3;

//        table = new char[][] {
//                {'X', '_', 'O'},
//                {'X', 'X', '_'},
//                {'_', 'O', '_'}
//        };
//        numberOfPlacedTokens = 5;
    }

    // Methods //
    public boolean makeMove(Player player, int y, int x) {
        if(tableIsFull()) {
            return false;
        }

        if(y >= tableSize || x >= tableSize || y < 0 || x < 0) {
            return false;
        }

        if(table[y][x] != emptyField) {
            return false;
        }

        placeToken(player.getSymbol(), y, x);
        return true;
    }

    private boolean tableIsFull() {
        return numberOfPlacedTokens >= tableSize * tableSize;
    }

    private void placeToken(char symbol, int y, int x) {
        table[y][x] = symbol;
        numberOfPlacedTokens++;
    }

    private void removeToken(int y, int x) {
        table[y][x] = emptyField;
        numberOfPlacedTokens--;
    }

    public boolean isGameOver() {
        char victorSymbol = gameVictorSymbol();
        if(victorSymbol != emptyField) {
            return true;
        }

        if(numberOfPlacedTokens == tableSize * tableSize) {
            return true;
        }

        return false;
    }

    public char gameVictorSymbol() {
        char result;

        result = checkMainDiagonal();
        if(result != emptyField) {
            return result;
        }

        result = checkSecondaryDiagonal();
        if(result != emptyField) {
            return result;
        }

        result = checkRows();
        if(result != emptyField) {
            return result;
        }

        result = checkColumns();
        if(result != emptyField) {
            return result;
        }


        return emptyField;
    }

    private char checkMainDiagonal() {
        char symbol = table[0][0];
        boolean isOver = true;

        if(symbol != emptyField) {
            for (int i = 1; i < tableSize; i++) {
                if (table[i][i] != symbol) {
                    isOver = false;
                    break;
                }
            }

            if (isOver) {
                return symbol;
            }
        }
        return emptyField;
    }

    private char checkSecondaryDiagonal() {
        char symbol = table[tableSize - 1][0];
        boolean isOver = true;
        if(symbol != emptyField) {
            for (int i = 1; i < tableSize; i++) {
                if (table[(tableSize - 1) - i][i] != symbol) {
                    isOver = false;
                    break;
                }
            }

            if (isOver) {
                return symbol;
            }
        }
        return emptyField;
    }

    private char checkRows() {
        char symbol;
        boolean isOver = true;

        for(int i = 0; i < tableSize; i++) {
            symbol = table[i][0];
            for(int j = 1; j < tableSize; j++) {
                if (table[i][j] != symbol) {
                    isOver = false;
                    break;
                }
            }
            if(isOver) {
                return symbol;
            } else {
                isOver = true;
            }
        }

        return emptyField;
    }

    private char checkColumns() {
        char symbol;
        boolean isOver = true;

        for(int i = 0; i < tableSize; i++) {
            symbol = table[0][i];
            for(int j = 1; j < tableSize; j++) {
                if (table[j][i] != symbol) {
                    isOver = false;
                    break;
                }
            }
            if(isOver) {
                return symbol;
            } else {
                isOver = true;
            }
        }
        return emptyField;
    }

    public void print() {
        for(int i = 0; i < tableSize; i++) {
            System.out.print('|');
            for(int j = 0; j < tableSize; j++) {
                System.out.print(table[i][j]);
                System.out.print('|');
            }
            System.out.println();
        }
    }

    // AI //
    // Standard algorithm //
    public Coordinates generateBestMove(Player player) {
        int y = 0;
        int x = 0;
        int max = Integer.MIN_VALUE;
        int result;
        Player opponent;

        if(player == player1) {
            opponent = player2;
        } else {
            opponent = player1;
        }

        for(int i = 0; i < tableSize; i++) {
            for(int j = 0; j < tableSize; j++) {
                if(!makeMove(player, i, j)) {
                    continue;
                }

                result = minValue(player, opponent);
                if(result > max) {
                    max = result;
                    y = i;
                    x = j;
                }

                removeToken(i, j);
            }
        }
        return new Coordinates(y, x);
    }

    private int minValue(Player playerInTurn, Player opponent) {
        turns++;
        int v = Integer.MAX_VALUE;
        int result;
        char winner;

        winner = gameVictorSymbol();
        if(winner == playerInTurn.getSymbol()) {
            return calculateRealVictoryValue();
        } else if(winner == opponent.getSymbol()) {
            return calculateRealDefeatValue();
        }

        for(int i = 0; i < tableSize; i++) {
            for(int j = 0; j < tableSize; j++) {
                if(!makeMove(opponent, i, j)){
                    if(tableIsFull()) {
                        return drawValue;
                    }
                    continue;
                }

                result = maxValue(playerInTurn, opponent);
                if (result < v) {
                    v = result;
                }

                removeToken(i, j);
            }
        }
        if(v == Integer.MAX_VALUE) {
            v = 0;
        }
        return v;
    }

    private int maxValue(Player playerInTurn, Player opponent) {
        turns++;
        int v = Integer.MIN_VALUE;
        int result;
        char winner;

        winner = gameVictorSymbol();
        if(winner == playerInTurn.getSymbol()) {
            return calculateRealVictoryValue();
        } else if(winner == opponent.getSymbol()) {
            return calculateRealDefeatValue();
        }

        for(int i = 0; i < tableSize; i++) {
            for(int j = 0; j < tableSize; j++) {
                if(!makeMove(playerInTurn, i, j)) {
                    if(tableIsFull()) {
                        return drawValue;
                    }
                    continue;
                }

                result = minValue(playerInTurn, opponent);

                if (v < result) {
                    v = result;
                }

                removeToken(i, j);
            }
        }

        if(v == Integer.MIN_VALUE) {
            v = 0;
        }
        return v;
    }

    // Maximum effort algorithm //
    public Coordinates generateMaximumEffortMove(Player player) {
        int y = 0;
        int x = 0;
        int max = Integer.MIN_VALUE;
        Coordinates result;
        Player opponent;

        if(player == player1) {
            opponent = player2;
        } else {
            opponent = player1;
        }

        for(int i = 0; i < tableSize; i++) {
            for(int j = 0; j < tableSize; j++) {
                if(!makeMove(player, i, j)) {
                    continue;
                }

                result = maximumEffortMinValue(player, opponent);
                if(result.getY() > max) {
                    max = result.getY();
                    y = i;
                    x = j;
                }

                removeToken(i, j);
            }
        }
        return new Coordinates(y, x);
    }

    private Coordinates maximumEffortMinValue(Player playerInTurn, Player opponent) {
        turns++;
        int v = Integer.MAX_VALUE;
        Coordinates result;
        char winner;
        int leafSum = 0;

        winner = gameVictorSymbol();
        if(winner == playerInTurn.getSymbol()) {
            return new Coordinates(calculateRealVictoryValue(), 1);
        } else if(winner == opponent.getSymbol()) {
            return new Coordinates(calculateRealDefeatValue(), 1);
        }

        for(int i = 0; i < tableSize; i++) {
            for(int j = 0; j < tableSize; j++) {
                if(!makeMove(opponent, i, j)){
                    if(tableIsFull()) {
                        return new Coordinates(drawValue, 1);
                    }
                    continue;
                }

                result = maximumEffortMaxValue(playerInTurn, opponent);
                if(result.getX() == 0) {
                    if (result.getY() < v) {
                        v = result.getY();
                    }
                } else {
                    leafSum += result.getY();
                }

                removeToken(i, j);
            }
        }
        if(v == Integer.MAX_VALUE) {
            v = 0;
        }

        return new Coordinates(v + leafSum, 0);
    }

    private Coordinates maximumEffortMaxValue(Player playerInTurn, Player opponent) {
        turns++;
        int v = Integer.MIN_VALUE;
        Coordinates result;
        char winner;
        int leafSum = 0;

        winner = gameVictorSymbol();
        if(winner == playerInTurn.getSymbol()) {
            return new Coordinates(calculateRealVictoryValue(), 1);
        } else if(winner == opponent.getSymbol()) {
            return new Coordinates(calculateRealDefeatValue(), 1);
        }

        for(int i = 0; i < tableSize; i++) {
            for(int j = 0; j < tableSize; j++) {
                if(!makeMove(playerInTurn, i, j)) {
                    if(tableIsFull()) {
                        return new Coordinates(drawValue, 1);
                    }
                    continue;
                }

                result = maximumEffortMinValue(playerInTurn, opponent);

                if(result.getX() == 0) {
                    if (v < result.getY()) {
                        v = result.getY();
                    }
                } else {
                    leafSum += result.getY();
                }

                removeToken(i, j);
            }
        }

        if(v == Integer.MIN_VALUE) {
            v = 0;
        }

        return new Coordinates(v + leafSum, 0);
    }

    // Maximum effort pruning //
    public Coordinates generateMaximumPrunedEffortMove(Player player) {
        int y = 0;
        int x = 0;
        int max = Integer.MIN_VALUE;
        Coordinates result;
        Player opponent;

        if(player == player1) {
            opponent = player2;
        } else {
            opponent = player1;
        }

        for(int i = 0; i < tableSize; i++) {
            for(int j = 0; j < tableSize; j++) {
                if(!makeMove(player, i, j)) {
                    continue;
                }

                result = maximumPrunedEffortMinValue(player, opponent, max, Integer.MAX_VALUE);
                if(result.getY() > max) {
                    max = result.getY();
                    y = i;
                    x = j;
                }

                removeToken(i, j);
            }
        }
        return new Coordinates(y, x);
    }

    private Coordinates maximumPrunedEffortMinValue(
            Player playerInTurn,
            Player opponent,
            int alpha,
            int beta
    ) {
        turns++;
        int v = Integer.MAX_VALUE;
        Coordinates result;
        char winner;
        int leafSum = 0;

        winner = gameVictorSymbol();
        if(winner == playerInTurn.getSymbol()) {
            return new Coordinates(calculateRealVictoryValue(), 1);
        } else if(winner == opponent.getSymbol()) {
            return new Coordinates(calculateRealDefeatValue(), 1);
        }

        for(int i = 0; i < tableSize; i++) {
            for(int j = 0; j < tableSize; j++) {
                if(!makeMove(opponent, i, j)){
                    if(tableIsFull()) {
                        return new Coordinates(drawValue, 1);
                    }
                    continue;
                }

                result = maximumPrunedEffortMaxValue(playerInTurn, opponent, alpha, v);

                if(result.getX() == 0) {
                    if (result.getY() < v) {
                        v = result.getY();
                    }
                } else {
                    leafSum += result.getY();
                }

                removeToken(i, j);

                if(v + leafSum < alpha) {
                    break;
                }
            }
        }
        if(v == Integer.MAX_VALUE) {
            v = 0;
        }

        return new Coordinates(v + leafSum, 0);
    }

    private Coordinates maximumPrunedEffortMaxValue(
            Player playerInTurn,
            Player opponent,
            int alpha,
            int beta
    ) {
        turns++;
        int v = Integer.MIN_VALUE;
        Coordinates result;
        char winner;
        int leafSum = 0;

        winner = gameVictorSymbol();
        if(winner == playerInTurn.getSymbol()) {
            return new Coordinates(calculateRealVictoryValue(), 1);
        } else if(winner == opponent.getSymbol()) {
            return new Coordinates(calculateRealDefeatValue(), 1);
        }

        for(int i = 0; i < tableSize; i++) {
            for(int j = 0; j < tableSize; j++) {
                if(!makeMove(playerInTurn, i, j)) {
                    if(tableIsFull()) {
                        return new Coordinates(drawValue, 1);
                    }
                    continue;
                }

                result = maximumPrunedEffortMinValue(playerInTurn, opponent, v, beta);

                if(result.getX() == 0) {
                    if (v < result.getY()) {
                        v = result.getY();
                    }
                } else {
                    leafSum += result.getY();
                }

                removeToken(i, j);

                if(v + leafSum > beta) {
                    break;
                }
            }
        }

        if(v == Integer.MIN_VALUE) {
            v = 0;
        }

        return new Coordinates(v + leafSum, 0);
    }

    // these imply the game has been evaluated as finished
    private int calculateRealVictoryValue() {
        return (tableSize * tableSize - numberOfPlacedTokens) + victoryValue;
    }

    private int calculateRealDefeatValue() {
        return -(tableSize * tableSize - numberOfPlacedTokens) + defeatValue;
    }
}
