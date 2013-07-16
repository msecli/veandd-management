package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.*;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ListView;

public class DialogAssociaPtoA  extends Dialog{

	private ListView<PersonaleModel> list1 = new ListView<PersonaleModel>();//Lista sorgente
	ListView<PersonaleModel> list2 = new ListView<PersonaleModel>();//Lista destinazione
	private Button btnAssocia= new Button("Associa");
	private String commessa=new String();
	
	public DialogAssociaPtoA(String comm){
		
		comm=comm.substring(0, comm.indexOf("("));
		commessa=comm;
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(580);
		setHeight(800);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setHeading("Associazione Dipendenti.");
		setModal(true);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);		
						
		ContentPanel cntpnlVista = new ContentPanel(); //Pannello contenente Form 
		cntpnlVista.setHeading("Commessa: "+commessa);
		cntpnlVista.setFrame(false);
		cntpnlVista.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cntpnlVista.setWidth(560);
		cntpnlVista.setHeight(760);
		cntpnlVista.setStyleAttribute("padding", "10px");
		
		ContentPanel cntpnlSelezioni= new ContentPanel(); //Pannello contenente liste dati (Form)
		cntpnlSelezioni.setHeaderVisible(false);
		cntpnlSelezioni.setFrame(false);
		cntpnlSelezioni.setLayout(new FormLayout());
		cntpnlSelezioni.setBorders(false);
		cntpnlSelezioni.setBodyBorder(false);
		cntpnlSelezioni.setStyleAttribute("padding-top", "10px");
		cntpnlSelezioni.setStyleAttribute("padding-left", "10px");
		cntpnlSelezioni.setWidth(540);
		cntpnlSelezioni.setHeight(660);
		
		ContentPanel cp = new ContentPanel(); //pannello contenente le due liste
		cp.setHeaderVisible(false);
		cp.setSize(520, 600);
		cp.setFrame(false);
		cp.setBorders(false);
		cp.setBodyBorder(false);
		cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cp.setStyleAttribute("padding-top", "5px");
		
		ButtonBar buttonBar= new ButtonBar();
		buttonBar.setAlignment(HorizontalAlignment.CENTER);
		buttonBar.setStyleAttribute("margin-top", "10px");
		buttonBar.setStyleAttribute("padding-top", "7px");
		buttonBar.setStyleAttribute("padding-bottom", "5px");
		buttonBar.setBorders(true);
		buttonBar.setWidth(520);
		
		list1.setDisplayProperty("nomeCompleto");
		try {
			getListaPersonale();
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Caricamento lista nomi non effettuato.");
		}
		
		
		list2.setDisplayProperty("nomeCompleto");
		ListStore<PersonaleModel> store = new ListStore<PersonaleModel>();
		store.setStoreSorter(new StoreSorter<PersonaleModel>());
		list2.setStore(store);
		
		new ListViewDragSource(list1);
		new ListViewDragSource(list2);

		new ListViewDropTarget(list1);
		new ListViewDropTarget(list2);

		RowData data = new RowData(.5, 1);
		data.setMargins(new Margins(5));
		
		buttonBar.add(btnAssocia);

		cntpnlSelezioni.add(cp);
		cntpnlSelezioni.add(buttonBar);
		
		cp.add(list1, data);
		cp.add(list2, data);
		
		cntpnlVista.add(cntpnlSelezioni,new RowData());				
		
		bodyContainer.add(cntpnlVista);
		add(bodyContainer);	
		
		
		btnAssocia.addSelectionListener(new SelectionListener<ButtonEvent>() {	
			@Override
			public void componentSelected(ButtonEvent ce) {
				
					List<String> listaDipendenti= new ArrayList<String>();
					String pm= new String();
					
					for (PersonaleModel p: list2.getStore().getModels()){
						listaDipendenti.add(p.getUsername());
					}
					
					AdministrationService.Util.getInstance().editAssociazioneCommessaDipendenti(pm, commessa, listaDipendenti, new AsyncCallback<Boolean>() {
						
						@Override
						public void onSuccess(Boolean result) {
							if(result==false)
								Window.alert("error: Impossibile efettuare l'associazione.");
							else {				
								hide();
							}
						}			

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore connessione on editAssociazioneCommessaDipendenti();");
							caught.printStackTrace();					
						}
					});					
			}
		});		
	}
	
		
	private void getListaPersonale() {		
		AdministrationService.Util.getInstance().getAllPersonaleModel(new AsyncCallback<List<PersonaleModel>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getAllPersonaleModel();");
				caught.printStackTrace();
				
			}

			@Override
			public void onSuccess(List<PersonaleModel> result) {
				if(result!=null){
					
					loadList(result);
					
				}else Window.alert("error: Errore durante l'accesso ai dati Personale.");	
			}		
		});	
	}
	
		
	//caricamento liste view
	private void loadList(List<PersonaleModel> lista) {
		ListStore<PersonaleModel> store = new ListStore<PersonaleModel>();  
	    List<PersonaleModel> listaApp= new ArrayList<PersonaleModel>();
	    String app= new String();
	    String app1=new String();
	    
		for(PersonaleModel p:lista){
			app=p.getTipologiaLavoratore();
			app1=p.getRuolo();
			
			boolean ruolo=(app1.compareTo("DIP")==0)||(app1.compareTo("PM")==0)||(app1.compareTo("DIR")==0);
			//se l'utente ha ruolo DIP PM o DIR comparirà nella lista associazioni
			if(ruolo) 
				listaApp.add(p);					
		}
	    
	    store.setStoreSorter(new StoreSorter<PersonaleModel>());  
	    store.setDefaultSort("nomeCompleto", SortDir.ASC);
	    store.add(listaApp);  
	    list1.setStore(store);
	    list1.refresh();
	}
}
