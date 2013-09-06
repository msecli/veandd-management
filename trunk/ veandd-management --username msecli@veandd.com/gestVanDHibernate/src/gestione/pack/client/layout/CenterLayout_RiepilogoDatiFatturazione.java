package gestione.pack.client.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.DatiFatturazioneCommessaModel;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
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
		private SimpleComboBox<String> smplcmbxPM;
		private GroupingStore<DatiFatturazioneMeseModel>store = new GroupingStore<DatiFatturazioneMeseModel>();
		private EditorGrid<DatiFatturazioneMeseModel> gridRiepilogo;
		private ColumnModel cm;
		
		private Button btnSelect;
		private Button btnPrint;
		
		CntpnlRiepilogoMese(){
			
			setHeading("Riepilogo Dati Fatturazione (Mensile).");
			setHeaderVisible(false);
			setCollapsible(false);
			setBorders(false);
			setFrame(true);
			setScrollMode(Scroll.AUTO);
			setSize(w-140, h-50);
		//	setPosition(3, 3);
			setLayout(new FitLayout());
			
			Resizable r=new Resizable(this);
		    r.setMinWidth(w-200);
		    r.setMinHeight(h-80);
			
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
			
			/*
			smplcmbxPM = new SimpleComboBox<String>();
			smplcmbxPM.setFieldLabel("Project Manager");
			smplcmbxPM.setName("pm");
			smplcmbxPM.setAllowBlank(true);
			smplcmbxPM.setTriggerAction(TriggerAction.ALL);
			smplcmbxPM.setEmptyText("Project Manager..");
			smplcmbxPM.setAllowBlank(false);
			getNomePM();	
			*/
			
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
					
					SessionManagementService.Util.getInstance().setDataReportDatiFatturazioneInSession(anno, mese, "RIEP.FATT",
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
						
			ToolBar tlbOperazioni= new ToolBar();
			tlbOperazioni.add(smplcmbxAnno);
			tlbOperazioni.add(smplcmbxMese);
			tlbOperazioni.add(btnSelect);
			tlbOperazioni.add(cp);
			setTopComponent(tlbOperazioni);
			
		    try {
		    	cm = new ColumnModel(createColumns());	
			} catch (Exception e) {
				e.printStackTrace();
				Window.alert("error: Problema createColumns().");			
			}	

			store.groupBy("pm");
			    
		    GroupSummaryView summary = new GroupSummaryView();  
		    summary.setForceFit(false);  
		    summary.setShowGroupedColumn(false);  
			   
		    		    
		    gridRiepilogo= new EditorGrid<DatiFatturazioneMeseModel>(store, cm);  
		    gridRiepilogo.setBorders(false);  
		    gridRiepilogo.setView(summary);  
		    gridRiepilogo.getView().setShowDirtyCells(false);
		    			   		   	    	   
		  	add(gridRiepilogo);			
		}
		
		
		private class FormSubmitCompleteHandler implements SubmitCompleteHandler {

			@Override
			public void onSubmitComplete(final SubmitCompleteEvent event) {
				
				//Window.open("/FileStorage/RiepilogoAnnuale.pdf", "_blank", "1");
				
			}
		}
	/*
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
						smplcmbxPM.recalculate();
												
					}else Window.alert("error: Errore durante l'accesso ai dati PM.");			
				}
			});		
		}*/


		private List<ColumnConfig> createColumns() {
			List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			final NumberFormat number= NumberFormat.getFormat("0.00");
			
			SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
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
		    configs.add(column); 
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("cliente");  
			column.setHeader("Cliente");  
			column.setWidth(150);  
			column.setRowHeader(true); 
		    configs.add(column); 
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("oggettoAttivita");  
		    column.setHeader("Oggetto Att.");  
		    column.setWidth(140);  
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
		    columnOreLavoro.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			}); 
		    configs.add(columnOreLavoro); 	
		    
		    SummaryColumnConfig<Double> columnOreFatturate=new SummaryColumnConfig<Double>();		
		    columnOreFatturate.setId("oreFatturate");  
		    columnOreFatturate.setHeader("Ore Fatturate");  
		    columnOreFatturate.setWidth(75);    
		    columnOreFatturate.setRowHeader(true);   
		    columnOreFatturate.setAlignment(HorizontalAlignment.RIGHT);    
		    columnOreFatturate.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
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
		    columnImporto.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {				
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					Float n=model.get(property);
					return num.format(n);
				}  	
			});  
		    configs.add(columnImporto); 	
		    	    
		    SummaryColumnConfig<Double> variazioneSal=new SummaryColumnConfig<Double>();		
		    variazioneSal.setId("variazioneSal");  
		    variazioneSal.setHeader("Var. SAL");  
		    variazioneSal.setWidth(80);    
		    variazioneSal.setRowHeader(true); 
		    variazioneSal.setAlignment(HorizontalAlignment.RIGHT);  	
		    variazioneSal.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
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
		    columnImportoSal.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {				
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					Float n=model.get(property);
					return num.format(n);
				}  	
			});  
		    configs.add(columnImportoSal);
		    
		    SummaryColumnConfig<Double> variazionePcl=new SummaryColumnConfig<Double>();		
		    variazionePcl.setId("variazionePcl");  
		    variazionePcl.setHeader("Var. PCL");  
		    variazionePcl.setWidth(80);    
		    variazionePcl.setRowHeader(true); 
		    variazionePcl.setAlignment(HorizontalAlignment.RIGHT);  	
		    variazionePcl.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
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
		    columnImportoPcl.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {				
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					Float n=model.get(property);
					return num.format(n);
				}  	
			});  
		    configs.add(columnImportoPcl);
		    
		    SummaryColumnConfig<Double> oreScaricate=new SummaryColumnConfig<Double>();		
		    oreScaricate.setId("oreScaricate");  
		    oreScaricate.setHeader("Scaricate");  
		    oreScaricate.setWidth(80);    
		    oreScaricate.setRowHeader(true); 
		    oreScaricate.setAlignment(HorizontalAlignment.RIGHT);  	
		    oreScaricate.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		   configs.add(oreScaricate);
		    
		    
		    SummaryColumnConfig<Double> margine=new SummaryColumnConfig<Double>();		
		    margine.setId("margine");  
		    margine.setHeader("Margine");  
		    margine.setWidth(80);    
		    margine.setRowHeader(true);  
		    margine.setAlignment(HorizontalAlignment.RIGHT);  	
		    margine.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		   configs.add(margine);	
		   
		   SummaryColumnConfig<Double> note=new SummaryColumnConfig<Double>();		
		   note.setId("note");  
		   note.setHeader("Note");  
		   note.setWidth(200);    
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
	
	
	private class CntpnlRiepilogoDatiFatturazioneCommesse extends ContentPanel{
		
		private GroupingStore<DatiFatturazioneCommessaModel>store = new GroupingStore<DatiFatturazioneCommessaModel>();
		private EditorGrid<DatiFatturazioneCommessaModel> gridRiepilogo;
		private ColumnModel cm;	
		
		CntpnlRiepilogoDatiFatturazioneCommesse(){
			
			setHeading("Riepilogo Dati Fatturazione");
			setHeaderVisible(true);
			setCollapsible(false);
			setExpanded(true);
			setBorders(false);
			setScrollMode(Scroll.NONE);	
			setWidth(1000);
			setFrame(true);
			setScrollMode(Scroll.AUTOY);
		
			/*smplcmbxCommessa = new SimpleComboBox<String>();
			smplcmbxCommessa.setFieldLabel("Personal Manager");
			smplcmbxCommessa.setName("pm");
			smplcmbxCommessa.setTriggerAction(TriggerAction.ALL);
			smplcmbxCommessa.setEmptyText("Personal Manager..");
			smplcmbxCommessa.setAllowBlank(false);
			getCommesse();		
			
			btnSelect= new Button("OK");
			btnSelect.addSelectionListener(new SelectionListener<ButtonEvent>() {		
				@Override
				public void componentSelected(ButtonEvent ce) {		
					if(smplcmbxCommessa.isValid()){			
						caricaTabellaDatiRiepilogoTotale(smplcmbxCommessa.getRawValue().toString());			
					}else Window.alert("Controllare i dati selezionati!");
				}
			});*/	
			
			/*HorizontalPanel horizontalPanel = new HorizontalPanel();
			horizontalPanel.setSpacing(5);
			horizontalPanel.add(smplcmbxCommessa);
			horizontalPanel.add(btnSelect);*/
			
			//setTopComponent(horizontalPanel);
			
			try {
			    	cm = new ColumnModel(createColumnsRiepilogoTotale());	
				} catch (Exception e) {
					e.printStackTrace();
					Window.alert("error: Problema createColumns().");			
			}
			
			store.groupBy("numeroCommessa");
				    
			GroupSummaryView summary = new GroupSummaryView();  
			summary.setForceFit(true);  
			summary.setShowGroupedColumn(false);  
			
			gridRiepilogo= new EditorGrid<DatiFatturazioneCommessaModel>(store, cm);  
			gridRiepilogo.setBorders(false);  
			gridRiepilogo.setView(summary);  
			gridRiepilogo.getView().setShowDirtyCells(false);	    
			    
			//caricaTabellaDatiFatturazioneCommesse();
			
			ContentPanel cntpnlGrid= new ContentPanel();
			cntpnlGrid.setBodyBorder(false);  
			cntpnlGrid.setBorders(false);
			cntpnlGrid.setFrame(true);
			cntpnlGrid.setLayout(new FitLayout());  
			cntpnlGrid.setHeaderVisible(false);
			cntpnlGrid.setWidth(980);
			cntpnlGrid.setHeight(400);
			cntpnlGrid.setScrollMode(Scroll.AUTOY);
			cntpnlGrid.add(gridRiepilogo);
			   	    
			VerticalPanel vp= new VerticalPanel();
			vp.setBorders(false);
			vp.add(cntpnlGrid);
			  	
			add(vp);
		}
		

		private List<ColumnConfig> createColumnsRiepilogoTotale() {
			List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			final NumberFormat number= NumberFormat.getFormat("0.00");
			SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
		    column.setId("numeroCommessa");  
		    column.setHeader("Commessa");  
		    column.setWidth(140);  
		    column.setRowHeader(true);  
		    configs.add(column); 
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("mese");  
			column.setHeader("Mese");  
			column.setWidth(60);  
			column.setRowHeader(true); 
		    configs.add(column);
		    	    
		    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
		    columnOreLavoro.setId("oreEseguite");  
		    columnOreLavoro.setHeader("Ore Eseguite");  
		    columnOreLavoro.setWidth(80);    
		    columnOreLavoro.setRowHeader(true); 
		    columnOreLavoro.setSummaryType(SummaryType.SUM);  
		    columnOreLavoro.setAlignment(HorizontalAlignment.RIGHT);  	
		    columnOreLavoro.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
				@Override
				public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
						Grid<DatiFatturazioneCommessaModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    columnOreLavoro.setSummaryRenderer(new SummaryRenderer() {  
		   			@Override
				public String render(Number value, Map<String, Number> data) {
		   				Float n=value.floatValue();
						return number.format(n);
				}  
		      });  
		    configs.add(columnOreLavoro); 	
		    
		    SummaryColumnConfig<Double> columnOreFatturate=new SummaryColumnConfig<Double>();		
		    columnOreFatturate.setId("oreFatturate");  
		    columnOreFatturate.setHeader("Ore Fatturate");  
		    columnOreFatturate.setWidth(80);    
		    columnOreFatturate.setRowHeader(true); 
		    columnOreFatturate.setSummaryType(SummaryType.SUM);  
		    columnOreFatturate.setAlignment(HorizontalAlignment.RIGHT);    
		    columnOreFatturate.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
				@Override
				public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
						Grid<DatiFatturazioneCommessaModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    columnOreFatturate.setSummaryRenderer(new SummaryRenderer() {  
		   			@Override
				public String render(Number value, Map<String, Number> data) {
		   				Float n=value.floatValue();
						return number.format(n);
				}  
		      });      
		    configs.add(columnOreFatturate); 	
		      
		    SummaryColumnConfig<Double> columnImporto=new SummaryColumnConfig<Double>();		
		    columnImporto.setId("importo");  
		    columnImporto.setHeader("Importo");  
		    columnImporto.setWidth(80);    
		    columnImporto.setRowHeader(true); 
		    columnImporto.setSummaryType(SummaryType.SUM);  
		    columnImporto.setAlignment(HorizontalAlignment.RIGHT);
		    columnImporto.setStyle("color:#e71d2b;");
		    //columnImporto.setNumberFormat(NumberFormat.getCurrencyFormat("EUR"));
		    
		    columnImporto.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
				@Override
				public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
						Grid<DatiFatturazioneCommessaModel> grid) {				
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					Float n=model.get(property);
					return num.format(n);
				}  	
			});
		    columnImporto.setSummaryRenderer(new SummaryRenderer() {  
		   			@Override
				public String render(Number value, Map<String, Number> data) {
		   				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
		   				Float n=value.floatValue();
						return num.format(n);
				}  
		      });      
		    configs.add(columnImporto); 	
		    	    
		    SummaryColumnConfig<Double> variazioneSal=new SummaryColumnConfig<Double>();		
		    variazioneSal.setId("variazioneSal");  
		    variazioneSal.setHeader("SAL");  
		    variazioneSal.setWidth(80);    
		    variazioneSal.setRowHeader(true); 
		    variazioneSal.setSummaryType(SummaryType.SUM);  
		    variazioneSal.setAlignment(HorizontalAlignment.RIGHT);  	
		    variazioneSal.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
				@Override
				public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
						Grid<DatiFatturazioneCommessaModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    variazioneSal.setSummaryRenderer(new SummaryRenderer() {  
		   			@Override
				public String render(Number value, Map<String, Number> data) {
		   				Float n=value.floatValue();
						return number.format(n);
				}  
		      });  
		    configs.add(variazioneSal); 	    
		    
		    SummaryColumnConfig<Double> variazionePcl=new SummaryColumnConfig<Double>();		
		    variazionePcl.setId("variazionePcl");  
		    variazionePcl.setHeader("PCL");  
		    variazionePcl.setWidth(80);    
		    variazionePcl.setRowHeader(true); 
		    variazionePcl.setSummaryType(SummaryType.SUM);  
		    variazionePcl.setAlignment(HorizontalAlignment.RIGHT);  	
		    variazionePcl.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
				@Override
				public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
						Grid<DatiFatturazioneCommessaModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    variazionePcl.setSummaryRenderer(new SummaryRenderer() {  
		   			@Override
				public String render(Number value, Map<String, Number> data) {
		   				Float n=value.floatValue();
						return number.format(n);
				}  
		      });  
		    configs.add(variazionePcl);     
		    
		    SummaryColumnConfig<Double> margine=new SummaryColumnConfig<Double>();		
		    margine.setId("margine");  
		    margine.setHeader("Margine");  
		    margine.setWidth(80);    
		    margine.setRowHeader(true); 
		    margine.setSummaryType(SummaryType.SUM);  
		    margine.setAlignment(HorizontalAlignment.RIGHT);  	
		    margine.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
				@Override
				public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
						Grid<DatiFatturazioneCommessaModel> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    margine.setSummaryRenderer(new SummaryRenderer() {		
				@Override
				public String render(Number value, Map<String, Number> data) {				
					Float n=value.floatValue();
					return number.format(n);
				}
			});
		   configs.add(margine);
			
			return configs;
		}
		
		
		private void caricaTabellaDatiFatturazioneCommesse() {
			AdministrationService.Util.getInstance().getRiepilogoDatiFatturazioneCommessa(".",new AsyncCallback<List<DatiFatturazioneCommessaModel>>() {
				
				@Override
				public void onSuccess(List<DatiFatturazioneCommessaModel> result) {
					if(result==null)
						Window.alert("error: Problemi durante l'accesso ai dati di fatturazione");
					else	
						if(result.size()==0)
							Window.alert("Nessun dato rilevato in base ai criteri di ricerca selezionati.");
						loadTable(result);			
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getRiepilogoDatiFatturazioneCommessa();");
					caught.printStackTrace();		
				}
			}); //AsyncCallback	   
		}
		
		private void loadTable(List<DatiFatturazioneCommessaModel> result) {
			try {
				store.removeAll();
				store.add(result);
				store.groupBy("numeroCommessa");
				gridRiepilogo.reconfigure(store, cm);
		    		    	
			} catch (NullPointerException e) {
				Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
					e.printStackTrace();
			}			
		}
		/*
		private void getCommesse() {//ricavo le commesse con dati di foglio fatturazione
			AdministrationService.Util.getInstance().getCommesseRiepilogoDatiFatturazione(new AsyncCallback<List<String>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getCommesseRiepilogoDatiFatturazione();");
					caught.printStackTrace();
				}

				@Override
				public void onSuccess(List<String> result) {
					if(result!=null){
						smplcmbxCommessa.add(result);
						smplcmbxCommessa.recalculate();
					}else Window.alert("error: Errore durante l'accesso ai dati Commesse.");			
				}
			});		
		}*/
	}	
}


