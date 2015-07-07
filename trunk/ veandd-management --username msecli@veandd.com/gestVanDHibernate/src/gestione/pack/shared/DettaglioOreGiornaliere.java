package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the dettaglio_ore_giornaliere database table.
 * 
 */
@Entity
@Table(name="dettaglio_ore_giornaliere")
public class DettaglioOreGiornaliere extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_DETTAGLIO_ORE_GIORNALIERE")
	private int idDettaglioOreGiornaliere;

	private String deltaOreGiorno;

    @Temporal( TemporalType.DATE)
	private Date giornoRiferimento;

	private String giustificativo;

	private String oreAssenzeRecupero;

	private String oreViaggio;
	
	private String deltaOreViaggio;

	private String oreFerie;

	private String orePermesso;

	private String oreStraordinario;

	private String oreAbbuono;
	
	private String statoRevisione;

	private String totaleOreGiorno;
	
	private String noteAggiuntive;
	
	private String fermoMacchina;

	//bi-directional many-to-one association to DettaglioIntervalliCommesse
	@OneToMany(mappedBy="dettaglioOreGiornaliere", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE,CascadeType.DELETE_ORPHAN})
	private Set<DettaglioIntervalliCommesse> dettaglioIntervalliCommesses;

	//bi-directional many-to-one association to DettaglioIntervalliIU
	@OneToMany(mappedBy="dettaglioOreGiornaliere", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE,CascadeType.DELETE_ORPHAN})
	private Set<DettaglioIntervalliIU> dettaglioIntervalliIUs;

	//bi-directional many-to-one association to FoglioOreMese
	@ManyToOne
	@JoinColumn(name="ID_FOGLIO_ORE")
	private FoglioOreMese foglioOreMese;

    public DettaglioOreGiornaliere() {
    }

	public int getIdDettaglioOreGiornaliere() {
		return this.idDettaglioOreGiornaliere;
	}

	public void setIdDettaglioOreGiornaliere(int idDettaglioOreGiornaliere) {
		this.idDettaglioOreGiornaliere = idDettaglioOreGiornaliere;
	}

	public String getDeltaOreGiorno() {
		return this.deltaOreGiorno;
	}

	public void setDeltaOreGiorno(String deltaOreGiorno) {
		this.deltaOreGiorno = deltaOreGiorno;
	}

	public Date getGiornoRiferimento() {
		return this.giornoRiferimento;
	}

	public void setGiornoRiferimento(Date giornoRiferimento) {
		this.giornoRiferimento = giornoRiferimento;
	}

	public String getGiustificativo() {
		return this.giustificativo;
	}

	public void setGiustificativo(String giustificativo) {
		this.giustificativo = giustificativo;
	}

	public String getOreAssenzeRecupero() {
		return this.oreAssenzeRecupero;
	}

	public void setOreAssenzeRecupero(String oreAssenzeRecupero) {
		this.oreAssenzeRecupero = oreAssenzeRecupero;
	}

	public String getOreViaggio() {
		return this.oreViaggio;
	}

	public void setOreViaggio(String oreViaggio) {
		this.oreViaggio = oreViaggio;
	}

	public String getOreFerie() {
		return this.oreFerie;
	}

	public void setOreFerie(String oreFerie) {
		this.oreFerie = oreFerie;
	}

	public String getOrePermesso() {
		return this.orePermesso;
	}

	public void setOrePermesso(String orePermesso) {
		this.orePermesso = orePermesso;
	}

	public String getOreStraordinario() {
		return this.oreStraordinario;
	}

	public void setOreStraordinario(String oreStraordinario) {
		this.oreStraordinario = oreStraordinario;
	}

	public String getStatoRevisione() {
		return this.statoRevisione;
	}

	public void setStatoRevisione(String statoRevisione) {
		this.statoRevisione = statoRevisione;
	}

	public String getTotaleOreGiorno() {
		return this.totaleOreGiorno;
	}

	public void setTotaleOreGiorno(String totaleOreGiorno) {
		this.totaleOreGiorno = totaleOreGiorno;
	}

	public Set<DettaglioIntervalliCommesse> getDettaglioIntervalliCommesses() {
		return this.dettaglioIntervalliCommesses;
	}

	public void setDettaglioIntervalliCommesses(Set<DettaglioIntervalliCommesse> dettaglioIntervalliCommesses) {
		this.dettaglioIntervalliCommesses = dettaglioIntervalliCommesses;
	}
	
	public Set<DettaglioIntervalliIU> getDettaglioIntervalliIUs() {
		return this.dettaglioIntervalliIUs;
	}

	public void setDettaglioIntervalliIUs(Set<DettaglioIntervalliIU> dettaglioIntervalliIUs) {
		this.dettaglioIntervalliIUs = dettaglioIntervalliIUs;
	}
	
	public FoglioOreMese getFoglioOreMese() {
		return this.foglioOreMese;
	}

	public void setFoglioOreMese(FoglioOreMese foglioOreMese) {
		this.foglioOreMese = foglioOreMese;
	}

	public String getDeltaOreViaggio() {
		return deltaOreViaggio;
	}

	public void setDeltaOreViaggio(String deltaOreViaggio) {
		this.deltaOreViaggio = deltaOreViaggio;
	}

	public String getNoteAggiuntive() {
		return noteAggiuntive;
	}

	public void setNoteAggiuntive(String noteAggiuntive) {
		this.noteAggiuntive = noteAggiuntive;
	}

	public String getOreAbbuono() {
		return oreAbbuono;
	}

	public void setOreAbbuono(String oreAbbuono) {
		this.oreAbbuono = oreAbbuono;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((giornoRiferimento == null) ? 0 : giornoRiferimento
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DettaglioOreGiornaliere other = (DettaglioOreGiornaliere) obj;
		if (giornoRiferimento == null) {
			if (other.giornoRiferimento != null)
				return false;
		} else if (!giornoRiferimento.equals(other.giornoRiferimento))
			return false;
		return true;
	}

	public String getFermoMacchina() {
		return fermoMacchina;
	}

	public void setFermoMacchina(String fermoMacchina) {
		this.fermoMacchina = fermoMacchina;
	}

	
	
}