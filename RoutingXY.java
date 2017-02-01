package Router;

public class RoutingXY implements Routing{
	public static int[] to_XY_Adresse(int adr) {
		// convertiion de la position de routeur (codé iniitialement en un
		// entier ) en coordonnées XY
		int[] temp = new int[2];
		int nbr_colon = 3;
		int nbr_line = 3;

		temp[0] = adr / nbr_colon;
		temp[1] = adr % nbr_line;

		return temp;

	}

		public int Request_routing(int destination ,int AdresseManager) {
			//public int Request_routing(int destination ,Manager Manager) {
//pour le routage en entrée  : flit et l'adress de manager 
//par ce que l'adresse de manager c'est la nouvelle adress de flit header 
			
			int x = to_XY_Adresse(AdresseManager)[0];
			int y = to_XY_Adresse(AdresseManager)[1];

		int dest = destination;
		int x1 = to_XY_Adresse(dest)[0];
		int y1 = to_XY_Adresse(dest)[1];
		
		
		if (y > y1)
			return 1;// W
		if (y < y1)
			return 3;// E
		if (y == y1 & x > x1)
			return 4;// N
		if (y == y1 & x < x1)
			return 2;// S
		if (x == x1 & y == y1)
			return 0;// L
		return -1;
	}

}
