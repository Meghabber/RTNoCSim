package Router;

import java.util.ArrayList;

public class VcBuffer {

	public int VC_id;

	public  int Flit_counter;

public ArrayList<Flit> Data;

	public  int Size_of_VcBuffer;//nombre de slot dans VCbuffer?? in final-param
	//buffer_size()
public int port;
//vc_id
	public VcBuffer(int port,int VC_id) {
		this.Data=new ArrayList<>();
		this.Size_of_VcBuffer = Final_parametre.Buffer_size;

//en parametre buffer_size() et le physiquel link (0 1 2 3 4)
		this.port=port;
		this.VC_id=VC_id;//?????????????????
		
		Flit_counter=0;

	}

	

	/*si le buffer est vide retourné  1 si non retourné 0 */
	
	public Flit getFlit() {
		return Data.get(0);
	}



	public void setData(Flit flit) {
		
		Data.add(flit);
	
			
	}



	public boolean Check_Flit_Condiate() {
		if (this.Data.isEmpty())
			return false;
		else
			return true;
	}
	

	

	public void erese_Flit() {
		Update_Flit_Counter();
		this.Data.remove(0);
	}

	public void Update_Flit_Counter() {
		this.Flit_counter--;
		
	}

	public void Update_Status() {
		
		Flit_counter=this.Size_of_VcBuffer;
		//lok
		
		
	}
	public int getVC_id() {
	return VC_id;
}



public void setVC_id(int vC_id) {
	VC_id = vC_id;
}



public int getPort() {
	return port;
}



public void setPort(int port) {
	this.port = port;
}



}