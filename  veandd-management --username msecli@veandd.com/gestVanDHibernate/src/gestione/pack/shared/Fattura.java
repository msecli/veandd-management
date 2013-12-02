package gestione.pack.shared;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="fattura")
public class Fattura extends LightEntity implements IsSerializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_FATTURA")
	private int idFattura;
	
	@Column(name="idFoglioFatturazione")
	private int idFoglioFatturazione;

	private String aliquotaIva;

	private String condizioniPagamento;

	private String dataFatturazione;

	private String numeroFattura;

	private String statoElaborazione;

	private String statoPagamento;

	//bi-directional many-to-one association to AttivitaFatturata
	@OneToMany(mappedBy="fattura", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE,CascadeType.DELETE_ORPHAN})
	private Set<AttivitaFatturata> attivitaFatturatas;

	//bi-directional many-to-one association to Ordine
    @ManyToOne
	@JoinColumn(name="idOrdine")
	private Ordine ordine;

    public Fattura() {
    }

	public int getIdFattura() {
		return this.idFattura;
	}

	public void setIdFattura(int idFattura) {
		this.idFattura = idFattura;
	}

	public String getAliquotaIva() {
		return this.aliquotaIva;
	}

	public void setAliquotaIva(String aliquotaIva) {
		this.aliquotaIva = aliquotaIva;
	}

	public String getCondizioniPagamento() {
		return this.condizioniPagamento;
	}

	public void setCondizioniPagamento(String condizioniPagamento) {
		this.condizioniPagamento = condizioniPagamento;
	}

	public String getDataFatturazione() {
		return this.dataFatturazione;
	}

	public void setDataFatturazione(String dataFatturazione) {
		this.dataFatturazione = dataFatturazione;
	}

	public String getNumeroFattura() {
		return this.numeroFattura;
	}

	public void setNumeroFattura(String numeroFattura) {
		this.numeroFattura = numeroFattura;
	}

	public String getStatoElaborazione() {
		return this.statoElaborazione;
	}

	public void setStatoElaborazione(String statoElaborazione) {
		this.statoElaborazione = statoElaborazione;
	}

	public String getStatoPagamento() {
		return this.statoPagamento;
	}

	public void setStatoPagamento(String statoPagamento) {
		this.statoPagamento = statoPagamento;
	}

	public Set<AttivitaFatturata> getAttivitaFatturatas() {
		return this.attivitaFatturatas;
	}

	public void setAttivitaFatturatas(Set<AttivitaFatturata> attivitaFatturatas) {
		this.attivitaFatturatas = attivitaFatturatas;
	}
	
	public Ordine getOrdine() {
		return this.ordine;
	}

	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}

	public int getIdFoglioFatturazione() {
		return idFoglioFatturazione;
	}

	public void setIdFoglioFatturazione(int idFoglioFatturazione) {
		this.idFoglioFatturazione = idFoglioFatturazione;
	}
	
	
}
