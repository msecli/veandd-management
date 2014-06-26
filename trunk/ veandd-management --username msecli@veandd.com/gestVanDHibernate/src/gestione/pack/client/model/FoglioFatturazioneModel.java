package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class FoglioFatturazioneModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FoglioFatturazioneModel(){}
	
	public FoglioFatturazioneModel(String numeroOrdine, String numeroRda, String oreOrdine, String residuoOre, String importo, String importoResiduo, Float tariffaOraria,
			String oreEseguiteRegistrate, String salAttuale, String pclAttuale, String oreFatturare, String importoFatturare,//importo reale inserito che può differire da tariffa*ore
			String importoRtv, String variazioneSal, String variazionePcl, String oreScaricate, String note, String stato, String oreRimborsoSpese){
		
		set("numeroOrdine", numeroOrdine);
		set("numeroRda", numeroRda);
		set("oreOrdine", oreOrdine);
		set("residuoOre", residuoOre);
		set("importo", importo);
		set("importoResiduo", importoResiduo);
		set("tariffaOraria", tariffaOraria);
		set("oreEseguiteRegistrate", oreEseguiteRegistrate);
		set("salAttuale", salAttuale);
		set("pclAttuale", pclAttuale);
		set("oreFatturare", oreFatturare);
		set("importoDaFatturare", importoFatturare);
		set("importoRtv",importoRtv);
		set("variazioneSal",variazioneSal);
		set("variazionePcl",variazionePcl);
		set("oreScaricate",oreScaricate);
		set("note",note);
		set("stato", stato);
		set("oreRimborsoSpese", oreRimborsoSpese);		
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
