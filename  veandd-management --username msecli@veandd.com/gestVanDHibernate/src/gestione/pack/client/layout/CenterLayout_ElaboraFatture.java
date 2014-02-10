package gestione.pack.client.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.layout.panel.PanelFormInserimentoDatiFattura;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.model.FatturaModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
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
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
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


public class CenterLayout_ElaboraFatture extends LayoutContainer{

	public CenterLayout_ElaboraFatture(){}
	
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
		private SimpleComboBox<String> smplcmbxOrderBy;
		//private SimpleComboBox<String> smplcmbxPM;
		private GroupingStore<DatiFatturazioneMeseModel>store = new GroupingStore<DatiFatturazioneMeseModel>();
		private EditorGrid<DatiFatturazioneMeseModel> gridRiepilogo;
		private ColumnModel cm;
		private GridSelectionModel<DatiFatturazioneMeseModel> sm = new CheckBoxSelectionModel<DatiFatturazioneMeseModel>();
		
		private Button btnSelect;
		private Button btnPrint;
		private Button btnShowFormFatturazione;
		//private Button btnRiepDatiFatt;
		
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
					
					SessionManagementService.Util.getInstance().setDataReportDatiFatturazioneInSession(anno, mese, null, "RIEP.FATT",
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
			
			btnShowFormFatturazione=new Button();
			btnShowFormFatturazione.setSize("55px","25px");	   
			btnShowFormFatturazione.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
			btnShowFormFatturazione.setToolTip("Elabora Fattura");
			btnShowFormFatturazione.setIconAlign(IconAlign.TOP);
			btnShowFormFatturazione.setSize(26, 26);
			btnShowFormFatturazione.disable();
			btnShowFormFatturazione.addSelectionListener(new SelectionListener<ButtonEvent>() {				
				@Override
				public void componentSelected(ButtonEvent ce) {									
					//numeroOrdine, numeroCommessa, oggetto, importoEffettivo, idFoglioFatturazione
					//cerco i dati relativi all'ordine passato e restituisco un form per completare i dati di fatturazione
					final String numeroOrdine=sm.getSelectedItem().get("numeroOrdine");
					final int idFoglioFatturazione=sm.getSelectedItem().get("idFoglioFatturazione");
					//final String descrizione=sm.getSelectedItem().get("oggettoAttivita");
					AdministrationService.Util.getInstance().elaboraDatiPerFattura(numeroOrdine, idFoglioFatturazione, new AsyncCallback<FatturaModel>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Error on elaboraDatiPerFattura()");
						}

						@Override
						public void onSuccess(FatturaModel result) {
							if(result!=null){
								showDialog(numeroOrdine, idFoglioFatturazione, result);						
							}
							else
								Window.alert("Problemi durante il recupero dei dati per la compilazione della fattura!");
						}
					});
					
				}
			});
					
			fp.setMethod(FormPanel.METHOD_POST);
			fp.setAction(url);
			//fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
			fp.add(btnPrint);
			ContentPanel cp= new ContentPanel();
			cp.setHeaderVisible(false);
			cp.add(fp);
						
			ToolBar tlbOperazioni= new ToolBar();
			tlbOperazioni.add(smplcmbxAnno);
			tlbOperazioni.add(smplcmbxMese);
			tlbOperazioni.add(btnSelect);
			tlbOperazioni.add(new SeparatorToolItem());
			tlbOperazioni.add(cp);
			tlbOperazioni.add(new SeparatorToolItem());
			tlbOperazioni.add(btnShowFormFatturazione);
			tlbOperazioni.add(new SeparatorToolItem());
			tlbOperazioni.add(txtGroup);
			tlbOperazioni.add(smplcmbxOrderBy);
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
		    gridRiepilogo.setColumnLines(true);
		    gridRiepilogo.setStripeRows(true);
		    gridRiepilogo.setSelectionModel(sm);
			gridRiepilogo.setView(summary);  
			gridRiepilogo.getView().setShowDirtyCells(false);
			gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<DatiFatturazioneMeseModel>>() {  
		          public void handleEvent(SelectionChangedEvent<DatiFatturazioneMeseModel> be) {  	        	
			            if (be.getSelection().size() > 0) { 
			            	   btnShowFormFatturazione.enable();
			            } else{
			            	btnShowFormFatturazione.disable();
			            }
			            	
			          }		          
			}); 		
			gridRiepilogo.addListener(Events.CellDoubleClick, new Listener<BaseEvent>() {

				@Override
				public void handleEvent(BaseEvent be) {
					final String numeroOrdine=sm.getSelectedItem().get("numeroOrdine");
					final int idFoglioFatturazione=sm.getSelectedItem().get("idFoglioFatturazione");
					AdministrationService.Util.getInstance().elaboraDatiPerFattura(numeroOrdine, idFoglioFatturazione, new AsyncCallback<FatturaModel>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Error on elaboraDatiPerFattura()");						
						}

						@Override
						public void onSuccess(FatturaModel result) {
							if(result!=null){									
								showDialog(numeroOrdine, idFoglioFatturazione, result);
							}
							else
								Window.alert("Problemi durante il recupero dei dati per la compilazione della fattura!");
						}
					});
				}
			});
		    			   		   	    	   
		  	add(gridRiepilogo);			
		}
		
	
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
					//Float n=value;
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
		     columnImportoEffettivo.setStyle("color:#e71d2b; background-color:#f0f6f6;");
		     //e1e3e1  d2f5af
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
						//Float n=value;
						return num.format(value);
					}
				});
			configs.add(columnImportoEffettivo); 
	
			column=new SummaryColumnConfig<Double>();		
			column.setId("statoFattura"); 
			column.setToolTip("Stato Fatturazione");
			column.setHeader("Stato Fatturazione");  
			column.setWidth(50);  
			column.setRowHeader(true); 
			column.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,
						String property, ColumnData config, int rowIndex,
						int colIndex,
						ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {
					
					String t= model.get("statoFattura");
					if(t.compareTo("S")==0)												
							config.style = config.style + ";background-color:" + "#90EE90" + ";";									
						else
							config.style = config.style + ";background-color:" + "#F08080" + ";";
					
					return "";
				}
			});
			configs.add(column);
			
		    return configs;
		}

		
		private void showDialog(String numeroOrdine, int idFoglioFatturazione, FatturaModel result){
			PanelFormInserimentoDatiFattura dp= new PanelFormInserimentoDatiFattura(numeroOrdine, idFoglioFatturazione, result);
			dp.setSize(630, 440);
			dp.setButtons("");
			dp.setHeading("Dati per la fatturazione.");
			dp.setConstrain(false);
			dp.show();
			
			dp.addListener(Events.Hide, new Listener<ComponentEvent>() {
			     
				@Override
				public void handleEvent(ComponentEvent be) {
					
					String meseRif= new String();
					String anno= smplcmbxAnno.getRawValue().toString();
					String data;
					meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
					data=meseRif+anno;
					
					sm.getSelectedItem().set("statoFattura", "S");
					gridRiepilogo.reconfigure(store, cm);
			    }
			});	
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
				for(DatiFatturazioneMeseModel d: result)
					if((Float)d.get("importo")!=0)
						store.add(d);
				store.groupBy("pm");
				gridRiepilogo.reconfigure(store, cm);	    		    	
			} catch (NullPointerException e) {
				Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
					e.printStackTrace();
			}
		}	
	}	
}


