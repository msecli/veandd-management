package gestione.pack.client.model;

public class RiepilogoCostiDipendentiBean {

	private int idDip;
	private String nome;
	private String costoAnnuo;
	private String costoOrario;
	private String gruppoLavoro;
	private String costoStruttura;
	private String costoOneri;
	private String costoHw;
	private String costoSw;
	private String costoHwSw;
	private String costoOrarioTotale; 
	private String tipoOrario;
	private String orePreviste;
	private String costoTrasferta;
	
	
	public RiepilogoCostiDipendentiBean(int idDip, String nome, String tipoOrario, String orePreviste, String costoAnnuo, 
			String costoOrario, String costoTrasferta,String gruppoLavoro, String costoStruttura, String costoOneri, 
			String costoSw, String costoHw, String costoHwSw, String costoOrarioTotale){
		
		this.idDip=idDip;
		this.nome= nome;
		this.costoAnnuo=costoAnnuo;
		this.costoOrario= costoOrario;
		this.gruppoLavoro=gruppoLavoro;
		this.costoStruttura= costoStruttura;
		this.costoOneri=costoOneri;
		this.tipoOrario=tipoOrario;
		this.orePreviste= orePreviste;
		this.costoHw=costoHw;
		this.costoSw=costoSw;
		this.costoHwSw=costoHwSw;
		this.costoTrasferta=costoTrasferta;
		this.costoOrarioTotale=costoOrarioTotale;
		
	}


	/**
	 * @return the idDip
	 */
	public int getIdDip() {
		return idDip;
	}


	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}


	/**
	 * @return the costoAnnuo
	 */
	public String getCostoAnnuo() {
		return costoAnnuo;
	}


	/**
	 * @return the costoOrario
	 */
	public String getCostoOrario() {
		return costoOrario;
	}


	/**
	 * @return the gruppoLavoro
	 */
	public String getGruppoLavoro() {
		return gruppoLavoro;
	}


	/**
	 * @return the costoStruttura
	 */
	public String getCostoStruttura() {
		return costoStruttura;
	}


	/**
	 * @return the costoOneri
	 */
	public String getCostoOneri() {
		return costoOneri;
	}


	/**
	 * @return the costoHw
	 */
	public String getCostoHw() {
		return costoHw;
	}


	/**
	 * @return the costoSw
	 */
	public String getCostoSw() {
		return costoSw;
	}


	/**
	 * @return the costoHwSw
	 */
	public String getCostoHwSw() {
		return costoHwSw;
	}


	/**
	 * @return the costoOrarioTotale
	 */
	public String getCostoOrarioTotale() {
		return costoOrarioTotale;
	}


	/**
	 * @return the tipoOrario
	 */
	public String getTipoOrario() {
		return tipoOrario;
	}


	/**
	 * @return the orePreviste
	 */
	public String getOrePreviste() {
		return orePreviste;
	}


	/**
	 * @return the costoTrasferta
	 */
	public String getCostoTrasferta() {
		return costoTrasferta;
	}
	
	
	

	}
