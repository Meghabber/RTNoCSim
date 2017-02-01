// Cette classe est modifié par Amine : adaptation RT
package Router;

import java.util.ArrayList;




/**
 * @author MEGHABBER
 *
 */
public class Manager {

	public int Adresse;/* adresse de Manager */

	public int Switching_info[][];// une matrice contient l'information sur
									// l'etat de manager

	public int List_candidate[];// list des buffer candidate

	public ArrayList<ArrayList<Integer>> Port_List_for_arbiter;// chaque port et
																// les buffers
																// qui sort avec
																// lui

	public int neighbor_Index_List[];// adresse de routeur correspondant à
										// chaque
										// port

	public int outVC[];// le vc du Manager adjacent qui est  selectionné temporairement  pour un probable move du flit candidat.
	public int vcbuffer[];// le VC du Manager en cours qui est  selectionné temporairement  pour transferer son  flit candidat.
	public ArrayList<VcBuffer> List_vcBuffer;
	public RoutingXY route;
	Round_Robin_Arbiter arbiter;
  
	public int Number_Slots_per_vcBuffer;// nombre de slot dans VCbuffer

	public int Number_vcBuffer;// nbr port *nbrvc per link final

	private int nbr_port;// nombre de port dans le routure
	public ArrayList< ArrayList<Flit> > NI;

	/*************************************************************************************/
	public Manager(int adress) {
	
		this.Adresse = adress;

		this.Number_Slots_per_vcBuffer = Final_parametre.Buffer_size;

		this.neighbor_Index_List = new int[Final_parametre.Number_of_physiquelLink];// 5 ports  L0 W1 S2 E3 N4

		
		this.outVC = new int[5];// 5 ports  L0 W1 S2 E3 N4
		
		this.vcbuffer = new int[5];// 5 ports  L0 W1 S2 E3 N4
		for(int i=0;i<5;i++) 
		{
			this.outVC[i] = -1;
			this.vcbuffer[i] = -1;
		}
			
		Set_neighbor_Index_List(adress);// remplir la liste

		for (int i = 0; i < neighbor_Index_List.length; i++) // calculer le nbr de port pour ce routeur
			if (neighbor_Index_List[i] != -1)
				this.nbr_port++;

		//System.out.println("====" + adress + "=== Nombre de port  : "
				//+ nbr_port);
		//System.out.print(" neighbor_Index_List ");
		
		/*for (int i = 0; i < neighbor_Index_List.length; i++) {
			System.out.print("|" + neighbor_Index_List[i] + "|");
		}
		System.out.println();*/

		this.Number_vcBuffer = nbr_port	* Final_parametre.Number_vcbuffer_per_Plink;// recupérer le nbr de vcbuffer
		//System.out.println("Number_vcBuffer : " + Number_vcBuffer);

		this.Switching_info = new int[Number_vcBuffer][6];
															// colonne 0 : Local_Credit_VC
															// colonne 1 : VC_locked_Status
															// colonne 2 : output / port
															// colonne 3 :  output_VC_id
															// colonne 4 :crédit_output_VC)
															// colonne 5 : Priority

		this.Generat_vcbufer();
		
		for (int i = 0; i < Number_vcBuffer; i++) {// initialiser la matrice de
			// switching_info
			this.Switching_info[i][0] = Final_parametre.Buffer_size-List_vcBuffer.get(i).Data.size(); //Local_Credit_VC
			this.Switching_info[i][1] = -1;		//  VC_locked_Status
			this.Switching_info[i][2] = -1;		//  output / port
			this.Switching_info[i][3] = -1;		// output_VC_id
			this.Switching_info[i][4] = -1;		// crédit_output_VC)
			this.Switching_info[i][5] = i%Final_parametre.Number_vcbuffer_per_Plink;		//Priority

		}

		List_candidate = new int[Number_vcBuffer];

		this.Port_List_for_arbiter = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < Final_parametre.Number_of_physiquelLink; i++)
			this.Port_List_for_arbiter.add(new ArrayList<Integer>());

		this.route = new RoutingXY();
		this.arbiter = new Round_Robin_Arbiter();
		
	this.NI=new ArrayList<ArrayList<Flit>>();
		
