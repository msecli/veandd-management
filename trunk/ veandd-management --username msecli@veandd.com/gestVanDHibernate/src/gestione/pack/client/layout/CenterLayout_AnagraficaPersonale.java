package gestione.pack.client.layout;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.utility.DatiComboBox;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.binding.SimpleComboBoxFieldBinding;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;

public class CenterLayout_AnagraficaPersonale extends LayoutContainer {
		
	private TextField<String> txtfldNome;
	private TextField<String> txtfldCognome;
	private TextField<String> txtfldUsername;
	private TextField<String> txtfldPassword;
	private TextField<String> txtfldBadge;
	private SimpleComboBox <String> smplcmbxTipoOrario;
	private SimpleComboBox <String> smplcmbxTipoLavoratore;
	private SimpleComboBox <String> smplcmbxRuolo;
	private SimpleComboBox <String> smplcmbxSede;
	private SimpleComboBox <String> smplcmbxSedeOperativa;
	private SimpleComboBox<String> smplcmbxGruppolavoro;
	private TextField<String> txtfldCostoOrario;
	private TextField<String> txtfldCostoStruttura;
	private TextField<String> txtfldOreDirette;
	private TextField<String> txtfldOreIndirette;
	private TextField<String> txtfldPermessi;
	private TextField<String> txtfldFerie;
	private TextField<String> txtfldExt;
	private TextField<String> txtfldOreRecupero;
	private FormPanel frmpnlAnagrPersonale;
	private PagingToolBar toolBar = new PagingToolBar(20);
		
	//private Button btnLoad;
	private Button btnReset;
	private Button btnSend;
	private Button btnEdit;
	private Button btnDelete;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();	
	
	private ListStore<PersonaleModel> store=new ListStore<PersonaleModel>();
	private ColumnModel cm;
	private Grid<PersonaleModel> grid;
	private FormBinding formBindings;
	private NumberField nmbrfldId;
	private ContentPanel cntntpnlParent;
	private HorizontalPanel horizontalPanel;
	
	//Creazione del Form per l'inserimento dei dati	    
    private FormPanel formPanel=createForm();
	
	public CenterLayout_AnagraficaPersonale() {
		setBorders(false);										
	}
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);	
	    
//Creazione del Layout Esterno e del BorderLayout			
	    final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();

		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		//layoutContainer.setWidth(w-225);
		//layoutContainer.setHeight(h-54);
			
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
		
		ContentPanel cntpnlGrid = new ContentPanel();  
	    cntpnlGrid.setBodyBorder(false);  
	    cntpnlGrid.setButtonAlign(HorizontalAlignment.CENTER);  
	    cntpnlGrid.setLayout(new FitLayout());  
	    cntpnlGrid.setHeaderVisible(false);
	    cntpnlGrid.setBorders(false);
	    cntpnlGrid.setWidth(655);
	    cntpnlGrid.setHeight(760);
	    cntpnlGrid.setScrollMode(Scroll.AUTOY);   
	    cntpnlGrid.setBottomComponent(toolBar);

	    caricaTabellaDati();
	    
//Definizione colonne griglia    
	    cm = new ColumnModel(createColumns());	
	    store.setDefaultSort("cognome", SortDir.ASC);
	    
	    grid = new Grid<PersonaleModel>(store, cm); 
	    grid.setBorders(false);  
	    grid.setStripeRows(true);  
	    grid.setColumnLines(true);  
	    grid.setColumnReordering(true);  
	    
	    grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	   	      
	    grid.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<PersonaleModel>>() {  
	          public void handleEvent(SelectionChangedEvent<PersonaleModel> be) {  
	        	
	            if (be.getSelection().size() > 0) {      
	              
	              formBindings.bind((ModelData) be.getSelection().get(0));
	              btnSend.setEnabled(false);
	              btnDelete.setEnabled(true);
	              btnEdit.setEnabled(true);
	              
	            } else {  
	            	
	              formBindings.unbind();  
	            }	            
	          }             
	        }); 
	    	   
	    cntpnlGrid.add(grid);  
	    
