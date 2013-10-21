package gestione.pack.shared;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the attivita_ordine database table.
 * 
 */
@Entity
@Table(name="attivita_ordine")
public class AttivitaOrdine implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idAttivitaOrdine;

	private String descrizioneAttivita;

	private String tariffaAttivita;

	//bi-directional many-to-one association to Ordine
    @ManyToOne
	@JoinColumn(name="idOrdine")
	private Ordine ordine;

    public AttivitaOrdine() {
    }

	public int getIdAttivitaOrdine() {
		return this.idAttivitaOrdine;
	}

	public void setIdAttivitaOrdine(int idAttivitaOrdine) {
		this.idAttivitaOrdine = idAttivitaOrdine;
	}

	public String getDescrizioneAttivita() {
		return this.descrizioneAttivita;
	}

	public void setDescrizioneAttivita(String descrizioneAttivita) {
		this.descrizioneAttivita = descrizioneAttivita;
	}

	public String getTariffaAttivita() {
		return this.tariffaAttivita;
	}

	public void setTariffaAttivita(String tariffaAttivita) {
		this.tariffaAttivita = tariffaAttivita;
	}

	public Ordine getOrdine() {
		return this.ordine;
	}

	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}
	
}