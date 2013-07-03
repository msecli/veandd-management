/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package gestione.pack.client;

import java.util.Date;
import java.util.List;

import gestione.pack.client.model.ClienteModel;
import gestione.pack.client.model.CommessaModel;
import gestione.pack.client.model.DatiFatturazioneCommessaModel;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.model.FoglioFatturazioneModel;
import gestione.pack.client.model.GiustificativiModel;
import gestione.pack.client.model.IntervalliCommesseModel;
import gestione.pack.client.model.IntervalliIUModel;
import gestione.pack.client.model.PersonaleAssociatoModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.model.RdaModel;
import gestione.pack.client.model.RdoCompletaModel;
import gestione.pack.client.model.RiepilogoCommesseGiornalieroModel;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.model.RiepilogoOreDipCommesse;
import gestione.pack.client.model.RiepilogoOreDipCommesseGiornaliero;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;
import gestione.pack.client.model.RiepilogoOreModel;
import gestione.pack.client.model.RiepilogoOreTotaliCommesse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("AdministrationService")
public interface AdministrationService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static AdministrationServiceAsync instance;
		public static AdministrationServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(AdministrationService.class);
			}
			return instance;
		}
	}

	
	//--------------------PERSONALE
	
	void insertDataPersonale(String nome, String cognome, String username,
			String password, String nBadge, String ruolo, String tipoOrario,
			String tipoLavoratore, String gruppoLavoro, String costoOrario,
			String costoStruttura, String sede, String oreDirette,
			String oreIndirette, String permessi, String ferie, String ext,
			String oreRecupero, String oreRecupero2);

	void editDataPersonale(int id, String nome, String cognome,
			String username, String password, String nBadge, String ruolo,
			String tipoOrario, String tipoLavoratore, String gruppoLavoro,
			String costoOrario, String costoStruttura, String sede,
			String oreDirette, String oreIndirette, String permessi,
			String ferie, String ext, String oreRecupero, String oreRecupero2);

	void removeDataPersonale(int id) throws IllegalArgumentException;

	List<PersonaleModel> getAllPersonaleModel() throws IllegalArgumentException;
	
	List<String> getNomePM() throws IllegalArgumentException;	
	
	
	
	//-------------------CLIENTI
	
	List<ClienteModel> getAllClientiModel() throws IllegalArgumentException;

	void insertDataCliente(int codCliente, String ragSociale,
			String codFiscale, String partitaIVA, String codRaggr,
			String comune, String provincia, String stato, String indirizzo,
			String cap, String telefono, String fax, String email) throws IllegalArgumentException;

	void editDataCliente(int parseInt, String ragSociale, String codFiscale,
			String partitaIVA, String codRaggr, String comune,
			String provincia, String stato, String indirizzo, String cap,
			String telefono, String fax, String email) throws IllegalArgumentException;

	void removeDataCliente(int parseInt) throws IllegalArgumentException;
	
	List<String> getRagioneSociale() throws IllegalArgumentException;
	
	
	//----------------------RDA
	
	
	List<RdaModel> getAllRdaModel() throws IllegalArgumentException;
	
	List<String> getAllNumeroRdo() throws IllegalArgumentException;
	
	/*boolean insertDataRda(int idcliente, String numRda, String ragioneSociale);

	boolean editDataRda(int idRda, int idcliente, String numRda) throws IllegalArgumentException;

	boolean deleteDataRda(int idRda) throws IllegalArgumentException;*/

	
	List<RdoCompletaModel> getAllRdoCompletaModel() throws IllegalArgumentException;
	
	boolean saveRdoCompleta(String numRdo, String cliente, String numOfferta,
			Date dataOfferte, String importo, String numOrdine,
			String descrizione, Date dataInizio, Date dataFine, String tariffa,
			String numRisorse, String oreDisp, String oreRes) throws IllegalArgumentException;
	
	boolean editRdoCompleta(int idRdo, String numRdo, String cliente,
			String numOfferta, Date dataOfferte, String importo,
			String numOrdine, String descrizione, Date dataInizio,
			Date dataFine, String tariffa, String numRisorse, String oreDisp,
			String oreRes)  throws IllegalArgumentException;
	
	boolean deleteRdoCompleta(int idRdo) throws IllegalArgumentException;


	//----------------------OFFERTA
/*
	void insertDataOfferta(String numOfferta, String numRda, Date dataOfferta,
			String descrizione, String tariffa);

	void editDataOfferta(int idofferta, String numOfferta, String numRda, Date dataOfferta,
			String descrizione, String tariffa) throws IllegalArgumentException;

	void deleteDataOfferta(int idOfferta);

	List<OffertaModel> getAllOfferteModel() throws IllegalArgumentException;

	Set<Offerta> getAllOfferte() throws IllegalArgumentException;

	*/
	//---------------------ORDINE
