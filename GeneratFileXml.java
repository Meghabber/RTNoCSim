/******************************************************************************/
/* PROJET          : NoC Simulator                                               */
/* SOCIETE         : Oran University                                                   */
/* TAG             : NoC_20151122                                 */
/* NOM DU FICHIER  : GenerateFileXml.java                                              */

/* Laboratory : LAPECI                                                                        */
/* DESCRIPTION     :                                                          */
/* L'objet de cette classe est  générer un fichier scenarion. xml        */
/*  a  partir de du fichier csv (filecsv3)    */
/*  Cette classe fait appel à la classe ReadCSVFile.java   */
/*  Le ficheir filecsv3 contient les informations suivante  */
/*   pour chaque paquet: VC, ArrivalCycle, Destination, NbrFlit     */
/*        */
/*        */
/*   Le ficheir scenario.xml sera sous la forme de :     */
/*  <paquet Vc=0 ArrivalCycle=0 >  */
/*        <Flit type=1 src=0 dest=1 NbrFlit=2 />  */
/*        <Flit type=0  />  */
/*        <Flit type=0  />  */
/*   </paquet>      */
/*        */
/******************************************************************************/
/* Date de creation : 01/06/15                                   */
/******************************************************************************/

package Router;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GeneratFileXml {

	public static void main(String args[]) throws IOException
	{
		int NocSize=3;
		int vcbufferSize=5;
		int NumberOfbuffer=3;
		ReadCSVFile CsvFile=new ReadCSVFile("./filecsv3.csv");
		ArrayList<String []> listp=new ArrayList<>();
		String p[];
		for (int i = 1; i < CsvFile.secnario.size(); i++) {
			p=CsvFile.secnario.get(i);
			listp.add(p);
		}
		//le paquet contient (Vc ,Arivel time   , Src, Dest, NumbetFlit)
		
		try{
		// Create file 
			//<?xml version="1.0" encoding="UTF-8"?>
		FileWriter file = new FileWriter("scenario1.xml");
     PrintWriter out=new PrintWriter(file);
     char G='"';
		out.println("<?xml version="+G+"1.0"+G+" encoding="+G+"UTF-8"+G+"?>");
		out.println();
		out.println("<RunSimulation>");
		out.println("<Noc size="+NocSize+" >");
		out.println("<Router VcBufferSize="+vcbufferSize+" NumberOfVcBuffer="+NumberOfbuffer+" />");
		out.println("</Noc>");
		out.println();
		out.println("<scenario>");
		out.println();
		for(int i=0;i<listp.size();i++){
			
			out.println("<paquet Vc="+ listp.get(i)[0] +" ArrivalCycle="+listp.get(i)[1] +" Priority="+listp.get(i)[2]+" >");
			out.println("<Flit type=1 src="+listp.get(i)[3] +" dest="+listp.get(i)[4] +" NbrFlit="+listp.get(i)[5] +" />");
			for(int j=0;j<Integer.parseInt(listp.get(i)[5]) ;j++){
				out.println("<Flit type=0  />");
			}
			out.println("</paquet>");
			out.println();
		}
		
		out.println();
		out.println("</scenario>");
		out.println();
		out.println("</RunSimulation>");
		//Close the output stream
		out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}