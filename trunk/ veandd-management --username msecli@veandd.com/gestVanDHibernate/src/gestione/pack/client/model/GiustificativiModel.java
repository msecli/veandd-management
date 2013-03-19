package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class GiustificativiModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GiustificativiModel(){}
	
	public GiustificativiModel(String orePreviste, String totGenerale, String delta, String oreViaggio, String oreDeltaViaggio, 
			String oreRecupero,  String giustificativo, String statoRevisione,  String straordinario, String oreFerie, String orePermesso, String noteAggiuntive){
		
		set("orePreviste",orePreviste);
		set("totGenerale",totGenerale);
		set("delta",delta);
		set("oreViaggio",oreViaggio);
		set("oreDeltaViaggio", oreDeltaViaggio);
		set("oreRecupero", oreRecupero);
		set("giustificativo",giustificativo);
		set("statoRevisione", statoRevisione);		
		set("oreFerie",oreFerie);
		set("orePermesso", orePermesso);
		set("straordinario", straordinario);
		set("noteAggiuntive", noteAggiuntive);
	}
	
	public String getOrePreviste(){
		
		return get("orePreviste");
		
	}
	
	public String getDelta(){
		
		return get("delta");
		
	}
	
	public String getOreFerie(){
		
		return get("oreFerie");
	
	}
	
	public String getOrePermesso(){
		
		return get("orePermesso");
		
	}
	
	public String getOreStraordinario(){
		
		return get("straordinario");
		
	}

	public String getOreViaggio(){
		
		return get("oreViaggio");
	
	}
	
	public String getOreDeltaViaggio(){
		
		return get("oreDeltaViaggio");
		
	}
	
	public String getOreRecupero(){
		
		return get("oreRecupero");
		
	}
	
	public String getGiustificativo(){
		
		return get("giustificativo");
		
	}	
	
	public String getTotGenerale(){
		
		return get("totGenerale");
		
	}	
	
	public String getStatoRevisione(){
		return get("statoRevisione");
	}
	
}
