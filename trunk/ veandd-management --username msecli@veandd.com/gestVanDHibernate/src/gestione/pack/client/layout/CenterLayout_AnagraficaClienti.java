package gestione.pack.client.layout;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.ClienteModel;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;


public class CenterLayout_AnagraficaClienti extends LayoutContainer {
		
	private Button btnSend;
	private Button btnEdit;
	private Button btnDelete;
	private Button btnReset;
	private TextField<String> txtfldCodRaggr;
	private TextField<String> txtfldEmail;
	private TextField<String> txtfldTelefono;
	private TextField<String> txtfldIndirizzo;
	private TextField<String> txtfldComune;
	private TextField<String> txtfldPartitaIva;
	private TextField<String> txtfldCodiceFiscale;
	private TextField<String> txtfldRagioneSociale;
	private TextField<String> txtfldCodCliente;
	private FieldSet fldstIndirizzo;
	private FieldSet fldstDomiciliazioneBancaria;
	private TextField<String> txtfldCondPagamento;
	private TextField<String> txtfldValuta;
	private TextField<String> txtfldBanca;
	private TextField<String> txtfldAbi;
	private TextField<String> txtfldCAB;
	private TextField<String> txtfldCab;
	private FormPanel frmpnlAnagraficaClienti = new FormPanel();
	private FormPanel frmpnlDomBancaria= new FormPanel();
	private FormPanel frmpnlRaggiungibile= new FormPanel();
	private TextField<String> txtfldProvincia;
	private TextField<String> txtfldStato;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	private TextField<String> txtfldFax;
	private TextField<String> txtfldCap;
	
	private ColumnModel cm;
	private Grid<ClienteModel> grid;
	private FormBinding formBindings;
		
	
	public CenterLayout_AnagraficaClienti() {												
	}
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);

//Creazione del Form per l'inserimento dei dati	    
	    FormPanel formPanel=createForm();
	    	    		
//Creazione del Layout Esterno e del BorderLayout		
		final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();

		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		layoutContainer.setWidth(w-225);
		layoutContainer.setHeight(h-54);
			
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);
		
