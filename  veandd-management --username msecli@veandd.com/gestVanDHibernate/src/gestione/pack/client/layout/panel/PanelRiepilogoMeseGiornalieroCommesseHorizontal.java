package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.model.RiepilogoMeseGiornalieroModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
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
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;

public class PanelRiepilogoMeseGiornalieroCommesseHorizontal  extends LayoutContainer{
	
	private ListStore<RiepilogoMeseGiornalieroModel>store = new ListStore<RiepilogoMeseGiornalieroModel>();
	private Grid<RiepilogoMeseGiornalieroModel> gridRiepilogo;
	private ColumnModel cmRiepilogo;
	private CellSelectionModel<RiepilogoMeseGiornalieroModel> cm=new CellSelectionModel<RiepilogoMeseGiornalieroModel>();
	
	//private Button btnViewFoglioOre;
	//private Button btnSelect;
	//private SimpleComboBox<String> smplcmbxAnno;
	//private SimpleComboBox<String> smplcmbxMese;
	private ComboBox<PersonaleModel> cmbxDipendente= new ComboBox<PersonaleModel>();
	private TextField<String> txtOreTotaliIntCommesse;
	private TextField<String> txtOreTotaliIntIU;
	private Button btnPrint= new Button();
	
	private String dataR;
	private String usernameR;
	
	//private int h=Window.getClientHeight();
	//private int w=Window.getClientWidth();
	
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	public PanelRiepilogoMeseGiornalieroCommesseHorizontal(String username, Date data){
		String dataRif=data.toString();
		String mese=dataRif.substring(4, 7);
		String anno=dataRif.substring(dataRif.length()-4,dataRif.length());
		
		this.dataR=mese+anno;
		this.usernameR=username;
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
		//cpGrid.setSize(w-225, h-65);
		cpGrid.setSize(1450, 580);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());	
		Resizable r=new Resizable(cpGrid);
		
		FormLayout frmLyout= new FormLayout();
		frmLyout.setLabelWidth(200);
		ContentPanel cntpnlTxtField= new ContentPanel();
		cntpnlTxtField.setBodyBorder(false);         
		cntpnlTxtField.setHeaderVisible(false);
		cntpnlTxtField.setLayoutData(new RowData());
		cntpnlTxtField.setLayout(frmLyout);
		cntpnlTxtField.setWidth(500);
		cntpnlTxtField.setHeight(100);
		cntpnlTxtField.setStyleAttribute("padding-top", "20px");
				
		/*Date d= new Date();
		String dt= d.toString();
		String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(dt.substring(4, 7)));
		String anno= dt.substring(dt.length()-4, dt.length());*/
		
		txtOreTotaliIntCommesse= new TextField<String>();
		txtOreTotaliIntCommesse.setFieldLabel("Totale Ore da Riepilogo Commesse");
		txtOreTotaliIntCommesse.setValue("0.00");
		txtOreTotaliIntCommesse.setEnabled(false);
		txtOreTotaliIntCommesse.setWidth(30);
		
		txtOreTotaliIntIU= new TextField<String>();
		txtOreTotaliIntIU.setFieldLabel("Totale Ore da Intervalli I/U");
		txtOreTotaliIntIU.setValue("0.00");
		txtOreTotaliIntIU.setEnabled(false);
		txtOreTotaliIntIU.setWidth(30);
		
		cntpnlTxtField.add(txtOreTotaliIntIU);
		cntpnlTxtField.add(txtOreTotaliIntCommesse);
		/*
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
		//tlbrOperazioni.add(smplcmbxAnno);
		   
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
		//tlbrOperazioni.add(smplcmbxMese);
		
		ListStore<PersonaleModel> storeP=new ListStore<PersonaleModel>();
		cmbxDipendente.setStore(storeP);
		cmbxDipendente.setFieldLabel("Dipendente");
		cmbxDipendente.setEnabled(true);
		cmbxDipendente.setEmptyText("Selezionare il dipendente..");
		cmbxDipendente.setEditable(true);
		cmbxDipendente.setVisible(true);
		cmbxDipendente.setTriggerAction(TriggerAction.ALL);
		cmbxDipendente.setAllowBlank(false);
		cmbxDipendente.setDisplayField("nomeCompleto");
		cmbxDipendente.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {					
					getAllDipendenti();					
			}
	
		});		
		//tlbrOperazioni.add(cmbxDipendente);
		
		
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

		});*/
		//tlbrOperazioni.add(btnSelect);
		
		btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
	   	btnPrint.setIconAlign(IconAlign.TOP);
	   	btnPrint.setSize(26, 26);
		btnPrint.setToolTip("Stampa");
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				
			    String totOreCommesse= txtOreTotaliIntCommesse.getValue().toString();
			    String totOreIU= txtOreTotaliIntIU.getValue().toString();
				
