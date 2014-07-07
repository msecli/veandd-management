package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.model.RiepilogoOreDipFatturazione;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

public class DialogCheckCommesseFatturate  extends Dialog{
	
	private ListStore<RiepilogoOreDipFatturazione>store = new ListStore<RiepilogoOreDipFatturazione>();
	private Grid<RiepilogoOreDipFatturazione> gridRiepilogo;
	private ColumnModel cm;	
	
	public DialogCheckCommesseFatturate(String pm, String anno, String meseRif){
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(900);
		setHeight(550);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setHeading("Controllo ore");
		setModal(true);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);
		
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setHeight(490);
		cntpnlGrid.setWidth(850);
		cntpnlGrid.setFrame(true);
		cntpnlGrid.setBorders(false);
		cntpnlGrid.setLayout(new FitLayout());
		
		cm=new ColumnModel(createColumns());
		gridRiepilogo= new Grid<RiepilogoOreDipFatturazione>(store, cm);  
		gridRiepilogo.setItemId("grid");
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);
		gridRiepilogo.getView().setShowDirtyCells(false);
		
		cntpnlGrid.add(gridRiepilogo);
		
		bodyContainer.add(cntpnlGrid);
		
		add(bodyContainer);		
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		return configs;
	}

}