//Pannelli per l'impostazione dei contenuti		
				
		ContentPanel cntpnlDati = new ContentPanel();
		cntpnlDati.setStyleAttribute("padding-left", "6px");
		cntpnlDati.setHeaderVisible(false);		
		cntpnlDati.setScrollMode(Scroll.NONE);
		cntpnlDati.setBorders(false);
		cntpnlDati.setFrame(true);
		cntpnlDati.setStyleAttribute("padding-left", "15px");
		
		ContentPanel cntpnlGrid = new ContentPanel();  
	    cntpnlGrid.setBodyBorder(true);
	    cntpnlGrid.setHeading("Dati Clienti");  
	    cntpnlGrid.setButtonAlign(HorizontalAlignment.CENTER);  
	    cntpnlGrid.setLayout(new FitLayout());  
	    cntpnlGrid.setHeaderVisible(false);
	    cntpnlGrid.setWidth(665);
	    cntpnlGrid.setHeight(780);
	    cntpnlGrid.setScrollMode(Scroll.AUTOY);
	    	    	
		final ListStore<ClienteModel> store = new ListStore<ClienteModel>();  	
		caricaTabellaDati();
	   	    
	    cm = new ColumnModel(createColumns());
		
	    grid = new Grid<ClienteModel>(store, cm);   
	    grid.setBorders(false);  
	    grid.setStripeRows(true);  
	    grid.setColumnLines(true);  
	    grid.setColumnReordering(true);  
	    grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

	    //Binding tra Form e Tabella
	    grid.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<ClienteModel>>() {  
	          public void handleEvent(SelectionChangedEvent<ClienteModel> be) {          	
	            if (be.getSelection().size() > 0) {   
	              formBindings.bind((ModelData) be.getSelection().get(0));   
	              btnSend.setEnabled(false);
	              btnEdit.setEnabled(true);
	    	      btnDelete.setEnabled(true);
	              txtfldCodCliente.setEnabled(false);
	              
	            } else {  
	              formBindings.unbind();  
	            }
	      
	          }             
	        }); 
	    	   
	    cntpnlGrid.add(grid);  
	    
	    formBindings = new FormBinding(formPanel, true);
	    formBindings.autoBind();
	    formBindings.setStore((Store<ClienteModel>) grid.getStore());
		
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.setStyleAttribute("padding-top", "0px");
		
		btnSend = new Button("Insert");
	    btnSend.setWidth("65px");
	    btnSend.setEnabled(true);
	    buttonBar.add(btnSend);
		
		btnEdit = new Button("Edit");
	    btnEdit.setEnabled(false);
	    buttonBar.add(btnEdit);
	    btnEdit.setWidth("65px");
	    
	    btnDelete = new Button("Delete");
	    btnDelete.setEnabled(false);
	    buttonBar.add(btnDelete);
	    btnDelete.setWidth("65px");
	    
	    btnReset = new Button("X");
	    btnReset.setEnabled(true);
	    buttonBar.add(btnReset);
	    btnReset.setWidth("35px");
			
		ContentPanel cntntpnlParent = new ContentPanel();
		cntntpnlParent.setCollapsible(false);
		cntntpnlParent.setHeaderVisible(true);
		cntntpnlParent.setBorders(false);	
		cntntpnlParent.setWidth(w-215);
		cntntpnlParent.setHeight(h-55);
		cntntpnlParent.setHeading("Anagrafica Clienti");
		cntntpnlParent.setScrollMode(Scroll.AUTOY);
			
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setStyleAttribute("padding", "5px");
		
		ContentPanel cpLayout= new ContentPanel();
		cpLayout.setCollapsible(false);
		cpLayout.setFrame(true);
		cpLayout.setHeaderVisible(false);
		cpLayout.setHeading("Dettaglio Cliente.");
		//cpLayout.setStyleAttribute("padding-left", "7px");
		//cpLayout.setStyleAttribute("margin-top", "5px");
		cpLayout.setWidth(1050);
		cpLayout.setHeight(850);
		
		cpLayout.setButtonAlign(HorizontalAlignment.CENTER);
		cpLayout.addButton(btnSend);
		cpLayout.addButton(btnEdit);
		cpLayout.addButton(btnDelete);
		cpLayout.addButton(btnReset);
		
		//cntpnlDati.add(buttonBar);
		cntpnlDati.add(cntpnlGrid);
		horizontalPanel.add(formPanel);
		horizontalPanel.add(cntpnlDati);
		cpLayout.add(horizontalPanel);
		cntntpnlParent.add(cpLayout);
		bodyContainer.add(cntntpnlParent);
		
		layoutContainer.add(cpLayout, new FitData(3, 3, 3, 3));
		add(layoutContainer);
		
		
//Istruzioni per permettere l'inserimento di nuovi record	
		
		btnSend.addSelectionListener(new SelectionListener<ButtonEvent>() {	
			@Override
			public void componentSelected(ButtonEvent ce){			
				if(frmpnlAnagraficaClienti.isValid()){			
				int codCliente;
	    		String ragSociale=new String();
	    		String codFiscale=new String();
	    		String partitaIVA=new String();
	    		String codRaggr=new String();
	    		String comune=new String();
	    		String provincia=new String();
	    		String stato=new String();
	    		String indirizzo= new String();
	    		String cap= new String();
	    		String telefono= new String();
	    		String fax= new String();
	    		String email= new String();
	    		   	
	    		try{
	    			if(txtfldCodCliente.getRawValue().isEmpty()){ codCliente=0;}else{codCliente=Integer.parseInt(txtfldCodCliente.getValue().toString());}
	    			if(txtfldRagioneSociale.getRawValue().isEmpty()){ ragSociale="";}else{ragSociale=txtfldRagioneSociale.getValue().toString();};
	    			if(txtfldCodiceFiscale.getRawValue().isEmpty()){  codFiscale = "";}else{codFiscale=txtfldCodiceFiscale.getValue().toString();};
	    			if(txtfldPartitaIva.getRawValue().isEmpty()){  partitaIVA = "";}else{partitaIVA=txtfldPartitaIva.getValue().toString();};
	    			if(txtfldCodRaggr.getRawValue().isEmpty()){  codRaggr = "";}else{codRaggr=txtfldCodRaggr.getValue().toString();};
	    			if(txtfldComune.getRawValue().isEmpty()){  comune = "";}else{comune=txtfldComune.getRawValue().toString();};
	    			if(txtfldProvincia.getRawValue().isEmpty()){  provincia = "";}else{provincia=txtfldProvincia.getRawValue().toString();};
	    			if(txtfldStato.getRawValue().isEmpty()){  stato = "";}else{stato=txtfldStato.getRawValue().toString();};
	    			if(txtfldIndirizzo.getRawValue().isEmpty()){  indirizzo = "";}else{indirizzo=txtfldIndirizzo.getValue().toString();};
	    			if(txtfldCap.getRawValue().isEmpty()){  cap = "";}else{ cap=txtfldCap.getValue().toString();};
	    			if(txtfldTelefono.getRawValue().isEmpty()){  telefono = "";}else{ telefono=txtfldTelefono.getValue().toString();};
	    			if(txtfldFax.getRawValue().isEmpty()){  fax = "";}else{ fax=txtfldFax.getValue().toString();};
	    			if(txtfldEmail.getRawValue().isEmpty()){  email = "";}else{ email=txtfldEmail.getValue().toString();};
	    	
	    	
	    			AdministrationService.Util.getInstance().insertDataCliente(codCliente, ragSociale, codFiscale, partitaIVA, codRaggr, comune, provincia, stato, indirizzo, 
	    						cap,  telefono, fax, email, new AsyncCallback<Void>() {

							@Override
							public void onFailure(
									Throwable caught) {
								Window.alert("Errore connessione on InsertData;");
								caught.printStackTrace();
								
							}
							@Override
							public void onSuccess(Void result) {
								Window.alert("OK Inserimento Corretto.");
								caricaTabellaDati();	
							}
						}); //insert data    			
	    		}catch(Exception e)
	    			{System.out.println("Problemi insertClienti()"+ e);}			
				}else{Window.alert("Controllare i campi inseriti!");}				
			} 
		});
			
