package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.Set;


/*
 *
 * The persistent class for the attivita database table.
 * 
 */
@Entity
@Table(name="attivita")
public class Attivita extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_ATTIVITA")
	private int idAttivita;

    @Temporal( TemporalType.DATE)
	private Date dataAssociazione;

    @Temporal( TemporalType.DATE)
	private Date dataFineAssociazione;

	private int idAttivitaPianAssociata;
	
	private String descrizioneAttivita;

	//bi-directional many-to-one association to AssociazionePtoa
	@OneToMany(mappedBy="attivita", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN})
	private Set<AssociazionePtoA> associazionePtoas;

	//bi-directional many-to-one association to Commessa
	@ManyToOne
	@JoinColumn(name="COD_COMMESSA")
	private Commessa commessa;

    public Attivita() {
    }

	public int getIdAttivita() {
		return this.idAttivita;
	}

	public void setIdAttivita(int idAttivita) {
		this.idAttivita = idAttivita;
	}

	public Date getDataInizio() {
		return this.dataAssociazione;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataAssociazione = dataInizio;
	}

	public Date getDataTermine() {
		return this.dataFineAssociazione;
	}

	public void setDataTermine(Date dataTermine) {
		this.dataFineAssociazione = dataTermine;
	}

	public int getIdAttivitaAssociata() {
		return this.idAttivitaPianAssociata;
	}

	public void setIdAttivitaAssociata(int idAttivitaAssociata) {
		this.idAttivitaPianAssociata = idAttivitaAssociata;
	}

	public String getDescrizioneAttivita() {
		return descrizioneAttivita;
	}

	public void setDescrizioneAttivita(String descrizioneAttivita) {
		this.descrizioneAttivita = descrizioneAttivita;
	}

	public Set<AssociazionePtoA> getAssociazionePtoas() {
		return this.associazionePtoas;
	}

	public void setAssociazionePtoas(Set<AssociazionePtoA> associazionePtoas) {
		this.associazionePtoas = associazionePtoas;
	}
	
	public Commessa getCommessa() {
		return this.commessa;
	}

	public void setCommessa(Commessa commessa) {
		this.commessa = commessa;
	}
	
}