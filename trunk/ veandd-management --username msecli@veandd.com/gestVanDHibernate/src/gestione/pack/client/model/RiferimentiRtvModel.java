package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiferimentiRtvModel extends BaseModel implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RiferimentiRtvModel(){
		
	}
	
	public RiferimentiRtvModel(int idRiferimento, int idCliente, String ragioneSociale, String sezione, String reparto, String indirizzo,
			String referente, String telefono, String email){
		
		set("idRiferimento", idRiferimento);
		set("idCliente",idCliente);
		set("ragioneSociale",ragioneSociale);
		set("sezione", sezione);
		set("reparto",reparto);
		set("indirizzo", indirizzo);
		set("referente", referente);
		set("telefono", telefono);
		set("email",email);		
		
	}
	
}
