package gestione.pack.client.model;

public class RiepilogoMensileDatiIntervalliCommesseJavaBean {

	private String username;
	private String mese;
	private String numeroCommessa;
	private String giorno;
	private String oreLavoro;
	private String oreViaggio;
	private String oreTotali;
	
	public RiepilogoMensileDatiIntervalliCommesseJavaBean(String username, String mese, String numeroCommessa, String giorno, String oreLavoro
			,String oreViaggio, String oreTotali){
		
		this.username=username;
		this.mese=mese;
		this.numeroCommessa=numeroCommessa;
		this.giorno=giorno;
		this.oreLavoro=oreLavoro;
		this.oreViaggio=oreViaggio;
		this.oreTotali=oreTotali;
		
	}

	public String getUsername() {
		return username;
	}

	public String getMese() {
		return mese;
	}

	public String getNumeroCommessa() {
		return numeroCommessa;
	}

	public String getGiorno() {
		return giorno;
	}

	public String getOreLavoro() {
		return oreLavoro;
	}

	public String getOreViaggio() {
		return oreViaggio;
	}

	public String getOreTotali() {
		return oreTotali;
	}
	
}
