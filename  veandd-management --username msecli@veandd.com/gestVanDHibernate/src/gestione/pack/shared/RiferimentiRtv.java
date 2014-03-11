package gestione.pack.shared;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="riferimenti_rtv")
public class RiferimentiRtv implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idRiferimentiRtv;

	private String email;

	private String indirizzo;

	private String reparto;

	private String riferimento;

	private String sezione;

	private String telefono;

	//bi-directional many-to-one association to Cliente
    @ManyToOne
	@JoinColumn(name="idCliente")
	private Cliente cliente;

    public RiferimentiRtv() {
    }

	public int getIdRiferimentiRtv() {
		return this.idRiferimentiRtv;
	}

	public void setIdRiferimentiRtv(int idRiferimentiRtv) {
		this.idRiferimentiRtv = idRiferimentiRtv;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getReparto() {
		return this.reparto;
	}

	public void setReparto(String reparto) {
		this.reparto = reparto;
	}

	public String getRiferimento() {
		return this.riferimento;
	}

	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}

	public String getSezione() {
		return this.sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
