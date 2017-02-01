/* PROJET          : NoC Simulator                                               */
/* SOCIETE         : Oran University                                                   */
/* TAG             : NoC_20151122                                 */
/* NOM DU FICHIER  : ReadCSVFile.java                                              */

/* Laboratory : LAPECI                                                                        */
/* DESCRIPTION     :                                                          */
/* L'objet de cette classe esr de lire le fichier CSV et le charger dans un        */
/*  ArrayList. Cette classe est appelé par par GenerateFileXml.java        */


/******************************************************************************/
/* Date de creation : 01/06/15                                   */
/******************************************************************************/
package Router;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadCSVFile {

	ArrayList<String[]> secnario;

	public ReadCSVFile(String name) throws IOException {

		try {
			String chemin = name;
			BufferedReader fichier_source = new BufferedReader(new FileReader(
					chemin));
			String chaine;
int i=0;
			secnario = new ArrayList<>();

			while ((chaine = fichier_source.readLine()) != null) {

				String[] tabChaine = chaine.split(";");

				secnario.add(tabChaine);
				 for(int j=0;j<tabChaine.length;j++)
				 System.out.print(secnario.get(i)[j]);
				 System.out.println();
i++;
			}
			fichier_source.close();
		} catch (FileNotFoundException e) {
			System.out.println("Le fichier est introuvable !");
		}
	}
	
	


}
