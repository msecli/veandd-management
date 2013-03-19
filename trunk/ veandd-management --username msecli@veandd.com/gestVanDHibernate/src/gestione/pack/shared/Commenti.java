package gestione.pack.shared;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="commenti")
public class Commenti extends LightEntity implements IsSerializable {

private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="idCommenti", nullable=false)
	private int idCommenti;
	private String testo;
	private String username;
	private String dataRichiesta;
	
	public String getTesto() {
		return testo;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}
	public int getIdCommenti() {
		return idCommenti;
	}
	public void setIdCommenti(int idCommenti) {
		this.idCommenti = idCommenti;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDataRichiesta() {
		return dataRichiesta;
	}
	public void setDataRichiesta(String dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}
	
	
}