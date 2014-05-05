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

import gestione.pack.client.model.AnagraficaHardwareModel;
import gestione.pack.client.model.ClienteModel;
import gestione.pack.client.model.CommentiModel;
import gestione.pack.client.model.CommessaModel;
import gestione.pack.client.model.CostiHwSwModel;
import gestione.pack.client.model.CostingModel;
import gestione.pack.client.model.CostingRisorsaModel;
import gestione.pack.client.model.DatiFatturazioneCommessaModel;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.model.DettaglioTrasfertaModel;
import gestione.pack.client.model.FatturaModel;
import gestione.pack.client.model.FoglioFatturazioneModel;
import gestione.pack.client.model.GestioneCostiDipendentiModel;
import gestione.pack.client.model.GestioneRdoCommesse;
import gestione.pack.client.model.GiorniFestiviModel;
import gestione.pack.client.model.GiustificativiModel;
import gestione.pack.client.model.IntervalliCommesseModel;
import gestione.pack.client.model.IntervalliIUModel;
import gestione.pack.client.model.PeriodoSbloccoModel;
import gestione.pack.client.model.PersonaleAssociatoModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.model.RdaModel;
import gestione.pack.client.model.RdoCompletaModel;
import gestione.pack.client.model.RiepilogoCostiDipendentiModel;
import gestione.pack.client.model.RiepilogoMensileOrdiniModel;
import gestione.pack.client.model.RiepilogoMeseGiornalieroModel;
import gestione.pack.client.model.RiepilogoOreAnnualiDipendente;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliModel;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.model.RiepilogoOreDipCommesse;
import gestione.pack.client.model.RiepilogoOreDipCommesseGiornaliero;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;
import gestione.pack.client.model.RiepilogoOreModel;
import gestione.pack.client.model.RiepilogoOreTotaliCommesse;
import gestione.pack.client.model.RiepilogoRichiesteModel;
import gestione.pack.client.model.RiepilogoSALPCLModel;
import gestione.pack.client.model.RiferimentiRtvModel;
import gestione.pack.client.model.RtvModel;
import gestione.pack.client.model.TariffaOrdineModel;

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
	
	boolean insertDataPersonale(String nome, String cognome, String username,
			String username2, String statoRapporto, String badge, String ruolo,
			String tipoO, String tipoL, String gruppoL, String costoO,
			String costoS, String sede, String sedeOperativa,
			String oreDirette, String oreIndirette, String permessi,
			String ferie, String ext, String oreRecupero)throws IllegalArgumentException;

	void editDataPersonale(int id, String nome, String cognome,
			String username, String string, String statoRapporto, String badge,
			String ruolo, String tipoO, String tipoL, String gruppoL,
			String costoO, String costoS, String sede, String sedeOperativa,
			String oreDirette, String oreIndirette, String permessi,
			String ferie, String ex_fest, String oreRecupero)throws IllegalArgumentException;
	
	
	void removeDataPersonale(int id) throws IllegalArgumentException;

	List<PersonaleModel> getAllPersonaleModel() throws IllegalArgumentException;
	
	List<String> getNomePM() throws IllegalArgumentException;	
	
	
	
	//-------------------CLIENTI
	
	List<ClienteModel> getAllClientiModel() throws IllegalArgumentException;

	void insertDataCliente(int codCliente, String ragSociale,
			String codFiscale, String partitaIVA, String codRaggr,
			String codFornitore, String comune, String provincia, String stato,
			String indirizzo, String cap, String telefono, String fax,
			String email);

	void editDataCliente(int parseInt, String ragSociale, String codFiscale,
			String partitaIVA, String codRaggr, String codFornitore,
			String comune, String provincia, String stato, String indirizzo,
			String cap, String telefono, String fax, String email);

	void removeDataCliente(int parseInt) throws IllegalArgumentException;
	
	List<String> getRagioneSociale() throws IllegalArgumentException;
	
	List<ClienteModel> getListaClientiModel() throws IllegalArgumentException;
	
	
	//----------------------RDA
	
	
	List<RdaModel> getAllRdaModel() throws IllegalArgumentException;
	
	List<String> getAllNumeroRdo() throws IllegalArgumentException;
	
	/*boolean insertDataRda(int idcliente, String numRda, String ragioneSociale);

	boolean editDataRda(int idRda, int idcliente, String numRda) throws IllegalArgumentException;

	boolean deleteDataRda(int idRda) throws IllegalArgumentException;*/

	
	List<RdoCompletaModel> getAllRdoCompletaModel() throws IllegalArgumentException;
	
	boolean saveRdoCompleta(String numRdo, String cliente, String numOfferta,
			Date dataOfferte, String importo, String numOrdine,
			String descrizione, String elementoWbs, String conto,
			String prCenter, String bem, Date dataInizio, Date dataFine,
			String tariffa, String numRisorse, String oreDisp, String oreRes,
			List<TariffaOrdineModel> listaTar, String importoOrdine,
			String importoResiduoOrdine);
	
	boolean editRdoCompleta(int idRdo, String numRdo, String cliente,
			String numOfferta, Date dataOfferte, String importo,
			String numOrdine, String descrizione, String elementoWbs,
			String conto, String prCenter, String bem, Date dataInizio,
			Date dataFine, String tariffa, String numRisorse, String oreDisp,
			String oreRes, List<TariffaOrdineModel> listaTar,
			String importoOrdine, String importoResiduoOrdine);
	
	boolean deleteRdoCompleta(int idRdo) throws IllegalArgumentException;

	boolean eliminaAssociazioneOrdine(String numeroOrdine) throws IllegalArgumentException;

	List<TariffaOrdineModel> loadTariffePerOrdine(int idRdo)throws IllegalArgumentException;
	
	boolean chiudiOrdine(String numeroOrdine)throws IllegalArgumentException;
	
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
	
	
	List<CommessaModel> getAllCommesseModel(String pm, String statoSelezionato);

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
	
	List<CommessaModel> getCommesseAperte();

	List<CommessaModel> getCommesseByPM(String nome, String cognome);

	
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
	
	List<CommessaModel> getCommesseAperteSenzaOrdine()throws IllegalArgumentException;

	//List<IntervalliIUModel> getIntervalliIUTimbratrice(String u)throws IllegalArgumentException;
	
	List<CommessaModel> getCommesseByPmConAssociazioni(String nome,
			String cognome)throws IllegalArgumentException;

	List<CommessaModel> getCommesseAperteConAssociazioni()throws IllegalArgumentException;

	
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
			String data)throws IllegalArgumentException;
	
	List<RiepilogoFoglioOreModel> getRiepilogoMeseFoglioOre(String username,
			Date data)throws IllegalArgumentException;

	List<String> loadIntervalliToolTip(String username, Date giorno)throws IllegalArgumentException;
	
	Boolean checkOreIntervalliIUOreCommesse(
			String username, Date data)throws IllegalArgumentException;
	
	boolean eliminaDatiGiorno(String username, Date giorno)throws IllegalArgumentException;
	
	boolean elaboraDatiOreCollaboratori(RiepilogoOreDipCommesseGiornaliero g,
			Date data);
	
	List<RiepilogoOreDipCommesseGiornaliero> getDatiOreCollaboratori(String pm,
			Date data)throws IllegalArgumentException;

