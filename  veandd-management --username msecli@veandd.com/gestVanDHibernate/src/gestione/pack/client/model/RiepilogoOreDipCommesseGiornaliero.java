package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoOreDipCommesseGiornaliero extends BaseModel implements IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiepilogoOreDipCommesseGiornaliero(){}
	
	public RiepilogoOreDipCommesseGiornaliero(String numeroCommessa, String dipendente, String giorno, Float oreLavoro, Float oreViaggio, Float totOre){
		
		set("numeroCommessa", numeroCommessa);
		set("dipendente", dipendente);
		set("giorno", giorno);
		set("oreLavoro",oreLavoro);
		set("oreViaggio", oreViaggio);		
		set("totOre", totOre);
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
	
	public Float getTotOre(){
		return get("totOre");
	}

}
