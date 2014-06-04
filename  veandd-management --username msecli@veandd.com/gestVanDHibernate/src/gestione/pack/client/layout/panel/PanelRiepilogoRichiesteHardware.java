package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.CostingRisorsaModel;
import gestione.pack.client.model.RiepilogoRichiesteModel;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelRiepilogoRichiesteHardware extends LayoutContainer{

	private ListStore<RiepilogoRichiesteModel>store = new ListStore<RiepilogoRichiesteModel>();
	private EditorGrid<RiepilogoRichiesteModel> gridRiepilogo;
	private ColumnModel cmCommessa;
	private CellSelectionModel<RiepilogoRichiesteModel> cm;
	private RowExpander expander;
	private  CheckBoxSelectionModel<RiepilogoRichiesteModel> sm = new CheckBoxSelectionModel<RiepilogoRichiesteModel>();  
	
	private Button btnAdd;
	private Button btnConferma;
	
	private int h=Window.getClientHeight();
		
	public PanelRiepilogoRichiesteHardware(){
		
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
	    	cmCommessa = new ColumnModel(createColumns());    	
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	    
	    caricaDatiTabella();
	    cm=new CellSelectionModel<RiepilogoRichiesteModel>();
	    cm.setSelectionMode(SelectionMode.SIMPLE);
	    gridRiepilogo= new EditorGrid<RiepilogoRichiesteModel>(store, cmCommessa);  
	    gridRiepilogo.setItemId("grid");
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setStripeRows(true);  
	    gridRiepilogo.setColumnLines(true);  
	    gridRiepilogo.setColumnReordering(true);  
	    gridRiepilogo.getView().setShowDirtyCells(false);
	    gridRiepilogo.addPlugin(expander);
	    gridRiepilogo.addPlugin(sm);
	    gridRiepilogo.setSelectionModel(sm);
	   	
	    btnAdd= new Button();
	    btnAdd.setStyleAttribute("padding-left", "2px");
	    btnAdd.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.add()));
	  	btnAdd.setIconAlign(IconAlign.TOP);
	  	btnAdd.setSize(26, 26);
	  	btnAdd.setToolTip("Nuovo Hardware");
	    
	    btnConferma= new Button();
	    btnConferma.setStyleAttribute("padding-left", "2px");
	    btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	    btnConferma.setIconAlign(IconAlign.TOP);
	    btnConferma.setSize(26, 26);
	    btnConferma.setToolTip("Conferma dati inseriti");
	    btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    ToolBar tlbrButton= new ToolBar();
	    //tlbrButton.add(btnAdd);
	    tlbrButton.add(btnConferma);
	    
	    cntpnlGrid.add(gridRiepilogo);
	    cntpnlGrid.setTopComponent(tlbrButton);
	    
	    //bodyContainer.add(cntpnlGrid);
	    layoutContainer.add(cntpnlGrid, new FitData(3, 3, 3, 3));
	    add(layoutContainer);	    
	}

	
	private void caricaDatiTabella() {
		AdministrationService.Util.getInstance().getRiepilogoRichiesteItUtente("ALL", new AsyncCallback<List<RiepilogoRichiesteModel>>() {
			
			@Override
			public void onSuccess(List<RiepilogoRichiesteModel> result) {
				loadData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getRiepilogoRichiesteItUtente();");
			}
		});
	}

	private void loadData(List<RiepilogoRichiesteModel> result) {
		store.removeAll();
		store.add(result);
		gridRiepilogo.reconfigure(store, cmCommessa);
	}
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		XTemplate tpl = XTemplate.create("<p><b>Username:</b> {username}</p><br>" +
				"<p><b>Problematica:</b> {guasto}</p>");  
		    
		expander = new RowExpander();
		expander.setTemplate(tpl); 
		expander.setWidth(20);
				
		configs.add(expander);		
		sm.setSelectionMode(SelectionMode.SIMPLE);
		configs.add(sm.getColumn());
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("idRichiesta");  
	    column.setHeader("ID");  
	    column.setWidth(30);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("dataRichiesta");  
	    column.setHeader("Data Richiesta");  
	    column.setWidth(150);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("dataEvasioneRichiesta");  
	    column.setHeader("Data Evasione");  
	    column.setWidth(150);  
	    column.setRowHeader(true);
	    DateField dtfldInizio= new DateField();
	    column.setEditor(new CellEditor(dtfldInizio));  
	    column.setDateTimeFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("stato");  
	    column.setHeader("Stato");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("guasto");  
	    column.setHeader("Problematica");  
	    column.setWidth(130);  
	    column.setRowHeader(true);  
	    configs.add(column);
		
		return configs;
	}
	
}
