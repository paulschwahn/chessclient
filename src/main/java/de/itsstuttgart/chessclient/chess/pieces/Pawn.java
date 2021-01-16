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
public class Pawn extends ChessPiece {

    public Pawn(Side side, int col, int row) {
        super(side, col, row);
    }

    @Override
    public void draw(GraphicsContext gc, double squareSize) {
        double small = squareSize * 0.85;
        double left = (squareSize - small) / 2d;
        gc.drawImage(ChessClient.instance.themeManager.getPieceImage("pawn", this.getSide()), (this.getColumn() * squareSize) + left, (this.getRow() * squareSize) + left, small, small);
    }

    @Override
    public List<int[]> getPossibleMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        boolean firstMove = (getSide() == Side.WHITE) ? this.getChessRow() == 2 : this.getChessRow() == 7;

        int forward = (getSide() == Side.WHITE) ? this.row - 1 : this.row + 1;
        if (!board.hasPiece(col, forward) && inBounds(col, forward))
            moves.add(new int[] {col, forward});

        if (firstMove) {
            int first = (getSide() == Side.WHITE) ? this.row - 2 : this.row + 2;
            if (!board.hasPiece(col, forward) && !board.hasPiece(col, first) && inBounds(col, first))
                moves.add(new int[] {col, first});
        }

        if (inBounds(col - 1, forward) && board.hasPiece(col - 1, forward) && board.getPiece(col - 1, forward).getSide() != this.side)
            moves.add(new int[] {col - 1, forward});

        if (inBounds(col + 1, forward) && board.hasPiece(col + 1, forward) && board.getPiece(col + 1, forward).getSide() != this.side)
            moves.add(new int[] {col + 1, forward});

        return cleanChecks(moves, board);
    }
}
