package gestione.pack.client.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.layout.panel.PanelRiepilogoDatiPerFatturazione;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
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
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;


public class CenterLayout_RiepilogoDatiFatturazione extends LayoutContainer{

	public CenterLayout_RiepilogoDatiFatturazione(){}
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";

	private String ruolo= new String();
	
	public CenterLayout_RiepilogoDatiFatturazione(String ruolo){
		this.ruolo=ruolo;
	}
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);

		final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
			   				
		layoutContainer.add(new CntpnlRiepilogoMese(), new FitData(3, 3, 3, 3));
		add(layoutContainer);
	}
			
	
	private class CntpnlRiepilogoMese extends ContentPanel{
		
		private SimpleComboBox<String> smplcmbxMese= new SimpleComboBox<String>();
		private SimpleComboBox<String> smplcmbxAnno;
		private SimpleComboBox<String> smplcmbxOrderBy;
		private SimpleComboBox<String> smplcmbxPM;
		
		private GroupingStore<DatiFatturazioneMeseModel>store = new GroupingStore<DatiFatturazioneMeseModel>();
		private GroupingStore<DatiFatturazioneMeseModel>storeRes = new GroupingStore<DatiFatturazioneMeseModel>();
		private EditorGrid<DatiFatturazioneMeseModel> gridRiepilogo;
		private ColumnModel cm;
				
		private Button btnSelect;
		private Button btnPrint;
		private Button btnRiepDatiFatt;
		private Button btnConfermaDati;
		
		CntpnlRiepilogoMese(){
			
			setHeading("Riepilogo Dati Fatturazione (Mensile).");
			setHeaderVisible(false);
			setCollapsible(false);
			setBorders(false);
			setFrame(true);
			setScrollMode(Scroll.AUTO);
			setSize(w-140, h-120);
		//	setPosition(3, 3);
			setLayout(new FitLayout());
			
			Resizable r=new Resizable(this);
		   			
		    Date d= new Date();
			String data= d.toString();
			String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(data.substring(4, 7)));
			String anno= data.substring(data.length()-4, data.length());
		    
			smplcmbxMese= new SimpleComboBox<String>();
			smplcmbxMese.setFieldLabel("Mese");
			smplcmbxMese.setName("mese");
			smplcmbxMese.setEmptyText("Mese..");
			smplcmbxMese.setAllowBlank(false);
			 for(String l : DatiComboBox.getMese()){
				 smplcmbxMese.add(l);}
			smplcmbxMese.setTriggerAction(TriggerAction.ALL);
			smplcmbxMese.setSimpleValue(mese);
			
			smplcmbxAnno= new SimpleComboBox<String>();
			smplcmbxAnno.setFieldLabel("Anno");
			smplcmbxAnno.setName("anno");
			smplcmbxAnno.setEmptyText("Anno..");
			smplcmbxAnno.setAllowBlank(false);
			 for(String l : DatiComboBox.getAnno()){
				 smplcmbxAnno.add(l);}
			smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
			smplcmbxAnno.setSimpleValue(anno);
			
			smplcmbxPM = new SimpleComboBox<String>();
			smplcmbxPM.setName("pm");
			smplcmbxPM.setAllowBlank(true);
			smplcmbxPM.setTriggerAction(TriggerAction.ALL);
			smplcmbxPM.setEmptyText("Project Manager..");
			smplcmbxPM.setAllowBlank(false);
			smplcmbxPM.setWidth(180);
			smplcmbxPM.addListener(Events.Select, new Listener<BaseEvent>() {
				@Override
				public void handleEvent(BaseEvent be) {
					if(smplcmbxPM.getRawValue().toString().compareTo("Tutti")==0)
						gridRiepilogo.reconfigure(store, cm);
						
					else{
						storeRes.removeAll();
						for(DatiFatturazioneMeseModel r:store.getModels())
							if(smplcmbxPM.getRawValue().toString().compareTo((String) r.get("pm"))==0)
								storeRes.add(r);
						storeRes.setSortField("numeroCommessa");
						storeRes.setSortDir(SortDir.ASC);
						storeRes.groupBy("pm");
						gridRiepilogo.reconfigure(storeRes, cm);
					}
				}
			});
			getNomePM();	
			
			
			smplcmbxOrderBy= new SimpleComboBox<String>();
			smplcmbxOrderBy.setFieldLabel("Group By");
			smplcmbxOrderBy.setEmptyText("Selezionare..");
			smplcmbxOrderBy.setAllowBlank(false);
			smplcmbxOrderBy.add("PM");
			smplcmbxOrderBy.add("Tutti");
			smplcmbxOrderBy.add("Sede");
			smplcmbxOrderBy.add("Cliente");
			smplcmbxOrderBy.setWidth(110);
			smplcmbxOrderBy.setTriggerAction(TriggerAction.ALL);
			smplcmbxOrderBy.setSimpleValue("PM");
			smplcmbxOrderBy.addListener(Events.Select, new Listener<BaseEvent>(){
				@Override
				public void handleEvent(BaseEvent be) {
					String raggruppamento=smplcmbxOrderBy.getRawValue().toString().toLowerCase();
					if(raggruppamento.compareTo("Tutti")==0){
						//store.groupBy("",true);
						gridRiepilogo.reconfigure(store, cm);
					}
					else{
						store.groupBy(raggruppamento,true);
						gridRiepilogo.reconfigure(store, cm);
					}
				}		
			});
			
			Text txtGroup= new Text();
			txtGroup.setText("Group By: ");
			Text txtFiltri= new Text();
			txtFiltri.setText("Filtri per la stampa: ");
			
			btnSelect= new Button();
			btnSelect.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.reload()));
			btnSelect.setToolTip("Load");
			btnSelect.setIconAlign(IconAlign.TOP);
			btnSelect.setSize(26, 26);
			btnSelect.addSelectionListener(new SelectionListener<ButtonEvent>() {		
				@Override
				public void componentSelected(ButtonEvent ce) {		
					if(smplcmbxMese.isValid()&&smplcmbxAnno.isValid()){
						String meseRif= new String();
						String anno= smplcmbxAnno.getRawValue().toString();
						String data;
						meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
						data=meseRif+anno;
						caricaTabellaDati(data);			
					}else Window.alert("Controllare i dati selezionati!");
				}
			});	
			
			btnPrint = new Button();
			btnPrint.setSize("55px","25px");	   
			btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
			btnPrint.setToolTip("Stampa");
			btnPrint.setIconAlign(IconAlign.TOP);
			btnPrint.setSize(26, 26);
			btnPrint.setEnabled(true);
			btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {		
				@Override
				public void componentSelected(ButtonEvent ce) {
					String mese=smplcmbxMese.getRawValue().toString();
					String anno=smplcmbxAnno.getRawValue().toString();
					mese=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
					GroupingStore<DatiFatturazioneMeseModel> storeF= new GroupingStore<DatiFatturazioneMeseModel>();
					storeF.add(store.getModels());
					
					if(smplcmbxPM.getValue().getValue().compareTo("")!=0){
						storeF.removeAll();
						storeF.add(storeRes.getModels());
					}						
					
					SessionManagementService.Util.getInstance().setDataReportDatiFatturazioneInSession(anno, mese, storeF.getModels(), "RIEP.FATT",
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
			
			btnRiepDatiFatt= new Button();
			btnRiepDatiFatt.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
			//btnRiepDatiFatt.setToolTip("Load");
			btnRiepDatiFatt.setIconAlign(IconAlign.TOP);
			btnRiepDatiFatt.setSize(26, 26);
			btnRiepDatiFatt.addSelectionListener(new SelectionListener<ButtonEvent>() {		
				@Override
				public void componentSelected(ButtonEvent ce) {		
					Dialog d= new Dialog();
					//d.setHeaderVisible(false);
					d.setSize(1350, 1060);
					d.setButtons("");
					d.setHeading("Dati per la fatturazione.");
					d.add(new PanelRiepilogoDatiPerFatturazione(store.getModels()));
					d.show();
				}
			});
			
			btnConfermaDati= new Button();
			btnConfermaDati.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
			btnConfermaDati.setToolTip("Conferma i dati del mese");
			btnConfermaDati.setIconAlign(IconAlign.TOP);
			btnConfermaDati.setSize(26, 26);
			btnConfermaDati.addSelectionListener(new SelectionListener<ButtonEvent>() {		
				@Override
				public void componentSelected(ButtonEvent ce) {
					String mese=smplcmbxMese.getRawValue().toString();
					String anno=smplcmbxAnno.getRawValue().toString();
					mese=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
					
					AdministrationService.Util.getInstance().setStatoFoglioFatturazione(mese, anno, new AsyncCallback<Boolean>(){
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore di connessione on setStatoFoglioFatturazione();");
						}

						@Override
						public void onSuccess(Boolean result) {
							if(result)
								Window.alert("Conferma avvenuta con successo!");
							else
								Window.alert("Problemi riscontrati durante l'accesso ai dati!");
						}
					});
				}
			});
			if((ruolo.compareTo("AMM")!=0)&&(ruolo.compareTo("UA")!=0))
				btnConfermaDati.setVisible(false);		
			
			fp.setMethod(FormPanel.METHOD_POST);
			fp.setAction(url);
			fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
			fp.add(btnPrint);
			ContentPanel cp= new ContentPanel();
			cp.setHeaderVisible(false);
			cp.add(fp);
						
			ToolBar tlbOperazioni= new ToolBar();
			tlbOperazioni.add(smplcmbxAnno);
			tlbOperazioni.add(smplcmbxMese);
			tlbOperazioni.add(btnSelect);
			tlbOperazioni.add(new SeparatorToolItem());
			tlbOperazioni.add(txtGroup);
			tlbOperazioni.add(smplcmbxOrderBy);
			tlbOperazioni.add(new SeparatorToolItem());
			tlbOperazioni.add(txtFiltri);
			tlbOperazioni.add(smplcmbxPM);			
			tlbOperazioni.add(cp);
			tlbOperazioni.add(new SeparatorToolItem());
			tlbOperazioni.add(btnConfermaDati);
			setTopComponent(tlbOperazioni);
			
		    try {
		    	cm = new ColumnModel(createColumns());	
			} catch (Exception e) {
				e.printStackTrace();
				Window.alert("error: Problema createColumns().");			
			}	

			store.groupBy("pm");
			store.setSortField("numeroCommessa");
			store.setSortDir(SortDir.ASC);			
			    
		    GroupSummaryView summary = new GroupSummaryView();  
		    summary.setForceFit(false);  
		    summary.setShowGroupedColumn(false);     
		    		    
		    gridRiepilogo= new EditorGrid<DatiFatturazioneMeseModel>(store, cm);  
		    gridRiepilogo.setBorders(false); 
		    gridRiepilogo.setColumnLines(true);
		    gridRiepilogo.setStripeRows(true);
		    gridRiepilogo.setView(summary);  
		    gridRiepilogo.getView().setShowDirtyCells(false);
		   // gridRiepilogo.addPlugin(expander); 
		    			   		   	    	   
		  	add(gridRiepilogo);			
		}
		
		
		private class FormSubmitCompleteHandler implements SubmitCompleteHandler {

			@Override
			public void onSubmitComplete(final SubmitCompleteEvent event) {
				
				//Window.open("/FileStorage/RiepilogoAnnuale.pdf", "_blank", "1");
				
			}
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
						smplcmbxPM.add(result);
						smplcmbxPM.add("Tutti");
						smplcmbxPM.recalculate();
												
					}else Window.alert("error: Errore durante l'accesso ai dati PM.");			
				}
			});		
		}


		private List<ColumnConfig> createColumns() {
			List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			final NumberFormat number= NumberFormat.getFormat("0.00");
			
			/*XTemplate tpl = XTemplate.create("<p><b>Note:</b> {note}</p><br>");  
			expander = new RowExpander();
			expander.setTemplate(tpl); 
			expander.setWidth(20);	
			
			configs.add(expander);*/
			
			SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
		    column.setId("sede");  
		    column.setHeader("Sede");  
		    column.setWidth(30);  
		    column.setRowHeader(true);  
		    configs.add(column); 
			
			column=new SummaryColumnConfig<Double>();		
		    column.setId("pm");  
		    column.setHeader("Project Manager");  
		    column.setWidth(140);  
		    column.setRowHeader(true);  
		    configs.add(column); 
			
			column=new SummaryColumnConfig<Double>();		
		    column.setId("numeroCommessa");  
		    column.setHeader("Commessa");  
		    column.setWidth(70);  
		    column.setRowHeader(true);  
		    column.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					return model.get(property);
				}  	
			});
		    column.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					return "TOTALE";
				}
			});
		    configs.add(column); 
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("cliente");  
			column.setHeader("Cliente");  
			column.setWidth(150);  
			column.setRowHeader(true); 
		    configs.add(column); 
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("numeroOrdine");  
			column.setHeader("Ordine");  
			column.setWidth(80);  
			column.setRowHeader(true); 
		    configs.add(column); 
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("oggettoAttivita");  
		    column.setHeader("Oggetto");  
		    column.setWidth(140);  
		    column.setRowHeader(true); 
		    configs.add(column);
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("attivitaOrdine");  
		    column.setHeader("Attivita' ordine");  
		    column.setWidth(100);  
		    column.setRowHeader(true); 
		    configs.add(column);
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("tariffaOraria");  
		    column.setHeader("Tariffa");  
		    column.setWidth(80);  
		    column.setRowHeader(true);
		    column.setAlignment(HorizontalAlignment.RIGHT); 
		    column.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			}); 
		    configs.add(column);
		    	    
		    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
		    columnOreLavoro.setId("oreEseguite");  
		    columnOreLavoro.setHeader("Ore Eseguite");  
		    columnOreLavoro.setWidth(75);    
		    columnOreLavoro.setRowHeader(true); 
		    columnOreLavoro.setAlignment(HorizontalAlignment.RIGHT);
		    columnOreLavoro.setSummaryType(SummaryType.SUM);  
		    columnOreLavoro.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			}); 
		    columnOreLavoro.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					return number.format(value);
				}
			});
		    configs.add(columnOreLavoro); 	
		    
		    SummaryColumnConfig<Double> columnOreFatturate=new SummaryColumnConfig<Double>();		
		    columnOreFatturate.setId("oreFatturate");  
		    columnOreFatturate.setHeader("Ore Fatturate");  
		    columnOreFatturate.setWidth(75);    
		    columnOreFatturate.setRowHeader(true);   
		    columnOreFatturate.setAlignment(HorizontalAlignment.RIGHT);    
		    columnOreFatturate.setSummaryType(SummaryType.SUM);  
		    columnOreFatturate.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});     
		    columnOreFatturate.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					return number.format(value);
				}
			});
		     configs.add(columnOreFatturate); 	
		      
		    SummaryColumnConfig<Double> columnImporto=new SummaryColumnConfig<Double>();		
		    columnImporto.setId("importo");  
		    columnImporto.setHeader("Importo");  
		    columnImporto.setWidth(100);    
		    columnImporto.setRowHeader(true); 
		    columnImporto.setAlignment(HorizontalAlignment.RIGHT);
		    columnImporto.setStyle("color:#e71d2b;");
		    columnImporto.setSummaryType(SummaryType.SUM);  
		    columnImporto.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {				
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					Float n=model.get(property);
					return num.format(n);
				}  	
			});  
		    columnImporto.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					
					return num.format(value);
				}
			});
		     configs.add(columnImporto); 
		     
		     SummaryColumnConfig<Double> columnImportoEffettivo=new SummaryColumnConfig<Double>();		
		     columnImportoEffettivo.setId("importoEffettivo");  
		     columnImportoEffettivo.setHeader("Importo Effettivo");  
		     columnImportoEffettivo.setWidth(100);    
		     columnImportoEffettivo.setRowHeader(true); 
		     columnImportoEffettivo.setAlignment(HorizontalAlignment.RIGHT);
		     columnImportoEffettivo.setStyle("color:#e71d2b; background-color:#d2f5af;");
		     columnImportoEffettivo.setSummaryType(SummaryType.SUM);  
		     columnImportoEffettivo.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
					@Override
					public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
							Grid<DatiFatturazioneMeseModel> grid) {				
						final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
						Float n=model.get(property);
						return num.format(n);
					}  	
				});  
		     columnImportoEffettivo.setSummaryRenderer(new SummaryRenderer() {
					
					@Override
					public String render(Number value, Map<String, Number> data) {
						final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
						
						return num.format(value);
					}
				});
			configs.add(columnImportoEffettivo); 
		    	    
		    SummaryColumnConfig<Double> variazioneSal=new SummaryColumnConfig<Double>();		
		    variazioneSal.setId("variazioneSal");  
		    variazioneSal.setHeader("Var. SAL");  
		    variazioneSal.setWidth(80);    
		    variazioneSal.setRowHeader(true); 
		    variazioneSal.setAlignment(HorizontalAlignment.RIGHT);  
		    variazioneSal.setSummaryType(SummaryType.SUM);  
		    variazioneSal.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    variazioneSal.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					return number.format(value);
				}
			});
		    configs.add(variazioneSal); 	
		    
		    SummaryColumnConfig<Double> columnImportoSal=new SummaryColumnConfig<Double>();		
		    columnImportoSal.setId("importoSal");  
		    columnImportoSal.setHeader("Importo Sal");  
		    columnImportoSal.setWidth(100);    
		    columnImportoSal.setRowHeader(true); 
		    columnImportoSal.setAlignment(HorizontalAlignment.RIGHT);
		    columnImportoSal.setStyle("color:#e71d2b;");
		    columnImportoSal.setSummaryType(SummaryType.SUM);  
		    columnImportoSal.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {				
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					Float n=model.get(property);
					return num.format(n);
				}  	
			});  
		    columnImportoSal.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					
					return num.format(value);
				}
			});
		    configs.add(columnImportoSal);
		    
		    SummaryColumnConfig<Double> totaleOreSal=new SummaryColumnConfig<Double>();		
		    totaleOreSal.setId("totaleOreSal");  
		    totaleOreSal.setHeader("TOT. SAL");  
		    totaleOreSal.setWidth(80);    
		    totaleOreSal.setRowHeader(true); 
		    totaleOreSal.setAlignment(HorizontalAlignment.RIGHT);  
		    //totaleOreSal.setSummaryType(SummaryType.SUM);  
		    totaleOreSal.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    /*totaleOreSal.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					return number.format(value);
				}
			});*/
		    configs.add(totaleOreSal);     
		    
		    SummaryColumnConfig<Double> variazionePcl=new SummaryColumnConfig<Double>();		
		    variazionePcl.setId("variazionePcl");  
		    variazionePcl.setHeader("Var. PCL");  
		    variazionePcl.setWidth(80);    
		    variazionePcl.setRowHeader(true); 
		    variazionePcl.setAlignment(HorizontalAlignment.RIGHT);  
		    variazionePcl.setSummaryType(SummaryType.SUM);  
		    variazionePcl.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    variazionePcl.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					return number.format(value);
				}
			});
		    configs.add(variazionePcl);     
		    
		    SummaryColumnConfig<Double> columnImportoPcl=new SummaryColumnConfig<Double>();		
		    columnImportoPcl.setId("importoPcl");  
		    columnImportoPcl.setHeader("Importo Pcl");  
		    columnImportoPcl.setWidth(100);    
		    columnImportoPcl.setRowHeader(true); 
		    columnImportoPcl.setAlignment(HorizontalAlignment.RIGHT);
		    columnImportoPcl.setStyle("color:#e71d2b;");
		    columnImportoPcl.setSummaryType(SummaryType.SUM);  
		    columnImportoPcl.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {				
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					Float n=model.get(property);
					return num.format(n);
				}  	
			});  
		    columnImportoPcl.setSummaryRenderer(new SummaryRenderer() {			
				@Override
				public String render(Number value, Map<String, Number> data) {
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					
					return num.format(value);
				}
			});
		    configs.add(columnImportoPcl);
		    
		    SummaryColumnConfig<Double> totaleOrePcl=new SummaryColumnConfig<Double>();		
		    totaleOrePcl.setId("totaleOrePcl");  
		    totaleOrePcl.setHeader("TOT. PCL");  
		    totaleOrePcl.setWidth(80);    
		    totaleOrePcl.setRowHeader(true); 
		    totaleOrePcl.setAlignment(HorizontalAlignment.RIGHT);  
		    //totaleOrePcl.setSummaryType(SummaryType.SUM);  
		    totaleOrePcl.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    /*totaleOrePcl.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					return number.format(value);
				}
			});*/
		    configs.add(totaleOrePcl);     
		    
		    SummaryColumnConfig<Double> oreScaricate=new SummaryColumnConfig<Double>();		
		    oreScaricate.setId("oreScaricate");  
		    oreScaricate.setHeader("Scaricate");  
		    oreScaricate.setWidth(80);    
		    oreScaricate.setRowHeader(true); 
		    oreScaricate.setAlignment(HorizontalAlignment.RIGHT);  	
		    oreScaricate.setSummaryType(SummaryType.SUM);  
		    oreScaricate.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    oreScaricate.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					return number.format(value);
				}
			});
		    configs.add(oreScaricate);		    
		    
		    SummaryColumnConfig<Double> oreRimborso=new SummaryColumnConfig<Double>();		
		    oreRimborso.setId("oreRimborsoSpese");  
		    oreRimborso.setHeader("h/Rimborso Spese");  
		    oreRimborso.setWidth(100);    
		    oreRimborso.setRowHeader(true);  
		    oreRimborso.setAlignment(HorizontalAlignment.RIGHT); 
		    oreRimborso.setSummaryType(SummaryType.SUM);  
		    oreRimborso.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    oreRimborso.setSummaryRenderer(new SummaryRenderer() {				
				@Override
				public String render(Number value, Map<String, Number> data) {
					return number.format(value);
				}
			});
		    configs.add(oreRimborso);	
		    
		    SummaryColumnConfig<Double> margine=new SummaryColumnConfig<Double>();		
		    margine.setId("margine");  
		    margine.setHeader("Margine");  
		    margine.setWidth(80);    
		    margine.setRowHeader(true);  
		    margine.setAlignment(HorizontalAlignment.RIGHT);  	
		    margine.setSummaryType(SummaryType.SUM);  
		    margine.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    margine.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					return number.format(value);
				}
			});
		    configs.add(margine);			    
		    
		    
		    SummaryColumnConfig<Double> efficienza=new SummaryColumnConfig<Double>();		
		    efficienza.setId("efficienza");  
		    efficienza.setHeader("Eff.");  
		    efficienza.setWidth(55);    
		    efficienza.setRowHeader(true);  
		    efficienza.setAlignment(HorizontalAlignment.RIGHT);
		    efficienza.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {	
					
					String efficienza=model.get("efficienza");
					String numeroCommessa=model.get("numeroCommessa");
					if(numeroCommessa.compareTo("TOTALE")!=0)
						if(efficienza.length()>2)
						//if(!Float.valueOf(efficienza).isInfinite()&&!Float.valueOf(efficienza).isNaN())
							if(Float.valueOf(efficienza)<1.20)
							{
								String color = "#f0f080";
								config.style = config.style + ";background-color:" + color + ";";									
							}
							else{
								String color = "#90EE90"; 
								config.style = config.style + ";background-color:" + color + ";";
							}	
						else
							return "0.00";
					else
						config.style = config.style + ";background-color:" + "#FFFFFF" + ";";
					
					return model.get(property);
				}  	
			});
		   configs.add(efficienza);	
		   
		   SummaryColumnConfig<Double> note=new SummaryColumnConfig<Double>();		
		   note.setId("note");  
		   note.setHeader("Note");  
		   note.setWidth(250);    
		   note.setRowHeader(true);  
		   note.setAlignment(HorizontalAlignment.RIGHT);  	
		   configs.add(note);
		   
		   return configs;
		}

		
		private void caricaTabellaDati(String mese) {
			AdministrationService.Util.getInstance().getReportDatiFatturazioneMese(mese, new AsyncCallback<List<DatiFatturazioneMeseModel>>() {		
				@Override
				public void onSuccess(List<DatiFatturazioneMeseModel> result) {
					if(result==null)
						Window.alert("error: Problemi durante l'accesso ai dati di fatturazione.");
					else	
						if(result.size()==0)
							Window.alert("Nessun dato rilevato in base ai criteri di ricerca selezionati.");
						loadTable(result);			
				}
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getReportDatiFatturazioneMese();");
					caught.printStackTrace();		
				}
			}); //AsyncCallback	   
		}
		
		
		private void loadTable(List<DatiFatturazioneMeseModel> result) {
			try {
				store.removeAll();
				store.add(result);
				store.groupBy("pm");
				gridRiepilogo.reconfigure(store, cm);	    		    	
			} catch (NullPointerException e) {
				Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
					e.printStackTrace();
			}
		}	
	}	
}


