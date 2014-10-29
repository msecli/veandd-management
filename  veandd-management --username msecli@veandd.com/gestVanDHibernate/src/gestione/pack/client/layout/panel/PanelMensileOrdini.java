package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.RiepilogoMensileOrdiniModel;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.SummaryRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;

public class PanelMensileOrdini extends LayoutContainer{

	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private ListStore<RiepilogoMensileOrdiniModel> storeRiepOrdini=new ListStore<RiepilogoMensileOrdiniModel>();
	private ListStore<RiepilogoMensileOrdiniModel>storeRes = new ListStore<RiepilogoMensileOrdiniModel>();
	private ColumnModel cmRiepOrdini;
	
	private GroupingStore<RiepilogoMensileOrdiniModel> storeRiepMensile=new GroupingStore<RiepilogoMensileOrdiniModel>();
	private ColumnModel cmRiepMensile;
	
	private EditorGrid<RiepilogoMensileOrdiniModel> gridRiepOrdini;
	private EditorGrid<RiepilogoMensileOrdiniModel> gridRiepMensile;
	private CheckBoxSelectionModel<RiepilogoMensileOrdiniModel> sm = new CheckBoxSelectionModel<RiepilogoMensileOrdiniModel>();
	
	private SimpleComboBox<String> smplcmbxStatoOrdini;
	private SimpleComboBox<String> smplcmbxCliente;
	private SimpleComboBox<String> smplcmbxPM;
	private SimpleComboBox<String> smplcmbxAnno;
	
	private Button btnAggiorna;
	private Button btnChiudi;
	private Button btnPrint;
	
	private PanelRtv pnlRtv= new PanelRtv();
	
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	private String ruolo= new String();
	private String numeroOrdine= new String();
	
