package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

import gestione.pack.shared.Commessa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrdineModel  extends BaseModel implements Serializable {


	private static final long serialVersionUID = 1L;

	private List<CommessaModel> listaCommesse;
	
	public OrdineModel(){}
	
	public OrdineModel(int idordine, String numOrdine, String estensioneOrdine, String numRda, String commessa, String dataInizioOrdine,
			String dataFineOrdine, String descrizione, String tariffaOraria, String numeroRisorse, String numeroOre, String importo){
		
		set("idOrdine", idordine );
    	set("numeroOrdine", numOrdine);
    	set("estensioneOrdine", estensioneOrdine);
    	set("numeroRda",numRda);
    	set("numeroCommessa", commessa);
    	set("descrizione", descrizione);
    	set("tariffaOraria", tariffaOraria);
    	set("dataInizio", dataInizioOrdine);
    	set("dataFine", dataFineOrdine);
    	set("numeroRisorse", numeroRisorse);
    	set("numeroOre", numeroOre);
    	set("importo", importo);
		    	
	}
	
	public List<CommessaModel> getListaCommesse() {
		return listaCommesse;
	}

	public void setListaCommesse(List<CommessaModel> listaCommesse) {
		this.listaCommesse = listaCommesse;
	}
	
	public String getNumeroRda() {
		return get("numeroRda");
	}

	public int getIdOfferta() {
		return get("idOfferta");
	}
	
	public String getDescrizione() {
		return get("descrizione");
	}
	
	public String getTariffa() {
		return get("tariffa");
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
	
	public String getNumeroCommessa(){
		
		return get("numeroCommessa");
	}
}