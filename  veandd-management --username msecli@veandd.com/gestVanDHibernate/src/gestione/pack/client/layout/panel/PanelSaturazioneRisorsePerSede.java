package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.SaturazioneRisorsaModel;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelSaturazioneRisorsePerSede extends LayoutContainer{

	private SimpleComboBox<String> smplcmbxAnno;
	private SimpleComboBox<String> smplcmbxSede;
	private Button btnAggiorna;
	
	private GroupingStore<SaturazioneRisorsaModel> storeSaturazioneRisorsa=new GroupingStore<SaturazioneRisorsaModel>();
	private ColumnModel cmSaturazioneRisorsa1;
	private ColumnModel cmSaturazioneRisorsa2;
	private EditorGrid<SaturazioneRisorsaModel> gridSaturazioneRisorsa1;
	private EditorGrid<SaturazioneRisorsaModel> gridSaturazioneRisorsa2;
	
	public PanelSaturazioneRisorsePerSede(){
		
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		Date d= new Date();
		String data= d.toString();
		String anno= data.substring(data.length()-4, data.length());
		
		VerticalPanel hp= new VerticalPanel();
		hp.setSpacing(3);
		
		ContentPanel cpGridSaturazione1= new ContentPanel();
		cpGridSaturazione1.setHeaderVisible(false);
		cpGridSaturazione1.setBorders(false);
		cpGridSaturazione1.setFrame(true);
		cpGridSaturazione1.setHeight(460);
		cpGridSaturazione1.setWidth(1400);
		cpGridSaturazione1.setScrollMode(Scroll.AUTO);
		cpGridSaturazione1.setLayout(new FitLayout());
		cpGridSaturazione1.setButtonAlign(HorizontalAlignment.CENTER);
		
		ContentPanel cpGridSaturazione2= new ContentPanel();
		cpGridSaturazione2.setHeaderVisible(false);
		cpGridSaturazione2.setBorders(false);
		cpGridSaturazione2.setFrame(true);
		cpGridSaturazione2.setHeight(460);
		cpGridSaturazione2.setWidth(1400);
		cpGridSaturazione2.setScrollMode(Scroll.AUTO);
		cpGridSaturazione2.setLayout(new FitLayout());
		cpGridSaturazione2.setButtonAlign(HorizontalAlignment.CENTER);  
				
		storeSaturazioneRisorsa.groupBy("risorsa");
		cmSaturazioneRisorsa1=new ColumnModel(createColumnsCostingRisorse1());
		gridSaturazioneRisorsa1= new EditorGrid<SaturazioneRisorsaModel>(storeSaturazioneRisorsa, cmSaturazioneRisorsa1);
		gridSaturazioneRisorsa1.setBorders(false);
		gridSaturazioneRisorsa1.setItemId("grid");
		gridSaturazioneRisorsa1.setStripeRows(true);
		gridSaturazioneRisorsa1.setColumnLines(true);
				
		cmSaturazioneRisorsa2=new ColumnModel(createColumnsCostingRisorse2());
		gridSaturazioneRisorsa2= new EditorGrid<SaturazioneRisorsaModel>(storeSaturazioneRisorsa, cmSaturazioneRisorsa2);
		gridSaturazioneRisorsa2.setBorders(false);
		gridSaturazioneRisorsa2.setItemId("grid");
		gridSaturazioneRisorsa2.setStripeRows(true);
		gridSaturazioneRisorsa2.setColumnLines(true);
	
		ToolBar tlbrPanel= new ToolBar();
		
		btnAggiorna= new Button();
		btnAggiorna.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.refresh()));
		btnAggiorna.setIconAlign(IconAlign.TOP);
		btnAggiorna.setToolTip("Aggiorna");
		btnAggiorna.setSize(26, 26);
		btnAggiorna.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
		
				caricaDatiTabellaSaturazioneRisorsa(smplcmbxSede.getRawValue().toString(), smplcmbxAnno.getRawValue().toString());
				
			}
		});
		
		smplcmbxAnno= new SimpleComboBox<String>();
		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setAllowBlank(false);
		 for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);}
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setSimpleValue(anno);
				
		
		smplcmbxSede= new SimpleComboBox<String>();
		smplcmbxSede.setFieldLabel("Sede");
		smplcmbxSede.setName("sede");
		smplcmbxSede.setEmptyText("Sede..");
		smplcmbxSede.setAllowBlank(false);
		 for(String l : DatiComboBox.getSedeOperativa()){
			 smplcmbxSede.add(l);}
		smplcmbxSede.setTriggerAction(TriggerAction.ALL);
		smplcmbxSede.setSimpleValue(anno);
				
		tlbrPanel.add(smplcmbxSede);
		tlbrPanel.add(smplcmbxAnno);
		tlbrPanel.add(btnAggiorna);
		
		cpGridSaturazione1.setTopComponent(tlbrPanel);
		cpGridSaturazione1.add(gridSaturazioneRisorsa1);
		cpGridSaturazione2.add(gridSaturazioneRisorsa2);
	  	    
		hp.add(cpGridSaturazione1);
		hp.add(cpGridSaturazione2);
		
		layoutContainer.add(hp, new FitData(0, 3, 3, 0));
					
		add(layoutContainer);
	
	}
	
	
	private void caricaDatiTabellaSaturazioneRisorsa(String sede, String anno) {
		try {
			AdministrationService.Util.getInstance().getDatiSaturazioneRisorsa(sede, anno, new AsyncCallback<List<SaturazioneRisorsaModel>>() {
					@Override
					public void onSuccess(List<SaturazioneRisorsaModel> result) {
						loadTableSaturazione1(result);
						loadTableSaturazione2(result);
					}
			
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione;");
						caught.printStackTrace();
					}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("Problemi durante il caricamento dei dati sulla saturazione della risorsa.");
		}
	}

	
	private void loadTableSaturazione1(List<SaturazioneRisorsaModel> result) {
		storeSaturazioneRisorsa.setSortField("tipoRecord");
		storeSaturazioneRisorsa.setSortDir(SortDir.ASC);
		storeSaturazioneRisorsa.removeAll();
		storeSaturazioneRisorsa.add(result);
	}
	
	private List<ColumnConfig> createColumnsCostingRisorse1() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		GridCellRenderer<SaturazioneRisorsaModel> renderer = new GridCellRenderer<SaturazioneRisorsaModel>() {
            public String render(SaturazioneRisorsaModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<SaturazioneRisorsaModel> store, Grid<SaturazioneRisorsaModel> grid) {
            	final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
            	          	
            	Float n=model.get(property);
            	String descr=(String)model.get("descrizioneRecord");
            	
            	if(((Float)model.get(property)!=0) && (descr.compareTo("Disponibili")!=0))
            		config.style = config.style + ";background-color:#d2f5af;" +"font-weight:bold;";
            	else
            		config.style = config.style + ";background-color:#FFFFFF;" +"font-weight:normal;";
            	
            	if(((Float)model.get(property)>100) && (descr.compareTo("Saturazione (%)")==0))
            		config.style = config.style + ";background-color:#f5afaf;" +"font-weight:bold;";
            	else
            		if(((Float)model.get(property)==0) && (descr.compareTo("Saturazione (%)")==0))
            			config.style = config.style + ";background-color:#FFFFFF;" +"font-weight:normal;";
            		else
            			if(((Float)model.get(property)>0) && ((Float)model.get(property)<100) && (descr.compareTo("Saturazione (%)")==0))
            				config.style = config.style + ";background-color:#d2f5af;" +"font-weight:bold;";
            	
            	if(descr.compareTo("Disponibili")==0)
            		config.style = config.style + ";background-color:#cccccc;";
            	
				if(n!=null)										
					return num.format(n);
				else
					return num.format(0);
        }};
        
        GridCellRenderer<SaturazioneRisorsaModel> renderer1 = new GridCellRenderer<SaturazioneRisorsaModel>() {
            public String render(SaturazioneRisorsaModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<SaturazioneRisorsaModel> store, Grid<SaturazioneRisorsaModel> grid) {
            	final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
            	        	
            	
            	String descr=(String)model.get("descrizioneRecord");
                       
            	if(descr.compareTo("Disponibili")==0)
            		config.style = config.style + ";background-color:#cccccc;";
            	else
            		config.style = config.style + ";background-color:white;";
            	
				return model.get(property);
        }};
        
		
		ColumnConfig column = new ColumnConfig();
		
		column = new ColumnConfig();
		column.setId("risorsa");
		column.setHeader("Risorsa");
		column.setWidth(110);
		column.setRowHeader(true);
		column.setRenderer(renderer1);
		column.setAlignment(HorizontalAlignment.LEFT);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("descrizioneRecord");
		column.setHeader("Descr.");
		column.setWidth(90);
		column.setRowHeader(true);
		column.setRenderer(renderer1);
		column.setAlignment(HorizontalAlignment.LEFT);
		configs.add(column);
		
		/*column = new ColumnConfig();
		column.setId("tipoRecord");
		column.setHeader("Descr.");
		column.setWidth(90);
		column.setRowHeader(true);
		column.setAlignment(HorizontalAlignment.LEFT);
		configs.add(column);*/
		
		for(int i=1;i<=25;i++){
			column = new ColumnConfig();  
			column.setId("w"+String.valueOf(i));  
			column.setHeader("w"+String.valueOf(i));  
			column.setWidth(43);  
			column.setRowHeader(true);
			column.setAlignment(HorizontalAlignment.RIGHT);
			column.setRenderer(renderer);
			configs.add(column);
		}
	    return configs;
	}
	
	private void loadTableSaturazione2(List<SaturazioneRisorsaModel> result) {
		storeSaturazioneRisorsa.setSortField("tipoRecord");
		storeSaturazioneRisorsa.setSortDir(SortDir.ASC);
		storeSaturazioneRisorsa.removeAll();
		storeSaturazioneRisorsa.add(result);
	}
	
	private List<ColumnConfig> createColumnsCostingRisorse2() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		GridCellRenderer<SaturazioneRisorsaModel> renderer = new GridCellRenderer<SaturazioneRisorsaModel>() {
            public String render(SaturazioneRisorsaModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<SaturazioneRisorsaModel> store, Grid<SaturazioneRisorsaModel> grid) {
            	final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
	          	
            	Float n=model.get(property);
            	String descr=(String)model.get("descrizioneRecord");
            	
            	if(((Float)model.get(property)!=0) && (descr.compareTo("Disponibili")!=0))
            		config.style = config.style + ";background-color:#d2f5af;" +"font-weight:bold;";
            	else
            		config.style = config.style + ";background-color:#FFFFFF;" +"font-weight:normal;";
            	
            	if(((Float)model.get(property)>100) && (descr.compareTo("Saturazione (%)")==0))
            		config.style = config.style + ";background-color:#f5afaf;" +"font-weight:bold;";
            	else
            		if(((Float)model.get(property)==0) && (descr.compareTo("Saturazione (%)")==0))
            			config.style = config.style + ";background-color:#FFFFFF;" +"font-weight:normal;";
            		else
            			if(((Float)model.get(property)>0) && ((Float)model.get(property)<100) && (descr.compareTo("Saturazione (%)")==0))
            				config.style = config.style + ";background-color:#d2f5af;" +"font-weight:bold;";
            	
            	if(descr.compareTo("Disponibili")==0)
            		config.style = config.style + ";background-color:#cccccc;";
            	            	
				if(n!=null)										
					return num.format(n);
				else
					return num.format(0);
        }};    
        
        
        GridCellRenderer<SaturazioneRisorsaModel> renderer1 = new GridCellRenderer<SaturazioneRisorsaModel>() {
            public String render(SaturazioneRisorsaModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<SaturazioneRisorsaModel> store, Grid<SaturazioneRisorsaModel> grid) {
            	          	
            	String descr=(String)model.get("descrizioneRecord");
                       
            	if(descr.compareTo("Disponibili")==0)
            		config.style = config.style + ";background-color:#cccccc;";
            	else
            		config.style = config.style + ";background-color:white;";
            	return model.get(property);
        }};
        
		ColumnConfig column = new ColumnConfig();
		column = new ColumnConfig();
		column.setId("risorsa");
		column.setHeader("Risorsa");
		column.setWidth(110);
		column.setRowHeader(true);
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setRenderer(renderer1);
		configs.add(column);
				
		for(int i=26;i<=53;i++){
			column = new ColumnConfig();  
			column.setId("w"+String.valueOf(i));  
			column.setHeader("w"+String.valueOf(i));  
			column.setWidth(40);  
			column.setRowHeader(true);
			column.setAlignment(HorizontalAlignment.RIGHT);
			column.setRenderer(renderer);
			configs.add(column);
		}
	    return configs;
	}
}
