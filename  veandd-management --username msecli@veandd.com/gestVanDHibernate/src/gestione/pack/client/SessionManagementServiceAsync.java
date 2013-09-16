package gestione.pack.client;

import gestione.pack.client.model.RiepilogoOreNonFatturabiliModel;

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

}
