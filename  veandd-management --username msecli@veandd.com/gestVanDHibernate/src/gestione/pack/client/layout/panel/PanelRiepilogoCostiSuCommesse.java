package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.RiepilogoCostiDipSuCommesseFatturateModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

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
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.AggregationRenderer;
import com.extjs.gxt.ui.client.widget.grid.AggregationRowConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class PanelRiepilogoCostiSuCommesse  extends LayoutContainer{

	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private GroupingStore<RiepilogoCostiDipSuCommesseFatturateModel> store=new GroupingStore<RiepilogoCostiDipSuCommesseFatturateModel>();
	private ColumnModel cm;
	private EditorGrid<RiepilogoCostiDipSuCommesseFatturateModel> gridRiepilogo;
	
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	protected SimpleComboBox<String> smplcmbxAnno;
	protected SimpleComboBox<String> smplcmbxMese;
	protected SimpleComboBox<String> smplcmbxPm;
	protected Button btnAggiorna;
	protected Button btnMostraTotale;
	protected Button btnPrint;
	protected Label lblchbx;
	protected CheckBox chbxIncludi;
	protected Status status;
	
	public PanelRiepilogoCostiSuCommesse(){
		
	}
	
	
	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		status = new Status();
	    status.setBusy("Please wait...");
	    status.hide();
	    status.setAutoWidth(true);
		
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		VerticalPanel vp= new VerticalPanel();
		vp.setSpacing(3);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		//cpGrid.setHeading("Lista Dipendenti.");
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setHeight((h-65));
		cpGrid.setWidth(w-250);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setButtonAlign(HorizontalAlignment.CENTER);  
		Resizable r=new Resizable(cpGrid);
		
		ToolBar tlbrOpzioni= new ToolBar();
		
		Date d= new Date();
		String data= d.toString();
		String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(data.substring(4, 7)));
		String anno= data.substring(data.length()-4, data.length());
		
		smplcmbxPm= new SimpleComboBox<String>();
		smplcmbxPm.setEmptyText("Project Manager..");
		smplcmbxPm.setName("pm");
		smplcmbxPm.setAllowBlank(true);
		smplcmbxPm.setTriggerAction(TriggerAction.ALL);
		smplcmbxPm.setEmptyText("Project Manager..");
		smplcmbxPm.setAllowBlank(false);
		getNomePM();
		
		smplcmbxAnno= new SimpleComboBox<String>();
		smplcmbxAnno.setWidth(80);
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setVisible(true);
		smplcmbxAnno.setAllowBlank(false);
		for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);
		}
		smplcmbxAnno.setSimpleValue(anno);
		
		smplcmbxMese= new SimpleComboBox<String>();
		smplcmbxMese.setWidth(100);
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		smplcmbxMese.setEmptyText("Mese..");
		smplcmbxMese.setVisible(true);
		smplcmbxMese.setAllowBlank(false);
		for(String l : DatiComboBox.getMese()){
			smplcmbxMese.add(l);
		}
		smplcmbxMese.setSimpleValue(mese);
		
		lblchbx= new Label();
		lblchbx.setText("Includi non fatturabili:");
		
		chbxIncludi= new CheckBox();
				
		btnAggiorna=new Button();
		btnAggiorna.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.reload()));
		btnAggiorna.setIconAlign(IconAlign.BOTTOM);
		btnAggiorna.setToolTip("Aggiorna");
		btnAggiorna.setSize(26, 26);
		btnAggiorna.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					
					status.setBusy("Please wait...");
				    status.show();
					
					String pm=smplcmbxPm.getRawValue().toString();
					String anno=smplcmbxAnno.getRawValue().toString();
					String mese=smplcmbxMese.getRawValue().toString();
					boolean includi=false;
					
					if(chbxIncludi.getValue())
						includi=true;
					
					if(pm.compareTo("Tutti")!=0)
						AdministrationService.Util.getInstance().getRiepilogoCostiSuCommesseFatturate(pm, mese, anno, includi, new AsyncCallback<List<RiepilogoCostiDipSuCommesseFatturateModel>>() {

							@Override
							public void onFailure(Throwable caught) {
								status.hide();
								Window.alert("Problema di connessione on getRiepilogoCostiSuCommesseFatturate()");
							}

							@Override
							public void onSuccess(
								List<RiepilogoCostiDipSuCommesseFatturateModel> result) {
								status.hide();
								if(result!=null)
									caricaDatiTabella(result);
								else
									Window.alert("Impossibile effettuare il caricamento dati!");
							}
						});
					else
						AdministrationService.Util.getInstance().riepilogoTotaleMarginiSuCommesse(mese, anno, new AsyncCallback<List<RiepilogoCostiDipSuCommesseFatturateModel>>() {

							@Override
							public void onFailure(Throwable caught) {
								status.hide();
								Window.alert("Errore di connessione on riepilogoTotaleMarginiSuCommesse()");
							}

							@Override
							public void onSuccess(
									List<RiepilogoCostiDipSuCommesseFatturateModel> result) {
								status.hide();
								if(result!=null)
									caricaDatiTabella(result);
								else
									Window.alert("Impossibile accedere ai dati sui margini!");
							}		
						});		
				}
		});	  
		
						
		btnMostraTotale=new Button();
		btnMostraTotale.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riep_comm()));
		btnMostraTotale.setIconAlign(IconAlign.BOTTOM);
		btnMostraTotale.setToolTip("Totale");
		btnMostraTotale.setSize(26, 26);
		btnMostraTotale.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					
					
				}
		});	  
		
		btnPrint=new Button();
		btnPrint.setEnabled(true);
		btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
		btnPrint.setIconAlign(IconAlign.TOP);
		btnPrint.setToolTip("Stampa");
		btnPrint.setSize(26, 26);
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				SessionManagementService.Util.getInstance().setDatiReportCostiCommesseFatturate("RIEP.COSTICOMMESSE", store.getModels(),
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
		
		store.groupBy("numeroCommessa");
		store.setSortField("dipendente");
		store.setSortDir(SortDir.ASC);	
		
		GroupingView summary = new GroupingView();  
		summary.setForceFit(false);  
		summary.setShowGroupedColumn(false);
				
		cm = new ColumnModel(createColumns());		
		
		//AggregationRowConfig<RiepilogoCostiDipSuCommesseFatturateModel> agrTotale= new AggregationRowCosti();		
		//cm.addAggregationRow(agrTotale);
		
		gridRiepilogo= new EditorGrid<RiepilogoCostiDipSuCommesseFatturateModel>(store, cm);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setColumnLines(true);
		gridRiepilogo.setStripeRows(true);
		gridRiepilogo.setItemId("grid");
		gridRiepilogo.setView(summary);  
	    gridRiepilogo.getView().setShowDirtyCells(false);
		
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setAction(url);
		fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
		fp.add(btnPrint);
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(fp);
		
		tlbrOpzioni.add(smplcmbxAnno);
		tlbrOpzioni.add(smplcmbxMese);
		tlbrOpzioni.add(smplcmbxPm);
		tlbrOpzioni.add(new SeparatorToolItem());
		tlbrOpzioni.add(lblchbx);
		tlbrOpzioni.add(chbxIncludi);
		tlbrOpzioni.add(new SeparatorToolItem());
		tlbrOpzioni.add(btnAggiorna);
		tlbrOpzioni.add(new SeparatorToolItem());
		tlbrOpzioni.add(cp);
		tlbrOpzioni.add(status);
		
		cpGrid.add(gridRiepilogo);
		cpGrid.setTopComponent(tlbrOpzioni);
		
		layoutContainer.add(cpGrid, new FitData(3, 3, 3, 3));
		
		add(layoutContainer);
	
	}
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		GridCellRenderer<RiepilogoCostiDipSuCommesseFatturateModel> renderer= new GridCellRenderer<RiepilogoCostiDipSuCommesseFatturateModel>() {
			
			@Override
			public Object render(RiepilogoCostiDipSuCommesseFatturateModel model,
					String property, ColumnData config, int rowIndex, int colIndex,
					ListStore<RiepilogoCostiDipSuCommesseFatturateModel> store,
					Grid<RiepilogoCostiDipSuCommesseFatturateModel> grid) {
				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				Float n=model.get(property);
				return num.format(n);
			}
		};
			
		
		GridCellRenderer<RiepilogoCostiDipSuCommesseFatturateModel> percRenderer= new GridCellRenderer<RiepilogoCostiDipSuCommesseFatturateModel>() {
			
			@Override
			public Object render(RiepilogoCostiDipSuCommesseFatturateModel model,
					String property, ColumnData config, int rowIndex, int colIndex,
					ListStore<RiepilogoCostiDipSuCommesseFatturateModel> store,
					Grid<RiepilogoCostiDipSuCommesseFatturateModel> grid) {
				
				final NumberFormat num= NumberFormat.getPercentFormat();			
				Float n=model.get(property);
				return num.format(n);
			}
		};
		
				
		SummaryColumnConfig<Double> column;
		
		/*column = new SummaryColumnConfig<Double>();
	    column.setId("pm");
	    column.setHeader("Project Manager");
	    column.setWidth(140);
	    configs.add(column);*/
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("numeroCommessa");
	    column.setHeader("Numero Commessa");
	    column.setWidth(140);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("estensione");
	    column.setHeader("Estensione");
	    column.setWidth(140);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("attivita");
	    column.setHeader("Attivita'");
	    column.setWidth(140);
	    column.setRenderer(new GridCellRenderer<RiepilogoCostiDipSuCommesseFatturateModel>() {
			@Override
			public Object render(RiepilogoCostiDipSuCommesseFatturateModel model,
					String property, ColumnData config, int rowIndex,int colIndex, ListStore<RiepilogoCostiDipSuCommesseFatturateModel> store,
					Grid<RiepilogoCostiDipSuCommesseFatturateModel> grid) {
				if (model != null) {
	            	String attivita=model.get(property);
	            	if((attivita.compareTo("TOTALE")==0)||(attivita.compareTo("TOTALE-PREC")==0))
	            		return "<span style='font-weight:bold; color:#139213'>" + model.get(property) + "</span>";
	            	else
	            		return "<span style='font-weight:normal; color:#000000'>" + model.get(property) + "</span>";
	            }
				return "";
			}
		});
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("dipendente");
	    column.setHeader("Dipendente");
	    column.setWidth(140);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("oreEseguite");
	    column.setHeader("Ore Eseguite");
	    column.setWidth(80);
	    //column.setSummaryRenderer(summaryRenderer);
	   // column.setSummaryType(SummaryType.SUM);  
	    //column.setRenderer(render);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("costoOrario");
	    column.setHeader("Costo Orario");
	    column.setWidth(80);
	    column.setRenderer(renderer);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("costoTotale");
	    column.setHeader("Costo Totale");
	    column.setWidth(100);
	    //column.setSummaryRenderer(summaryRenderer);
	    column.setRenderer(renderer);
	  //  column.setSummaryType(SummaryType.SUM);  
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("importoScaricato");
	    column.setHeader("euro/Scaricato");
	    column.setWidth(100);
	    column.setRenderer(renderer);
	  //  column.setSummaryRenderer(summaryRenderer);
	  //  column.setSummaryType(SummaryType.SUM);  
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("importoFatturato");
	    column.setHeader("euro/Fatturato");
	    column.setWidth(100);
	    column.setRenderer(renderer);
	  //  column.setSummaryRenderer(summaryRenderer);
	  //  column.setSummaryType(SummaryType.SUM);  
	    configs.add(column);	
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("margine");
	    column.setHeader("euro/Margine");
	    column.setWidth(100);
	    column.setRenderer(renderer);
	 //   column.setSummaryRenderer(summaryRenderer);
	   // column.setSummaryType(SummaryType.SUM);  
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("rapporto");
	    column.setHeader("Margine/Scaricate");
	    column.setWidth(60);
	    column.setRenderer(percRenderer);
	    
	 //   column.setSummaryRenderer(summaryRenderer);
	   // column.setSummaryType(SummaryType.SUM);  
	    configs.add(column);
		
	    column = new SummaryColumnConfig<Double>();
	    column.setId("importoBilancio");
	    column.setHeader("Bilancio");
	    column.setWidth(100);
	    column.setRenderer(renderer); 
	    configs.add(column);
	    
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("differenzaBilancioScaricato");
	    column.setHeader("Delta");
	    column.setWidth(100);
	    column.setRenderer(renderer);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("differenzaPerc");
	    column.setHeader("Delta %");
	    column.setWidth(60);
	    column.setRenderer(percRenderer);
	    configs.add(column);
	    
		return configs;
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
					smplcmbxPm.add(result);
					smplcmbxPm.add("Tutti");
					smplcmbxPm.recalculate();
					//smplcmbxPm.setSimpleValue("Tutti");
												
				}else 
					Window.alert("error: Errore durante l'accesso ai dati PM.");
			}
		});
	}
	
	
	private void caricaDatiTabella(List<RiepilogoCostiDipSuCommesseFatturateModel> result) {
		store.removeAll();
		store.setStoreSorter(new StoreSorter<RiepilogoCostiDipSuCommesseFatturateModel>());  
		store.setSortField("dipendente");
		store.setSortDir(SortDir.ASC);	
		store.add(result);
	}
	
	
	private class AggregationRowCosti extends AggregationRowConfig<RiepilogoCostiDipSuCommesseFatturateModel>{
		
		public AggregationRowCosti(){
			final NumberFormat number= NumberFormat.getFormat("#,##0.0#;-#");
			AggregationRenderer<RiepilogoCostiDipSuCommesseFatturateModel> aggrRender= new AggregationRenderer<RiepilogoCostiDipSuCommesseFatturateModel>() {		
				@Override
				public Object render(Number value, int colIndex, Grid<RiepilogoCostiDipSuCommesseFatturateModel> grid,
						ListStore<RiepilogoCostiDipSuCommesseFatturateModel> store) {
					
					List<Integer> indiciRigaTotale= new ArrayList<Integer>();
					
					for(RiepilogoCostiDipSuCommesseFatturateModel r:store.getModels()){
						String attivita=r.get("attivita");
						if(attivita.compareTo("TOTALE")==0)
							indiciRigaTotale.add(Integer.valueOf(store.indexOf(r)));
					}
					ColumnConfig columnFour = grid.getColumnModel().getColumn(colIndex);
					Float totale=(float)0.00;
					
					/*for(int i:indiciRigaTotale){
						RiepilogoCostiDipSuCommesseFatturateModel r1=store.getAt(i);
						totale=totale+r1.get();
					}*/
					
					
					if(value!=null)
			    		  return number.format(value.doubleValue());
			    	else
			    		  return number.format((float) 0) ;
				}
			};
			
			setHtml("attivita", "<p style=\"font-size:15px; color:#000000; font-weight:bold;\">TOTALE</p>");	
						
			setSummaryType("oreEseguite", SummaryType.SUM);
			//setSummaryFormat("importoComplessivo", NumberFormat.getCurrencyFormat("EUR"));
			setRenderer("oreEseguite", aggrRender);
						
			/*setSummaryType("costoOrario", SummaryType.SUM);
			setRenderer("costoOrario", aggrRender);
			setCellStyle("costoOrario", "font-size:15px; color:#000000; font-weight:bold;");
			setSummaryFormat("costoOrario", NumberFormat.getCurrencyFormat("EUR"));*/
									
			setSummaryType("costoTotale", SummaryType.SUM);
			setRenderer("costoTotale", aggrRender);
			setCellStyle("costoTotale", "font-size:15px; color:#000000; font-weight:bold;");
			setSummaryFormat("costoTotale", NumberFormat.getCurrencyFormat("EUR"));
			
			setSummaryType("importoScaricato", SummaryType.SUM);
			setRenderer("importoScaricato", aggrRender);
			setCellStyle("importoScaricato", "font-size:15px; color:#000000; font-weight:bold;");
			setSummaryFormat("importoScaricato", NumberFormat.getCurrencyFormat("EUR"));
			
			setSummaryType("importoFatturato", SummaryType.SUM);
			setRenderer("importoFatturato", aggrRender);
			setCellStyle("importoFatturato", "font-size:15px; color:#000000; font-weight:bold;");
			setSummaryFormat("importoFatturato", NumberFormat.getCurrencyFormat("EUR"));
			
			setSummaryType("margine", SummaryType.SUM);
			setRenderer("margine", aggrRender);
			setCellStyle("margine", "font-size:15px; color:#000000; font-weight:bold;");
			setSummaryFormat("margine", NumberFormat.getCurrencyFormat("EUR"));
						
		}
	}
}