//Istruzioni necessarie a fornire il binding tra form e tabella con binding esplicito per le comboBox
	    formBindings = new FormBinding(formPanel, true);
	    formBindings.addFieldBinding(new SimpleComboBoxFieldBinding(smplcmbxRuolo, "ruolo"));
	    formBindings.addFieldBinding(new SimpleComboBoxFieldBinding(smplcmbxTipoOrario, "tipologiaOrario"));
	    formBindings.addFieldBinding(new SimpleComboBoxFieldBinding(smplcmbxTipoLavoratore, "tipologiaLavoratore"));
	    formBindings.addFieldBinding(new SimpleComboBoxFieldBinding(smplcmbxSede, "sede"));
	    formBindings.addFieldBinding(new SimpleComboBoxFieldBinding(smplcmbxSedeOperativa, "sedeOperativa"));
	    formBindings.addFieldBinding(new SimpleComboBoxFieldBinding(smplcmbxGruppolavoro, "gruppoLavoro"));    
	    formBindings.setStore((Store<PersonaleModel>) grid.getStore());
//	    
		
/*/Pannello per inserimento curriculum		
		FormPanel frmpnlFileUp=new FormPanel(); 
		frmpnlFileUp.setEncoding(Encoding.MULTIPART);
		frmpnlFileUp.setMethod(Method.POST);
		
		flpldfldCurriculum = new FileUploadField();
		frmpnlFileUp.add(flpldfldCurriculum, new FormData("80%"));
		flpldfldCurriculum.setFieldLabel("Curriculum");
		
		String fileName= FileUploadField.getStyleName(null);
		north.add(frmpnlFileUp);
*/		
		
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
	    
		/*cntntpnlParent = new ContentPanel();
		cntntpnlParent.setCollapsible(false);
		cntntpnlParent.setHeaderVisible(true);
		cntntpnlParent.setBorders(false);	
		cntntpnlParent.setWidth(w-215);
		cntntpnlParent.setHeight(h-55);
		cntntpnlParent.setHeading("Anagrafica Dipendenti.");
		cntntpnlParent.setScrollMode(Scroll.AUTOY);*/
			
		horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(10);
		
		//cntpnlDati.add(buttonBar);
		cntpnlDati.add(cntpnlGrid);
		horizontalPanel.add(formPanel);
		horizontalPanel.add(cntpnlDati);
		
		ContentPanel cpLayout= new ContentPanel();
		cpLayout.setCollapsible(false);
		cpLayout.setFrame(true);
		cpLayout.setHeaderVisible(false);
		cpLayout.setButtonAlign(HorizontalAlignment.CENTER);
		cpLayout.setHeading("Dettaglio Dipendente.");
		//cpLayout.setStyleAttribute("padding-left", "7px");
		//cpLayout.setStyleAttribute("margin-top", "5px");
		cpLayout.setWidth(1030);
		cpLayout.setHeight(850);
		cpLayout.addButton(btnSend);
		cpLayout.addButton(btnEdit);
		cpLayout.addButton(btnDelete);
		cpLayout.addButton(btnReset);
		
		cpLayout.add(horizontalPanel);
		//cntntpnlParent.add(cpLayout);
		bodyContainer.add(cpLayout);
		
		layoutContainer.add(bodyContainer, new FitData(3, 3, 3, 3));
		
		add(layoutContainer);
		    
