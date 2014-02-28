package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DatiFatturazioneCommessaModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DatiFatturazioneCommessaModel(){}
	
	public DatiFatturazioneCommessaModel(String numeroCommessa, String estensione,String attivita, String numeroOrdine, String numeroMese, String mese, String tariffa, 
			 Float oreEseguite, Float oreFatturate, Float importoFatturare, Float importo, Float variazioneSal, Float variazionePcl, Float margine){
		 
		set("commessa",numeroCommessa+"."+estensione);
		set("commessaAttivita",numeroCommessa+"."+estensione+"("+attivita+")");
		set("numeroCommessa", numeroCommessa);	
		set("estensione", estensione);
		set("numeroOrdine", numeroOrdine);
		set("numeroMese", numeroMese);
		set("mese",mese);
		set("oreEseguite", oreEseguite);
		set("oreFatturate", oreFatturate);
		set("importoFatturare", importoFatturare);
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
