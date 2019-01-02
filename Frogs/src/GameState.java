import static Constants.FrogConstants.*;

public class GameState {
    private StringBuilder gameBoard = new StringBuilder();
    private int emptyFieldPosition;
    private int N;

    GameState(int N) {
        emptyFieldPosition = N;
        this.N = N;

        generateLeftFrogs(N);
        generateEmptyField();
        generateRightFrogs(N);
    }

    private GameState(StringBuilder gameBoard, int emptyFieldPosition, int N) {
        this.gameBoard = gameBoard;
        this.emptyFieldPosition = emptyFieldPosition;
        this.N = N;
    }

    private void generateLeftFrogs(int N) {
        for(int i = 0; i < N; i++) {
            gameBoard.append(leftFrogSymbol);
        }
    }

    private void generateEmptyField() {
        gameBoard.append(emptyFieldSymbol);
    }

    private void generateRightFrogs(int N) {
        for(int i = 0; i < N; i++) {
            gameBoard.append(rightFrogSymbol);
        }
    }

    GameState makeMove(int relativeToEmptyFieldPosition) {
        int frogToBeMovedPosition = emptyFieldPosition + relativeToEmptyFieldPosition;

        if(!moveIsValid(relativeToEmptyFieldPosition, frogToBeMovedPosition)) {
            return null;
        }
        StringBuilder newBoard = new StringBuilder(swapChars(frogToBeMovedPosition, emptyFieldPosition));
        // thing to consider: gameBoard[frogToBeMovedPosition] now holds the empty field

        return new GameState(newBoard, frogToBeMovedPosition, this.N);
    }

    private boolean moveIsValid(int relativeToEmptyFieldPosition, int frogToBeMovedPosition) {
        if(Math.abs(relativeToEmptyFieldPosition) > jumpLimit || relativeToEmptyFieldPosition == 0) {
            return false;
        }

        if (frogToBeMovedPosition < 0 || frogToBeMovedPosition >= gameBoard.length()) {
            return false;
        }
        if (relativeToEmptyFieldPosition < 0 &&
                gameBoard.charAt(frogToBeMovedPosition) != leftFrogSymbol) {
            return false;
        }
        if (relativeToEmptyFieldPosition > 0 &&
                gameBoard.charAt(frogToBeMovedPosition) != rightFrogSymbol) {
            return false;
        }
        return true;
    }

    String getBoardCopy() {
        return new String(gameBoard);
    }

    private StringBuilder swapChars(int pos1, int pos2) {
        StringBuilder newOne = new StringBuilder(gameBoard);
        char swap = newOne.charAt(pos1);
        newOne.setCharAt(pos1, gameBoard.charAt(pos2));
        newOne.setCharAt(pos2, swap);
        return newOne;
    }

    public void print() {
        System.out.println(gameBoard);
    }

    public boolean middleIsEmpty() {
        return gameBoard.charAt(N) == emptyFieldSymbol;
    }
}
