package gestione.pack.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UtilityServiceAsync {

	void generateAttivitaOrdine(AsyncCallback<Void> asyncCallback);

	void insertIdAttivitaOrdineInFoglioFatturazione(AsyncCallback<Void> callback);

	
	
}