	public PanelMensileOrdini(String ruolo){
		this.ruolo=ruolo;
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		VerticalPanel vp= new VerticalPanel();
		vp.setSpacing(3);
		
		HorizontalPanel hp= new HorizontalPanel();
		hp.setSpacing(2);
				
		ContentPanel cpGridRiepOrdini= new ContentPanel();
		cpGridRiepOrdini.setHeaderVisible(true);
		cpGridRiepOrdini.setHeading("Riepilogo Ordini");
		cpGridRiepOrdini.setBorders(false);
		cpGridRiepOrdini.setFrame(true);
		cpGridRiepOrdini.setHeight((h-55)/2+80);
		cpGridRiepOrdini.setWidth(w-155);
		cpGridRiepOrdini.setScrollMode(Scroll.AUTO);
		cpGridRiepOrdini.setLayout(new FitLayout());
		cpGridRiepOrdini.setButtonAlign(HorizontalAlignment.CENTER);  
		Resizable r= new Resizable(cpGridRiepOrdini);
		
		ContentPanel cpGridRiepMensile= new ContentPanel();
		cpGridRiepMensile.setHeaderVisible(true);
		cpGridRiepMensile.setHeading("Dettaglio Mensile");
		cpGridRiepMensile.setBorders(false);
		cpGridRiepMensile.setFrame(true);
		cpGridRiepMensile.setHeight((h-55)/2-140);
		cpGridRiepMensile.setWidth((w-155)/2);
		cpGridRiepMensile.setScrollMode(Scroll.AUTO);
		cpGridRiepMensile.setLayout(new FitLayout());
		cpGridRiepMensile.setButtonAlign(HorizontalAlignment.CENTER);  
		
		GroupSummaryView summary = new GroupSummaryView();  
		summary.setForceFit(false);  
		summary.setShowGroupedColumn(false);
		
		//storeRiepOrdini.groupBy("statoOrdine");
		storeRiepOrdini.setDefaultSort("commessa", SortDir.ASC);
		cmRiepOrdini = new ColumnModel(createColumnsOrdini());		
		gridRiepOrdini= new EditorGrid<RiepilogoMensileOrdiniModel>(storeRiepOrdini, cmRiepOrdini);  
		gridRiepOrdini.setBorders(false);
		gridRiepOrdini.setItemId("grid");
		gridRiepOrdini.setColumnLines(true);
		gridRiepOrdini.setStripeRows(true);
		gridRiepOrdini.addPlugin(sm);
		gridRiepOrdini.setSelectionModel(sm);
		//gridRiepOrdini.setView(summary);
		gridRiepOrdini.addListener(Events.CellDoubleClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent be) {
				numeroOrdine=sm.getSelectedItem().get("numeroOrdine");
				pnlRtv.setNumeroOrdine(numeroOrdine);
				pnlRtv.caricaDatiTabellaRtv(numeroOrdine);	
										
				AdministrationService.Util.getInstance().getDettaglioMensileOrdine(numeroOrdine, new AsyncCallback<List<RiepilogoMensileOrdiniModel>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore di connessione on getDettaglioMensileOrdine();");
					}

					@Override
					public void onSuccess(
							List<RiepilogoMensileOrdiniModel> result) {
						caricaTabellaDatiDettaglioMensile(result);
					}
				});			
			}
		});
		
		GroupSummaryView summary1 = new GroupSummaryView();  
		summary1.setForceFit(false);  
		summary1.setShowGroupedColumn(false);				
		storeRiepMensile.groupBy("pm");
		storeRiepMensile.setDefaultSort("statoOrdine", SortDir.ASC);
		cmRiepMensile=new ColumnModel(createColumnsMesi());
		gridRiepMensile= new EditorGrid<RiepilogoMensileOrdiniModel>(storeRiepMensile, cmRiepMensile);
		gridRiepMensile.setBorders(false);
		gridRiepMensile.setItemId("grid");
		gridRiepMensile.setStripeRows(true); 
		gridRiepMensile.setColumnLines(true);
		gridRiepMensile.setView(summary1);
		
		ToolBar tlbrScelte= new ToolBar();
		
		Date d= new Date();
		String data= d.toString();
		String anno= data.substring(data.length()-4, data.length());
		smplcmbxAnno= new SimpleComboBox<String>();
		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setAllowBlank(false);
		smplcmbxAnno.setWidth(100);
		 for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);}
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setSimpleValue(anno);
		
		smplcmbxStatoOrdini=new SimpleComboBox<String>();
		smplcmbxStatoOrdini.setFieldLabel("Stato");
		smplcmbxStatoOrdini.setName("stato");
		smplcmbxStatoOrdini.setWidth(100);
		smplcmbxStatoOrdini.add("Aperti");
		smplcmbxStatoOrdini.add("Chiusi");
		smplcmbxStatoOrdini.add("Tutti");
		smplcmbxStatoOrdini.setTriggerAction(TriggerAction.ALL);
		smplcmbxStatoOrdini.setSimpleValue("Aperti");
		/*smplcmbxStatoOrdini.addSelectionChangedListener( new SelectionChangedListener<SimpleComboValue<String>>() {		
			@Override
			public void selectionChanged(
					SelectionChangedEvent<SimpleComboValue<String>> se) {
				if(smplcmbxStatoOrdini.getRawValue().toString().compareTo("Tutti")==0)
					gridRiepOrdini.reconfigure(storeRiepOrdini, cmRiepOrdini);
					
				else{
					storeRes.removeAll();
					for(RiepilogoMensileOrdiniModel r:storeRiepOrdini.getModels())
						if(smplcmbxStatoOrdini.getRawValue().toString().substring(0, 1).compareTo((String) r.get("statoOrdine"))==0)
							storeRes.add(r);
					gridRiepOrdini.reconfigure(storeRes, cmRiepOrdini);
				}
			}
		});*/
		
		smplcmbxPM= new SimpleComboBox<String>();
		smplcmbxPM.setFieldLabel("Project Manager");
		smplcmbxPM.setName("pm");
		smplcmbxPM.setAllowBlank(true);
		smplcmbxPM.setTriggerAction(TriggerAction.ALL);
		smplcmbxPM.setWidth(150);
		smplcmbxPM.setEmptyText("Project Manager...");
		smplcmbxPM.addSelectionChangedListener( new SelectionChangedListener<SimpleComboValue<String>>() {		
			@Override
			public void selectionChanged(
					SelectionChangedEvent<SimpleComboValue<String>> se) {
				if(smplcmbxPM.getRawValue().toString().compareTo("Tutti")==0)
					gridRiepOrdini.reconfigure(storeRiepOrdini, cmRiepOrdini);
					
				else{
					storeRes.removeAll();
					for(RiepilogoMensileOrdiniModel r:storeRiepOrdini.getModels())
						if(smplcmbxPM.getRawValue().toString().compareTo((String) r.get("pm"))==0)
							storeRes.add(r);
					storeRes.setSortField("commessa");
					storeRes.setSortDir(SortDir.ASC);
					gridRiepOrdini.reconfigure(storeRes, cmRiepOrdini);
				}
			}
		});
		smplcmbxPM.addListener(Events.Select, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				if(smplcmbxPM.getRawValue().toString().compareTo("Tutti")==0)
					gridRiepOrdini.reconfigure(storeRiepOrdini, cmRiepOrdini);
					
				else{
					storeRes.removeAll();
					for(RiepilogoMensileOrdiniModel r:storeRiepOrdini.getModels())
						if(smplcmbxPM.getRawValue().toString().compareTo((String) r.get("pm"))==0)
							storeRes.add(r);
					storeRes.setSortField("commessa");
					storeRes.setSortDir(SortDir.ASC);
					gridRiepOrdini.reconfigure(storeRes, cmRiepOrdini);
				}
			}
		});
		getNomePm();
		
		smplcmbxCliente= new SimpleComboBox<String>();
		smplcmbxCliente.setFieldLabel("Cliente");
		smplcmbxCliente.setName("cliente");
		smplcmbxCliente.setAllowBlank(true);
		smplcmbxCliente.setTriggerAction(TriggerAction.ALL);
		smplcmbxCliente.setWidth(150);
		smplcmbxCliente.setEmptyText("Cliente...");
		smplcmbxCliente.addSelectionChangedListener( new SelectionChangedListener<SimpleComboValue<String>>() {		
			@Override
			public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {				
				if(smplcmbxCliente.getRawValue().toString().compareTo("Tutti")==0)
					gridRiepOrdini.reconfigure(storeRiepOrdini, cmRiepOrdini);
					
				else{
					storeRes.removeAll();
					for(RiepilogoMensileOrdiniModel r:storeRiepOrdini.getModels())
						if(smplcmbxCliente.getRawValue().toString().compareTo((String) r.get("cliente"))==0)
							storeRes.add(r);
					storeRes.setSortField("commessa");
					storeRes.setSortDir(SortDir.ASC);
					gridRiepOrdini.reconfigure(storeRes, cmRiepOrdini);
				}						
			}
		});
		smplcmbxCliente.addListener(Events.Select, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				if(smplcmbxCliente.getRawValue().toString().compareTo("Tutti")==0)
					gridRiepOrdini.reconfigure(storeRiepOrdini, cmRiepOrdini);
					
				else{
					storeRes.removeAll();
					for(RiepilogoMensileOrdiniModel r:storeRiepOrdini.getModels())
						if(smplcmbxCliente.getRawValue().toString().compareTo((String) r.get("cliente"))==0)
							storeRes.add(r);
					storeRes.setSortField("commessa");
					storeRes.setSortDir(SortDir.ASC);
					gridRiepOrdini.reconfigure(storeRes, cmRiepOrdini);
				}	
			}
		});
		getClienti();
		
		btnAggiorna= new Button();
		btnAggiorna.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.reload()));
		btnAggiorna.setToolTip("Load");
		btnAggiorna.setIconAlign(IconAlign.TOP);
		btnAggiorna.setSize(26, 26);
		btnAggiorna.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
				caricaTabellaDatiOrdini();	
			}
		});	
			
		btnChiudi=new Button();
		btnChiudi.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.chiudiCommessa()));
		btnChiudi.setToolTip("Chiudi Ordini");
		btnChiudi.setIconAlign(IconAlign.TOP);
		btnChiudi.setSize(26, 26);
		btnChiudi.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {		
						
				List<RiepilogoMensileOrdiniModel> listaC= new ArrayList<RiepilogoMensileOrdiniModel>();
				listaC.addAll(gridRiepOrdini.getSelectionModel().getSelectedItems());
				 				
 				for(RiepilogoMensileOrdiniModel c:listaC){
 					String numeroOrdine=c.get("numeroOrdine");
 					AdministrationService.Util.getInstance().chiudiOrdine(numeroOrdine, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore di connessione on confermaEditCommenti()");
							
						}

						@Override
						public void onSuccess(Boolean result) {
														
						}
					});
 					
 				}
 				caricaTabellaDatiOrdini();
			}
		});	
		
		btnPrint=new Button();
		btnPrint.setSize("55px","25px");	   
		btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
		btnPrint.setToolTip("Stampa");
		btnPrint.setIconAlign(IconAlign.TOP);
		btnPrint.setSize(26, 26);
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
							
				SessionManagementService.Util.getInstance().setDatiMensileInSession("STAMPAMENSILE", smplcmbxAnno.getRawValue().toString(),
						storeRes.getModels(), new AsyncCallback<Boolean>() {

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
			}
		});
		
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setAction(url);
		fp.add(btnPrint);
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(fp);		
		
		if(ruolo.compareTo("PM")==0){
			btnChiudi.setVisible(false);
			btnPrint.setVisible(false);
			smplcmbxAnno.setVisible(false);
		}
			
		Text txtFiltri= new Text();
		txtFiltri.setText("Filtri: ");
		Text txtCerca= new Text();
		txtCerca.setText("Cerca: ");
		Text txtStampa= new Text();
		txtStampa.setText("Stampa: ");
		final TextField<String> txtfldsearch= new TextField<String>();
		txtfldsearch.setEmptyText("Digita Ordine..");
		txtfldsearch.addKeyListener(new KeyListener(){
			public void componentKeyUp(ComponentEvent event) {
				if(txtfldsearch.getRawValue().isEmpty()){
		    		 storeRes.removeAll();
		    		 gridRiepOrdini.reconfigure(storeRiepOrdini, cmRiepOrdini);
		    	}else{
		    		 String numOrdine;		 	    		 
		    		 String campo= txtfldsearch.getValue().toString();	    			 		    		 
		    		 storeRes.removeAll();
		    		 for(RiepilogoMensileOrdiniModel r:storeRiepOrdini.getModels()){
		    			 numOrdine=(String)r.get("numeroOrdine");
		    			 if(numOrdine.contains(campo))
		    				 storeRes.add(r);	    			 
		    		 }	    		 
		    		 gridRiepOrdini.reconfigure(storeRes, cmRiepOrdini);			 
		    	 } 
		     }	    	  	 
		});		   
		
		tlbrScelte.add(txtCerca);
		tlbrScelte.add(smplcmbxStatoOrdini);
		tlbrScelte.add(btnAggiorna);
		tlbrScelte.add(new SeparatorToolItem());
		tlbrScelte.add(txtFiltri);
		tlbrScelte.add(smplcmbxCliente);
		tlbrScelte.add(smplcmbxPM);
		tlbrScelte.add(txtfldsearch);
		tlbrScelte.add(new SeparatorToolItem());		
		tlbrScelte.add(btnChiudi);
		tlbrScelte.add(new SeparatorToolItem());
		tlbrScelte.add(txtStampa);
		tlbrScelte.add(smplcmbxAnno);
		tlbrScelte.add(cp);
			
		cpGridRiepOrdini.setTopComponent(tlbrScelte);
		cpGridRiepOrdini.add(gridRiepOrdini);
		cpGridRiepMensile.add(gridRiepMensile);
		
		hp.add(cpGridRiepMensile);
		if(ruolo.compareTo("PM")==0)
			pnlRtv.setButtonDisable();
		hp.add(pnlRtv);
		
		vp.add(cpGridRiepOrdini);
		vp.add(hp);
		   
		layoutContainer.add(vp, new FitData(0, 3, 3, 0));
					
		add(layoutContainer);	
	}
	
	private void caricaTabellaDatiOrdini() {
			String stato=smplcmbxStatoOrdini.getRawValue().toString();
			AdministrationService.Util.getInstance().getRiepilogoOrdini(stato,
					new AsyncCallback<List<RiepilogoMensileOrdiniModel>>(){

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore di connessione on getRiepilogoOrdiniPerAnno();");
				}

				@Override
				public void onSuccess(List<RiepilogoMensileOrdiniModel> result) {
					if(result==null)
						Window.alert("Impossibile completare l'operazione selezionata!");
					else
						loadTable(result);
				}					
			});	
	}		

	void loadTable(List<RiepilogoMensileOrdiniModel> result){
		storeRiepOrdini.removeAll();
		storeRiepOrdini.add(result);
		storeRes.removeAll();
		storeRes.add(result);
		gridRiepOrdini.reconfigure(storeRiepOrdini, cmRiepOrdini);
	}

	private void caricaTabellaDatiDettaglioMensile(List<RiepilogoMensileOrdiniModel> result) {
		storeRiepMensile.removeAll();
		storeRiepMensile.add(result);
		storeRiepMensile.setDefaultSort("statoOrdine", SortDir.ASC);
		gridRiepMensile.reconfigure(storeRiepMensile, cmRiepMensile);
	}
	
	private List<ColumnConfig> createColumnsMesi() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
		SummaryColumnConfig<Double> column = new SummaryColumnConfig<Double>();  
		GridCellRenderer<RiepilogoMensileOrdiniModel> renderer = new GridCellRenderer<RiepilogoMensileOrdiniModel>() {
            public String render(RiepilogoMensileOrdiniModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<RiepilogoMensileOrdiniModel> store, Grid<RiepilogoMensileOrdiniModel> grid) {
            	Float n=model.get(property);
            	return num.format(n);				
        }};
        
		column = new SummaryColumnConfig<Double>();  
	    column.setId("pm");  
	    column.setHeader("Project Manager");  
	    column.setWidth(140);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
		
	    column = new SummaryColumnConfig<Double>();  
	    column.setId("cliente");  
	    column.setHeader("Mese");  
	    column.setWidth(100);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
		
	    column = new SummaryColumnConfig<Double>();  
	    column.setId("importoOrdine");  
	    column.setHeader("Importo Fatturato");  
	    column.setWidth(100);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setSummaryType(SummaryType.SUM);
	    column.setRenderer(renderer);
	    column.setSummaryRenderer(new SummaryRenderer() {
			
			@Override
			public String render(Number value, Map<String, Number> data) {
				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				return num.format(value);
			}
		});
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();  
	    column.setId("oreOrdine");  
	    column.setHeader("Ore Fatturate");  
	    column.setWidth(100);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setSummaryType(SummaryType.SUM); 
	    column.setRenderer(renderer);
	    column.setSummaryRenderer(new SummaryRenderer() {
			
			@Override
			public String render(Number value, Map<String, Number> data) {
				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				return num.format(value);
			}
		});
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();  
	    column.setId("numeroOrdine");  
	    column.setHeader("Tariffa");  
	    column.setWidth(100);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();  
	    column.setId("dataOrdine");  
	    column.setHeader("Descrizione");
	    column.setWidth(200);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    	    
		return configs;		
	}

	
	private List<ColumnConfig> createColumnsOrdini() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number= NumberFormat.getFormat("0.00");
		
		//TODO se metto la selezione con check box non fa il summary!
		
		GridCellRenderer<RiepilogoMensileOrdiniModel> renderer = new GridCellRenderer<RiepilogoMensileOrdiniModel>() {
            public String render(RiepilogoMensileOrdiniModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<RiepilogoMensileOrdiniModel> store, Grid<RiepilogoMensileOrdiniModel> grid) {
            	Float n=model.get(property);
            	return number.format(n);				
        }};
		
        sm.setSelectionMode(SelectionMode.MULTI);
		configs.add(sm.getColumn());
     
		ColumnConfig /*column = new ColumnConfig();  
	    column.setId("statoOrdine");  
	    column.setHeader("Stato");  
	    column.setWidth(30);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);*/		
		
		column = new ColumnConfig();  
	    column.setId("cliente");  
	    column.setHeader("Cliente");  
	    column.setWidth(180);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("pm");  
	    column.setHeader("Project Manager");  
	    column.setWidth(140);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("numeroOrdine");  
	    column.setHeader("Ordine");
	    column.setWidth(100);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoMensileOrdiniModel>() {

			@Override
			public Object render(RiepilogoMensileOrdiniModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<RiepilogoMensileOrdiniModel> store, Grid<RiepilogoMensileOrdiniModel> grid) {
				Float importoResiduo=model.get("importoResiduo");
				if(importoResiduo>0)
				{
					String color = "#90EE90";					                    
					config.style = config.style + ";background-color:" + color + ";";									
				}
				else
					config.style = config.style + ";background-color:" + "#FFFFFF" + ";";
				
				return model.get(property);
			}
		});
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("dataOrdine");  
	    column.setHeader("Data Ordine");  
	    column.setWidth(100);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
		
	    column = new ColumnConfig();  
	    column.setId("commessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(100);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("numeroRda");  
	    column.setHeader("RDA");  
	    column.setWidth(80);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("attivita");  
	    column.setHeader("Attivita");  
	    column.setWidth(160);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("numeroOfferta");  
	    column.setHeader("Offerta");  
	    column.setWidth(80);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("tariffa");  
	    column.setHeader("Tariffa");  
	    column.setWidth(60);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("importoOrdine");  
	    column.setHeader("Importo");  
	    column.setWidth(100);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(renderer);
	   /* column.setSummaryType(SummaryType.SUM); 
	    column.setSummaryRenderer(new SummaryRenderer() {
			
			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}
		});*/
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("oreOrdine");  
	    column.setHeader("Ore");  
	    column.setWidth(80);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(renderer);
	    column.setStyle("background-color:#FFFF7E;");
	    /*column.setSummaryType(SummaryType.SUM); 
	    column.setSummaryRenderer(new SummaryRenderer() {
			
			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}
		});*/
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("importoResiduo");  
	    column.setHeader("Importo Residuo");  
	    column.setWidth(100);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(renderer);	    
	    /*column.setSummaryType(SummaryType.SUM); 
	    column.setSummaryRenderer(new SummaryRenderer() {
			
			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}
		});*/
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("oreResidue");  
	    column.setHeader("Ore Residue");  
	    column.setWidth(80);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoMensileOrdiniModel>() {

			@Override
			public Object render(RiepilogoMensileOrdiniModel model,
					String property, ColumnData config, int rowIndex,
					int colIndex, ListStore<RiepilogoMensileOrdiniModel> store,
					Grid<RiepilogoMensileOrdiniModel> grid) {
				
				if((Float)model.get(property)>0)
					config.style = config.style +"font-weight:bold;" ;  
            	else
            		config.style = config.style +"font-weight:normal;" ;
				
				Float n=model.get(property);
            	return number.format(n);
			}
		});
	    column.setStyle("background-color:#FFFF7E;");
	    /*column.setSummaryType(SummaryType.SUM); 
	    column.setSummaryRenderer(new SummaryRenderer() {			
			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}
		});*/
	    configs.add(column);
		
		return configs;
	}

	private void getNomePm() {				
		AdministrationService.Util.getInstance().getNomePM(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getNomePM();");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<String> result) {
				if(result!=null){
					smplcmbxPM.add(result);
					smplcmbxPM.add("Tutti");
					//smplcmbxPM.setSimpleValue("Tutti");
				}				
				else Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		});			
	}
	
	public void getClienti(){
		
		AdministrationService.Util.getInstance().getRagioneSociale(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione;");
				caught.printStackTrace();				
			}
			@Override
			public void onSuccess(List<String> result) {
				if(result!=null){
					java.util.Collections.sort(result);
					smplcmbxCliente.add(result);
					smplcmbxCliente.recalculate();
					smplcmbxCliente.add("Tutti");				
				}
				else Window.alert("error: Errorre durante l'accesso ai dati Clienti.");					
			}
		});		
	}
}
