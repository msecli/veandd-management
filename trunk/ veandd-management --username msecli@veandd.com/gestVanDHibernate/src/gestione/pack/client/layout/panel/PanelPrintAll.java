package gestione.pack.client.layout.panel;


import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.ConstantiMSG;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.server.Constanti;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.sun.org.apache.bcel.internal.generic.NEW;

/*
 * Stampa del riepilogo delle ore, nel mese, discriminato per sede operativa 
 * */

public class PanelPrintAll extends LayoutContainer {
	
	public PanelPrintAll(){}
	
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private com.google.gwt.user.client.ui.FormPanel fp1= new com.google.gwt.user.client.ui.FormPanel();
	private SimpleComboBox<String> smplcmbxAnno;
    private SimpleComboBox<String> smplcmbxMese;
    private SimpleComboBox<String> smplcmbxSede= new SimpleComboBox<String>();
    private ComboBox<PersonaleModel> cmbxDipendente= new ComboBox<PersonaleModel>();
	
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	 
	    final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
				
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);
	    
	    ContentPanel cp= new ContentPanel();
	    cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
	    cp.setHeading("Stampa del riepilogo ore di tutti i Dipendenti");
	    cp.setFrame(true);
	    cp.setSize(500, 80);
	    		
	    HorizontalPanel hpAll= new HorizontalPanel();
	    hpAll.setSpacing(6);
	    
	    smplcmbxMese= new SimpleComboBox<String>();
	    smplcmbxMese.setFieldLabel("Mese");
		smplcmbxMese.setName("mese");
		smplcmbxMese.setEmptyText("Mese..");
		smplcmbxMese.setAllowBlank(false);
		 for(String l : DatiComboBox.getMese()){
			 smplcmbxMese.add(l);}
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		
		smplcmbxAnno = new SimpleComboBox<String>();
		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setWidth(75);
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setAllowBlank(false);
		 for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);}
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		
		smplcmbxSede.setFieldLabel("Sede");
		smplcmbxSede.setWidth(65);
		smplcmbxSede.setEmptyText("Sede..");
		smplcmbxSede.setAllowBlank(false);
		smplcmbxSede.add("T");
		smplcmbxSede.add("B");
	    		
	    com.google.gwt.user.client.ui.Button btnPrint = new com.google.gwt.user.client.ui.Button("Stampa");
	    btnPrint.setSize("55px","25px");	   
	    btnPrint.addClickHandler(new SubmitClickHandler());    
	    
    	fp.setMethod(FormPanel.METHOD_POST);
	    fp.setAction(url);
	    fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
	    fp.add(btnPrint);
	    
	    btnPrint.setWidth("100%");
	    
	    hpAll.add(smplcmbxAnno);
	  	hpAll.add(smplcmbxMese);
	  	hpAll.add(smplcmbxSede);
	  	hpAll.add(fp);
	  	
	  	cp.add(hpAll);
	  	
	  	
	  	//--------------------------------------------------STAMPA DEL SINGOLO------------------------------------
	  	ContentPanel cp1= new ContentPanel();
	    cp1.setLayout(new RowLayout(Orientation.HORIZONTAL));
	    cp1.setHeading("Stampa del riepilogo ore di un singolo Dipendente");
	    cp1.setFrame(true);
	    cp1.setSize(500, 95);
	    cp1.setStyleAttribute("padding-top", "15px");
	    
	    ListStore<PersonaleModel> store=new ListStore<PersonaleModel>();
		cmbxDipendente.setStore(store);
		cmbxDipendente.setFieldLabel("Dipendente");
		cmbxDipendente.setEnabled(true);
		cmbxDipendente.setEmptyText("Selezionare il dipendente..");
		cmbxDipendente.setEditable(false);
		cmbxDipendente.setVisible(true);
		cmbxDipendente.setTriggerAction(TriggerAction.ALL);
		cmbxDipendente.setAllowBlank(false);
		cmbxDipendente.setDisplayField("nomeCompleto");
		cmbxDipendente.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {					
					getAllDipendenti();					
			}		
		});
		
		smplcmbxMese= new SimpleComboBox<String>();
	    smplcmbxMese.setFieldLabel("Mese");
		smplcmbxMese.setName("mese");
		smplcmbxMese.setEmptyText("Mese..");
		smplcmbxMese.setAllowBlank(false);
		 for(String l : DatiComboBox.getMese()){
			 smplcmbxMese.add(l);}
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		
		smplcmbxAnno = new SimpleComboBox<String>();
		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setWidth(75);
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setAllowBlank(false);
		 for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);}
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
	    
		HorizontalPanel hpSingolo= new HorizontalPanel();
	    hpSingolo.setSpacing(6);
	    
	    com.google.gwt.user.client.ui.Button btnPrint1 = new com.google.gwt.user.client.ui.Button("Stampa");
	    btnPrint1.setSize("55px","25px");	   
	    btnPrint1.addClickHandler(new SubmitClickHandlerONE());
	    
	    fp1.setMethod(FormPanel.METHOD_POST);
	    fp1.setAction(url);
	    fp1.addSubmitCompleteHandler(new FormSubmitCompleteHandlerONE());  
	    fp1.add(btnPrint1);
	    
	    btnPrint1.setWidth("100%");
	    hpSingolo.add(smplcmbxAnno);
	  	hpSingolo.add(smplcmbxMese);
	  	hpSingolo.add(cmbxDipendente);
	  	hpSingolo.add(fp1);
	  	
	  	cp1.add(hpSingolo);
	    
	    bodyContainer.add(cp);
	    bodyContainer.add(cp1);
		
		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);    
	}
	
		
	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {

		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			
			Window.open("/FileStorage/RiepilogoTotali.pdf", "_blank", "1");
			/*if(event.getResults().isEmpty()){
				Window.open(ConstantiMSG.PATHAmazon+"FileStorage/RiepilogoTotali.pdf", "_blank", "1");
			}
			else{			
				Window.open(ConstantiMSG.PATHAmazon+"FileStorage/RiepilogoTotali.pdf", "_blank", "1");	
			}*/
		}
	}
	
	private final class SubmitClickHandler implements ClickHandler {

		@Override
		public void onClick(final ClickEvent event) {
			
			String data= new String();
			String anno=smplcmbxAnno.getRawValue().toString();
			String meseCorrente=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
			data=meseCorrente+anno;
			String sedeOperativa=smplcmbxSede.getRawValue().toString();
			//String username="";
			
			if(smplcmbxAnno.isValid()&&smplcmbxMese.isValid())
			SessionManagementService.Util.getInstance().setDataInSession(data, sedeOperativa ,"", "ALL", new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error on setDataInSession()");					
				}

				@Override
				public void onSuccess(Boolean result) {
					if(result)
						fp.submit();
					else
						Window.alert("Problemi durante il settaggio dei parametri in Sessione (http)");
				}
			});				
			else 
				Window.alert("Controllare i dati inseriti!");
		}
	}
	
	
	private final class SubmitClickHandlerONE implements ClickHandler {

		@Override
		public void onClick(final ClickEvent event) {
			
			String data= new String();
			String anno=smplcmbxAnno.getRawValue().toString();
			String meseCorrente=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
			data=meseCorrente+anno;
			String username=cmbxDipendente.getValue().getUsername().toString();
			
			
			if(smplcmbxAnno.isValid()&&smplcmbxMese.isValid())
			SessionManagementService.Util.getInstance().setDataInSession(data, "" , username, "ONE", new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error on setDataInSession()");					
				}

				@Override
				public void onSuccess(Boolean result) {
					if(result)
						fp.submit();
					else
						Window.alert("Problemi durante il settaggio dei parametri in Sessione (http)");
				}
			});				
			else 
				Window.alert("Controllare i dati inseriti!");
		}
	}
	
	
	private class FormSubmitCompleteHandlerONE implements SubmitCompleteHandler {

		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			
			String nome= cmbxDipendente.getValue().getUsername().toString();
			
			Window.open("/FileStorage/RiepilogoOre_"+nome+".pdf", "_blank", "1");
			/*if(event.getResults().isEmpty()){
				Window.open(ConstantiMSG.PATHAmazon+"FileStorage/RiepilogoTotali.pdf", "_blank", "1");
			}
			else{			
				Window.open(ConstantiMSG.PATHAmazon+"FileStorage/RiepilogoTotali.pdf", "_blank", "1");	
			}*/
		}
	}
	
	
	private void getAllDipendenti() {	
		AdministrationService.Util.getInstance().getListaDipendentiModel("UG", new AsyncCallback<List<PersonaleModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getCommesseByPM();");
				caught.printStackTrace();		
			}

			@Override
			public void onSuccess(List<PersonaleModel> result) {
				if(result!=null){		
					ListStore<PersonaleModel> lista= new ListStore<PersonaleModel>();
					lista.setStoreSorter(new StoreSorter<PersonaleModel>());  
					lista.setDefaultSort("nomeCompleto", SortDir.ASC);
					
					lista.add(result);				
					cmbxDipendente.clear();
					cmbxDipendente.setStore(lista);
					
				}else Window.alert("error: Errore durante l'accesso ai dati Personale.");				
			}
		});
	}	
	
}