//Istruzioni per permettere l'inserimento dei dati    
	    btnSend.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
			  if(frmpnlAnagrPersonale.isValid()){
				  
			  	String nome= new String();
	    		String cognome=new String();
	    		String badge=new String();
	    		String username=new String();
	    		String password=new String();
	    		String tipoL=new String();
	    		String tipoO=new String();
	    		String ruolo=new String();
	    		String gruppoL= new String();
	    		String sede= new String();
	    		String sedeOperativa= new String();
	    		String oreDirette= new String();
	    		String oreIndirette= new String();
	    		String permessi= new String();
	    		String ferie= new String();
	    		String ext= new String();
	    		String costoO= new String();
	    		String costoS= new String();
	    		String oreRecupero=new String();
	    	
	    		try{
	    			if(txtfldNome.getRawValue().isEmpty()){ nome="";}else{nome=txtfldNome.getValue().toString();}
	    			if(txtfldCognome.getRawValue().isEmpty()){ cognome="";}else{cognome=txtfldCognome.getValue().toString();};
	    			if(txtfldBadge.getRawValue().isEmpty()){  badge = "";}else{badge=txtfldBadge.getValue().toString();};
	    			if(txtfldUsername.getRawValue().isEmpty()){  username = "";}else{username=txtfldUsername.getValue().toString();};
	    			if(txtfldPassword.getRawValue().isEmpty()){  password = "";}else{password=txtfldPassword.getValue().toString();};
	    			if(smplcmbxTipoLavoratore.getRawValue().isEmpty()){  tipoL = "";}else{tipoL=smplcmbxTipoLavoratore.getRawValue().toString();};
	    			if(smplcmbxTipoOrario.getRawValue().isEmpty()){  tipoO = "";}else{tipoO=smplcmbxTipoOrario.getRawValue().toString();};
	    			if(smplcmbxRuolo.getRawValue().isEmpty()){  ruolo = "";}else{ruolo=smplcmbxRuolo.getRawValue().toString();};
	    			if(smplcmbxGruppolavoro.getRawValue().isEmpty()){  gruppoL = "";}else{gruppoL=smplcmbxGruppolavoro.getRawValue().toString();};
	    			if(txtfldCostoOrario.getRawValue().isEmpty()){  costoO = "";}else{ costoO=txtfldCostoOrario.getValue().toString();};
	    			if(txtfldCostoStruttura.getRawValue().isEmpty()){  costoS = "";}else{ costoS=txtfldCostoStruttura.getValue().toString();};
	    			if(smplcmbxSede.getRawValue().isEmpty()){ sede="";}else{sede=smplcmbxSede.getRawValue().toString();}
	    			if(smplcmbxSedeOperativa.getRawValue().isEmpty()){ sedeOperativa="";}else{sedeOperativa=smplcmbxSedeOperativa.getRawValue().toString();}
	    			if(txtfldOreDirette.getRawValue().isEmpty()){ oreDirette="";}else{oreDirette=txtfldOreDirette.getValue().toString();}
	    			if(txtfldOreIndirette.getRawValue().isEmpty()){ oreIndirette="";}else{oreIndirette=txtfldOreIndirette.getValue().toString();}
	    			if(txtfldPermessi.getRawValue().isEmpty()){ permessi="";}else{permessi=txtfldPermessi.getValue().toString();}
	    			if(txtfldFerie.getRawValue().isEmpty()){ ferie="";}else{ferie=txtfldFerie.getValue().toString();}
	    			if(txtfldExt.getRawValue().isEmpty()){ ext="";}else{ext=txtfldExt.getValue().toString();}
	    			if(txtfldOreRecupero.getRawValue().isEmpty()){ oreRecupero="";}else{oreRecupero=txtfldOreRecupero.getValue().toString();}
	    			 
	    			//Al momento dell'inserimento la password sarà uguale all'username
	    			AdministrationService.Util.getInstance().insertDataPersonale(nome, cognome, username, username, badge, ruolo, tipoO, tipoL, gruppoL, costoO,  costoS, 
	    						sede, sedeOperativa, oreDirette, oreIndirette, permessi, ferie, ext, oreRecupero, new AsyncCallback<Void>() {

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
	    			{Window.alert("Problemi InsertPersonale(); "+ e);}
	    	}else{Window.alert("Controllare i dati inseriti!");}
			  
			}			
		});   	

	    
