package model;

import java.util.*;

public class Game {

    private Board player;
    private Board computer;

    private int nextGuesses[][] = new int[4][2];
    private boolean generatedGuesses = false;
    private ArrayList<Integer> remainingShips = new ArrayList<Integer>();

    public Game () {
        player = new Board();
        computer = new Board();

        initializeGuess();
    }

    public void showGame() {
        System.out.println("--\tPlayer's board --");
        player.showBoard();
        System.out.println("------------------------");
        System.out.println("--\tComputer's board --");
        computer.showBoard();
    }

    public void play() {
        int currentLength;
        Random r = new Random();

        initializeShipLength();

        while (notSolved(computer) && notSolved(player)) {
            showGame();

            userGuess();

            if (!notSolved(computer)) {
                System.out.println("Congratulations! You won!");
                break;
            }

            // todo: computer guess

            String guess;
            int column, row, result;
            if (!generatedGuesses) {
                do {
                    column = r.nextInt(computer.SIZE);
                    row = r.nextInt(computer.SIZE);
                    guess = "" + computer.ABC.charAt(column) + row;
                } while (!player.isValidGuess(guess));
            }
            else {
                int next[] = getNextGuess();
                guess = "" + computer.ABC.charAt(next[0]) + next[1];
                column = next[0];
                row = next[1];
            }

            System.out.println("Computer guess: " + guess);

            result = player.shootAt(row, column);

            switch (result) {
                case 0:
                    // no ship at given coordinates: delete guess if it was included in generatedGuesses
                    if (generatedGuesses) generatedGuesses = deleteGuess(column, row);
                    break;
                case 1:
                    // ship was sunk, re-initialize nextGuesses variable, delete appropriate ship length
                    int shipLength = player.getShipLengthAt(column, row);
                    remainingShips.remove(shipLength);
                    initializeGuess();
                    break;
                case 2:
                    // todo: there's a ship, generate next guesses

                    nextGuesses = generateNextGuesses(column, row, generatedGuesses);
                    generatedGuesses = true;

                    break;
            }
        }

        if (!notSolved(player)) {
            System.out.println("You lost. Better luck next time!");
        }
    }

    private void initializeShipLength() {
        for (int i = 0; i < player.SHIPS.length; i++) {
            remainingShips.add(player.SHIPS[i]);
        }
    }

    private void userGuess() {
        Scanner in = new Scanner(System.in);
        String guess;
        do {
            System.out.println("Take a guess! e.g. A2");
            guess = in.nextLine();
        } while (!computer.isValidGuess(guess));
        int result = computer.shootAt(Character.getNumericValue(guess.charAt(1)), player.ABC.indexOf(guess.charAt(0)));

        switch (result) {
            case 0:
                System.out.println("There's no ship at given coordinates");
                break;
            case 1:
                System.out.println("There was a ship at given position and it was sunk");
                break;
            case 2:
                System.out.println("There was a ship at given position and it is still afloat");
                break;
        }
    }

    public Boolean notSolved(Board board) {
        return !board.areShipsShot();
    }

    private void initializeGuess() {
        for (int i = 0; i < 4; i++) {
            Arrays.fill(nextGuesses[i], -1);
        }
    }

    private int[][] generateNextGuesses(int column, int row, boolean goodDir) {
        int nextGuesses[][] = new int[4][2];
        int maxShipLengthRem = 5;

        for (int i = 0; i < remainingShips.toArray().length; i++) {
            int current = remainingShips.get(i);
            if (current != -1 && current < maxShipLengthRem) maxShipLengthRem = current;
        }

        if (goodDir) {
            for (int i = -1; i <= 1; i += 2) {
                if (column + i >= 0 && column + i < player.SIZE) {
                    // shot ship is at the left or right
                    if (player.getCharAt(row, column+i) == player.SHOTSHIP) {
                        // generate guesses in this direction
                        if (column + i - 1 > 0 && player.getCharAt(row, column+i-1) == '~') { // to the left
                            nextGuesses[0][0] = column+i-1;
                            nextGuesses[0][1] = row;
                        }

                        if (column + i + 1 < player.SIZE && player.getCharAt(row, column+i+1) == '~') { // to the right
                            nextGuesses[1][0] = column+i+1;
                            nextGuesses[1][1] = row;
                        }

                        break;
                    }

                    // todo: shot ship is either up or down; not implemented yet because of only horizontal ships
                }
            }
        }
        else {
            for (int i = -1; i <= 1; i += 2) {
                nextGuesses[i+1][0] = column+i;
                nextGuesses[i+1][1] = row;
            }
            for (int i = 0; i <= 2; i += 2) {
                nextGuesses[i+1][0] = column;
                nextGuesses[i+1][1] = row+i;
            }
        }

        return nextGuesses;
    }

    private boolean deleteGuess(int column, int row) {
        boolean guessesLeft = false;
        for (int i = 0; i < 4; i++) {
            if (nextGuesses[i][0] == column && nextGuesses[i][1] == row) {
                nextGuesses[i][0] = -1;
                nextGuesses[i][1] = -1;
            }
            else if (nextGuesses[i][0] != -1 && nextGuesses[i][1] != -1) {
                guessesLeft = true;
            }
         }

        return guessesLeft;
    }

    // RETURN: next valid guess
    //         only called when generatedGuesses is true (there is at least one)
    private int[] getNextGuess() {
        for (int i = 0; i < 4; i++) {
            String guess = "" + player.ABC.charAt(nextGuesses[i][0]) + nextGuesses[i][1]; // todo: wrong!

            if (!player.isValidGuess(guess)) System.out.println("Not a valid guess: " + guess);
            if (nextGuesses[i][0] != -1 && player.isValidGuess(guess)) {
                return nextGuesses[i];
            }
        }

        return new int[0]; // this should never-ever happen! if it returns this u fucked up lol
    }
}
