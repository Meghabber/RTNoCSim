package Router;

import java.util.ArrayList;

public class Noc {
public ArrayList<Manager> ListManager;

public Noc(int sizeNoc){
	for (int i = 0; i < sizeNoc*sizeNoc; i++) {
		ListManager.add(new Manager(i));
	}
	
}

}
