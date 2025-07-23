+-------------------+
|      ChessGame    |
+-------------------+
| board: Board      |
| currentPlayer: Player |
| movePiece(from, to) |
| isCheckmate(): boolean |
| isStalemate(): boolean |
  getBoard(): string
+-------------------+

+-------------------+
|      Board       |
+-------------------+
| grid: Piece[][]  |
| movePiece(from, to) |
| getPieceAt(pos): Piece |
| isPositionValid(pos): boolean |
+-------------------+

+-------------------+
|      Player      |
+-------------------+
| color: Color     |
| makeMove(from, to): boolean |
+-------------------+

+----------------------+
|        Piece         |
+----------------------+
| x: Integer
  y: Integer  |
| color: Color         |
| isValidMove(to): boolean |
| move(x, y): void       |
+----------------------+
| (abstract class)     |
+----------------------+

+----------------------+
|      Pawn (Piece)    |
+----------------------+
| isValidMove(to)      |
+----------------------+

+----------------------+
|     Knight (Piece)   |
+----------------------+
| isValidMove(to)      |
+----------------------+

+----------------------+
|      Rook (Piece)    |
+----------------------+
| isValidMove(to)      |
+----------------------+

+----------------------+
|     Bishop (Piece)   |
+----------------------+
| isValidMove(to)      |
+----------------------+

+----------------------+
|      Queen (Piece)   |
+----------------------+
| isValidMove(to)      |
+----------------------+

+----------------------+
|      King (Piece)    |
+----------------------+
| isValidMove(to)      |
+----------------------+

+-------------------+
|      Color  enum      |
+-------------------+
| WHITE, BLACK     |
+-------------------+

abstract class Piece {
    int x, y;
    boolean isWhite;

    public Piece(int x, int y, boolean isWhite) {
        this.x = x;
        this.y = y;
        this.isWhite = isWhite;
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    // Abstract method to be implemented by each specific piece
    public abstract boolean isValidMove(int newX, int newY);
}


class Knight extends Piece {
    @Override
    boolean isValidMove(int x, int y) {
        int dx = Math.abs(to.x - this.position.x);
        int dy = Math.abs(to.y - this.position.y);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }
}


class Board {
    Piece[][] grid = new Piece[8][8];

    public Board() {
        initializeBoard();
    }

    // Initialize the chessboard with pieces in their starting positions
    private void initializeBoard() {
        // Place pawns
        for (int i = 0; i < 8; i++) {
            grid[i][1] = new Pawn(i, 1, true);  // White pawns
            grid[i][6] = new Pawn(i, 6, false); // Black pawns
        }

        // Place other pieces
        placeMajorPieces(0, true);  // White back row
        placeMajorPieces(7, false); // Black back row
    }

    // Places rooks, knights, bishops, queen, and king in correct order
    private void placeMajorPieces(int row, boolean isWhite) {
        grid[0][row] = new Rook(0, row, isWhite);
        grid[7][row] = new Rook(7, row, isWhite);
        grid[1][row] = new Knight(1, row, isWhite);
        grid[6][row] = new Knight(6, row, isWhite);
        grid[2][row] = new Bishop(2, row, isWhite);
        grid[5][row] = new Bishop(5, row, isWhite);
        grid[3][row] = new Queen(3, row, isWhite);
        grid[4][row] = new King(4, row, isWhite);
    }

    public Piece getPieceAt(int x, int y) {
        return grid[x][y];
    }

    public boolean isPositionValid(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public void movePiece(int fromX, int fromY, int toX, int toY) {
        Piece piece = grid[fromX][fromY];
        if (piece != null && piece.isValidMove(toX, toY)) {
            grid[toX][toY] = piece;
            grid[fromX][fromY] = null;
            piece.move(toX, toY);
        }
    }
}


class ChessGame {
    Board board = new Board();
    boolean currentPlayer = true; 

    public void movePiece(int fromX, int fromY, int toX, int toY) {
        Piece piece = board.getPieceAt(fromX, fromY);
        if (piece != null && piece.isWhite == currentPlayer) {
            board.movePiece(fromX, fromY, toX, toY);
            currentPlayer = !currentPlayer; 

            if (board.isCheckmate(currentPlayer)) {
                System.out.println((currentPlayer ? "White" : "Black") + " is in checkmate! Game over.");
                System.exit(0);
            } else if (board.isStalemate(currentPlayer)) {
                System.out.println("Game is a stalemate!");
                System.exit(0);
            }
        } else {
            System.out.println("Invalid move or not your turn.");
        }
    }

    public void displayBoard() {
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                Piece piece = board.getPieceAt(x, y);
                if (piece == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(getPieceSymbol(piece) + " ");
                }
            }
            System.out.println();
        }
    }

    private char getPieceSymbol(Piece piece) {
        if (piece instanceof Pawn) return piece.isWhite ? 'P' : 'p';
        if (piece instanceof Rook) return piece.isWhite ? 'R' : 'r';
        if (piece instanceof Knight) return piece.isWhite ? 'N' : 'n';
        if (piece instanceof Bishop) return piece.isWhite ? 'B' : 'b';
        if (piece instanceof Queen) return piece.isWhite ? 'Q' : 'q';
        if (piece instanceof King) return piece.isWhite ? 'K' : 'k';
        return '?';
    }
}
