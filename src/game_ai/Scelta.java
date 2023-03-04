package game_ai;

import it.unical.mat.embasp.languages.*;

@Id("scelta")
public class Scelta {

	@Param(0)
	String scelta;

	public Scelta() {
		
	}

	public Scelta(String scelta) {
		this.scelta = scelta;
	}

	@Override
	public String toString() {
		return "Scelta [scelta=" + scelta + "]";
	}

	public String getScelta() {
		return scelta;
	}

	public void setScelta(String scelta) {
		this.scelta = scelta;
	}
	

	
	
}