/*
	boolean insertDataOrdine(String numOrdine, String numRda,
			String numeroCommessa,String estensione, Date dataInizio, Date dataFine,
			String descrizione, String tariffaOraria, String numeroRisorse,
			String numeroOre);

	boolean editDataOrdine(int idrdine, String numOrdine, String numRda,
			String numeroCommessa, String estensione, Date dataInizio,
			Date dataFine, String descrizione, String tariffaOraria,
			String numeroRisorse, String numeroOre);

	List<OrdineModel> getAllOrdineModel() throws IllegalArgumentException;

	boolean deleteDataOrdine(int idOrdine) throws IllegalArgumentException;*/

	List<String> getAllListaOrdini() throws IllegalArgumentException;
	
	
	//-----------------------COMMESSE
	List<CommessaModel> getAllCommesseModel() throws IllegalArgumentException;

	boolean insertDataCommessa(String numCommessa, String estensione,
			String tipoCommessa, String pM, String statoCommessa,
			String oreLavoro, String oreLavoroResidue, String tariffaSal, 
			String salAttuale, String pclAttuale, String descrizione,
			String note);

	boolean editDataCommessa(int i,String numCommessa, String estensione,
			String tipoCommessa, String pM, String statoCommessa,
			String oreLavoro, String oreLavoroResidue, String tariffaSal, 
			String salAttuale, String pclAttuale, String descrizione,
			String note);

	boolean deleteDataCommessa(int parseInt)throws IllegalArgumentException;

	boolean associaOrdineCommessa(String numeroCommessa, String numOrdine,
			String numRda, Date dataInizio, Date dataFine, String descrizione,
			String tariffaOraria, String numeroRisorse, String numeroOre)throws IllegalArgumentException;

	boolean associaOrdinePresenteCommessa(String idCommessa, String numOrdine)throws IllegalArgumentException;

	boolean closeCommessa(int parseInt)throws IllegalArgumentException;

	List<String> getCommesse()throws IllegalArgumentException;
	
	List<String> getCommesseAperte()throws IllegalArgumentException;

	List<String> getCommesseByPM(String nome, String cognome)throws IllegalArgumentException;

	
	//---------------------------------------Associazioni Commesse Attivita Dip
	boolean createAssociazioneCommessaDipendenti(
			String pm, String commessa, List<String> listaDipendenti)throws IllegalArgumentException;

	boolean deleteAssociazioneCommessaDipendenti(int idAssociazione,
			String commessa, String nome, String cognome)throws IllegalArgumentException;

	boolean editAssociazioneCommessaDipendenti(String pm, String commessa,
			List<String> listaDipendenti)throws IllegalArgumentException;

	List<PersonaleAssociatoModel> getAssociazioniPersonaleCommessaByPM(String cognome)
			throws IllegalArgumentException;

	List<PersonaleAssociatoModel> getAssociazioniPersonaleCommessa()throws IllegalArgumentException;

	
	List<IntervalliCommesseModel> getAssociazioniPersonaleCommessaByUsername(
			String username, Date data);

	//List<IntervalliIUModel> getIntervalliIUTimbratrice(String u)throws IllegalArgumentException;

	
	//--------------------------------------FOGLIO ORE------------------------------------------------------
	
	boolean insertFoglioOreGiorno(String username, Date giorno,
			String totOreGenerale, String delta, String oreViaggio,
			String oreAssRecupero, String deltaOreViaggio,
			String giustificativo, String oreStraordinario, String oreFerie,
			String orePermesso, String revisione, String oreAbbuono,
			List<String> intervalliIU,
			List<IntervalliCommesseModel> intervalliC, String oreRecuperoTot,
			String noteAggiuntive);
	
	boolean insertFoglioOreGiorno(String username, Date giorno,
			List<IntervalliCommesseModel> intervalliC);

	
	List<IntervalliIUModel> loadIntervalliIU(String username, Date data)throws IllegalArgumentException;

	GiustificativiModel loadGiustificativi(String username, Date giorno)throws IllegalArgumentException;

	RiepilogoOreModel loadRiepilogoMese(String username, Date mese)throws IllegalArgumentException;

	List<String> getListaDipendenti(String string)throws IllegalArgumentException;

	List<RiepilogoOreDipCommesse> getRiepilogoOreDipCommesse(String mese,
			String pm)throws IllegalArgumentException;

	List<RiepilogoOreTotaliCommesse> getRiepilogoOreTotCommesse(String pm,
			String data);
	
	List<RiepilogoFoglioOreModel> getRiepilogoMeseFoglioOre(String username,
			Date data);

	List<String> loadIntervalliToolTip(String username, Date giorno);
	


//----------------------------------FATTURAZIONE-------------------------------------------------------------------
	List<RiepilogoOreDipFatturazione> getRiepilogoOreDipFatturazione(
			String mese, String pm)throws IllegalArgumentException;

	FoglioFatturazioneModel getDatiFatturazionePerOrdine(
			String numeroCommessa, String string)throws IllegalArgumentException;

	boolean insertDatiFoglioFatturazione(String oreEseguite, String salAttuale,
			String pclAttuale, String oreFatturere, String variazioneSAL,
			String variazionePCL, String meseCorrente, String note,
			String statoElaborazione, String commessaSelezionata,
			String tariffaUtilizzata);

	List<DatiFatturazioneMeseModel> getReportDatiFatturazioneMese(String mese)throws IllegalArgumentException;

	List<String> getCommesseRiepilogoDatiFatturazione()throws IllegalArgumentException;

	List<DatiFatturazioneCommessaModel> getRiepilogoDatiFatturazioneCommessa()throws IllegalArgumentException;

	List<RiepilogoOreTotaliCommesse> getElencoCommesseSuFoglioFatturazione(
			String numCommessa, String numEstensione, String data) throws IllegalArgumentException;
	
//---------------------------------VARIE----------------------------------------------------------------------------
	boolean invioCommenti(String testo, String username)throws IllegalArgumentException;

	List<CommessaModel> getAllCommesseModelByPm(String cognomePm)throws IllegalArgumentException;

	List<PersonaleModel> getListaDipendentiModel(String string)throws IllegalArgumentException;

	boolean setRiepilogoOreOnSession(List<RiepilogoFoglioOreModel> lista)throws IllegalArgumentException;

	List<RiepilogoOreDipCommesseGiornaliero> getRiepilogoGiornalieroCommesse(
			String username, Date data)throws IllegalArgumentException;

	
	
	
}
