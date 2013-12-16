package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoOreDipCommesse extends BaseModel implements IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiepilogoOreDipCommesse(){}
	
	public RiepilogoOreDipCommesse(int idCommessa, String numeroCommessa, int idDipendente, String dipendente, Float oreLavoro, Float oreViaggio, Float totOre){
		
		set("idCommessa", idCommessa);
		set("idDipendente", idDipendente);
		set("numeroCommessa", numeroCommessa);
		set("dipendente", dipendente);
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
