package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoOreDipFatturazione extends BaseModel implements IsSerializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiepilogoOreDipFatturazione(){}
	
	public RiepilogoOreDipFatturazione(String numeroCommessa, String meseRif, String numeroMese, int idDip, String dipendente, Float oreLavoro, 
			Float oreViaggio,  Float oreStrao, Float oreTotali, Float oreTotaliIU, Boolean checkOre){		
		set("numeroCommessa", numeroCommessa);
		set("dipendente", dipendente);
		set("meseRif",meseRif);
		set("numeroMese",numeroMese);
		set("indicativoMese",numeroMese+"."+meseRif);
		set("oreLavoro",oreLavoro);
		set("oreViaggio", oreViaggio);	
		set("oreStraordinario", oreStrao);
		set("oreTotali", oreTotali);
		set("idDip", idDip);
		set("checkOre", checkOre);
		set("oreTotaliIU", oreTotaliIU);
		
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
	
	public int getIdPersonale(){
		return get("idDip");
	}
	
	
}
