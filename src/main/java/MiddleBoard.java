import java.util.Collection;
import java.util.LinkedList;

public class MiddleBoard extends Board2 {
	/**
	 * 
	 */
	// private static final long serialVersionUID = 6091744538421101679L;
	private int TOPBIT;

	private int SIDEMASK;

	private int LASTMASK, ENDBIT;

	private int bound1, bound2;

	private long count2 = 0, count8 = 0, count4 = 0;

	public static long backtrackcount = 0;

	public MiddleBoard(int size) {
		super(size);

		TOPBIT = 1 << sizee; // size-1
		SIDEMASK = TOPBIT | 1;
		LASTMASK = TOPBIT | 1;
		ENDBIT = TOPBIT >> 1;
	}

	public String getLastMask() {
		return java.lang.Integer.toBinaryString(LASTMASK);
	}

	public String getEndBit() {
		return java.lang.Integer.toBinaryString(ENDBIT);
	}

	@Override
	Collection<Board2> init() {
		Collection<Board2> boards = new LinkedList<Board2>();
		MiddleBoard bnew;
		for (bound1 = 1, bound2 = size - 2; bound1 < bound2; bound1++, bound2--) {
			try {
				bnew = (MiddleBoard) this.clone();
			} catch (CloneNotSupportedException e) {
				// should never happen
				e.printStackTrace();
				throw new InternalError(e.toString());
			}
			bnew.setAndIncLine(1 << bound1); // Af few more boards
			boards.add(bnew);
			LASTMASK |= LASTMASK >> 1 | LASTMASK << 1;
			ENDBIT >>= 1;
		}
		return boards;
	}

	public String toString() {

		StringBuffer bout = new StringBuffer(super.toString());
		return bout.toString();
	}

	@Override
	public boolean checkBounds() {

		if (getLine() < bound1) {
			possiblePlacements[currentBoardLine] |= SIDEMASK;
			possiblePlacements[currentBoardLine] ^= SIDEMASK;
		} else if (getLine() == this.bound2) {

			if ((isOccupiedHorizontal[this.getBoardLine()] & SIDEMASK) == 0)
				// This board can never become a solution
				return false;

			if ((isOccupiedHorizontal[this.getBoardLine()] & SIDEMASK) != SIDEMASK)
				possiblePlacements[currentBoardLine] &= SIDEMASK;
		}

		return true;
	}

	@Override
	public synchronized void backtrackDoubleLinkedList() {
		// System.out.println("backtrackLinkedList, mask is : " + MASK);
		BoardLine firstLine = new BoardLine(
				isOccupiedHorizontal[currentBoardLine],
				isOccupiedRightDiagonal[currentBoardLine],
				isOccupiedLeftDiagonal[currentBoardLine], currentBoardLine);
		BoardLine line = firstLine;

		BoardLine bl = firstLine;

		for (int linenr = currentBoardLine; linenr < this.size; linenr++) {
			bl = new BoardLine(bl);
		}

		while (line != null) {
			// checkpoint here
			if (line.lineNumber == sizee) {
				if (line.hasPossiblePlacements()) {
					if ((line.possiblePlacements & LASTMASK) == 0) {
						line.queenPlacement = line.possiblePlacements;
						
						// We could make a special version of check that is faster?
						setBoard(line);													
						this.check();
					}
				}
			} else {
				if (line.lineNumber < bound1) {
					line.possiblePlacements |= SIDEMASK;
					line.possiblePlacements ^= SIDEMASK;
				} else if (line.lineNumber == bound2) {

					if ((line.horisontal & SIDEMASK) == 0) {
						line.possiblePlacements = 0;
					}

					if ((line.horisontal & SIDEMASK) != SIDEMASK) {
						line.possiblePlacements &= SIDEMASK;
					}
				}
			}

			// backtrack
			while (line != null && line.possiblePlacements == 0) {
				line = line.prev;
				if (line == null) {
					return;
				}
			}
			int selected = -line.possiblePlacements & line.possiblePlacements;
			line.queenPlacement = selected;
			// int selected = Integer.lowestOneBit(line.possiblePlacements);
			line.possiblePlacements ^= selected;
			
			
			line.next.horisontal = line.horisontal | selected;
			line.next.rightDiagonal = (line.rightDiagonal | selected) >>> 1;
			line.next.leftDiagonal = (line.leftDiagonal | selected) << 1;
			line.next.possiblePlacements = Board2.MASK
					& ~(line.next.leftDiagonal | line.next.horisontal | line.next.rightDiagonal);
			line = line.next;

			// System.out.println("end of while: " + line.lineNumber);
		}

	}
	public void setBoard(BoardLine last) {
		BoardLine foo = last;
		while (foo != null) {
			board[foo.lineNumber] = foo.queenPlacement;
			foo = foo.prev;
		}
	}
	
