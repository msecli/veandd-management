package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;


/**
 * The persistent class for the ordine database table.
 * 
 */
@Entity
@Table(name="ordine")
public class Ordine extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="NUMERO_ORDINE")
	private int numeroOrdine;

	private String codiceOrdine; //numero effettivo dell'ordine

    @Temporal( TemporalType.DATE)
	private Date dataChiusuraOrdine;

    @Temporal( TemporalType.DATE)
    @Column(name="dataFine", nullable=true)
	private Date dataFine;

    @Temporal( TemporalType.DATE)
    @Column(name="dataInizio", nullable=true)
	private Date dataInizio;

    @Temporal( TemporalType.DATE)
	private Date dataRichiestaOrdine;

	private String descrizioneAttivita;

    @Lob()
	private byte[] documentoOrdine;

	private String nomeDocumento;

	private String numRevisione;

	private String numRisorse;

	private String offertaAssociata;

	private String oreBudget;

	private String oreResidueBudget;

	private String statoAccettazione;

	private String statoOrdine;

	private String tariffaOraria;

	//bi-directional many-to-one association to Rda
	@ManyToOne
	@JoinColumn(name="NUMERO_RDA")
	private Rda rda;

	//bi-directional many-to-one association to Commessa
	@ManyToOne
	@JoinColumn(name="COD_COMMESSA")
	private Commessa commessa;

    public Ordine() {
    }

	public int getNumeroOrdine() {
		return this.numeroOrdine;
	}

	public void setNumeroOrdine(int numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
	}

	public String getCodiceOrdine() {
		return this.codiceOrdine;
	}

	public void setCodiceOrdine(String codiceOrdine) {
		this.codiceOrdine = codiceOrdine;
	}

	public Date getDataChiusuraOrdine() {
		return this.dataChiusuraOrdine;
	}

	public void setDataChiusuraOrdine(Date dataChiusuraOrdine) {
		this.dataChiusuraOrdine = dataChiusuraOrdine;
	}

	public Date getDataFine() {
		return this.dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public Date getDataInizio() {
		return this.dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataRichiestaOrdine() {
		return this.dataRichiestaOrdine;
	}

	public void setDataRichiestaOrdine(Date dataRichiestaOrdine) {
		this.dataRichiestaOrdine = dataRichiestaOrdine;
	}

	public String getDescrizioneAttivita() {
		return this.descrizioneAttivita;
	}

	public void setDescrizioneAttivita(String descrizioneAttivita) {
		this.descrizioneAttivita = descrizioneAttivita;
	}

	public byte[] getDocumentoOrdine() {
		return this.documentoOrdine;
	}

	public void setDocumentoOrdine(byte[] documentoOrdine) {
		this.documentoOrdine = documentoOrdine;
	}

	public String getNomeDocumento() {
		return this.nomeDocumento;
	}

	public void setNomeDocumento(String nomeDocumento) {
		this.nomeDocumento = nomeDocumento;
	}

	public String getNumRevisione() {
		return this.numRevisione;
	}

	public void setNumRevisione(String numRevisione) {
		this.numRevisione = numRevisione;
	}

	public String getNumRisorse() {
		return this.numRisorse;
	}

	public void setNumRisorse(String numRisorse) {
		this.numRisorse = numRisorse;
	}

	public String getOffertaAssociata() {
		return this.offertaAssociata;
	}

	public void setOffertaAssociata(String offertaAssociata) {
		this.offertaAssociata = offertaAssociata;
	}

	public String getOreBudget() {
		return this.oreBudget;
	}

	public void setOreBudget(String oreBudget) {
		this.oreBudget = oreBudget;
	}

	public String getOreResidueBudget() {
		return this.oreResidueBudget;
	}

	public void setOreResidueBudget(String oreResidueBudget) {
		this.oreResidueBudget = oreResidueBudget;
	}

	public String getStatoAccettazione() {
		return this.statoAccettazione;
	}

	public void setStatoAccettazione(String statoAccettazione) {
		this.statoAccettazione = statoAccettazione;
	}

	public String getStatoOrdine() {
		return this.statoOrdine;
	}

	public void setStatoOrdine(String statoOrdine) {
		this.statoOrdine = statoOrdine;
	}

	public String getTariffaOraria() {
		return this.tariffaOraria;
	}

	public void setTariffaOraria(String tariffaOraria) {
		this.tariffaOraria = tariffaOraria;
	}

	public Rda getRda() {
		return this.rda;
	}

	public void setRda(Rda rda) {
		this.rda = rda;
	}
	
	public Commessa getCommessa() {
		return this.commessa;
	}

	public void setCommessa(Commessa commessa) {
		this.commessa = commessa;
	}
	
}