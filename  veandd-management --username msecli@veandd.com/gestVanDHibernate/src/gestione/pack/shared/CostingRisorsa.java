package gestione.pack.shared;

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
	private String efficienza;
	private String progetto;
	private String oreLavoro;
	private String tariffa;

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
}
