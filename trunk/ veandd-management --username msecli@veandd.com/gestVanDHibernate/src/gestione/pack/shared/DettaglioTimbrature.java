package gestione.pack.shared;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;


@Entity
@Table(name="dettaglio_timbrature")
public class DettaglioTimbrature extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idDETTAGLIO_TIMBRATURE;

	private String numeroBadge;
	
	private String giorno;
	
	private String movimento;

	private String orario;
	
	private String giustificativo;

    public DettaglioTimbrature() {
    }

	public int getIdDETTAGLIO_TIMBRATURE() {
		return this.idDETTAGLIO_TIMBRATURE;
	}

	public void setIdDETTAGLIO_TIMBRATURE(int idDETTAGLIO_TIMBRATURE) {
		this.idDETTAGLIO_TIMBRATURE = idDETTAGLIO_TIMBRATURE;
	}

	public String getMovimento() {
		return this.movimento;
	}

	public void setMovimento(String movimento) {
		this.movimento = movimento;
	}

	public String getOrario() {
		return this.orario;
	}

	public void setOrario(String orario) {
		this.orario = orario;
	}

	public String getNumeroBadge() {
		return numeroBadge;
	}

	public void setNumeroBadge(String numeroBadge) {
		this.numeroBadge = numeroBadge;
	}

	public String getGiorno() {
		return giorno;
	}

	public void setGiorno(String giorno) {
		this.giorno = giorno;
	}

	public String getGiustificativo() {
		return giustificativo;
	}

	public void setGiustificativo(String giustificativo) {
		this.giustificativo = giustificativo;
	}

}
