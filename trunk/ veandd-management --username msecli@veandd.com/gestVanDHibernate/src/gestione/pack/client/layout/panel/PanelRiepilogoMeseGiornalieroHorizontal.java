package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RiepilogoMeseGiornalieroModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelRiepilogoMeseGiornalieroHorizontal extends LayoutContainer{

	private ListStore<RiepilogoMeseGiornalieroModel>store = new ListStore<RiepilogoMeseGiornalieroModel>();
	private Grid<RiepilogoMeseGiornalieroModel> gridRiepilogo;
	private ColumnModel cmRiepilogo;
	private CellSelectionModel<RiepilogoMeseGiornalieroModel> cm=new CellSelectionModel<RiepilogoMeseGiornalieroModel>();
	
	private Button btnViewFoglioOre;
	private Button btnSelect;
	private SimpleComboBox<String> smplcmbxAnno;
	private SimpleComboBox<String> smplcmbxMese;
	private SimpleComboBox<String> smplcmbxSede;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	public PanelRiepilogoMeseGiornalieroHorizontal(){
		
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
		//layoutContainer.setSize(1200, 900);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setSize(w-225, h-65);
		cpGrid.setScrollMode(Scroll.AUTOY);
		cpGrid.setLayout(new FitLayout());
		
		Resizable r=new Resizable(cpGrid);
	
		ToolBar tlbrOperazioni= new ToolBar();
		cpGrid.setTopComponent(tlbrOperazioni);
		
		Date d= new Date();
		String dt= d.toString();
		String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(dt.substring(4, 7)));
		String anno= dt.substring(dt.length()-4, dt.length());
		
		smplcmbxAnno= new SimpleComboBox<String>();
		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setAllowBlank(false);
		for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);}
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setSimpleValue(anno);
		smplcmbxAnno.setWidth(80);
		tlbrOperazioni.add(smplcmbxAnno);
		   
		smplcmbxMese= new SimpleComboBox<String>();
		smplcmbxMese.setFieldLabel("Mese");
		smplcmbxMese.setName("mese");
		smplcmbxMese.setEmptyText("Mese..");
		smplcmbxMese.setAllowBlank(false);
		for(String l : DatiComboBox.getMese()){
			 smplcmbxMese.add(l);}
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		smplcmbxMese.setSimpleValue(mese);
		smplcmbxMese.setWidth(80);
		tlbrOperazioni.add(smplcmbxMese);
		
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
				caricaTabella();
				
			}
		});
		tlbrOperazioni.add(btnSelect);
			
		btnViewFoglioOre= new Button();
		btnViewFoglioOre.setStyleAttribute("padding-left", "2px");
	  	btnViewFoglioOre.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
	  	btnViewFoglioOre.setIconAlign(IconAlign.TOP);
	  	btnViewFoglioOre.setToolTip("Modifica dati.");
	  	btnViewFoglioOre.setSize(26, 26);
	  	btnViewFoglioOre.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
						
				if(smplcmbxMese.isValid()&&smplcmbxAnno.isValid()){			
								    	
			    	int indiceColonna;
					String dipendente= new String();
					String meseRif=ClientUtility.traduciMeseToNumber(ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString()).toLowerCase());
					String anno= smplcmbxAnno.getRawValue().toString();
					indiceColonna=cm.getSelectCell().cell;
					dipendente=cm.getSelectedItem().get("username");
					
					Date retVal = null;
				      try
				        {
				            retVal = DateTimeFormat.getFormat( "dd-MM-yyyy" ).parse( indiceColonna+"-"+meseRif+"-"+anno );
				        }
				      catch ( Exception e )
				        {
				            retVal = null;
				        }
					
					
					Dialog d =new  DialogRilevazionePresenze(retVal,dipendente);
					d.show();
								
					d.addListener(Events.Hide, new Listener<ComponentEvent>() {			     
						@Override
						public void handleEvent(ComponentEvent be) {
								
						}
					});	
				}
			}
		});
		tlbrOperazioni.add(btnViewFoglioOre);
	  	
	  	
	  	try {	    	
			cmRiepilogo = new ColumnModel(createColumns()); 
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	  	
		GridView view= new GridView();
		view.setAutoFill(false);
		view.setShowDirtyCells(false);
		view.setForceFit(false);	
		view.setAutoFill(false);
		
		store.setStoreSorter(new StoreSorter<RiepilogoMeseGiornalieroModel>()); 
		store.setDefaultSort("dipendente", SortDir.ASC);
		
		gridRiepilogo= new Grid<RiepilogoMeseGiornalieroModel>(store, cmRiepilogo);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);
		gridRiepilogo.setSelectionModel(cm);
		gridRiepilogo.setView(view);	
		//gridRiepilogo.setSize(980, 830);
							
		cpGrid.add(gridRiepilogo);
				
		//caricaTabella();
		
		layoutContainer.add(cpGrid, new FitData(3,3,3,3));
		add(layoutContainer);
	}
	

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column=new ColumnConfig();
		column.setId("dipendente");  
		column.setHeader("Dipendente");  
		column.setWidth(150);	
		column.setResizable(false);
		configs.add(column); 
		
		for(int i=1;i<=31;i++){
		
			
		column=new ColumnConfig();
		column.setId("giorno"+i);  
		column.setHeader(String.valueOf(i));  
		column.setWidth(27); 
		column.setResizable(false);
		column.setRenderer(new GridCellRenderer<RiepilogoMeseGiornalieroModel>() {
				@Override
				public Object render(RiepilogoMeseGiornalieroModel model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<RiepilogoMeseGiornalieroModel> store, Grid<RiepilogoMeseGiornalieroModel> grid) {
					
					String color = "#90EE90";
					String grey="#DDDDDD";
					String stato=model.get(property);
					
					if(stato.compareTo("2")==0)
						config.style = config.style + ";background-color:" + grey + ";";
					else
					if(stato.compareTo("0")==0)
					{
						config.style = config.style + ";background-color:" + color + ";";									
					}
					else{
						color = "#F08080";  
						config.style = config.style + ";background-color:" + color + ";";
					}
					return "";
				}
			});
		configs.add(column);
		}/*
		
		column=new ColumnConfig();
		column.setId("giorno2");  
		column.setHeader("2");  
		column.setWidth(27); 		
		column.setResizable(false);
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno3");  
		column.setHeader("3");  
		column.setWidth(27); 	
		column.setResizable(false);
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno4");  
		column.setHeader("4");  
		column.setWidth(27); 		
		column.setResizable(false);
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno5");  
		column.setHeader("5");  
		column.setWidth(27);  
		column.setResizable(false);
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno6");  
		column.setHeader("6");  
		column.setWidth(27);   
		column.setResizable(false);
		configs.add(column);
			
		column=new ColumnConfig();
		column.setId("giorno7");  
		column.setHeader("7");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno8");  
		column.setHeader("8");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno9");  
		column.setHeader("9");  
		column.setWidth(27); column.setResizable(false); 
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno10");  
		column.setHeader("10");  
		column.setWidth(27); column.setResizable(false); 
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno11");  
		column.setHeader("11");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno12");  
		column.setHeader("12");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno13");  
		column.setHeader("13");  
		column.setWidth(27); column.setResizable(false); 
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno14");  
		column.setHeader("14");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno15");  
		column.setHeader("15");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno16");  
		column.setHeader("16");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno17");  
		column.setHeader("17");  
		column.setWidth(27); column.setResizable(false);;  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno18");  
		column.setHeader("18");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno19");  
		column.setHeader("19");  
		column.setWidth(27); column.setResizable(false); 
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno20");  
		column.setHeader("20");  
		column.setWidth(27); column.setResizable(false);
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno21");  
		column.setHeader("21");  
		column.setWidth(27); column.setResizable(false); 
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno22");  
		column.setHeader("22");  
		column.setWidth(27); column.setResizable(false); 
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno23");  
		column.setHeader("23");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno24");  
		column.setHeader("24");  
		column.setWidth(27); column.setResizable(false); 
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno25");  
		column.setHeader("25");  
		column.setWidth(27); column.setResizable(false); 
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno26");  
		column.setHeader("26");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno27");  
		column.setHeader("27");  
		column.setWidth(27); column.setResizable(false); 
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno28");  
		column.setHeader("28");  
		column.setWidth(27); column.setResizable(false);
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno29");  
		column.setHeader("29");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno30");  
		column.setHeader("30");  
		column.setWidth(27); column.setResizable(false);  
		configs.add(column);
		
		column=new ColumnConfig();
		column.setId("giorno31");  
		column.setHeader("31");  
		column.setWidth(27); column.setResizable(false); 
		configs.add(column);
		*/
		return configs;
	}

	private void caricaTabella() {
		
		String sede=new String();
		String data=new String();
		String mese= new String();
		
		String anno=smplcmbxAnno.getRawValue().toString();		
		mese=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
		sede=smplcmbxSede.getRawValue().toString();
		
		data=mese+anno;
		
		AdministrationService.Util.getInstance().getRiepilogoMensileDettagliatoHorizontalLayout(sede, data, new AsyncCallback<List<RiepilogoMeseGiornalieroModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getRiepilogoMensileDettagliatoHorizontalLayout()");
				
			}

			@Override
			public void onSuccess(List<RiepilogoMeseGiornalieroModel> result) {
				if(result!=null)
					loadTable(result);
				else
					Window.alert("Impossibile accedere ai dati per il riepilogo giornaliero.");
			}		
		});
		
	}
	
	private void loadTable(List<RiepilogoMeseGiornalieroModel> result) {
		
		store.removeAll();
		store.add(result);
		store.setStoreSorter(new StoreSorter<RiepilogoMeseGiornalieroModel>());  
	    store.setDefaultSort("dipendente", SortDir.ASC);
		gridRiepilogo.reconfigure(store, cmRiepilogo);
		
	}
	
}
