package Router;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import Router.Final_parametre;


public class Simulation_Results_To_XML_File {
	
	public  Element Scenario;
	public  Element Simulation;
	public  Document document;
	//public  Element Cycle;
	
	public Simulation_Results_To_XML_File()
	{
	  	this.Scenario = new Element("Scenario");
		this.document = new Document(Scenario);
	}
	
	
	
	public void Create_XML_Simulation_Results()
	{
		
	}
	
	
	public void Generate_XML_NoC_Generation()
	{
		Element NoC_Generation = new Element("NoC_Generation");
		Element NoC_Size = new Element("NoC_Size");
		NoC_Size.setText(""+Final_parametre.Noc_size);
		Element NoC_Nbr_Buffer_Per_Port = new Element("NoC_Nbr_Buffer_Per_Port");
		NoC_Nbr_Buffer_Per_Port.setText(""+ Final_parametre.Number_vcbuffer_per_Plink);
		Element Buffer_size = new Element("Buffer_size");
		Buffer_size.setText(""+ Final_parametre.Buffer_size);
		
		
		this.Scenario.addContent(NoC_Generation);
		NoC_Generation.addContent(NoC_Size);
		NoC_Generation.addContent(NoC_Nbr_Buffer_Per_Port);
		NoC_Generation.addContent(Buffer_size);
		
		
		
		
		
		
		
	}
	
	
	public void Genarate_XML_NoC_Configuration()
	{
		
		Element NoC_Configuration = new Element("NoC_Configuration");
		Element Routing = new Element("Routing");
		Routing.setText("XY");
		Element Arbiter = new Element("Arbiter");
		Arbiter.setText("Round robin");
		Element Switching = new Element("Switching");
		Switching.setText("Wormole");
		Element Flow_Control = new Element("Flow_Control");
		Flow_Control.setText("Credit Based");
		Element Flit_Length = new Element("Flit_Length");
		Flit_Length.setText("16");
		this.Scenario.addContent(NoC_Configuration);
		NoC_Configuration.addContent(Routing);
		NoC_Configuration.addContent(Arbiter);
		NoC_Configuration.addContent(Switching);
		NoC_Configuration.addContent(Flow_Control);
		NoC_Configuration.addContent(Flit_Length);
	}
	
	public void  Genarate_XML_NoC_Execution_Time()
	{
		Element Execution_Time = new Element("Execution_Time");
		Execution_Time.setText(""+ Run_Similation.Simulation_Life );
		this.Scenario.addContent(Execution_Time);
	}
	
	
	public void   Genarate_XML_Simulation ()
	{
		
		this.Simulation = new Element("Simulation");
		this.Scenario.addContent(Simulation);
		
		
		
		
	}
	
	public Element Genarate_XML_Cycle( int Cycle_number)
	{
		
		Element Cycle= new Element("Cycle");
		Cycle.setAttribute("Cycle", ""+Cycle_number);
		this.Simulation.addContent(Cycle);
		return Cycle;
			
	}
	
	
	
	
	public void Remove_XML_Cycle( Element Cycle)
	{
		
		this.Simulation.removeContent(Cycle);
		
			
	}
	
	public Element   Genarate_XML_Router(Element Cycle, int Manager_ID )
	{
		Element Router= new Element("Router");
		Router.setAttribute("Router", ""+Manager_ID);
		Cycle.addContent(Router);
		return Router;
		
		
	}
	
	
	public Element   Genarate_XML_Arrival_Data(Element Router, ArrayList<Paquet> List_Arrival_paquet, int ID_Manager)
	{
		
		Element Arrival_Data= new Element("Arrival_Data");
		for(int i=0;i<List_Arrival_paquet.size();i++)
		{
			if(List_Arrival_paquet.get(i).getFlit(0).Src== ID_Manager)
			{
				Element Router_Source= new Element("Router_Source");
				Router_Source.addContent(""+ List_Arrival_paquet.get(i).getFlit(0).Src);
				Arrival_Data.addContent(Router_Source);
				Element Router_Destination= new Element("Router_Destination");
				Router_Destination.addContent(""+ List_Arrival_paquet.get(i).getFlit(0).dest);
				Arrival_Data.addContent(Router_Destination);
				Element Buffer_Source= new Element("Buffer_Source");
				Buffer_Source.addContent(""+ List_Arrival_paquet.get(i).vc);
				Arrival_Data.addContent(Buffer_Source);
				Element Flit_Number= new Element("Flit_Number");
				Flit_Number.addContent(""+ List_Arrival_paquet.get(i).size());
				Arrival_Data.addContent(Flit_Number);
				
				
			}
			
			
			
		}
		Router.addContent(Arrival_Data);
		return Arrival_Data;
		
	}
	
	
	public Element   Genarate_XML_Port(Element Router, int Port_ID)
	{
		
		
		
		Element Port= new Element("Port");
		
		if (Port_ID==0)
		{
			Port.setAttribute("Port", "Local");	
		}
		else if (Port_ID==1)
		{
			Port.setAttribute("Port", "West");
		}
		else if (Port_ID==2)
		{
			Port.setAttribute("Port", "South");
		}
		else if (Port_ID==3)
		{
			Port.setAttribute("Port", "East");
		}
		else if (Port_ID==4)
		{
			Port.setAttribute("Port", "North");
		}
		
		Router.addContent(Port);
		return Port;
		
	}
	public Element   Genarate_XML_Output_Port(Element Port)
	{
		Element Output_Port= new Element("Output_Port");
		Port.addContent(Output_Port);
		return Output_Port;
	}
	
	public Element   Genarate_XML_Input_Port(Element Port)
	{
		Element Input_Port= new Element("Input_Port");
		Port.addContent(Input_Port);
		return Input_Port;
	}
	
	
	public Element   Genarate_XML_Buffer(Element Input_Port, int Buffer_ID, int Credit, int F_Type )
	{
		if (Credit==Final_parametre.Buffer_size)
		{
			return null;
		}
		else {
			Element Buffer= new Element("Buffer");
			Buffer.setAttribute("Buffer", ""+Buffer_ID);
			Input_Port.addContent(Buffer);
			
			Element Credit_Buffer= new Element("Credit_Buffer");
			Credit_Buffer.setText(""+Credit);
			Buffer.addContent(Credit_Buffer);
			
			Element Flit_Type= new Element("Flit_Type");
			
			if (F_Type ==1)
			{
				Flit_Type.setText("Header");	
			}
			else 
			{
				Flit_Type.setText("Data");	
			}
			/**else 
			{
				//Flit_Type.setText("null");	
				//Buffer.removeContent(Flit_Type);
				Input_Port.removeContent(Buffer);
			}		**/
			Buffer.addContent(Flit_Type);
			
			
			return Buffer;
			
			
			
		}
		
	}
	
	/**
	 * cette methose sert à:
	 * @author Meghabber
	 *@category : method
	 * 
	 */
	public void   Genarate_XML_save()
	{
		
		
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		try {
			sortie.output(this.document, new FileOutputStream("simulation.xml"));
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	

}

