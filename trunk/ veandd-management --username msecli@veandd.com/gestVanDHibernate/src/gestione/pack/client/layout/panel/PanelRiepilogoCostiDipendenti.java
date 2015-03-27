package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.CostingRisorsaModel;
import gestione.pack.client.model.OffertaModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.model.RiepilogoCostiDipendentiModel;
import gestione.pack.client.utility.MyImages;
import gestione.pack.shared.Personale;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/*AL MOMENTO I COSTI VENGONO CARICATI STATICAMENTE DA .CSV*/


public class PanelRiepilogoCostiDipendenti extends LayoutContainer{
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private ListStore<RiepilogoCostiDipendentiModel> store=new ListStore<RiepilogoCostiDipendentiModel>();
	private ColumnModel cm;
	
	private EditorGrid<RiepilogoCostiDipendentiModel> gridRiepilogo;
	
	private SimpleComboBox<String> smplcmbxSede;
	protected ComboBox<PersonaleModel> cmbxPersonale;
	private TextField<String> txtfldcostoAnno;
	private TextField<String> txtfldcostoOrario;
	private TextField<String> txtfldcostoTrasferta;
	private TextField<String> txtfldoreAnno;
	private TextField<String> txtfldCostoStruttura;
	private TextField<String> txtfldCostoOneri;
	private TextField<String> txtfldCostoHardware;
	private TextField<String> txtfldCostoSoftware;
	
	private CellSelectionModel<RiepilogoCostiDipendentiModel> csm=new CellSelectionModel<RiepilogoCostiDipendentiModel>();
	
	private Button btnPrint;
	private Button btnConfirm;
	private Button btnAdd;
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	public PanelRiepilogoCostiDipendenti(){
		
	}
	
	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		VerticalPanel vp= new VerticalPanel();
		vp.setSpacing(3);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(true);
		cpGrid.setHeading("Lista Dipendenti.");
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setHeight((h-65));
		cpGrid.setWidth(w-250);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setButtonAlign(HorizontalAlignment.CENTER);  
		Resizable r=new Resizable(cpGrid);
		
