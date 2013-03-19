package gestione.pack.client.model;


import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;



public class PersonaleModel extends BaseModel implements IsSerializable{
		
	private static final long serialVersionUID = 1L;


		public PersonaleModel() {
	    }
	    
	      
	    public PersonaleModel(int id_PERSONALE2, String nome2, String cognome2, String username2, String password2, String numeroBadge2,
				String ruolo2, String tipologiaOrario2, String tipologiaLavoratore2, String gruppoLavoro2,
				String costoOrario2, String costoStruttura2, String sede2, String oreDirette2, String oreIndirette2, String orePermessi2,
				String oreFerie2, String oreExFest2, String oreRecupero) {
	    	
	    	//impostazione della proprietà che dovrà essere uguale al momento della configurazione delle colonne
			set("idPersonale", id_PERSONALE2);
			set("nome", nome2);
	        set("cognome", cognome2);
	        set("username", username2);
	        set("password", password2);
	        set("nBadge",numeroBadge2);
	        set("ruolo", ruolo2);
	        set("tipologiaOrario", tipologiaOrario2);
	        set("tipologiaLavoratore", tipologiaLavoratore2);
	        set("gruppoLavoro", gruppoLavoro2);
	        set("costoOrario", costoOrario2);
	        set("costoStruttura", costoStruttura2);
	        set("sede", sede2);
	        set("oreDirette", oreDirette2);
	        set("oreIndirette", oreIndirette2);
	        set("permessi", orePermessi2);
	        set("ferie", oreFerie2);
	        set("ext", oreExFest2);
	        set("oreRecupero", oreRecupero); 
	        set("nomeCompleto", cognome2+" "+nome2);//usato nelle ListView
		}

	    
	    public String getNome() {
			return get("nome");
		}

	    public String getCognome() {
			return get("cognome");
		}
	    
	    public String getUsername() {
			return get("username");
		}

	    public String getTipologiaLavoratore(){
	    	
	    	return get("tipologiaLavoratore");
	    }
	    
	    public String getRuolo(){
	    	
	    	return get("ruolo");
	    }
}
