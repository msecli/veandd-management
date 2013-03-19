package gestione.pack.shared;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Set;


/**
 * The persistent class for the rda database table.
 * 
 */
@Entity
@Table(name="rda")
public class Rda extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="NUMERO_RDA", nullable=false)
	private int numeroRda;

	@Column(name="codiceRDA")
	private String codiceRDA;

    @Lob()
	private byte[] documentoRDA;

	private String nomeDocumento;

	//bi-directional many-to-one association to Cliente
	@ManyToOne()
	@JoinColumn(name="COD_CLIENTE")
	private Cliente cliente;

	//bi-directional many-to-one association to Offerta
	@OneToMany(mappedBy="rda", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<Offerta> offertas;

	//bi-directional many-to-one association to Ordine
	@OneToMany(mappedBy="rda", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<Ordine> ordines;

    public Rda() {
    }

	public int getNumeroRda() {
		return this.numeroRda;
	}

	public void setNumeroRda(int numeroRda) {
		this.numeroRda = numeroRda;
	}

	public String getCodiceRDA() {
		return this.codiceRDA;
	}

	public void setCodiceRDA(String codiceRDA) {
		this.codiceRDA = codiceRDA;
	}

	public byte[] getDocumentoRDA() {
		return this.documentoRDA;
	}

	public void setDocumentoRDA(byte[] documentoRDA) {
		this.documentoRDA = documentoRDA;
	}

	public String getNomeDocumento() {
		return this.nomeDocumento;
	}

	public void setNomeDocumento(String nomeDocumento) {
		this.nomeDocumento = nomeDocumento;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public Set<Offerta> getOffertas() {
		return this.offertas;
	}

	public void setOffertas(Set<Offerta> offertas) {
		this.offertas = offertas;
	}
	
	public Set<Ordine> getOrdines() {
		return this.ordines;
	}

	public void setOrdines(Set<Ordine> ordines) {
		this.ordines = ordines;
	}
	
}