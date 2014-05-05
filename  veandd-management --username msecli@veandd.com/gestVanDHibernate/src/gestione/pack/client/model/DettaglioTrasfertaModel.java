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

	public DettaglioTrasfertaModel(int ID_DETTAGLIO_TRASFERTA, Float numGiorni, Float numeroNotti,  Float numViaggi, Float oreViaggio, Float kmStradali, Float costoCarburante,
			Float diariaGiorno, Float costoAutostrada, boolean usoVetturaPropria, Float costoTreno, Float costoAereo, Float costoAlbergo, Float costoPranzo,
			Float costoCena, Float costoNoleggioAuto, Float costoTrasportiLocali, Float costiVari){
		
		set("idDettaglioTrasferta", ID_DETTAGLIO_TRASFERTA);
		set("numGiorni", numGiorni);
		set("numeroNotti", numeroNotti);
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
		set("costoTrasportiLocali", costoTrasportiLocali);
		set("costiVari", costiVari);
		
	}
}
