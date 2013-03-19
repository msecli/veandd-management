package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class RdoCompletaModel extends BaseModel {

private static final long serialVersionUID = 1L;
	

    public RdoCompletaModel() {
    }
    
    public RdoCompletaModel(int idRdo, String numeroRdo, String cliente, String numOfferta, String dataOfferta, String descrizione, String importo,
    		String numOrdine, String commessa, String dataInizioOrdine, String dataFineOrdine, String tariffaOraria, String numeroRisorse, String numeroOre, String numeroOreResidue)
    {
    	set("numeroRdo",numeroRdo); 
    	set("idRdo", idRdo);
    	set("cliente", cliente);
    	set("descrizione", descrizione);
    	set("numeroOfferta", numOfferta);
    	set("importo", importo);
    	set("dataOfferta", dataOfferta);
    	set("numeroOrdine", numOrdine);
    	set("numeroCommessa", commessa);
    	set("tariffaOraria", tariffaOraria);
    	set("dataInizio", dataInizioOrdine);
    	set("dataFine", dataFineOrdine);
    	set("numeroRisorse", numeroRisorse);
    	set("numeroOre", numeroOre);
    	set("numeroOreResidue", numeroOreResidue);
    }
    

	public String getNumeroRda() {
		return get("numeroRdo");
	}

	public String getCliente() {
		return get("cliente");
	}
	
	public int getIdRdo() {
		return get("idRdo");
	}
		
	public String getNumeroOfferta() {
		return get("numeroOfferta");
	}
	
	public String getDescrizione() {
		return get("descrizione");
	}
	
	public String getImporto() {
		return get("importo");
	}
	
	public String getData(){		
		return get("dataOfferta");
	}
	
	public String getNumeroOrdine(){
		
		return get("numeroOrdine");
	}
	
	public String getTariffa() {
		return get("tariffaOraria");
	}
	
	public String getDataInizio(){
		
		return get("dataInizio");
	}
	
	public String getDataFine(){
		
		return get("dataFine");
	}
	
	public String getNumeroRisorse(){
		
		return get("numeroRisorse");
	}
	
	public String getNumeroOre(){
		
		return get("numeroOre");
	}
	
	public String getNumeroOreResidue(){
		
		return get("numeroOreResidue");
	}
	
	public String getNumeroCommessa(){
		
		return get("numeroCommessa");
	}
	
}