// end insert
		 
//istruzioni per permettere l'edit dei dati di un cliente

		btnEdit.addSelectionListener(new SelectionListener<ButtonEvent>() {
		
			@Override
			public void componentSelected(ButtonEvent ce){
				
				if(frmpnlAnagraficaClienti.isValid()){
					
				int codCliente;
	    		String ragSociale=new String();
	    		String codFiscale=new String();
	    		String partitaIVA=new String();
	    		String codRaggr=new String();
	    		String comune=new String();
	    		String provincia=new String();
	    		String stato=new String();
	    		String indirizzo= new String();
	    		String cap= new String();
	    		String telefono= new String();
	    		String fax= new String();
	    		String email= new String();
		    	
		    		try{
		    			if(txtfldCodCliente.getRawValue().isEmpty()){ codCliente=0;}else{codCliente=Integer.parseInt(txtfldCodCliente.getValue().toString());}
		    			if(txtfldRagioneSociale.getRawValue().isEmpty()){ ragSociale="";}else{ragSociale=txtfldRagioneSociale.getValue().toString();};
		    			if(txtfldCodiceFiscale.getRawValue().isEmpty()){  codFiscale = "";}else{codFiscale=txtfldCodiceFiscale.getValue().toString();};
		    			if(txtfldPartitaIva.getRawValue().isEmpty()){  partitaIVA = "";}else{partitaIVA=txtfldPartitaIva.getValue().toString();};
		    			if(txtfldCodRaggr.getRawValue().isEmpty()){  codRaggr = "";}else{codRaggr=txtfldCodRaggr.getValue().toString();};
		    			if(txtfldComune.getRawValue().isEmpty()){  comune = "";}else{comune=txtfldComune.getRawValue().toString();};
		    			if(txtfldProvincia.getRawValue().isEmpty()){  provincia = "";}else{provincia=txtfldProvincia.getRawValue().toString();};
		    			if(txtfldStato.getRawValue().isEmpty()){  stato = "";}else{stato=txtfldStato.getRawValue().toString();};
		    			if(txtfldIndirizzo.getRawValue().isEmpty()){  indirizzo = "";}else{indirizzo=txtfldIndirizzo.getValue().toString();};
		    			if(txtfldCap.getRawValue().isEmpty()){  cap = "";}else{ cap=txtfldCap.getValue().toString();};
		    			if(txtfldTelefono.getRawValue().isEmpty()){  telefono = "";}else{ telefono=txtfldTelefono.getValue().toString();};
		    			if(txtfldFax.getRawValue().isEmpty()){  fax = "";}else{ fax=txtfldFax.getValue().toString();};
		    			if(txtfldEmail.getRawValue().isEmpty()){  email = "";}else{ email=txtfldEmail.getValue().toString();};
		    		    //da aggiungere la domiciliazione bancaria
		    			
		    			AdministrationService.Util.getInstance().editDataCliente(codCliente, ragSociale, codFiscale, partitaIVA, codRaggr, comune, provincia, stato, indirizzo, 
	    						cap,  telefono, fax, email, new AsyncCallback<Void>() {

								@Override
								public void onFailure(
										Throwable caught) {
									Window.alert("Errore connessione;");
									caught.printStackTrace();
									
								}

								@Override
								public void onSuccess(Void result) {
									Window.alert("Edit OK");
									caricaTabellaDati();	
								}
							}); //edit data
		    			
		    		}catch(NullPointerException e)
		    			{System.out.println("Problemi Edit "+ e);}
		    	
	    	txtfldCodCliente.enable();
	    	frmpnlAnagraficaClienti.clear();
	    	btnSend.setEnabled(true);
	    	btnEdit.setEnabled(false);
	    	btnDelete.setEnabled(false);
			
				} else {Window.alert("Controllare i dati inseriti!");}			
			}	
		});	    
		
