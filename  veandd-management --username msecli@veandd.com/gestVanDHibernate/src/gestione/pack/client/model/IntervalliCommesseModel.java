package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class IntervalliCommesseModel  extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IntervalliCommesseModel(){
		
	}
	
	public IntervalliCommesseModel( String numeroCommessa, String oreLavoro, String oreViaggio, 
			String totOreLavoro, String totOreViaggio, String descrizione, String giustificativo){	
		
		//set("idAssociazionePtoC", idAssociazione);
		set("numeroCommessa", numeroCommessa);
		set("oreLavoro", oreLavoro);
		set("oreViaggio", oreViaggio);
		set("totOreLavoro", totOreLavoro);
		set("totOreViaggio", totOreViaggio);
		set("descrizione", descrizione);
		set("giustificativo", giustificativo);
	}
	
	public String getNumeroCommessa(){
		
		return get("numeroCommessa");
	
	}
	
	public String getEstensione(){
		
		return get("estensione");
		
	}
	
	public String getOreLavoro(){
		
		return get("oreLavoro");
		
	}
	
	public String getOreViaggio(){
		
		return get("oreViaggio");
		
	}
	
	public String getIdAssociazione(){
		
		return get("idAssociazionePtoC");
	}
	
	public String getTotOreLavoro(){
		
		return get("totOreLavoro");
		
	}
	
	public String getTotOreViaggio(){
		
		return get("totOreViaggio");
		
	}
	
	public String getDescrizione(){
		return get("descrizione");
	}
	
}