	@Override
	public synchronized void backtrackLinkedList() {
		// System.out.println("backtrackLinkedList, mask is : " + MASK);
		BoardLine firstLine = new BoardLine(
				isOccupiedHorizontal[currentBoardLine],
				isOccupiedRightDiagonal[currentBoardLine],
				isOccupiedLeftDiagonal[currentBoardLine], currentBoardLine);
		BoardLine line = firstLine;
		while (line != null) {
			// line.printBoard();
			// checkpoint here
			if (line.lineNumber == sizee) {
				if (line.hasPossiblePlacements()) {
					if ((line.possiblePlacements & LASTMASK) == 0) {
						line.queenPlacement = line.possiblePlacements;						
						// We could make a special version of check that is faster?
						setBoard(line);
														
						this.check();
					}
				}
			} else {
				if (line.lineNumber < bound1) {
					line.possiblePlacements |= SIDEMASK;
					line.possiblePlacements ^= SIDEMASK;
				} else if (line.lineNumber == bound2) {

					if ((line.horisontal & SIDEMASK) == 0) {
						line.possiblePlacements = 0;
					}

					if ((line.horisontal & SIDEMASK) != SIDEMASK) {
						line.possiblePlacements &= SIDEMASK;
					}
				}
			}

			// backtrack
			while (line != null && line.possiblePlacements == 0) {
				line = line.prev;
				if (line == null) {
					return;
				}
			}

			int selected = -line.possiblePlacements & line.possiblePlacements;
			line.queenPlacement = selected;
			// int selected = Integer.lowestOneBit(line.possiblePlacements);
			line.possiblePlacements ^= selected;
			line = new BoardLine((line.horisontal | selected),
					(line.rightDiagonal | selected) >>> 1,
					(line.leftDiagonal | selected) << 1, line);

			// System.out.println("end of while: " + line.lineNumber);
		}

	}