//istruzioni per permettere l'edit dei dati di un dipendente    
	    btnEdit.addSelectionListener(new SelectionListener<ButtonEvent>() {
		
	    	@Override
	    	public void componentSelected(ButtonEvent ce){
	    	  if(frmpnlAnagrPersonale.isValid()){
	    		  
	    	  	String nome= new String();
	    		String cognome=new String();
	    		String badge=new String();
	    		String username=new String();
	    		//String password=new String();
	    		String tipoL=new String();
	    		String tipoO=new String();
	    		String ruolo=new String();
	    		String gruppoL= new String();
	    		String sede= new String();
	    		String sedeOperativa= new String();
	    		String oreDirette= new String();
	    		String oreIndirette= new String();
	    		String permessi= new String();
	    		String ferie= new String();
	    		String ex_fest= new String();
	    		String costoO;
	    		String costoS;
	    		String oreRecupero=new String();
	    	
	    		try{
	    			/*if(txtfldNome.getRawValue().isEmpty()){ nome="";}else{nome=txtfldNome.getValue().toString();}
	    			if(txtfldCognome.getRawValue().isEmpty()){ cognome="";}else{cognome=txtfldCognome.getValue().toString();};
	    			if(txtfldBadge.getRawValue().isEmpty()){  badge = "";}else{badge=txtfldBadge.getValue().toString();};
	    			if(txtfldUsername.getRawValue().isEmpty()){  username = "";}else{username=txtfldUsername.getValue().toString();};
	    			if(txtfldPassword.getRawValue().isEmpty()){  password = "";}else{password=txtfldPassword.getValue().toString();};
	    			if(smplcmbxTipoLavoratore.getRawValue().isEmpty()){  tipoL = "";}else{tipoL=smplcmbxTipoLavoratore.getRawValue().toString();};
	    			if(smplcmbxTipoOrario.getRawValue().isEmpty()){  tipoO = "";}else{tipoO=smplcmbxTipoOrario.getRawValue().toString();};
	    			if(smplcmbxRuolo.getRawValue().isEmpty()){  ruolo = "";}else{ruolo=smplcmbxRuolo.getRawValue().toString();};
	    			if(smplcmbxGruppolavoro.getRawValue().isEmpty()){  gruppoL = "";}else{gruppoL=smplcmbxGruppolavoro.getRawValue().toString();};
	    			if(txtfldCostoOrario.getRawValue().isEmpty()){  costoO = "";}else{ costoO=txtfldCostoOrario.getValue().toString();};
	    			if(txtfldCostoStruttura.getRawValue().isEmpty()){  costoS = "";}else{ costoS=txtfldCostoStruttura.getValue().toString();};
	    			if(smplcmbxSede.getRawValue().isEmpty()){ sede="";}else{sede=smplcmbxSede.getRawValue().toString();}
	    			if(txtfldOreDirette.getRawValue().isEmpty()){ oreDirette="";}else{oreDirette=txtfldOreDirette.getValue().toString();}
	    			if(txtfldOreIndirette.getRawValue().isEmpty()){ oreIndirette="";}else{oreIndirette=txtfldOreIndirette.getValue().toString();}
	    			if(txtfldPermessi.getRawValue().isEmpty()){ permessi="";}else{permessi=txtfldPermessi.getValue().toString();}
	    			if(txtfldFerie.getRawValue().isEmpty()){ ferie="";}else{ferie=txtfldFerie.getValue().toString();}
	    			if(txtfldExt.getRawValue().isEmpty()){ ex_fest="";}else{ex_fest=txtfldExt.getValue().toString();}
	    			if(txtfldOreRecupero.getRawValue().isEmpty()){ oreRecupero="";}else{oreRecupero=txtfldOreRecupero.getValue().toString();}*/
	    			
	    			//La Edit, lato server, impedirà la modifica della password, fattibile solo dall'Amministratore di sistema
	    			
	    			List<Record> listaMod=store.getModifiedRecords();
					store.commitChanges();
					for(Record r:listaMod){
						PersonaleModel p= (PersonaleModel) r.getModel();
						int id=p.get("idPersonale");
						nome=p.getNome();
						cognome= p.getCognome();
						username=p.getUsername();
						badge=p.get("nBadge");
						ruolo=p.get("ruolo");
						tipoO=p.get("tipologiaOrario");
						tipoL=p.get("tipologiaLavoratore");
						gruppoL=p.get("gruppoLavoro");
						costoO=p.get("costoOrario");
						costoS=p.get("costoStruttura");
						sede=p.get("sede");
						sedeOperativa=p.get("sedeOperativa");
						oreDirette=p.get("oreDirette");
						oreIndirette=p.get("oreIndirette");
						permessi=p.get("permessi");
						ferie=p.get("ferie");
						ex_fest="0.0";
						oreRecupero=p.get("oreRecupero");
						
						AdministrationService.Util.getInstance().editDataPersonale(id, nome, cognome, username, "", 
		    					badge, ruolo, tipoO, tipoL, gruppoL, costoO,  costoS, sede, sedeOperativa, oreDirette, oreIndirette, permessi,
		    					ferie, ex_fest, oreRecupero, new AsyncCallback<Void>() {

								@Override
								public void onFailure(
										Throwable caught) {
									Window.alert("Errore connessione on EditPersonale();");
									caught.printStackTrace();
									
								}

								@Override
								public void onSuccess(Void result) {
									//Window.alert("OK Modifica Effettuata.");
									caricaTabellaDati();
										
								}
							}); //edit data
					}
	    			
	    			
	    		}catch(NullPointerException e)
	    			{System.out.println("Problemi Edit "+ e);}
	    	
	    		frmpnlAnagrPersonale.clear();
	    		btnSend.setEnabled(true);
	    		btnEdit.setEnabled(false);
	    		btnDelete.setEnabled(false);
    	
	    	}else {Window.alert("Controllare i campi inseriti!");}
	      }
	    
	    });        
	    
