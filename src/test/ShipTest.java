package test;

import model.Ship;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ShipTest {
    Ship testShip;
    Ship testShip1; // test Ship with length of 1
    int[] coord1 = {3, 0};

    @Before
    public void setUp() {
        testShip = new Ship (0, 0, 3, 0);
        testShip1 = new Ship (2, 2, 2, 2);
    }

    @Test
    public void testLength() {
        assertEquals(testShip.getLength(), 4);
        assertEquals(testShip1.getLength(), 1);
    }

    @Test
    public void testHas() {
        assertFalse(testShip.has(0, 2));
        assertTrue(testShip.has(0,0));
        assertTrue(testShip.has(coord1[0], coord1[1]));
        assertFalse(testShip.has(0, 4));
        assertFalse(testShip.has(5, 0));
    }

    @Test
    public void testShoot() {
        assertFalse(testShip.isShot());
        Boolean[] prevIsShot = new Boolean[testShip.getLength()];
        Arrays.fill(prevIsShot, false);
        assertFalse(testShip.shoot(coord1[0], coord1[1]));
        testShip.shootAll();
        assertTrue(testShip.isShot());
    }
}
