package Router;

import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Run_Similation {
	public int Cycle;
	int Noc_size;
	public static int Simulation_Life=20;
	public static int  Schedule=0; //0:RoundRobin   1:Preemptive RT   2= Non Preemptive RT
	ArrayList<Manager> List_Manager;
	

	/**
	 * 
	 */
	/**
	 * 
	 */
	public Run_Similation() {
	    Injection in=new Injection();
		this.List_Manager = new ArrayList<>();
		Noc_size = Final_parametre.Noc_size;

	/**************************** initialisation de la plateforme NoC ***************************/
		for (int i = 0 ; i < Noc_size * Noc_size ; i++ )
		List_Manager.add(new Manager(i));
		
		System.out.println("Nombre de Manager" + List_Manager.size());
		
	/**************************** initialisation  de l application***************************/	
		ArrayList<Paquet> listP;
		listP=in.listePaquet;	
		
		

		
		System.out.println("size buffer (id : 3) est  :"+List_Manager.get(0).List_vcBuffer.get(2).Data.size());
		
/********************************** PREPARATION DU FICHIER XML RESULTANT***********************************/
		
		Simulation_Results_To_XML_File  XML_File= new Simulation_Results_To_XML_File();
		
		XML_File.Generate_XML_NoC_Generation();
	    XML_File.Genarate_XML_NoC_Configuration();
		XML_File.Genarate_XML_NoC_Execution_Time();
		
		
		
		/********************************** DEROULRMRNT DE LA SIMULATION A PAS FIXE***********************************/
		XML_File.Genarate_XML_Simulation ();
		//this.Simulation_Life=10;
		
		
		
		
		
		
		
		for(int cycle =0;cycle<Simulation_Life;cycle++){
		
			System.out.println("-------------------------------------------------------------------------- Cycle :"+cycle+" -------------------------------------------------------------------------------");
			
			
			
			//preparer une nouvelle balise dans le ficheir XML pour ce cycle en cours
			Element XML_Cycle= new Element("Cycle");
			XML_Cycle=XML_File.Genarate_XML_Cycle(cycle);
			
			// 	prend les paqeut et les injecter das les buffer locaux des routeur
			// ToRouter: pour detecter les nouveau paquets venues
						ToRouter(listP, List_Manager, cycle);
						
			/************************************** selection de flit pour ce Manager********************************************/
		for (int i = 0; i < List_Manager.size(); i++) {
			//preparer une nouvelle balise dans le ficheir XML pour ce routeur en cours
			      Element XML_Router= new Element("Router");
			      XML_Router=XML_File.Genarate_XML_Router(XML_Cycle, i);
		     // Selection des candidats
			      List_Manager.get(i).Selction_condidate();
			// le tri de chaque port de ce Manager de telle façon 
			// on mets à la tete de la file, le buffer qui prends la main (principe RoundRobin)
			      List_Manager.get(i).Tri_table_port();
			      /*for (int a=0;a<5;a++)
				  {
					  System.out.println("Le portList du Port "+a);
					
					
						for(int j=0;j<List_Manager.get(i).Port_List_for_arbiter.get(a).size();j++)
							System.out.println("( "+List_Manager.get(i).Port_List_for_arbiter.get(a).get(j)+")");
							
							
					  
				  }*/
			for (int a = 0; a < 5; a++) {/**début de la selection de flit **/
				List_Manager.get(i).outVC[a] = -1; //le vcbuffer destination
			
				List_Manager.get(i).vcbuffer[a] = -1;// le vcbuffer source
     			if (List_Manager.get(i).Port_List_for_arbiter.get(a).size() > 0) {
			// selectionner le input_buffer qui va envoyer son flit en output
				List_Manager.get(i).vcbuffer[a]=List_Manager.get(i).Selector_Flit(a); // il prend Port_List.get(0) sans clear de Port_List
			
				//if(List_Manager.get(i).List_vcBuffer.get(List_Manager.get(i).vcbuffer[a]).Data.size()>0)//si le buffer selected n est pas vide
			  if( List_Manager.get(i).List_vcBuffer.get(List_Manager.get(i).vcbuffer[a]).Flit_counter > 0)
				{
				  //System.out.println("Print vcbuffer du port  "+a+"  est  "+List_Manager.get(i).Port_List_for_arbiter.get(2).size());
				     if (List_Manager.get(i).is_Header(//si le flit est header 
						   List_Manager.get(i).Get_Flit(List_Manager.get(i).vcbuffer[a]))) {
						// Lancer l arbitration pour ce output_port(a) en traitant son Port_List_For_Arbiter
							List_Manager.get(i).vcbuffer[a]=List_Manager.get(i).Update_Run_Flit(a);// il prend Port_List.get(0) avc  clear de Port_List
						//appel check_out_put_available(port_convertion (port)) 
							List_Manager.get(i).outVC[a] = List_Manager.get( List_Manager.get(i).neighbor_Index_List[a]).get_out_VC_Id(a);
						//System.out.println("vcbuffer in next router for Header : "+ List_Manager.get(i).outVC[a]);
							
						//si il existe buffer libre dans next router
							if (List_Manager.get(i).outVC[a] >= 0 & a!=0) {
								List_Manager.get(i).Switching_info[List_Manager.get(i).vcbuffer[a]][3] = List_Manager.get(i).outVC[a]; //save id de out VC
								
								
								List_Manager.get(i).Switching_info[List_Manager.get(i).vcbuffer[a]][4] = List_Manager
										.get(List_Manager.get(i).neighbor_Index_List[a])
										.garented_VC_credit(List_Manager.get(i).outVC[a]);                      //save crédit de out vcbuffer
								
								// amine: question pourquoi ce traitement. ne sert a rien
								List_Manager
								.get(List_Manager.get(i).neighbor_Index_List[a]).Lock_VC(List_Manager.get(i).outVC[a],List_Manager.get(i).List_vcBuffer.get(List_Manager.get(i).vcbuffer[a]).Data.get(0).get_nbr_FLit_attached());//loked vcbuffer destination
								// amine: question pourquoi ce traitement. ne sert a rien	
							//	List_Manager.get(i).Switching_info[List_Manager.get(i).vcbuffer[a]][0]=
							//			Final_parametre.Buffer_size-List_Manager.get(i).List_vcBuffer.get(List_Manager.get(i).vcbuffer[a]).Data.size();
								// amine: question pourquoi ce traitement. ne sert a rien
								List_Manager.get(i).Get_Flit(List_Manager.get(i).vcbuffer[a]).latency++;
								
							} 
							   else {// dans le cas: il n'existe pas de VC libre dans le next router--> outVC=-1
								  // System.out.println("PAS DE VCout POUR CE HEADER FLIT   "+ cycle );
								    List_Manager.get(i).outVC[a]=List_Manager.get(i).Next_Free_Data(a);
								     
								  	 List_Manager.get(i).vcbuffer[a]=List_Manager.get(i).Update_Run_Flit(a);
								   
								  }
							
							}
				              //si le Flit est DATA
				 else  {
						/**si l'en tête de liste contient Flit data **/
					 
					     if ( List_Manager.get(i).Get_Flit(List_Manager.get(i).vcbuffer[a]).get_Type()==0)
					     {
					    	   	 List_Manager.get(i).outVC[a]=List_Manager.get(i).Next_Free_Data(a);
								 List_Manager.get(i).vcbuffer[a] = List_Manager.get(i).Update_Run_Flit(a);//pour vidi la liste 
		    	 
					     }
					        
							}
				
			              }
			}
			else 
			{
				//System.out.println("Port list est vide");
			}
			
			// le code de else  : si le buffer selected est vide 
               
				
			} /** Fin du Traitement pour un Port   **/

			
			
			//----------------------move--------------------------------move-----------------------------------//
			
			// Pour ce Manager en cours : recuperer les info pour XML-File: Input et Output		
			
			int Suivre_switch_info=0;		
					  for (int k=0;k < List_Manager.get(i).Port_List_for_arbiter.size();k++)
					{// faire le tour sur tous les ports d un Router pour tracer dans XML_File.
						 
						 // System.out.println("neighbor_Index_List size= "+	List_Manager.get(i).Port_List_for_arbiter.size()); 
						  if (List_Manager.get(i).neighbor_Index_List[k] != -1)// faire le tour sur les port existants
					{
						Element XML_Port= new Element("Port");
					
					XML_Port=XML_File.Genarate_XML_Port(XML_Router, k);// reperer le nom de buffer dans list_port
					
					Element XML_Input_Port= new Element("Input_Port");	
					XML_Input_Port =XML_File.Genarate_XML_Input_Port(XML_Port);
					// System.out.println("numero de Port  en question= "+	k );
					// Traitement pour chaque buffer: cette boucle est formulé de telle façon de boucler que sur les buffer d un port en question.
					
					for (int h=k*Final_parametre.Number_vcbuffer_per_Plink; h< k*Final_parametre.Number_vcbuffer_per_Plink+Final_parametre.Number_vcbuffer_per_Plink; h++)
					{
						//System.out.println("numero de Buffer  en question= "+	h );
						//tester si le buffer en question n'est pas vide.
						//if (List_Manager.get(i).List_vcBuffer.get(Suivre_switch_info).Data.isEmpty() != true)
						if (List_Manager.get(i).List_vcBuffer.get(Suivre_switch_info).Data.size() != 0)

						{
							Element XML_Buffer= new Element("Buffer");	
						
						XML_Buffer =XML_File.Genarate_XML_Buffer(XML_Input_Port,h /** Buffer_ID**/,List_Manager.get(i).Switching_info[Suivre_switch_info][0] /** Credit**/,List_Manager.get(i).List_vcBuffer.get(Suivre_switch_info).Data.get(0).Type /** Flit_Type**/);
						 
						
						}
					/**
						else 
						{
							Element XML_Buffer= new Element("Buffer");	
							
							XML_Buffer =XML_File.Genarate_XML_Buffer(XML_Input_Port,h,final_parametre.Buffer_size ,-1);
							
							
						}**/
						Suivre_switch_info++;
						
					}// Fin de traitement pour chaque buffer
				
				if (XML_Input_Port.getContentSize()==0)
					{
						XML_Port.removeContent(XML_Input_Port);
					}
					
					
					if (XML_Port.getContentSize()==0)
					{
						XML_Router.removeContent(XML_Port);
					}
					
					
					}// Fin de recuperation  les info pour XML-File: Input et Output	
						
						
					
					
				}
					 
					  if (XML_Router.getContentSize()==0)
						{
						  XML_Cycle.removeContent(XML_Router);
						}	
						// afficher List_Port
					  //for (int i=0;i<3;i++)
					 // afficher VC out
					 /* for (int a = 0; a < 5; a++) {
							System.out.println("VC_BUFFER du  le port "+a+ "  est "+List_Manager.get(i).vcbuffer[a] );
							System.out.println("OUT_VC du port     "+a+ "  est    "+List_Manager.get(i).outVC[a] );
							
							
							
						}
						System.out.println();*/
					  
					// Afficher vcbuffer[a] et outVC[a]
						/*for (int a = 0; a < 5; a++) {
							System.out.println("yyyyyyyyyyy Pour le Manager "+i+" le port "+a+ " le vcbuffer est "+List_Manager.get(i).vcbuffer[a] );
							System.out.println("yyyyyyyyyyy Pour le Manager "+i+" le port "+a+ " le outVC est    "+List_Manager.get(i).outVC[a] );
							
							
							
						}
						System.out.println();
						*/
						
			
		}/**Fin de selection de flit pour ce Manager **/
		
		
		// traitement pour local
		

				
			
				
				
				

			// afficher l'etat des outVC et vcbuffer du Manager
		for(int ii=0; ii<3;ii++){
			for (int a = 0; a < 5; a++)
			{
				//System.out.println("============== VCout du Manager"+ii+"****port"+a+" est    "+List_Manager.get(ii).outVC[a]);
					
			}
			
			for (int a = 0; a < 5; a++)
			{
				
				//System.out.println("============== vcbuffer du Manager"+ii+"****port"+a+" est    "+List_Manager.get(ii).vcbuffer[a]);	
			}
			// affichier letat des buffer
			
			for(int k=0; k<List_Manager.get(ii).List_vcBuffer.size();k++){
			
					//System.out.println("===Manager"+ii+"  l etat du  Buffer"+List_Manager.get(ii).List_vcBuffer.get(k).Data.size());
				//System.out.println("===Manager"+ii+"   Buffer"+k+" etat"+List_Manager.get(ii).List_vcBuffer.get(k).Data.get(0).dest);
				
			}

			
	}
		
		
		
		
		
		//  apres la selection faite au niveau de chaque port, Preparer le move pour tous les port ds different Manager
		
		for (int i = 0; i < List_Manager.size(); i++) {
			//stem.out.println("------------------------- MOVE POUR MAnager "+i+"  ---------------------------");
			
			
			/*for(int d=0;d<5;d++)//afficher la liste port for arbiter
			{System.out.println("Port_List_for_arbite "+d+" : "+List_Manager.get(i).Port_List_for_arbiter.get(d).size());
			for(int j=0;j<List_Manager.get(i).Port_List_for_arbiter.get(d).size();j++)
			System.out.println(j+" : ( "+List_Manager.get(i).Port_List_for_arbiter.get(d).get(j)+")");
			
			}*/
			// afficher Port_List
			/* for (int a=0;a<5;a++)
			  {
				  System.out.println("Avant MoveLe portList du Port "+a);
				
				
					for(int j=0;j<List_Manager.get(i).Port_List_for_arbiter.get(a).size();j++)
						System.out.println("( "+List_Manager.get(i).Port_List_for_arbiter.get(a).get(j)+")");
						
						
				  
			  }*/
			
			for (int a = 1; a < 5; a++) // pour tous les port sauf le port local
			{
				//code du MOVE 
				if(List_Manager.get(i).outVC[a]!=-1 ){// ce traitement fait le suivant:
					                                      // 1-  move le flit vers le routeurNext .
					                                      // 2-  update le credit du ce routeurNext. 
					                                      // 3-  met a jour Switching_info[vcbuffer][4] 
					                                      // 4-  incremente le credit local de vcbuffer.
					                                      // 5-  ecrase le flit du buffer origine
					                                      // 6-  Mettre ajour(incremente) Switching_info[vcbuffer][4] du router en aval
					System.out.println("Manager   ="+i);
					System.out.println("MOVE pour le Port  :"+a+"  du cycle  "+cycle);	
					int route_info = List_Manager.get(i).Switching_info[List_Manager.get(i).vcbuffer[a]][3];// get l'adresse de vcbuffer out dans switching_info
					Flit f;	
					
				   f=List_Manager.get(i).Get_Flit(List_Manager.get(i).vcbuffer[a]);	//get flit

				if(route_info>=0){
					//if (a!=0)// pour tous les ports sauf le port Local
					//{
						List_Manager.get(List_Manager.get(i).neighbor_Index_List[a]).Move_Flit_To_next_Router(route_info, f);// 1- move le flit et 
						List_Manager.get(List_Manager.get(i).neighbor_Index_List[a]).Update_Credit_Local(route_info);   // 2- MAJ le credit local  RouterDestination
						//System.out.println("Mettre a jour le credit local"+List_Manager.get(List_Manager.get(i).neighbor_Index_List[a]).garented_VC_credit(route_info)+"Pour le cycle "+cycle+"Manager "+i+ " Buffer" + List_Manager.get(i).vcbuffer[a]+ "next router  "+List_Manager.get(i).neighbor_Index_List[a]); 	
						// ANALYSE
						List_Manager.get(i).Switching_info[List_Manager.get(i).vcbuffer[a]][4]= 
								List_Manager.get(List_Manager.get(i).neighbor_Index_List[a]).garented_VC_credit(route_info);//3-garented_VC_credit

						
						
						
				//	}
				
				
				
				List_Manager.get(i).List_vcBuffer.get(List_Manager.get(i).vcbuffer[a]).erese_Flit();//4-suprimer le flit
				List_Manager.get(i).Update_Credit(List_Manager.get(i).vcbuffer[a]);// 5-incrémenter le crédit local de vcbuffer Src	
				
				//System.out.println("-------------FLIT COUNTER------------------->"+	List_Manager.get(i).List_vcBuffer.get(List_Manager.get(i).vcbuffer[a]).Flit_counter);
				if(List_Manager.get(i).List_vcBuffer.get(List_Manager.get(i).vcbuffer[a]).Flit_counter == 0  ) // 6-if flit counter [][0] ==0 inisialisela line de vc buffer Src
				{List_Manager.get(i).Update_Status(List_Manager.get(i).vcbuffer[a]);
				/*******/
					}
				
				// incrementer le output_Credit du routeur adjacent a ce vcbuffer[a]. que pour les cas
				//1- swithch_info[][3]=vcbuffer[a]
				//2-  Flit_Couter <> 0
				// for chaque buffer du Manager adjacent: explorer la matrice switch_info
				//System.out.println(" pour le buffer "+List_Manager.get(i).vcbuffer[a]+" candidat pr le port "+a);
				//System.out.println("Le routeur adjacent du Manager "+i+"  est le routeur "+List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[a]));
				if (List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[a])!=-1 ) 
				{
					for (int BufferLenght=0;BufferLenght<List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[a])).List_vcBuffer.size();BufferLenght++ )
					{//System.out.println(" entree de la boucle"+BufferLenght);
						
						 if (List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[a])).Switching_info[BufferLenght][3]==List_Manager.get(i).vcbuffer[a])
				        	//	 &  le Switching_info[BufferLenght][2]= port_inversion(port_adjacent))
				          
						{
							/*System.out.println("son routeur  adjacent "+ List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[a])+
									"son buffer "+ BufferLenght+"son output_Creditavant"
									+List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[a])).Switching_info[BufferLenght][4]);*/
							
							List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[a])).Switching_info[BufferLenght][4]++;//incrementer le output_Credit du routeur adjacent a ce vcbuffer[a]
				           
							/*System.out.println("son routeur  adjacent "+ List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[a])+
									"son buffer "+ BufferLenght+
									"son output_Credit apres"
									+List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[a])).Switching_info[BufferLenght][4]);*/
							}	
						
					}	
				}
				
			         
				
				}// Fin route_info
				}
				
				
				
				
			}// Fin Move pour ce port
			
			
			
			
			//move pour port local 
			if (List_Manager.get(i).Port_List_for_arbiter.get(0).size() > 0) {
				System.out.println("LOCAL :-----------------------MOVE pour le Port  :0   "+"  du cycle  "+cycle+ "  MANAGER  "+i);	
				int	vcbuffer0=List_Manager.get(i).Update_Run_Flit(0);
				System.out.println("pour le manager "+i+"vcbuffer0= "+vcbuffer0);
				System.out.println("vcbuffer0 dupliquata"+List_Manager.get(i).vcbuffer[0]);
					Flit f =new Flit();	
					f=List_Manager.get(i).Get_Flit(vcbuffer0);
					f.latency=cycle-f.ArivelTime; //caluler latancy
					
					List_Manager.get(i).NI.get(vcbuffer0).add(f);
					List_Manager.get(i).List_vcBuffer.get(vcbuffer0).erese_Flit()	;
					List_Manager.get(i).Update_Credit(vcbuffer0);//incrémenter le crédit local de vcbuffer Src
					if(List_Manager.get(i).List_vcBuffer.get(vcbuffer0).Flit_counter == 0  ) //if flit counter [][0] ==0 inisialisela line de vc buffer Src
					List_Manager.get(i).Update_Status(vcbuffer0);
					// traitement pour le router adjacent --> special port local 0
					//System.out.println("avant boucle adjacent");
					if (List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])!=-1 ) 
					{  //System.out.println("aapres boucle adjacent");
					System.out.println("Le routeur correspoandadnt"+List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])).Adresse);
						for(int r=0;r<5;r++)
						{
							System.out.println("Pour le manager"+i+"  	neighbor_Index_List    "+List_Manager.get(i).neighbor_Index_List[r]);
						}
					for (int BufferLenght=0;BufferLenght<List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])).List_vcBuffer.size();BufferLenght++ )
						{//System.out.println(" entree de la boucle"+BufferLenght);
							//System.out.println(" affiche iteration"+BufferLenght+" buffer"+List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])).Switching_info[BufferLenght][3]);
							 if (List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])).Switching_info[BufferLenght][3]==List_Manager.get(i).vcbuffer[0])
					        	//	 &  le Switching_info[BufferLenght][2]= port_inversion(port_adjacent))
							 { 
							
								System.out.println("son routeur  adjacent "+ List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])+
										"son buffer "+ BufferLenght+"son output_Creditavant"
										+List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])).Switching_info[BufferLenght][4]);
							 System.out.println("Credit avant"+List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])).Switching_info[BufferLenght][4]);
								List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])).Switching_info[BufferLenght][4]++;//incrementer le output_Credit du routeur adjacent a ce vcbuffer[a]
								System.out.println("Credit apres"+List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])).Switching_info[BufferLenght][4]);
								System.out.println("son routeur  adjacent "+ List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])+
										"son buffer "+ BufferLenght+
										"son output_Credit apres"
										+List_Manager.get(List_Manager.get(i).Router_Correspondant_VCID(List_Manager.get(i).vcbuffer[0])).Switching_info[BufferLenght][4]);
								}	
							
						}	
					}	
			
			
			}	
			
			
	}   /*Fin Traitement pour les  de ce Manager */
		
		
		// afficher la matrice Switch_Info au debut du cycle			
				for(int ii=0; ii<3;ii++){
					System.out.println("============== switch Info da manager "+ii+"****cycle"+cycle);
					List_Manager.get(ii).view_Switching_information();		
					/*for(int d=0;d<5;d++)
					{System.out.println("Final Port_List_for_arbite "+d+" est de : "+List_Manager.get(ii).Port_List_for_arbiter.get(d).size());
					
					// afficher le contenu de port_list_for_arbiter  pour chaque port
					for(int j=0;j<List_Manager.get(ii).Port_List_for_arbiter.get(d).size();j++)
					System.out.println("( "+List_Manager.get(ii).Port_List_for_arbiter.get(d).get(j)+")");
					
					}*/
					
			}
		
		
		
		
					if (XML_Cycle.getContentSize()==0)
					{
						XML_File.Remove_XML_Cycle(XML_Cycle);
					}	
				
				
				
				
				
				
				
				
				XML_File.Genarate_XML_save();
	    /*for(int ii=0; ii<List_Manager.size();ii++){
		System.out.println("============== switch Info da manager "+ii+"****cycle"+cycle);
		List_Manager.get(ii).view_Switching_information();		
         }*/
				
				
				// vider tous les Port_List pour le prochain cycle
				for(int ii=0; ii<List_Manager.size();ii++){
					for(int d=0; d<5;d++)
					List_Manager.get(ii).Port_List_for_arbiter.get(d).clear();
				}
				
				
	}
		
		//exemple pour afficher le saut 
		
