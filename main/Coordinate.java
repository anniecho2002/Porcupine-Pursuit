package main;

/**
 * A coordinate that corresponds to a Point on the Map
 * @author anniecho
 *
 */
record Coordinate(int x, int y){
  
  /**
   * Returns the corresponding Point to the coordinate.
   * @return
   */
  public Point toPoint(){
    return new Point(x + 0.5d, y + 0.5d);
  }
  
}