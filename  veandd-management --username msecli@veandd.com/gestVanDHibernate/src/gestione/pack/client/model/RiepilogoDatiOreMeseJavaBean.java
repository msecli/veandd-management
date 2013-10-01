package gestione.pack.client.model;
/**
 * The persistent class for the dettaglio_ore_giornaliere database table.
 * 
 */

public class RiepilogoDatiOreMeseJavaBean {
	
	private String username;
    
	private String giorno;
	
	private String totGiorno;
	
	private String oreViaggio;
	
	private String deltaOreViaggio;

	private String oreTotali;
		
	private String oreRecupero;

	private String oreFerie;

	private String orePermesso;

	private String oreStraordinario;
	
	private String deltaGiornaliero;

	private String giustificativo;

	private String noteAggiuntive;
	
	
	public RiepilogoDatiOreMeseJavaBean( String username, String giorno, String totGiorno, String oreViaggio, String deltaOreViaggio, 
			String oreTotali,String oreRecupero, String oreFerie, String orePermesso, String oreStraordinario, String deltaGiornaliero,
			String giustificativo, String noteAggiuntive){
		
		this.username=username;
		this.giorno=giorno;
		this.totGiorno=totGiorno;
		this.oreViaggio=oreViaggio;
		this.deltaOreViaggio= deltaOreViaggio;
		this.oreTotali= oreTotali;
		this.oreRecupero= oreRecupero;
		this.oreFerie=oreFerie;
		this.orePermesso=orePermesso;
		this.oreStraordinario=oreStraordinario;
		this.deltaGiornaliero=deltaGiornaliero;
		this.giustificativo=giustificativo;
		this.noteAggiuntive=noteAggiuntive;
		
	}

	
	public String getUsername() {
		return username;
	}

	public String getGiorno() {
		return giorno;
	}

	public String getTotGiorno() {
		return totGiorno;
	}

	public String getOreViaggio() {
		return oreViaggio;
	}

	public String getDeltaOreViaggio() {
		return deltaOreViaggio;
	}

	public String getOreTotali() {
		return oreTotali;
	}

	public String getOreRecupero() {
		return oreRecupero;
	}

	public String getOreFerie() {
		return oreFerie;
	}

	public String getOrePermesso() {
		return orePermesso;
	}

	public String getOreStraordinario() {
		return oreStraordinario;
	}

	public String getDeltaGiornaliero() {
		return deltaGiornaliero;
	}

	public String getGiustificativo() {
		return giustificativo;
	}

	public String getNoteAggiuntive() {
		return noteAggiuntive;
	}

  	
	
	
}