//istruzioni per permettere l'eliminazione di un record		
		btnDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {
		
			@Override
			public void componentSelected(ButtonEvent ce){
				
				try{
        			
					AdministrationService.Util.getInstance().removeDataCliente(Integer.parseInt(txtfldCodCliente.getValue().toString()), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore connessione on removeData;");
								caught.printStackTrace();
								
							}

							@Override
							public void onSuccess(Void result) {
								Window.alert("OK Record Rimosso");
								caricaTabellaDati();
									
							}
						}); //delete data
					
				}catch(NullPointerException e)
					{System.out.println("Problemi Remove "+ e);}
			
    			txtfldCodCliente.enable();
    			btnSend.setEnabled(true);
    			btnEdit.setEnabled(false);
    	    	btnDelete.setEnabled(false);
    			frmpnlAnagraficaClienti.clear();
			}
		
		});
			
//Reset Form
		btnReset.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce){
				
				frmpnlAnagraficaClienti.reset();
	    		btnSend.setEnabled(true);
	    		btnEdit.setEnabled(false);
    	    	btnDelete.setEnabled(false);
	    		txtfldCodCliente.setEnabled(true);
	    		grid.getSelectionModel().deselectAll();
	    		formBindings.unbind();
	    		
			}	
		});
//			

	}


