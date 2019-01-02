import java.util.Collections;
import java.util.Stack;
import static Constants.FrogConstants.*;

class StateTree {
    // technically this seems to be solvable without the use of a tree, but the
    // task requires we use that data structure
    private class Node {
        GameState gameState;
        Node[] children = new Node[4];
        private int numberOfChildren = 0;
        private int countOfTimesWhenMiddleWasFree;

        Node(int N) {
            gameState = new GameState(N);
            countOfTimesWhenMiddleWasFree = 0;
        }

        Node(GameState gameState, int countOfTimesWhenMiddleWasFree) {
            this.gameState = gameState;
            this.countOfTimesWhenMiddleWasFree = countOfTimesWhenMiddleWasFree;
        }

        void addChild(Node child) {
            if(numberOfChildren >= 2 * jumpLimit) return; // just in case; shouldn't be entered, ever
            children[numberOfChildren++] = child;
        }
    }

    private Stack<String> currentStatePath = new Stack<>();
    private Node root;
    private boolean solutionFound = false;
    private int N;

    StateTree(int N) {
        this.N = N;
        root = new Node(N);
        currentStatePath.push(root.gameState.getBoardCopy());
    }

    private void generateNextMoves(Node node){
        for(int i = -jumpLimit; i <= jumpLimit; i++){
            if(i == 0) continue;

            GameState newlyGeneratedState = node.gameState.makeMove(i);
            int newCountOfTimesWhenMiddleWasFree = node.countOfTimesWhenMiddleWasFree;
            if(newlyGeneratedState != null) {
                if(newlyGeneratedState.middleIsEmpty()) {
                    newCountOfTimesWhenMiddleWasFree++;
                    // System.out.print("N is free here: ");
                    // newlyGeneratedState.print();
                }
                createNextNode(node, newlyGeneratedState, newCountOfTimesWhenMiddleWasFree);
            }
            if(solutionFound) return;
        }

        if(node.countOfTimesWhenMiddleWasFree >= N) {
            solutionFound = true;
            return;
        }
        currentStatePath.pop();
    }

    private void createNextNode(
            Node currentNode,
            GameState newState,
            int countOfTimesWhenMiddleWasEmpty
    ) {
        Node newNode = new Node(newState, countOfTimesWhenMiddleWasEmpty);
        currentStatePath.push(newNode.gameState.getBoardCopy()); // push stringbuilder instead
        currentNode.addChild(newNode);
        generateNextMoves(newNode);
    }

    void solveTask() {
        generateNextMoves(root);
        printSolution();
    }

    private void printSolution() {
        String poppedBoard;
        Collections.reverse(currentStatePath);
        while(!currentStatePath.isEmpty()) {
            poppedBoard = currentStatePath.pop();
            System.out.println(poppedBoard);
        }
    }
}
