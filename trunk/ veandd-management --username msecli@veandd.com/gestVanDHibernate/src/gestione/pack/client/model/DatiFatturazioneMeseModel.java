package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DatiFatturazioneMeseModel extends BaseModel implements IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DatiFatturazioneMeseModel(){}
	
	public  DatiFatturazioneMeseModel(String pm, String commessa, String cliente, String oggettoAttivita, Float oreEseguite, 
			Float oreFatturate, Float tariffaOraria, Float importo, Float variazioneSal, Float variazionePcl, Float margine){
		set("pm", pm);
		set("numeroCommessa", commessa);
		set("cliente", cliente);
		set("oggettoAttivita", oggettoAttivita);
		set("tariffaOraria", tariffaOraria);
		set("oreEseguite", oreEseguite);
		set("oreFatturate", oreFatturate);
		set("variazioneSal",variazioneSal);
		set("variazionePcl",variazionePcl);
		set("importo",importo);
		set("margine", margine);
	}
	
	public Float getTariffaOraria(){
		return get("tariffaOraria");
	}
	public Float getOreEseguite(){
		return get("oreEseguite");
	}
	public Float getOreFatturate(){
		return get("oreFatturate");
	}
	public Float getVariazioneSal(){
		return get("variazioneSal");
	}
	public Float getVariazionePcl(){
		return get("variazionePcl");
	}
	public Float getMargine(){
		return get("margine");
	}
	public Float getImporto(){
		return get("importo");
	}

}
