package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RdaModel extends BaseModel  implements IsSerializable{

private static final long serialVersionUID = 1L;
	

    public RdaModel() {
    }
    
    public RdaModel(int numeroRda2, int cliente2, String codiceRda, String ragioneSociale)
    {
    	set("numeroRda",numeroRda2); //id RDA su DB
    	set("idCliente", cliente2);
    	set("codiceRda", codiceRda); // numero effettivo di RDA
    	set("ragioneSociale", ragioneSociale);
    }

	public int getNumeroRda() {
		return get("numeroRda");
	}

	public int getCliente() {
		return get("idCliente");
	}
	
	public String getCodiceRda() {
		return get("codiceRda");
	}
	
}
