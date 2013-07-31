package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.model.RiepilogoMeseGiornalieroModel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

public class PanelRiepilogoMeseGiornalieroHorizontal extends LayoutContainer{

	private GroupingStore<RiepilogoMeseGiornalieroModel>store = new GroupingStore<RiepilogoMeseGiornalieroModel>();
	private Grid<RiepilogoMeseGiornalieroModel> gridRiepilogo;
	private ColumnModel cmRiepilogo;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	public PanelRiepilogoMeseGiornalieroHorizontal(){
		
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
	
		final FitLayout fl= new FitLayout();
		
		setLayout(fl);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);	
		
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setSize(w-220, h-70);
		cpGrid.setScrollMode(Scroll.AUTO);
		//cpGrid.setLayout(new FitLayout());
	
		Resizable r=new Resizable(cpGrid);
		
		try {	    	
			cmRiepilogo = new ColumnModel(createColumns()); 
	    	//caricaTabella();
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
				
		gridRiepilogo= new Grid<RiepilogoMeseGiornalieroModel>(store, cmRiepilogo);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);
		gridRiepilogo.getView().setShowDirtyCells(false); 
		
		cpGrid.add(gridRiepilogo);
		
		bodyContainer.add(cpGrid);
		add(bodyContainer,new FitData(3, 3, 3, 3));
	}
	

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column=new ColumnConfig();
		column.setId("dipendente");  
		column.setHeader("Dipendente");  
		column.setWidth(150);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column); 
		
		column=new ColumnConfig();
		column.setId("giorno1");  
		column.setHeader("1");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno2");  
		column.setHeader("2");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno3");  
		column.setHeader("3");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno4");  
		column.setHeader("4");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno5");  
		column.setHeader("5");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno6");  
		column.setHeader("6");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno7");  
		column.setHeader("7");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno8");  
		column.setHeader("8");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno9");  
		column.setHeader("9");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno10");  
		column.setHeader("10");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno11");  
		column.setHeader("11");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno12");  
		column.setHeader("12");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno13");  
		column.setHeader("13");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno14");  
		column.setHeader("14");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno15");  
		column.setHeader("15");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno16");  
		column.setHeader("16");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno17");  
		column.setHeader("17");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno18");  
		column.setHeader("18");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno19");  
		column.setHeader("19");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno20");  
		column.setHeader("20");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno21");  
		column.setHeader("21");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno22");  
		column.setHeader("22");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno23");  
		column.setHeader("23");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno24");  
		column.setHeader("24");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno25");  
		column.setHeader("25");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno26");  
		column.setHeader("26");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno27");  
		column.setHeader("27");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno28");  
		column.setHeader("28");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno29");  
		column.setHeader("29");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno30");  
		column.setHeader("30");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno31");  
		column.setHeader("31");  
		column.setWidth(30);  
		column.setRowHeader(true);  
		configs.add(column);
		
		return configs;
	}

	private void caricaTabella() {
		// TODO Auto-generated method stub
		
	}
	
}
