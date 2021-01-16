package de.itsstuttgart.chessclient.chess.pieces;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.chess.ChessBoard;
import de.itsstuttgart.chessclient.chess.ChessPiece;
import de.itsstuttgart.chessclient.chess.Side;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * created by paul on 16.01.21 at 18:37
 */
public class Queen extends ChessPiece {

    public Queen(Side side, int col, int row) {
        super(side, col, row);
    }

    @Override
    public void draw(GraphicsContext gc, double squareSize) {
        double small = squareSize * 0.85;
        double left = (squareSize - small) / 2d;
        gc.drawImage(ChessClient.instance.themeManager.getPieceImage("queen", this.getSide()), (this.getColumn() * squareSize) + left, (this.getRow() * squareSize) + left, small, small);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<int[]> getPossibleMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            // diag
            placeDiagonal(i, this.row - (this.col - i), moves, board);
            placeDiagonal(this.col + (this.row - i), i, moves, board);

            // horizontal
            if (i != this.col) {
                if (canMoveHere(i, this.row, board)) {
                    if (clearPath(i, this.row, board)) {
                        moves.add(new int[] {i, this.row});
                    }
                }
            }

            // vertical
            if (i != this.row) {
                if (canMoveHere(this.col, i, board)) {
                    if (clearPath(this.col, i, board)) {
                        moves.add(new int[] {this.col, i});
                    }
                }
            }
        }

        return cleanChecks(moves, board);
    }
}
