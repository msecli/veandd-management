package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoOreDipFatturazione extends BaseModel implements IsSerializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiepilogoOreDipFatturazione(){}
	
	public RiepilogoOreDipFatturazione(String numeroCommessa, int idDip, String dipendente, Float oreLavoro, Float oreViaggio, Float oreTotali){		
		set("numeroCommessa", numeroCommessa);
		set("dipendente", dipendente);
		set("oreLavoro",oreLavoro);
		set("oreViaggio", oreViaggio);	
		set("oreTotali", oreTotali);
		set("idDip", idDip);
	}
	
	public String getNumeroCommessa() {
		return get("numeroCommessa");
	}
	
	public String getDipendente(){
		return get("dipendente");
	}
	
	public Float getOreLavoro(){
		return get("oreLavoro");		
	}
	
	public Float getOreViaggio(){
		return get("oreViaggio");		
	}
	
	public Float getOreTotali(){
		return get("oreTotali");		
	}
}
