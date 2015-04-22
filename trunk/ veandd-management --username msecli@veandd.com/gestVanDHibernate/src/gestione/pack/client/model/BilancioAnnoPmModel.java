package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class BilancioAnnoPmModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BilancioAnnoPmModel(){
		
	}
	
	public BilancioAnnoPmModel(int idBilancio, int idPm, String pm, String anno, String importo){
		
		set("idBilancio", idBilancio);
		set("idPm", idPm);
		set("anno", anno);
		set("importo", importo);
		
	}
	
	
}
