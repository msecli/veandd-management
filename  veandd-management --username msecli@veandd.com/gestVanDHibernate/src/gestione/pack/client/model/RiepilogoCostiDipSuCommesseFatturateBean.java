package gestione.pack.client.model;

public class RiepilogoCostiDipSuCommesseFatturateBean {

	private int idCommessa;
	private String pm;
	private String numeroCommessa;
	private String commessa;
	private String estensione;
	private String attivita;
	private String dipendente; 
	private Float oreEseguite;
	private Float costoOrario; 
	private Float costoTotale;
	private Float importoFatturato;
	private Float importoScaricato;
	private Float margine;
	private Float rapporto;
	
	
	public RiepilogoCostiDipSuCommesseFatturateBean(int idCommessa, String pm, String numeroCommessa, String commessa, String estensione, String attivita, 
			String dipendente, Float oreEseguite, Float costoOrario, Float costoTotale, Float importoFatturato, Float importoScaricato,
			Float margine, Float rapporto){
		
		this.idCommessa=idCommessa;
		this.pm=pm;
		this.numeroCommessa=numeroCommessa;
		this.commessa=commessa;
		this.estensione=estensione;
		this.attivita=attivita;
		this.dipendente=dipendente;
		this.oreEseguite=oreEseguite;
		this.costoOrario=costoOrario;
		this.costoTotale=costoTotale;
		this.importoFatturato=importoFatturato;
		this.importoScaricato=importoScaricato;
		this.margine=margine;
		this.rapporto=rapporto;
		
	}


	/**
	 * @return the idCommessa
	 */
	public int getIdCommessa() {
		return idCommessa;
	}


	/**
	 * @return the pm
	 */
	public String getPm() {
		return pm;
	}


	/**
	 * @return the numeroCommessa
	 */
	public String getNumeroCommessa() {
		return numeroCommessa;
	}


	/**
	 * @return the commessa
	 */
	public String getCommessa() {
		return commessa;
	}


	/**
	 * @return the estensione
	 */
	public String getEstensione() {
		return estensione;
	}


	/**
	 * @return the attivita
	 */
	public String getAttivita() {
		return attivita;
	}


	/**
	 * @return the dipendente
	 */
	public String getDipendente() {
		return dipendente;
	}


	/**
	 * @return the oreEseguite
	 */
	public Float getOreEseguite() {
		return oreEseguite;
	}


	/**
	 * @return the costoOrario
	 */
	public Float getCostoOrario() {
		return costoOrario;
	}


	/**
	 * @return the costoTotale
	 */
	public Float getCostoTotale() {
		return costoTotale;
	}


	/**
	 * @return the importoFatturato
	 */
	public Float getImportoFatturato() {
		return importoFatturato;
	}


	/**
	 * @return the importoScaricato
	 */
	public Float getImportoScaricato() {
		return importoScaricato;
	}


	/**
	 * @return the margine
	 */
	public Float getMargine() {
		return margine;
	}


	/**
	 * @return the rapporto
	 */
	public Float getRapporto() {
		return rapporto;
	}
	
	
	
}
