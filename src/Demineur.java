import java.util.Scanner;

public class Demineur {
	
	public static Scanner sc = new Scanner(System.in);
	public static int niveauJeu = 0;
	public static int tailleTableau = 0;
	public static Tableau tableau;
	public static boolean gameOver = false;
	public static boolean play = true;
	public static int x = 0;
	public static int y = 0;
	public static char c = ' ';
	public static int help = 0;
    public static int nombreBombes = 0;
	
	public static void main(String[] args) {
		
		reglagesJeu();
        calculNombreBombesEtHelps();
		tableau = new Tableau(tailleTableau, nombreBombes);
		
		premiereSelection();
		tableau.Generer(x, y);

		while (play) {
		
			if (gameOver || Gagner()) {
				for (int i = 0; i < tableau.taille; i++){
					for (int j = 0; j < tableau.taille; j++){
						tableau.cell[i][j].Voir = true;
					}
				}
				tableau.Afficher();
				if (gameOver) 
					System.out.println("Game Over !!!");
				else {
					System.out.println("Felicitations pour votre victoire!!");
					if (niveauJeu == 1) System.out.println("Vous etes moyennement intelligent !");
					if (niveauJeu == 2) System.out.println("Vous etes intelligent (mais pas trop quand meme) !!");
					if (niveauJeu == 3) System.out.println("Vous etes tres intelligent !!!");
				}
				Reessayer();
				if (play == false) break;
			}
			
			Jouer();
		}
	}
	
	public static void reglagesJeu() {
		// Choisir la difficulte
		System.out.println("Quelle difficulte choisissez-vous?");
        System.out.println("Niveau facile: tapez 1");
        System.out.println("Niveau moyen: tapez 2");
        System.out.println("Niveau difficile: tapez 3");
        niveauJeu = sc.nextInt ();
        while (niveauJeu > 3 || niveauJeu < 1) {
        	System.out.println("1, 2 et 3? Quelle difficulte choisissez-vous?");
        	niveauJeu = sc.nextInt();
        }
        
        // Choisir la dimension
        System.out.println("De quelle taille voulez-vous votre tableau? (nombre de lignes = nombre de colonnes)");
        tailleTableau = sc.nextInt();
        while (tailleTableau <= 1) {
        	System.out.println("Il faut que la taille > 1. De quelle taille voulez-vous votre tableau?");
        	tailleTableau = sc.nextInt();
        }
        
        
	}
	
	public static void calculNombreBombesEtHelps() {
        if (niveauJeu == 1) {
            nombreBombes = (tailleTableau * tailleTableau)/10;
            help = nombreBombes/2;
        } else if (niveauJeu == 2) {
        	nombreBombes = (tailleTableau * tailleTableau)/5;
        	help = nombreBombes/3;
        } else if (niveauJeu == 3) {
        	nombreBombes = (tailleTableau * tailleTableau)/2;
        	help = nombreBombes/5;
        }
        if (help == 0) help = 1;
        if (nombreBombes == 0) nombreBombes = 1;
	}

	public static void selectionLigneColonne() {
		System.out.println("Selectionnez la case a ouvrir, tapez la ligne et la colonne.");
		System.out.println("Si vous voulez indiquer ou retirer la presence de bombes par des drapeaux, tapez p");
		System.out.println("Si vous avez besoin d'aide, tapez h (nombre d'aides restants: " + help + ")");
		try {
			c = ' ';
			x = sc.nextInt(); 
			y = sc.nextInt(); 
			x = x - 1; y = y - 1;
			while (x < 0 || y < 0 || x >= tableau.taille || y >= tableau.taille || tableau.cell[x][y].Voir) {
				if (x < 0 || y < 0 || x >= tableau.taille || y >= tableau.taille) 
					System.out.println("Cette case n'existe pas, choisissez-en une dans le tableau!");
				else if (tableau.cell[x][y].Voir) 
					System.out.println("Case deja selectionnee. Selectionnez une autre case non revelee: ");
				x = sc.nextInt(); 
				y = sc.nextInt(); 
				x = x - 1; y = y - 1;
			}
		} catch(Exception e) {
			c = sc.nextLine().charAt(0);
		}
		
	}
	
	public static void premiereSelection() {
		tableau.Afficher();
		selectionLigneColonne();
		while (c == 'p' || c == 'h' || c != ' ') {
			if (c == 'p') System.out.println("Vous devez ouvrir la premiere case avant d'utiliser les drapeaux. La premiere fois, il n'y aura pas de bombes");
			if (c == 'h') System.out.println("La premiere fois, il n'y aura pas de bombes. Vous n'avez pas besoin d'aide!");
			selectionLigneColonne();
		}
	}
	
	public static void Jouer() {
		boolean ok = true;
		while (ok) {
			tableau.Afficher();
			selectionLigneColonne();
		
			if (c == 'h') {
				if (help != 0) {
					Aider();
					ok = false;
				} else {
					System.out.println("Vous n'avez plus soulagement!!!");
					ok = true;
				}
			} else ok = false;
		} 	
		if (c == 'p') indiquerBombe();
		else {
			if (tableau.cell[x][y].Bombe) gameOver = true;
			else if (tableau.cell[x][y].NbBombe != 0) {
				tableau.cell[x][y].Voir = true;
				tableau.cell[x][y].drapeauBombe = false;
			}
			else tableau.Etaler(x, y);
		}
	}
	
    public static void indiquerBombe() {
		System.out.print("Choisissez la ligne et la colonne: ");
        int drapeauX = sc.nextInt();
        int drapeauY = sc.nextInt();
        drapeauX--; drapeauY--;
        while (drapeauX < 0 || drapeauY < 0 || drapeauX >= tableau.taille || drapeauY >= tableau.taille || tableau.cell[drapeauX][drapeauY].Voir) {
			if (tableau.cell[drapeauX][drapeauY].Voir) 
				System.out.println("Case deja selectionnee. Vous savez deja qu'il n'y a pas de bombes. Choisissez une autre case!");
            if (drapeauX < 0 || drapeauY < 0 || drapeauX >= tableau.taille || drapeauY >= tableau.taille) 
				System.out.println("Cette case n'existe pas, choisissez-en une dans le tableau!");
            drapeauX = sc.nextInt(); 
            drapeauY = sc.nextInt(); 
            drapeauX--; drapeauY--;
        }
        if (tableau.cell[drapeauX][drapeauY].drapeauBombe) {
			tableau.cell[drapeauX][drapeauY].drapeauBombe = false;
			tableau.nombreBombes++;
        } else {
			tableau.cell[drapeauX][drapeauY].drapeauBombe = true;
			tableau.nombreBombes--;
		}
		tableau.Afficher();
    }
	
	public static void Aider() {
		help = help - 1;
		do {
			x = (int)(Math.random()*tableau.taille);
        	y = (int)(Math.random()*tableau.taille);
		} while (tableau.cell[x][y].Bombe || tableau.cell[x][y].Voir);
	}
	
	public static void Reessayer() {
		System.out.println("Reessayer? (Y/N)");
		char s = sc.next().charAt(0);
		if (s == 'Y' || s == 'y') {
			gameOver = false;
			reglagesJeu();
            calculNombreBombesEtHelps();
			tableau = new Tableau(tailleTableau, nombreBombes);
			premiereSelection();
			tableau.Generer(x, y);
		} else play = false;
	}
	
	public static boolean Gagner() {
		for (int i = 0; i < tableau.taille; i++){
			for (int j = 0; j < tableau.taille; j++){
				if (tableau.cell[i][j].Bombe == false && tableau.cell[i][j].Voir == false) return false;
			}
		}
		return true;
	}
}
