package gestione.pack.client.model;


import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;

public class FatturaModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FatturaModel(){
		
	}
	
	public FatturaModel(int idFattura,String ragioneSociale, String indirizzo, String cap, String citta, String piva, String codiceFornitore, String numeroFattura, String dataFattura,
			String condizioni, String filiale, String iban, String numeroOrdine, String numeroOfferta, String lineaOrdine, String bem, String elementoWbs, String conto
			, String prCenter, String imponibile, String iva, String totaleIva, String totaleImporto, List<AttivitaFatturateModel> listaAttivita){
		
		set("idFattura",idFattura);
		set("ragioneSociale",ragioneSociale);
		set("indirizzo",indirizzo);
		set("cap",cap);
		set("citta",citta);
		set("piva",piva);
		set("codiceFornitore",codiceFornitore);
		set("numeroFattura", numeroFattura);
		set("dataFattura",dataFattura);
		set("condizioni",condizioni);
		set("filiale",filiale);
		set("iban",iban);
		set("numeroOrdine",numeroOrdine);
		set("numeroOfferta",numeroOfferta);
		set("lineaOrdine",lineaOrdine);
		set("bem",bem);
		set("elementoWbs",elementoWbs);
		set("conto",conto);
		set("prCenter",prCenter);
		set("imponibile",imponibile);
		set("iva",iva);
		set("totaleIva",totaleIva);
		set("totaleImporto",totaleImporto);
		set("listaAttivita",listaAttivita);
		
	}
}
