package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RtvJavaBean extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String numeroOrdine;
	private String numeroRtv;
	private String codiceFornitore;
	private String nomeResponsabile;
	private String dataOrdine;
	private String dataEmissione;
	private String importo;
	private String importoOrdine;
	private String importoAvanzamenti;
	private String meseRiferimento;
	private String attivita;
	private String statoLavori;
	private String cdcRichiedente;
	private String commessaCliente;
	private String ente;		
	private String dataInizioAttivita;
	private String dataFineAttivita;
	
	private String ragioneSociale;
	private String sezione;
	private String reparto;
	private String indirizzo;
	private String referente;
	private String telefono;
	private String email;	
		
	public RtvJavaBean(String numeroOrdine, String numeroRtv, String codiceFornitore, String nomeResponsabile,
			String  dataOrdine, String dataEmissione, String importo, String importoOrdine, String importoAvanzamenti, String meseRiferimento, String attivita, String statoLavori, 
			String cdcRichiedente, String commessaCliente, String ente, String dataInizioAttivita, String dataFineAttivita,
			String ragioneSociale, String sezione, String reparto, String indirizzo, String referente, String telefono, String email){
		
		this.numeroOrdine= numeroOrdine;
		this.numeroRtv=numeroRtv;
		this.codiceFornitore=codiceFornitore;
		this.nomeResponsabile=nomeResponsabile;
		this.dataOrdine=dataOrdine;
		this.dataEmissione=dataEmissione;
		this.importo=importo;
		this.importoOrdine=importoOrdine;
		this.importoAvanzamenti=importoAvanzamenti;
		this.meseRiferimento=meseRiferimento;
		this.attivita=attivita;
		this.statoLavori=statoLavori;
		this.cdcRichiedente=cdcRichiedente;
		this.commessaCliente=commessaCliente;
		this.ente=ente;
		this.dataInizioAttivita=dataInizioAttivita;
		this.dataFineAttivita=dataFineAttivita;
		
		//Riferimenti RTV
		this.ragioneSociale=ragioneSociale;
		this.sezione=sezione;
		this.reparto=reparto;
		this.referente=referente;
		this.indirizzo=indirizzo;
		this.telefono=telefono;
		this.email=email;		
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the numeroOrdine
	 */
	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	/**
	 * @return the numeroRtv
	 */
	public String getNumeroRtv() {
		return numeroRtv;
	}

	/**
	 * @return the codiceFornitore
	 */
	public String getCodiceFornitore() {
		return codiceFornitore;
	}

	/**
	 * @return the nomeResponsabile
	 */
	public String getNomeResponsabile() {
		return nomeResponsabile;
	}

	/**
	 * @return the dataOrdine
	 */
	public String getDataOrdine() {
		return dataOrdine;
	}

	/**
	 * @return the dataEmissione
	 */
	public String getDataEmissione() {
		return dataEmissione;
	}

	/**
	 * @return the importo
	 */
	public String getImporto() {
		return importo;
	}

	/**
	 * @return the importoOrdine
	 */
	public String getImportoOrdine() {
		return importoOrdine;
	}

	/**
	 * @return the importoAvanzamenti
	 */
	public String getImportoAvanzamenti() {
		return importoAvanzamenti;
	}

	/**
	 * @return the meseRiferimento
	 */
	public String getMeseRiferimento() {
		return meseRiferimento;
	}

	/**
	 * @return the attivita
	 */
	public String getAttivita() {
		return attivita;
	}

	/**
	 * @return the statoLavori
	 */
	public String getStatoLavori() {
		return statoLavori;
	}

	/**
	 * @return the cdcRichiedente
	 */
	public String getCdcRichiedente() {
		return cdcRichiedente;
	}

	/**
	 * @return the commessaCliente
	 */
	public String getCommessaCliente() {
		return commessaCliente;
	}

	/**
	 * @return the ente
	 */
	public String getEnte() {
		return ente;
	}

	/**
	 * @return the dataInizioAttivita
	 */
	public String getDataInizioAttivita() {
		return dataInizioAttivita;
	}

	/**
	 * @return the dataFineAttivita
	 */
	public String getDataFineAttivita() {
		return dataFineAttivita;
	}

	/**
	 * @return the ragioneSociale
	 */
	public String getRagioneSociale() {
		return ragioneSociale;
	}

	/**
	 * @return the sezione
	 */
	public String getSezione() {
		return sezione;
	}

	/**
	 * @return the reparto
	 */
	public String getReparto() {
		return reparto;
	}

	/**
	 * @return the referente
	 */
	public String getReferente() {
		return referente;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}	

	public String getIndirizzo() {
		return indirizzo;
	}
	
}
