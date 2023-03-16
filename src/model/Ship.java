package model;

import java.util.Arrays;

public class Ship {
    private int[] startCoord;
    private int[] endCoord;
    private int length;
    private Boolean[] isShot;

    public Ship(int startR, int startC, int endR, int endC) {
        startCoord = new int[]{startR, startC};
        endCoord = new int[]{endR, endC};

        if (startR == endR) length = endC - startC + 1;
        else length = endR - startR + 1;

        isShot = new Boolean[length];
        Arrays.fill(isShot, false);
    }

    public int[] getStartCoord() { return startCoord; }
    public int[] getEndCoord() { return endCoord; }
    public int getLength() { return length; }

    public Boolean isShot() {
        for (int i = 0; i < length; i++) {
            if (!isShot[i]) return false;
        }
        return true;
    }

    // RETURN: true if ship is on the coordinates given
    public Boolean has(int r, int c) {
        return r >= startCoord[0] &&
                r <= endCoord[0] &&
                c >= startCoord[1] &&
                c <= endCoord[1];
    }

    // RETURN: true if ship was sunk
    // INPUT: assumes the ship is on the coordinates given (has() returns true)
    // MODIFY: changes isShot[] accordingly
    // todo testing
    public Boolean shoot(int r, int c) {
        int coord;

        if (startCoord[0] == endCoord[0]) coord = c - startCoord[1]; // x coordinates matching --> vertical ship
        else coord = r - startCoord[0]; // y coordinates matching --> horizontal ship

        isShot[coord] = true;
        return isShot();
    }

    // FOR TESTING PURPOSES
    public void shootAll() {
        for (int i = 0; i < length; i++) {
            isShot[i] = true;
        }
    }
}
