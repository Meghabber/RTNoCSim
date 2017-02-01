package Router;

public class Flit {
	
	public  int Src; 
	public  int dest; 
	public int Type;
	private int NumberFlit;
    public int latency;
	public int ArivelTime;
	// ajouter par amine
	public int VC;
	public int Priority;
	
	public int get_Vc()
	{
		return 0;
		
	}
	
	public int getLatency() {
		return latency;
	}

	public int getArivelTime() {
		return ArivelTime;
	}

	public Flit(int Type) {
		this.Type = Type;
		latency=0;

	}

	public Flit() {

	}

	public void setSrc(int src) {

		Src = src;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public void setType(int type) {
		Type = type;
	}

	public void setNumberFlit(int numberFlit) {
		NumberFlit = numberFlit;
	}

	public int get_Src() {

		return Src;

	}

	public int get_dest() {

		return dest;
	}

	public int get_nbr_FLit_attached() {
		return NumberFlit;
	}

	public int get_Type() {
		return Type;
	}
	// ajouter par amine
	public int get_Priority() {
		return Priority;
	}
	// ajouter par amine
	public void setPriority(int priority) {
		priority = Priority;
	}

}