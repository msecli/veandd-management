package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.ClienteModel;
import gestione.pack.client.model.OffertaModel;
import gestione.pack.client.model.RiepilogoMeseGiornalieroModel;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

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
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelGestioneOfferte extends LayoutContainer{

	private int h=Window.getClientHeight();
	
	private ListStore<OffertaModel> store=new ListStore<OffertaModel>();
	private ColumnModel cm;
	private EditorGrid<OffertaModel> gridRiepilogo;
	private CellSelectionModel<OffertaModel> csm=new CellSelectionModel<OffertaModel>();
	
	protected Button btnAdd;
	protected Button btnRemove;
	protected Button btnConfirm;
	
	protected ComboBox<ClienteModel> cmbxCliente;
	protected TextField<String> txtfldNumeroOfferta;
	protected TextField<String> txtfldDescrizione;
	protected DateField dtfldScadenzaControllo;
	protected TextField<String> txtfldImporto;
	protected SimpleComboBox<String> smplcmbxStatoOfferta;
	
	public PanelGestioneOfferte(){
		
	}
	
	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		layoutContainer.setScrollMode(Scroll.NONE);
	
	 	ToolBar toolBar= new ToolBar();
	  	
	  	ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setBodyBorder(false);         
		cntpnlGrid.setLayout(new FitLayout());  
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setHeight(h-55);
		cntpnlGrid.setBorders(true);
		cntpnlGrid.setScrollMode(Scroll.AUTO);
						
		cm = new ColumnModel(createColumns());	
		store.setDefaultSort("cognome", SortDir.ASC);
		gridRiepilogo= new EditorGrid<OffertaModel>(store, cm);  
		gridRiepilogo.setBorders(false);
		gridRiepilogo.setSelectionModel(csm);
	    cntpnlGrid.add(gridRiepilogo);
		
	    smplcmbxStatoOfferta=new SimpleComboBox<String>();
	    smplcmbxStatoOfferta.setFieldLabel("Stato Offerta..");
		smplcmbxStatoOfferta.add("Pending");
		smplcmbxStatoOfferta.add("Accettata");
		smplcmbxStatoOfferta.add("Chiusa");
		smplcmbxStatoOfferta.add("Tutte");
		smplcmbxStatoOfferta.setTriggerAction(TriggerAction.ALL);
		smplcmbxStatoOfferta.setSimpleValue("Pending");
		smplcmbxStatoOfferta.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {
			
			@Override
			public void selectionChanged(
					SelectionChangedEvent<SimpleComboValue<String>> se) {
				
				String stato=smplcmbxStatoOfferta.getRawValue().toString();
				
				caricaTabellaDati(stato);
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
				OffertaModel am= new OffertaModel();  	        
		        gridRiepilogo.stopEditing();  
		        store.insert(am, 0);  
		        gridRiepilogo.startEditing(store.indexOf(am), 0);  
			}
		});
	    
	    btnRemove=new Button();
	    btnRemove.setStyleAttribute("padding-left", "2px");
	    btnRemove.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.elimina()));
	    btnRemove.setIconAlign(IconAlign.TOP);
	    btnRemove.setSize(26, 26);
	    btnRemove.setToolTip("Elimina Offerta");
	    btnRemove.addSelectionListener(new SelectionListener<ButtonEvent>() {
	    	
			@Override
			public void componentSelected(ButtonEvent ce){
				
				/* l'eliminazione deve essere effettuata se non c'è un ordine agganciato, altrimenti errore
				 * Se provo ad eliminare un'offerta e c'è l'ordine allora non lo posso fare
				 */
				Integer id=csm.getSelectedItem().get("idOfferta");
				
				AdministrationService.Util.getInstance().deleteOffertaById(id, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore di connessione on insertNewOffertaWithRda();");
							}

							@Override
							public void onSuccess(Boolean result) {
								if(result){
									caricaTabellaDati("Pending");			
								}else{
									Window.alert("error: Impossibile effettuare l'eliminazione!");
									//
								}
							}
					});
				
				
			}
		});
	    
	    btnConfirm=new Button();
	    btnConfirm.setStyleAttribute("padding-left", "2px");
	    btnConfirm.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	    btnConfirm.setIconAlign(IconAlign.TOP);
	    btnConfirm.setSize(26, 26);
	    btnConfirm.setToolTip("Conferma Offerta");
	    btnConfirm.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				
				String numeroOfferta=new String();
				String descrizione=new String();
				String importo=new String();
				Integer idCliente = 0;
				Date dataOfferta= new Date(); 
				String ragioneSociale= new String();
				
				for(Record record: store.getModifiedRecords()){
				
					numeroOfferta=(String)record.get("numeroOfferta");
					descrizione=(String)record.get("descrizione");
					importo=(String)record.get("importo");
					ragioneSociale=(String)record.get("ragioneSociale");
					dataOfferta= (Date)record.get("dataOfferta");
					
					AdministrationService.Util.getInstance().insertNewOffertaWithRda(0, idCliente, numeroOfferta, ragioneSociale, dataOfferta, descrizione, 
						importo, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore di connessione on insertNewOffertaWithRda();");
							}

							@Override
							public void onSuccess(Boolean result) {
								if(result){
									caricaTabellaDati("Pending");			
								}else{
									Window.alert("error: Impossibile effettuare l'inserimento dei dati!");
								}
							}
					});
				}
			}
		});
	    
	    toolBar.add(smplcmbxStatoOfferta);
	    toolBar.add(new SeparatorToolItem());
		toolBar.add(btnAdd);
	    toolBar.add(new SeparatorToolItem());
	    toolBar.add(btnRemove);
	    toolBar.add(new SeparatorToolItem());
	    toolBar.add(btnConfirm);
		
	    caricaTabellaDati("Pending");
		cntpnlGrid.setTopComponent(toolBar);		
		
		layoutContainer.add(cntpnlGrid, new FitData(3, 3, 3, 3));
		
		add(layoutContainer);
	}	
	
	
	private void caricaTabellaDati(String stato) {
		
		AdministrationService.Util.getInstance().getAllOfferteModel(stato,new AsyncCallback<List<OffertaModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getAllOfferteModel();");
			}

			@Override
			public void onSuccess(List<OffertaModel> result) {
				if(result!=null)
				if(result.size()>=0){
					loadData(result);								
				}else{
					Window.alert("error: Impossibile effettuare il caricamento dei dati!");
				}
			}
		});
	}


	private void loadData(List<OffertaModel> result) {
		store.removeAll();
		store.add(result);
	}


	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column = new ColumnConfig();
		
	    column=new ColumnConfig();
	    column.setId("ragioneSociale");
	    column.setHeader("Cliente");
	    column.setWidth(300);
	    column.setRowHeader(true);
	    ListStore<ClienteModel> store= new ListStore<ClienteModel>();
	    cmbxCliente=new ComboBox<ClienteModel>();
	    cmbxCliente.setStore(store);
	    cmbxCliente.setDisplayField("ragioneSociale");
	    cmbxCliente.setEmptyText("Selezionare..");
	    cmbxCliente.setEditable(true);
	    cmbxCliente.setVisible(true);
	    cmbxCliente.setTriggerAction(TriggerAction.ALL);
	    cmbxCliente.setForceSelection(true);
	    cmbxCliente.setAllowBlank(false);
	    
	    cmbxCliente.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
					getAllDipendenti();
			}

			private void getAllDipendenti() {
				AdministrationService.Util.getInstance().getAllClientiModel(new AsyncCallback<List<ClienteModel>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on getListaDipendentiModel();");
						caught.printStackTrace();		
					}

					@Override
					public void onSuccess(List<ClienteModel> result) {
						if(result!=null){
							ListStore<ClienteModel> lista= new ListStore<ClienteModel>();
							lista.setStoreSorter(new StoreSorter<ClienteModel>());  
							lista.setDefaultSort("ragioneSociale", SortDir.ASC);
							
							lista.add(result);
							cmbxCliente.clear();
							cmbxCliente.setStore(lista);
							
						}else 
							Window.alert("error: Errore durante l'accesso ai dati Personale.");				
					}
				});				
			}
		});
	    CellEditor editor = new CellEditor(cmbxCliente) {
	    	@Override  
	        public Object preProcessValue(Object value) {
	          if (value == null) {  
	            return value;  
	          }  
	          return cmbxCliente.getValue();  
	        } 
	        @Override  
	        public Object postProcessValue(Object value) {
	          if (value == null) {
	            return value;  
	          }  
	          return ((ModelData) value).get("ragioneSociale");  
	        }  
	    };
	    column.setEditor(editor);
	    configs.add(column);  
	  
	    column = new ColumnConfig();
	    column.setId("numeroOfferta");  
	    column.setHeader("Offerta");  
	    column.setWidth(120);
	    txtfldNumeroOfferta= new TextField<String>();	    
	    column.setEditor(new CellEditor(txtfldNumeroOfferta));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("descrizione");
	    column.setHeader("Descrizione");
	    txtfldDescrizione= new TextField<String>();	    
	    column.setEditor(new CellEditor(txtfldDescrizione));  
	    column.setWidth(400);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("dataOfferta");  
	    column.setHeader("Data Offerta");  
	    column.setWidth(160);
	    dtfldScadenzaControllo= new DateField();
	    dtfldScadenzaControllo.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/y"));  
	    column.setEditor(new CellEditor(dtfldScadenzaControllo));  
	    column.setDateTimeFormat(DateTimeFormat.getFormat("dd MMM yyy"));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("importo");  
	    column.setHeader("Importo");  
	    column.setWidth(160);  
	    txtfldImporto= new TextField<String>();	    
	    txtfldImporto.setRegex("[0-9]+[.][0-9]{1}[0-9]{1}|0.00|0.0");
	    txtfldImporto.getMessages().setRegexText("Deve essere un numero!");
	    txtfldImporto.setValue("0.00");
	    txtfldImporto.setAllowBlank(false);	
	    txtfldImporto.addKeyListener(new KeyListener(){
			
			 public void componentKeyDown(ComponentEvent event) {
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){
						
						if(txtfldImporto.getValue()==null)
							txtfldImporto.setValue("0.00");
						else{
							String valore= txtfldImporto.getValue().toString();
													
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
							txtfldImporto.setValue(valore);
						}
					}
			 }
	    });
	    column.setEditor(new CellEditor(txtfldImporto));
	    configs.add(column);	    
	    
	    column = new ColumnConfig();  
	    column.setId("statoOfferta");  
	    column.setHeader("Stato");  
	    column.setWidth(160);  
	    configs.add(column);
	
	    return configs;
	}
	
}