/*for(int i=0;i<List_Manager.size();i++){
	System.out.println("manager "+i+" :\n \n");
	
	for(int j=0;j<List_Manager.get(i).Number_vcBuffer;j++)
		if(List_Manager.get(i).NI.get(j).size()>0){
			int P=0;
			for(int k=0;k<List_Manager.get(i).NI.get(j).size();k++){
				if(List_Manager.get(i).NI.get(j).get(k).get_Type()==1){
					P=List_Manager.get(i).NI.get(j).get(k).get_nbr_FLit_attached();
			System.out.println("p : "+P);}
	        System.out.println("latency : "+List_Manager.get(i).NI.get(j).get(k).getLatency());
	        }
	        }
	System.out.println("----------------------");

}*/
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Cette fonction sert à détecter les nouvelle Flit venues.
	 * @param Listpaquet
	 * @param listM
	 * @param cycle
	 */
	
	
	
	
	

public ArrayList<Paquet> ToRouter(ArrayList<Paquet> Listpaquet,ArrayList<Manager> listM,int cycle){
	
	int adress;
	Paquet paquet;
	
	
	ArrayList<Paquet> List_Arrival_paquet;
	List_Arrival_paquet = new ArrayList<>();
	
	
			for(int p=0;p<Listpaquet.size();p++){
				
				paquet=Listpaquet.get(p);
				adress=paquet.getFlit(0).get_Src();
				
				// si le arrivalcycle du paquet=cycle en cours. et le VC demandé est lock
				// on incremente son cycle
				if(paquet.ArrivalCycle==cycle & listM.get(adress).Switching_info[paquet.vc][1]==1){
					Listpaquet.get(p).ArrivalCycle++;	
					}
				
				//si le arrivalcycle du paquet=cycle en cours. et le VC demandé est libte
				// on lock ce vc pour ce paquet.
				if(paquet.ArrivalCycle==cycle&listM.get(adress).Switching_info[paquet.vc][1]!=1){
				//header contient l'adresse src
				
					
			     // on lock le vc pour ce paquet
				listM.get(adress).Switching_info[paquet.vc][1]=1;
				// mettre a jour le credit dans switch_info
				listM.get(adress).Switching_info[paquet.vc][0]=Final_parametre.Buffer_size-(paquet.getFlit(0).get_nbr_FLit_attached()+1);
				//mettre a jour le Flit_Counter du buffer loué
				listM.get(adress).List_vcBuffer.get(paquet.vc).Flit_counter=paquet.getFlit(0).get_nbr_FLit_attached()+1;
				
				// attacher les flit au slots du baffer loué
				for(int i=0;i<paquet.size();i++) 
					listM.get(adress).List_vcBuffer.get(paquet.getVc()).Data.add(paquet.getFlit(i));
				
				List_Arrival_paquet.add(paquet);
				
				//Listpaquet.remove(p);
		}
			}
			
			
			
			return List_Arrival_paquet;
				
}
	

		
	// ****************fin

	

	public static void main(String[] args) {
		new Run_Similation();
		
	}
}
