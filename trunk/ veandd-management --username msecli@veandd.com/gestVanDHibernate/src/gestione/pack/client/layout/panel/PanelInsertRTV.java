package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.CostingModel;
import gestione.pack.client.model.CostingRisorsaModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelInsertRTV extends LayoutContainer{
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private ListStore<CostingModel> store=new ListStore<CostingModel>();
	private ColumnModel cm;
	private EditorGrid<CostingRisorsaModel> gridRtv;
	
	private SimpleComboBox<String> smplcmbxPersonalManager= new SimpleComboBox<String>();
	private SimpleComboBox<String> smplcmbxMese=new SimpleComboBox<String>();
	private SimpleComboBox<String> smplcmbxAnno=new SimpleComboBox<String>();
	private Button btnAggiorna=new Button();
	
	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
	
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(true);
		cpGrid.setHeading("Inserimento Dati RTV");
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setSize(w-230, h-65);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setButtonAlign(HorizontalAlignment.CENTER);  
		
		Resizable r= new Resizable(cpGrid);
		
		Date d= new Date();
		String dt= d.toString();
		String anno= dt.substring(dt.length()-4, dt.length());
		String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(dt.substring(4, 7)));
	
		smplcmbxAnno= new SimpleComboBox<String>();
		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setEmptyText("Anno...");
		smplcmbxAnno.setAllowBlank(false);
		smplcmbxAnno.setEnabled(true);
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setWidth(100);
		for(String a:DatiComboBox.getAnno())
			smplcmbxAnno.add(a);
		smplcmbxAnno.setSimpleValue(anno);
				
		smplcmbxMese= new SimpleComboBox<String>();
		smplcmbxMese.setFieldLabel("Mese");
		smplcmbxMese.setName("mese");
		smplcmbxMese.setEmptyText("Mese...");
		smplcmbxMese.setAllowBlank(false);
		smplcmbxMese.setEnabled(true);
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		smplcmbxMese.setWidth(100);
		for(String m:DatiComboBox.getMese())
			smplcmbxMese.add(m);
		smplcmbxMese.setSimpleValue(mese);
		
		smplcmbxPersonalManager = new SimpleComboBox<String>();
		smplcmbxPersonalManager.setFieldLabel("Project Manager");
		smplcmbxPersonalManager.setName("pm");
		smplcmbxPersonalManager.setAllowBlank(true);
		smplcmbxPersonalManager.setTriggerAction(TriggerAction.ALL);
		smplcmbxPersonalManager.setEmptyText("Project Manager..");
		smplcmbxPersonalManager.setAllowBlank(false);
		getNomePM();
		
		
		btnAggiorna.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.refresh()));
		btnAggiorna.setIconAlign(IconAlign.TOP);
		btnAggiorna.setToolTip("Carica Ordini");
		btnAggiorna.setSize(26, 26);
	  	btnAggiorna.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				
			}
		});
		
		ToolBar tlbr= new ToolBar();
				
		tlbr.add(smplcmbxPersonalManager);
		tlbr.add(smplcmbxAnno);
		tlbr.add(smplcmbxMese);
		tlbr.add(new SeparatorToolItem());
		tlbr.add(btnAggiorna);	
		
		cpGrid.setTopComponent(tlbr);
		
		cm=new ColumnModel(createColumns());
	    gridRtv= new EditorGrid<CostingRisorsaModel>(store, cm);
	    gridRtv.setBorders(false);
	    gridRtv.setItemId("grid");
	    gridRtv.setStripeRows(true); 
	    gridRtv.setColumnLines(true);
	    gridRtv.setWidth(2600);
	    
	    cpGrid.add(gridRtv);
		
		layoutContainer.add(cpGrid, new FitData(3, 3, 3, 3));			
		add(layoutContainer);
	
	}

	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
		CellEditor editorTxt;
		
		GridCellRenderer<CostingRisorsaModel> renderer = new GridCellRenderer<CostingRisorsaModel>() {
            public String render(CostingRisorsaModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<CostingRisorsaModel> store, Grid<CostingRisorsaModel> grid) {
            	final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
            	Float n=model.get(property);
				if(n!=null)            	
					return num.format(n);
				else
					return num.format(0);
        }};
        
        ColumnConfig column= new ColumnConfig();
        column.setId("numeroOrdine");
	    column.setHeader("Ordine");
	    column.setWidth(125);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    
	    column= new ColumnConfig();
        column.setId("numeroRtv");
	    column.setHeader("RTV");
	    column.setWidth(125);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
        
	    column= new ColumnConfig();
        column.setId("importo");
	    column.setHeader("Importo RTV");
	    column.setWidth(125);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
        
        return configs;
	}
	

	private void getNomePM() {
		AdministrationService.Util.getInstance().getNomePM(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getNomePM();");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<String> result) {
				if(result!=null){
					smplcmbxPersonalManager.add(result);
					smplcmbxPersonalManager.recalculate();
							
				}else 
					Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		});				
	}

}