//----------------------------------FATTURAZIONE-------------------------------------------------------------------
	List<RiepilogoOreDipFatturazione> getRiepilogoOreDipFatturazione(
			String mese, String pm)throws IllegalArgumentException;

	FoglioFatturazioneModel getDatiFatturazionePerOrdine(
			String numeroCommessa, String data, int idAttivita)throws IllegalArgumentException;

	boolean insertDatiFoglioFatturazione(String oreEseguite, String salAttuale,
			String pclAttuale, String oreFatturere, String importoFatturare,
			String variazioneSAL, String variazionePCL, String meseCorrente,
			String note, String statoElaborazione, String commessaSelezionata,
			String tariffaUtilizzata, String flagSal, int idAttivita);

	List<DatiFatturazioneMeseModel> getReportDatiFatturazioneMese(String mese)throws IllegalArgumentException;

	List<String> getCommesseRiepilogoDatiFatturazione()throws IllegalArgumentException;

	List<DatiFatturazioneCommessaModel> getRiepilogoDatiFatturazioneCommessa(
			String commessaSelected)throws IllegalArgumentException;

	List<RiepilogoOreTotaliCommesse> getElencoCommesseSuFoglioFatturazione(
			String numCommessa, String numEstensione, String data) throws IllegalArgumentException;
	
