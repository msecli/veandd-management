package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.Set;

/**
 * The persistent class for the commessa database table.
 * 
 */

@Entity
@Table(name="commessa")
public class Commessa extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_COMMESSA")
	private int codCommessa;

    @Temporal( TemporalType.DATE)
	private Date dataChiusura;

    @Temporal( TemporalType.DATE)
	private Date dataElaborazione;

	private String denominazioneAttivita;

	private String estensione;

	private String matricolaPM;

	private String note;

	private String numeroCommessa;

	private float oreLavoro;

	private float residuoOreLavoro;
	
	private String pclAttuale;
	
	private String salAttuale;
	
	private String tariffaSal; //serve quando non c'è un ordine(per sal o pcl)

	private String statoCommessa;

	private String tipoCommessa;
	
	private String ragioneSocialeCliente;
	
	private String escludiDaPa;
	
	
	//OLD
	//private Integer codiceOrdine;
	//bi-directional many-to-one association to Ordine
	/*@OneToMany(mappedBy="commessa", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<Ordine> ordines;*/
	
	//bi-directional many-to-one association to Attivita
	@OneToMany(mappedBy="commessa", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN})
	private Set<Attivita> attivitas;
	
	@OneToMany(mappedBy="commessa", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<FoglioFatturazione> foglioFatturaziones;

	//bi-directional many-to-one association to Costing
	@OneToMany(mappedBy="commessa", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<Costing> costings;
	
	@ManyToOne
	@JoinColumn(name="codiceOrdine")
	private Ordine ordine;
	
    public Commessa() {
    }

	public int getCodCommessa() {
		return this.codCommessa;
	}

	public void setCodCommessa(int codCommessa) {
		this.codCommessa = codCommessa;
	}

	public Date getDataChiusura() {
		return this.dataChiusura;
	}

	public void setDataChiusura(Date dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	public Date getDataElaborazione() {
		return this.dataElaborazione;
	}

	public void setDataElaborazione(Date dataElaborazione) {
		this.dataElaborazione = dataElaborazione;
	}

	public String getDenominazioneAttivita() {
		return this.denominazioneAttivita;
	}

	public void setDenominazioneAttivita(String denominazioneAttivita) {
		this.denominazioneAttivita = denominazioneAttivita;
	}

	public String getEstensione() {
		return this.estensione;
	}

	public void setEstensione(String estensione) {
		this.estensione = estensione;
	}

	public String getMatricolaPM() {
		return this.matricolaPM;
	}

	public void setMatricolaPM(String matricolaPM) {
		this.matricolaPM = matricolaPM;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNumeroCommessa() {
		return this.numeroCommessa;
	}

	public void setNumeroCommessa(String numeroCommessa) {
		this.numeroCommessa = numeroCommessa;
	}

	public float getOreLavoro() {
		return this.oreLavoro;
	}

	public void setOreLavoro(float oreLavoro) {
		this.oreLavoro = oreLavoro;
	}

	public float getResiduoOreLavoro() {
		return this.residuoOreLavoro;
	}

	public void setResiduoOreLavoro(float residuoOreLavoro) {
		this.residuoOreLavoro = residuoOreLavoro;
	}
	
	public void setTariffaSal(String tariffaPreOrdine) {
		this.tariffaSal = tariffaPreOrdine;
	}

	public String getTariffaSal() {
		return this.tariffaSal;
	}
	
	public String getStatoCommessa() {
		return this.statoCommessa;
	}

	public void setStatoCommessa(String statoCommessa) {
		this.statoCommessa = statoCommessa;
	}

	public String getTipoCommessa() {
		return this.tipoCommessa;
	}

	public void setTipoCommessa(String tipoCommessa) {
		this.tipoCommessa = tipoCommessa;
	}

	/*public Set<Ordine> getOrdines() {
		return this.ordines;
	}

	public void setOrdines(Set<Ordine> ordines) {
		this.ordines = ordines;
	}*/

	public Set<Attivita> getAttivitas() {
		return attivitas;
	}

	public void setAttivitas(Set<Attivita> attivitas) {
		this.attivitas = attivitas;
	}
	
	public Set<FoglioFatturazione> getFoglioFatturaziones() {
		return this.foglioFatturaziones;
	}

	public void setFoglioFatturaziones(Set<FoglioFatturazione> foglioFatturaziones) {
		this.foglioFatturaziones = foglioFatturaziones;
	}

	public String getPclAttuale() {
		return pclAttuale;
	}

	public void setPclAttuale(String pclAttuale) {
		this.pclAttuale = pclAttuale;
	}

	public String getSalAttuale() {
		return salAttuale;
	}

	public void setSalAttuale(String salAttuale) {
		this.salAttuale = salAttuale;
	}

	public Set<Costing> getCostings() {
		return costings;
	}

	public void setCostings(Set<Costing> costings) {
		this.costings = costings;
	}

	public String getRagioneSocialeCliente() {
		return ragioneSocialeCliente;
	}

	public void setRagioneSocialeCliente(String ragioneSocialeCliente) {
		this.ragioneSocialeCliente = ragioneSocialeCliente;
	}

	public Ordine getOrdine() {
		return ordine;
	}

	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}

	public String getEscludiDaPa() {
		return escludiDaPa;
	}

	public void setEscludiDaPa(String escludiDaPa) {
		this.escludiDaPa = escludiDaPa;
	}

	//Da togliere dopo il primo upload
	/*public Integer getCodiceOrdine() {
		return codiceOrdine;
	}

	public void setCodiceOrdine(Integer codiceOrdine) {
		this.codiceOrdine = codiceOrdine;
	}*/
	
	
}