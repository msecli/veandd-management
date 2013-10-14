package gestione.pack.client.layout.panel;

import java.util.List;

import gestione.pack.client.AdministrationService;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.extjs.gxt.ui.client.widget.ContentPanel;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;


public class DialogAssociaOrdine extends Dialog{
	
	private SimpleComboBox<String> smplcmbxNumeroRdo;
	SimpleComboBox<String> smplcmbxOrdini = new SimpleComboBox<String>();
	
	public DialogAssociaOrdine(String idCommessa){
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(420);
		setHeight(180);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setHeading("Associazione Ordine.");
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
		
		//FormPanel formInsertNew= createFormNewOrdine(idCommessa);//form per inserire un nuovo ordine
		//formInsertNew.setBorders(false);
		FormPanel frmAddPresente= createFormListaOrdini(idCommessa);//form per la scelta di un ordine gi� presente
		frmAddPresente.setBorders(false);
		
		VerticalPanel vp= new VerticalPanel();
		LayoutContainer container= new LayoutContainer();
		
		ContentPanel panel= new ContentPanel();
		panel.setWidth(390);
		panel.setBorders(false);
		panel.setHeaderVisible(false);
		panel.setExpanded(true);
		panel.setCollapsible(false);
		panel.setHeading("Associazione Ordine.");
		panel.setFrame(false);
		
		//fldstFormNew.add(formInsertNew);
		fldstFormListaOrdini.add(frmAddPresente);
		
		//vp.add(fldstFormNew);
		vp.add(fldstFormListaOrdini);
		panel.add(vp);
		
		container.add(panel);
		add(container);	
	}
		
	
	private FormPanel createFormListaOrdini(final String idCommessa) {
		
		final FormPanel frmPanel= new FormPanel();
		frmPanel.setFrame(false);
		frmPanel.setHeaderVisible(false);
		frmPanel.setHeading("Lista Ordini.");
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
		
		smplcmbxOrdini.setName("ordine");
		smplcmbxOrdini.setTriggerAction(TriggerAction.ALL);
		try {	
			getOrdini();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		smplcmbxOrdini.setFieldLabel("Ordine");
		smplcmbxOrdini.setName("numeroRda");
		smplcmbxOrdini.setTriggerAction(TriggerAction.ALL);
		smplcmbxOrdini.setAllowBlank(false);
		left1.add(smplcmbxOrdini, new FormData("85%"));
		
		LayoutContainer right1 = new LayoutContainer();  
	    layout1 = new FormLayout();
	    right1.setLayout(layout1);
	    
	    Button btnR=new Button("Associa");
	    btnR.setWidth("65px");
	    
	    //Asociazione di un ordine gi� presente alla commessa selezionata
	    btnR.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				if(frmPanel.isValid()){
																	
					AdministrationService.Util.getInstance().associaOrdinePresenteCommessa(idCommessa, smplcmbxOrdini.getRawValue().toString(), new AsyncCallback<Boolean>() {

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


	
	public void getRdos(){
		
		AdministrationService.Util.getInstance().getAllNumeroRdo(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione;");
				caught.printStackTrace();
				
			}

			@Override
			public void onSuccess(List<String> result) {
				if(result != null)
					{
						smplcmbxNumeroRdo.add(result);
						smplcmbxNumeroRdo.recalculate();
					}else Window.alert("error: Impossibile aggiornare la Lista.");		
			}
		});	
	}
	
	
	private void getOrdini() {
		
		AdministrationService.Util.getInstance().getAllListaOrdini(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione;");
				caught.printStackTrace();
				
			}

			@Override
			public void onSuccess(List<String> result) {
				if(result != null)
					{
						smplcmbxOrdini.add(result);
						smplcmbxOrdini.recalculate();
					}else Window.alert("error: Impossibile aggiornare la Lista Ordini.");		
			}
		});	
	}

}