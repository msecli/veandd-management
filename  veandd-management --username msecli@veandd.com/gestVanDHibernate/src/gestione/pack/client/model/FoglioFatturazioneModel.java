package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class FoglioFatturazioneModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FoglioFatturazioneModel(){}
	
	public FoglioFatturazioneModel(String numeroOrdine, String oreOrdine, String residuoOre, Float tariffaOraria, String oreEseguiteRegistrate, String salAttuale, String pclAttuale, String oreFatturare
			, String variazioneSal, String variazionePcl, String oreScaricate, String note, String stato){
		
		set("numeroOrdine", numeroOrdine);
		set("oreOrdine", oreOrdine);
		set("residuoOre", residuoOre);
		set("tariffaOraria", tariffaOraria);
		set("oreEseguiteRegistrate", oreEseguiteRegistrate);
		set("salAttuale", salAttuale);
		set("pclAttuale", pclAttuale);
		set("oreFatturare", oreFatturare);
		set("variazioneSal",variazioneSal);
		set("variazionePcl",variazionePcl);
		set("oreScaricate",oreScaricate);
		set("note",note);
		set("stato", stato);
	}
	
	public String getStato(){
		return get("stato");
	}	
	public String getNumeroOrdine(){
		return get("numeroOrdine");
	}
	public String getOreOrdine(){
		return get("oreOrdine");
	}
	public String getResiduoOre(){
		return get("residuoOre");
	}
	public String getOreLavorate(){
		return get("oreLavorate");
	}
	public Float getTariffaOraria(){
		return get("tariffaOraria");
	}
	public String getOreEseguiteRegistrate(){
		return get("oreEseguiteRegistrate");
	}	
	public String getsalAttuale(){
		return get("salAttuale");
	}
	public String getPclAttuale(){
		return get("pclAttuale");
	}
	public String getOreFatturate(){
		return get("oreFatturare");
	}
	public String getVariazioneSal(){
		return get("variazioneSal");
	}
	public String getVariazionePcl(){
		return get("variazionePcl");
	}
	public String getOreScaricate(){
		return get("oreScaricate");
	}
	public String getNote(){
		return get("note");
	}
	
}