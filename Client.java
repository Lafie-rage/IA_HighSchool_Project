import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

public class Client{

	private static String lastMove = "";
  public static ListeBonus bonus = new ListeBonus();
	public static Labyrinthe laby;
	public static Path pathToFollow = null;
	public static Case nearestMoule = null;

	public static void main(String[] args){
		if(args.length!=3){
			System.out.println("Il faut 3 arguments : l'adresse ip du serveur, le port et le nom d'équipe.");
			System.exit(0);
		}
		Random rand=new Random();

		try{
			Socket s = new Socket(args[0],Integer.parseInt(args[1]));
			boolean fin=false;

			// ecriture
			OutputStream os  = s.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			//lecture
			InputStream is = s.getInputStream();
			BufferedReader bf = new BufferedReader(
			new InputStreamReader(is));

			pw.println(args[2]);
			pw.flush();

			String numJoueur = bf.readLine();

			System.out.println("Numero de joueur : "+numJoueur);

			while(!fin){
				String msg = bf.readLine();

				fin=msg.equals("FIN");

				if(!fin){

					/*-----------------------------------------------------------------------*/

					/*TODO - mettre votre stratégie en place ici*/
					/*Quelques lignes de code pour vous aider*/

					//Creation du labyrinthe en fonction des informations recues
					//Bande de veinards, c'est déjà écrit ! Par contre la doc de cette classe n'est pas complète.
					//Faut pas trop en demander non plus !
					laby = new Labyrinthe(msg);
					Joueur curPlayer = new Joueur(laby.getJoueur(Integer.parseInt(numJoueur)));
					ArrayList<String> directions = getPossiblesDirections(laby, curPlayer);

					//Informations sur le joueur
					ArrayList<Integer> infosMoule = new ArrayList<Integer>();
					ArrayList<Case> listMoules = new ArrayList<Case>();
					//Parcours du plateau pour trouver toutes les moules et leur valeur
					for(int j=0;j<laby.getTailleY();j++)
						for(int i=0;i<laby.getTailleX();i++)
							if(laby.getXY(i,j).getType()==Case.MOULE){
								listMoules.add(laby.getXY(i,j));
								infosMoule.add(i);infosMoule.add(j);infosMoule.add(laby.getXY(i,j).getPointRapporte());
							}

					//Affichage des informations sur les moules du plateau
					if(pathToFollow == null) {
						Case posPlayer = laby.getXY(curPlayer.getPosX(), curPlayer.getPosY());
						nearestMoule = getNearestMoule(listMoules, posPlayer);

						pathToFollow = laby.getPathTo(posPlayer, nearestMoule);
					}
					msg = getChoosenDirection();
					pw.println(msg);
					pw.flush();
				}

			}
			s.close();

		} catch(Exception e){
			System.err.println(e);
			e.printStackTrace();
		}
	}

	private static ArrayList<String> getPossiblesDirections(Labyrinthe laby, Joueur player) {
		ArrayList<String> directions = new ArrayList<String>();
		ListIterator<String> iter = directions.listIterator();
		if (!laby.isADune(player.getPosX(), player.getPosY(), "E")) {
			directions.add("E");
		}
		if (!laby.isADune(player.getPosX(), player.getPosY(), "O")) {
			directions.add("O");
		}
		if (!laby.isADune(player.getPosX(), player.getPosY(), "S")) {
			directions.add("S");
		}
		if (!laby.isADune(player.getPosX(), player.getPosY(), "N")) {
			directions.add("N");
		}
		if(directions.size() > 1)
			directions.remove(lastMove);
		return directions;
	}

	private static String getChoosenDirection() {
		int nbBieres = 0,
				nbFrites = 0;
		String msg = pathToFollow.getNextDirection(); // comportement final...
		Case next = pathToFollow.way.peek();
		if(next.getType() == 2)
			nbFrites++;
		if(next.getType() == 3)
			nbBieres++;
		System.out.println("Nb frites : " + bonus.getCountFrites());
		if(bonus.getCountBieres() > 0 && pathToFollow.way.size() > 2) {
			System.out.println("BIERE");
			bonus.useBieres();
			msg = "B-" + msg;
			for(int i = 0; i < 2; i++) {
				msg += "-" + pathToFollow.getNextDirection();
				next = pathToFollow.way.peek();
				if(next.getType() == 2)
					nbFrites++;
				if(next.getType() == 3)
					nbBieres++;
			}
		}
		else if(bonus.getCountFrites() > 0 && pathToFollow.way.size() > 1 && pathToFollow.lookAtNextDirection().equals(msg)) {
			System.out.println("FRITES");
			bonus.useFrites();
			pathToFollow.getNextDirection();
			msg = "F-" + msg;
			next = pathToFollow.way.peek();
			nbFrites = 0;
			nbBieres = 0;
			if(next.getType() == 2)
				nbFrites++;
			if(next.getType() == 3)
				nbBieres++;

		}
		if(pathToFollow.way.peek() == nearestMoule) pathToFollow= null;
		bonus.addBieres(nbBieres);
		bonus.addFrites(nbFrites);
		System.out.println(msg);
		return msg;
	}

	private static Case getNearestMoule(ArrayList<Case> listMoules, Case player) {
    int distanceToNearest = Integer.MAX_VALUE;
    Case nearest = null;
    for(Case moule : listMoules) {
      int distance =  laby.getPathTo(player, moule).distance;
      if(distanceToNearest > distance) {
        distanceToNearest = distance;
        nearest = moule;
      }
		}
		return nearest;
  }

}
