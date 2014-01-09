package gestione.pack.shared;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="periodo_sblocco_giorni")
public class PeriodoSbloccoGiorni implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idPeriodo;

    @Temporal( TemporalType.DATE)
	private Date dataFine;

    @Temporal( TemporalType.DATE)
	private Date dataInizio;

	private String sede;

    public PeriodoSbloccoGiorni() {
    }

	public int getIdPeriodo() {
		return this.idPeriodo;
	}

	public void setIdPeriodo(int idPeriodo) {
		this.idPeriodo = idPeriodo;
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

	public String getSede() {
		return this.sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

}
