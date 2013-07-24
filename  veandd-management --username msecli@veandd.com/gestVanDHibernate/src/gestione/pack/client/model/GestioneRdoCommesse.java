package gestione.pack.client.model;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;

public class GestioneRdoCommesse extends BaseModel {

private static final long serialVersionUID = 1L;
	

    public GestioneRdoCommesse() {
    }

	public GestioneRdoCommesse(int idRdo, int idCommessa, String cliente,
			String pm, String numCommessa, String estensioneCommessa,
			String statoCommessa, String tipologiaCommessa,
			String tariffaSalPcl, String oreLavoroCommessa, String descrizione,
			String numRdo, String numOfferta, String numOrdine,
			String statoOrdine, String tariffaOrdine, String oreOrdine,
			Date dataOrdine) {
		
		set("idRdo", idRdo);
    	set("idCommessa", idCommessa);
    	set("cliente", cliente);
    	set("pm", pm);
    	set("numeroCommessa", numCommessa);
    	set("estensioneCommessa", estensioneCommessa);
    	set("tariffaSalPcl", tariffaSalPcl);
    	set("statoCommessa", statoCommessa);
    	set("oreLavoroCommessa", oreLavoroCommessa);
    	set("tipologiaCommessa", tipologiaCommessa);
    	set("descrizione", descrizione);
    	
    	set("numeroRdo",numRdo); 
    	set("numeroOfferta", numOfferta);
    	set("numeroOrdine", numOrdine);
    	set("tariffaOraria", tariffaOrdine);
    	set("numeroOre", oreOrdine);    	
    	set("statoOrdine", statoOrdine);
    	set("dataOrdine", dataOrdine);
	}


	public String getNumeroRdo(){
		return get("numeroRdo");
	}
    
    public String getNumeroCommessa(){
		return get("numeroCommessa");
	}
    
    public String getNumeroOfferta(){
		return get("numeroOfferta");
	}
    
    public String getNumeroOrdine(){
		return get("numeroOrdine");
	}
}