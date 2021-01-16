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
public class Bishop extends ChessPiece {

    public Bishop(Side side, int col, int row) {
        super(side, col, row);
    }

    @Override
    public void draw(GraphicsContext gc, double squareSize) {
        double small = squareSize * 0.85;
        double left = (squareSize - small) / 2d;
        gc.drawImage(ChessClient.instance.themeManager.getPieceImage("bishop", this.getSide()), (this.getColumn() * squareSize) + left, (this.getRow() * squareSize) + left, small, small);
    }

    @Override
    public List<int[]> getPossibleMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();

        // diag
        for (int i = 0; i < 8; i++) {
            placeDiagonal(i, this.row - (this.col - i), moves, board);
            placeDiagonal(this.col + (this.row - i), i, moves, board);
        }
        return cleanChecks(moves, board);
    }
}
