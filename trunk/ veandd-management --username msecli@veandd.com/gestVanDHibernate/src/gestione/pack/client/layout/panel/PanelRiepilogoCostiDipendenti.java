package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.RiepilogoCostiDipendentiModel;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/*AL MOMENTO I COSTI VENGONO CARICATI STATICAMENTE DA .CVS*/


public class PanelRiepilogoCostiDipendenti extends LayoutContainer{
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private ListStore<RiepilogoCostiDipendentiModel> store=new ListStore<RiepilogoCostiDipendentiModel>();
	private ColumnModel cm;
	
	private EditorGrid<RiepilogoCostiDipendentiModel> gridRiepilogo;
		
	private Button btnPrint;
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	public PanelRiepilogoCostiDipendenti(){
		
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		VerticalPanel vp= new VerticalPanel();
		vp.setSpacing(3);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(true);
		cpGrid.setHeading("Lista Dipendenti.");
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setHeight((h-65));
		cpGrid.setWidth(w-250);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setButtonAlign(HorizontalAlignment.CENTER);  
		Resizable r=new Resizable(cpGrid);
		
		btnPrint= new Button();
		btnPrint.setEnabled(true);
		btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
		btnPrint.setIconAlign(IconAlign.TOP);
		btnPrint.setToolTip("Stampa");
		btnPrint.setSize(26, 26);
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				SessionManagementService.Util.getInstance().setDatiReportCostiDip("RIEP.COSTI", store.getModels(),
						new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error on setNomeReport()");					
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result)
							fp.submit();
						else
							Window.alert("Problemi durante il settaggio dei parametri in Sessione (http)");
					}
				});	
			}		
		});
	  	  	
		caricaTabellaDati();
				
		cm = new ColumnModel(createColumns());		
		gridRiepilogo= new EditorGrid<RiepilogoCostiDipendentiModel>(store, cm);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setColumnLines(true);
		gridRiepilogo.setStripeRows(true);
		gridRiepilogo.setItemId("grid");
			     
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setAction(url);
		fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
		fp.add(btnPrint);
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(fp);
		
		ToolBar tlBar= new ToolBar();
		//tlBar.add(cp);
				
		cpGrid.setTopComponent(tlBar);
		cpGrid.add(gridRiepilogo); 
	      
	    layoutContainer.add(cpGrid, new FitData(3, 3, 3, 3));
				
		add(layoutContainer);
	}
	

	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {
		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			
			//Window.open("/FileStorage/RiepilogoAnnuale.pdf", "_blank", "1");
			
		}
	}
	
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
				
		ColumnConfig column = new ColumnConfig();
		    
	    column = new ColumnConfig();  
	    column.setId("nome");  
	    column.setHeader("Nominativo");  
	    column.setWidth(140);  
	    configs.add(column);  
	    
	    column = new ColumnConfig();  
	    column.setId("tipoOrario");  
	    column.setHeader("Orario Giornaliero");  
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("orePreviste");  
	    column.setHeader("Ore Previste");  
	    column.setWidth(100);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoOrario");  
	    column.setHeader("Costo Orario");  
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoStruttura");  
	    column.setHeader("Costo Struttura");  
	    column.setWidth(140);  
	    configs.add(column);
	        
	    column = new ColumnConfig();  
	    column.setId("costoOneri");  
	    column.setHeader("Costo Orario");  
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoSwCadVari");  
	    column.setHeader("Costo SW CAD/Office");  
	    column.setWidth(140);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoSwOffice");  
	    column.setHeader("Costo SW Vari");  
	    column.setWidth(140);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoHw");  
	    column.setHeader("Costo HW");  
	    column.setWidth(140);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoOrarioTotale");  
	    column.setHeader("CostoTot/h");  
	    column.setWidth(140);  
	    configs.add(column);
	    	    
	    column = new ColumnConfig();  
	    column.setId("gruppoLavoro");  
	    column.setHeader("Area");  
	    column.setWidth(80);  
	    configs.add(column);
	    	        	    
	    return configs;
	}

	
	private void caricaTabellaDati() {
			
		try {
			AdministrationService.Util.getInstance().getRiepilogoDatiCostiPersonale("sede", new AsyncCallback<List<RiepilogoCostiDipendentiModel>>() {
					@Override
					public void onSuccess(List<RiepilogoCostiDipendentiModel> result) {
						if(result==null)
							Window.alert("Impossibile accedere ai dati sui costi dipendenti!");
						else
							loadTable(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on getRiepilogoDatiCostiPersonale();");
						caught.printStackTrace();
					}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("Problemi durante il caricamento dei dati sui costi personale.");
		}	
	}
	
	
	private void loadTable(List<RiepilogoCostiDipendentiModel> result) {
				
		store.removeAll();
		store.setStoreSorter(new StoreSorter<RiepilogoCostiDipendentiModel>());  
	    store.setDefaultSort("nome", SortDir.ASC);
		store.add(result);
	}	
	
}