		for(int p=0;p<Number_vcBuffer;p++)
		this.NI.add(new ArrayList<Flit>());
		
		//System.out.println("ni size : "+NI.size());
		
		//System.out.println();
		//System.out.println("********************************************** \n");

	}

	public void Set_neighbor_Index_List(int adress) {

		this.neighbor_Index_List[0] = adress;// port local 0
		// A ne pas prendre en considération le local !!!!

		if (adress % Final_parametre.Noc_size == 0)
			this.neighbor_Index_List[1] = -1;// port W 1
		else
			this.neighbor_Index_List[1] = adress - 1;

		if (adress / Final_parametre.Noc_size == Final_parametre.Noc_size - 1)
			this.neighbor_Index_List[2] = -1; // port S 2
		else
			this.neighbor_Index_List[2] = adress + Final_parametre.Noc_size;

		if (adress % Final_parametre.Noc_size == Final_parametre.Noc_size - 1)
			this.neighbor_Index_List[3] = -1;// port E 3
		else
			this.neighbor_Index_List[3] = adress + 1;

		if (adress / Final_parametre.Noc_size == 0)
			this.neighbor_Index_List[4] = -1;// port N 4
		else
			this.neighbor_Index_List[4] = adress - Final_parametre.Noc_size;
		
		

	}

	public void Generat_vcbufer() {// initialiser la List de vcbuffer

		this.List_vcBuffer = new ArrayList<>();
int m=-1;
		for (int i = 0; i < neighbor_Index_List.length; i++) {

			if (neighbor_Index_List[i] != -1) {
				// i*nbr_vcbuffer ; (i+1)*nbvcperplik
				// for(int
				// j=i;j<(final_parametre.Number_vcbuffer_per_Plink)+i;j++){
				for (int j = 0; j < Final_parametre.Number_vcbuffer_per_Plink; j++) {
					m++;
				//	System.out
				//			.print("| "
				//					+ "["
				//					+ i
				//					+ ","
				////					+ (m)
				//					+ "]");
/**********************************************???????			*******/
					this.List_vcBuffer.add(new VcBuffer(i, m));// j l'indice i
																// le port

				}
			}

		}

	//	System.out.println("List_vcBuffer.size() --->[" + List_vcBuffer.size()
			//	+ " ]");
	}

	
	
	public void add_flit_condidate(int vcid) {

		if (List_vcBuffer.get(vcid).Check_Flit_Condiate())// test le buffer est
															// vide ou nn
			List_candidate[vcid] = 1;

		else
			List_candidate[vcid] = 0;

	}

	public boolean is_Header(Flit flit) {
		if (flit.get_Type() == 1)
			return true;
		else
			return false;
	}

	public int Route_info(Flit flit) {// pour remplire Switching_info[][4]
										// créditvc dest

		return this.route.Request_routing(flit.dest,Adresse);
	}

	 
	
	int VcServedWest=0;
	int VcServedSud=0;
	int VcServedEst=0;
	int VCservedNord=0;
	int Vcservedlocal=0;
	
	public void Tri_table_port() {
		
		// cette methose sert a creer les differents list_port
		// pour chaque port, on commence par le port reccement servi en faisant le tour des buffer existant
		// le principe de Round Robin en utilisant le modulo à partir du dernier buffer servi (VCservedlocal,...)
		int c=0;
for(int i=0;i<Number_vcBuffer;i++)
if(List_candidate[i]==1) c++;
// ajouter amine
//System.out.println("nombre de candidate pour ce manager= "+c);



// ci c<>0 veut dire que au moins un candidat existe

 if(c>0){
	//------------------------------------------------------------------------------------------
	 // construire 	 Port_List_for_arbiter  pour port Local
		int temp0 =Vcservedlocal;
		for(int i=Vcservedlocal ;i<Number_vcBuffer+Vcservedlocal;i++){
		// si    output_Port, pour ce buffer= Local  et Port_list_for_arbiter pour ce port l
		if (Switching_info[i%Number_vcBuffer][2] ==0 & Port_List_for_arbiter.get(0).size()==0){
			temp0=(i%Number_vcBuffer)+1; 
			}
		
			if(Switching_info[i%Number_vcBuffer][2] ==0 ){
				if(List_vcBuffer.get(i%Number_vcBuffer).Data.size()>0)
				Port_List_for_arbiter.get(0).add(i%Number_vcBuffer);
				}
			
		}
		
	Vcservedlocal=temp0;
	
	 
	//------------------------------------------------------------------------------------------
	// construire 	 Port_List_for_arbiter  pour port West
	 				int temp1 =VcServedWest;
				for(int i=VcServedWest ;i<Number_vcBuffer+VcServedWest;i++){
					
					if (Switching_info[i%Number_vcBuffer][2] ==1&Port_List_for_arbiter.get(1).size()==0){
						temp1=(i%Number_vcBuffer)+1; 
					}
					if(Switching_info[i%Number_vcBuffer][2] ==1 ){
						if(List_vcBuffer.get(i%Number_vcBuffer).Data.size()>0)
						Port_List_for_arbiter.get(1).add(i%Number_vcBuffer);
						}
					
				}
				VcServedWest=temp1;
				
				
				
	//------------------------------------------------------------------------------------------
	// construire 	 Port_List_for_arbiter  pour port Sud
				int temp2=VcServedSud;
			for(int i=VcServedSud;i<(Number_vcBuffer+VcServedSud);i++){
				if(Switching_info[i%Number_vcBuffer][2]==2&Port_List_for_arbiter.get(2).size()==0){
					temp2=(i%Number_vcBuffer)+1;
					 
				}
			
				if(Switching_info[i%Number_vcBuffer][2]==2 ){
					if(List_vcBuffer.get(i%Number_vcBuffer).Data.size()>0)
					Port_List_for_arbiter.get(2).add(i%Number_vcBuffer);
					}
						}
			VcServedSud=temp2;
			
			
  //------------------------------------------------------------------------------------------			
// construire 	 Port_List_for_arbiter  pour port East
			int temp3=VcServedEst;
			 for(int i=VcServedEst;i< Number_vcBuffer+VcServedEst;i++){
				// System.out.println((Number_vcBuffer+VcServedEst)+"VcServedEst : "+VcServedEst+" i: "+(i%Number_vcBuffer));
				 if(Switching_info[i%Number_vcBuffer][2] == 3 & Port_List_for_arbiter.get(3).size()==0 ){
				 temp3=(i%Number_vcBuffer)+1;
				 }
				
				 if(Switching_info[i%Number_vcBuffer][2]==3){
					 if(List_vcBuffer.get(i%Number_vcBuffer).Data.size()>0)
				 Port_List_for_arbiter.get(3).add(i%Number_vcBuffer);}  
				 
			 }
			VcServedEst=temp3;
			
			
	//------------------------------------------------------------------------------------------
	// construire 	 Port_List_for_arbiter  pour port Nord
			int temp4=VCservedNord;
		for(int i=VCservedNord;i<(Number_vcBuffer+VCservedNord);i++){
		if(Switching_info[i%Number_vcBuffer][2]==4&Port_List_for_arbiter.get(4).size()==0){
		temp4=(i%Number_vcBuffer)+1;
		}
		if(Switching_info[i%Number_vcBuffer][2]==4 ){
			if(List_vcBuffer.get(i%Number_vcBuffer).Data.size()>0)	
			Port_List_for_arbiter.get(4).add(i%Number_vcBuffer);
			}
	}
		VCservedNord=temp4;
 }
 
 // ajouter amine affichege de Port_list_for_arbiter
 for (int B = 0; B < Port_List_for_arbiter.size();B++)
	{
	 for (int V = 0; V < Port_List_for_arbiter.get(B).size();V++)
	 {
		// System.out.println("Port_List_for_arbiter= "+B+"  data="+V+"  est de   "+Port_List_for_arbiter.get(B).get(V)); 
	 }
				
		
	}
 
	}

	public int Update_Run_Flit(int portlink) {// portlink l'index de port 0L 1W 2S 3E 4E=N
		// et return le l'indice de input_buffer  qui voudrait envoyer son flit 
		//et supprimer l indice de buffer  dans la liste port for arbiter 

		return this.arbiter.Req_Arbitration(this.Port_List_for_arbiter
				.get(portlink));

	}
	public int Selector_Flit(int portlink) {//en entrer  portlink l'index de port 0L 1W 2S 3E 4E=N
		// et return  le l'ndice de flite qui voudrait quitter
		//mais  garder le flit dans la list port for arbiter 

		return this.arbiter.Selector(this.Port_List_for_arbiter
				.get(portlink));

	}

	public int Update_Run_Flit(int portlink,int first_data) {// portlink l'index de port 0L 1W 2S 3E 4E=N
		// et retourné le l'ndice de flite qui voudrait quitter
		//avec argument first_data si elle est des slot libre dans le routure distination

		return this.arbiter.Req_Arbitration(this.Port_List_for_arbiter
				.get(portlink));

	}

	public int port_corespondance(int port) {
		if (port == 1)
			return 3;
		if (port == 2)
			return 4;
		if (port == 3)
			return 1;
		if (port == 4)
			return 2;
		return 0;
	}

	public int ckeck_Output_VC_Availabel(int port) {
		/**
		 * si flit est header retourné le position de VCbuffer correspondant
		 * dans la list_vcbuffer de next router (si le vcbuffer n'est pas
		 * disponible rotourner -1)
		 **/
/***********************************************/
		int Vcid = -1;
 
		int buffer_index=0;//le pointure dans 
		for(int k=0;k<port ;k++)
		{
			if (this.neighbor_Index_List[k]!=-1){
				buffer_index=buffer_index+Final_parametre.Number_vcbuffer_per_Plink;
				
			}
		}
		//cherche le pointure dans la liste de vc buffer 
		
		for (int i = buffer_index ; i < buffer_index+Final_parametre.Number_vcbuffer_per_Plink; i++)
			
				if (Switching_info[i][1] == -1)
					return i;

			
		return Vcid;

	}
	
	/**
	 * cette  methode sert a calculer me Router adjacent a un buffer
	 * en savant le buffer , on calculera le port correspondant
	 * et puis le routeur adjacent
	 **/
	public int Router_Correspondant_VCID(int VCID)
	{
		
		int Port=-1;
		int index_Port=-1;
		if (VCID < Final_parametre.Number_vcbuffer_per_Plink )
		{
			return -1;
		}
			
		for(int i=0;i<5;i++)// tous les ports
		{
			if (this.neighbor_Index_List[i]!=-1)
			{
				index_Port=index_Port+3;
				if(VCID<=index_Port)
				{
					
					return neighbor_Index_List[i];// le routeur correspeondant a ce buffer
				}
				
			}
			
			
		}
		return -1;
		
	}

	public int get_out_VC_Id(int port) {// retourné le buffer disponible
												// dans le next roter
		//if(port==0) return -11;
		return ckeck_Output_VC_Availabel(port_corespondance(port)) ;

	}

	public int get_info_credit_local(int vcid) {// retourner le crédit local 0
		return this.Switching_info[vcid][0];

	}

	public int garented_VC_credit(int vcid) {// retourner le crédit local 0
		 
			return	get_info_credit_local(vcid);

	}

	/**************************************/
	public Flit Get_Flit(int vcbuffer) {
		
		if(this.List_vcBuffer.get(vcbuffer).Data.size()>0)
		return this.List_vcBuffer.get(vcbuffer).Data.get(0);
		else {
			System.out.println("liste buffer est vide ");
			return null;
		}
	}

	// rout info c'est le buffer
	public void Move_Flit_To_next_Router(int Route_info, Flit flit) { 
		
 
			List_vcBuffer.get(Route_info).Data.add(flit);		
 
	
				//Update_Credit_Local(Route_info);
	}

	/******************************************/



	public void Update_Credit_Local(int vcid) {// en cas de reception d un nouveau flit
		this.Switching_info[vcid][0]--;
	}
	public void Update_Credit(int vcid) {// en cas d ecraser un flit de ce buffer
		this.Switching_info[vcid][0]++;
		
		// ajouter le traitement pour le routeur adjacent de ce portlink
		
		
		
		
	}

	public void Update_Status(int vcid) {// initialisation de buffer
	
			System.out.println("#################### update status "+vcid+"################");
			this.Switching_info[vcid][0] = Number_Slots_per_vcBuffer;
			this.Switching_info[vcid][1] = -1;
			this.Switching_info[vcid][2] = -1;
			this.Switching_info[vcid][3] = -1;
			this.Switching_info[vcid][4] = -1;
		

	}

	public void Lock_VC(int vcid,int number_Flit_atached) {// reserver le buffer (1réserver ,-1 libre)
		this.Switching_info[vcid][1] = 1;
		List_vcBuffer.get(vcid).Flit_counter=number_Flit_atached+1;
	}

	public void view_Switching_information() {
		for (int i = 0; i < Number_vcBuffer; i++) {
			//System.out.print("  "+i+" : ");
			for (int j = 0; j < 6; j++) { // 5 information dans la matrice
				
				if (j == 0)
					System.out.print("|la matrice switch_info      " + Switching_info[i][j]);

				if (j == 1)
					if (Switching_info[i][j] == 1)
						System.out.print("|True");
					else
						System.out.print("|False");

				if (j == 2) {
					if (Switching_info[i][j] == 0)
						System.out.print("| L");
					if (Switching_info[i][j] == 1)
						System.out.print("| W");
					if (Switching_info[i][j] == 2)
						System.out.print("| S");
					if (Switching_info[i][j] == 3)
						System.out.print("| E");
					if (Switching_info[i][j] == 4)
						System.out.print("| N");
					if(Switching_info[i][j] ==-1)System.out.print("|" + Switching_info[i][j]);

				}
				if (j == 3)
					System.out.print("|" + Switching_info[i][j]);
				if (j == 4)
					System.out.print("|" + Switching_info[i][j]);
				if (j == 5)
					System.out.print("|" + Switching_info[i][j]);
			}
			System.out.println();
		}

	}

	public int First_Data(int port) {
		int vcid = -1;
		for (int i = 0; i < Port_List_for_arbiter.get(port).size(); i++) {
			vcid = Port_List_for_arbiter.get(port).get(i);
			if (!is_Header(List_vcBuffer.get(vcid).getFlit())) {
				return i;
			}
		}

		return -2;
	}
	/**retourner position de dans la list ( Port_List_for_arbiter.get(port)) 
	 * 
	 de  premier Flit data qui a des slot libre dans le vcbuffer destination **/
	public int Next_Free_Data(int port) {//en entrée le port  et le pointeur dans la liste d'attente dans le port 
		int vcid = -1;
		//###########
		for (int i = 0; i < Port_List_for_arbiter.get(port).size(); i++) {
			
			vcid = Port_List_for_arbiter.get(port).get(0);
			// recommendadtion de Amine
			// vcid = Port_List_for_arbiter.get(port).get(i);
			//System.out.println("DATAaaaaaaaaaaaaaaaaaaaaaaa vcid :"+vcid);
			//System.out.println("i :"+i+" vcid :"+vcid+"  is header :"+is_Header(List_vcBuffer.get(vcid).getFlit()));
			
			if ((!is_Header(List_vcBuffer.get(vcid).getFlit()))& Switching_info[vcid][4]>0) {//testé si le flit est n'est pas hesder 
				System.out.println("DATAaaaaaaaaaaaaaaaaaaaaaaa vcid :"+port);																//en pluse le vcbuffer deestination contient des slot libre
				return Switching_info[vcid][3];//return le VCout
			}
		}

		return -1;
	}
	
	
	public void Selction_condidate(){ //remplire la liste condidate et faire le routage 
		
		for(int i=0;i<Number_vcBuffer;i++){
			add_flit_condidate(i);
			//affichage pour la list condidate
			//System.out.print("" + List_candidate[i]+ "|");
			// si le flit en cours est de type Header, calculer le routage pour ce flit en question
			if(List_candidate[i]==1){
				if(is_Header(List_vcBuffer.get(i).getFlit()))
					Switching_info[i][2]= Route_info(List_vcBuffer.get(i).getFlit());//lencer le routage juste pour flit header
				
			}
			
			
			
		}
	}
}