				SessionManagementService.Util.getInstance().setDataRiepilogoCommesseInSession(dataR, usernameR, "COMM", totOreCommesse,
						totOreIU, store.getModels(), new AsyncCallback<Boolean>() {

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
	    ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(fp);
		
		ToolBar tlBar= new ToolBar();
		tlBar.add(cp);     
		
		try {	    	
			cmRiepilogo = new ColumnModel(createColumns(31)); 
			caricaTabella();
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
		store.setDefaultSort("commessa", SortDir.ASC);
		
		gridRiepilogo= new Grid<RiepilogoMeseGiornalieroModel>(store, cmRiepilogo);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);
		gridRiepilogo.setSelectionModel(cm);
		gridRiepilogo.setView(view);	
		//gridRiepilogo.setSize(980, 830);
		  	
		cpGrid.add(gridRiepilogo);
		cpGrid.setTopComponent(tlBar);
		cpGrid.setBottomComponent(cntpnlTxtField);
				
		//caricaTabella();
		
		layoutContainer.add(cpGrid, new FitData(3,3,3,3));
		//layoutContainer.add(cntpnlTxtField);
		add(layoutContainer);	
	}
	
	
	private List<ColumnConfig> createColumns(int numeroGiorniMese) {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
					
		ColumnConfig column=new ColumnConfig();
		column.setId("commessa");  
		column.setHeader("Commessa");  
		column.setWidth(150);	
		column.setResizable(true);
		column.setRenderer(new GridCellRenderer<RiepilogoMeseGiornalieroModel>() {

			@Override
			public Object render(RiepilogoMeseGiornalieroModel model,
					String property, ColumnData config, int rowIndex, int colIndex,
					ListStore<RiepilogoMeseGiornalieroModel> store, Grid<RiepilogoMeseGiornalieroModel> grid) {
				
				String val=model.get("commessa");
				if(val.compareTo("_Tot.Giorno")==0)
					config.style = config.style + ";background-color:#d2f5af;";
				else
					config.style = config.style + ";background-color:#ffffff;";
				
				return model.get(property);
			}
		});
		configs.add(column); 
				
	
		for(int i=1;i<=numeroGiorniMese;i++){		
			
			column=new ColumnConfig();
			column.setId("giorno"+i);  
			column.setHeader(String.valueOf(i));  
			column.setWidth(38); 
			column.setResizable(true);
			column.setRenderer(new GridCellRenderer<RiepilogoMeseGiornalieroModel>() {
				@Override
				public Object render(RiepilogoMeseGiornalieroModel model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<RiepilogoMeseGiornalieroModel> store, Grid<RiepilogoMeseGiornalieroModel> grid) {
					String orario=model.get(property);
							
					if(!orario.startsWith(";")&&orario.length()>2)
						if(Float.valueOf(orario)!=0)
							config.style = config.style + ";font-weight:bold;" ;
						else
							config.style = config.style + ";font-weight:normal;" ;
										
					String val=model.get("commessa");
					if(val.compareTo("_Tot.Giorno")==0 )
						config.style = config.style + ";background-color:#d2f5af;";
					else
						config.style = config.style + ";background-color:#ffffff;";
					
					return model.get(property);
				}
			});
			configs.add(column);
		}
		
		column=new ColumnConfig();
		column.setId("totale");  
		column.setHeader("Tot.Ore");  
		column.setWidth(55); 
		column.setResizable(true);
		column.setRenderer(new GridCellRenderer<RiepilogoMeseGiornalieroModel>() {
			@Override
			public Object render(RiepilogoMeseGiornalieroModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<RiepilogoMeseGiornalieroModel> store, Grid<RiepilogoMeseGiornalieroModel> grid) {
				String orario=model.get(property);
						
				if(!orario.startsWith(";")&&orario.length()>2)
					if(Float.valueOf(orario)!=0)
						config.style = config.style + ";font-weight:bold;"+ "background-color:#d2f5af;";
					else
						config.style = config.style + ";font-weight:normal;"+"background-color:#d2f5af;" ;
													
				return model.get(property);
			}
		});
		configs.add(column);
	
		return configs;
	}
	

	private void caricaTabella() {
		
		/*String dipendente=new String();
		String data=new String();
		String mese= new String();
		
		String anno=smplcmbxAnno.getRawValue().toString();		
		mese=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
		dipendente=cmbxDipendente.getValue().getUsername();
		
		data=mese+anno;
		*/
		AdministrationService.Util.getInstance().getRiepilogoMensileDettagliatoCommesseHorizontalLayout(usernameR, dataR, new AsyncCallback<List<RiepilogoMeseGiornalieroModel>>() {

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
	/*
	private void getAllDipendenti() {
		AdministrationService.Util.getInstance().getListaDipendentiModel("", new AsyncCallback<List<PersonaleModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getCommesseByPM();");
				caught.printStackTrace();		
			}

			@Override
			public void onSuccess(List<PersonaleModel> result) {
				if(result!=null){		
					ListStore<PersonaleModel> lista= new ListStore<PersonaleModel>();
					lista.setStoreSorter(new StoreSorter<PersonaleModel>());  
					lista.setDefaultSort("nomeCompleto", SortDir.ASC);
					
					lista.add(result);				
					cmbxDipendente.clear();
					cmbxDipendente.setStore(lista);
					
				}else Window.alert("error: Errore durante l'accesso ai dati Personale.");				
			}
		});
	}	*/
	
	private void loadTable(List<RiepilogoMeseGiornalieroModel> result) {
		final NumberFormat number = NumberFormat.getFormat("0.00");
		RiepilogoMeseGiornalieroModel recordTotali= new RiepilogoMeseGiornalieroModel();
		recordTotali=result.remove(result.size()-1);
		
		//Prelevo ed elimino l'ultima entry del result
		txtOreTotaliIntCommesse.setValue(number.format((Float)Float.valueOf((String) recordTotali.get("dipendente"))));
		txtOreTotaliIntIU.setValue(number.format((Float)Float.valueOf((String) recordTotali.get("username"))));
		
		int numeroGiorniMese=0;
		String anno=dataR.substring(3,dataR.length());//smplcmbxAnno.getRawValue().toString();		
		String mese=ClientUtility.traduciMeseToIt(dataR.substring(0,3));//ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
		numeroGiorniMese=ClientUtility.getGiorniMese(mese, anno);
		
		store.removeAll();
		store.add(result);
		store.setStoreSorter(new StoreSorter<RiepilogoMeseGiornalieroModel>());  
	    store.setDefaultSort("commessa", SortDir.ASC);
	    cmRiepilogo=new ColumnModel(createColumns(numeroGiorniMese));
		gridRiepilogo.reconfigure(store, cmRiepilogo);
	}		

}
