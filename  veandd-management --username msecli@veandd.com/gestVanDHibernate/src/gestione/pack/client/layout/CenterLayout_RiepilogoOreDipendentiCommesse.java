package gestione.pack.client.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RiepilogoOreDipCommesse;
import gestione.pack.client.model.RiepilogoOreTotaliCommesse;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
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
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;

public class CenterLayout_RiepilogoOreDipendentiCommesse extends LayoutContainer{

	public CenterLayout_RiepilogoOreDipendentiCommesse(){}
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();

	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);

		final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		layoutContainer.setWidth(w-225);
		layoutContainer.setHeight(h-54);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);		
		
		HorizontalPanel hpLayout= new HorizontalPanel();	
		hpLayout.add(new CntpnlRiepilogoMese());
		hpLayout.add(new CntpnlRiepilogoTotale());
			  	
	    bodyContainer.add(hpLayout);    
	   				
		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);
	}
			
	
	private class CntpnlRiepilogoMese extends ContentPanel{
		
		private SimpleComboBox<String> smplcmbxMese= new SimpleComboBox<String>();
		private SimpleComboBox<String> smplcmbxAnno;
		private SimpleComboBox<String> smplcmbxPM=new SimpleComboBox<String>();
		private GroupingStore<RiepilogoOreDipCommesse>store = new GroupingStore<RiepilogoOreDipCommesse>();
		private EditorGrid<RiepilogoOreDipCommesse> gridRiepilogo;
		private ColumnModel cm;
		
		private Button btnSelect;
		
		CntpnlRiepilogoMese(){
			
			setHeading("Riepilogo Ore (Mensile).");
			setHeaderVisible(true);
			setCollapsible(false);
			setBorders(false);
			setScrollMode(Scroll.NONE);	
			setWidth(500);
			setFrame(true);
			//cntpnlRiepilogo.setHeight(h-55);
			setScrollMode(Scroll.AUTOY);
			
			smplcmbxMese= new SimpleComboBox<String>();
			smplcmbxMese.setWidth(110);
			smplcmbxMese.setFieldLabel("Mese");
			smplcmbxMese.setName("mese");
			smplcmbxMese.setEmptyText("Mese..");
			smplcmbxMese.setAllowBlank(false);
			 for(String l : DatiComboBox.getMese()){
				 smplcmbxMese.add(l);}
			smplcmbxMese.setTriggerAction(TriggerAction.ALL);
			
			smplcmbxAnno= new SimpleComboBox<String>();
			smplcmbxAnno.setWidth(75);
			smplcmbxAnno.setFieldLabel("Anno");
			smplcmbxAnno.setName("anno");
			smplcmbxAnno.setEmptyText("Anno..");
			smplcmbxAnno.setAllowBlank(false);
			 for(String l : DatiComboBox.getAnno()){
				 smplcmbxAnno.add(l);}
			smplcmbxAnno.setSimpleValue("2013");
			smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
			
			smplcmbxPM = new SimpleComboBox<String>();
			smplcmbxPM.setFieldLabel("Project Manager");
			smplcmbxPM.setName("pm");
			smplcmbxPM.setAllowBlank(true);
			smplcmbxPM.setTriggerAction(TriggerAction.ALL);
			smplcmbxPM.setEmptyText("Project Manager..");
			smplcmbxPM.setAllowBlank(false);
			getNomePM();		
			
			btnSelect= new Button("OK");
			btnSelect.addSelectionListener(new SelectionListener<ButtonEvent>() {		
				@Override
				public void componentSelected(ButtonEvent ce) {		
					if(smplcmbxMese.isValid()&&smplcmbxPM.isValid()&&smplcmbxAnno.isValid()){
						String meseRif= new String(); 
						String anno=smplcmbxAnno.getRawValue().toString();
						String data= new String();
						meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
						data=meseRif+anno;
						caricaTabellaDati(data, smplcmbxPM.getRawValue().toString());			
					}else Window.alert("Controllare i dati selezionati!");
				}
			});	
			
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			horizontalPanel.setSpacing(5);
			horizontalPanel.add(smplcmbxAnno);
			horizontalPanel.add(smplcmbxMese);
			horizontalPanel.add(smplcmbxPM);
			horizontalPanel.add(btnSelect);
			setTopComponent(horizontalPanel);
			
			//caricaTabellaDati(meseCorrente);
		    try {
		    	cm = new ColumnModel(createColumns());	
			} catch (Exception e) {
				e.printStackTrace();
				Window.alert("error: Problema createColumns().");			
			}	

			store.groupBy("numeroCommessa");
			    
		    GroupSummaryView summary = new GroupSummaryView();  
		    summary.setForceFit(true);  
		    summary.setShowGroupedColumn(false);  
			    
		    gridRiepilogo= new EditorGrid<RiepilogoOreDipCommesse>(store, cm);  
		    gridRiepilogo.setBorders(false);  
		    gridRiepilogo.setView(summary);  
		    gridRiepilogo.getView().setShowDirtyCells(false);	 	    
		    
		    ContentPanel cntpnlGrid= new ContentPanel();
		    cntpnlGrid.setBodyBorder(false);  
		    cntpnlGrid.setBorders(false);
		    cntpnlGrid.setFrame(true);
		    cntpnlGrid.setLayout(new FitLayout());  
		    cntpnlGrid.setHeaderVisible(false);
		    cntpnlGrid.setWidth(480);
		    cntpnlGrid.setHeight(500);
		    cntpnlGrid.setScrollMode(Scroll.AUTOY);
		    cntpnlGrid.add(gridRiepilogo);
		   	    
		    VerticalPanel vp= new VerticalPanel();
		    vp.setBorders(false);
		  	vp.add(cntpnlGrid);
		  	add(vp);			
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
						//smplcmbxPM.add("Tutti");
						smplcmbxPM.recalculate();
					}else Window.alert("error: Errore durante l'accesso ai dati PM.");			
				}
			});		
		}


		private List<ColumnConfig> createColumns() {
			List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			final NumberFormat number= NumberFormat.getFormat("0.00");
			
			SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
		    column.setId("numeroCommessa");  
		    column.setHeader("Commessa");  
		    column.setWidth(140);  
		    column.setRowHeader(true);  
		    configs.add(column); 
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("dipendente");  
			column.setHeader("Dipendente");  
			column.setWidth(120);  
			column.setRowHeader(true); 
			column.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
				return "Totale Ore:";
	   			}  
			});  
		    configs.add(column); 
		    	    
		    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
		    columnOreLavoro.setId("oreLavoro");  
		    columnOreLavoro.setHeader("Ore Lavoro");  
		    columnOreLavoro.setWidth(80);    
		    columnOreLavoro.setRowHeader(true); 
		    columnOreLavoro.setSummaryType(SummaryType.SUM);  
		    columnOreLavoro.setAlignment(HorizontalAlignment.LEFT);  	
		    columnOreLavoro.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesse>() {
				@Override
				public Object render(RiepilogoOreDipCommesse model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreDipCommesse> store,
						Grid<RiepilogoOreDipCommesse> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    columnOreLavoro.setSummaryRenderer(new SummaryRenderer() {  
		   			@Override
				public String render(Number value, Map<String, Number> data) {
		   			/*	GroupingStore<RiepilogoOreDipCommesse>store1 = new GroupingStore<RiepilogoOreDipCommesse>();
		   				String tot="0.00";
		   				store1.add(store.getModels());
		   				for(RiepilogoOreDipCommesse riep: store1.getModels()){
		   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreLavoro()));
		   				}
		   				
		   				Float n=Float.valueOf(tot);*/
						return number.format(value);
				}  
		      });  
		    configs.add(columnOreLavoro); 	
		    
		    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
		    columnOreViaggio.setId("oreViaggio");  
		    columnOreViaggio.setHeader("Ore Viaggio");  
		    columnOreViaggio.setWidth(80);    
		    columnOreViaggio.setRowHeader(true); 
		    columnOreViaggio.setSummaryType(SummaryType.SUM);  
		    columnOreViaggio.setAlignment(HorizontalAlignment.LEFT);    
		    columnOreViaggio.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesse>() {
				@Override
				public Object render(RiepilogoOreDipCommesse model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreDipCommesse> store,
						Grid<RiepilogoOreDipCommesse> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    columnOreViaggio.setSummaryRenderer(new SummaryRenderer() {  
		   			@Override
				public String render(Number value, Map<String, Number> data) {
		   				/*GroupingStore<RiepilogoOreDipCommesse>store1 = new GroupingStore<RiepilogoOreDipCommesse>();
		   				String tot="0.00";
		   				store1.add(store.getModels());
		   				for(RiepilogoOreDipCommesse riep: store1.getModels()){
		   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreViaggio()));
		   				}
		   				
		   				Float n=Float.valueOf(tot);*/
						return number.format(value);
				}  
		      });      
		    configs.add(columnOreViaggio); 	
		    
		    SummaryColumnConfig<Double> columnOreTotali=new SummaryColumnConfig<Double>();		
		    columnOreTotali.setId("totOre");  
		    columnOreTotali.setHeader("Totale");  
		    columnOreTotali.setWidth(85);    
		    columnOreTotali.setRowHeader(true); 
		    columnOreTotali.setSummaryType(SummaryType.SUM);  
		    columnOreTotali.setAlignment(HorizontalAlignment.LEFT);   
		    columnOreTotali.setStyle("color:#e71d2b;");
		    columnOreTotali.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesse>() {
				@Override
				public Object render(RiepilogoOreDipCommesse model,
						String property, ColumnData config, int rowIndex,
						int colIndex,
						ListStore<RiepilogoOreDipCommesse> store,
						Grid<RiepilogoOreDipCommesse> grid) {
					Float n=model.get(property);
					return number.format(n);
				}			
			});
		    columnOreTotali.setSummaryRenderer(new SummaryRenderer() {  
		   			@Override
				public String render(Number value, Map<String, Number> data) {
		   				/*GroupingStore<RiepilogoOreDipCommesse>store1 = new GroupingStore<RiepilogoOreDipCommesse>();
		   				String tot="0.00";
		   				store1.add(store.getModels());
		   				for(RiepilogoOreDipCommesse riep: store1.getModels()){
		   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getTotOre()));
		   				}
		   				
		   				Float n=Float.valueOf(tot);*/
						return number.format(value);
				}  
		    });      
		    configs.add(columnOreTotali); 
			
			return configs;
		}

		
		private void caricaTabellaDati(String mese, String pm) {
			AdministrationService.Util.getInstance().getRiepilogoOreDipCommesse(mese, pm, new AsyncCallback<List<RiepilogoOreDipCommesse>>() {		
				@Override
				public void onSuccess(List<RiepilogoOreDipCommesse> result) {
					if(result==null)
						Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
					else	
						if(result.size()==0)
							Window.alert("Nessun dato rilevato in base ai criteri di ricerca selezionati.");
						loadTable(result);			
				}
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getRiepilogoOreDipCommesse();");
					caught.printStackTrace();		
				}
			}); //AsyncCallback	   
		}
		
		
		private void loadTable(List<RiepilogoOreDipCommesse> result) {
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
	}
	
	
	private class CntpnlRiepilogoTotale extends ContentPanel{
		
		private SimpleComboBox<String> smplcmbxPM= new SimpleComboBox<String>();
		private Button btnSelect;
		private GroupingStore<RiepilogoOreTotaliCommesse>store = new GroupingStore<RiepilogoOreTotaliCommesse>();
		private EditorGrid<RiepilogoOreTotaliCommesse> gridRiepilogo;
		private ColumnModel cm;	
		CntpnlRiepilogoTotale(){
			
			setHeading("Riepilogo Ore Totali.");
			setHeaderVisible(true);
			setCollapsible(false);
			setBorders(false);
			setScrollMode(Scroll.NONE);	
			setWidth(650);
			setFrame(true);
			setScrollMode(Scroll.AUTOY);
			setStyleAttribute("padding-left", "20px");
			
			smplcmbxPM = new SimpleComboBox<String>();
			smplcmbxPM.setFieldLabel("Project Manager");
			smplcmbxPM.setName("pm");
			smplcmbxPM.setAllowBlank(true);
			smplcmbxPM.setTriggerAction(TriggerAction.ALL);
			smplcmbxPM.setEmptyText("Project Manager..");
			smplcmbxPM.setAllowBlank(false);
			getNomePM();		
			
			btnSelect= new Button("OK");
			btnSelect.addSelectionListener(new SelectionListener<ButtonEvent>() {		
				@Override
				public void componentSelected(ButtonEvent ce) {		
					if(smplcmbxPM.isValid()){			
						caricaTabellaDatiRiepilogoTotale(smplcmbxPM.getRawValue().toString());			
					}else Window.alert("Controllare i dati selezionati!");
				}
			});	
			
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			horizontalPanel.setSpacing(5);
			horizontalPanel.add(smplcmbxPM);
			horizontalPanel.add(btnSelect);
			
			setTopComponent(horizontalPanel);
			
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
				    
			gridRiepilogo= new EditorGrid<RiepilogoOreTotaliCommesse>(store, cm);  
			gridRiepilogo.setBorders(false);  
			gridRiepilogo.setView(summary);  
			gridRiepilogo.getView().setShowDirtyCells(false);	    
			    
			ContentPanel cntpnlGrid= new ContentPanel();
			cntpnlGrid.setBodyBorder(false);  
			cntpnlGrid.setBorders(false);
			cntpnlGrid.setFrame(true);
			cntpnlGrid.setLayout(new FitLayout());  
			cntpnlGrid.setHeaderVisible(false);
			cntpnlGrid.setWidth(610);
			cntpnlGrid.setHeight(500);
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
		    column.setId("estensione");  
			column.setHeader("Estensione");  
			column.setWidth(55);  
			column.setRowHeader(true); 
		    configs.add(column); 
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("numeroOrdine");  
			column.setHeader("Ordine");  
			column.setWidth(80);  
			column.setRowHeader(true); 
		    configs.add(column);
		    	    
		    SummaryColumnConfig<Double> columnOreOrdine=new SummaryColumnConfig<Double>();		
		    columnOreOrdine.setId("oreOrdine");  
		    columnOreOrdine.setHeader("Ore Ordine");  
		    columnOreOrdine.setWidth(55);    
		    columnOreOrdine.setRowHeader(true); 
		    columnOreOrdine.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(columnOreOrdine); 	
		    
		    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
		    columnOreLavoro.setId("oreLavoro");  
		    columnOreLavoro.setHeader("Ore Lavoro");  
		    columnOreLavoro.setWidth(55);    
		    columnOreLavoro.setRowHeader(true);
		    columnOreLavoro.setAlignment(HorizontalAlignment.RIGHT);
		    columnOreLavoro.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {
				@Override
				public Object render(RiepilogoOreTotaliCommesse model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreTotaliCommesse> store,
						Grid<RiepilogoOreTotaliCommesse> grid) {
					Float n=model.get(property);
					return number.format(n);
				}  	
			});
		    configs.add(columnOreLavoro); 		
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("differenza");  
			column.setHeader("Residuo Ore");  
			column.setWidth(55);  
			column.setRowHeader(true); 
			column.setAlignment(HorizontalAlignment.RIGHT);
			column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {
				@Override
				public Object render(RiepilogoOreTotaliCommesse model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreTotaliCommesse> store,
						Grid<RiepilogoOreTotaliCommesse> grid) {
					String diff=new String();
					Float n=model.getOreLavoro();			
					diff=ClientUtility.calcoloDelta(model.getOreOrdine(), number.format(n));	
					return diff;
				}  	
			});
		    configs.add(column);
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("completamento");  
			column.setHeader("Compl.(%)");  
			column.setWidth(45);  
			column.setRowHeader(true); 
			column.setAlignment(HorizontalAlignment.RIGHT);
			column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {
				@Override
				public Object render(RiepilogoOreTotaliCommesse model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreTotaliCommesse> store,
						Grid<RiepilogoOreTotaliCommesse> grid) {
					float completamento=0;
					String compl= new String();
					completamento=(model.getOreLavoro()/Float.valueOf(model.getOreOrdine())*100);
					compl=number.format(completamento);					
					return (compl+"%");
				}  	
			});
		    configs.add(column);
			
			return configs;
		}
		
		
		private void caricaTabellaDatiRiepilogoTotale(String pm) {
			AdministrationService.Util.getInstance().getRiepilogoOreTotCommesse(pm, "", new AsyncCallback<List<RiepilogoOreTotaliCommesse>>() {
				
				@Override
				public void onSuccess(List<RiepilogoOreTotaliCommesse> result) {
					if(result==null)
						Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
					else	
						if(result.size()==0)
							Window.alert("Nessun dato rilevato in base ai criteri di ricerca selezionati.");
						loadTable(result);			
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getRiepilogoOreDipCommesse();");
					caught.printStackTrace();		
				}
			}); //AsyncCallback	   
		}
		
		private void loadTable(List<RiepilogoOreTotaliCommesse> result) {
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
						//smplcmbxPM.add("Tutti");
						smplcmbxPM.recalculate();
					}else Window.alert("error: Errore durante l'accesso ai dati PM.");			
				}
			});		
		}
	}	
}

