package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;
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
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
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
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.SummaryRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelRiepilogoOreCommesseDettDipendenti extends LayoutContainer{

	private GroupingStore<RiepilogoOreDipFatturazione>store = new GroupingStore<RiepilogoOreDipFatturazione>();
	private Grid<RiepilogoOreDipFatturazione> gridRiepilogo;
	private ColumnModel cm;
	
	private SimpleComboBox<String> smplcmbxMese;
	private SimpleComboBox<String> smplcmbxAnno;
	private SimpleComboBox<String> smplcmbxSede;
	private SimpleComboBox<String> smplcmbxOrderBy;
	private Button btnSelect;
	private Button btnShowDettaglioOre;
	private int idDip;
		
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	public PanelRiepilogoOreCommesseDettDipendenti(){
		
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
		
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.setBorders(false);
		cp.setFrame(true);
				
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setSize(w-230, h-65);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
	
		Resizable r=new Resizable(cpGrid);
		
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
		
		smplcmbxSede=new SimpleComboBox<String>();
		smplcmbxSede.setFieldLabel("Sede");
		smplcmbxSede.setName("sede");
		smplcmbxSede.setAllowBlank(false);
		smplcmbxSede.add("T");
		smplcmbxSede.add("B");
		smplcmbxSede.setTriggerAction(TriggerAction.ALL);
		smplcmbxSede.setSimpleValue("T");	
		smplcmbxSede.setWidth(70);
		
		smplcmbxOrderBy= new SimpleComboBox<String>();
		smplcmbxOrderBy.setFieldLabel("Group By");
		smplcmbxOrderBy.setEmptyText("Selezionare..");
		smplcmbxOrderBy.setAllowBlank(false);
		smplcmbxOrderBy.add("Dipendente");
		smplcmbxOrderBy.add("Commessa");
		smplcmbxOrderBy.setWidth(110);
		smplcmbxOrderBy.setTriggerAction(TriggerAction.ALL);
		smplcmbxOrderBy.setSimpleValue("PM");
		smplcmbxOrderBy.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				String raggruppamento=smplcmbxOrderBy.getRawValue().toString().toLowerCase();
				if(raggruppamento.compareTo("commessa")==0){
					store.groupBy("numeroCommessa",true);
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
					String sede=smplcmbxSede.getRawValue().toString();
					String data;
					meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
					data=meseRif+anno;
					caricaTabellaDati(data,sede);			
				}else Window.alert("Controllare i dati selezionati!");
			}			
		});			
		
		
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
		summary.setStartCollapsed(false);
		    		   		
		gridRiepilogo= new Grid<RiepilogoOreDipFatturazione>(store, cm);  
		gridRiepilogo.setItemId("grid");
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);  
		gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		gridRiepilogo.setView(summary);
		gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<RiepilogoOreDipFatturazione>>() {  
	          public void handleEvent(SelectionChangedEvent<RiepilogoOreDipFatturazione> be) {  
	        	
	            if (be.getSelection().size() > 0) {      
	            	//String commessa= new String();
	            	//commessa=be.getSelectedItem().getNumeroCommessa();
	            		
	            	idDip=be.getSelectedItem().get("idDip");
	            	//numCommessa=commessa.substring(0,commessa.indexOf("."));
	            	//numEstensione=commessa.substring(commessa.indexOf(".")+1, commessa.indexOf("(")-1);		
	            		            		               		            	
	            } else {  
	                
	           }
	         }
	    });
						
		ToolBar tlbrRiepilogoOre= new ToolBar();
			    
	    btnShowDettaglioOre= new Button();
	    btnShowDettaglioOre.setEnabled(false);
	    btnShowDettaglioOre.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
	    btnShowDettaglioOre.setToolTip("Riepilogo Mesi Precedenti");
	    btnShowDettaglioOre.setIconAlign(IconAlign.TOP);
	    btnShowDettaglioOre.setSize(26, 26);
	    btnShowDettaglioOre.addSelectionListener(new SelectionListener<ButtonEvent>() {			
			@Override
			public void componentSelected(ButtonEvent ce) {
				String meseRif= new String(); 
				String anno=smplcmbxAnno.getRawValue().toString();		
				meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
								
				Dialog d= new Dialog();
				d.setSize(550, 800);
				d.setButtons("");
				d.add(new PanelRiepilogoGiornalieroCommesse(idDip, anno, meseRif, "1")); //tipo indica da quale layout creo la classe
				d.show();
			}
		});
	    
	    tlbrRiepilogoOre.add(smplcmbxAnno);
	    tlbrRiepilogoOre.add(smplcmbxMese);
	    tlbrRiepilogoOre.add(smplcmbxSede);
	    tlbrRiepilogoOre.add(btnSelect);
	    tlbrRiepilogoOre.add(new SeparatorToolItem());
	    tlbrRiepilogoOre.add(btnShowDettaglioOre);
	    tlbrRiepilogoOre.add(new SeparatorToolItem());
	    tlbrRiepilogoOre.add(txtGroup);
	    tlbrRiepilogoOre.add(smplcmbxOrderBy);
	    
	    //tlbrOrderBy.add(smplcmbxOrderBy);
	   	    
   	   /* cp.add(cpGrid);
   	    cp.setTopComponent(tlbrRiepilogoOre);
   	    */
   	    cpGrid.add(gridRiepilogo);
	    cpGrid.setTopComponent(tlbrRiepilogoOre);
   	    
   	    layoutContainer.add(cpGrid, new FitData(3,3,3,3));
		add(layoutContainer);	
	}
	
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number = NumberFormat.getFormat("0.00");
		
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
	    configs.add(column); 
	    	    
	    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
	    columnOreLavoro.setId("oreLavoro");  
	    columnOreLavoro.setHeader("Ore Lavoro");  
	    columnOreLavoro.setWidth(63);    
	    columnOreLavoro.setRowHeader(true); 
	    columnOreLavoro.setAlignment(HorizontalAlignment.LEFT);
	    columnOreLavoro.setSummaryType(SummaryType.SUM); 
	    columnOreLavoro.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
			@Override
			public Object render(RiepilogoOreDipFatturazione model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreDipFatturazione> store,
					Grid<RiepilogoOreDipFatturazione> grid) {
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
	    
	    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
	    columnOreViaggio.setId("oreViaggio");  
	    columnOreViaggio.setHeader("Ore Viaggio");  
	    columnOreViaggio.setWidth(63);    
	    columnOreViaggio.setRowHeader(true); 
	    columnOreViaggio.setAlignment(HorizontalAlignment.LEFT);
	    columnOreViaggio.setSummaryType(SummaryType.SUM); 
	    columnOreViaggio.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
			@Override
			public Object render(RiepilogoOreDipFatturazione model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreDipFatturazione> store,
					Grid<RiepilogoOreDipFatturazione> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});   
	    columnOreViaggio.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}  
	    });  
	    configs.add(columnOreViaggio); 
	    
	    SummaryColumnConfig<Double> columnOreTotali=new SummaryColumnConfig<Double>();		
	    columnOreTotali.setId("oreTotali");  
	    columnOreTotali.setHeader("Totale Commesse");  
	    columnOreTotali.setWidth(63);    
	    columnOreTotali.setRowHeader(true); 
	    columnOreTotali.setAlignment(HorizontalAlignment.LEFT);  
	    columnOreTotali.setSummaryType(SummaryType.SUM); 
	    columnOreTotali.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
			@Override
			public Object render(RiepilogoOreDipFatturazione model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreDipFatturazione> store,
					Grid<RiepilogoOreDipFatturazione> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});    
	    columnOreTotali.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}  
	    });  
	    configs.add(columnOreTotali); 
	    /*
	    SummaryColumnConfig<Double> columnOreTotaliIU=new SummaryColumnConfig<Double>();		
	    columnOreTotaliIU.setId("oreTotaliIU");  
	    columnOreTotaliIU.setHeader("Totale Intervalli I/U");  
	    columnOreTotaliIU.setWidth(63);    
	    columnOreTotaliIU.setRowHeader(true); 
	    columnOreTotaliIU.setAlignment(HorizontalAlignment.LEFT);  
	    columnOreTotaliIU.setSummaryType(SummaryType.SUM); 
	    columnOreTotaliIU.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
			@Override
			public Object render(RiepilogoOreDipFatturazione model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreDipFatturazione> store,
					Grid<RiepilogoOreDipFatturazione> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});    
	    columnOreTotaliIU.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}  
	    });  
	    configs.add(columnOreTotaliIU);*/
	    
		return configs;
	}

	
	private void caricaTabellaDati(String data, String sede) {
			
		AdministrationService.Util.getInstance().getRiepilogoOreCommesseDettDipendenti(data, sede, new AsyncCallback<List<RiepilogoOreDipFatturazione>>() {	
			@Override
			public void onSuccess(List<RiepilogoOreDipFatturazione> result) {
				if(result==null)
					Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
				else	
					if(result.size()==0){
						Window.alert("Nessun dato (ore lavoro) rilevato in base ai criteri di ricerca selezionati.");
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
		
	private void loadTable(List<RiepilogoOreDipFatturazione> result) {
		
		try {
			btnShowDettaglioOre.enable();
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

