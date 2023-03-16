package model;

import java.util.Arrays;
import java.util.Random;

public class Board {
    String ABC = "ABCDEFGHIJ";
    //int[] SHIPS = {1,1,2,2,3,4,5}; og size = 8
    int SIZE = 4;
    int[] SHIPS = {1, 1, 2, 2, 3};
    char NOSHIP = '-';
    char SHOTSHIP = 'X';

    private int points;

    private char[][] board;
    private char[][] solution;

    private Ship[] ships;

    public Board() {
        board = new char[SIZE][SIZE];
        solution = new char[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(board[i], '~');
            Arrays.fill(solution[i], '~');
        }

        ships = new Ship[SHIPS.length];
        placeShips();
    }

    public int getShipsLength() { return ships.length; }
    public Boolean areShipsShot() {
        for (int i = 0; i < ships.length; i++) {
            if (!ships[i].isShot()) return false;
        }
        return true;
    }

    // Adding ships specified in SHIPS to the board
    private void placeShips() {
        Random r = new Random();

        // todo: this version assumes horizontal ships only.
        for (int i = 0; i < SHIPS.length; i++) {
            do {
                int startR = r.nextInt(SIZE);
                int startC = r.nextInt(SIZE);
                int endR = startR;
                int endC = startC + SHIPS[i] - 1;

                ships[i] = new Ship(startR, startC, endR, endC);
            }
            while (!isCompatible(ships[i]));

            for (int j = ships[i].getStartCoord()[1]; j <= ships[i].getEndCoord()[1]; j++) {
                char c;
                if (SHIPS[i] == 1) c = 'O';
                else if (ships[i].getStartCoord()[1] == j) c = '<';
                else if (ships[i].getEndCoord()[1] == j) c = '>';
                else c = '§';
                solution[ships[i].getStartCoord()[0]][j] = c;
            }
        }
    }

    // todo for non-horizontal ships
    // used during placing ships, checks if randomly generated coordinates are compatible with the board
    //      INPUT: assumes that both end coordinates are larger than/equal to start coordinates
    public Boolean isCompatible(Ship ship) {
        if (ship.getEndCoord()[1] >= SIZE
                || ship.getEndCoord()[0] >= SIZE
                )
            return false; // if ship doesn't fit on table
        for (int i = ship.getStartCoord()[1]; i <= ship.getEndCoord()[1]; i++) {
            if (solution[ship.getStartCoord()[0]][i] != '~') return false;
        }

        return true;
    }


    public void showBoard() {
        System.out.print("\t");
        for (int i = 0; i < SIZE; i++) { System.out.print(ABC.charAt(i)+" "); }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i+"\t");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(solution[i][j]+" ");
            }
            System.out.println();
        }
    }


    // INPUT: guess in a two-character format: 1st is letter 2nd is number
    // RETURN:  0 if there's no ship at position
    //          1 if the ship was sunk
    //          2 if the ship was shot but not sunk
    public int shootAt(int row, int column) {
        for (int i = 0; i < ships.length; i++) {
            if (ships[i].has(row, column)) {
                Boolean result = ships[i].shoot(row, column);
                modifyAt(row, column, SHOTSHIP);
                if (result) return 1;
                else {
                    return 2;
                }
            }
        }

        modifyAt(row, column, NOSHIP);
        return 0;
    }

    // RETURN:  true if guess is in a 2-character long letter-int format, corresponding to the current table size
    public Boolean isValidGuess(String guess) {
        char columnChar = guess.charAt(0);
        int row = Character.getNumericValue(guess.charAt(1));

        char charOnBoard;
        if (row >= 0 && row < SIZE && isValidLetter(columnChar)) charOnBoard = board[row][ABC.indexOf(columnChar)];
        else return false;

        return guess.length() == 2
                && charOnBoard != NOSHIP && charOnBoard != SHOTSHIP;
    }

    // RETURN:  true if letter is in ABC range
    public Boolean isValidLetter(char c) {
        for (int i = 0; i < SIZE; i++) {
            if (c == ABC.charAt(i)) return true;
        }
        return false;
    }

    // MODIFY: board at given coordinates
    // todo: current version also modifies solution for easier testing, delete!
    public void modifyAt(int r, int c, char placeholder) {
        board[r][c] = placeholder;
        solution[r][c] = placeholder; // todo: DELETE!!!!
    }

    public char getCharAt(int r, int c) {
        return board[r][c];
    }

    public int getShipLengthAt(int column, int row) {
        for (int i = 0; i < ships.length; i++) {
            if (ships[i].has(row, column)) {
                return ships[i].getLength();
            }
        }

        return 0; // again, this should never-ever happen
    }

    // FUNCTIONS FOR TESTING PURPOSES
    public Ship[] getShips() {
        return ships;
    }
    public void shootAllShips() {
        for (int i = 0; i < ships.length; i++) {
            ships[i].shootAll();
        }
    }


}
