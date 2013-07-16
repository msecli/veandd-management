package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class GestioneRdoCommesse extends BaseModel {

private static final long serialVersionUID = 1L;
	

    public GestioneRdoCommesse() {
    }
    
    public GestioneRdoCommesse(int idRdo, int idCommessa,String cliente, String pm, String commessa, String salAttuale, 
    		String pclAttuale, String tariffaSalPcl, String numeroRdo,  String numOfferta, 
    		String numOrdine,   String tariffaOraria,  String numeroOre, String numeroOreResidue, String statoCommessa, String statoOrdine)
    {
    	set("idRdo", idRdo);
    	set("idCommessa", idCommessa);
    	set("cliente", cliente);
    	set("pm", pm);
    	set("numeroCommessa", commessa);
    	set("tariffaSalPcl", tariffaSalPcl);
    	set("salAttuale", salAttuale);
    	set("pclAttuale", pclAttuale);
    	
    	set("numeroRdo",numeroRdo); 
    	set("numeroOfferta", numOfferta);
    	set("numeroOrdine", numOrdine);
    	set("tariffaOraria", tariffaOraria);
    	set("numeroOre", numeroOre);
    	set("numeroOreResidue", numeroOreResidue);
    	
    	set("statoCommessa", statoCommessa);
    	set("statoOrdine", statoOrdine);
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