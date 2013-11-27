package gestione.pack.shared;

import javax.persistence.*;
import net.sf.gilead.pojo.gwt.LightEntity;
import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="attivita_fatturata")
public class AttivitaFatturata extends LightEntity implements IsSerializable{

	private static final long serialVersionUID = 1L;

	@Id
	private int idATTIVITA_FATTURATA;

	private String descrizione;

	private String importo;

	//bi-directional many-to-one association to Fattura
    @ManyToOne
    @JoinColumn(name="ID_FATTURA")
	private Fattura fattura;

    public AttivitaFatturata() {
    }

	public int getIdATTIVITA_FATTURATA() {
		return this.idATTIVITA_FATTURATA;
	}

	public void setIdATTIVITA_FATTURATA(int idATTIVITA_FATTURATA) {
		this.idATTIVITA_FATTURATA = idATTIVITA_FATTURATA;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getImporto() {
		return this.importo;
	}

	public void setImporto(String importo) {
		this.importo = importo;
	}

	public Fattura getFattura() {
		return this.fattura;
	}

	public void setFattura(Fattura fattura) {
		this.fattura = fattura;
	}
	
}
