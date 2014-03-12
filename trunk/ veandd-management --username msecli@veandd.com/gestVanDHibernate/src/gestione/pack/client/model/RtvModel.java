package gestione.pack.client.model;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RtvModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RtvModel(){
		
	}
	
	public RtvModel(int idRtv, int idOrdine, String numeroOrdine, String numeroRtv, String codiceFornitore, String nomeResponsabile,
			Date dataOrdine, Date dataEmissione, float importo, String importoOrdine, String importoAvanzamenti, String meseRiferimento, String attivita, String statoLavori, 
			String cdcRichiedente, String commessaCliente, String ente, Date dataInizioAttivita, Date dataFineAttivita){
		set("idRtv",idRtv);
		set("idOrdine",idOrdine);
		set("numeroOrdine", numeroOrdine);
		set("numeroRtv", numeroRtv);
		set("codiceFornitore", codiceFornitore);
		set("nomeResponsabile", nomeResponsabile);
		set("dataOrdine", dataOrdine);
		set("dataEmissione", dataEmissione);
		set("importo",importo);
		set("importoOrdine", importoOrdine);
		set("importoAvanzamenti", importoAvanzamenti);
		set("meseRiferimento",meseRiferimento);
		set("attivita",attivita);
		set("statoLavori",statoLavori);
		set("cdcRichiedente", cdcRichiedente);
		set("commessaCliente", commessaCliente);
		set("ente",ente);		
		set("dataInizioAttivita", dataInizioAttivita);
		set("dataFineAttivita", dataFineAttivita);
	}
	
	
	
}
