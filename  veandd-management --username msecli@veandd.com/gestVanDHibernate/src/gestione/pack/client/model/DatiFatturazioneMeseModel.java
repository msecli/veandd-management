package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DatiFatturazioneMeseModel extends BaseModel implements IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DatiFatturazioneMeseModel(){}
	
	public  DatiFatturazioneMeseModel(int idFoglioFatturazione, String sede, String pm, String commessa, String cliente,  String numeroOrdine,  String oggettoAttivita, String attivitaOrdine, Float oreEseguite, 
			Float oreFatturate, Float tariffaOraria, Float importo, Float importoEffettivo, Float variazioneSal, Float importoSal, Float variazionePcl,
			Float importoPcl, Float oreScaricate, Float margine, String note, String statoFattura){
		set("sede", sede);
		set("idFoglioFatturazione", idFoglioFatturazione);
		set("pm", pm);
		set("numeroCommessa", commessa);
		set("cliente", cliente);
		set("numeroOrdine", numeroOrdine);
		set("oggettoAttivita", oggettoAttivita);
		set("attivitaOrdine",attivitaOrdine);
		set("tariffaOraria", tariffaOraria);
		set("oreEseguite", oreEseguite);
		set("oreFatturate", oreFatturate);
		set("variazioneSal",variazioneSal);
		set("importoSal", importoSal);
		set("variazionePcl",variazionePcl);
		set("importoPcl", importoPcl);
		set("importo",importo);
		set("importoEffettivo", importoEffettivo);
		set("oreScaricate", oreScaricate);
		set("margine", margine);
		set("note", note);
		set("statoFattura",statoFattura);
	}
	
	public String getPM(){
		return get("pm");
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
