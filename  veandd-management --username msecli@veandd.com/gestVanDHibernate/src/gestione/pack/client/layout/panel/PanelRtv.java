package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gestione.pack.client.model.RiepilogoMensileOrdiniModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
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
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelRtv extends ContentPanel{

	//uso il grouping per fare la somma del fatturato
	private GroupingStore<RiepilogoMensileOrdiniModel> storeRtv=new GroupingStore<RiepilogoMensileOrdiniModel>();
	private ColumnModel cmRiepRtv;
	private EditorGrid<RiepilogoMensileOrdiniModel> gridRiepRtv;
	
	private Button btnAddRtv;
	private int w=Window.getClientWidth();
	private int h=Window.getClientHeight();
	
	private String numeroO= new  String();
	
	
	public PanelRtv(){
				
		setHeaderVisible(true);
		setHeading("RTV");
		setBorders(false);
		setFrame(true);
		setScrollMode(Scroll.AUTO);
		setLayout(new FitLayout());
		setWidth((w-155)/2-50);
		setHeight((h-55)/2-140);
		
		GroupSummaryView summary = new GroupSummaryView();  
		summary.setForceFit(false);  
		summary.setShowGroupedColumn(false);
		
		storeRtv.groupBy("numeroOrdine");
		cmRiepRtv=new ColumnModel(createColumnsRTV());
		gridRiepRtv= new EditorGrid<RiepilogoMensileOrdiniModel>(storeRtv, cmRiepRtv);
		gridRiepRtv.setBorders(false);
		gridRiepRtv.setItemId("grid");
		gridRiepRtv.setStripeRows(true); 
		gridRiepRtv.setColumnLines(true);
		gridRiepRtv.setView(summary);	
		
		ToolBar tlbrRtv= new ToolBar();
		
		btnAddRtv= new Button();
		btnAddRtv.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.add()));
		btnAddRtv.setToolTip("Aggiungi RTV");
		btnAddRtv.setIconAlign(IconAlign.TOP);
		btnAddRtv.setSize(26, 26);
		btnAddRtv.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {	
				DialogFormInserimentoDatiRtv dp= new DialogFormInserimentoDatiRtv(numeroO);
				dp.setSize(630, 240);
				dp.setButtons("");
				dp.setHeading("Dati per l'RTV.");
				dp.setConstrain(false);
				dp.show();
				
				dp.addListener(Events.Hide, new Listener<ComponentEvent>() {
				     
					@Override
					public void handleEvent(ComponentEvent be) {
						
						
				    }
				});			
				
			}				
		});	
		tlbrRtv.add(btnAddRtv);
					
		setTopComponent(tlbrRtv);
		add(gridRiepRtv);
	}

	private List<ColumnConfig> createColumnsRTV() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
		SummaryColumnConfig<Double> column = new SummaryColumnConfig<Double>();  
		GridCellRenderer<RiepilogoMensileOrdiniModel> renderer = new GridCellRenderer<RiepilogoMensileOrdiniModel>() {
            public String render(RiepilogoMensileOrdiniModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<RiepilogoMensileOrdiniModel> store, Grid<RiepilogoMensileOrdiniModel> grid) {
            	Float n=model.get(property);
            	return num.format(n);				
            }
		};
        
		column = new SummaryColumnConfig<Double>();  
	    column.setId("numeroOrdine");  
	    column.setHeader("Ordine");  
	    column.setWidth(140);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
		
	    column = new SummaryColumnConfig<Double>();  
	    column.setId("meseRiferimento");  
	    column.setHeader("Mese");  
	    column.setWidth(140);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
		
	    column = new SummaryColumnConfig<Double>();  
	    column.setId("importo");  
	    column.setHeader("Importo da Fatturare");  
	    column.setWidth(160);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setSummaryType(SummaryType.SUM);
	    column.setRenderer(renderer);
	    column.setSummaryRenderer(new SummaryRenderer() {
			
			@Override
			public String render(Number value, Map<String, Number> data) {
				//final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				return num.format(value);
			}
		});
	    configs.add(column);
	    	    
		return configs;		
	}
	
	public void setNumeroOrdine(String numeroOrdine){
		numeroO=numeroOrdine;
	}
	
	
		
}
