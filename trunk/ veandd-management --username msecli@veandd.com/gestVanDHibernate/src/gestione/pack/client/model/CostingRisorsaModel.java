package gestione.pack.client.model;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;

public class CostingRisorsaModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CostingRisorsaModel(){
		
	}
	
	public CostingRisorsaModel(int idCostingRisorsa, String area, String cliente, String progetto, String commessa, int idRisorsa, String risorsa,
			Float costoOrario, Float costoStruttura,Float oreLavoro, Date dataInizio, Date dataFine, Float numeroSettimane, Float orePerSettimana,
			Float oreViaggio, Float giorniViaggio, /*Float diaria,*/ Float costoDiaria,Float costoTotOre, Float costoTrasferta, Float costoTotale, String efficienza, 
			Float oreDaFatturare, Float oreTrasferta, String tariffa, Float fatturatoTotale, Float ebit, Float ebitPerc, Date dataInizioAttivita, Date dataFineAttivita){
		
		set("idCostingRisorsa",idCostingRisorsa);
		set("area",area);	
		set("cliente",cliente);
		set("progetto",progetto);
		set("commessa",commessa);
		set("idRisorsa", idRisorsa);
		set("risorsa", risorsa);		
		set("costoOrario",costoOrario);
		set("costoStruttura", costoStruttura);
		set("oreLavoro", oreLavoro);
		set("dataInizio", dataInizio);
		set("dataFine", dataFine);
		set("numeroSettimane",numeroSettimane);
		set("orePerSettimana", orePerSettimana);
		set("oreViaggio", oreViaggio);
		set("giorniViaggio", giorniViaggio);
		set("costoDiaria",costoDiaria);
		set("costoTotOre",costoTotOre);
		set("costoTrasferta", costoTrasferta);
		set("costoTotale", costoTotale);
		set("efficienza", efficienza);
		set("oreDaFatturare", oreDaFatturare);
		set("oreTrasferta", oreTrasferta);
		set("tariffa",tariffa);
		set("fatturatoTotale", fatturatoTotale);
		set("ebit",ebit);
		set("ebitPerc",ebitPerc);
		set("dataInizioAttivita",dataInizioAttivita);
		set("dataFineAttivita", dataFineAttivita);
		
	}
}
