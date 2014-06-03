package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.AnagraficaHardwareModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelAnagraficaHardware extends LayoutContainer{
	
	private ListStore<AnagraficaHardwareModel>store = new ListStore<AnagraficaHardwareModel>();
	private EditorGrid<AnagraficaHardwareModel> gridRiepilogo;
	private ColumnModel cm;
	private RowExpander expander;
	//private  CheckBoxSelectionModel<AnagraficaHardwareModel> sm = new CheckBoxSelectionModel<AnagraficaHardwareModel>();
	
	private Button btnAdd;
	private Button btnConferma;
	private ComboBox<PersonaleModel> cmbxPersonale= new ComboBox<PersonaleModel>();
		
	private int h=Window.getClientHeight();
		
	public PanelAnagraficaHardware(){
		
	}
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    setItemId("pnlRiepilogo");
	    
	    final FitLayout fl= new FitLayout();
	    LayoutContainer layoutContainer= new LayoutContainer();
	    layoutContainer.setBorders(false);
	    layoutContainer.setLayout(fl);	
	    //layoutContainer.setScrollMode(Scroll.NONE);
	  			
	  	ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setBodyBorder(false);         
		cntpnlGrid.setLayout(new FitLayout());  
		cntpnlGrid.setHeaderVisible(false);
		//cntpnlGrid.setWidth(530);
		cntpnlGrid.setHeight(h-40);
		cntpnlGrid.setBorders(true);
		cntpnlGrid.setScrollMode(Scroll.AUTO);
					   
		Resizable r=new Resizable(cntpnlGrid);
		
	    try {	    	
	    	cm = new ColumnModel(createColumns());  
	    	
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	    
	    caricaTabellaDati();
	    gridRiepilogo= new EditorGrid<AnagraficaHardwareModel>(store, cm);  
	    gridRiepilogo.setItemId("grid");
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setStripeRows(true);  
	    gridRiepilogo.setColumnLines(true);  
	    gridRiepilogo.setColumnReordering(true); 
	    gridRiepilogo.setColumnResize(true);
	    gridRiepilogo.getView().setShowDirtyCells(true);
	    gridRiepilogo.addPlugin(expander);  
	   // gridRiepilogo.addPlugin(sm);
	   // gridRiepilogo.setSelectionModel(sm);
	   	
	    btnAdd= new Button();
	    btnAdd.setStyleAttribute("padding-left", "2px");
	    btnAdd.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.add()));
	  	btnAdd.setIconAlign(IconAlign.TOP);
	  	btnAdd.setSize(26, 26);
	  	btnAdd.setToolTip("Nuovo Hardware");
	  	btnAdd.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				AnagraficaHardwareModel am= new AnagraficaHardwareModel();  
		        
		        gridRiepilogo.stopEditing();  
		        store.insert(am, 0);  
		        gridRiepilogo.startEditing(store.indexOf(am), 0);  
			}
		});
	    
	    btnConferma= new Button();
	    btnConferma.setStyleAttribute("padding-left", "2px");
	    btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	    btnConferma.setIconAlign(IconAlign.TOP);
	    btnConferma.setSize(26, 26);
	    btnConferma.setToolTip("Conferma dati inseriti");
	    btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
	    	//TODO check su presenza nodo indicato che deve essere univoco
	    	
			@Override
			public void componentSelected(ButtonEvent ce) {			
				for(Record record: store.getModifiedRecords()){		    		  
		    		  AnagraficaHardwareModel am = new AnagraficaHardwareModel();
		    		  am=(AnagraficaHardwareModel) record.getModel();
		    		  Integer id=(Integer)am.get("idHardware");
		    		  
		    		  AdministrationService.Util.getInstance().editDataAnagraficaHardware(id,(String) am.get("username"),
		    				  (String)am.get("gruppoLavoro"),(String)am.get("assistenza"),(String)am.get("codiceModello"),(String)am.get("cpu"),(String)am.get("fornitoreAssistenza"),(String)am.get("hardware"),
		    				  (String)am.get("hd"),(String)am.get("ip"),(String)am.get("ipFiat"),(String)am.get("modello"),(String)am.get("nodo"),(String)am.get("note"),
		    				  (String)am.get("ram"),(Date)am.get("scadenzaControllo"),(String)am.get("sede"),(String)am.get("serialId"),(String)am.get("serialNumber"),(String)am.get("sistemaOperativo"),
		    				  (String)am.get("stato"),(String)am.get("svga"),(String)am.get("tipologia"), (String)am.get("utilizzo"),new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore connessione on editDataAnagraficaHardware();");
						}

						@Override
						public void onSuccess(Boolean result) {
							if(result){
								Window.alert("Modifica effettuata.");
								caricaTabellaDati();
								
							}else{
								Window.alert("error: Impossibile effettuare la modifica!");
							}	
						}   			  
					}); 	  
		    	  }
		    	  store.commitChanges();				
			}
		});
	    
	    ToolBar tlbrButton= new ToolBar();
	    tlbrButton.add(btnAdd);
	    tlbrButton.add(btnConferma);
	    
	    cntpnlGrid.add(gridRiepilogo);
	    cntpnlGrid.setTopComponent(tlbrButton);
	    
	    //bodyContainer.add(cntpnlGrid);
	    layoutContainer.add(cntpnlGrid, new FitData(3, 3, 3, 3));
	    add(layoutContainer);
	    
	}

	private void caricaTabellaDati() {
		
		AdministrationService.Util.getInstance().getRiepilogoAnagraficaHardware(new AsyncCallback<List<AnagraficaHardwareModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getRiepilogoAnagraficaHardware();");
			}

			@Override
			public void onSuccess(List<AnagraficaHardwareModel> result) {									
				if(result!=null){
					loadTable(result);
				}				
				else Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		}); 	  
	}
	
	
	private void loadTable(List<AnagraficaHardwareModel> result) {		
		store.removeAll();
		store.add(result);
		gridRiepilogo.reconfigure(store, cm);		
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		CellEditor editorTxt;
		XTemplate tpl = XTemplate.create("<p><b>Note:</b> {note}</p><br>");  
		    
		expander = new RowExpander();
		expander.setTemplate(tpl); 
		expander.setWidth(20);
				
		configs.add(expander);
		//sm.setSelectionMode(SelectionMode.SIMPLE);
		//configs.add(sm.getColumn());
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("nodo");  
	    column.setHeader("Nodo");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldNodo= new TextField<String>();
	    editorTxt= new CellEditor(txtfldNodo){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("username");
	    column.setHeader("Utente");  
	    column.setWidth(120);  
	    column.setRowHeader(true);
	    ListStore<PersonaleModel> store= new ListStore<PersonaleModel>();
	    cmbxPersonale.setStore(store);
	    cmbxPersonale.setDisplayField("username");
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
							
						}else Window.alert("error: Errore durante l'accesso ai dati Personale.");				
					}
				});				
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
	          return ((ModelData) value).get("username");  
	        }  
	    };
	    column.setEditor(editor);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("hardware");  
	    column.setHeader("Hardware");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldHardware= new TextField<String>();
	    editorTxt= new CellEditor(txtfldHardware){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("modello");  
	    column.setHeader("Modello");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldModello= new TextField<String>();
	    editorTxt= new CellEditor(txtfldModello){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("ip");  
	    column.setHeader("IP");  
	    column.setWidth(100);  
	    column.setRowHeader(true); 
	    TextField<String> txtfldIp= new TextField<String>();
	    editorTxt= new CellEditor(txtfldIp){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);	    
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("utilizzo");  
	    column.setHeader("Utilizzo");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldUtilizzo= new TextField<String>();
	    editorTxt= new CellEditor(txtfldUtilizzo){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("sede");  
	    column.setHeader("Sede");  
	    column.setWidth(30);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldSede= new TextField<String>();
	    editorTxt= new CellEditor(txtfldSede){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("tipologia");  
	    column.setHeader("Tipologia");  
	    column.setWidth(30);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldTipologia= new TextField<String>();
	    editorTxt= new CellEditor(txtfldTipologia){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("assistenza");  
	    column.setHeader("Assistenza");  
	    column.setWidth(30);  
	    column.setRowHeader(true);
	    TextField<String> txtfldAssistenza= new TextField<String>();
	    editorTxt= new CellEditor(txtfldAssistenza){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("scadenzaControllo");  
	    column.setHeader("Scadenza Contr.");  
	    column.setWidth(100);  
	    column.setRowHeader(true);     
	    //TextField<String> txtfldScadenzaControllo= new TextField<String>();
	    DateField dtfldScadenzaControllo= new DateField();
	    dtfldScadenzaControllo.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/y"));  
	    column.setEditor(new CellEditor(dtfldScadenzaControllo));  
	    column.setDateTimeFormat(DateTimeFormat.getFormat("dd MMM yyy"));
	   /* editorTxt= new CellEditor(dtfldScadenzaControllo){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);*/
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("fornitoreAssistenza");  
	    column.setHeader("Fornit. Assist.");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldFornitoreAssistenza= new TextField<String>();
	    editorTxt= new CellEditor(txtfldFornitoreAssistenza){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("cpu");  
	    column.setHeader("CPU");  
	    column.setWidth(70);  
	    column.setRowHeader(true); 
	    TextField<String> txtfldCPU= new TextField<String>();
	    txtfldCPU.setMaxLength(25);
	    editorTxt= new CellEditor(txtfldCPU){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("ram");  
	    column.setHeader("RAM");  
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldRam= new TextField<String>();
	    txtfldRam.setMaxLength(5);
	    editorTxt= new CellEditor(txtfldRam){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("hd");  
	    column.setHeader("HD");  
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldHd= new TextField<String>();
	    txtfldHd.setMaxLength(10);
	    editorTxt= new CellEditor(txtfldHd){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("svga");  
	    column.setHeader("SVGA");  
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldSvga= new TextField<String>();
	    txtfldSvga.setMaxLength(25);
	    editorTxt= new CellEditor(txtfldSvga){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("sistemaOperativo");  
	    column.setHeader("S.O.");  
	    column.setWidth(90);  
	    column.setRowHeader(true); 
	    TextField<String> txtfldSO= new TextField<String>();
	    txtfldSO.setMaxLength(25);
	    editorTxt= new CellEditor(txtfldSO){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("serialId");  
	    column.setHeader("SerialID");  
	    column.setWidth(130);  
	    column.setRowHeader(true);   
	    TextField<String> txtfldId= new TextField<String>();
	    txtfldId.setMaxLength(17);
	    editorTxt= new CellEditor(txtfldId){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("ipFiat");  
	    column.setHeader("IP FIAT");  
	    column.setWidth(130);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldIpFiat= new TextField<String>();
	    txtfldIpFiat.setMaxLength(16);
	    editorTxt= new CellEditor(txtfldIpFiat){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("codiceModello");  
	    column.setHeader("Cod. Modello");  
	    column.setWidth(130);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldCodModello= new TextField<String>();
	    txtfldCodModello.setMaxLength(30);
	    editorTxt= new CellEditor(txtfldCodModello){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("serialNumber");  
	    column.setHeader("S/N");  
	    column.setWidth(130);  
	    column.setRowHeader(true);  
	    TextField<String> txtfldCodSerialNumber= new TextField<String>();
	    txtfldCodSerialNumber.setMaxLength(30);
	    editorTxt= new CellEditor(txtfldCodSerialNumber){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("stato");  
	    column.setHeader("Stato");  
	    column.setWidth(80);  
	    column.setRowHeader(true); 
	    final SimpleComboBox<String> smplcmbxStato= new SimpleComboBox<String>();
	    smplcmbxStato.setForceSelection(true);  
	    smplcmbxStato.setTriggerAction(TriggerAction.ALL); 
	    smplcmbxStato.add("Utilizzato");
	    smplcmbxStato.add("Dismesso");
	    editorTxt= new CellEditor(smplcmbxStato){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return smplcmbxStato.findModel(value.toString());  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return ((ModelData) value).get("value");  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("note");  
	    column.setHeader("Note");  
	    column.setWidth(255);  
	    column.setRowHeader(true); 
	    TextField<String> txtfldNote= new TextField<String>();
	    txtfldNote.setMaxLength(255);
	    editorTxt= new CellEditor(txtfldNote){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column); 
		
		return configs;
	}


}
