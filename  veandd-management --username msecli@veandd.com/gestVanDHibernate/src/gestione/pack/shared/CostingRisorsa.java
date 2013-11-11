package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The persistent class for the costing_risorsa database table.
 * 
 */
@Entity
@Table(name="costing_risorsa")
public class CostingRisorsa extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_COSTING_RISORSA")
	private int idCostingRisorsa;

	private String costoConsulenza;

	private String costoHw;
	
	private String costoSw;

	private String costoOneri;

	private String costoOrarioRisorsa;

	private String costoStruttura;

	private String efficienza;

	private String lc;

	private String progetto;

	private String costoTrasferta;

	private String orePianificate;

	private String tariffa;

	//bi-directional many-to-one association to Costing
    @ManyToOne
	@JoinColumn(name="idCosting")
	private Costing costing;

	//bi-directional many-to-one association to Personale
    @ManyToOne
	@JoinColumn(name="idPersonale")
	private Personale personale;

    public CostingRisorsa() {
    }

	public int getIdCostingRisorsa() {
		return this.idCostingRisorsa;
	}

	public void setIdCostingRisorsa(int idCostingRisorsa) {
		this.idCostingRisorsa = idCostingRisorsa;
	}

	public String getCostoConsulenza() {
		return this.costoConsulenza;
	}

	public void setCostoConsulenza(String costoConsulenza) {
		this.costoConsulenza = costoConsulenza;
	}

	

	public String getCostoHw() {
		return costoHw;
	}

	public void setCostoHw(String costoHw) {
		this.costoHw = costoHw;
	}

	public String getCostoSw() {
		return costoSw;
	}

	public void setCostoSw(String costoSw) {
		this.costoSw = costoSw;
	}

	public String getCostoOneri() {
		return this.costoOneri;
	}

	public void setCostoOneri(String costoOneri) {
		this.costoOneri = costoOneri;
	}

	public String getCostoOrarioRisorsa() {
		return this.costoOrarioRisorsa;
	}

	public void setCostoOrarioRisorsa(String costoOrarioRisorsa) {
		this.costoOrarioRisorsa = costoOrarioRisorsa;
	}

	public String getCostoStruttura() {
		return this.costoStruttura;
	}

	public void setCostoStruttura(String costoStruttura) {
		this.costoStruttura = costoStruttura;
	}

	public String getEfficienza() {
		return this.efficienza;
	}

	public void setEfficienza(String efficienza) {
		this.efficienza = efficienza;
	}

	public String getLc() {
		return this.lc;
	}

	public void setLc(String lc) {
		this.lc = lc;
	}

	public String getDescProgetto() {
		return this.progetto;
	}

	public void setDescProgetto(String progetto) {
		this.progetto = progetto;
	}

	public String getCostoTrasferta() {
		return this.costoTrasferta;
	}

	public void setCostotrasferta(String costoTrasferta) {
		this.costoTrasferta = costoTrasferta;
	}

	public String getOrePianificate() {
		return this.orePianificate;
	}

	public void setOrePianificate(String orePianificate) {
		this.orePianificate = orePianificate;
	}

	public String getTariffa() {
		return this.tariffa;
	}

	public void setTariffa(String tariffa) {
		this.tariffa = tariffa;
	}

	public Costing getCosting() {
		return this.costing;
	}

	public void setCosting(Costing costing) {
		this.costing = costing;
	}
	
	public Personale getPersonale() {
		return this.personale;
	}

	public void setPersonale(Personale personale) {
		this.personale = personale;
	}
	
}
