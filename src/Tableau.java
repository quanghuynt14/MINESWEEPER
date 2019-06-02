public class Tableau {
	Cellule [][] cell;
	int taille;
	int nombreBombes;
	
	public Tableau(int taille, int nombreBombes) {
		this.taille = taille;
		this.cell = new Cellule [taille][taille];
		this.nombreBombes = nombreBombes;
		
		for (int i = 0; i < this.taille; i++){
            for (int j = 0; j < this.taille; j++)
				this.cell[i][j] = new Cellule(false, 0, false, false);
				
		}
	}
	
	public void Generer(int x, int y) {
		
        //Creer Bombe  
        for (int i = 0; i < this.nombreBombes; i++) {
        	int A = 0; int B = 0; boolean ok = true;
        	do {
				ok = true;
        		A = (int)(Math.random()*this.taille);
        		B = (int)(Math.random()*this.taille);
        		//System.out.println(A + " " + B);
        		if (this.taille > 4) {
					for (int k = x-1; k <= x+1; k++)
						for (int h = y-1; h <= y+1; h++) 
							if ((k != x || h != y) && k >= 0 && k < this.taille && h >= 0 && h < this.taille) {
								if (A == k && B == h) ok = false;	
							}
				}
        	} while (this.cell[A][B].Bombe || ok == false || (A == x && B == y));
            this.cell[A][B].Bombe = true;
        }
        
        //Creer NbBombe
        for (int i = 0; i < this.taille; i++) 
			for (int j = 0; j < this.taille; j++) {
				
				for (int k = i-1; k <= i+1; k++)
					for (int h = j-1; h <= j+1; h++) 
						if ((k != i || h != j) && k >= 0 && k < this.taille && h >= 0 && h < this.taille && this.cell[k][h].Bombe == true) {
							this.cell[i][j].NbBombe++;	
						}
			}
		
        // Jouer avec la premiere selection
		if (this.cell[x][y].NbBombe != 0) this.cell[x][y].Voir = true;
		else Etaler(x, y);
	
	}
	
	public void Etaler(int i, int j) {
		this.cell[i][j].Voir = true;
		this.cell[i][j].drapeauBombe = false;
        for (int k = i-1; k <= i+1; k++)
            for (int h = j-1; h <= j+1; h++) 
                if ((k != i || h != j) && k >= 0 && k < this.taille && h >= 0 && h < this.taille && this.cell[k][h].Voir == false) {
                    if (this.cell[k][h].NbBombe != 0) {
						this.cell[k][h].Voir = true;
						this.cell[k][h].drapeauBombe = false;
					}
                    else Etaler(k, h);
                }
	}
	
	public void Afficher() {
		effaceEcran();
		System.out.println("DEMINEUR! Nombre de bombes restantes: " + this.nombreBombes);
		System.out.println("");
		
		// ordre des colonnes
		System.out.print("   |");
        for (int i = 0; i < this.taille; i++) 
			if ((i + 1) < 10) System.out.print(" " + (i + 1) + " |");
			else System.out.print(" " + (i + 1) + "|");
		System.out.println("");
		
        // bordure
        System.out.print("-- +");
        for (int i = 0; i < this.taille*4; i++) 
			if ((i + 1) % 4 == 0) System.out.print("+");
			else System.out.print("-");
        System.out.println("");
        
        // bombe
        for (int i = 0; i < this.taille; i++) {
			if (i + 1 < 10) System.out.print(" ");
            System.out.print((i + 1) + " |");
            for (int j = 0; j < this.taille; j++) {
                if (this.cell[i][j].Voir) {
                    if (this.cell[i][j].Bombe) System.out.print(" X ");
                    else System.out.print(" " + this.cell[i][j].NbBombe + " ");
                } 
                else if (this.cell[i][j].drapeauBombe) System.out.print("///");
                else System.out.print(" . ");
                System.out.print("|");
            }
            System.out.println("");
            // bordure
			System.out.print("-- +");
			for (int j = 0; j < this.taille*4; j++) 
				if ((j + 1) % 4 == 0) System.out.print("+");
				else System.out.print("-");
			System.out.println("");
        }
        
        System.out.println();
    }
    
    // Efface ecran pour Windowns
    public void effaceEcran() {
        try {	
			new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		}catch(Exception E) {
			System.out.println(E);
		}
    }
    
	//Efface ecran pour Linux
	/*
	public void effaceEcran() {  
		System.out.print("\033[H\033[2J");  
		System.out.flush();  
	} 
	*/
}
