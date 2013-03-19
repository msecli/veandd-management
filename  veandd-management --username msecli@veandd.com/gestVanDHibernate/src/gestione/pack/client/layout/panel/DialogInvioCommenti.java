package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DialogInvioCommenti  extends Dialog {

	private TextArea txtCommenti= new TextArea();
	protected Button btnInvia;
	protected String utente= new String();
	
	public DialogInvioCommenti(final String username){
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(450);
		setHeight(340);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setHeading("Invio Commenti.");
		setButtonAlign(HorizontalAlignment.CENTER);
		setModal(true);		
		
		utente=username;
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);
		
		ContentPanel cp = new ContentPanel(); //pannello contenente le due liste
		cp.setHeaderVisible(false);
		cp.setSize(420, 255);
		cp.setFrame(false);
		cp.setBorders(false);
		cp.setBodyBorder(false);
		cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cp.setStyleAttribute("padding-top", "5px");	
		
		txtCommenti.setAllowBlank(false);
		txtCommenti.setSize(420, 250);
		txtCommenti.setMaxLength(310);
		txtCommenti.setEmptyText("Segnalare eventuali problemi e anomalie riscontrate..");
		cp.add(txtCommenti);
		
		bodyContainer.add(cp);
		add(bodyContainer);
	}
	
	 protected void createButtons() {
		    super.createButtons();
		    
		    getButtonBar().add(new FillToolItem());
		    btnInvia =new Button("Invia");
		    btnInvia.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					if(txtCommenti.isValid()){
						String testo= txtCommenti.getValue().toString();
						AdministrationService.Util.getInstance().invioCommenti(testo, utente, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore di connessione on invioCommenti()");
								hide();
							}

							@Override
							public void onSuccess(Boolean result) {
								if(result==true){
									Window.alert("Messaggio inviato!");
									hide();
								}else{
									Window.alert("error: Impossibile completare l'operazione.");
								}
								
							}
						
						});
					}else Window.alert("E' necessario inserire del testo!");
						
				}
			});

		    addButton(btnInvia);    
		  }
}
