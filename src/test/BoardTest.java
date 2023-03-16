package test;

import model.Board;
import model.Ship;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {
    Board board;
    Ship wrongShips[];

    @Before
    public void setup() {
        board = new Board();
        wrongShips = new Ship[2];
        wrongShips[0] = new Ship(6, 6, 15, 6);
        wrongShips[1] = board.getShips()[0];
    }

    @Test
    public void testIsCompatible() {
        assertFalse(board.isCompatible(wrongShips[0]));
        assertFalse(board.isCompatible(wrongShips[1]));
    }

    @Test
    public void testAreShipsShot() {
        assertFalse(board.areShipsShot());
        board.shootAllShips();
        assertTrue(board.areShipsShot());
    }

    @Test
    public void testIsValidGuess() {
        assertTrue(board.isValidGuess("A3"));
        assertFalse(board.isValidGuess("A34"));
        assertFalse(board.isValidGuess("BB"));
        assertFalse(board.isValidGuess("05"));
        assertFalse(board.isValidGuess("K2"));
    }

    @Test
    public void testIsValidLetter() {
        assertTrue(board.isValidLetter('A'));
        assertTrue(board.isValidLetter('J'));
        assertFalse(board.isValidLetter('K'));
        assertFalse(board.isValidLetter('0'));
    }

}
