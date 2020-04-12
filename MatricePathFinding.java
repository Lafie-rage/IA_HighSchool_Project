import java.lang.Math;
import java.util.*;

public class MatricePathFinding {
  public static int manhattanDistance(Case c, Case s) {
    return (Math.abs(s.getX() - c.getX()) + Math.abs(s.getY() - c.getY()));
  }

  public static double euclideanDistance(Case c, Case s) {
    return (Math.sqrt(Math.pow(s.getX() - c.getX(), 2) + Math.pow(s.getY() - c.getY(), 2)));
  }

  public static Path findPathTo(Case e, Case s, Case[] tableau) {
    // Dijkstra

    System.out.println(e.x + " : " + e.y);
    System.out.println(s.x + " : " + s.y);

    ArrayList<Case> remainingCases = new ArrayList<Case>();
    ArrayList<Case> validCases = new ArrayList<Case>();


    for (Case c : tableau){
      if(c.getType() != Case.DUNE) {
        remainingCases.add(c);
        validCases.add(c);
      }
    }
    e.parcouru = 0;

    while(!remainingCases.isEmpty()) {
      Case x = getLowestDistanceCase(remainingCases, e);
      remainingCases.remove(x);
      for (Case c : getListNeighbours(x, validCases)) {
        if(c.parcouru > x.parcouru + (int)euclideanDistance(x, c)) {
          c.parcouru = x.parcouru + (int)euclideanDistance(x, c);
          c.previous = x;
          //System.out.println(c.previous.x + " " + c.previous.y);
        }
      }
    }

    Stack<Case> way = new Stack<Case>();

    int distance = 1;
    Case cpyS = s;

    while(cpyS != e) {
      way.push(cpyS);
      cpyS = cpyS.previous;
      distance++;
    }

    way.push(e);

    return new Path(distance, way);
  }

  public static Case getLowestDistanceCase(List<Case> list, Case e) {
    Case lowestDistanceCase = list.get(0);
    for (int i = 1; i < list.size(); i++) {
        if (list.get(i).parcouru < lowestDistanceCase.parcouru) {
            lowestDistanceCase = list.get(i);
        }
    }
    return lowestDistanceCase;
  }

  public static ArrayList<Case> getListNeighbours(Case x, List<Case> list) {
    ArrayList<Case> listNeighbours = new ArrayList<>();
    for(Case c : list) {
      if(c.getX() == x.getX())
        if(c.getY() == x.getY()+1 || c.getY() == x.getY()-1)
          listNeighbours.add(c);
      if(c.getY() == x.getY())
        if(c.getX() == x.getX()+1 || c.getX() == x.getX()-1)
          listNeighbours.add(c);
    }
    return listNeighbours;
  }

}
