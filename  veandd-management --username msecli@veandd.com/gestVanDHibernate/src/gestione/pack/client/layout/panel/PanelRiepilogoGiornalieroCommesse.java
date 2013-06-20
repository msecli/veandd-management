package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.RiepilogoOreDipCommesseGiornaliero;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.ConstantiMSG;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class PanelRiepilogoGiornalieroCommesse extends LayoutContainer{	
	
	private GroupingStore<RiepilogoOreDipCommesseGiornaliero>store = new GroupingStore<RiepilogoOreDipCommesseGiornaliero>();
	private Grid<RiepilogoOreDipCommesseGiornaliero> gridRiepilogo;
	private ColumnModel cmCommessa;
	private String username= new String();
	private String tLavoratore= new String();
	private Date data;
	private Button btnPrint= new Button();
	
	com.google.gwt.user.client.ui.Button btnPrint1 = new com.google.gwt.user.client.ui.Button("Stampa");
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	public PanelRiepilogoGiornalieroCommesse(String user, Date dataRiferimento){
		username=user;
		data=dataRiferimento;
		
	}
			
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    setItemId("pnlRiepilogo");
	    
	    final FitLayout fl= new FitLayout();
	    LayoutContainer layoutContainer= new LayoutContainer();
	    layoutContainer.setBorders(false);
	    layoutContainer.setLayout(fl);	
	  		
	    LayoutContainer bodyContainer = new LayoutContainer();
	    bodyContainer.setLayout(new FlowLayout());
	  	bodyContainer.setBorders(false);
	  	  	  	
	  	//Button GXT
	  	btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print()));
		btnPrint.setToolTip("Stampa");
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				
				
				String dataRif=data.toString();
				String mese=dataRif.substring(4, 7);
				
				String anno=dataRif.substring(dataRif.length()-4,dataRif.length());
			    
				mese=ClientUtility.traduciMeseToIt(mese);
				
			    dataRif=mese+anno;
			    
				SessionManagementService.Util.getInstance().setDataInSession(dataRif, username, "COMM", new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error on setDataInSession()");					
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
		
		fp.add(btnPrint);
		fp.setMethod(FormPanel.METHOD_POST);
	    fp.setAction(url);
	    fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
	   
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setBodyBorder(false);         
		cntpnlGrid.setLayout(new FitLayout());  
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setWidth(500);
		cntpnlGrid.setHeight(500);
		cntpnlGrid.setScrollMode(Scroll.AUTOY);
				
		caricaTabellaDati();
	    
	    try {	    
	    		cmCommessa = new ColumnModel(createColumns());
	    	} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	    
	    store.groupBy("numeroCommessa");
	    store.setSortField("giorno");
	    
	    GroupSummaryView summary = new GroupSummaryView();  
	    summary.setForceFit(true);  
	    summary.setShowGroupedColumn(false);
	    summary.setStartCollapsed(false);
		    
	    gridRiepilogo= new EditorGrid<RiepilogoOreDipCommesseGiornaliero>(store, cmCommessa);  
	    gridRiepilogo.setItemId("grid");
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setView(summary);  
	    gridRiepilogo.getView().setShowDirtyCells(false);
	    
	    cntpnlGrid.add(gridRiepilogo);
	    	    
	    ContentPanel cntpnlLayout= new ContentPanel();
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setCollapsible(false);
		cntpnlLayout.setExpanded(true);
		cntpnlLayout.setHeading("Riepilogo Giornaliero.");
		cntpnlLayout.setSize(515, 545);
		cntpnlLayout.setFrame(true);
		cntpnlLayout.add(fp);
		cntpnlLayout.add(cntpnlGrid);
	    
		bodyContainer.add(cntpnlLayout);    
		
		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);    
	}


	private void caricaTabellaDati() {
		AdministrationService.Util.getInstance().getRiepilogoGiornalieroCommesse(username, data,  new AsyncCallback<List<RiepilogoOreDipCommesseGiornaliero>>() {	
			@Override
			public void onSuccess(List<RiepilogoOreDipCommesseGiornaliero> result) {
				if(result==null)
					Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
				else	
					if(result.size()==0){
						//Window.alert("Nessun dato rilevato in base ai criteri di ricerca selezionati.");				
					}
					else loadTable(result);			
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getRiepilogoOreDipFatturazione();");
				caught.printStackTrace();		
			}
		}); //AsyncCallback	  		
	}
	
	
	private void loadTable(List<RiepilogoOreDipCommesseGiornaliero> result) {
		try {
			store.removeAll();
			store.add(result);
			gridRiepilogo.reconfigure(store, cmCommessa);				
		} catch (NullPointerException e) {
			Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
				e.printStackTrace();
		}
	}

	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number= NumberFormat.getFormat("0.00");
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("numeroCommessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(85);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("giorno");  
	    column.setHeader("Giorno");  
	    column.setWidth(85);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	   	    	    
	    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
	    columnOreLavoro.setId("oreLavoro");  
	    columnOreLavoro.setHeader("Ore Lavoro");  
	    columnOreLavoro.setWidth(60);    
	    columnOreLavoro.setRowHeader(true);  
	    columnOreLavoro.setAlignment(HorizontalAlignment.LEFT);  	
	    columnOreLavoro.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesseGiornaliero>() {
			@Override
			public Object render(RiepilogoOreDipCommesseGiornaliero model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreDipCommesseGiornaliero> store,
					Grid<RiepilogoOreDipCommesseGiornaliero> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	   
	    configs.add(columnOreLavoro); 	
	    
	    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
	    columnOreViaggio.setId("oreViaggio");  
	    columnOreViaggio.setHeader("Ore Viaggio");  
	    columnOreViaggio.setWidth(60);    
	    columnOreViaggio.setRowHeader(true);  
	    columnOreViaggio.setAlignment(HorizontalAlignment.LEFT);    
	    columnOreViaggio.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesseGiornaliero>() {
			@Override
			public Object render(RiepilogoOreDipCommesseGiornaliero model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreDipCommesseGiornaliero> store,
					Grid<RiepilogoOreDipCommesseGiornaliero> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	        
	    configs.add(columnOreViaggio); 	
	    
	    SummaryColumnConfig<Double> columnOreTotali=new SummaryColumnConfig<Double>();		
	    columnOreTotali.setId("totOre");  
	    columnOreTotali.setHeader("Totale");  
	    columnOreTotali.setWidth(60);    
	    columnOreTotali.setRowHeader(true);   
	    columnOreTotali.setAlignment(HorizontalAlignment.LEFT);   
	    columnOreTotali.setStyle("color:#e71d2b;");
	    columnOreTotali.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesseGiornaliero>() {
			@Override
			public Object render(RiepilogoOreDipCommesseGiornaliero model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreDipCommesseGiornaliero> store,
					Grid<RiepilogoOreDipCommesseGiornaliero> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});
	       
	    configs.add(columnOreTotali); 		
		return configs;
	}
	
	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {
		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			 	
			String nome, cognome, nomeFile;
			
			nome=username.substring(0, username.indexOf("."));
			nome=nome.substring(0, 1).toUpperCase()+nome.substring(1,nome.length());
			
			cognome=username.substring(username.indexOf(".")+1, username.length());
			cognome=cognome.substring(0, 1).toUpperCase()+cognome.substring(1,cognome.length());
			
			nomeFile=cognome+nome+"_Report.pdf";
			
			Window.open("/FileStorage/RiepiloghiCommesse/"+nomeFile, "_blank", "1");
			/*
			 if(event.getResults().isEmpty()){
				//Window.alert("Errore durante la creazione del file!");	 	
			 }
			else{		
				nome=username.substring(0, username.indexOf("."));
				nome=nome.substring(0, 1).toUpperCase()+nome.substring(1,nome.length());
				
				cognome=username.substring(username.indexOf(".")+1, username.length());
				cognome=cognome.substring(0, 1).toUpperCase()+cognome.substring(1,cognome.length());
				
				nomeFile=cognome+nome+"_Report.pdf";
				
				Window.open(ConstantiMSG.PATHAmazon+"FileStorage/RiepiloghiCommesse/"+nomeFile, "_blank", "1");
			}*/
		}
	}
}
