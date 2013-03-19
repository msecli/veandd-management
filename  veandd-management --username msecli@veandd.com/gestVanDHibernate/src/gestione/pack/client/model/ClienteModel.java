package gestione.pack.client.model;

import java.util.HashSet;
import java.util.Set;

import gestione.pack.shared.Rda;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ClienteModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;

	private int idCliente;

	private String banca;

	private String cap;

	private String citta;

	private String codABI;

	private String codCAB;

	private String codCIN;

	private String codFiscale;

	private String condizioniPagamento;

	private String email;

	private String fax;

	private String indirizzo;

	private String partitaIVA;

	private String provincia;

	private String ragioneSociale;

	private String ramm;

	private String rgq;

	private String rua;

	private String rut;

	private String stato;

	private String telefono;

	private String valuta;
	
	private String codRaggr;

	private Set<Rda> rdas= new HashSet<Rda>();

	public ClienteModel(int codCliente2, String ragioneSociale2, String codFiscale2, String partitaIVA2, String codRaggr2,
			String citta2, String provincia2, String stato2, String indirizzo2,	String cap2, String telefono2, String fax2, String email2,
			String banca2, String valuta2, String codABI2, String codCAB2, String codCIN2, String condizioniPagamento2, Set<Rda> rdas2) {
		
		//impostazione della proprietà che dovrà essere uguale (il nome) al momento della configurazione delle colonne
		
		set("idCliente", codCliente2);
		set("ragioneSociale",ragioneSociale2);
		set("codFiscale",codFiscale2);
		set("partitaIVA",partitaIVA2);
		set("codRaggr", codRaggr2);
		set("citta", citta2);
		set("provincia",provincia2);
		set("stato",stato2);
		set("indirizzo",indirizzo2);
		set("cap", cap2);
		set("telefono", telefono2);
		set("fax",fax2);
		set("email",email2);
		set("banca", banca2);
		set("valuta", valuta2);
		set("codABI", codABI2);
		set("codCAB", codCAB2);
		set("codCIN", codCIN2);
		set("condizioniPagamento", condizioniPagamento2);
		//set("rDAS", rdas2);
	}


	public ClienteModel() {
		// TODO Auto-generated constructor stub
	}


	public int getCodCliente() {
		return this.idCliente;
	}

	public void setCodCliente(int codCliente) {
		this.idCliente = codCliente;
	}

	public String getBanca() {
		return this.banca;
	}

	public void setBanca(String banca) {
		this.banca = banca;
	}

	public String getCap() {
		return this.cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCitta() {
		return this.citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getCodABI() {
		return this.codABI;
	}

	public void setCodABI(String codABI) {
		this.codABI = codABI;
	}

	public String getCodCAB() {
		return this.codCAB;
	}

	public void setCodCAB(String codCAB) {
		this.codCAB = codCAB;
	}

	public String getCodCIN() {
		return this.codCIN;
	}

	public void setCodCIN(String codCIN) {
		this.codCIN = codCIN;
	}

	public String getCodFiscale() {
		return this.codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public String getCondizioniPagamento() {
		return this.condizioniPagamento;
	}

	public void setCondizioniPagamento(String condizioniPagamento) {
		this.condizioniPagamento = condizioniPagamento;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getPartitaIVA() {
		return this.partitaIVA;
	}

	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}

	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getCodRaggr() {
		return codRaggr;
	}

	public void setCodRaggr(String codRaggr) {
		this.codRaggr = codRaggr;
	}

	public String getRagioneSociale() {
		return this.ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getRamm() {
		return this.ramm;
	}

	public void setRamm(String ramm) {
		this.ramm = ramm;
	}

	public String getRgq() {
		return this.rgq;
	}

	public void setRgq(String rgq) {
		this.rgq = rgq;
	}

	public String getRua() {
		return this.rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getRut() {
		return this.rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getValuta() {
		return this.valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public Set<Rda> getRdas() {
		return this.rdas;
	}

	public void setRdas(Set<Rda> rdas) {
		this.rdas = rdas;
	}
	
}
