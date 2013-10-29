package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoOreTotaliCommesse extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiepilogoOreTotaliCommesse(){}
	
	public RiepilogoOreTotaliCommesse(String numeroCommessa, String estensione, String numeroOrdine, String oreOrdine , Float  oreLavoro, String compilato){	
		set("numeroCommessa", numeroCommessa);
		set("estensione", estensione);
		set("numeroOrdine", numeroOrdine);
		set("oreLavoro",oreLavoro);
		set("oreOrdine", oreOrdine);		
		set("compilato", compilato);
	}
	
	public RiepilogoOreTotaliCommesse(String numeroCommessa, String estensione, Float sal, String salDaButtare, Float pcl, String numeroOrdine, 
			String oggettoOrdine, String descrizioneAttivita, int idAttivita, String oreOrdine , Float  oreLavoro, String compilato){	
		set("numeroCommessa", numeroCommessa);
		set("estensione", estensione);
		set("sal",sal);
		set("salDaButtare", salDaButtare);
		set("pcl",pcl);
		set("numeroOrdine", numeroOrdine);
		set("oggettoOrdine", oggettoOrdine);
		set("descrizioneAttivita", descrizioneAttivita);
		set("oreLavoro",oreLavoro);
		set("oreOrdine", oreOrdine);		
		set("compilato", compilato);
		set("idAttivita",idAttivita);
	}
	
	public String getNumeroCommessa() {
		return get("numeroCommessa");
	}
	
	public String getNumeroOrdine(){
		return get("numeroOrdine");
	}
	
	public String getEstensione(){
		return get("estensione");
	}
		
	public Float getOreLavoro(){
		return get("oreLavoro");		
	}
	
	public String getOreOrdine(){
		return get("oreOrdine");		
	}
	
	public String getFlagCompilato(){
		return get("compilato");
	}
	
}
