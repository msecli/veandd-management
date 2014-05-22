package gestione.pack.client.model;

import java.util.List;

public class FatturaJavaBean {

	private String ragioneSociale; private String indirizzo; private String cap; private String citta; private String piva; private String codiceFornitore; private String numeroFattura; private String dataFattura;
	private String condizioni; private String filiale; private String iban; private String numeroOrdine; private String numeroOfferta; private String lineaOrdine; private String bem; private String elementoWbs; private String conto
	; private String prCenter; private String imponibile; private String iva; private String totaleIva; private String totaleImporto; 
	private String nomeAzienda; private String capitaleSociale; private String sedeLegale; private String sedeOperativa; private String registroImprese; private String rea; 
	private List<AttivitaFatturateJavaBean> listaA; private String annoFattura; private String testoNonImponibile;
	
	public FatturaJavaBean(String ragioneSociale, String indirizzo, String cap, String citta, String piva, String codiceFornitore, String numeroFattura, String dataFattura,
			String annoFattura, String condizioni, String filiale, String iban, String numeroOrdine, String numeroOfferta, String lineaOrdine, String bem, String elementoWbs, String conto
			, String prCenter, String imponibile, String iva, String totaleIva, String totaleImporto, List<AttivitaFatturateJavaBean> listaA, 
			String nomeAzienda, String capitaleSociale, String sedeLegale, String sedeOperativa, String registroImprese, String rea, String testoNonImponibile){
		
		this.ragioneSociale=ragioneSociale;
		this.indirizzo=indirizzo;
		this.cap=cap;
		this.citta=citta;
		this.piva=piva;
		this.codiceFornitore=codiceFornitore;
		this.numeroFattura=numeroFattura;
		this.dataFattura=dataFattura;
		this.annoFattura=annoFattura;
		this.condizioni=condizioni;
		this.filiale=filiale;
		this.iban=iban;
		this.numeroOrdine=numeroOrdine;
		this.numeroOfferta=numeroOfferta;
		this.lineaOrdine=lineaOrdine;
		this.bem=bem;
		this.elementoWbs=elementoWbs;
		this.conto=conto;
		this.prCenter=prCenter;
		this.imponibile=imponibile;
		this.iva=iva;
		this.totaleIva=totaleIva;
		this.totaleImporto=totaleImporto;	
		this.nomeAzienda=nomeAzienda;
		this.capitaleSociale=capitaleSociale;
		this.sedeLegale=sedeLegale;
		this.sedeOperativa=sedeOperativa;
		this.registroImprese= registroImprese;
		this.rea=rea;
		this.listaA=listaA;
		this.testoNonImponibile=testoNonImponibile;
	}
	
	

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public String getCap() {
		return cap;
	}

	public String getCitta() {
		return citta;
	}

	public String getPiva() {
		return piva;
	}

	public String getCodiceFornitore() {
		return codiceFornitore;
	}

	public String getNumeroFattura() {
		return numeroFattura;
	}

	public String getDataFattura() {
		return dataFattura;
	}

	public String getCondizioni() {
		return condizioni;
	}

	public String getFiliale() {
		return filiale;
	}

	public String getIban() {
		return iban;
	}

	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	public String getNumeroOfferta() {
		return numeroOfferta;
	}

	public String getLineaOrdine() {
		return lineaOrdine;
	}

	public String getBem() {
		return bem;
	}

	public String getElementoWbs() {
		return elementoWbs;
	}

	public String getConto() {
		return conto;
	}

	public String getPrCenter() {
		return prCenter;
	}

	public String getImponibile() {
		return imponibile;
	}

	public String getIva() {
		return iva;
	}

	public String getTotaleIva() {
		return totaleIva;
	}

	public String getTotaleImporto() {
		return totaleImporto;
	}

	public String getCapitaleSociale() {
		return capitaleSociale;
	}

	public String getNomeAzienda() {
		return nomeAzienda;
	}

	public String getSedeLegale() {
		return sedeLegale;
	}

	public String getSedeOperativa() {
		return sedeOperativa;
	}

	public String getRegistroImprese() {
		return registroImprese;
	}

	public String getRea() {
		return rea;
	}

	public List<AttivitaFatturateJavaBean> getListaA() {
		return listaA;
	}

	public String getAnnoFattura() {
		return annoFattura;
	}

	public String getTestoNonImponibile() {
		return testoNonImponibile;
	}

}
