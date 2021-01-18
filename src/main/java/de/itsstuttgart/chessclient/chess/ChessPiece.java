package de.itsstuttgart.chessclient.chess;

import de.itsstuttgart.chessclient.chess.pieces.*;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * created by paul on 16.01.21 at 17:47
 */
public abstract class ChessPiece {

    protected Side side;
    protected int col, row;

    public ChessPiece(Side side, int col, int row) {
        this.side = side;
        this.col = col;
        this.row = row;
    }

    public Side getSide() {
        return side;
    }

    public int getColumn() {
        return col;
    }

    public int getRow() {
        return row;
    }

    /**
     * Used for getting the reversed chess notation for the pieces row
     *
     * @return actual row (8-1)
     */
    public int getChessRow() {
        return 8 - this.row;
    }

    /**
     * Checks if a given location is within bounds of the chess board
     *
     * @param col location column
     * @param row location row
     * @return if within bounds or not
     */
    public boolean inBounds(int col, int row) {
        return col >= 0 && row >= 0 && col < 8 && row < 8;
    }

    /**
     * Checks if the given location is being blocked by your own piece
     *
     * @param col location column
     * @param row location row
     * @param board board to check against
     * @return if its safe to move there
     */
    public boolean canMoveHere(int col, int row, ChessBoard board) {
        return !board.hasPiece(col, row) || board.getPiece(col, row).getSide() != this.side;
    }

    /**
     * Converts this pieces location to a byte value
     *
     * col  row
     * 0000 0000
     *
     * Example for g5 (col 6, row 3):
     * 0110 0011
     * -> 0x63,
     *
     * this leads to the hex representation being an accurate display for the pieces position
     * while only taking up 1 byte of space
     *
     * @return positional byte
     */
    public byte getLocation() {
        return (byte)((this.col & 0xf) | (this.row << 4 & 0xf0));
    }

    /**
     * Checks if there is a clear path between this piece and the given location
     *
     * @param col location column
     * @param row location row
     * @param board game board to check on
     * @return is the path clear?
     */
    public boolean clearPath(int col, int row, ChessBoard board) {
        int stepDirectionX = Integer.compare(col - this.col, 0);
        int stepDirectionY = Integer.compare(row - this.row, 0);

        int tempCol = this.col + stepDirectionX;
        int tempRow = this.row + stepDirectionY;

        while (tempCol != col || tempRow != row) {
            if (board.hasPiece(tempCol, tempRow))
                return false;
            tempCol += stepDirectionX;
            tempRow += stepDirectionY;
        }
        return true;
    }

    protected void placeDiagonal(int col, int row, List<int[]> moves, ChessBoard board) {
        if (col != this.col) {
            if (this.inBounds(col, row)) {
                if (canMoveHere(col, row, board)) {
                    if (this.clearPath(col, row, board)) {
                        moves.add(new int[] {col, row});
                    }
                }
            }
        }
    }

    /**
     * Draws this piece on a given {@link GraphicsContext} using the local coordinates {@link ChessPiece#col} and {@link ChessPiece#row}
     *
     * @param gc context to render on provided by canvas
     * @param squareSize used for dimensions of the rendered piece
     */
    public abstract void draw(GraphicsContext gc, double squareSize);

    /**
     * Returns a clean list of all possible allowed moves
     *
     * @param board board to compare against
     * @return list of moves for this piece
     */
    public abstract List<int[]> getPossibleMoves(ChessBoard board);

    /**
     * Removes any moves that would result in the king getting checked
     *
     * @param moves unfiltered list of moves
     * @param board board to compare against
     * @return filtered list of allowed moves
     */
    protected List<int[]> cleanChecks(List<int[]> moves, ChessBoard board) {
        if (board.isDisableCheckChecking())
            return moves;

        ChessPiece king = board.getBoard().stream()
                .filter(p -> p.getSide() == side)
                .filter(p -> p instanceof King)
                .findFirst().orElse(null);

        if (king == null)
            return moves;

        List<int[]> newMoves = new ArrayList<>();

        board.setDisableCheckChecking(true);

        for (int[] move : moves) {
            int oldCol = this.col;
            int oldRow = this.row;
            ChessPiece take = board.getPiece(move[0], move[1]);
            List<ChessPiece> shallowCopy = new ArrayList<>(board.getBoard());
            if (take != null)
                shallowCopy.remove(take);
            this.col = move[0];
            this.row = move[1];

            ChessBoard copy = new ChessBoard(shallowCopy, board.getMySide());
            copy.setDisableCheckChecking(true);

            boolean invalidMove = shallowCopy
                    .stream()
                    .filter(p -> p.getSide() != this.side)
                    .anyMatch(p -> p.getPossibleMoves(copy)
                            .stream()
                            .anyMatch(m -> m[0] == king.col && m[1] == king.row)
                    );
            this.col = oldCol;
            this.row = oldRow;
            if (!invalidMove)
                newMoves.add(move);
        }

        board.setDisableCheckChecking(false);

        return newMoves;
    }

    /**
     * Creates a new chess piece by fen notation of a piece
     * (capital = white, letter = type)
     *
     * @param c character
     * @param row row to be placed in
     * @param col col to be placed in
     * @return
     */
    public static ChessPiece fromFEN(char c, int row, int col) {
        Side side = Side.fromCase(Character.isUpperCase(c));
        char lower = Character.toLowerCase(c);
        switch (lower) {
            case 'p':
                return new Pawn(side, col, row);
            case 'n':
                return new Knight(side, col, row);
            case 'b':
                return new Bishop(side, col, row);
            case 'r':
                return new Rook(side, col, row);
            case 'q':
                return new Queen(side, col, row);
            case 'k':
                return new King(side, col, row);
        }
        return null;
    }

    public char toFEN() {
        char let = 'x';
        if (this instanceof Pawn) let = 'p';
        if (this instanceof Knight) let = 'n';
        if (this instanceof Bishop) let = 'b';
        if (this instanceof Rook) let = 'r';
        if (this instanceof Queen) let = 'q';
        if (this instanceof King) let = 'k';
        if (this.side == Side.WHITE)
            let = Character.toUpperCase(let);

        return let;
    }

    public void setColumn(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
