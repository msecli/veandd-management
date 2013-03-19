package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TimbraturaModel extends BaseModel implements IsSerializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimbraturaModel(){}
	
	public TimbraturaModel(String numeroBadge, String data, String movimento, String orario){
		set("numeroBadge", numeroBadge);
		set("data", data); //gg/mm/aa
		set("movimento", movimento);
		set("orario", orario);			
	}
	
	public String getMovimento(){
		
		return get("movimento");
	}
	
	public String getNumeroBadge(){
		
		return get("numeroBadge");
	}
	
	public String getData(){
		
		return get("data");
	}
	
	public String getOrario(){
		
		return get("orario");
	}
}
