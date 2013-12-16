package gestione.pack.client.layout.panel;

import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.CommessaModel;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DialogAssociaCommessaToOrdine extends Dialog{

	private ComboBox<CommessaModel> smplcmbxListaCommesse;
	private String numeroOrdine;
	
	public DialogAssociaCommessaToOrdine(String numeroOrdine){
		
		this.numeroOrdine=numeroOrdine;
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(420);
		setHeight(180);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setHeading("Associazione Commessa.");
		setModal(true);
		
		FieldSet fldstFormNew= new FieldSet();
		fldstFormNew.setExpanded(true);
		fldstFormNew.setBorders(false);
		fldstFormNew.setCollapsible(true);
		fldstFormNew.setStyleAttribute("margin-left", "7px");
		fldstFormNew.setStyleAttribute("margin-top", "12px");
		fldstFormNew.setHeading("Nuovo Ordine");
		
		FieldSet fldstFormListaOrdini= new FieldSet();
		fldstFormListaOrdini.setExpanded(true);
		fldstFormListaOrdini.setBorders(false);
		fldstFormListaOrdini.setCollapsible(true);
		fldstFormListaOrdini.setStyleAttribute("margin-left", "7px");
		fldstFormListaOrdini.setStyleAttribute("margin-top", "12px");
		fldstFormListaOrdini.setHeading("Lista Ordini");
		
		FormPanel frmAddPresente= createFormListaCommesse();//form per la scelta di un ordine già presente
		frmAddPresente.setBorders(false);
		
		VerticalPanel vp= new VerticalPanel();
		LayoutContainer container= new LayoutContainer();
		
		ContentPanel panel= new ContentPanel();
		panel.setWidth(390);
		panel.setBorders(false);
		panel.setHeaderVisible(false);
		panel.setExpanded(true);
		panel.setCollapsible(false);
		panel.setHeading("Associazione Commessa.");
		panel.setFrame(false);
		
		//fldstFormNew.add(formInsertNew);
		fldstFormListaOrdini.add(frmAddPresente);
		
		//vp.add(fldstFormNew);
		vp.add(fldstFormListaOrdini);
		panel.add(vp);
		
		container.add(panel);
		add(container);	
		
	}
	
	
	private FormPanel createFormListaCommesse() {
		
		final FormPanel frmPanel= new FormPanel();
		frmPanel.setFrame(false);
		frmPanel.setHeaderVisible(false);
		frmPanel.setHeading("Lista Commesse.");
		frmPanel.setCollapsible(false);
		frmPanel.setLabelWidth(100);
		frmPanel.setWidth("350px");
		frmPanel.setBorders(false);
		
		FieldSet fldstRdo = new FieldSet();
	    FormData fd_fldstRdo = new FormData("100%");
	    fd_fldstRdo.setMargins(new Margins(-5, 0, -5, -10));
	    fldstRdo.setCollapsible(false);
	    fldstRdo.setExpanded(true);
	    fldstRdo.setBorders(false);
	    fldstRdo.setWidth("350px");
		
		LayoutContainer main1 = new LayoutContainer();
		main1.setLayout(new ColumnLayout());  
	    main1.setBorders(false);
	    main1.setWidth("350px");
	    
	    LayoutContainer left1=new LayoutContainer();
	    FormLayout layout1= new FormLayout();
	    layout1.setLabelAlign(LabelAlign.LEFT);
	    layout1.setLabelWidth(75);
	    left1.setLayout(layout1);
		
	    ListStore<CommessaModel> store=new ListStore<CommessaModel>();
	    smplcmbxListaCommesse=new ComboBox<CommessaModel>();
	    smplcmbxListaCommesse.setStore(store);		
		smplcmbxListaCommesse.setTriggerAction(TriggerAction.ALL);
		try {	
			getListaCommesse();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		smplcmbxListaCommesse.setFieldLabel("Commesse");
		smplcmbxListaCommesse.setName("commessa");
		smplcmbxListaCommesse.setDisplayField("commessa");
		smplcmbxListaCommesse.setTriggerAction(TriggerAction.ALL);
		smplcmbxListaCommesse.setAllowBlank(false);
		left1.add(smplcmbxListaCommesse, new FormData("85%"));
		
		LayoutContainer right1 = new LayoutContainer();  
	    layout1 = new FormLayout();
	    right1.setLayout(layout1);
	    
	    Button btnR=new Button("Associa");
	    btnR.setWidth("65px");
	    
	    //Asociazione di un ordine già presente alla commessa selezionata
	    btnR.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				if(frmPanel.isValid()){
									
					int idCommessa=smplcmbxListaCommesse.getValue().get("idCommessa");
					AdministrationService.Util.getInstance().associaOrdinePresenteCommessa(String.valueOf(idCommessa), numeroOrdine, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on InsertData();");
						caught.printStackTrace();
						
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result==true){
							hide();				
						}
						else Window.alert("error: Inserimento sulla tabella Ordini non effettuato!");
							
					}
				});	    //AsyncCallback()    		        
	        
		      }else{Window.alert("Controllare i campi inseriti!");}	
				
			}
		});
	    
	    right1.add(btnR);
	    
	    main1.add(left1, new ColumnData(240.0));	    
	    main1.add(right1, new ColumnData(100.0));
	   
	    fldstRdo.add(main1);   
	    frmPanel.add(fldstRdo, fd_fldstRdo);
	    
		return frmPanel;	
	}


	private void getListaCommesse() {
		
		AdministrationService.Util.getInstance().getCommesseAperteSenzaOrdine( new AsyncCallback<List<CommessaModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getCommesseByPM();");
				caught.printStackTrace();				
			}

			@Override
			public void onSuccess(List<CommessaModel> result) {
				if(result!=null){
					ListStore<CommessaModel> lista= new ListStore<CommessaModel>();
					lista.setStoreSorter(new StoreSorter<CommessaModel>());  
					lista.setDefaultSort("commessa", SortDir.ASC);
					
					lista.add(result);				
					smplcmbxListaCommesse.clear();
					smplcmbxListaCommesse.setStore(lista);
				}else Window.alert("error: Errore durante l'accesso ai dati Commesse.");
				
			}
		});
		
	}
	
}
