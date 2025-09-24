package g62727.dev3.oxono.model;

import g62727.dev3.oxono.util.Strategy;

import java.util.ArrayList;
import java.util.List;

public class MiniMaxStrategy implements Strategy {
    private Game game;
    private Oxono oxono;
    private int depth;
    private Totem totem;
    private Position positionTotem;
    private Position positionToken;

    public MiniMaxStrategy(int depth, Game game, Oxono oxono) {
        this.depth = depth;
        this.game = game;
        this.oxono = oxono;
    }

    @Override
    public Totem chooseTotem(Oxono oxono) {
        Move bestMove = findBestMove(oxono);
        System.out.println(bestMove);
        this.totem = bestMove.getTotem();
        this.positionTotem = bestMove.getTotemPos();
        this.positionToken = bestMove.getTokenPos();
        return this.totem;
    }

    @Override
    public Position chooseTotemMove(Oxono oxono, Totem totem) {
        return this.positionTotem;
    }

    @Override
    public Position chooseTokenInsert(Oxono oxono, Totem totem) {
        return this.positionToken;
    }

    private void simulateMoveBlack(Oxono oxono, Move move) {
        oxono.move(move.getTotem(), move.getTotemPos());
        oxono.insert(new Token(move.getTotem().getSymbol(), Color.BLACK), move.getTokenPos());
    }

    private void simulateMovePink(Oxono oxono, Move move) {
        oxono.move(move.getTotem(), move.getTotemPos());
        oxono.insert(new Token(move.getTotem().getSymbol(), Color.PINK), move.getTokenPos());
    }

    private void undoMove(Oxono oxono, Move move) {
        oxono.removePawn(move.getTokenPos());
        oxono.move(move.getTotem(), oxono.getTotemPosition(move.getTotem()));
    }

    private Move findBestMove(Oxono oxono) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Move> possibleMoves = getPossibleMoves();

        for (Move move : possibleMoves) {

            simulateMoveBlack(oxono, move);

            int score = minimax(oxono, depth - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);

            undoMove(oxono, move);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int minimax(Oxono oxono, int depth, boolean isMaximizing, int alpha, int beta) {
        if (depth == 0 || oxono.checkForWinner() || game.isDraw()) {
            return evaluateBoard(depth);
        }

        List<Move> possibleMoves = getPossibleMoves();

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : possibleMoves) {

                simulateMoveBlack(oxono, move);

                int eval = minimax(oxono, depth - 1, false, alpha, beta);

                undoMove(oxono, move);

                maxEval = Math.max(maxEval, eval);

                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : possibleMoves) {

                simulateMovePink(oxono, move);

                int eval = minimax(oxono,depth - 1, true, alpha, beta);

                undoMove(oxono, move);

                minEval = Math.min(minEval, eval);

                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }

    //private List<Move> getPossibleMoves() {
    //    List<Move> possibleMoves = new ArrayList<>();

    //    //Totem totemO = (Totem) this.board.getPawnAt(totO);
    //    //Totem totemX = (Totem) this.board.getPawnAt(totX);

    //    //getPossiblesMovesForTotem(totemO, possibleMoves);
    //    //getPossiblesMovesForTotem(totemX, possibleMoves);

    //    return possibleMoves;
    //}

    private List<Move> getPossibleMoves() {
        List<Move> possibleMoves = new ArrayList<>();

        Totem totemO = (Totem) this.oxono.getPawnAt(this.oxono.totem_O_Pos());
        Totem totemX = (Totem) this.oxono.getPawnAt(this.oxono.totem_X_Pos());

        getPossiblesMovesForTotem(totemO, possibleMoves);
        getPossiblesMovesForTotem(totemX, possibleMoves);

        return possibleMoves;
    }

    private void getPossiblesMovesForTotem(Totem totem, List<Move> possibleMoves) {
        if (this.game.enoughToMoveTotem(totem.getSymbol())) {
            Position position = (totem.getSymbol() == Symbol.O) ? this.oxono.totem_O_Pos() : this.oxono.totem_X_Pos();
            List<Position> possibleTotemMoves = this.oxono.getValidMoves(totem);
            for (Position posTotem : possibleTotemMoves) {
                List<Position> possibleTokenInserts = this.oxono.getValidInsert(posTotem.getRow(), posTotem.getCol());
                for (Position posToken : possibleTokenInserts) {
                    Move move = new Move(totem, posTotem, posToken);
                    possibleMoves.add(move);
                }
            }
        }
    }

    private int evaluateBoard(int depth) {

        if (depth == 0) {
            return -1;
        }

        if (game.win()) {
            //this.game.setRunning(true);
            if (this.game.getToPlay().getColor() == Color.PINK) {
                return - (100 * (Board.BOARD_SIZE * Board.BOARD_SIZE - 2)) / depth ;
            } else {
                return (100 * (Board.BOARD_SIZE * Board.BOARD_SIZE - 2)) / depth;
            }
        }

        if (game.isDraw()) {
            //this.game.setRunning(true);
            return 0;
        }

        return -100;
    }

    private int evaluatePosition(Board board, int row, int col) {
        int score = 0;
        Pawn pawn = board.getPawnAt(new Position(row, col));
        if (pawn instanceof Token token) {
            score += checkLine(board, row, col, 0, 1, token);
            score += checkLine(board, row, col, 1, 0, token);
            score += checkLine(board, row, col, 0, -1, token);
            score += checkLine(board, row, col, -1, 0, token);
        }
        return score;
    }

    private int checkLine(Board board, int row, int col, int rowDelta, int colDelta, Token startToken) {
        int score = 0;
        int symbolCount = 1;
        int colorCount = 1;

        for (int i = 1; i < 4; i++) {
            int newRow = row + i * rowDelta;
            int newCol = col + i * colDelta;
            if (newRow < 0 || newRow >= Board.BOARD_SIZE || newCol < 0 || newCol >= Board.BOARD_SIZE) {
                break;
            }
            Pawn pawn = board.getPawnAt(new Position(newRow, newCol));
            if (pawn instanceof Token token) {
                if (token.getSymbol() == startToken.getSymbol()) {
                    symbolCount++;
                }
                if (token.getColor() == startToken.getColor()) {
                    colorCount++;
                }
            } else {
                break;
            }
        }

        if (symbolCount == 2) score += 1;
        else if (symbolCount == 3) score += 5;
        else if (symbolCount == 4) score += 100;

        if (colorCount == 2) score += 1;
        else if (colorCount == 3) score += 5;
        else if (colorCount == 4) score += 100;

        return score;
    }
}
