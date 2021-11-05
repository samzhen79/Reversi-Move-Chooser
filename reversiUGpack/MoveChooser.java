import java.util.ArrayList;
import java.util.Collections;

public class MoveChooser {
  
    public static Move chooseMove(BoardState boardState){
		int searchDepth = Othello.searchDepth;

	    return minimax(boardState, searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static int minimax_val(BoardState node, int depth, int alpha, int beta) {
    	ArrayList<Move> moves = node.getLegalMoves();
		int result;

    	if (depth == 0){
    		return evaluateNode(node);
    	}
    	// White (Computer)
    	else if(node.colour == 1) {
    		Integer maxResult = Integer.MIN_VALUE;
    		for (int i = 0; i <= moves.size(); i++) {
    			// Copy node as to not affect the actual board state
    			BoardState node1 = node.deepCopy();

    			// If no moves are available then switch player turn
    			if (moves.isEmpty()) {
					node1.colour *= -1;
    			}
    			// Since using "i <= moves.size()" have to break when i = moves.size()
    			else if (i < moves.size()){
	    			Move move = moves.get(i);
	    			node1.makeLegalMove(move.x, move.y);
    			}
    			else {
    				break;
    			}

				result = minimax_val(node1, depth-1, alpha, beta);
				maxResult = Math.max(maxResult, result);

    			alpha = Math.max(alpha, result);
    			if (beta <= alpha) {
    				// Prune branch
    				break;
    			}
    		}
    		return maxResult;
    	}
    	// Black (Player)
    	else {
    		Integer minResult = Integer.MAX_VALUE;
    		for (int i = 0; i <= moves.size(); i++) {
    			BoardState node1 = node.deepCopy();

    			if (moves.isEmpty()) {
					node1.colour *= -1;
    			}
    			else if (i < moves.size()) {
	    			Move move = moves.get(i);
	    			node1.makeLegalMove(move.x, move.y);
    			}
    			else {
    				break;
    			}

				result = minimax_val(node1, depth-1, alpha, beta);
				minResult = Math.min(minResult, result);

    			beta = Math.min(beta, result);
    			if (beta <= alpha) {
    				break;
    			}
    		}
    		return minResult;
    	}
    }

    private static Move minimax(BoardState node, int depth, int alpha, int beta) {
    	ArrayList<Move> moves = node.getLegalMoves();
    	ArrayList<Integer> results = new ArrayList<>();
    	int result;

    	// If no moves are available then we don't need to choose a move
    	if (moves.isEmpty()) {
			return null;
    	}
    	// White (Computer)
    	else if(node.colour == 1) {
    		for (int i = 0; i < moves.size(); i++) {
    			// Copy node as to not affect the actual board state
				BoardState node1 = node.deepCopy();
    			Move move = moves.get(i);

    			node1.makeLegalMove(move.x, move.y);

    			result = minimax_val(node1, depth-1, alpha, beta);
    			results.add(result);

    			alpha = Math.max(alpha, result);
    			if (beta <= alpha) {
    				// Prune branch
    				break;
    			}
    		}
    		// The index of the max result matches the index of the move that produces the result
    		return moves.get(results.indexOf(Collections.max(results)));
    	}
    	// Black (Player)
    	// Note: Unused as only moves for white will be chosen by the MoveChooser class
    	// Could be useful in other applications (Computer vs Computer)
    	else {
    		for (int i = 0; i < moves.size(); i++) {
    			BoardState node1 = node.deepCopy();
    			Move move = moves.get(i);

    			node1.makeLegalMove(move.x, move.y);

    			result = minimax_val(node1, depth-1, alpha, beta);
    			results.add(result);

    			beta = Math.min(beta, result);
    			if (beta <= alpha) {
    				break;
    			}
    		}
    		return moves.get(results.indexOf(Collections.min(results)));
    	}
    }

    private static int evaluateNode(BoardState boardState) {
    	// Higher values are better positions
    	int staticEvaluation[][] = {{120,-20,20,5,5,20,-20,120},
    								{-20,-40,-5,-5,-5,-5,-40,-20},
    								{20,-5,15,3,3,15,-5,20},
    								{5,-5,3,3,3,3,-5,5},
    								{5,-5,3,3,3,3,-5,5},
    								{20,-5,15,3,3,15,-5,20},
    								{-20,-40,-5,-5,-5,-5,-40,-20},
    								{120,-20,20,5,5,20,-20,120}};
    	int value = 0;

    	for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				// White (Computer)
				if (boardState.getContents(row,col) == 1) {
					value += staticEvaluation[row][col];
				}
				// Black (Player)
				else if (boardState.getContents(row,col) == -1) {
					value -= staticEvaluation[row][col];
				}
			}
		}

	return value;
	}
}
