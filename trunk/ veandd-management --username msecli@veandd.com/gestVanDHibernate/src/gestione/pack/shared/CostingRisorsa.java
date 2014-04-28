package gestione.pack.shared;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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

	private String costoOrario;
	private String costoStruttura;
	private String costoOneri;
	private String costoHwSw;
	private String efficienza;
	private String progetto;
	private String oreLavoro;
	private String tariffa;
	@Temporal( TemporalType.DATE)
	private Date dataInizioAttivita;
	@Temporal( TemporalType.DATE)
	private Date dataFineAttivita;

	//bi-directional many-to-one association to Costing
    @ManyToOne
	@JoinColumn(name="idCosting")
	private Costing costing;

	//bi-directional many-to-one association to Personale
    @ManyToOne
	@JoinColumn(name="idPersonale")
	private Personale personale;

  //bi-directional many-to-one association to DettaglioOreGiornaliere
  	@OneToMany(mappedBy="costingRisorsa", fetch=FetchType.LAZY)
  	@Cascade({CascadeType.SAVE_UPDATE})
  	private Set<DettaglioTrasferta> dettaglioTrasfertas;
    
    public CostingRisorsa() {
    }

	public int getIdCostingRisorsa() {
		return this.idCostingRisorsa;
	}

	public void setIdCostingRisorsa(int idCostingRisorsa) {
		this.idCostingRisorsa = idCostingRisorsa;
	}	

	public String getCostoOrarioRisorsa() {
		return this.costoOrario;
	}

	public void setCostoOrarioRisorsa(String costoOrarioRisorsa) {
		this.costoOrario = costoOrarioRisorsa;
	}

	public String getEfficienza() {
		return this.efficienza;
	}

	public void setEfficienza(String efficienza) {
		this.efficienza = efficienza;
	}

	public String getDescProgetto() {
		return this.progetto;
	}

	public void setDescProgetto(String progetto) {
		this.progetto = progetto;
	}


	public String getOreLavoro() {
		return this.oreLavoro;
	}

	public void setOreLavoro(String orePianificate) {
		this.oreLavoro = orePianificate;
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

	public Set<DettaglioTrasferta> getDettaglioTrasfertas() {
		return dettaglioTrasfertas;
	}

	public void setDettaglioTrasfertas(Set<DettaglioTrasferta> dettaglioTrasfertas) {
		this.dettaglioTrasfertas = dettaglioTrasfertas;
	}

	/**
	 * @return the costoOrario
	 */
	public String getCostoOrario() {
		return costoOrario;
	}

	/**
	 * @param costoOrario the costoOrario to set
	 */
	public void setCostoOrario(String costoOrario) {
		this.costoOrario = costoOrario;
	}

	/**
	 * @return the costoStruttura
	 */
	public String getCostoStruttura() {
		return costoStruttura;
	}

	/**
	 * @param costoStruttura the costoStruttura to set
	 */
	public void setCostoStruttura(String costoStruttura) {
		this.costoStruttura = costoStruttura;
	}

	/**
	 * @return the costoOneri
	 */
	public String getCostoOneri() {
		return costoOneri;
	}

	/**
	 * @param costoOneri the costoOneri to set
	 */
	public void setCostoOneri(String costoOneri) {
		this.costoOneri = costoOneri;
	}

	/**
	 * @return the costoHwSw
	 */
	public String getCostoHwSw() {
		return costoHwSw;
	}

	/**
	 * @param costoHwSw the costoHwSw to set
	 */
	public void setCostoHwSw(String costoHwSw) {
		this.costoHwSw = costoHwSw;
	}

	/**
	 * @return the progetto
	 */
	public String getProgetto() {
		return progetto;
	}

	/**
	 * @param progetto the progetto to set
	 */
	public void setProgetto(String progetto) {
		this.progetto = progetto;
	}

	public Date getDataInizioAttivita() {
		return dataInizioAttivita;
	}

	public void setDataInizioAttivita(Date dataInizioAttivita) {
		this.dataInizioAttivita = dataInizioAttivita;
	}

	public Date getDataFineAttivita() {
		return dataFineAttivita;
	}

	public void setDataFineAttivita(Date dataFineAttivita) {
		this.dataFineAttivita = dataFineAttivita;
	}	
	
	
}
