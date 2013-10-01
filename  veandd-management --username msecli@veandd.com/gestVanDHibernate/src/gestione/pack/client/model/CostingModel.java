package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class CostingModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CostingModel(){
		
	}
	
	//anche per il cliente salvo l'id
	public CostingModel(int idCosting, int cliente, String numeroCommessa, String descrizione, String numeroRevisione){
		
		set("idCosting",idCosting);
		set("cliente", cliente);
		set("commessa", numeroCommessa);
		set("descrizione", descrizione);
		set("numeroRevisione", numeroRevisione);		
	}
	
}
