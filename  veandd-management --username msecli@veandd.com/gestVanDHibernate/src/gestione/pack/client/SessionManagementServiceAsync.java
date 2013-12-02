package gestione.pack.client;

import gestione.pack.client.model.AttivitaFatturateModel;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.model.FatturaModel;
import gestione.pack.client.model.RiepilogoCostiDipendentiModel;
import gestione.pack.client.model.RiepilogoMeseGiornalieroModel;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliModel;
import gestione.pack.client.model.RiepilogoSALPCLModel;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SessionManagementServiceAsync {

	void setUserName(String userName, AsyncCallback<Void> callback);

	void getUserName(AsyncCallback<String> asyncCallback);

	void login(String username, String password, AsyncCallback<String> callback);

	void getRuolo(AsyncCallback<String> callback);

	void logOut(AsyncCallback<Void> callback);

	void setDataInSession(String mese, String sede, String username,
			String operazione, AsyncCallback<Boolean> asyncCallback);

	void setDataInSession(String dataRif, String username,
			String operazione, String totOreCommesse, String totOreIU, AsyncCallback<Boolean> asyncCallback);

	void getSede(AsyncCallback<String> asyncCallback);

	void setDataReportAnnualeInSession(String anno, String sede,
			String operazione, AsyncCallback<Boolean> asyncCallback);

	void setDataReportDatiFatturazioneInSession(String anno, String mese,
			String string, AsyncCallback<Boolean> asyncCallback);

	void setNomeReport(String string, List<RiepilogoOreNonFatturabiliModel> list, AsyncCallback<Boolean> asyncCallback);


	void setDatiReportCostiDip(String string,
			List<RiepilogoCostiDipendentiModel> models,
			AsyncCallback<Boolean> callback);

	void setDataFileTmbInSession(String data, String sede,
			AsyncCallback<Boolean> asyncCallback);

	void setDatiReportSalPcl(String string, List<RiepilogoSALPCLModel> models,
			AsyncCallback<Boolean> asyncCallback);

	void setDatiReportSalPclRiassunto(String string,
			List<RiepilogoSALPCLModel> models,
			AsyncCallback<Boolean> asyncCallback);

	void setDataReportDatiFatturazioneInSession(String string,
			List<DatiFatturazioneMeseModel> listaDati,
			AsyncCallback<Boolean> asyncCallback);

	void setDataRiepilogoCommesseInSession(String dataR, String usernameR,
			String string, String totOreCommesse, String totOreIU,
			List<RiepilogoMeseGiornalieroModel> models,
			AsyncCallback<Boolean> asyncCallback);

	void setDataFattura(String numeroOrdine, int idFoglioFatturazione,
			FatturaModel fM, List<AttivitaFatturateModel> listaAM, String operazione, AsyncCallback<Boolean> asyncCallback);
	

}
