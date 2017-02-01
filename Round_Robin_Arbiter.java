// ajouter par amine
// mettre a jour pour le RT
package Router;

import java.util.ArrayList;

public class Round_Robin_Arbiter implements Arbiter{

 public  int Laste_Buffer_served;

 
//pour arbitrer et supprimer le premier de le list Port_List_for_Arbiter
 public int Req_Arbitration(ArrayList<Integer> List_port) {
	  if(!List_port.isEmpty()){
		  
		
	int LS=List_port.get(0);
	
	//List_port.clear();
	
	return LS ;}
	  else {System.out.println("Liste port est vide");
		
	}
	  return -1;
	  
}
 
 
 /*****************************changéé***************************************/
 public int Selector(ArrayList<Integer> List_port) {//pour selection 
	 //cettte  méthode sert a pré-selectionner un candidat de List_Port_for_Arbiter
	  if(!List_port.isEmpty()){
		
	int LS=List_port.get(0);
	
	return LS ;
	
	  }
	  else {System.out.println("Liste port est vide");
		
	}
	  return -1;
	  
}
 /********************************************************************/
 public int Req_Arbitration(ArrayList<Integer> List_port,int first_data) {
	 
	 //pour arbitré et supprimer qui est le id (first_data) de le list
	 
	  if(!List_port.isEmpty()){
		  System.out.println("in arbitre 2 == "+first_data);
	int LS=List_port.get(first_data);
	System.out.println("le port de sortier : "+LS);
	List_port.remove(first_data);
	System.out.println("size is : "+List_port.size());
	return LS ;}
	  else {System.out.println("Liste port est vide");
		
	}
	  return -1;
	  
 }

}