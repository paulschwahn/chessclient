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
public class King extends ChessPiece {

    public King(Side side, int col, int row) {
        super(side, col, row);
    }

    @Override
    public void draw(GraphicsContext gc, double squareSize) {
        double small = squareSize * 0.85;
        double left = (squareSize - small) / 2d;
        gc.drawImage(ChessClient.instance.themeManager.getPieceImage("king", this.getSide()), (this.getColumn() * squareSize) + left, (this.getRow() * squareSize) + left, small, small);
    }

    @Override
    public List<int[]> getPossibleMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        for (int colOffset = -1; colOffset <= 1; colOffset++) {
            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                if (colOffset != 0 || rowOffset != 0) {
                    int col = this.col + colOffset;
                    int row = this.row + rowOffset;
                    if (this.inBounds(col, row)) {
                        if (this.canMoveHere(col, row, board)) {
                            moves.add(new int[]{col, row});
                        }
                    }
                }
            }
        }
        return cleanChecks(moves, board);
    }
}
