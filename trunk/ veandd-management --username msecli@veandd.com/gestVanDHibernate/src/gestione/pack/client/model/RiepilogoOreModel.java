package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoOreModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiepilogoOreModel(){
		
	}
	
	public RiepilogoOreModel(String totGenerale, String oreAssenza, String oreStraordinario, String oreRecupero, String oreRecuperoTotale){
		set("totGenerale", totGenerale);
		set("oreAssenza", oreAssenza);
		set("oreStraordinario", oreStraordinario);
		set("oreRecupero", oreRecupero);
		set("oreRecuperoTotale", oreRecuperoTotale);
		
	}
	
	public String getTotOreGenerale(){
		
		return get("totGenerale");		
		
	}
	
	public String getOreAssenza(){
		
		return get("oreAssenza");		
		
	}
	
	public String getOreStraordinario(){
	
		return get("oreStraordinario");		
	
	}

	public String getOreRecupero(){
	
		return get("oreRecupero");		
	
	}
	
	public String getOreRecuperoTotale(){
		
		return get("oreRecuperoTotale");		
	
	}
	
}
