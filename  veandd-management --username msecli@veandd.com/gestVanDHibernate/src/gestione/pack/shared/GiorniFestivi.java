package gestione.pack.shared;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="giorni_festivi")
public class GiorniFestivi implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idGiorno;

    @Temporal( TemporalType.DATE)
	private Date giorno;

	
	public int getIdGiorno() {
		return idGiorno;
	}

	public void setIdGiorno(int idGiorno) {
		this.idGiorno = idGiorno;
	}

	public Date getGiorno() {
		return giorno;
	}

	public void setGiorno(Date giorno) {
		this.giorno = giorno;
	}
   

}
