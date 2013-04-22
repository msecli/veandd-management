package gestione.pack.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SessionManagementServiceAsync {

	void setUserName(String userName, AsyncCallback<Void> callback);

	void getUserName(AsyncCallback<String> asyncCallback);

	void login(String username, String password, AsyncCallback<String> callback);

	void getRuolo(AsyncCallback<String> callback);

	void logOut(AsyncCallback<Void> callback);

	void setDataInSession(String mese, String operazione, String username, AsyncCallback<Boolean> asyncCallback);

	void setDataInSession(String dataRif, String username,
			AsyncCallback<Boolean> asyncCallback);

}
