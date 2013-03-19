package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class IntervalliIUModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IntervalliIUModel(){}
	
	public IntervalliIUModel(String movimento, String orario, String sorgente, String sostituito){
		
		set("movimento", movimento);
		set("orario", orario);
		set("sorgente", sorgente);
		set("sostituito", sostituito);		
		
	}
	
	
	public String getMovimento(){
		
		return get("movimento");
	}
	
	public String getSorgente(){
		
		return get("sorgente");
	}
	
	public String getSostituito(){
		
		return get("sostituito");
	}
	
	public String getOrario(){
		
		return get("orario");
	}
	
	
}
