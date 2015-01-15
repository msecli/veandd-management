package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.layout.CenterLayout_RiepiloghiSalPcl;
import gestione.pack.client.model.CommessaModel;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelProtocolloCommesse extends LayoutContainer{

	private GroupingStore<CommessaModel>storeC = new GroupingStore<CommessaModel>();
	private Grid<CommessaModel> gridRiepilogo;
	//private GridSelectionModel<CommessaModel> sm = new CheckBoxSelectionModel<CommessaModel>();
	private ColumnModel cm;
	
	private SimpleComboBox<String> smplcmbxAnno;
	private Button btnAddCommessa;
	
	private List<CommessaModel> listaDati= new ArrayList<CommessaModel>();
	
	public PanelProtocolloCommesse(){
		
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setHeight(950);
		cpGrid.setWidth(1250);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setButtonAlign(HorizontalAlignment.CENTER);
		
		storeC.groupBy("annoProtocollo");
		storeC.setSortDir(SortDir.DESC);
		storeC.add(listaDati);
					   
		cm = new ColumnModel(createColumns());
		
		GroupingView view = new GroupingView();  
	    view.setShowGroupedColumn(false);  
	    view.setForceFit(true);  
	    /*view.setGroupRenderer(new GridGroupRenderer() {  
	      public String render(GroupColumnData data) {  
	        String f = cm.getColumnById(data.field).getHeader();  
	        //String l = data.models.size() == 1 ? "Item" : "Items";  
	        return f + ": " + data.group ;//+ " (" + data.models.size() + " " + l + ")";  
	      }
	    });*/
		
		gridRiepilogo= new Grid<CommessaModel>(storeC, cm);  
		gridRiepilogo.setBorders(false); 
		gridRiepilogo.setColumnLines(true);
		gridRiepilogo.setStripeRows(true);
		gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		gridRiepilogo.setView(view);
		/*gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<CommessaModel>>() {  
	          public void handleEvent(SelectionChangedEvent<CommessaModel> be) {
	        	  if (be.getSelection().size() > 0) {
	        		  	            	
	        	  }
	          }
		});*/
		caricaTabellaDati();
		
		Date d= new Date();
		String data= d.toString();
		String anno= data.substring(data.length()-4, data.length());
		
		smplcmbxAnno=new SimpleComboBox<String>();
		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setWidth(75);
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setAllowBlank(false);
		 for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);
			 smplcmbxAnno.select(1);
		}
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setSimpleValue(anno);
		smplcmbxAnno.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				caricaTabellaDati();
			}
		});
		
		btnAddCommessa=new Button();
		btnAddCommessa.setSize(26, 26);
		btnAddCommessa.setEnabled(true);
		btnAddCommessa.setToolTip("Nuova commessa");
		btnAddCommessa.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.add()));	
		btnAddCommessa.setIconAlign(IconAlign.TOP);
		btnAddCommessa.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {		
				Dialog d= new Dialog();
	        	d.setSize(1060, 890);
	        	d.add(new PanelCommessa());
	        	d.setCollapsible(true);
	        	d.setScrollMode(Scroll.NONE);
	        	d.setButtons("");
	        	d.setConstrain(false);
	        	d.show();				
			}				
		});
		
		ToolBar tlbrPanel= new ToolBar();
		
		cpGrid.add(gridRiepilogo);
		tlbrPanel.add(smplcmbxAnno);
		tlbrPanel.add(btnAddCommessa);
		cpGrid.setTopComponent(tlbrPanel);
		
		layoutContainer.add(cpGrid, new FitData(0, 3, 3, 0));
		add(layoutContainer);
		
	}
	
	private List<ColumnConfig> createColumns() {
		
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("annoProtocollo");  
	    column.setHeader("Anno");  
	    column.setWidth(120);  
	    column.setRowHeader(true);  
	    configs.add(column); 		
		
		column=new ColumnConfig();		
	    column.setId("ragioneSociale");  
	    column.setHeader("Cliente");  
	    column.setWidth(120);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column); 
	    
	    column=new ColumnConfig();
	    column.setId("pm");
	    column.setHeader("Project Manager");
	    column.setWidth(120);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
		
	    column=new ColumnConfig();
		column.setId("numeroCommessa");
		column.setHeader("Commessa");
		column.setWidth(70);
		column.setRowHeader(true);
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
		    
		column=new ColumnConfig();
	    column.setId("estensione");
	    column.setHeader("Est.");
	    column.setWidth(45);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);  
	    
	    column=new ColumnConfig();		
		column.setId("rdo");  
		column.setHeader("Rdo");  
		column.setWidth(70);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
		
		column=new ColumnConfig();	
		column.setId("numeroOfferta");  
		column.setHeader("N. Offerta");  
		column.setWidth(70);
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
	    
	    column=new ColumnConfig();		
		column.setId("numeroOrdine");  
		column.setHeader("N. Ordine");  
		column.setWidth(70);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
	        	    
		column=new ColumnConfig();		
		column.setId("statoCommessa");  
		column.setHeader("Stato");  
		column.setWidth(70);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
		
	    return configs;
	}
	

	private void caricaTabellaDati() {
		AdministrationService.Util.getInstance().getAllCommesseModel("","Tutte", new AsyncCallback<List<CommessaModel>>() {
			@Override
			public void onSuccess(List<CommessaModel> result) {
				if(result==null)
					Window.alert("error: Impossibile efettuare il caricamento dati in tabella.");
				else	
					loadTable(result);		
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getAllCommesseModel();");
				caught.printStackTrace();		
			}
		}); //AsyncCallback	  
	}

	
	private void loadTable(List<CommessaModel> lista) {
		CommessaModel cm=lista.remove(lista.size()-1);
		String annoProtocollo="";
		String annoSelected=smplcmbxAnno.getRawValue().toString();
		storeC.removeAll();
		storeC.setStoreSorter(new StoreSorter<CommessaModel>());  
		storeC.setDefaultSort("numeroCommessa", SortDir.ASC);
		for(CommessaModel c:lista){
			annoProtocollo=c.get("annoProtocollo");
			if(annoProtocollo.compareTo(annoSelected)==0)
				storeC.add(c);
		}
		//storeC.add(lista);
	}		
	
}