//istruzioni per permettere l'eliminazione di un record	    
	    
	    btnDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {
	    	
	    	@Override
	    	public void componentSelected(ButtonEvent ce){
	    		
	    		try{
	    			AdministrationService.Util.getInstance().removeDataPersonale(nmbrfldId.getValue().intValue(), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore connessione on removePersonale();");
								caught.printStackTrace();					
							}

							@Override
							public void onSuccess(Void result) {
								Window.alert("OK Record Rimosso");
								caricaTabellaDati();
									
							}
						}); //edit data
	    			
	    		}catch(NullPointerException e)
	    			{System.out.println("Problemi Remove "+ e);}
	    	
    			frmpnlAnagrPersonale.clear();
    			btnSend.setEnabled(true);
    			btnEdit.setEnabled(false);
    	    	btnDelete.setEnabled(false);
	    	}
		});  
	    	
//Reset Form
		btnReset.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce){
				
				frmpnlAnagrPersonale.reset();
	    		btnSend.setEnabled(true);
	    		btnEdit.setEnabled(false);
    	    	btnDelete.setEnabled(false);
    	    	formBindings.unbind();    	    	
    	    	grid.getSelectionModel().deselectAll();
			}
		
		});
	   
	}
//


	private List<ColumnConfig> createColumns() {
		
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column = new ColumnConfig();  
		/*column.setId("idPersonale");  
	    column.setHeader("ID");  
	    column.setWidth(30);  
	    column.setRowHeader(true);  
	    configs.add(column); */ 
	    
	    column=new ColumnConfig();		
	    column.setId("nome");  
	    column.setHeader("Nome");  
	    column.setWidth(140);  
	    column.setRowHeader(true);  
	    configs.add(column);  
	  
	    column = new ColumnConfig();  
	    column.setId("cognome");  
	    column.setHeader("Cognome");  
	    column.setWidth(120);  
	    configs.add(column);  
	    
	    column = new ColumnConfig();  
	    column.setId("username");  
	    column.setHeader("Username");  
	    column.setWidth(160);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("nBadge");  
	    column.setHeader("Badge");  
	    column.setWidth(60);  
	    configs.add(column);   
	    
	    
	    column = new ColumnConfig();  
	    column.setId("gruppoLavoro");  
	    column.setHeader("Gruppo di Lavoro");  
	    column.setWidth(160);  
	    configs.add(column);  
	
	    return configs;
	}


	private FormPanel createForm() {
		
	   	frmpnlAnagrPersonale = new FormPanel();
			
		frmpnlAnagrPersonale.setLabelWidth(100);
	    frmpnlAnagrPersonale.setFrame(false);
	    frmpnlAnagrPersonale.setButtonAlign(HorizontalAlignment.CENTER);
	    //frmpnlAnagrPersonale.setHeading("Dettaglio Dipendente");
	    frmpnlAnagrPersonale.setHeaderVisible(false);
	    frmpnlAnagrPersonale.setWidth("305px");
	    frmpnlAnagrPersonale.setBorders(false);
	    frmpnlAnagrPersonale.setBodyBorder(false);
	    
	    nmbrfldId = new NumberField();
	    frmpnlAnagrPersonale.add(nmbrfldId, new FormData("50%"));
	    nmbrfldId.setFieldLabel("ID");
	    nmbrfldId.setName("idPersonale");
	    nmbrfldId.setEnabled(false);
	    nmbrfldId.setVisible(false);
	    
	    txtfldNome = new TextField<String>();
	    txtfldNome.setAllowBlank(false);
	    txtfldNome.setMaxLength(45);
	    txtfldNome.setName("nome");
	    txtfldNome.setSelectOnFocus(true);
	    frmpnlAnagrPersonale.add(txtfldNome, new FormData("80%"));
	    txtfldNome.setFieldLabel("Nome");
	    
	    txtfldCognome = new TextField<String>();
	    txtfldCognome.setAllowBlank(false);
	    txtfldCognome.setMaxLength(45);
	    txtfldCognome.setName("cognome");
	    frmpnlAnagrPersonale.add(txtfldCognome, new FormData("80%"));
	    txtfldCognome.setFieldLabel("Cognome");
	    
	    //deve essere presente solo 1 carattere "." nell'username
	    txtfldUsername = new TextField<String>();
	    txtfldUsername.setMaxLength(45);
	    txtfldUsername.setName("username");
	    txtfldUsername.setRegex("[a-zA-Z]+[.]{1}[a-zA-Z]+");
	    txtfldUsername.getMessages().setRegexText("Deve essere formato da caratteri!");
	    frmpnlAnagrPersonale.add(txtfldUsername, new FormData("80%"));
	    txtfldUsername.setFieldLabel("Username");
	    txtfldUsername.setAllowBlank(false);
	    
	    txtfldPassword = new TextField<String>();
	    txtfldPassword.setName("password");
	    //txtfldPassword.setMaxLength(8);
	   // frmpnlAnagrPersonale.add(txtfldPassword, new FormData("80%"));
	    txtfldPassword.setFieldLabel("Password");
	    
	    txtfldBadge = new TextField<String>();
	    //txtfldBadge.setMaxLength(6);
	    txtfldBadge.setName("nBadge");
	    frmpnlAnagrPersonale.add(txtfldBadge, new FormData("80%"));
	    txtfldBadge.setFieldLabel("Nr. Badge");
	    txtfldBadge.setRegex("[0-9]*");
	    txtfldBadge.getMessages().setRegexText("Deve essere formato da cifre 0-9");
	  
	    smplcmbxRuolo = new SimpleComboBox<String>();
	    smplcmbxRuolo.setFieldLabel("Ruolo");
	    smplcmbxRuolo.setName("ruolo");
	    smplcmbxRuolo.setToolTip("AMM: Amministratore DIR: Direzione PM: PersonaleManager UG: Ufficio Gestione Personale UA: UfficioAmministrazione DIP: Dipendente");
	    for(String l : DatiComboBox.getRuoli()){
	    	smplcmbxRuolo.add(l);}
	    smplcmbxRuolo.setTriggerAction(TriggerAction.ALL);
	    frmpnlAnagrPersonale.add(smplcmbxRuolo, new FormData("65%"));
    
	    smplcmbxTipoOrario = new SimpleComboBox <String> ();
	    smplcmbxTipoOrario.setFieldLabel("Tipo. Orario");
	    smplcmbxTipoOrario.setName("tipologiaOrario");
	    smplcmbxTipoOrario.setToolTip("8: FullTime 7: PartTime(7 ore) 6: PartTime(6 ore) 4: PartTime(4 ore) A: Altro Contratto");
	    for(String l : DatiComboBox.getTipoOrario()){
	    	smplcmbxTipoOrario.add(l);}
	    smplcmbxTipoOrario.setTriggerAction(TriggerAction.ALL);
	    frmpnlAnagrPersonale.add(smplcmbxTipoOrario, new FormData("65%"));
	    
	    smplcmbxTipoLavoratore = new SimpleComboBox <String>();
	    smplcmbxTipoLavoratore.setFieldLabel("Tipo. Lavoratore");
	    smplcmbxTipoLavoratore.setName("tipologiaLavoratore");
	    smplcmbxTipoLavoratore.setToolTip("D: Dipendente C: Collaboratore S: Stage I: P.IVA");
	    for(String l : DatiComboBox.getTipoLavoratore()){
	    	smplcmbxTipoLavoratore.add(l);}
	    smplcmbxTipoLavoratore.setTriggerAction(TriggerAction.ALL);
	    frmpnlAnagrPersonale.add(smplcmbxTipoLavoratore, new FormData("65%"));
	    
	    smplcmbxSede = new SimpleComboBox<String>();
	    smplcmbxSede.setFieldLabel("Sede");
	    smplcmbxSede.setName("sede");
	    smplcmbxSede.setToolTip("S: Sede F: Fuori Sede");
	    for(String l : DatiComboBox.getSede()){
	    	smplcmbxSede.add(l);}
	    smplcmbxSede.setTriggerAction(TriggerAction.ALL);
	    frmpnlAnagrPersonale.add(smplcmbxSede, new FormData("65%"));
	    
	    smplcmbxSedeOperativa = new SimpleComboBox<String>();
	    smplcmbxSedeOperativa.setAllowBlank(false);
	    smplcmbxSedeOperativa.setFieldLabel("Sede Oper.");
	    smplcmbxSedeOperativa.setName("sedeOperativa");
	    smplcmbxSedeOperativa.setToolTip("T: Torino B: Brescia");
	    smplcmbxSedeOperativa.add("T");
	    smplcmbxSedeOperativa.add("B");
	    smplcmbxSede.setTriggerAction(TriggerAction.ALL);
	    frmpnlAnagrPersonale.add(smplcmbxSedeOperativa, new FormData("65%")); 
	    
	    smplcmbxGruppolavoro = new SimpleComboBox<String>();
	    smplcmbxGruppolavoro.setName("gruppoLavoro");
	    for(String l : DatiComboBox.getGruppoLavoro()){
	    	smplcmbxGruppolavoro.add(l);}
	    smplcmbxGruppolavoro.setTriggerAction(TriggerAction.ALL);	    
	    frmpnlAnagrPersonale.add(smplcmbxGruppolavoro, new FormData("75%"));
	    smplcmbxGruppolavoro.setFieldLabel("Gruppo di Lavoro");
	    
	    txtfldCostoOrario = new TextField<String>();
	    txtfldCostoOrario.setMaxLength(7);
	    //txtfldCostoOrario.addKeyListener(keyListener);
	    txtfldCostoOrario.setName("costoOrario");
	    frmpnlAnagrPersonale.add(txtfldCostoOrario, new FormData("55%"));
	    txtfldCostoOrario.setFieldLabel("Costo Orario");
	    txtfldCostoOrario.setValue("0.0");
	    txtfldCostoOrario.setRegex("[0-9]+[.]{1}[0-9]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
	    txtfldCostoOrario.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
	    txtfldCostoOrario.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldCostoOrario.getValue()==null)
							txtfldCostoOrario.setValue("0.00");
						else{
							String valore= txtfldCostoOrario.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldCostoOrario.setValue(valore);
						}						
					}
			 }
		});
	    
	    txtfldCostoStruttura = new TextField<String>();
	    txtfldCostoStruttura.setMaxLength(7);
	    txtfldCostoStruttura.setName("costoStruttura");
	    txtfldCostoStruttura.setValue("0.0");
	    frmpnlAnagrPersonale.add(txtfldCostoStruttura, new FormData("55%"));
	    txtfldCostoStruttura.setFieldLabel("Costo Struttura");
	    txtfldCostoStruttura.setRegex("[0-9]+[.]{1}[0-9]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
	    txtfldCostoStruttura.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
	    txtfldCostoStruttura.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldCostoStruttura.getValue()==null)
							txtfldCostoStruttura.setValue("0.00");
						else{
							String valore= txtfldCostoStruttura.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldCostoStruttura.setValue(valore);
						}						
					}
			 }
		});
	    txtfldCostoStruttura.setStyleAttribute("padding-bottom", "10px");	    
		
	    FieldSet fldstNewFieldset = new FieldSet();
	    frmpnlAnagrPersonale.add(fldstNewFieldset, new FormData("95%"));
	    fldstNewFieldset.setHeading("Dettaglio Ore");
	    fldstNewFieldset.setCollapsible(true);
	    fldstNewFieldset.setExpanded(true);
	   
	    LayoutContainer main = new LayoutContainer();
	    main.setLayout(new ColumnLayout());  
	    main.setBorders(false);
	    
	    LayoutContainer left=new LayoutContainer();
	    FormLayout layout= new FormLayout();
	    layout.setLabelAlign(LabelAlign.TOP);
	    left.setLayout(layout);
	    
	    txtfldOreDirette = new TextField<String>();
	    txtfldOreDirette.setMaxLength(10);
	    txtfldOreDirette.setName("oreDirette");
	    txtfldOreDirette.setValue("0");
	    txtfldOreDirette.setFieldLabel("Ore Dirette");
	    txtfldOreDirette.setRegex("[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0|0");
	    txtfldOreDirette.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
	    txtfldOreDirette.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldOreDirette.getValue()==null)
							txtfldOreDirette.setValue("0.00");
						else{
							String valore= txtfldOreDirette.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldOreDirette.setValue(valore);
						}						
					}
			 }
		});
	    left.add(txtfldOreDirette, new FormData("50%"));
	 	    
	    txtfldOreIndirette = new TextField<String>();
	    txtfldOreIndirette.setMaxLength(10);
	    txtfldOreIndirette.setName("oreIndirette");
	    txtfldOreIndirette.setValue("0");
	    left.add(txtfldOreIndirette, new FormData("50%"));
	    txtfldOreIndirette.setRegex("[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0|0");
	    txtfldOreIndirette.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
	    txtfldOreIndirette.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldOreIndirette.getValue()==null)
							txtfldOreIndirette.setValue("0.00");
						else{
							String valore= txtfldOreIndirette.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldOreIndirette.setValue(valore);
						}						
					}
			 }
		});
	    txtfldOreIndirette.setFieldLabel("Ore Indirette");
	    
	    txtfldExt = new TextField<String>();
	    txtfldExt.setMaxLength(10);
	    txtfldExt.setName("ext");
	    txtfldExt.setValue("0");
	    left.add(txtfldExt, new FormData("50%"));
	    txtfldExt.setFieldLabel("Ex/Festivi");
	    txtfldExt.setRegex("[-]?[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0|0");
	    txtfldExt.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
	    txtfldExt.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldExt.getValue()==null)
							txtfldExt.setValue("0.00");
						else{
							String valore= txtfldExt.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldExt.setValue(valore);
						}						
					}
			 }
		});
	    
	    LayoutContainer right = new LayoutContainer();  
	    right.setStyleAttribute("padding-left", "10px");  
	    layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.TOP);  
	    right.setLayout(layout); 
	    
	    txtfldPermessi = new TextField<String>();
	    txtfldPermessi.setName("permessi");
	    txtfldPermessi.setValue("0.0");
	    right.add(txtfldPermessi, new FormData("50%"));
	    txtfldPermessi.setFieldLabel("Permessi");
	    txtfldPermessi.setRegex("[-]?[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0|0");
	    txtfldPermessi.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
	    txtfldPermessi.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldPermessi.getValue()==null)
							txtfldPermessi.setValue("0.00");
						else{
							String valore= txtfldPermessi.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldPermessi.setValue(valore);
						}						
					}
			 }
		});
	    
	    txtfldFerie = new TextField<String>();
	    txtfldFerie.setName("ferie");
	    txtfldFerie.setValue("0.0");
	    right.add(txtfldFerie, new FormData("50%"));
	    txtfldFerie.setFieldLabel("Ferie");
	    txtfldFerie.setRegex("[-]?[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0|0");
	    txtfldFerie.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
	    txtfldFerie.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldFerie.getValue()==null)
							txtfldFerie.setValue("0.00");
						else{
							String valore= txtfldFerie.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldFerie.setValue(valore);
						}						
					}
			 }
		});
		
		txtfldOreRecupero=new TextField<String>();
		txtfldOreRecupero.setName("oreRecupero");
		txtfldOreRecupero.setValue("0.0");
		right.add(txtfldOreRecupero, new FormData("50%"));
	    txtfldOreRecupero.setFieldLabel("Ore Recupero");
	    txtfldOreRecupero.setRegex("[-]?[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0|0");
	    txtfldOreRecupero.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
	    txtfldOreRecupero.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldOreRecupero.getValue()==null)
							txtfldOreRecupero.setValue("0.00");
						else{
							String valore= txtfldOreRecupero.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldOreRecupero.setValue(valore);
						}						
					}
			 }
		});
	    
	    main.add(left, new ColumnData(100));
	    left.setWidth("50%");
	    main.add(right, new ColumnData(100));
	    right.setWidth("50%");
	    
	    fldstNewFieldset.add(main);    
	    return frmpnlAnagrPersonale;
	}
	

	private void caricaTabellaDati() {
		try {

			AdministrationService.Util.getInstance().getAllPersonaleModel(
					new AsyncCallback<List<PersonaleModel>>() {

						@Override
						public void onSuccess(List<PersonaleModel> result) {
							loadTable(result);
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore connessione;");
							caught.printStackTrace();
						}
					}); // AsyncCallback
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("Problemi Caricamento Dati Personale.");
		}
	}
	
	
	private void loadTable(List<PersonaleModel> lista) {
	
	    // add paging support for a local collection of models  
	    PagingModelMemoryProxy proxy = new PagingModelMemoryProxy(lista); 
	    
	    // loader  
	    PagingLoader<PagingLoadResult<PersonaleModel>> loader = new BasePagingLoader<PagingLoadResult<PersonaleModel>>(proxy);  
	    loader.setRemoteSort(true);
	    
	    ListStore<PersonaleModel> store1= new ListStore<PersonaleModel>(loader);
	    store1.setDefaultSort("cognome", SortDir.ASC);
	    loader.load(0, 30); 
	    loader.setSortDir(SortDir.ASC);
	    loader.setSortField("cognome");
	    toolBar.bind(loader); 
	    toolBar.setActivePage(1);
		grid.reconfigure(store1, cm);
		
		grid.getAriaSupport().setDescribedBy(toolBar.getId() + "-display");
		btnSend.setEnabled(true);
		
	}
		      
}