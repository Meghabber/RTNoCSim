/******************************************************************************/
/* PROJET          : NoC Simulator                                               */
/* SOCIETE         : Oran University                                                   */
/* TAG             : NoC_20151122                                 */
/* NOM DU FICHIER  : injection.java                                              */

/* Laboratory : LAPECI                                                                        */
/* DESCRIPTION     :                                                          */
/* L'objet de cette classe est recuperer  les paramettre final  de la plateforme NoC        */
/*  aprés instatnciation de des Flit à partir du scenario.xml    */
/*     */
/*    */
/*        */
/*        */
/*        */

/*        */
/******************************************************************************/
/* Date de creation : 01/06/15                                   */
/******************************************************************************/


package Router;

import java.util.ArrayList;

public class Injection {
	public ArrayList<Paquet> listePaquet;

	public Injection() {
		importFile imfile = new importFile("./scenario1.xml");
		Final_parametre.Buffer_size = imfile.getVcBufferSize();
		
		Final_parametre.Noc_size = imfile.getNocSize();
		Final_parametre.Number_vcbuffer_per_Plink = imfile.getNumberVCbuffer();
		
		listePaquet =imfile.ListPaquet;

	}


}

