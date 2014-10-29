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
	
	private Float	oreOrdine;
	private Float oreResidueOrdine;
	private Float importoOrdine;
	private Float importoResiduoOrdine;
	private Integer idEstensioneCommessa;

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

	/**
	 * @return the oreOrdine
	 */
	public Float getOreOrdine() {
		return oreOrdine;
	}

	/**
	 * @param oreOrdine the oreOrdine to set
	 */
	public void setOreOrdine(Float oreOrdine) {
		this.oreOrdine = oreOrdine;
	}

	/**
	 * @return the oreResidueOrdine
	 */
	public Float getOreResidueOrdine() {
		return oreResidueOrdine;
	}

	/**
	 * @param oreResidueOrdine the oreResidueOrdine to set
	 */
	public void setOreResidueOrdine(Float oreResidueOrdine) {
		this.oreResidueOrdine = oreResidueOrdine;
	}

	/**
	 * @return the importoOrdine
	 */
	public Float getImportoOrdine() {
		return importoOrdine;
	}

	/**
	 * @param importoOrdine the importoOrdine to set
	 */
	public void setImportoOrdine(Float importoOrdine) {
		this.importoOrdine = importoOrdine;
	}

	/**
	 * @return the importoResiduoOrdine
	 */
	public Float getImportoResiduoOrdine() {
		return importoResiduoOrdine;
	}

	/**
	 * @param importoResiduoOrdine the importoResiduoOrdine to set
	 */
	public void setImportoResiduoOrdine(Float importoResiduoOrdine) {
		this.importoResiduoOrdine = importoResiduoOrdine;
	}

	public Integer getIdEstensioneCommessa() {
		return idEstensioneCommessa;
	}

	public void setIdEstensioneCommessa(Integer idEstensioneCommessa) {
		this.idEstensioneCommessa = idEstensioneCommessa;
	}
	
	
	
}