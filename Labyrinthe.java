import java.util.ArrayList;
import java.util.Random;

class Labyrinthe{
  private int tailleX, tailleY;
  private Case tableau[];
  private ArrayList<Joueur> listeJoueur;
  public MatricePathFinding matricePathFinding;

  public Labyrinthe(){
    tailleX=3;
    tailleY=3;
    tableau = new Case[tailleX*tailleY];
    for(int i=0;i<tailleX*tailleY;i++)
      tableau[i]=new Case(i%tailleX, (int)i/tailleX);
    tableau[4].setType("SABLE");
    listeJoueur=new ArrayList<Joueur>();
  }

  public Labyrinthe(String infos){
    String[] tailleInfos=infos.split("/");
    if(tailleInfos.length!=3){
      System.out.println("Impossible de construire un labyrinthe à partir de ces informations :");
      System.out.println(infos);
      System.exit(0);
    }
    String[] taille=tailleInfos[0].split("x");
    if(taille.length!=2){
      System.out.println("Impossible de construire un labyrinthe à partir de ces informations car la taille n'est pas reconnue :");
      System.out.println(infos);
      System.exit(0);
    }
    try{
      tailleX=Integer.parseInt(taille[0]);
      tailleY=Integer.parseInt(taille[1]);
    } catch(java.lang.NumberFormatException e){
      System.out.println("Impossible de construire un labyrinthe à partir de ces informations car la taille n'est pas reconnue :");
      System.out.println(infos);
      System.exit(0);
    }


    String[] infosLaby = tailleInfos[1].split("-");
    if(infosLaby.length!=tailleX*tailleY){
      System.out.println("les infos du labyrinthe envoyé et la taille ne correspondent pas");
      System.out.println("tailleX : "+tailleX+" - tailleY : "+tailleY);
      System.out.println("infos : \n"+infos);
      System.exit(0);
    }

    tableau = new Case[tailleX*tailleY];

    for(int i=0;i<tailleX*tailleY;i++){
      if(infosLaby[i].equals("S"))
      tableau[i] = new Case("SABLE",0, i%tailleX, (int)i/tailleX);
      else{
        if(infosLaby[i].equals("D"))
        tableau[i] = new Case("DUNE",0, i%tailleX, (int)i/tailleX);
        else{
          if(infosLaby[i].equals("F"))
            tableau[i] = new Case("FRITE",0, i%tailleX, (int)i/tailleX);
          else{
            if(infosLaby[i].equals("B"))
              tableau[i] = new Case("BIERE",0, i%tailleX, (int)i/tailleX);
            else{
              try{
                tableau[i] = new Case("MOULE",Integer.parseInt(infosLaby[i]), i%tailleX, (int)i/tailleX);
              } catch(java.lang.NumberFormatException e){
                System.out.println("Je ne comprends pas cette case : "+infosLaby[i]);
                System.exit(0);
              }
            }
          }
        }
      }
    }

    listeJoueur=new ArrayList<Joueur>();

    String[] infosJoueur = tailleInfos[2].split("-");
    int nbJoueur=Integer.parseInt(infosJoueur[0]);
    for(int i=0;i<nbJoueur;i++){
      String[] infosPos = infosJoueur[i+1].split(",");
      listeJoueur.add(new Joueur(Integer.parseInt(infosPos[0]),Integer.parseInt(infosPos[1]),"joueur"+(i+1)));
    }

  }

  public Labyrinthe(Labyrinthe la){
    tailleX=la.tailleX;
    tailleY=la.tailleY;
    listeJoueur=new ArrayList<Joueur>();
    for(int i=0;i<la.listeJoueur.size();i++)
      listeJoueur.add(new Joueur(la.listeJoueur.get(i)));
    tableau=new Case[la.tableau.length];
    for(int i=0;i<la.tableau.length;i++)
      tableau[i]=new Case(la.tableau[i]);
  }

  public int getTailleX(){
    return tailleX;
  }

  public int getTailleY(){
    return tailleY;
  }

  public Case getXY(int x, int y){
    // TODO - Vérifier que X et y soit bien dans le plateau
    return tableau[y*tailleX+x];
  }

  public int getNbJoueur(){
    return listeJoueur.size();
  }

  public Joueur getJoueur(int n){
    return listeJoueur.get(n);
  }


  public int getIndex(int x, int y){
    return y*tailleX+x;
  }

  public String toString(){
    String retour=tailleX+"x"+tailleY+"/";
    retour=retour.concat(tableau[0].toString());
    for(int i=1;i<tableau.length;i++)
      retour=retour.concat("-"+tableau[i]);
    retour=retour.concat("/"+listeJoueur.size()+"-"+listeJoueur.get(0).getPosX()+","+listeJoueur.get(0).getPosY());

    for(int i=1;i<listeJoueur.size();i++)
      retour=retour.concat("-"+listeJoueur.get(i).getPosX()+","+listeJoueur.get(i).getPosY());

    return retour;
  }

  public String toStringLaby(){
    String retour="";
    for(int j=0;j<tailleY;j++){
      for(int i=0;i<tailleX;i++)
        if(getXY(i,j).getType()!=Case.MOULE)
          retour=retour.concat(" - "+getXY(i,j).getType());
        else
          retour=retour.concat(" - "+getXY(i,j).getPointRapporte());
      retour=retour.concat("\n");
    }

    return retour;
  }

  public boolean isADune(int x, int y, String direction) {
    switch(direction){
      case "E":
        x++;
      break;
      case "N":
        y--;
      break;
      case "S":
        y++;
      break;
      case "O":
        x--;
      break;
    }
    return tableau[y * tailleX + x].getType() == Case.DUNE;
  }

  public boolean isABeer(int x, int y, String direction) {
    switch(direction){
      case "None" : // Current case
        break;
      case "E":
        x++;
      break;
      case "N":
        y--;
      break;
      case "S":
        y++;
      break;
      case "O":
        x--;
      break;
    }
    return tableau[y * tailleX + x].getType() == Case.BIERE;
  }

  public boolean isChips(int x, int y, String direction) {
    switch(direction){
      case "E":
        x++;
      break;
      case "N":
        y--;
      break;
      case "S":
        y++;
      break;
      case "O":
        x--;
      break;
    }
    return tableau[y * tailleX + x].getType() == Case.FRITE;
  }

  public Path getPathTo(Case from, Case to) {
    return MatricePathFinding.findPathTo(from, to, tableau);
  }
}