//---------------------------------VARIE----------------------------------------------------------------------------
	boolean invioCommenti(String testo, String username)throws IllegalArgumentException;

	List<CommessaModel> getAllCommesseModelByPm(String cognomePm)throws IllegalArgumentException;

	List<PersonaleModel> getListaDipendentiModel(String string)throws IllegalArgumentException;

	boolean setRiepilogoOreOnSession(List<RiepilogoFoglioOreModel> lista)throws IllegalArgumentException;

	List<RiepilogoOreDipCommesseGiornaliero> getRiepilogoGiornalieroCommesse(
			int idDip, String username, Date data);

	List<RiepilogoFoglioOreModel> getRiepilogoMeseFoglioOre(Date value,
			String pm, String sede, String cognome);

	boolean confermaGiorniDipendente(String username, Date data) throws IllegalArgumentException;

	boolean confermaGiorniTuttiDipendenti(String string, Date data)throws IllegalArgumentException;

	List<CommentiModel> getAllCommenti()throws IllegalArgumentException;

	boolean deleteCommento(int d)throws IllegalArgumentException;

	List<GestioneRdoCommesse> getAllRdoCommesse()throws IllegalArgumentException;

	List<RiepilogoSALPCLModel> getRiepilogoSalPcl(String data,
			String tabSelected, String pm);

	List<RiepilogoSALPCLModel> getRiepilogoSalPclRiassunto(String data,
			String tabSelected)throws IllegalArgumentException;

	boolean confermaEditCommenti(int i)throws IllegalArgumentException;

	List<RiepilogoMeseGiornalieroModel> getRiepilogoMensileDettagliatoHorizontalLayout(
			String sede, String data)throws IllegalArgumentException;

	List<RiepilogoOreAnnualiDipendente> getRiepilogoAnnualeOreDipendenti(
			String anno, String sede)throws IllegalArgumentException;

	List<RiepilogoOreNonFatturabiliModel> getRiepilogoOreNonFatturate(
			String data, String groupBy)throws IllegalArgumentException;
	
	List<RiepilogoOreNonFatturabiliModel> getRiepilogoOreIndiretti(String data,
			String string)throws IllegalArgumentException;

	List<RiepilogoOreDipFatturazione> getRiepilogoTotCommessePerDipendenti(
			String mese, String sede, String pm);

	List<RiepilogoOreDipFatturazione> getRiepilogoOreCommesseDettDipendenti(
			String data, String sede)throws IllegalArgumentException;
	
	List<RiepilogoMeseGiornalieroModel> getRiepilogoMensileDettagliatoCommesseHorizontalLayout(
			String dipendente, String data)throws IllegalArgumentException;

	List<PeriodoSbloccoModel> getDatiPeriodoSblocco();
	
	boolean confermaPeriodoSblocco(Date dataInizio,
			Date dataFine, String sede)throws IllegalArgumentException;

	boolean eliminaPeriodoSblocco(int idSel)throws IllegalArgumentException;

	boolean inserisciGiornoFestivo(Date giorno, String sede);

	boolean eliminaGiornoFestivi(int idSel)throws IllegalArgumentException;

	List<GiorniFestiviModel> getGiorniFestivi()throws IllegalArgumentException;
	
