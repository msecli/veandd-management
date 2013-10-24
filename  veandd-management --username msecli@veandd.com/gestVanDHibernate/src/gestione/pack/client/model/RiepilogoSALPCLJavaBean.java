package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoSALPCLJavaBean extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pm;
	private String numeroCommessa;	
	private String estensione;
	private String cliente;
	private String attivita;
	private Float precedente;
	private Float variazione;
	private Float attuale;
	private String tariffa;
	private Float importoComplessivo;
	private Float oreEseguite;
	private Float margine;
	private Float importoMese;
	
	public RiepilogoSALPCLJavaBean(String pm, String numeroCommessa, String estensione, String cliente, String attivita,
			Float precedente, Float variazione, Float attuale, String tariffa, Float importoComplessivo, Float oreEseguite,
			Float margine, Float importoMese){
		
		this.pm=pm;
		this.numeroCommessa=numeroCommessa;
		this.estensione=estensione;
		this.cliente=cliente;
		this.attivita=attivita;
		this.precedente=precedente;
		this.variazione=variazione;
		this.attuale=attuale;
		this.tariffa=tariffa;
		this.importoComplessivo=importoComplessivo;
		this.oreEseguite=oreEseguite;
		this.margine=margine;
		this.importoMese=importoMese;	
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getNumeroCommessa() {
		return numeroCommessa;
	}

	public void setNumeroCommessa(String numeroCommessa) {
		this.numeroCommessa = numeroCommessa;
	}

	public String getEstensione() {
		return estensione;
	}

	public void setEstensione(String estensione) {
		this.estensione = estensione;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getAttivita() {
		return attivita;
	}

	public void setAttivita(String attivita) {
		this.attivita = attivita;
	}

	public Float getPrecedente() {
		return precedente;
	}

	public void setPrecedente(Float precedente) {
		this.precedente = precedente;
	}

	public Float getVariazione() {
		return variazione;
	}

	public void setVariazione(Float variazione) {
		this.variazione = variazione;
	}

	public Float getAttuale() {
		return attuale;
	}

	public void setAttuale(Float attuale) {
		this.attuale = attuale;
	}

	public String getTariffa() {
		return tariffa;
	}

	public void setTariffa(String tariffa) {
		this.tariffa = tariffa;
	}

	public Float getImportoComplessivo() {
		return importoComplessivo;
	}

	public void setImportoComplessivo(Float importoComplessivo) {
		this.importoComplessivo = importoComplessivo;
	}

	public Float getOreEseguite() {
		return oreEseguite;
	}

	public void setOreEseguite(Float oreEseguite) {
		this.oreEseguite = oreEseguite;
	}

	public Float getMargine() {
		return margine;
	}

	public void setMargine(Float margine) {
		this.margine = margine;
	}

	public Float getImportoMese() {
		return importoMese;
	}

	public void setImportoMese(Float importoMese) {
		this.importoMese = importoMese;
	}
	
}
