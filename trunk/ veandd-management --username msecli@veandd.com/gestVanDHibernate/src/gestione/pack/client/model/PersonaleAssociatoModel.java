package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class PersonaleAssociatoModel extends BaseModel implements IsSerializable{

	private static final long serialVersionUID = 1L;

	public PersonaleAssociatoModel(){}
	
	public PersonaleAssociatoModel(int idAssociazione, String commessa, String nome, String cognome, String attivita){
		
		set("idAssociazione", idAssociazione);
		set("commessa", commessa);//viene settato a numCommessa+estensione
		set("nome", nome);
        set("cognome", cognome);	
        set("attivita", attivita);
	}
		
	public int getIdAssociazione(){
		
		return get("idAssociazione");
	}
	
	public String getNome() {
		return get("nome");
	}

    public String getCognome() {
		return get("cognome");
	}
    
    public String getCommessa(){
    	return get("commessa");
    }
	
}
