package gestione.pack.client.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class FatturaModel extends BaseModel  implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<AttivitaFatturateModel> listaAttF;
	
	
	public FatturaModel(){
		
	}
	
	public FatturaModel(int idFattura, int idFoglioFatturazione, String descrizioneOrdine, String ragioneSociale, String indirizzo, String cap, String citta, String piva, 
			String codiceFornitore, String numeroFattura, Date dataFattura,
			String condizioni, String filiale, String iban, String numeroOrdine, String numeroOfferta, String lineaOrdine, String bem, String elementoWbs, String conto
			, String prCenter, String imponibile, String iva, String totaleIva, String totaleImporto, String nomeAzienda, String capitaleSociale, String sedeLegale,
			String sedeOperativa, String registroImprese, String rea){
		
		set("idFattura",idFattura);
		set("idFoglioFatturazione", idFoglioFatturazione);
		set("descrizione", descrizioneOrdine);
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
		set("nomeAzienda",nomeAzienda);
		set("capitaleSociale" , capitaleSociale);
		set("sedeLegale", sedeLegale);
		set("sedeOperativa",sedeOperativa);
		set("registroImprese", registroImprese);
		set("rea", rea);
				
	}

	public List<AttivitaFatturateModel> getListaAttF() {
		return listaAttF;
	}

	public void setListaAttF(List<AttivitaFatturateModel> listaAttF) {
		this.listaAttF = listaAttF;
	}
	
	
}
