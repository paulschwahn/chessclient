package de.itsstuttgart.chessclient.chess;

import de.itsstuttgart.chessclient.chess.pieces.King;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * created by paul on 16.01.21 at 17:46
 */
public class ChessBoard {

    private List<ChessPiece> board;
    private Side nextMove;

    private int halfMoveClock;
    private int fullMoveNumber;

    private ChessPiece selected;

    private Side mySide;

    private boolean disableCheckChecking;

    public ChessBoard(List<ChessPiece> board, Side mySide) {
        this.board = board;
        this.mySide = mySide;
    }

    /**
     * Initializes the standard Chess Board
     */
    public ChessBoard() {
        this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    /**
     * Creates a new game board with a given FEN
     *
     * @param fen Forsyth Edwards Notation
     */
    public ChessBoard(String fen) {
        fromFEN(fen);
        this.mySide = Side.WHITE;
    }

    /**
     * Renders the game board onto the given context
     *
     * @param gc context provided by canvas
     * @param squareSize size of one chess square
     */
    public void drawBoard(GraphicsContext gc, double squareSize) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Paint color = ((x + y) % 2 == 1) ? new Color(0.11372549019, 0.11372549019, 0.11372549019, 1) : new Color(0.83529411764, 0.83529411764, 0.83529411764, 1);
                gc.setFill(color);
                gc.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
            }
        }
        gc.setImageSmoothing(false);

        this.disableCheckChecking = true;
        // In check display
        this.board.stream()
                .filter(p -> p instanceof King)
                .filter(p -> this.board.stream()
                        .filter(m -> m.getSide() != p.getSide())
                        .anyMatch(m -> m.getPossibleMoves(this).stream()
                                .anyMatch(i -> i[0] == p.getColumn() && i[1] == p.getRow())
                        )
                ).forEach(p -> {
            gc.setFill(new Color(1.0d, 0.3882353d, 0.2784314d, 0.75d));
            double ovalSize = squareSize * 0.95;
            double left = (squareSize - ovalSize) / 2d;
            gc.fillOval((p.getColumn() * squareSize) + left, (p.getRow() * squareSize) + left, ovalSize, ovalSize);
        });
        this.disableCheckChecking = false;

        // experimental selected highlighting
//        if (this.selected != null) {
//            gc.setFill(new Color(0.37254903d, 0.61960787d, 0.627451d, 0.75d));
//            double ovalSize = squareSize * 0.95;
//            double left = (squareSize - ovalSize) / 2d;
//            gc.fillOval((this.selected.getColumn() * squareSize) + left, (this.selected.getRow() * squareSize) + left, ovalSize, ovalSize);
//        }

        this.board.forEach(p -> p.draw(gc, squareSize));

        if (this.selected != null) {
            List<int[]> moves = this.selected.getPossibleMoves(ChessBoard.this);
            for (int[] move : moves) {
                gc.setFill(new Color(0.37254903d, 0.61960787d, 0.627451d, 0.75d));
                double ovalSize = squareSize * 0.35;
                double left = (squareSize - ovalSize) / 2d;
                gc.fillOval((move[0] * squareSize) + left, (move[1] * squareSize) + left, ovalSize, ovalSize);
            }
        }

    }

    public List<ChessPiece> getBoard() {
        return board;
    }

    public ChessPiece getPiece(int col, int row) {
        return this.board.stream().filter(p -> p.getRow() == row && p.getColumn() == col).findFirst().orElse(null);
    }

    public boolean hasPiece(int col, int row) {
        return this.board.stream().anyMatch(p -> p.getRow() == row && p.getColumn() == col);
    }

    /**
     * Reads a GameState using the "Forsyth Edwards Notation"
     *
     * @param fen Notation
     */
    private void fromFEN(String fen) {
        this.board = new ArrayList<>();
        String[] parts = fen.split(" ");

        String[] positioning = parts[0].split("/");
        this.nextMove = Side.fromFEN(parts[1]);

        String castling = parts[2];
        String enPassant = parts[3];
        this.halfMoveClock = Integer.parseInt(parts[4]);
        this.fullMoveNumber = Integer.parseInt(parts[4]);

        for (int row = 0; row < 8; row++) {
            char[] rowChars = positioning[row].toCharArray();

            for (int col = 0; col < 8; col++) {
                char c = rowChars[col];
                if (Character.isDigit(c)) {
                    col += Integer.parseInt(Character.toString(c)) - 1;
                } else {
                    this.board.add(ChessPiece.fromFEN(c, row, col));
                }
            }
        }
    }

    public void click(GraphicsContext gc, double squareSize, int col, int row) {
        if (selected == null) {
            this.board.stream().filter(p -> p.getRow() == row && p.getColumn() == col).filter(p -> p.getPossibleMoves(this).size() > 0).findFirst().ifPresent(p -> {
                this.selected = p;
            });
        } else {
            this.selected.getPossibleMoves(this).stream().filter(m -> m[0] == col && m[1] == row).findFirst().ifPresent(m -> {
                // captures
                if (hasPiece(m[0], m[1])) {
                    this.board.removeIf(p -> p.col == m[0] && p.row == m[1]);
                }

                this.selected.col = m[0];
                this.selected.row = m[1];
            });
            this.selected = null;
        }
        this.drawBoard(gc, squareSize);
    }

    public boolean isDisableCheckChecking() {
        return disableCheckChecking;
    }

    public void setDisableCheckChecking(boolean disableCheckChecking) {
        this.disableCheckChecking = disableCheckChecking;
    }

    /**
     * Checks if a given side has no possible moves left, in addition also checks if the king is currently in check,
     * otherwise its a stalemate, not a checkmate
     *
     * @param side side to check
     * @return checkmate or not
     */
    public boolean isCheckMate(Side side) {
        boolean inCheck = this.board.stream()
                .filter(p -> p instanceof King)
                .anyMatch(p -> this.board.stream()
                        .filter(m -> m.getSide() != side)
                        .anyMatch(m -> m.getPossibleMoves(this).stream()
                                .anyMatch(i -> i[0] == p.getColumn() && i[1] == p.getRow())
                        )
                );
        return inCheck && !canMove(side);
    }

    /**
     * Checks if any piece has the ability to move on the board, used for checkmate and stalemate detection
     *
     * @param side side to check
     * @return has moves left or not
     */
    public boolean canMove(Side side) {
        return this.board.stream().filter(p -> p.getSide() == side).allMatch(p -> p.getPossibleMoves(this).size() == 0);
    }
}