private List<ColumnConfig> createColumns() {
		
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column = new ColumnConfig();  
		column.setId("idCliente");  
	    column.setHeader("ID");  
	    column.setWidth(35);  
	    column.setRowHeader(true);  
	    configs.add(column);  
	    
	    column=new ColumnConfig();		
	    column.setId("ragioneSociale");  
	    column.setHeader("Ragione Sociale");  
	    column.setWidth(150);  
	    column.setRowHeader(true);  
	    configs.add(column);  
	    
	    column=new ColumnConfig();		
	    column.setId("citta");  
	    column.setHeader("Citta'");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("telefono");  
	    column.setHeader("Telefono");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	    configs.add(column);  
	    
	    column=new ColumnConfig();		
	    column.setId("fax");  
	    column.setHeader("Fax");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	    configs.add(column);  
	    
	    column=new ColumnConfig();		
	    column.setId("email");  
	    column.setHeader("Email");  
	    column.setWidth(160);  
	    column.setRowHeader(true);  
	    configs.add(column);  
	    
	    return configs;
		
	}


	private FormPanel createForm() {	
		
		//da aggiungere i nomi ad ogni widget per il binding
		
		frmpnlAnagraficaClienti.setLabelWidth(100);
				    
	    frmpnlAnagraficaClienti.setFrame(false);
	    frmpnlAnagraficaClienti.setBorders(false);
		//frmpnlAnagraficaClienti.setHeading("Dettaglio Cliente");
	    frmpnlAnagraficaClienti.setHeaderVisible(false);
		
		txtfldCodCliente = new TextField<String>();
		txtfldCodCliente.setMaxLength(5);
		txtfldCodCliente.setAllowBlank(false);
		txtfldCodCliente.setFieldLabel("Codice Cliente");
		txtfldCodCliente.setName("idCliente");
		txtfldCodCliente.setEnabled(true);
		txtfldCodCliente.setRegex("[0-9]+");
		txtfldCodCliente.getMessages().setRegexText("Deve essere un numero");
		FormData fd_txtfldCodCliente = new FormData("50%");
		fd_txtfldCodCliente.setMargins(new Margins(0, 0, 0, 0));
		frmpnlAnagraficaClienti.add(txtfldCodCliente, fd_txtfldCodCliente);
				
		txtfldRagioneSociale = new TextField<String>();
		txtfldRagioneSociale.setMaxLength(45);
		txtfldRagioneSociale.setFieldLabel("Ragione Sociale");
		txtfldRagioneSociale.setName("ragioneSociale");
		frmpnlAnagraficaClienti.add(txtfldRagioneSociale, new FormData("90%"));
		
		txtfldCodiceFiscale = new TextField<String>();
		txtfldCodiceFiscale.setMaxLength(16);
		txtfldCodiceFiscale.setName("codFiscale");
		txtfldCodiceFiscale.setFieldLabel("Codice Fiscale");
		frmpnlAnagraficaClienti.add(txtfldCodiceFiscale, new FormData("90%"));
		
		txtfldPartitaIva = new TextField<String>();
		txtfldPartitaIva.setMaxLength(45);
		frmpnlAnagraficaClienti.add(txtfldPartitaIva, new FormData("90%"));
		txtfldPartitaIva.setFieldLabel("Partita IVA");
		txtfldPartitaIva.setName("partitaIVA");
		
		txtfldCodRaggr = new TextField<String>();
		txtfldCodRaggr.setMaxLength(45);
		FormData fd_txtfldCodRaggr = new FormData("90%");
		fd_txtfldCodRaggr.setMargins(new Margins(0, 0, 5, 0));
		frmpnlAnagraficaClienti.add(txtfldCodRaggr, fd_txtfldCodRaggr);
		txtfldCodRaggr.setFieldLabel("Cod. Raggr.");
		txtfldCodRaggr.setName("codRaggr");
		
		fldstIndirizzo = new FieldSet();
		
		frmpnlRaggiungibile.setLabelWidth(80);
		frmpnlRaggiungibile.setHeaderVisible(false);
		
		txtfldComune = new TextField<String>();
		txtfldComune.setMaxLength(45);
		frmpnlRaggiungibile.add(txtfldComune, new FormData("95%"));
		txtfldComune.setFieldLabel("Comune");
		txtfldComune.setName("citta");
		
		txtfldProvincia = new TextField<String>();
		txtfldProvincia.setMaxLength(2);
		frmpnlRaggiungibile.add(txtfldProvincia, new FormData("55%"));
		txtfldProvincia.setFieldLabel("Provincia");
		txtfldProvincia.setName("provincia");
		
		txtfldStato = new TextField<String>();
		txtfldStato.setMaxLength(45);
		frmpnlRaggiungibile.add(txtfldStato, new FormData("95%"));
		txtfldStato.setFieldLabel("Stato");
		txtfldStato.setName("stato");
		
		txtfldIndirizzo = new TextField<String>();
		txtfldIndirizzo.setMaxLength(45);
		frmpnlRaggiungibile.add(txtfldIndirizzo, new FormData("95%"));
		txtfldIndirizzo.setFieldLabel("Indirizzo");
		txtfldIndirizzo.setName("indirizzo");
		
		txtfldCap = new TextField<String>();
		txtfldCap.setMaxLength(6);
		frmpnlRaggiungibile.add(txtfldCap, new FormData("55%"));
		txtfldCap.setFieldLabel("CAP");
		txtfldCap.setRegex("[0-9]*");
		txtfldCap.getMessages().setRegexText("Deve essere un numero.");
		txtfldCap.setName("cap");
		
		txtfldTelefono = new TextField<String>();
		txtfldTelefono.setMaxLength(15);
		frmpnlRaggiungibile.add(txtfldTelefono, new FormData("95%"));
		txtfldTelefono.setFieldLabel("Telefono");
		txtfldTelefono.setName("telefono");
		txtfldTelefono.setRegex("[+]?[0-9]*");
		txtfldTelefono.getMessages().setRegexText("Deve essere un numero eventualmente preceduto dal +");
		
		txtfldFax = new TextField<String>();
		txtfldFax.setMaxLength(15);
		frmpnlRaggiungibile.add(txtfldFax, new FormData("95%"));
		txtfldFax.setFieldLabel("FAX");
		txtfldFax.setName("fax");
		txtfldFax.setRegex("[+]?[0-9]*");
		txtfldFax.getMessages().setRegexText("Deve essere un numero eventualmente preceduto dal +");
		
		txtfldEmail = new TextField<String>();
		frmpnlRaggiungibile.add(txtfldEmail, new FormData("95%"));
		txtfldEmail.setFieldLabel("Email");
		txtfldEmail.setName("email");
		txtfldEmail.setRegex("[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.]{1}[A-Za-z]{2,4}");
		txtfldEmail.getMessages().setRegexText("Deve essere un indirizzo email in un formato corretto!");
		
		fldstIndirizzo.add(frmpnlRaggiungibile);
		
		frmpnlAnagraficaClienti.add(fldstIndirizzo, new FormData("100%"));
		fldstIndirizzo.setHeading("Domiciliazione");
		fldstIndirizzo.setCollapsible(true);
		
		fldstDomiciliazioneBancaria = new FieldSet();
		fldstDomiciliazioneBancaria.setExpanded(false);
		
		frmpnlDomBancaria.setLabelWidth(80);
		frmpnlDomBancaria.setHeaderVisible(false);
				
		txtfldBanca = new TextField<String>();
		txtfldBanca.setMaxLength(45);
		FormData fd_txtfldBanca = new FormData("80%");
		fd_txtfldBanca.setMargins(new Margins(0, 0, 0, 0));
		frmpnlDomBancaria.add(txtfldBanca, fd_txtfldBanca);
		txtfldBanca.setFieldLabel("Banca");
		
		txtfldValuta = new TextField<String>();
		txtfldValuta.setMaxLength(20);
		frmpnlDomBancaria.add(txtfldValuta, new FormData("60%"));
		txtfldValuta.setFieldLabel("Valuta");
		
		txtfldAbi = new TextField<String>();
		txtfldAbi.setMaxLength(5);
		frmpnlDomBancaria.add(txtfldAbi, new FormData("60%"));
		txtfldAbi.setFieldLabel("ABI");
		
		txtfldCab = new TextField<String>();
		txtfldCab.setMaxLength(5);
		frmpnlDomBancaria.add(txtfldCab, new FormData("60%"));
		txtfldCab.setFieldLabel("CAB");
		
		txtfldCAB = new TextField<String>();
		txtfldCAB.setMaxLength(1);
		frmpnlDomBancaria.add(txtfldCAB, new FormData("60%"));
		txtfldCAB.setFieldLabel("CIN");
		
		txtfldCondPagamento = new TextField<String>();
		frmpnlDomBancaria.add(txtfldCondPagamento, new FormData("80%"));
		txtfldCondPagamento.setFieldLabel("Cond. Pagamento");
		
		fldstDomiciliazioneBancaria.add(frmpnlDomBancaria);
		frmpnlAnagraficaClienti.add(fldstDomiciliazioneBancaria, new FormData("100%"));
		fldstDomiciliazioneBancaria.setHeading("Domiciliazione Bancaria");
		fldstDomiciliazioneBancaria.setCollapsible(true);
		fldstDomiciliazioneBancaria.setExpanded(true);
		frmpnlAnagraficaClienti.setWidth("320");
		return frmpnlAnagraficaClienti;
	}
	
	
	private void caricaTabellaDati() {
		
		try{
			
			AdministrationService.Util.getInstance().getAllClientiModel(new AsyncCallback<List<ClienteModel>>() {
			
				@Override
				public void onSuccess(List<ClienteModel> result) {
					try {
						loadTable(result);
						
					} catch (Exception e) {
						System.out.println("ErroreAdd"+e);
					}
				
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getClienti();");
					caught.printStackTrace();
				
				}
						
			}); //AsyncCallback
		}catch(NullPointerException e)
		{System.out.println("Problemi Caricamento Dati in Tabella "+ e);}
	}
	
	
	private void loadTable(List<ClienteModel> lista) {
		
		ListStore<ClienteModel>store = new ListStore<ClienteModel>();
		store.add(lista);
		grid.reconfigure(store, cm);
		
	}
}

