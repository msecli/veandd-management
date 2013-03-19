package gestione.pack.client.utility;

import com.google.gwt.user.client.rpc.AsyncCallback;

import gestione.pack.client.SessionManagementService;



public class RecuperoParametriSessione {

	public static String user=new String();
	
		
	public String getUsername(){
		
			
		SessionManagementService.Util.getInstance().getUserName(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				
				user=setUsername(result);
			}
			
			

			@Override
			public void onFailure(Throwable caught) {
				
				
			}
		});
		
		return user;	
	}
	
	
	public  String setUsername(String username){
		
		return new String(username);	
		
	}
	
	
	
}
