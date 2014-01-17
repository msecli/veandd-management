package gestione.pack.shared;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="log_error")
public class LogErrori implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idLog;

    @Temporal( TemporalType.DATE)
	private Date oraOperazione;

	private String username;
	
	private String operazione;
	
	private String codiceErrore;
	
	private String esitoOperazione;

	/**
	 * @return the idLog
	 */
	public int getIdLog() {
		return idLog;
	}

	/**
	 * @param idLog the idLog to set
	 */
	public void setIdLog(int idLog) {
		this.idLog = idLog;
	}

	/**
	 * @return the oraOperazione
	 */
	public Date getOraOperazione() {
		return oraOperazione;
	}

	/**
	 * @param oraOperazione the oraOperazione to set
	 */
	public void setOraOperazione(Date oraOperazione) {
		this.oraOperazione = oraOperazione;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the operazione
	 */
	public String getOperazione() {
		return operazione;
	}

	/**
	 * @param operazione the operazione to set
	 */
	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}

	/**
	 * @return the codiceErrore
	 */
	public String getCodiceErrore() {
		return codiceErrore;
	}

	/**
	 * @param codiceErrore the codiceErrore to set
	 */
	public void setCodiceErrore(String codiceErrore) {
		this.codiceErrore = codiceErrore;
	}

	public String getEsitoOperazione() {
		return esitoOperazione;
	}

	public void setEsitoOperazione(String esitoOperazione) {
		this.esitoOperazione = esitoOperazione;
	}

	

	
	
	
	
}