	@Override
	public synchronized final void backtrackIterative() {
		int bit;
		int bitmap; // used for minimizing array lookups

		long begin = System.currentTimeMillis();
		// for lines above 'top' queen placement is predetermined
		while (currentBoardLine >= top) {
			countBacktrack();

			if (suspendBacktrack) {
				// dout("DETECTED a suspend request");
				long now = System.currentTimeMillis();
				long diff = now - begin;

				time += diff;
				// Don't add previous time spent again
				begin = System.currentTimeMillis();
				while (suspendBacktrack) {
					try { // We ignore interrupts......
						wait(); // Allow checkpointing thread to enter the
						// boards monitor
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (exitAfterCheckpoint) // testing purposes only
					return;
			}

			bitmap = Board2.MASK
					& ~(isOccupiedLeftDiagonal[currentBoardLine]
							| isOccupiedHorizontal[currentBoardLine] | isOccupiedRightDiagonal[currentBoardLine]);

			if (currentBoardLine == sizee) {
				if (bitmap != 0) {

					if ((bitmap & LASTMASK) == 0) {
						board[currentBoardLine] = bitmap;
						// System.out.println("b2: " + y + ", " + left + ", " +
						// down + ", " + right);
						this.check();
					}
					bitmap = 0; // We take the only solution that exists
				}
			} else {
				if (currentBoardLine < bound1) {
					bitmap |= SIDEMASK;
					bitmap ^= SIDEMASK;
				} else if (currentBoardLine == bound2) {

					if ((isOccupiedHorizontal[currentBoardLine] & SIDEMASK) == 0) {
						bitmap = 0;
					}

					if ((isOccupiedHorizontal[currentBoardLine] & SIDEMASK) != SIDEMASK) {
						bitmap &= SIDEMASK;
					}
				}
			}

			possiblePlacements[currentBoardLine] = bitmap;

			// Go back up if no possibleplacements
			if (bitmap == 0) {

				// NQueenBoards.dout("No more possible solutions: "
				// + currentBoardLine);

				while ((currentBoardLine >= top)
						&& (possiblePlacements[currentBoardLine]) == 0) {
					currentBoardLine--;
					// NQueenBoards.dout("Going Back " + currentBoardLine);

				}
			}

			/*
			 * Get next "sibling": Select first onbit (from right) Save our
			 * current selection remove the selection from possible solutions
			 * (So it isn't chosen again)
			 */
			possiblePlacements[currentBoardLine] ^= board[currentBoardLine] = -possiblePlacements[currentBoardLine]
					& possiblePlacements[currentBoardLine];

			bit = board[currentBoardLine]; // Previously chosen

			isOccupiedLeftDiagonal[currentBoardLine + 1] = (isOccupiedLeftDiagonal[currentBoardLine] | bit) << 1;
			isOccupiedHorizontal[currentBoardLine + 1] = (isOccupiedHorizontal[currentBoardLine] | bit);
			isOccupiedRightDiagonal[currentBoardLine + 1] = (isOccupiedRightDiagonal[currentBoardLine] | bit) >> 1;

			if (currentBoardLine >= top) {
				currentBoardLine++; // Go to child/Next line
			}

		}
		// We have finished board calculation
		time += System.currentTimeMillis() - begin;
		complete = true;

	}

	public void countBacktrack() {
		backtrackcount += 1;
	}

	public static long getBacktrackCount() {
		return backtrackcount;
	}

	public int getBound1() {
		return bound1;
	}

	public int getBound2() {
		return bound2;
	}

	public String getType() {
		return "middleboard";
	}

	public void backtrackRecursive(int y, int left, int down, int right) {
		countBacktrack();
		// System.out.println("backtrackMiddle");

		int bitmap;
		int bit;

		bitmap = MASK & ~(left | down | right);

		if (y == sizee) {
			if (bitmap != 0) {
				if ((bitmap & LASTMASK) == 0) {
					board[y] = bitmap;
					// System.out.println("b2: " + y + ", " + left + ", " + down
					// + ", " + right);
					this.check();
				}
			}
		} else {
			if (y < bound1) {
				bitmap |= SIDEMASK;
				bitmap ^= SIDEMASK;
			} else if (y == bound2) {

				if ((down & SIDEMASK) == 0)
					return;

				if ((down & SIDEMASK) != SIDEMASK) {
					bitmap &= SIDEMASK;
				}
			}
			while (bitmap != 0) {
				bit = -bitmap & bitmap;
				board[y] = bit;
				bitmap ^= board[y];
				backtrackRecursive(y + 1, (left | bit) << 1, down | bit,
						(right | bit) >> 1);
			}
			// NQueenBoards.dout("<");
		}
	}

	public void check() {
		int own, you, bit, ptn;

		if (board[bound2] == 1) {
			for (ptn = 2, own = 1; own <= sizee; own++, ptn <<= 1) {
				bit = 1;
				for (you = sizee; board[you] != ptn && board[own] >= bit; you--) {
					bit <<= 1;
				}
				if (board[own] > bit)
					return;
				if (board[own] < bit)
					break;
			}
			if (own > sizee) {
				count2++;
				return;
			}
		}

		/* 180-degree rotation */
		if (board[sizee] == ENDBIT) {
			for (you = sizee - 1, own = 1; own <= sizee; own++, you--) {
				bit = 1;
				for (ptn = TOPBIT; ptn != board[you] && board[own] >= bit; ptn >>= 1) {
					bit <<= 1;
				}
				if (board[own] > bit)
					return;
				if (board[own] < bit)
					break;
			}
			if (own > sizee) {
				count4++;
				return;
			}
		}

		/* 270-degree rotation */

		if (board[bound1] == TOPBIT) {
			for (ptn = TOPBIT >> 1, own = 1; own <= sizee; own++, ptn >>= 1) {
				bit = 1;
				for (you = 0; board[you] != ptn && board[own] >= bit; you++) {
					bit <<= 1;
				}
				if (board[own] > bit)
					return;
				if (board[own] < bit)
					break;
			}
		}
		count8++;

	}

	@Override
	long getTotal() {
		return count2 * 2 + count4 * 4 + count8 * 8;
	}

	public long getCount8() {
		return count8;
	}

	public long getCount4() {
		return count4;
	}

	public long getCount2() {
		return count2;
	}

	@Override
	long getUnique() {
		// System.out.println("b2(" + this.Count2 + ", " + this.Count4 + ", " +
		// this.Count8 + ")");
		return this.count2 + this.count4 + this.count8;
	}
}
