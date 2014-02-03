package gestione.pack.client.model;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;

public class RiepilogoRichiesteModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RiepilogoRichiesteModel(){
		
	}

	public RiepilogoRichiesteModel(int idRichiesta, int idHardware, int idUtente, String username, Date dataRichiesta, Date dataEvasioneRichiesta, String stato, String guasto){
		
		set("idRichiesta", idRichiesta);
		set("idHardware", idHardware);
		set("idUtente", idUtente);
		set("username", username);
		set("dataRichiesta", dataRichiesta);
		set("dataEvasioneRichiesta", dataEvasioneRichiesta);
		set("stato", stato);
		set("guasto",guasto);
		
	}
	
	
}
