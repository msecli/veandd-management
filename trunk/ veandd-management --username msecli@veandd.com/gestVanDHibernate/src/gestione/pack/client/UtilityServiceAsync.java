package gestione.pack.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UtilityServiceAsync {

	void generateAttivitaOrdine(AsyncCallback<Void> asyncCallback);

	void insertIdAttivitaOrdineInFoglioFatturazione(AsyncCallback<Void> callback);

	void checkIntervallicommesse(String periodo, AsyncCallback<List<String>> asyncCallback);
	
}
