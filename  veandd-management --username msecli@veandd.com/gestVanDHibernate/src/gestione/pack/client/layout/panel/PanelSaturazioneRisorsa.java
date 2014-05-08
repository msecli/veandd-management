package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.CostingModel;
import gestione.pack.client.model.SaturazioneRisorsaModel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PanelSaturazioneRisorsa extends LayoutContainer{

	private GroupingStore<SaturazioneRisorsaModel> storeSaturazioneRisorsa=new GroupingStore<SaturazioneRisorsaModel>();
	private ColumnModel cmSaturazioneRisorsa;
	private EditorGrid<SaturazioneRisorsaModel> gridSaturazioneRisorsa;
	private String numeroCommessa= new String();
	private int idRisorsa=0;
	
	public PanelSaturazioneRisorsa(int idRisorsa, String numeroCommessa){
		this.numeroCommessa=numeroCommessa;
		this.idRisorsa=idRisorsa;			
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		caricaDatiTabellaSaturazioneRisorsa();
		
		ContentPanel cpGridSaturazione= new ContentPanel();
		cpGridSaturazione.setHeaderVisible(true);
		cpGridSaturazione.setHeading("Dati sulla saturazione della risorsa selezionata");
		cpGridSaturazione.setBorders(false);
		cpGridSaturazione.setFrame(true);
		cpGridSaturazione.setHeight(450);
		cpGridSaturazione.setWidth(1050);
		cpGridSaturazione.setScrollMode(Scroll.AUTO);
		cpGridSaturazione.setLayout(new FitLayout());
		cpGridSaturazione.setButtonAlign(HorizontalAlignment.CENTER);  
		
		storeSaturazioneRisorsa.groupBy("risorsa");
		cmSaturazioneRisorsa=new ColumnModel(createColumnsCostingRisorse());
		gridSaturazioneRisorsa= new EditorGrid<SaturazioneRisorsaModel>(storeSaturazioneRisorsa, cmSaturazioneRisorsa);
		gridSaturazioneRisorsa.setBorders(false);
		gridSaturazioneRisorsa.setItemId("grid");
		gridSaturazioneRisorsa.setStripeRows(true); 
		gridSaturazioneRisorsa.setColumnLines(true);
		gridSaturazioneRisorsa.setWidth(2600);
		
		cpGridSaturazione.add(gridSaturazioneRisorsa);
	  	    
		layoutContainer.add(cpGridSaturazione, new FitData(0, 3, 3, 0));
					
		add(layoutContainer);
	
	}
	
	

	private void caricaDatiTabellaSaturazioneRisorsa() {
		try {
			AdministrationService.Util.getInstance().getDatiSaturazioneRisorsa(idRisorsa, new AsyncCallback<List<SaturazioneRisorsaModel>>() {
					@Override
					public void onSuccess(List<SaturazioneRisorsaModel> result) {
						loadTableSaturazione(result);
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

	
	private void loadTableSaturazione(List<SaturazioneRisorsaModel> result) {
		// TODO Auto-generated method stub
		
	}
	
	private List<ColumnConfig> createColumnsCostingRisorse() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		ColumnConfig column = new ColumnConfig();
		
		column = new ColumnConfig();
		column.setId("risorsa");
		column.setHeader("Risorsa");
		column.setWidth(90);
		column.setRowHeader(true);
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
		
		for(int i=0;i<30;i++){
			column = new ColumnConfig();  
			column.setId("w");  
			column.setHeader("w");  
			column.setWidth(35);  
			column.setRowHeader(true);
			column.setAlignment(HorizontalAlignment.RIGHT);
			configs.add(column);
		}
	    return configs;
	}
}
