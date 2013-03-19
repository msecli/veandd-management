package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;


/**
 * The persistent class for the offerta database table.
 * 
 */
@Entity
@Table(name="offerta")
public class Offerta extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="NUMERO_OFFERTA")
	private int numeroOfferta;

	private String avanzamento;

	private String codiceOfferta;

    @Temporal( TemporalType.DATE)
    @Column(nullable=true)
	private Date dataRedazione;

	private String descrizioneTecnica;

    @Lob()
	private byte[] documentoOfferta;

	private String nomeDocumento;

	private byte numRevisione;

	private byte numRisorse;

	private String statoOfferta;

	private String tariffaOraria;

	private short tempistica;

	//bi-directional many-to-one association to Rda
	@ManyToOne
	@JoinColumn(name="NUMERO_RDA")
	private Rda rda;

    public Offerta() {
    }

	public int getNumeroOfferta() {
		return this.numeroOfferta;
	}

	public void setNumeroOfferta(int numeroOfferta) {
		this.numeroOfferta = numeroOfferta;
	}

	public String getAvanzamento() {
		return this.avanzamento;
	}

	public void setAvanzamento(String avanzamento) {
		this.avanzamento = avanzamento;
	}

	public String getCodiceOfferta() {
		return this.codiceOfferta;
	}

	public void setCodiceOfferta(String codiceOfferta) {
		this.codiceOfferta = codiceOfferta;
	}

	public Date getDataRedazione() {
		return this.dataRedazione;
	}

	public void setDataRedazione(Date dataRedazione) {
		this.dataRedazione = dataRedazione;
	}

	public String getDescrizioneTecnica() {
		return this.descrizioneTecnica;
	}

	public void setDescrizioneTecnica(String descrizioneTecnica) {
		this.descrizioneTecnica = descrizioneTecnica;
	}

	public byte[] getDocumentoOfferta() {
		return this.documentoOfferta;
	}

	public void setDocumentoOfferta(byte[] documentoOfferta) {
		this.documentoOfferta = documentoOfferta;
	}

	public String getNomeDocumento() {
		return this.nomeDocumento;
	}

	public void setNomeDocumento(String nomeDocumento) {
		this.nomeDocumento = nomeDocumento;
	}

	public byte getNumRevisione() {
		return this.numRevisione;
	}

	public void setNumRevisione(byte numRevisione) {
		this.numRevisione = numRevisione;
	}

	public byte getNumRisorse() {
		return this.numRisorse;
	}

	public void setNumRisorse(byte numRisorse) {
		this.numRisorse = numRisorse;
	}

	public String getStatoOfferta() {
		return this.statoOfferta;
	}

	public void setStatoOfferta(String statoOfferta) {
		this.statoOfferta = statoOfferta;
	}

	public String getImporto() {
		return this.tariffaOraria;
	}

	public void setImporto(String tariffaOraria) {
		this.tariffaOraria = tariffaOraria;
	}

	public short getTempistica() {
		return this.tempistica;
	}

	public void setTempistica(short tempistica) {
		this.tempistica = tempistica;
	}

	public Rda getRda() {
		return this.rda;
	}

	public void setRda(Rda rda) {
		this.rda = rda;
	}
	
}