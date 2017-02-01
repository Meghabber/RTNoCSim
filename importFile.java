/******************************************************************************/
/* PROJET          : NoC Simulator                                               */
/* SOCIETE         : Oran University                                                   */
/* TAG             : NoC_20151122                                 */
/* NOM DU FICHIER  : importFile.java                                              */

/* Laboratory : LAPECI                                                                        */
/* DESCRIPTION     :                                                          */
/* L'objet de cette classe est  d'instancier les Flits/paquets        */
/*  a partir du fichier scenario.xml      */
/*  Les Flit sont généré avec : Scr, Dest, Type     */
/*        */
/*        */
/* Intput: Scenario.xml       */
/* Output: ListPaquet       */
/******************************************************************************/
/* Date de creation : 01/06/15                                   */
/******************************************************************************/


package Router;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class importFile {

	int NocSize;
	int VcBufferSize;
	int NumberVCbuffer;
	ArrayList<Paquet> ListPaquet;

	public importFile(String FileName) {
		FileInputStream fstream;
		boolean RunSimulation = false;
		boolean scenarioBegin = false;
		boolean paquetBegin = false;
		ListPaquet = new ArrayList<Paquet>();

		try {

			fstream = new FileInputStream(FileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			String temp2[];
			while (((strLine = br.readLine()) != null)
					&& RunSimulation == false) {
				// System.out.println(strLine);
				String[] temp = strLine.split(" ");

				/***************************************************/
				if (temp.length > 0 && temp[0].equals("<Noc")) {
                temp2=temp[1].split("=");
					NocSize = Integer.parseInt(temp2[1]);
				}
				/***************************************************/
				if (temp.length > 0 && temp[0].equals("<Router")) {
                   temp2=temp[1].split("=");
					VcBufferSize = Integer.parseInt(temp2[1]);
					temp2=temp[2].split("=");
					NumberVCbuffer = Integer.parseInt(temp2[1]);
				}
				/***************************************************/
				if (temp.length > 0 && temp[0].equals("<scenario>"))
					scenarioBegin = true;
				// ------------------------------------------------//
				while (scenarioBegin) {
					strLine = br.readLine();
					temp = strLine.split(" ");

					int vc = 0;
					if (temp.length > 0 && temp[0].equals("<paquet")) {
						paquetBegin = true;
						Paquet p = new Paquet();
						// dans le paquet-----------------------------

						while (paquetBegin) {
							
							
							//******************************** traitement pour VC
							temp2 = temp[1].split("=");
							if (temp2.length > 0 && temp2[0].equals("Vc")) {
								p.addVc(Integer.parseInt(temp2[1]));
							}
							
							
							
							//******************************** traitement pour ArrivalCycle
							temp2 = temp[2].split("=");
							if (temp2.length > 0
									&& temp2[0].equals("ArrivalCycle")) {
								p.ArrivalCycle = Integer.parseInt(temp2[1]);
							}
							
							
							//********************************** traitement pour Priority
							temp2 = temp[3].split("=");
							if (temp2.length > 0
									&& temp2[0].equals("Priority")) {
								p.Priority = Integer.parseInt(temp2[1]);
							}
							
							// Lire la deuxieme ligne du paquet et construire les flit
							strLine = br.readLine();
							temp = strLine.split(" ");

							Flit flit = new Flit();
							if ((strLine) != null && temp.length > 0
									&& temp[0].equals("<Flit")) {
								if (temp[1].equals("type=1")) {
									temp2 = temp[1].split("=");
									flit.setType(Integer.parseInt(temp2[1]));
									flit.Priority=p.Priority;
									temp2 = temp[2].split("=");
									flit.setSrc(Integer.parseInt(temp2[1]));

									temp2 = temp[3].split("=");
									flit.setDest(Integer.parseInt(temp2[1]));

									temp2 = temp[4].split("=");
									flit.setNumberFlit(Integer.parseInt(temp2[1]));
                                    flit.ArivelTime=p.ArrivalCycle;//le flit chargé ariveletime de paquet  
								} else {
									temp2 = temp[1].split("=");
									flit.setType(Integer.parseInt(temp2[1]));
									flit.ArivelTime=p.ArrivalCycle;//le flit chargé ariveletime de paquet 
									flit.Priority=p.Priority;
								}
								p.addFlit(flit);

							}

							if (temp.length > 0
									&& (temp[0].equals("</paquet>"))) {
								paquetBegin = false;
								ListPaquet.add(p);
							}

						}
					}

					if (temp.length > 0 && temp[0].equals("</scenario>")) {
						scenarioBegin = false;
					}

				}

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	// Affichier les Flit
		/*for(int i=0;i<3;i++)
		{
			
			System.out.println("le Paquet 1 son flit "+i);
			System.out.println("********************** Priority=  "+ListPaquet.get(6).paquet.get(i).Priority);
		}*/
	
	
	}

	public int getNocSize() {
		return NocSize;
	}

	public int getVcBufferSize() {
		return VcBufferSize;
	}

	public int getNumberVCbuffer() {
		return NumberVCbuffer;
	}

}
