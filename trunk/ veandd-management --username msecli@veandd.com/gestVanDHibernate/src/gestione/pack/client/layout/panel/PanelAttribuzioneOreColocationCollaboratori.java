package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RiepilogoOreDipCommesseGiornaliero;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelAttribuzioneOreColocationCollaboratori extends LayoutContainer{
	
	private GroupingStore<RiepilogoOreDipCommesseGiornaliero>store = new GroupingStore<RiepilogoOreDipCommesseGiornaliero>();
	private EditorGrid<RiepilogoOreDipCommesseGiornaliero> gridRiepilogo;
	private ColumnModel cm;
	//private CheckBoxSelectionModel<RiepilogoOreDipCommesseGiornaliero> sm = new CheckBoxSelectionModel<RiepilogoOreDipCommesseGiornaliero>();  
	
	private SimpleComboBox<String> smplcmbxPM;
	private DateField dtfldData;
	private Button btnAggiorna;
	private Button btnConferma;
	private Button btnReset;
	
	private int w=Window.getClientWidth();
	private String pm;
	protected int conta=0;
	protected int nModificati=0;
	
	private boolean completed=false;
	private boolean isnew=false;
	
	public PanelAttribuzioneOreColocationCollaboratori(String pm){
		this.pm=pm;
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
	
		final FitLayout fl= new FitLayout();
		
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setHeight((830));
		cpGrid.setWidth(w-250);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		
		smplcmbxPM = new SimpleComboBox<String>();
		smplcmbxPM.setFieldLabel("Project Manager");
		smplcmbxPM.setName("pm");
		smplcmbxPM.setAllowBlank(true);
		smplcmbxPM.setTriggerAction(TriggerAction.ALL);
		smplcmbxPM.setEmptyText("Project Manager..");
		smplcmbxPM.setAllowBlank(false);
		getNomePm();
		    
		dtfldData= new DateField();
		dtfldData.setValue(new Date());
		
		store.groupBy("dipendente");
		store.setSortField("numeroCommessa");
		store.setSortDir(SortDir.ASC);			
		caricaDatiTabella();
		
		GroupSummaryView summary = new GroupSummaryView();  
		summary.setForceFit(false);  
		summary.setShowGroupedColumn(false);
		summary.setStartCollapsed(false);
		
		cm = new ColumnModel(createColumns());		
		gridRiepilogo= new EditorGrid<RiepilogoOreDipCommesseGiornaliero>(store, cm);  
		gridRiepilogo.setBorders(false);
		gridRiepilogo.setItemId("grid");
		gridRiepilogo.setColumnLines(true);
	    gridRiepilogo.setStripeRows(true);
	    //gridRiepilogo.addPlugin(sm);
	    gridRiepilogo.setView(summary);
	    //gridRiepilogo.setSelectionModel(sm);
	    gridRiepilogo.getView().setShowDirtyCells(true);	      
		
		btnAggiorna= new Button();
		btnAggiorna.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.reload()));
		btnAggiorna.setToolTip("Load");
		btnAggiorna.setIconAlign(IconAlign.TOP);
		btnAggiorna.setSize(26, 26);
		btnAggiorna.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {		
				caricaDatiTabella();
			}
		});		
		
		btnConferma= new Button();
		btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
		btnConferma.setToolTip("Conferma");
		btnConferma.setIconAlign(IconAlign.TOP);
		btnConferma.setSize(26, 26);
		btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
				Date data=dtfldData.getValue();
				nModificati=store.getModifiedRecords().size();
						
				for(Record record: store.getModifiedRecords()){
					//isnew=true;			
					
					RiepilogoOreDipCommesseGiornaliero g= new RiepilogoOreDipCommesseGiornaliero();
		    		g=(RiepilogoOreDipCommesseGiornaliero) record.getModel();		    		  
		  
		    		
		    				AdministrationService.Util.getInstance().elaboraDatiOreCollaboratori(g, data, new AsyncCallback<Boolean>() {

		    					@Override
		    					public void onFailure(Throwable caught) {
		    						Window.alert("Errore di connessione on editDataCostiAzienda();");
		    					}

		    					@Override
		    					public void onSuccess(Boolean result) {									
		    						if(!result){
		    							Window.alert("Errore durante il salvataggio dati!");
		    						}
		    						else{
		    							//completed=true;
		    							conta+=1;
		    							if(conta==nModificati)
		    								caricaDatiTabella();
		    						}
		    					}		    			  
		    				});
		    			    		  
				}		    
				store.commitChanges();	    	
			}		
		});	
		
		btnReset= new Button();
		btnReset.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.azzera()));
		btnReset.setToolTip("Reset");
		btnReset.setIconAlign(IconAlign.TOP);
		btnReset.setSize(26, 26);
		btnReset.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {		
				store.rejectChanges();
			}
		});	
		
	    ToolBar tlbrOpzioni= new ToolBar();
		tlbrOpzioni.add(smplcmbxPM);
		tlbrOpzioni.add(new SeparatorToolItem());
		tlbrOpzioni.add(dtfldData);
		tlbrOpzioni.add(new SeparatorToolItem());
		tlbrOpzioni.add(btnAggiorna);
		tlbrOpzioni.add(new SeparatorToolItem());
		tlbrOpzioni.add(btnConferma);
		tlbrOpzioni.add(new SeparatorToolItem());
		tlbrOpzioni.add(btnReset);
	    
	    cpGrid.setTopComponent(tlbrOpzioni);
	    cpGrid.add(gridRiepilogo);
	    
		layoutContainer.add(cpGrid, new FitData(3, 3, 3, 3));
		add(layoutContainer);
	}
	
	

	private void caricaDatiTabella() {		
		String pm=smplcmbxPM.getRawValue().toString();
		Date data=dtfldData.getValue();
		nModificati=0;
		conta=0;
		
		if(pm!="")
		AdministrationService.Util.getInstance().getDatiOreCollaboratori(pm, data, new AsyncCallback<List<RiepilogoOreDipCommesseGiornaliero>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getDatiOreCollaboratori();");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<RiepilogoOreDipCommesseGiornaliero> result) {
				if(result!=null){
					store.removeAll();
					store.add(result);
					gridRiepilogo.reconfigure(store, cm);
				}				
				else Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		});						
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
					smplcmbxPM.setSimpleValue(pm);
				}				
				else Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		});
	}

	private List<ColumnConfig> createColumns() {
		
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number= NumberFormat.getFormat("0.00");
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("numeroCommessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(130);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("dipendente");  
	    column.setHeader("Dipendente");  
	    column.setWidth(200);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    //column.setRenderer(renderer);
	    configs.add(column); 
	   	       	    	    
	    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
	    columnOreLavoro.setId("oreLavoro");  
	    columnOreLavoro.setHeader("Ore Lavoro");  
	    columnOreLavoro.setWidth(80);    
	    columnOreLavoro.setRowHeader(true);  
	    columnOreLavoro.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreLavoro.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesseGiornaliero>() {
			@Override
			public Object render(RiepilogoOreDipCommesseGiornaliero model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreDipCommesseGiornaliero> store,
					Grid<RiepilogoOreDipCommesseGiornaliero> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});   
	    TextField<String> txtfldOreLavoro= new TextField<String>();
	    txtfldOreLavoro.setRegex("^([0-9]+).(0|00|15|30|45)$");
	    txtfldOreLavoro.getMessages().setRegexText("Deve essere un numero!");
	    CellEditor editor = new CellEditor(txtfldOreLavoro) {  
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
	          String n=(String)value;
	          return Float.valueOf(n);	          
	        }  
	    };
	    columnOreLavoro.setEditor(editor);
	    configs.add(columnOreLavoro); 	
	    
	    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
	    columnOreViaggio.setId("oreViaggio");  
	    columnOreViaggio.setHeader("Ore Viaggio");  
	    columnOreViaggio.setWidth(80);    
	    columnOreViaggio.setRowHeader(true);  
	    columnOreViaggio.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreViaggio.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesseGiornaliero>() {
			@Override
			public Object render(RiepilogoOreDipCommesseGiornaliero model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreDipCommesseGiornaliero> store,
					Grid<RiepilogoOreDipCommesseGiornaliero> grid) {
				Float n= model.get(property);
				return number.format(n);
			}  	
		}); 
	    TextField<String> txtfldOreViaggio= new TextField<String>();
	    txtfldOreViaggio.setRegex("^([0-9]+).(0|00|15|30|45)$");
	    txtfldOreViaggio.getMessages().setRegexText("Deve essere un numero!");
	    editor = new CellEditor(txtfldOreViaggio) {  
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
	          String n=(String)value;
	          return Float.valueOf(n);	 
	        }  
	    };
	  //  columnOreViaggio.setEditor(new CellEditor(txtfldOreViaggio));
	    columnOreViaggio.setEditor(editor);
	    configs.add(columnOreViaggio); 	
	  		
		return configs;
	}
}
