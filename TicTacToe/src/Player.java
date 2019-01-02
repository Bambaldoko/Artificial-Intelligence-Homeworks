public class Player {
    // Member variables //
    private char symbol;
    private GameTable table;

    // Constructor
    Player(char symbol) {
        this.symbol = symbol;
    }

    // Methods //
    public void setGameTable(GameTable table) {
        this.table = table;
    }

    public boolean makeMove(int y, int x) {
            return table.makeMove(this, y, x);
    }

    public char getSymbol() {
        return symbol;
    }
}
