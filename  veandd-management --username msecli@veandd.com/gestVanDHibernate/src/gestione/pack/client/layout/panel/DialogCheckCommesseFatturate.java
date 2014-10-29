package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DialogCheckCommesseFatturate  extends Dialog{
	
	private ListStore<RiepilogoOreDipFatturazione>store = new ListStore<RiepilogoOreDipFatturazione>();
	private Grid<RiepilogoOreDipFatturazione> gridRiepilogo;
	private ColumnModel cm;	
	
	public DialogCheckCommesseFatturate(String pm, String anno, String meseRif, List<RiepilogoOreDipFatturazione> listaCommesse){
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setResizable(true);
		setClosable(true);
		setConstrain(false);
		setButtons("");
		setHeading("Controllo ore");
		setModal(false);
				
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);
		
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setHeight(820);
		cntpnlGrid.setWidth(520);
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
		
		caricaDatiTabella(meseRif+anno, listaCommesse);
		
		cntpnlGrid.add(gridRiepilogo);
		
		bodyContainer.add(cntpnlGrid);
		
		add(bodyContainer);		
	}

	private void caricaDatiTabella(String meseRif, List<RiepilogoOreDipFatturazione> listaC) {
		
		//Per ogni commessa, nel mese di riferimento cerco gli eventuali fogli fatturazione compilati e controllo la differenza ore con un check
		AdministrationService.Util.getInstance().checkOreEseguiteFogliopFatturazione(meseRif, listaC, new AsyncCallback<List<RiepilogoOreDipFatturazione>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<RiepilogoOreDipFatturazione> result) {
				loadTable(result);
			}
		});
	}

		
	private void loadTable(List<RiepilogoOreDipFatturazione> result) {
		store.add(result);
		gridRiepilogo.reconfigure(store, cm);
	}
	
		
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("numeroCommessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(140);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    //oreLavoro, oreViaggio campi model	
	    
	    column=new ColumnConfig();		
	    column.setId("oreLavoro");  
		column.setHeader("h/Lavorate");  
		column.setWidth(90);  
		column.setRowHeader(true); 
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("oreViaggio");  
		column.setHeader("h/Registrate");  
		column.setWidth(90);  
		column.setRowHeader(true); 
	    configs.add(column); 
		
	    column=new ColumnConfig();		
	    column.setId("checkOre");  
		column.setHeader("Check");  
		column.setWidth(90);  
		column.setRowHeader(true); 
		column.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
	
			@Override
			public Object render(RiepilogoOreDipFatturazione model,
					String property, ColumnData config, int rowIndex,
					int colIndex, ListStore<RiepilogoOreDipFatturazione> store,
					Grid<RiepilogoOreDipFatturazione> grid) {
								
					Boolean check=model.get("checkOre");
					if(check!=null)
					if(check)
						config.style = config.style + ";background-color:" + "#90EE90" + ";";									
					else
						config.style = config.style + ";background-color:" + "#F08080" + ";";
				
					return "";
			}
		});
	    configs.add(column); 
	    
		return configs;
	}

}
