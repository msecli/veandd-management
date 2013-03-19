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
		FormPanel frmAddPresente= createFormListaOrdini(idCommessa);//form per la scelta di un ordine già presente
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
	    
	    //Asociazione di un ordine già presente alla commessa selezionata
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

/*
	private FormPanel createFormNewOrdine(final String idCommessa) {
		
		final TextArea txtrDescrizione;
		final TextField<String> txtfldTariffaOraria;
		final TextField<String> txtfldIdOrdine=new TextField<String>();
		final TextField<String> txtfldNumeroOrdine=new TextField<String>();
		final DateField dtfldDataInizioOrdine;
		final DateField dtfldDataFineOrdine;
		final TextField<String> txtfldNumeroRisorse;
		final TextField<String> txtfldNumeroOre;
		
		Button btnSend = new Button();
		Button btnReset = new Button();
		final FormPanel frmPanel= new FormPanel();
		
		frmPanel.setFrame(false);
		frmPanel.setHeading("Dati Ordine.");
		frmPanel.setCollapsible(false);
		frmPanel.setLabelWidth(100);
		frmPanel.setHeaderVisible(false);
		frmPanel.setWidth("350px");
		frmPanel.setBorders(false);
		
		frmPanel.add(txtfldIdOrdine, new FormData("45%"));
		txtfldIdOrdine.setFieldLabel("ID Offerta");
		txtfldIdOrdine.setName("idOrdine");
		txtfldIdOrdine.setEnabled(false);
		txtfldIdOrdine.setMaxLength(10);
		txtfldIdOrdine.setVisible(false);
		
		frmPanel.add(txtfldNumeroOrdine, new FormData("85%"));
		txtfldNumeroOrdine.setFieldLabel("Numero Ordine");
		txtfldNumeroOrdine.setName("numeroOrdine");
		txtfldNumeroOrdine.setMaxLength(20);
		txtfldNumeroOrdine.setAllowBlank(false);
			
		smplcmbxNumeroRdo = new SimpleComboBox<String>();
		smplcmbxNumeroRdo.setAllowBlank(false);
		
		try {	
			getRdos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		smplcmbxNumeroRdo.setFieldLabel("Numero RDO");
		smplcmbxNumeroRdo.setName("numeroRda");
		smplcmbxNumeroRdo.setTriggerAction(TriggerAction.ALL);
		frmPanel.add(smplcmbxNumeroRdo, new FormData("85%"));
		
		dtfldDataInizioOrdine = new DateField();
		dtfldDataInizioOrdine.setFieldLabel("Data Inizio");
		dtfldDataInizioOrdine.setAllowBlank(true);
		frmPanel.add(dtfldDataInizioOrdine, new FormData("85%"));
		
		dtfldDataFineOrdine = new DateField();
		dtfldDataFineOrdine.setFieldLabel("Data Fine");
		dtfldDataFineOrdine.setAllowBlank(true);
		frmPanel.add(dtfldDataFineOrdine, new FormData("85%"));
				
		txtrDescrizione = new TextArea();
		txtrDescrizione.setMaxLength(200);
		txtrDescrizione.setFieldLabel("Descrizione");
		txtrDescrizione.setName("descrizione");
		FormData fd_txtrDescrizione = new FormData("100%");
		fd_txtrDescrizione.setMargins(new Margins(10, 0, 0, 0));
		frmPanel.add(txtrDescrizione, fd_txtrDescrizione);
			
		//FieldSet per definire due colonne nel form
		FieldSet fldstNewFieldset = new FieldSet();
	    FormData fd_fldstNewFieldset = new FormData("100%");
	    fd_fldstNewFieldset.setMargins(new Margins(-8, 0, 0, -10));
	    fldstNewFieldset.setStyleAttribute("padding-top", "7px");
	    fldstNewFieldset.setStyleAttribute("margin", "-5px");
	    fldstNewFieldset.setCollapsible(false);
	    fldstNewFieldset.setExpanded(true);
	    fldstNewFieldset.setBorders(false);
	    frmPanel.add(fldstNewFieldset, fd_fldstNewFieldset);
			
		LayoutContainer main = new LayoutContainer();
		main.setStyleAttribute("padding-top", "8px");
	    main.setLayout(new ColumnLayout());  
	    main.setBorders(false);
	    
	    LayoutContainer left=new LayoutContainer();
	    left.setStyleAttribute("paddingRight", "0px");
	    FormLayout layout= new FormLayout();
	    layout.setLabelAlign(LabelAlign.LEFT);
	    layout.setLabelWidth(75);
	    left.setLayout(layout);
	    
	    txtfldTariffaOraria = new TextField<String>();
		txtfldTariffaOraria.setFieldLabel("Tariffa Oraria");
		txtfldTariffaOraria.setName("tariffaOraria");
		txtfldTariffaOraria.setRegex("[0-9]*[.]?[0-9]+");
		txtfldTariffaOraria.getMessages().setRegexText("Deve essere un numero eventualmente con il '.'");
		left.add(txtfldTariffaOraria, new FormData("75%"));
		
		txtfldNumeroRisorse = new TextField<String>();
		txtfldNumeroRisorse.setFieldLabel("Num. Risorse");
		txtfldNumeroRisorse.setName("numeroRisorse");
		txtfldNumeroRisorse.setRegex("[0-9]*");
		txtfldNumeroRisorse.getMessages().setRegexText("Deve essere un numero");
		left.add(txtfldNumeroRisorse, new FormData("75%"));
		
		LayoutContainer right = new LayoutContainer();  
	    layout = new FormLayout();
	    layout.setLabelWidth(75);
	    layout.setLabelAlign(LabelAlign.LEFT);  
	    right.setLayout(layout);
		
		txtfldNumeroOre = new TextField<String>();
		txtfldNumeroOre.setFieldLabel("Num. Ore");
		txtfldNumeroOre.setName("numeroOre");
		txtfldNumeroOre.setRegex("[0-9]*");
		txtfldNumeroOre.getMessages().setRegexText("Deve essere un numero");
		right.add(txtfldNumeroOre, new FormData("85%"));
		
		main.add(left, new ColumnData(165.0));
	    //left.setWidth("50%");
	    main.add(right, new ColumnData(145.0));
	    //right.setWidth("50%");
	    
	    fldstNewFieldset.add(main);
	    frmPanel.add(fldstNewFieldset);
		
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.setAlignment(HorizontalAlignment.CENTER);
		buttonBar.setStyleAttribute("margin-top", "15px");
		buttonBar.setStyleAttribute("padding-top", "5px");
		buttonBar.setStyleAttribute("padding-bottom", "5px");
		buttonBar.setBorders(true);
		
		frmPanel.add(buttonBar, new FormData("100%"));
		
		btnSend.setText("Associa");
		btnSend.setWidth("65px");
		
		//Associazione di un  nuovo ordine alla commessa selezionata
		btnSend.addSelectionListener(new SelectionListener<ButtonEvent>() {	
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				 if(frmPanel.isValid()){
						
						String numOrdine = new String(); //numero d'ordine effettivo, l'id è, invece, automatico da DB
						String numRda= new String();
						String descrizione = new String();
						String tariffaOraria= new String();
						String numeroRisorse= new String();
						String numeroOre= new String();
										
						Date dataInizio=new Date();
						Date dataFine=new Date();
						  	
						if(txtfldNumeroOrdine.getRawValue().isEmpty()){ numOrdine="";}else{numOrdine=txtfldNumeroOrdine.getValue().toString();}
						if(smplcmbxNumeroRdo.getRawValue().isEmpty()){ numRda="";}else{
							String app=new String();
							app=smplcmbxNumeroRdo.getRawValue().toString();
							int index = app.indexOf('(');
						    numRda = app.substring(0, index);
							}
						if(txtrDescrizione.getRawValue().isEmpty()){ descrizione="";}else{descrizione=txtrDescrizione.getValue().toString();}
						if(txtfldTariffaOraria.getRawValue().isEmpty()){ tariffaOraria="0.0";}else{tariffaOraria=txtfldTariffaOraria.getValue().toString();}
						if(txtfldNumeroOre.getRawValue().isEmpty()){ numeroOre="0";}else{numeroOre=txtfldNumeroOre.getValue().toString();}
						if(txtfldNumeroRisorse.getRawValue().isEmpty()){ numeroRisorse="0";}else{numeroRisorse=txtfldNumeroRisorse.getValue().toString();}
											
						dataInizio=dtfldDataInizioOrdine.getValue();
						dataFine= dtfldDataFineOrdine.getValue();
									
						AdministrationService.Util.getInstance().associaOrdineCommessa(idCommessa, numOrdine, numRda, dataInizio, dataFine, descrizione, tariffaOraria, numeroRisorse, numeroOre, new AsyncCallback<Boolean>() {

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
		
		btnReset.setWidth("25px");
		btnReset.setText("X");
		
		btnReset.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				frmPanel.clear();
				
			}
		});
						
		buttonBar.add(btnSend);
		buttonBar.add(btnReset);
		
		return frmPanel;				
	}*/

	
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