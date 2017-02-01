/******************************************************************************/
/* PROJET          : NoC Simulator                                               */
/* SOCIETE         : Oran University                                                   */
/* TAG             : NoC_20151122                                 */
/* NOM DU FICHIER  : paquet.java                                              */

/* Laboratory : LAPECI                                                                        */
/* DESCRIPTION     :                                                          */
/* Cette classe décrit la structure de paquet sous forme      */
/*   d'une liste  de Flit , le  Vc,  ArrivalCycle */
/*    */
/*   */
/*      */
/*        */
/*        */
/*        */
/******************************************************************************/
/* Date de creation : 01/06/15                                   */
/******************************************************************************/


package Router;

import java.util.ArrayList;

public class Paquet {
	public int vc;
	public ArrayList<Flit> paquet;
    public int ArrivalCycle;
    public int Priority;
	public int getArrivalCycle() {
		return ArrivalCycle;
	}

	public void setPriority(int Priority1) {
		Priority = Priority1;
	}
	public void setArrivalCycle(int arrivalCycle) {
		ArrivalCycle = arrivalCycle;
	}

	public Paquet(int vc) {
		// TODO Auto-generated constructor stub
      this.vc=vc;
     paquet=new ArrayList<>();
	}
	
	public Paquet() {
		// TODO Auto-generated constructor stub
     paquet=new ArrayList<>();
	}

	public void addVc(int vc) {
		this.vc = vc;
	}

	public int getVc() {
		return vc;
	}
public int size(){
	return paquet.size();
}

	public ArrayList<Flit> getPaquet() {
		return paquet;
	}

	public void addFlit(Flit flit) {
		this.paquet.add(flit);
	}
  
	 public Flit getFlit(int id){
	return paquet.get(id);
	}
}
