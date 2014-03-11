package gestione.pack.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import net.sf.gilead.pojo.gwt.LightEntity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;



/* Ok senza dichiarazione di onetomany...........next step creare rda e aggiuungere annotazioni....e bitimu*/

@Entity
@Table(name="cliente")
public class Cliente extends LightEntity implements IsSerializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="COD_CLIENTE", nullable=false)
	private int codCliente;

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
	
	private String codFornitore;

	//bi-directional many-to-one association to Rda
	@OneToMany(mappedBy="cliente", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<Rda> rdas;
	
	//bi-directional many-to-one association to RiferimentiRtv
	@OneToMany(mappedBy="cliente", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<RiferimentiRtv> riferimentiRtvs;

    public Cliente() {
    }

	public int getCodCliente() {
		return this.codCliente;
	}

	public void setCodCliente(int codCliente) {
		this.codCliente = codCliente;
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

	public String getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}

	public Set<RiferimentiRtv> getRiferimentiRtvs() {
		return riferimentiRtvs;
	}

	public void setRiferimentiRtvs(Set<RiferimentiRtv> riferimentiRtvs) {
		this.riferimentiRtvs = riferimentiRtvs;
	}
	
}