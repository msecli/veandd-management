package gestione.pack.shared;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="foglio_fatturazione")
public class FoglioFatturazione  extends LightEntity implements IsSerializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_FOGLIO_FATTURAZIONE")
	private int idFoglioFatturazione;

	private String meseCorrente;

	private String note;

	private String oreFatturare;

	private String oreEseguite;

	private String oreScaricate;

	private String PCLattuale;

	private String SALattuale;

	private String statoElaborazione;

	private String variazionePCL;

	private String variazioneSAL;
	
	private String tariffaUtilizzata;
	
	private String flagSalDaButtare;

	//bi-directional many-to-one association to Commessa
    @ManyToOne
	@JoinColumn(name="COD_COMMESSA")
	private Commessa commessa;

    public FoglioFatturazione() {
    }

	public int getIdFoglioFatturazione() {
		return this.idFoglioFatturazione;
	}

	public void setIdFoglioFatturazione(int idFoglioFatturazione) {
		this.idFoglioFatturazione = idFoglioFatturazione;
	}

	public String getMeseCorrente() {
		return this.meseCorrente;
	}

	public void setMeseCorrente(String meseCorrente) {
		this.meseCorrente = meseCorrente;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOreFatturare() {
		return this.oreFatturare;
	}

	public void setOreFatturare(String oreFatturare) {
		this.oreFatturare = oreFatturare;
	}

	public String getOreEseguite() {
		return this.oreEseguite;
	}

	public void setOreEseguite(String oreEseguite) {
		this.oreEseguite = oreEseguite;
	}

	public String getOreScaricate() {
		return this.oreScaricate;
	}

	public void setOreScaricate(String oreScaricate) {
		this.oreScaricate = oreScaricate;
	}

	public String getPCLattuale() {
		return this.PCLattuale;
	}

	public void setPCLattuale(String PCLattuale) {
		this.PCLattuale = PCLattuale;
	}

	public String getSALattuale() {
		return this.SALattuale;
	}

	public void setSALattuale(String SALattuale) {
		this.SALattuale = SALattuale;
	}

	public String getStatoElaborazione() {
		return this.statoElaborazione;
	}

	public void setStatoElaborazione(String statoElaborazione) {
		this.statoElaborazione = statoElaborazione;
	}

	public String getVariazionePCL() {
		return this.variazionePCL;
	}

	public void setVariazionePCL(String variazionePCL) {
		this.variazionePCL = variazionePCL;
	}

	public String getVariazioneSAL() {
		return this.variazioneSAL;
	}

	public void setVariazioneSAL(String variazioneSAL) {
		this.variazioneSAL = variazioneSAL;
	}

	public Commessa getCommessa() {
		return this.commessa;
	}

	public void setCommessa(Commessa commessa) {
		this.commessa = commessa;
	}

	public String getTariffaUtilizzata() {
		return tariffaUtilizzata;
	}

	public void setTariffaUtilizzata(String tariffaUtilizzata) {
		this.tariffaUtilizzata = tariffaUtilizzata;
	}

	public String getFlagSalDaButtare() {
		return flagSalDaButtare;
	}

	public void setFlagSalDaButtare(String flagSalDaButtare) {
		this.flagSalDaButtare = flagSalDaButtare;
	}
}
