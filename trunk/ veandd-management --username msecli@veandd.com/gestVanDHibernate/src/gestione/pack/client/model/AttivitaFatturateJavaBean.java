package gestione.pack.client.model;

public class AttivitaFatturateJavaBean {

	private String descrizione;
	private String importo;
	
	public AttivitaFatturateJavaBean(String descrizione, String importo){
		
		this.descrizione=descrizione;
		this.importo=importo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getImporto() {
		return importo;
	}
	
	
}
