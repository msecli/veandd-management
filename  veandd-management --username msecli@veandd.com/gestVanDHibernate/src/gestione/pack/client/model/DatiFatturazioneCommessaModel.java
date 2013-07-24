package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DatiFatturazioneCommessaModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DatiFatturazioneCommessaModel(){}
	
	public DatiFatturazioneCommessaModel(String numeroCommessa, String estensione, String numeroOrdine, String mese, String tariffa, Float oreEseguite, Float oreFatturate, Float importo, Float variazioneSal, Float variazionePcl, Float margine){
		
		set("numeroCommessa", numeroCommessa);	
		set("estensione", estensione);
		set("numeroOrdine", numeroOrdine);
		set("mese",mese);
		set("oreEseguite", oreEseguite);
		set("oreFatturate", oreFatturate);
		set("variazioneSal",variazioneSal);
		set("variazionePcl",variazionePcl);
		set("importo",importo);
		set("margine", margine);
		set("tariffa", tariffa);
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
