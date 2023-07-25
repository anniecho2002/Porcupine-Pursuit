package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import imgs.Img;

/**
 * A series of cells that make up a map.
 * Entities reside inside this map.
 * 
 * @author anniecho
 *
 */
class Map {

    private Random random = new Random();
    private int maxX = 16;
    private int maxY = 16;
    private final List<List<Cell>> innerMap = new ArrayList<>();

    /**
     * Creates a Map object.
     * Initialises the inner map.
     */
    public Map() {
        for (int x = 0; x < maxX; x++) {
            ArrayList<Cell> temp = new ArrayList<Cell>();
            innerMap.add(temp);
            for (int y = 0; y < maxY; y++) {
                if (x == 0 || x == maxX - 1 || y == 0 || y == maxY - 1){
                    temp.add(new Cell(List.of(getWallCell(0, true)), x, y));
                } else {
                    temp.add(new Cell(List.of(getGrassCell(0, true)), x, y));
                }
            }
        }
    }


    /**
     * Returns a grass cell.
     * @param num A number that corresponds to an image to return.
     * @param rand Boolean determines if it is random, or based off num parameter.
     * @return A grass cell with different background.
     */
    private Img getGrassCell(int num, boolean rand) {
        Img[] cellOptions = { Img.grass1, Img.grass2, Img.grass3, Img.grass4, Img.grass5 };
        int randomNumber = rand ? random.nextInt(5) : num;
        if (randomNumber < 0 || randomNumber > 4) return Img.grass1;
        else return cellOptions[randomNumber];
    }


    /**
     * Returns a wall/bush cell.
     * @param num A number that corresponds to an image to return.
     * @param rand Boolean determines if it is random, or based off num parameter.
     * @return A wall/bush cell with different background.
     */
    private Img getWallCell(int num, boolean rand) {
        Img[] outerCellOptions = { Img.bush0, Img.bush1, Img.bush2, Img.bush3, Img.bush4 };
        int randomNumber = rand ? random.nextInt(5) : num;
        if (randomNumber < 0 || randomNumber > 4)
            return Img.bush0;
        else
            return outerCellOptions[randomNumber];
    }


    /**
     * Returns a cell at a given x and y coordinate.
     * If it is out of bounds, return a random grass cell.
     * 
     * @param x Given x coordinate.
     * @param y Given y coordinate.
     * @return A cell at the given x and y coordinate.
     */
    public Cell get(int x, int y) {
        boolean isOut = x < 0 || y < 0 || x >= maxX || y >= maxY;
        if (isOut) {
            int combined = (Math.abs(x + y)) % 5;
            return new Cell(List.of(getGrassCell(combined, false)), x, y);
        }
        Cell result = innerMap.get(x).get(y);
        assert result != null;
        return result;
    }


    /**
     * Goes through each cell
     * 
     * @param p
     * @param range
     * @param action
     */
    public void performActionInRange(Coordinate centerCoord, int range, Consumer<Cell> action) {
        if(range < 0) throw new IllegalArgumentException("Range cannot be negative.");
        int minX = centerCoord.x() - range;
        int maxX = centerCoord.x() + range;
        int minY = centerCoord.y() - range;
        int maxY = centerCoord.y() + range;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Cell cell = get(x, y);
                if (cell != null) { // Make sure to check boundaries or handle out-of-bounds cases
                    action.accept(cell);
                }
            }
        }
    }
}