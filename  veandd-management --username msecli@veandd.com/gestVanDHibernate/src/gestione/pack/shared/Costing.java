package gestione.pack.shared;

import java.io.Serializable;
import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Set;


/**
 * The persistent class for the costing database table.
 * 
 */
@Entity
public class Costing  extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idCosting;

	private int cliente;

	private String descrizioneProgetto;
	
	private String numerorevisione;

	//bi-directional many-to-one association to Commessa
    @ManyToOne
	@JoinColumn(name="codCommessa")
	private Commessa commessa;

	//bi-directional many-to-one association to CostingRisorsa
	@OneToMany(mappedBy="costing")
	private Set<CostingRisorsa> costingRisorsas;

    public Costing() {
    }

	public int getIdCosting() {
		return this.idCosting;
	}

	public void setIdCosting(int idCosting) {
		this.idCosting = idCosting;
	}

	public int getCliente() {
		return this.cliente;
	}

	public void setCliente(int cliente) {
		this.cliente = cliente;
	}

	public String getDescrizioneProgetto() {
		return this.descrizioneProgetto;
	}

	public void setDescrizioneProgetto(String descrizioneProgetto) {
		this.descrizioneProgetto = descrizioneProgetto;
	}

	public Commessa getCommessa() {
		return this.commessa;
	}

	public void setCommessa(Commessa commessa) {
		this.commessa = commessa;
	}
	
	public Set<CostingRisorsa> getCostingRisorsas() {
		return this.costingRisorsas;
	}

	public void setCostingRisorsas(Set<CostingRisorsa> costingRisorsas) {
		this.costingRisorsas = costingRisorsas;
	}

	public String getNumerorevisione() {
		return numerorevisione;
	}

	public void setNumerorevisione(String numerorevisione) {
		this.numerorevisione = numerorevisione;
	}
	
}