		btnPrint= new Button();
		btnPrint.setEnabled(true);
		btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
		btnPrint.setIconAlign(IconAlign.TOP);
		btnPrint.setToolTip("Stampa");
		btnPrint.setSize(26, 26);
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				SessionManagementService.Util.getInstance().setDatiReportCostiDip("RIEP.COSTI", store.getModels(),
						new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error on setNomeReport()");					
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result)
							fp.submit();
						else
							Window.alert("Problemi durante il settaggio dei parametri in Sessione (http)");
					}
				});
			}
		});
		
		btnAdd= new Button();
	    btnAdd.setStyleAttribute("padding-left", "2px");
	    btnAdd.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.add()));
	  	btnAdd.setIconAlign(IconAlign.TOP);
	  	btnAdd.setSize(26, 26);
	  	btnAdd.setToolTip("Nuova Offerta");
	  	btnAdd.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				RiepilogoCostiDipendentiModel am= new RiepilogoCostiDipendentiModel(0, 0, "", "0.00", "", "0", "0.00", "0.00", "0.00", "0.00", "0.00", 
						"0.00", "", "", "");
					
		        gridRiepilogo.stopEditing();  
		        store.insert(am, 0);  
		        gridRiepilogo.startEditing(store.indexOf(am), 0);
		        
			}
		});
	  	
		btnConfirm=new Button();
		btnConfirm.setEnabled(true);
		btnConfirm.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
		btnConfirm.setIconAlign(IconAlign.TOP);
		btnConfirm.setToolTip("Conferma modifiche");
		btnConfirm.setSize(26, 26);
		btnConfirm.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				
				RiepilogoCostiDipendentiModel c=new RiepilogoCostiDipendentiModel();
								
				for(Record record: store.getModifiedRecords()){
									
					c=(RiepilogoCostiDipendentiModel) record.getModel();
					
					AdministrationService.Util.getInstance().editDatiCostiAzienda(c, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore di connessione on insertNewOffertaWithRda();");
						}

						@Override
						public void onSuccess(Boolean result) {
							if(result){
								caricaTabellaDati();			
							}else{
								Window.alert("error: Impossibile effettuare l'inserimento/modifica dei dati!");
							}
						}
					});
				}
			}
		});
		
		smplcmbxSede= new SimpleComboBox<String>();
		smplcmbxSede.setFieldLabel("Sede");
		smplcmbxSede.setWidth(65);
		smplcmbxSede.setEmptyText("Sede..");
		smplcmbxSede.setAllowBlank(false);
		smplcmbxSede.add("T");
		smplcmbxSede.add("B");
		smplcmbxSede.setStyleAttribute("padding-left", "2px");
		smplcmbxSede.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				caricaTabellaDati();
			}
		});
		
		//caricaTabellaDati();
		
		cm = new ColumnModel(createColumns());		
		gridRiepilogo= new EditorGrid<RiepilogoCostiDipendentiModel>(store, cm);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setColumnLines(true);
		gridRiepilogo.setStripeRows(true);
		gridRiepilogo.setItemId("grid");
		gridRiepilogo.setSelectionModel(csm);
		
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setAction(url);
		fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
		fp.add(btnPrint);
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(fp);
		
		ToolBar tlBar= new ToolBar();
		tlBar.add(smplcmbxSede);
		tlBar.add(btnAdd);
		tlBar.add(btnConfirm);
		tlBar.add(new SeparatorToolItem());
		tlBar.add(cp);
		
		cpGrid.setTopComponent(tlBar);
		cpGrid.add(gridRiepilogo); 
	    
	    layoutContainer.add(cpGrid, new FitData(3, 3, 3, 3));
		
		add(layoutContainer);
	}
	

	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {
		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			
			//Window.open("/FileStorage/RiepilogoAnnuale.pdf", "_blank", "1");
			
		}
	}
	
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
				
		ColumnConfig column = new ColumnConfig();
		   
	    column = new ColumnConfig();
	    column.setId("nome");
	    column.setHeader("Nominativo");
	    column.setWidth(140);
	    ListStore<PersonaleModel> store= new ListStore<PersonaleModel>();
	    cmbxPersonale=new ComboBox<PersonaleModel>();
	    cmbxPersonale.setStore(store);
	    cmbxPersonale.setDisplayField("nomeCompleto");
	    cmbxPersonale.setEmptyText("Selezionare..");
	    cmbxPersonale.setEditable(true);
	    cmbxPersonale.setVisible(true);
	    cmbxPersonale.setTriggerAction(TriggerAction.ALL);
	    cmbxPersonale.setForceSelection(true);
	    cmbxPersonale.setAllowBlank(false);
	    cmbxPersonale.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
					getAllDipendenti();
			}

			private void getAllDipendenti() {
				AdministrationService.Util.getInstance().getListaDipendentiModel("", new AsyncCallback<List<PersonaleModel>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on getListaDipendentiModel();");
						caught.printStackTrace();		
					}

					@Override
					public void onSuccess(List<PersonaleModel> result) {
						if(result!=null){
							ListStore<PersonaleModel> lista= new ListStore<PersonaleModel>();
							lista.setStoreSorter(new StoreSorter<PersonaleModel>());  
							lista.setDefaultSort("nomeCompleto", SortDir.ASC);
							
							lista.add(result);
							cmbxPersonale.clear();
							cmbxPersonale.setStore(lista);

						}else 
							Window.alert("error: Errore durante l'accesso ai dati Personale.");				
					}
				});
			}
		});
	    cmbxPersonale.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {	
				int idPersonale=cmbxPersonale.getValue().get("idPersonale");
				
				csm.getSelectedItem().set("idPersonale", idPersonale);
			}
		});
	    CellEditor editor = new CellEditor(cmbxPersonale) {
	    	@Override  
	        public Object preProcessValue(Object value) {
	          if (value == null) {
	            return value;  
	          }  
	          return cmbxPersonale.getValue();  
	        } 
	        @Override  
	        public Object postProcessValue(Object value) {
	          if (value == null) {
	            return value;
	          }  
	          return ((ModelData) value).get("nomeCompleto");  
	        }  
	    };
	    column.setEditor(editor);
	    configs.add(column);
	    
	    column = new ColumnConfig();
	    column.setId("tipoOrario");
	    column.setHeader("Orario Giornaliero");
	    column.setWidth(50); 
	    configs.add(column);
	    
	    column = new ColumnConfig();
	    column.setId("costoAnnuo");
	    column.setHeader("Costo Annuo");
	    column.setWidth(100);
	    txtfldcostoAnno= new TextField<String>();
	    txtfldcostoAnno.setValue("0.00");
	    txtfldcostoAnno.setRegex("^([0-9]+).([0-9]{1,2})$");
	    txtfldcostoAnno.addKeyListener(new KeyListener(){
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldcostoAnno.getValue()==null)
								txtfldcostoAnno.setValue("0.00");
							else{
								String valore= txtfldcostoAnno.getValue().toString();
														
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
								txtfldcostoAnno.setValue(valore);
							}						
						}
				 }
			});
	    column.setEditor(new CellEditor(txtfldcostoAnno));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("orePreviste");  
	    column.setHeader("Ore Previste");
	    column.setWidth(100);  
	    txtfldoreAnno= new TextField<String>();
	    txtfldoreAnno.setValue("0");
	    txtfldoreAnno.setRegex("^([0-9]+)");
	    txtfldoreAnno.addKeyListener(new KeyListener(){
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldoreAnno.getValue()==null)
								txtfldoreAnno.setValue("0");
							else{
								String valore= txtfldoreAnno.getValue().toString();
														
								if(valore.compareTo("")==0)
									valore ="0";
								
								txtfldoreAnno.setValue(valore);						
							}						
						}
				 }
			});
	    column.setEditor(new CellEditor(txtfldoreAnno));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoOrario");  
	    column.setHeader("Costo Orario");
	    column.setWidth(100);  
	    txtfldcostoOrario= new TextField<String>();
	    txtfldcostoOrario.setValue("0.00");
	    txtfldcostoOrario.setRegex("^([0-9]+).([0-9]{1,2})$");
	    txtfldcostoOrario.addKeyListener(new KeyListener(){
			
			/*	public void componentKeyUp(ComponentEvent event) {				
					txtfldNumeroOreResidue.setValue(txtfldNumeroOre.getValue());		
				}
				*/
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldcostoOrario.getValue()==null)
								txtfldcostoOrario.setValue("0.00");
							else{
								String valore= txtfldcostoOrario.getValue().toString();
														
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
								txtfldcostoOrario.setValue(valore);
							}						
						}
				 }
			});
	    column.setEditor(new CellEditor(txtfldcostoOrario));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoTrasferta");  
	    column.setHeader("Costo Trasferta");  
	    column.setWidth(100); 
	    txtfldcostoTrasferta= new TextField<String>();
	    txtfldcostoTrasferta.setValue("0.00");
	    txtfldcostoTrasferta.setRegex("^([0-9]+).([0-9]{1,2})$");
	    txtfldcostoTrasferta.addKeyListener(new KeyListener(){
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldcostoTrasferta.getValue()==null)
								txtfldcostoTrasferta.setValue("0.00");
							else{
								String valore= txtfldcostoTrasferta.getValue().toString();
														
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
								txtfldcostoTrasferta.setValue(valore);
							}						
						}
				 }
			});
	    column.setEditor(new CellEditor(txtfldcostoTrasferta));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoStruttura");  
	    column.setHeader("Costo Struttura");  
	    column.setWidth(100);
	    txtfldCostoStruttura= new TextField<String>();
	    txtfldCostoStruttura.setValue("0.00");
	    txtfldCostoStruttura.setRegex("^([0-9]+).([0-9]{1,2})$");
	    txtfldCostoStruttura.addKeyListener(new KeyListener(){
			
			/*	public void componentKeyUp(ComponentEvent event) {				
					txtfldNumeroOreResidue.setValue(txtfldNumeroOre.getValue());		
				}
				*/
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
	    column.setEditor(new CellEditor(txtfldCostoStruttura));
	    configs.add(column);
	    
	    column = new ColumnConfig();
	    column.setId("costoOneri");
	    column.setHeader("Costo Oneri");
	    column.setWidth(100);
	    txtfldCostoOneri= new TextField<String>();
	    txtfldCostoOneri.setValue("0.00");
	    txtfldCostoOneri.setRegex("^([0-9]+).(00|15|30|45)$");
	    txtfldCostoOneri.addKeyListener(new KeyListener(){
			
			/*	public void componentKeyUp(ComponentEvent event) {				
					txtfldNumeroOreResidue.setValue(txtfldNumeroOre.getValue());		
				}
				*/
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldCostoOneri.getValue()==null)
								txtfldCostoOneri.setValue("0.00");
							else{
								String valore= txtfldCostoOneri.getValue().toString();
														
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
								txtfldCostoOneri.setValue(valore);
							}						
						}
				 }
			});
	    column.setEditor(new CellEditor(txtfldCostoOneri));
	    configs.add(column);
	    
	    column = new ColumnConfig();
	    column.setId("costoSw");
	    column.setHeader("Costo SW");
	    column.setWidth(100);
	    txtfldCostoSoftware= new TextField<String>();
	    txtfldCostoSoftware.setValue("0.00");
	    txtfldCostoSoftware.setRegex("^([0-9]+).([0-9]{1,2})$");
	    txtfldCostoSoftware.addKeyListener(new KeyListener(){
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldCostoSoftware.getValue()==null)
								txtfldCostoSoftware.setValue("0.00");
							else{
								String valore= txtfldCostoSoftware.getValue().toString();
														
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
								txtfldCostoSoftware.setValue(valore);
								
							}						
						}
				 }
			});
	    column.setEditor(new CellEditor(txtfldCostoSoftware));
	    configs.add(column);
	    
	    column = new ColumnConfig();
	    column.setId("costoHw");
	    column.setHeader("Costo HW");
	    column.setWidth(100);
	    txtfldCostoHardware= new TextField<String>();
	    txtfldCostoHardware.setValue("0.00");
	    txtfldCostoHardware.setRegex("^([0-9]+).([0-9]{1,2})$");
	    txtfldCostoHardware.addKeyListener(new KeyListener(){
			
			/*	public void componentKeyUp(ComponentEvent event) {				
					txtfldNumeroOreResidue.setValue(txtfldNumeroOre.getValue());		
				}
				*/
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldCostoHardware.getValue()==null)
								txtfldCostoHardware.setValue("0.00");
							else{
								String valore= txtfldCostoHardware.getValue().toString();
														
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
								txtfldCostoHardware.setValue(valore);
							}						
						}
				 }
			});
	    column.setEditor(new CellEditor(txtfldCostoHardware));
	    configs.add(column);
	    
	    column = new ColumnConfig();
	    column.setId("costoHwSw");
	    column.setHeader("Costo HwSw");
	    column.setWidth(100);
	    configs.add(column);
	    
	    column = new ColumnConfig();
	    column.setId("costoOrarioTotale");
	    column.setHeader("CostoTot/h");
	    column.setWidth(100);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("gruppoLavoro");  
	    column.setHeader("Area");  
	    column.setWidth(160);  
	    configs.add(column);
	    
	    return configs;
	}

	
	private void caricaTabellaDati() {
			
		try {
			if(smplcmbxSede.isValid()){
				String sede=smplcmbxSede.getRawValue().toString();
				
				AdministrationService.Util.getInstance().getRiepilogoDatiCostiPersonale(sede, new AsyncCallback<List<RiepilogoCostiDipendentiModel>>() {
					@Override
					public void onSuccess(List<RiepilogoCostiDipendentiModel> result) {
						if(result==null)
							Window.alert("Impossibile accedere ai dati sui costi dipendenti!");
						else
							loadTable(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on getRiepilogoDatiCostiPersonale();");
						caught.printStackTrace();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("Problemi durante il caricamento dei dati sui costi personale.");
		}
	}
	
	
	private void loadTable(List<RiepilogoCostiDipendentiModel> result) {
				
		store.removeAll();
		store.setStoreSorter(new StoreSorter<RiepilogoCostiDipendentiModel>());  
	    store.setDefaultSort("nome", SortDir.ASC);
		store.add(result);
	}	
	
}
