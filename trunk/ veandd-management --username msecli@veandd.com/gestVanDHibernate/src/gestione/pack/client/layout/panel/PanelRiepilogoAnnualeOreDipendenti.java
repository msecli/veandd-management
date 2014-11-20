package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.RiepilogoOreAnnualiDipendente;
import gestione.pack.client.model.RiepilogoSALPCLModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
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
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class PanelRiepilogoAnnualeOreDipendenti extends LayoutContainer{

	private GroupingStore<RiepilogoOreAnnualiDipendente>store = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	private Grid<RiepilogoOreAnnualiDipendente> gridRiepilogo;
	private ColumnModel cmRiepilogo;
	
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	private SimpleComboBox<String> smplcmbxAnnoInizio;
	private SimpleComboBox<String> smplcmbxMeseInizio;
	private SimpleComboBox<String> smplcmbxAnnoFine;
	private SimpleComboBox<String> smplcmbxMeseFine;
	
	private SimpleComboBox<String> smplcmbxAnno;
	private SimpleComboBox<String> smplcmbxSede;
	private Button btnSelect;
	private Button btnPrint;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	public PanelRiepilogoAnnualeOreDipendenti(){
		
	}
	
	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);	
		
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setSize(w-230, h-65);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
	
		Resizable r=new Resizable(cpGrid);
		
		ToolBar tlbrOperazioni= new ToolBar();
		cpGrid.setTopComponent(tlbrOperazioni);
		
		Date d= new Date();
		String dt= d.toString();
		String anno= dt.substring(dt.length()-4, dt.length());
		String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(dt.substring(4, 7)));
				
		/*smplcmbxAnno= new SimpleComboBox<String>();
		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setAllowBlank(false);
		smplcmbxAnno.setEnabled(true);
		for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);}
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setSimpleValue(anno);
		smplcmbxAnno.setWidth(100);*/
		//tlbrOperazioni.add(smplcmbxAnno);
		
		smplcmbxAnnoInizio= new SimpleComboBox<String>();
		smplcmbxAnnoInizio.setFieldLabel("Anno Inizio");
		smplcmbxAnnoInizio.setName("annoI");
		smplcmbxAnnoInizio.setEmptyText("Anno Inizio..");
		smplcmbxAnnoInizio.setAllowBlank(false);
		smplcmbxAnnoInizio.setEnabled(true);
		smplcmbxAnnoInizio.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnnoInizio.setSimpleValue(anno);
		smplcmbxAnnoInizio.setWidth(100);
		tlbrOperazioni.add(smplcmbxAnnoInizio);
		
		smplcmbxMeseInizio= new SimpleComboBox<String>();
		smplcmbxMeseInizio.setFieldLabel("Mese Inizio");
		smplcmbxMeseInizio.setName("meseI");
		smplcmbxMeseInizio.setEmptyText("Mese Inizio..");
		smplcmbxMeseInizio.setAllowBlank(false);
		smplcmbxMeseInizio.setEnabled(true);
		smplcmbxMeseInizio.setTriggerAction(TriggerAction.ALL);
		smplcmbxMeseInizio.setSimpleValue(mese);
		smplcmbxMeseInizio.setWidth(100);
		tlbrOperazioni.add(smplcmbxMeseInizio);
		
		smplcmbxAnnoFine= new SimpleComboBox<String>();
		smplcmbxAnnoFine.setFieldLabel("Anno Fine");
		smplcmbxAnnoFine.setName("annoF");
		smplcmbxAnnoFine.setEmptyText("Anno Fine..");
		smplcmbxAnnoFine.setAllowBlank(false);
		smplcmbxAnnoFine.setEnabled(true);
		smplcmbxAnnoFine.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnnoFine.setSimpleValue(anno);
		smplcmbxAnnoFine.setWidth(100);
		for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnnoInizio.add(l);
			 smplcmbxAnnoFine.add(l);
		}
		tlbrOperazioni.add(smplcmbxAnnoFine);
		
		smplcmbxMeseFine= new SimpleComboBox<String>();
		smplcmbxMeseFine.setFieldLabel("Mese Fine");
		smplcmbxMeseFine.setName("meseF");
		smplcmbxMeseFine.setEmptyText("Mese Fine..");
		smplcmbxMeseFine.setAllowBlank(false);
		smplcmbxMeseFine.setEnabled(true);
		smplcmbxMeseFine.setTriggerAction(TriggerAction.ALL);
		smplcmbxMeseFine.setSimpleValue(mese);
		smplcmbxMeseFine.setWidth(100);
		for(String l : DatiComboBox.getMese()){
			smplcmbxMeseInizio.add(l);
			smplcmbxMeseFine.add(l);			 
		}
		//smplcmbxMeseFine.setSimpleValue(mese);
		tlbrOperazioni.add(smplcmbxMeseFine);
		
		smplcmbxSede=new SimpleComboBox<String>();
		smplcmbxSede.setFieldLabel("Sede");
		smplcmbxSede.setName("sede");
		smplcmbxSede.setEmptyText("Sede..");
		smplcmbxSede.setAllowBlank(false);
		smplcmbxSede.setEnabled(true);
		smplcmbxSede.add("T");
		smplcmbxSede.add("B");
		smplcmbxSede.setTriggerAction(TriggerAction.ALL);
		smplcmbxSede.setSimpleValue("T");
		smplcmbxSede.setWidth(70);
		tlbrOperazioni.add(smplcmbxSede);
		
		btnSelect= new Button();
		btnSelect.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.reload()));
		btnSelect.setToolTip("Load");
		btnSelect.setIconAlign(IconAlign.TOP);
		btnSelect.setSize(26, 26);
		btnSelect.setEnabled(true);
		btnSelect.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				String sede=smplcmbxSede.getRawValue().toString();
				//String anno=smplcmbxAnno.getValue().toString();
				caricaTabella(sede);
			}
		});
		tlbrOperazioni.add(btnSelect);
		
		btnPrint = new Button();
		btnPrint.setSize("55px","25px");	   
		btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
		btnPrint.setToolTip("Stampa");
		btnPrint.setIconAlign(IconAlign.TOP);
		btnPrint.setSize(26, 26);
		btnPrint.setEnabled(true);
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {
			//TODO passare la lista
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<RiepilogoOreAnnualiDipendente> listaRiep= new ArrayList<RiepilogoOreAnnualiDipendente>();
				listaRiep=store.getModels();
				
				SessionManagementService.Util.getInstance().setDataReportAnnualeInSession(listaRiep, "RIEP.ANNUALE",
						new AsyncCallback<Boolean>() {

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
		    
	    fp.setMethod(FormPanel.METHOD_POST);
		fp.setAction(url);
		fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
		fp.add(btnPrint);
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(fp);
		tlbrOperazioni.add(cp);
		
		try {
			cmRiepilogo = new ColumnModel(createColumns()); 
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	  	
		GroupSummaryView summary = new GroupSummaryView();  
	    summary.setForceFit(true);  
	    summary.setShowGroupedColumn(false);
	    summary.setStartCollapsed(false);
												
		store.setDefaultSort("cognome", SortDir.ASC);
		store.groupBy("anno");
		 
		gridRiepilogo= new EditorGrid<RiepilogoOreAnnualiDipendente>(store, cmRiepilogo);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);
		gridRiepilogo.setView(summary);	
									
		cpGrid.add(gridRiepilogo);
					
		layoutContainer.add(cpGrid, new FitData(3,3,3,3));
		add(layoutContainer);
	
	}
	

	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {

		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			
			//Window.open("/FileStorage/RiepilogoAnnuale.pdf", "_blank", "1");
			
		}
	}
	
	
	private void caricaTabella(String sede) {		
				
		String annoI=smplcmbxAnnoInizio.getRawValue().toString();
		String meseI=smplcmbxMeseInizio.getRawValue().toString();
		String annoF=smplcmbxAnnoFine.getRawValue().toString();
		String meseF=smplcmbxMeseFine.getRawValue().toString();
		
		if(smplcmbxAnnoInizio.isValid()&&smplcmbxAnnoFine.isValid()&&smplcmbxMeseInizio.isValid()&&smplcmbxMeseFine.isValid())
		AdministrationService.Util.getInstance().getRiepilogoAnnualeOreDipendenti(annoI,meseI,annoF,meseF,sede, new AsyncCallback<List<RiepilogoOreAnnualiDipendente>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getRiepilogoAnnualeOreDipendenti()");
							
			}

			@Override
			public void onSuccess(List<RiepilogoOreAnnualiDipendente> result) {
				if(result==null)
					Window.alert("Problema all'accesso ai dati di RiepilogoAnnualeOreDipendenti()");
				else
					loadData(result);
			}
		});		
		else
			Window.alert("Controllare i campi inseriti!");
	}
		
	
	private void loadData(List<RiepilogoOreAnnualiDipendente> result) {
		store.removeAll();
		store.add(result);
		store.setStoreSorter(new StoreSorter<RiepilogoOreAnnualiDipendente>());  
	    store.setDefaultSort("cognome", SortDir.ASC);
		gridRiepilogo.reconfigure(store, cmRiepilogo);
		
	}
	
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number = NumberFormat.getFormat("0.00");
		
		GridCellRenderer<RiepilogoSALPCLModel> renderer = new GridCellRenderer<RiepilogoSALPCLModel>() {
            public String render(RiepilogoSALPCLModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<RiepilogoSALPCLModel> store, Grid<RiepilogoSALPCLModel> grid) {
				
            	config.style = config.style + ";background-color:#d2f5af;" +"font-weight:bold;" ;
            	//config.style = config.style + ";background-color:#FFFFFF;" +"font-weight:normal;";    	
				return model.get(property);				
            }
		};
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("cognome");  
	    column.setHeader("Cognome");  
	    column.setWidth(90);  
	    column.setRowHeader(true);  
	    column.setSummaryRenderer(new SummaryRenderer() {
			@Override
			public String render(Number value, Map<String, Number> data) {
				return "Totale Ore:";
	   			}     			
		});  
	    configs.add(column); 
	    
	    column= new SummaryColumnConfig<Double>();
	    column.setId("nome");  
	    column.setHeader("Nome");  
	    column.setWidth(90);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    SummaryColumnConfig<Double> columnOreOrdinarie=new SummaryColumnConfig<Double>();		
	    columnOreOrdinarie.setId("oreOrdinarie");  
	    columnOreOrdinarie.setHeader("Ore Ordinarie");  
	    columnOreOrdinarie.setWidth(100);    
	    columnOreOrdinarie.setRowHeader(true); 
	    columnOreOrdinarie.setSummaryType(SummaryType.SUM);  
	    columnOreOrdinarie.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreOrdinarie.setToolTip("Calcolate escludendo eventuali ore straordinarie e ore a recupero");
	    columnOreOrdinarie.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			  	
		});
	    columnOreOrdinarie.setSummaryRenderer(new SummaryRenderer() {
	   			@Override
	   			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreOrdinare= riep.get("oreOrdinarie");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreOrdinare));
	   				}
	   				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
	   				Float n=Float.valueOf(tot);
					return num.format(n);
	   			}
	    });  
	    configs.add(columnOreOrdinarie);
	    
	    SummaryColumnConfig<Double> columnOreStrao=new SummaryColumnConfig<Double>();		
	    columnOreStrao.setId("oreStraordinarie");  
	    columnOreStrao.setHeader("Ore Straordinarie");  
	    columnOreStrao.setWidth(100);    
	    columnOreStrao.setRowHeader(true); 
	    columnOreStrao.setSummaryType(SummaryType.SUM);  
	    columnOreStrao.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreStrao.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				Float n=model.get(property);
				return number.format(n);
			}
		});
	    columnOreStrao.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
	   			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreStraordinarie= riep.get("oreStraordinarie");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreStraordinarie));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
	   			}  
	      });  
	    configs.add(columnOreStrao);
	    	    
	    SummaryColumnConfig<Double> columnOreRecupero=new SummaryColumnConfig<Double>();		
	    columnOreRecupero.setId("oreRecupero");
	    columnOreRecupero.setHeader("Ore Recupero");
	    columnOreRecupero.setWidth(100);
	    columnOreRecupero.setRowHeader(true);
	    columnOreRecupero.setSummaryType(SummaryType.SUM);
	    columnOreRecupero.setAlignment(HorizontalAlignment.RIGHT);
	    columnOreRecupero.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				Float n=model.get(property);
				return number.format(n);
			}
		});
	    columnOreRecupero.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
	   			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreStraordinarie= riep.get("oreRecupero");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreStraordinarie));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
	   			}  
	      });  
	    configs.add(columnOreRecupero);
	    	    
	    
	    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
	    columnOreViaggio.setId("oreViaggio");  
	    columnOreViaggio.setHeader("Viaggio");  
	    columnOreViaggio.setWidth(100);    
	    columnOreViaggio.setRowHeader(true); 
	    columnOreViaggio.setSummaryType(SummaryType.SUM);  
	    columnOreViaggio.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreViaggio.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				Float n=model.get(property);
				return number.format(n);
			}
			  	
		});
	    columnOreViaggio.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
	   			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreViaggio= riep.get("oreViaggio");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreViaggio));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
	   			}  
	      });  
	    configs.add(columnOreViaggio);
	    
	     
	    SummaryColumnConfig<Double> columnOreTotali=new SummaryColumnConfig<Double>();		
	    columnOreTotali.setId("totaleOreLavoro");  
	    columnOreTotali.setHeader("Totali");  
	    columnOreTotali.setWidth(100);    
	    columnOreTotali.setRowHeader(true); 
	    columnOreTotali.setSummaryType(SummaryType.SUM);  
	    columnOreTotali.setRenderer(renderer);
	    columnOreTotali.setAlignment(HorizontalAlignment.RIGHT); 
	    columnOreTotali.setToolTip("Calcolate come somma tra ore ordinarie, straordinarie e viaggio");
	    columnOreTotali.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				config.style = config.style + ";background-color:#d2f5af;" +"font-weight:bold;" ;
				Float n=model.get(property);
				return number.format(n);
			}			
		});
	    columnOreTotali.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreTotali= riep.get("totaleOreLavoro");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreTotali));
	   				}
	   				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
	   				Float n=Float.valueOf(tot);
					return num.format(n);
			}  
	    });
	    configs.add(columnOreTotali); 	
	    
	    SummaryColumnConfig<Double> columnOreFerie=new SummaryColumnConfig<Double>();		
	    columnOreFerie.setId("oreFerie");  
	    columnOreFerie.setHeader("Ferie");  
	    columnOreFerie.setWidth(100);    
	    columnOreFerie.setRowHeader(true); 
	    columnOreFerie.setSummaryType(SummaryType.SUM);  
	    columnOreFerie.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreFerie.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				Float n=model.get(property);
				if(n!=0)
					n=n*-1;
				return number.format(n);
			}			
		});
	    columnOreFerie.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreFerie= riep.get("oreFerie");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreFerie));
	   				}
	   				
	   				Float n=Float.valueOf(tot)*-1;
					return number.format(n);
			}  
	    });
	    configs.add(columnOreFerie);
	    
	    SummaryColumnConfig<Double> columnOrePermessoRolo=new SummaryColumnConfig<Double>();		
	    columnOrePermessoRolo.setId("orePermessoRol");  
	    columnOrePermessoRolo.setHeader("Permessi ROL");  
	    columnOrePermessoRolo.setWidth(100);    
	    columnOrePermessoRolo.setRowHeader(true); 
	    columnOrePermessoRolo.setSummaryType(SummaryType.SUM);  
	    columnOrePermessoRolo.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOrePermessoRolo.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				Float n=model.get(property);
				if(n!=0)
					n=n*-1;
				return number.format(n);
			}			
		});
	    columnOrePermessoRolo.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreFerie= riep.get("orePermessoRol");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreFerie));
	   				}
	   				
	   				Float n=Float.valueOf(tot)*-1;
					return number.format(n);
			}  
	    });
	    configs.add(columnOrePermessoRolo);
	    
	    SummaryColumnConfig<Double> columnOreMutua=new SummaryColumnConfig<Double>();		
	    columnOreMutua.setId("oreMutua");  
	    columnOreMutua.setHeader("Mutua");  
	    columnOreMutua.setWidth(100);    
	    columnOreMutua.setRowHeader(true); 
	    columnOreMutua.setSummaryType(SummaryType.SUM);  
	    columnOreMutua.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreMutua.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				Float n=model.get(property);
				if(n!=0)
					n=n*-1;
				return number.format(n);
			}			
		});
	    columnOreMutua.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreMutua= riep.get("oreMutua");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreMutua));
	   				}
	   				
	   				Float n=Float.valueOf(tot)*-1;
					return number.format(n);
			}  
	    });
	    configs.add(columnOreMutua);
	    
	    SummaryColumnConfig<Double> columnOreLegge104=new SummaryColumnConfig<Double>();		
	    columnOreLegge104.setId("oreLegge104");  
	    columnOreLegge104.setHeader("Legge 104");  
	    columnOreLegge104.setWidth(100);    
	    columnOreLegge104.setRowHeader(true); 
	    columnOreLegge104.setSummaryType(SummaryType.SUM);  
	    columnOreLegge104.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreLegge104.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				Float n=model.get(property);
				if(n!=0)
					n=n*-1;
				return number.format(n);
			}			
		});
	    columnOreLegge104.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreLegge104= riep.get("oreLegge104");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreLegge104));
	   				}
	   				
	   				Float n=Float.valueOf(tot)*-1;
					return number.format(n);
			}  
	    });
	    configs.add(columnOreLegge104);	    
	    
	    
	    SummaryColumnConfig<Double> columnOreCig=new SummaryColumnConfig<Double>();		
	    columnOreCig.setId("oreCig");  
	    columnOreCig.setHeader("CIG");  
	    columnOreCig.setWidth(100);    
	    columnOreCig.setRowHeader(true); 
	    columnOreCig.setSummaryType(SummaryType.SUM);  
	    columnOreCig.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreCig.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				Float n=model.get(property);
				if(n!=0)
					n=n*-1;
				return number.format(n);
			}			
		});
	    
	    columnOreCig.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreCig= riep.get("oreCig");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreCig));
	   				}
	   				
	   				Float n=Float.valueOf(tot)*-1;
					return number.format(n);
			}  
	    });
	    configs.add(columnOreCig);	    
	    
	    SummaryColumnConfig<Double> columnOreMaternita=new SummaryColumnConfig<Double>();		
	    columnOreMaternita.setId("oreMaternita");  
	    columnOreMaternita.setHeader("Maternita");  
	    columnOreMaternita.setWidth(100);    
	    columnOreMaternita.setRowHeader(true); 
	    columnOreMaternita.setSummaryType(SummaryType.SUM);  
	    columnOreMaternita.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreMaternita.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				Float n=model.get(property);
				if(n!=0)
					n=n*-1;
				return number.format(n);
			}			
		});
	    
	    columnOreMaternita.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreMaternita= riep.get("oreMaternita");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreMaternita));
	   				}
	   				
	   				Float n=Float.valueOf(tot)*-1;
					return number.format(n);
			}  
	    });
	    configs.add(columnOreMaternita);	
	    
	    SummaryColumnConfig<Double> columnOreTotaliGiustificativi=new SummaryColumnConfig<Double>();		
	    columnOreTotaliGiustificativi.setId("oreTotaliGiustificativi");  
	    columnOreTotaliGiustificativi.setHeader("Ore Totali Giust.");  
	    columnOreTotaliGiustificativi.setWidth(100);    
	    columnOreTotaliGiustificativi.setRowHeader(true); 
	    columnOreTotaliGiustificativi.setSummaryType(SummaryType.SUM);  
	    columnOreTotaliGiustificativi.setAlignment(HorizontalAlignment.RIGHT);  
	    //columnOreTotaliGiustificativi.setRenderer(renderer);
	    columnOreTotaliGiustificativi.setRenderer(new GridCellRenderer<RiepilogoOreAnnualiDipendente>() {
			@Override
			public Object render(RiepilogoOreAnnualiDipendente model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreAnnualiDipendente> store,
					Grid<RiepilogoOreAnnualiDipendente> grid) {
				config.style = config.style + ";background-color:#d2f5af;" +"font-weight:bold;" ;
				Float n=model.get(property);
				if(n!=0)
					n=n*-1;
				return number.format(n);
			}			
		});
	    
	    columnOreTotaliGiustificativi.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoOreAnnualiDipendente>store1 = new GroupingStore<RiepilogoOreAnnualiDipendente>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoOreAnnualiDipendente riep: store1.getModels()){
	   					Float oreMaternita= riep.get("oreTotaliGiustificativi");
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(oreMaternita));
	   				}
	   				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					Float n=Float.valueOf(tot)*-1;
					return num.format(n);
			}  
	    });
	    configs.add(columnOreTotaliGiustificativi);	
	       
	    
	    return configs;
	}
}
