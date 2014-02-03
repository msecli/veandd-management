package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.model.AnagraficaHardwareModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelAnagraficaHardware extends LayoutContainer{
	
	private ListStore<AnagraficaHardwareModel>store = new ListStore<AnagraficaHardwareModel>();
	private Grid<AnagraficaHardwareModel> gridRiepilogo;
	private ColumnModel cmCommessa;
	
	private Button btnAdd;
	private Button btnConferma;
	
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
	    	cmCommessa = new ColumnModel(createColumns());    	
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	    
	    gridRiepilogo= new Grid<AnagraficaHardwareModel>(store, cmCommessa);  
	    gridRiepilogo.setItemId("grid");
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setStripeRows(true);  
	    gridRiepilogo.setColumnLines(true);  
	    gridRiepilogo.setColumnReordering(true);  
	    gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    gridRiepilogo.getView().setShowDirtyCells(false);
	   
	
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
	    
	    ToolBar tlbrButton= new ToolBar();
	    tlbrButton.add(btnAdd);
	    tlbrButton.add(btnConferma);
	    
	    cntpnlGrid.add(gridRiepilogo);
	    cntpnlGrid.setTopComponent(tlbrButton);
	    
	    //bodyContainer.add(cntpnlGrid);
	    layoutContainer.add(cntpnlGrid, new FitData(3, 3, 3, 3));
	    add(layoutContainer);
	    
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("sede");  
	    column.setHeader("Sede");  
	    column.setWidth(30);  
	    column.setRowHeader(true);  
	    configs.add(column); 
		
		return configs;
	}

}
