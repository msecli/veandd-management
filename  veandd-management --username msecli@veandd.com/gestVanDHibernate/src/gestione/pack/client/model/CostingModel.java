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
	public CostingModel(int idCosting, String cliente, String area, String numeroCommessa, String descrizione, String numeroRevisione, String stato){
		
		set("idCosting",idCosting);
		set("cliente", cliente);
		set("area",area);
		set("commessa", numeroCommessa);
		set("descrizione", descrizione);
		set("numeroRevisione", numeroRevisione);		
		set("displayField", cliente+"; " +area +"; " +numeroCommessa+"; "+descrizione);
		set("stato", stato);
	}
	
}
