package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DettaglioTrasfertaModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DettaglioTrasfertaModel(){
		
	}

	public DettaglioTrasfertaModel(int ID_DETTAGLIO_TRASFERTA, Float numGiorni,  int numViaggi, Float oreViaggio, Float kmStradali, Float costoCarburante,
			Float diariaGiorno, Float costoAutostrada, char usoVetturaPropria, Float costoTreno, Float costoAereo, Float costoAlbergo, Float costoPranzo,
			Float costoCena, Float costoNoleggioAuto, Float costoTrasportiLocali, String costiVari){
		
		set("idDettaglioTrasferta", ID_DETTAGLIO_TRASFERTA);
		set("numGiorni", numGiorni);
		set("numViaggi", numViaggi);
		set("oreViaggio", oreViaggio);
		set("kmStradali", kmStradali);
		set("costoCarburante", costoCarburante);
		set("diariaGiorno", diariaGiorno);
		set("costoAutostrada", costoAutostrada);
		set("usoVetturaPropria", usoVetturaPropria);
		set("costoTreno", costoTreno);
		set("costoAereo", costoAereo);
		set("costoAlbergo", costoAlbergo);
		set("costoPranzo", costoPranzo);
		set("costoCena", costoCena);
		set("costoNoleggioAuto", costoNoleggioAuto);
		set("costotrasportiLocali", costoTrasportiLocali);
		set("costiVari", costiVari);
		
	}	
}
