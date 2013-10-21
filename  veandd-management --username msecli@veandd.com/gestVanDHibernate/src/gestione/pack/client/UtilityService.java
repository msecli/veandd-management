package gestione.pack.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("UtilityService")
public interface UtilityService  extends  RemoteService{

	public static class Util {
		private static UtilityServiceAsync instance;
		public static UtilityServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(UtilityService.class);
			}
			return instance;
		}
	}

	void generateAttivitaOrdine();

	void insertIdAttivitaOrdineInFoglioFatturazione();
	
	
	
	
}
