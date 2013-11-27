package gestione.pack.shared;


import javax.persistence.*;


import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;
	
	@Entity
	@Table(name="dati_fatturazione_azienda")
	public class DatiFatturazioneAzienda extends LightEntity implements IsSerializable {
		private static final long serialVersionUID = 1L;

		@Id
		private int idDatiFatturazioneAzienda;

		private String aliquotaIva;

		private String capitaleSociale;
		
		private String pIva;

		private String datiFiliale;

		private String iban;

		private String ragioneSociale;

		private String rea;

		private String registroImprese;

		private String sedeLegale;

		private String sedeOperativa;

	    public DatiFatturazioneAzienda() {
	    }

		public int getIdDatiFatturazioneAzienda() {
			return this.idDatiFatturazioneAzienda;
		}

		public void setIdDatiFatturazioneAzienda(int idDatiFatturazioneAzienda) {
			this.idDatiFatturazioneAzienda = idDatiFatturazioneAzienda;
		}

		public String getAliquotaIva() {
			return this.aliquotaIva;
		}

		public void setAliquotaIva(String aliquotaIva) {
			this.aliquotaIva = aliquotaIva;
		}

		public String getCapitaleSociale() {
			return this.capitaleSociale;
		}

		public void setCapitaleSociale(String capitaleSociale) {
			this.capitaleSociale = capitaleSociale;
		}

		public String getDatiFiliale() {
			return this.datiFiliale;
		}

		public void setDatiFiliale(String datiFiliale) {
			this.datiFiliale = datiFiliale;
		}

		public String getIban() {
			return this.iban;
		}

		public void setIban(String iban) {
			this.iban = iban;
		}

		public String getRagioneSociale() {
			return this.ragioneSociale;
		}

		public void setRagioneSociale(String ragioneSociale) {
			this.ragioneSociale = ragioneSociale;
		}

		public String getRea() {
			return this.rea;
		}

		public void setRea(String rea) {
			this.rea = rea;
		}

		public String getRegistroImprese() {
			return this.registroImprese;
		}

		public void setRegistroImprese(String registroImprese) {
			this.registroImprese = registroImprese;
		}

		public String getSedeLegale() {
			return this.sedeLegale;
		}

		public void setSedeLegale(String sedeLegale) {
			this.sedeLegale = sedeLegale;
		}

		public String getSedeOperativa() {
			return this.sedeOperativa;
		}

		public void setSedeOperativa(String sedeOperativa) {
			this.sedeOperativa = sedeOperativa;
		}

		public String getpIva() {
			return pIva;
		}

		public void setpIva(String pIva) {
			this.pIva = pIva;
		}

	}
