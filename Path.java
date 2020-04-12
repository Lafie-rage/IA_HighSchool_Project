import java.util.Stack;

public class Path {
  public int distance;
  public Stack<Case> way;

  public Path() {
    this.distance = Integer.MAX_VALUE;
    this.way = new Stack<Case>();
  }

  public Path(int distance) {
    this();
    this.distance = distance;
  }

  public Path(int distance, Stack<Case> way) {
    this(distance);
    this.way = way;
  }

  public String getNextDirection() {
    Case from = way.pop();
    Case to = way.peek();
    if(from.x + 1 == to.x && from.y == to.y) { // Est
      return "E";
    }
    if(from.x - 1 == to.x && from.y == to.y) { // Ouest
      return "O";
    }
    if(from.y + 1 == to.y && from.x == to.x) { // Sud
      return "S";
    }
    if(from.y - 1 == to.y && from.x == to.x) { // Nord
      return "N";
    }
    return "ERROR";
  }

  public String lookAtNextDirection() {
    Case from = way.pop();
    Case to = way.peek();
    way.push(from);
    if(from.x + 1 == to.x && from.y == to.y) { // Est
      return "E";
    }
    if(from.x - 1 == to.x && from.y == to.y) { // Ouest
      return "O";
    }
    if(from.y + 1 == to.y && from.x == to.x) { // Sud
      return "S";
    }
    if(from.y - 1 == to.y && from.x == to.x) { // Nord
      return "N";
    }
    return "ERROR";
  }
}