//---------------------------------------------COSTI
	List<GestioneCostiDipendentiModel> getDatiCostiPersonale(int idPersonale) throws IllegalArgumentException;

	List<CostiHwSwModel> getDatiCostiHwSw(int id) throws IllegalArgumentException;

	boolean saveAssociaCostiHwSw(int idSelected, CostiHwSwModel costo)  throws IllegalArgumentException;

	void editDatiCostiAzienda(GestioneCostiDipendentiModel g) throws IllegalArgumentException;

	List<RiepilogoCostiDipendentiModel> getRiepilogoDatiCostiPersonale(
			String string) throws IllegalArgumentException;

	List<CommentiModel> getAllCommenti(String utente);

	List<CostingModel> getListaDatiCosting(String username)throws IllegalArgumentException;

	List<CostingModel> getDatiCosting(int costing)throws IllegalArgumentException;

	boolean insertDataCosting(String commessa, String area, int idCliente,
			String descrizione, String usernamePM);

	List<CostingRisorsaModel> getRiepilogoDatiCostingRisorse(int idCosting)throws IllegalArgumentException;

	CostingRisorsaModel getDatiCostiDipendenteSelezionato(int idPersonale, int idCosting)throws IllegalArgumentException;

	boolean confermaCostingDipendente(int idSelected, CostingRisorsaModel c)throws IllegalArgumentException;

	boolean deleteRisorsaCosting(int idSelected)throws IllegalArgumentException;

	boolean saveNewVersionCosting(int idSelected)throws IllegalArgumentException;

	boolean editStatoCosting(int idSelected, String operazione)throws IllegalArgumentException;

	FatturaModel elaboraDatiPerFattura(String numeroOrdine,
			int idFoglioFatturazione)throws IllegalArgumentException;

	List<RiepilogoOreDipFatturazione> getRiepilogoOreCommesseDettDipendentiPeriodo(
			String anno, String string, String string2, String string3,
			String pm, List<CommessaModel> listaCommesseSel)throws IllegalArgumentException;

	boolean saveDatiTrasfertaUtente(int idRisorsa, int idCostingSelected,
			String numeroViaggi, String oreViaggio, String kmStradali,
			String carburante, String autostrada, boolean usoAutoPropria,
			String costotreno, String costoAereo, String costiVari,
			String numeroGiorni, String numeroNotti, String costoAlbergo, String costoPranzo,
			String costoCena, String noleggioAuto, String trasportoLocale,
			String costoDiaria);
	
	DettaglioTrasfertaModel loadDataTrasferta(int idCostingSelected) throws IllegalArgumentException;

	
	//------------------------STRUMENTI AMMINISTRATIVI
	
	List<AnagraficaHardwareModel> getRiepilogoAnagraficaHardware() throws IllegalArgumentException;

	
	boolean editDataAnagraficaHardware(Integer idHardware, String username,
			String gruppoLavoro, String assistenza, String codiceModello,
			String cpu, String fornitoreAssistenza, String hardware, String hd,
			String ip, String ipFiat, String modello, String nodo, String note,
			String ram, Date scadenzaControllo, String sede, String serialId,
			String serialNumber, String sistemaOperativo, String stato,
			String svga, String tipologia, String utilizzo)	throws IllegalArgumentException;

	boolean insertRichiestaIt(String username, Date dataR, String ora, String pc);

	List<RiepilogoRichiesteModel> getRiepilogoRichiesteItUtente(String username);

	List<RiepilogoMensileOrdiniModel> getRiepilogoOrdini(String stato);

	List<RiepilogoMensileOrdiniModel> getDettaglioMensileOrdine(
			String numeroOrdine)throws IllegalArgumentException;

	List<RiferimentiRtvModel> getDatiReferenti()throws IllegalArgumentException;

	List<RtvModel> getDatiRtv(String numeroO);

	List<RiepilogoSALPCLModel> getRiepilogoSalPclOld(String data,
			String tabSelected) throws IllegalArgumentException;

	boolean setStatoFoglioFatturazione(String mese, String anno);
	
}
