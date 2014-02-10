package gestione.pack.client.model;

public class DatiFatturazioneMeseJavaBean {

	private String pm;
	private String commessa;
	private String cliente;
	private String numeroOrdine;
	private String oggettoAttivita;
	private Float tariffaOraria;
	private Float oreEseguite;
	private Float oreFatturate;
	private Float variazioneSal;
	private Float importoSal;
	private Float variazionePcl;
	private Float importoPcl;
	private Float importo;
	private Float importoEffettivo;
	private Float oreScaricate;
	private Float margine;
	private String note;
	
	public  DatiFatturazioneMeseJavaBean(String pm, String commessa, String cliente, String numeroOrdine, String oggettoAttivita, Float oreEseguite, 
			Float oreFatturate, Float tariffaOraria, Float importo, Float importoEffettivo, Float variazioneSal, Float importoSal, Float variazionePcl,
			Float importoPcl, Float oreScaricate, Float margine, String note){
		
		this.pm=pm;
		this.commessa= commessa;
		this.cliente=cliente;
		this.numeroOrdine=numeroOrdine;
		this.oggettoAttivita=oggettoAttivita;
		this.tariffaOraria=tariffaOraria;
		this.oreEseguite=oreEseguite;
		this.oreFatturate=oreFatturate;
		this.variazioneSal=variazioneSal;
		this.importoSal=importoSal;
		this.variazionePcl=variazionePcl;
		this.importoPcl=importoPcl;
		this.importo=importo;
		this.importoEffettivo= importoEffettivo;
		this.oreScaricate=oreScaricate;
		this.margine=margine;
		this.note=note;
	}
	
	
	public String getNumeroOrdine(){
		return numeroOrdine;
	}	
	public String getPm() {
		return pm;
	}
	public void setPm(String pm) {
		this.pm = pm;
	}
	public String getCommessa() {
		return commessa;
	}
	public void setCommessa(String commessa) {
		this.commessa = commessa;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getOggettoAttivita() {
		return oggettoAttivita;
	}
	public void setOggettoAttivita(String oggettoAttivita) {
		this.oggettoAttivita = oggettoAttivita;
	}
	public Float getTariffaOraria() {
		return tariffaOraria;
	}
	public void setTariffaOraria(Float tariffaOraria) {
		this.tariffaOraria = tariffaOraria;
	}
	public Float getOreEseguite() {
		return oreEseguite;
	}
	public void setOreEseguite(Float oreEseguite) {
		this.oreEseguite = oreEseguite;
	}
	public Float getOreFatturate() {
		return oreFatturate;
	}
	public void setOreFatturate(Float oreFatturate) {
		this.oreFatturate = oreFatturate;
	}
	public Float getVariazioneSal() {
		return variazioneSal;
	}
	public void setVariazioneSal(Float variazioneSal) {
		this.variazioneSal = variazioneSal;
	}
	public Float getImportoSal() {
		return importoSal;
	}
	public void setImportoSal(Float importoSal) {
		this.importoSal = importoSal;
	}
	public Float getVariazionePcl() {
		return variazionePcl;
	}
	public void setVariazionePcl(Float variazionePcl) {
		this.variazionePcl = variazionePcl;
	}
	public Float getImportoPcl() {
		return importoPcl;
	}
	public void setImportoPcl(Float importoPcl) {
		this.importoPcl = importoPcl;
	}
	public Float getImporto() {
		return importo;
	}
	public void setImporto(Float importo) {
		this.importo = importo;
	}
	
	public Float getImportoEffettivo() {
		return importoEffettivo;
	}
	
	public Float getOreScaricate() {
		return oreScaricate;
	}
	public void setOreScaricate(Float oreScaricate) {
		this.oreScaricate = oreScaricate;
	}
	public Float getMargine() {
		return margine;
	}
	public void setMargine(Float margine) {
		this.margine = margine;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}	
}
