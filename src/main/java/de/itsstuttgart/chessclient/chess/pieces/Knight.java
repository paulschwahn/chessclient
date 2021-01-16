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
public class Knight extends ChessPiece {

    public Knight(Side side, int col, int row) {
        super(side, col, row);
    }

    @Override
    public void draw(GraphicsContext gc, double squareSize) {
        double small = squareSize * 0.85;
        double left = (squareSize - small) / 2d;
        gc.drawImage(ChessClient.instance.themeManager.getPieceImage("knight", this.getSide()), (this.getColumn() * squareSize) + left, (this.getRow() * squareSize) + left, small, small);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<int[]> getPossibleMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        for (int colOff = -2; colOff < 3; colOff += 4) {
            for (int rowOff = -1; rowOff < 2; rowOff += 2) {

                int col = this.col + colOff;
                int row = this.row + rowOff;
                if (this.canMoveHere(col, row, board)) {
                    if (this.inBounds(col, row)) {
                        moves.add(new int[]{col, row});
                    }
                }
            }
        }
        for (int colOff = -1; colOff < 2; colOff += 2) {
            for (int rowOff = -2; rowOff < 3; rowOff += 4) {

                int col = this.col + colOff;
                int row = this.row + rowOff;

                if (this.canMoveHere(col, row, board)) {
                    if (this.inBounds(col, row)) {
                        moves.add(new int[]{col, row});
                    }
                }
            }
        }
        return cleanChecks(moves, board);
    }
}
