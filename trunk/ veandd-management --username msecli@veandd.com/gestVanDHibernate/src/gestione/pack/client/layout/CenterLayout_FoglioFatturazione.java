package gestione.pack.client.layout;

//modifiche dalla versione 3

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;
import gestione.pack.client.model.RiepilogoOreTotaliCommesse;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.model.FoglioFatturazioneModel;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CenterLayout_FoglioFatturazione extends LayoutContainer {

public CenterLayout_FoglioFatturazione(){}
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	private SimpleComboBox<String> smplcmbxMese= new SimpleComboBox<String>();
	private SimpleComboBox<String> smplcmbxAnno=new SimpleComboBox<String>();
	private SimpleComboBox<String> smplcmbxPM=new SimpleComboBox<String>();
	private Button btnSelect;
	private TextField<String> txtfldUsername= new TextField<String>();
	private HorizontalPanel hpLayout;
	private Button btnSalva= new Button("Salva");
	private boolean trovato=true;
	private String numCommessa= "";
	private String numEstensione= "";
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);

	    try {
			selectLayout(); //Se l'accesso è stato effettuato da un PM ci sarà la precompilazione del nome PM (successivamente anche mese e dati relativi)		
		} catch (Exception e) {
			Window.alert("error: Impossibile recuperare i dati in sessione.");
		}
	    
		final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		layoutContainer.setWidth(w-225);
		layoutContainer.setHeight(h-54);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);		
		
		hpLayout= new HorizontalPanel();
		hpLayout.setSpacing(0);
		hpLayout.setItemId("hpLayout");
		hpLayout.setStyleAttribute("margin-left", "5px");
				
		/*ContentPanel cntpnlLayout = new ContentPanel(); //pannello esterno
		cntpnlLayout.setHeading("Fatturazione.");
		cntpnlLayout.setHeaderVisible(true);
		cntpnlLayout.setCollapsible(false);
		cntpnlLayout.setBorders(false);
		cntpnlLayout.setWidth(w-215);
		cntpnlLayout.setHeight(h-55);
		cntpnlLayout.setScrollMode(Scroll.AUTO);
		*/
		
		ContentPanel cntpnlFoglioFatturazione= new ContentPanel();
		cntpnlFoglioFatturazione.setHeading("Foglio di Fatturazione.");
		cntpnlFoglioFatturazione.setHeaderVisible(false);
		cntpnlFoglioFatturazione.setCollapsible(false);
		cntpnlFoglioFatturazione.setBorders(false);
		cntpnlFoglioFatturazione.setWidth(1090);
		cntpnlFoglioFatturazione.setHeight(800);
		cntpnlFoglioFatturazione.setFrame(true);
		cntpnlFoglioFatturazione.setButtonAlign(HorizontalAlignment.CENTER);
		cntpnlFoglioFatturazione.setStyleAttribute("padding-left", "7px");
		cntpnlFoglioFatturazione.setStyleAttribute("margin-top", "15px");
		
		cntpnlFoglioFatturazione.addButton(btnSalva);
		btnSalva.setEnabled(false);
		btnSalva.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				CntpnlDatiFatturazioneOrdine cp= new CntpnlDatiFatturazioneOrdine();
				HorizontalPanel hp= new HorizontalPanel();
				hp=hpLayout;
				
				cp=(CntpnlDatiFatturazioneOrdine) hp.getItemByItemId("panelDatiFatturazione");
				if(cp.txtfldOreDaFatturare.isValid()&&cp.txtfldVariazioneSAL.isValid()&&cp.txtfldVariazionePCL.isValid()){
					String oreEseguite= cp.txtfldOreEseguiteRegistrate.getValue().toString();		
					String salIniziale= cp.txtfldSALIniziale.getValue().toString();
					String pclIniziale= cp.txtfldPCLIniziale.getValue().toString();
					String oreFatturare= cp.txtfldOreDaFatturare.getValue().toString();
					String variazioneSAL= cp.txtfldVariazioneSAL.getValue().toString();
					String variazionePCL= cp.txtfldVariazionePCL.getValue().toString();
					String tariffaUtilizzata=cp.txtfldCostoOrario.getValue().toString();
					
					String meseCorrente=new String();
					String anno=new String();
					String data= new String();
					String note= new String();
					String statoElaborazione="1"; //se differenziare salvataggio PM da salvataggioUA allora usare 1 o 2
					
					if(cp.txtaNote.getValue()==null)
						note="";
					else note=cp.txtaNote.getValue().toString();
					
					anno=smplcmbxAnno.getRawValue().toString();
					meseCorrente=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
					data=meseCorrente+anno;
					AdministrationService.Util.getInstance().insertDatiFoglioFatturazione(oreEseguite,salIniziale,pclIniziale,oreFatturare,variazioneSAL,
							variazionePCL, data, note, statoElaborazione, cp.commessaSelezionata, tariffaUtilizzata, new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Problema di connessione on insertDatiFoglioFatturazione();");
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Boolean result) {
									if(result){
										Window.alert("Inserimento avvenuto con successo.");
										reloadFoglioFatt();
										
									}else{
										Window.alert("error: Impossibile inserire i dati del foglio fatturazione!");
									}
								}
					});				
				}			
			}
		});
				
		Date d= new Date();
		String data= d.toString();
		String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(data.substring(4, 7)));
		String anno= data.substring(data.length()-4, data.length());
			
		txtfldUsername.setVisible(false);
		
		smplcmbxMese.setWidth(110);
		smplcmbxMese.setFieldLabel("Mese");
		smplcmbxMese.setName("mese");
		smplcmbxMese.setEmptyText("Mese..");
		smplcmbxMese.setAllowBlank(false);
		 for(String l : DatiComboBox.getMese()){
			 smplcmbxMese.add(l);}
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		smplcmbxMese.setSimpleValue(mese);
			
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
		
		smplcmbxPM = new SimpleComboBox<String>();
		smplcmbxPM.setFieldLabel("Project Manager");
		smplcmbxPM.setName("pm");
		smplcmbxPM.setAllowBlank(true);
		smplcmbxPM.setTriggerAction(TriggerAction.ALL);
		smplcmbxPM.setEmptyText("Project Manager..");
		smplcmbxPM.setAllowBlank(false);
		getNomePM();	
		
		/*if(!txtfldUsername.getRawValue().isEmpty()){	//primo caricamento dati se ad accedere è stato un pm
			String nome=new String();
			String cognome= new String();
			nome=txtfldUsername.getValue().substring(0,txtfldUsername.getValue().indexOf("."));
			cognome=txtfldUsername.getValue().substring(txtfldUsername.getValue().indexOf(".")+1, txtfldUsername.getValue().length());
			
			nome=nome.substring(0,1).toUpperCase() + nome.substring(1,nome.length());
			cognome=cognome.substring(0,1).toUpperCase() + cognome.substring(1,cognome.length());		
			smplcmbxPM.setSimpleValue(cognome+" "+nome);
			//hpLayout.add(new CntpnlRiepilogoOreDipFatturazione());
		}
		else
			{*/
				hpLayout.add(new CntpnlRiepilogoOreDipFatturazione());
				hpLayout.add(new CntpnlDatiFatturazioneOrdine());
				hpLayout.layout();
			//}
		
		btnSelect= new Button("OK");
		btnSelect.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {		
				if(smplcmbxMese.isValid()&&smplcmbxPM.isValid()&&smplcmbxAnno.isValid()){			
					hpLayout.removeAll();
					hpLayout.add(new CntpnlRiepilogoOreDipFatturazione());
					hpLayout.add(new CntpnlDatiFatturazioneOrdine());
					hpLayout.layout();
				}else Window.alert("Controllare i dati selezionati!");
			}
		});		
			
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		horizontalPanel.add(smplcmbxAnno);
		horizontalPanel.add(smplcmbxMese);
		horizontalPanel.add(smplcmbxPM);
		horizontalPanel.add(btnSelect);
		
		cntpnlFoglioFatturazione.setTopComponent(horizontalPanel);
		cntpnlFoglioFatturazione.add(hpLayout);
		
		//cntpnlLayout.add(cntpnlFoglioFatturazione);
	    
		bodyContainer.add(cntpnlFoglioFatturazione);
	   				
		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);
	}	
	
	private void reloadFoglioFatt() {
		hpLayout.removeAll();
		hpLayout.add(new CntpnlRiepilogoOreDipFatturazione());
		hpLayout.add(new CntpnlDatiFatturazioneOrdine());
		hpLayout.layout();		
	}
	
	
	private void selectLayout() {
		BodyLayout_PersonalManager lcPM = new BodyLayout_PersonalManager();	
		
		if (getParent().getParent().getParent().getParent().getClass().equals(lcPM.getClass())) {
			lcPM = (BodyLayout_PersonalManager) getParent().getParent()	.getParent().getParent();
			txtfldUsername.setValue(lcPM.txtfldUsername.getValue().toString());
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
	
	
	private class CntpnlRiepilogoOreDipFatturazione extends ContentPanel{
		
		private GroupingStore<RiepilogoOreDipFatturazione>store = new GroupingStore<RiepilogoOreDipFatturazione>();
		private Grid<RiepilogoOreDipFatturazione> gridRiepilogo;
		private ColumnModel cm;
		private boolean nuovo=true;	
		
		CntpnlRiepilogoOreDipFatturazione(){		
			setHeading("Riepilogo Ore (Mensile).");
			setHeaderVisible(false);
			setCollapsible(false);
			setBorders(false);
			setBodyBorder(false);
			setScrollMode(Scroll.NONE);	
			setWidth(435);
			setFrame(false);
			setScrollMode(Scroll.AUTOY);
			setItemId("panelRiepilogoOre");
			
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
		    summary.setStartCollapsed(true);
		   		
		    gridRiepilogo= new Grid<RiepilogoOreDipFatturazione>(store, cm);  
		    gridRiepilogo.setItemId("grid");
		    gridRiepilogo.setBorders(false);  
		    gridRiepilogo.setStripeRows(true);  
		    gridRiepilogo.setColumnLines(true);  
		    gridRiepilogo.setColumnReordering(true);  
		    gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		    gridRiepilogo.setView(summary);
		    
		    gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		    gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<RiepilogoOreDipFatturazione>>() {  
		          public void handleEvent(SelectionChangedEvent<RiepilogoOreDipFatturazione> be) {  
		        	
		            if (be.getSelection().size() > 0) {      
		            	String commessa= new String();
		            	commessa=be.getSelectedItem().getNumeroCommessa();
		            			            	
		            	numCommessa=commessa.substring(0,commessa.indexOf("."));
		            	numEstensione=commessa.substring(commessa.indexOf(".")+1, commessa.indexOf("(")-1);		
		            		            	
		            	//hpLayout.removeAll();
		        		//hpLayout.add(new CntpnlRiepilogoOreDipFatturazione());
		        		CntpnlDatiFatturazioneOrdine cp=new CntpnlDatiFatturazioneOrdine();
		        		cp=(CntpnlDatiFatturazioneOrdine) hpLayout.getItemByItemId("panelDatiFatturazione");
		        		
		        		hpLayout.remove(cp);
		        		hpLayout.add(new CntpnlDatiFatturazioneOrdine());
		        		hpLayout.layout();	
		               		            	
		            } else {  
		                
		           }
		         }
		    }); 		   
		    
		    
		    ContentPanel cntpnlGrid= new ContentPanel();
		    cntpnlGrid.setBodyBorder(false);  
		    cntpnlGrid.setBorders(false);
		    cntpnlGrid.setFrame(true);
		    cntpnlGrid.setLayout(new FitLayout());  
		    cntpnlGrid.setHeaderVisible(false);
		    cntpnlGrid.setWidth(420);
		    cntpnlGrid.setHeight(705);
		    cntpnlGrid.setScrollMode(Scroll.AUTOY);
		    cntpnlGrid.add(gridRiepilogo);
		   	    
		    if(!smplcmbxMese.getRawValue().isEmpty()&&!smplcmbxPM.getRawValue().isEmpty()&&!smplcmbxAnno.getRawValue().isEmpty()){
		    	String meseRif= new String(); 
		    	String anno= new String();
		    	String data= new String();
		    	anno= smplcmbxAnno.getRawValue().toString();
				meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
				data=meseRif+anno;
		    	caricaTabellaRiepOreDipFatturazione(data, smplcmbxPM.getRawValue().toString());
		    }	  
		  	add(cntpnlGrid);
		  	layout();
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
		    configs.add(columnOreLavoro); 	
		    
		    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
		    columnOreViaggio.setId("oreViaggio");  
		    columnOreViaggio.setHeader("Ore Viaggio");  
		    columnOreViaggio.setWidth(63);    
		    columnOreViaggio.setRowHeader(true); 
		    columnOreViaggio.setAlignment(HorizontalAlignment.LEFT);    
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
		    configs.add(columnOreViaggio); 
		    
		    SummaryColumnConfig<Double> columnOreTotali=new SummaryColumnConfig<Double>();		
		    columnOreTotali.setId("oreTotali");  
		    columnOreTotali.setHeader("Totale");  
		    columnOreTotali.setWidth(63);    
		    columnOreTotali.setRowHeader(true); 
		    columnOreTotali.setAlignment(HorizontalAlignment.LEFT);    
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
		    configs.add(columnOreTotali); 		
			return configs;
		}	

		
		private void caricaTabellaRiepOreDipFatturazione(String meseRif, String pm) {		
			AdministrationService.Util.getInstance().getRiepilogoOreDipFatturazione(meseRif, pm, new AsyncCallback<List<RiepilogoOreDipFatturazione>>() {	
				@Override
				public void onSuccess(List<RiepilogoOreDipFatturazione> result) {
					if(result==null)
						Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
					else	
						if(result.size()==0){
							Window.alert("Nessun dato (ore lavoro) rilevato in base ai criteri di ricerca selezionati.");
							trovato=false;
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
	
	
	private class CntpnlDatiFatturazioneOrdine extends ContentPanel{	
		private boolean nuovo =true;
		private String commessaSelezionata=new String();
		private TextField<String> txtfldSALIniziale= new TextField<String>();
		private TextField<String> txtfldPCLIniziale= new TextField<String>();
		private TextField<String> txtfldTotOre= new TextField<String>();
		private TextField<String> txtfldOreOrdine= new TextField<String>();
		private TextField<String> txtfldOreResiduoOrdine= new TextField<String>();
		private TextField<String> txtfldCostoOrario= new TextField<String>();		
		private TextField<String> txtfldOreDaFatturare= new TextField<String>();
		private TextField<String> txtfldVariazioneSAL= new TextField<String>();
		private TextField<String> txtfldVariazionePCL= new TextField<String>();
		private TextField<String> txtfldOreScaricate= new TextField<String>();
		private TextField<String> txtfldDiffScaricateEseguite= new TextField<String>();
		private TextField<String> txtfldTotFatturato= new TextField<String>();
		private TextField<String> txtfldOreEseguiteRegistrate= new TextField<String>();
		private Text txtSalTotale= new Text();
		private Text txtPclTotale= new Text();
		
		private Text txtOreDaFatturare= new Text();
		private Text txtVariazioneSal= new Text();
		private Text txtVariazionePcl=new Text();
		private Text txtOreScaricate = new Text();
		private Text txtMargine= new Text();
		//private TextField<String> txtfldOreEseguiteEffettive= new TextField<String>();
		
		private TextArea txtaNote=new TextArea();
		
		private ColumnModel cmOrdine;
		private Grid<RiepilogoOreTotaliCommesse> gridOrdine; 
		
		CntpnlDatiFatturazioneOrdine(){
			
			setHeaderVisible(false);
			setCollapsible(false);
			setBorders(false);
			setBodyBorder(false);
			setScrollMode(Scroll.NONE);	
			setWidth(655);
			setHeight(550);
			setFrame(false);
			setItemId("panelDatiFatturazione");
									
			VerticalPanel vp=new VerticalPanel();
			vp.setItemId("vp");
			vp.setBorders(false);
			vp.setSpacing(4);
			vp.setStyleAttribute("margin-top", "-5px");
			
			if(smplcmbxMese.getRawValue().toString().compareTo("")!=0)
				caricaTabellaDati();
			
			//Tabella contenente elenco ordini
			ContentPanel cntpnlGrid = new ContentPanel();  
		    cntpnlGrid.setBodyBorder(false);
		    cntpnlGrid.setBorders(false);
		    cntpnlGrid.setHeading("Dati Ordini");  
		    cntpnlGrid.setButtonAlign(HorizontalAlignment.CENTER);  
		    cntpnlGrid.setLayout(new FitLayout());  
		    cntpnlGrid.setHeaderVisible(false);
		    cntpnlGrid.setWidth(575);
		    cntpnlGrid.setHeight(140);
		    cntpnlGrid.setScrollMode(Scroll.AUTO);
		
			final ListStore<RiepilogoOreTotaliCommesse> store = new ListStore<RiepilogoOreTotaliCommesse>();  
			cmOrdine = new ColumnModel(createColumns());		
		    		         
			gridOrdine = new Grid<RiepilogoOreTotaliCommesse>(store, cmOrdine);   
		    gridOrdine.setBorders(true);  
		    gridOrdine.setStripeRows(true);  
		    gridOrdine.setColumnLines(true);  
		    gridOrdine.setColumnReordering(true);  
		    gridOrdine.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);		    
		    gridOrdine.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<RiepilogoOreTotaliCommesse>>() {  
		          public void handleEvent(SelectionChangedEvent<RiepilogoOreTotaliCommesse> be) {  		        	
		            if (be.getSelection().size() > 0) {  
		            	if(be.getSelectedItem().getNumeroCommessa().compareTo("TOTALE")!=0){
		            		String numeroC= new String();
		            		String estensione=new String();
		            		numeroC=be.getSelectedItem().getNumeroCommessa();
		            		estensione=be.getSelectedItem().getEstensione();
		            		try{
		            			caricaDatiFatturazioneOrdine(numeroC+"."+estensione);
		            		}catch (Exception e) {
		            			e.printStackTrace();
		        				System.out.println("errore carica dati fatturazione");
							}	            		
		            		commessaSelezionata=(numeroC+"."+be.getSelectedItem().getEstensione());
		            		btnSalva.setEnabled(true);
		            	}		            	
		             } 	
		          }          
		        }); 
			
		    cntpnlGrid.add(gridOrdine);
		
			FieldSet fldsetRiepOreOrdine=new FieldSet();
			fldsetRiepOreOrdine.setCollapsible(false);
			fldsetRiepOreOrdine.setExpanded(true);
			fldsetRiepOreOrdine.setHeading("Ordine da Fatturare:");
			
			vp.add(fldsetRiepOreOrdine);
			
			ContentPanel cp= new ContentPanel();
			cp.setHeaderVisible(false);
			cp.setSize(575, 110);
			cp.setBorders(false);
			cp.setBodyBorder(false);
			cp.setFrame(false);
			cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
			cp.setStyleAttribute("padding-left", "5px");
			cp.setStyleAttribute("padding-top", "5px");
			
			LayoutContainer layoutCol1=new LayoutContainer();
			LayoutContainer layoutCol2=new LayoutContainer();
			LayoutContainer layoutCol3=new LayoutContainer();
			
			FormLayout layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol1.setLayout(layout);
					
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol2.setLayout(layout);
			
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol3.setLayout(layout);
			
			txtfldSALIniziale.setFieldLabel("SAL iniziale");
			txtfldSALIniziale.setEnabled(false);
			
			txtfldPCLIniziale.setFieldLabel("PCL iniziale");
			txtfldPCLIniziale.setEnabled(false);
			
			txtfldTotOre.setFieldLabel("Totale");
			txtfldTotOre.setEnabled(false);
			
			txtfldOreOrdine.setFieldLabel("Ore Ordine");
			txtfldOreOrdine.setEnabled(false);
			
			txtfldOreResiduoOrdine.setFieldLabel("Residuo Ore");
			txtfldOreResiduoOrdine.setEnabled(false);
			
			txtfldCostoOrario.setFieldLabel("Tariffa Oraria");
			txtfldCostoOrario.setEnabled(false);
			txtfldCostoOrario.setValue("0.00");
			
			txtfldOreEseguiteRegistrate.setFieldLabel("Ore Eseguite");
			txtfldOreEseguiteRegistrate.setEnabled(true);
			txtfldOreEseguiteRegistrate.setValue("0.00");
			txtfldOreEseguiteRegistrate.setRegex("[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
			txtfldOreEseguiteRegistrate.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
			txtfldOreEseguiteRegistrate.addKeyListener(new KeyListener(){
				
				public void componentKeyUp(ComponentEvent event) {
		    	  	if(hasValue(txtfldOreEseguiteRegistrate)&&txtfldOreEseguiteRegistrate.getValue()!=null){
		    	  		txtfldOreEseguiteRegistrate.clearInvalid();
		    	  		String scaricate= new String();
		    	  		String delta= new String();
		    	  		String totaleEuro= new String();
		    	  		String variazionePCL= new String();
		    	  		NumberFormat number = NumberFormat.getFormat("0.00");
		    	  		//number=NumberFormat.getCurrencyFormat("EUR");
		    	  		variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
		    	  		
		    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
		    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
		    	  		txtfldOreScaricate.setValue(scaricate);
		    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreScaricate.getValue().toString()));
		    	  		txtOreScaricate.setText("("+totaleEuro+")");
		    	  		
		    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
		    	  		txtfldDiffScaricateEseguite.setValue(delta);
		    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldDiffScaricateEseguite.getValue().toString()));
		    	  		txtMargine.setText("("+totaleEuro+")");
						
		    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreDaFatturare.getValue().toString()));				
						txtfldTotFatturato.setValue(totaleEuro);
						txtOreDaFatturare.setText("("+totaleEuro+")");
		    	  	}
		      }
				
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldOreEseguiteRegistrate.getValue()==null)
								txtfldOreEseguiteRegistrate.setValue("0.00");
							else{
								String valore= txtfldOreEseguiteRegistrate.getValue().toString();
														
								if(valore.compareTo("")==0)
									valore ="0.00";
								else
									if(valore.indexOf(".")==-1)
										valore=valore+".00";
									else{
										int index=valore.indexOf(".");
										int length=valore.length();
										
										if(valore.substring(index+1, length).length()==1)
											valore=valore+"0";		
										else if(valore.substring(index+1, length).length()==0)
											valore=valore+"00";
									}
								txtfldOreEseguiteRegistrate.setValue(valore);
							}						
						}
				 }
			});
								
			layoutCol1.add(txtfldSALIniziale, new FormData("95%"));
			layoutCol1.add(txtfldPCLIniziale, new FormData("95%"));
						
			layoutCol2.add(txtfldOreOrdine, new FormData("95%"));
			layoutCol2.add(txtfldOreResiduoOrdine, new FormData("95%"));
			layoutCol2.add(txtfldCostoOrario, new FormData("95%"));
			layoutCol2.add(txtfldOreEseguiteRegistrate, new FormData("95%"));		
			
			layoutCol3.add(txtSalTotale, new FormData("95%"));
			layoutCol3.add(txtPclTotale, new FormData("95%"));
			
			RowData data = new RowData(.33, 1);
			data.setMargins(new Margins(5));
			
			cp.add(layoutCol2, data);
			cp.add(layoutCol1, data);
			cp.add(layoutCol3, data);
			
			VerticalPanel hp1=new VerticalPanel();
			hp1.add(cntpnlGrid);
			hp1.add(cp);
						
			fldsetRiepOreOrdine.add(hp1);
				
			FieldSet fldsetFattura=new FieldSet();
			fldsetFattura.setCollapsible(false);
			fldsetFattura.setExpanded(true);
			fldsetFattura.setHeading("Dati Fatturazione:");
			
			vp.add(fldsetFattura);
			
			ContentPanel cp1=new ContentPanel();
			cp1.setHeaderVisible(false);
			cp1.setSize(575, 80);
			cp1.setBorders(false);
			cp1.setBodyBorder(false);
			cp1.setFrame(false);
			cp1.setLayout(new RowLayout(Orientation.HORIZONTAL));
							
			txtSalTotale.setText("0.00");
			txtPclTotale.setText("0.00");
			txtPclTotale.setStyleAttribute("padding-top", "7px");
			
			txtfldOreDaFatturare.setFieldLabel("Ore da Fatturare");
			txtfldOreDaFatturare.setEnabled(true);
			txtfldOreDaFatturare.setAllowBlank(false);
			txtfldOreDaFatturare.setValue("0.00");
			txtfldOreDaFatturare.setRegex("[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
			txtfldOreDaFatturare.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
			txtfldOreDaFatturare.addKeyListener(new KeyListener(){
			      public void componentKeyUp(ComponentEvent event) {
			    	  	if(hasValue(txtfldOreDaFatturare)&&txtfldOreDaFatturare.getValue()!=null){
			    	  		txtfldOreDaFatturare.clearInvalid();
			    	  		String scaricate= new String();
			    	  		String delta= new String();
			    	  		String totaleEuro= new String();
			    	  		String variazionePCL= new String();
			    	  		NumberFormat number = NumberFormat.getFormat("0.00");
			    	  		//number=NumberFormat.getCurrencyFormat("EUR");
			    	  		variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
			    	  		
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
			    	  		txtfldOreScaricate.setValue(scaricate);
			    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreScaricate.getValue().toString()));
			    	  		txtOreScaricate.setText("("+totaleEuro+")");
			    	  		
			    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
			    	  		txtfldDiffScaricateEseguite.setValue(delta);
			    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldDiffScaricateEseguite.getValue().toString()));
			    	  		txtMargine.setText("("+totaleEuro+")");
							
			    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreDaFatturare.getValue().toString()));				
							txtfldTotFatturato.setValue(totaleEuro);
							txtOreDaFatturare.setText("("+totaleEuro+")");
			    	  	}
			      }
			      
			      public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldOreDaFatturare.getValue()==null)
								txtfldOreDaFatturare.setValue("0.00");
							else{
								String valore= txtfldOreDaFatturare.getValue().toString();
														
								if(valore.compareTo("")==0)
									valore ="0.00";
								else
									if(valore.indexOf(".")==-1)
										valore=valore+".00";
									else{
										int index=valore.indexOf(".");
										int length=valore.length();
										
										if(valore.substring(index+1, length).length()==1)
											valore=valore+"0";		
										else if(valore.substring(index+1, length).length()==0)
											valore=valore+"00";
									}
								txtfldOreDaFatturare.setValue(valore);
							}
							if(hasValue(txtfldOreDaFatturare)&&txtfldOreDaFatturare.getValue()!=null){
				    	  		txtfldOreDaFatturare.clearInvalid();
				    	  		String scaricate= new String();
				    	  		String delta= new String();
				    	  		String totaleEuro= new String();
				    	  		String variazionePCL= new String();
				    	  		NumberFormat number = NumberFormat.getFormat("0.00");
				    	  		//number=NumberFormat.getCurrencyFormat("EUR");
				    	  		variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
				    	  		
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
				    	  		txtfldOreScaricate.setValue(scaricate);
				    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreScaricate.getValue().toString()));
				    	  		txtOreScaricate.setText("("+totaleEuro+")");
				    	  		
				    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
				    	  		txtfldDiffScaricateEseguite.setValue(delta);
				    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldDiffScaricateEseguite.getValue().toString()));
				    	  		txtMargine.setText("("+totaleEuro+")");
								
				    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreDaFatturare.getValue().toString()));				
								txtfldTotFatturato.setValue(totaleEuro);
								txtOreDaFatturare.setText("("+totaleEuro+")");
				    	  	}
						}	    		
				      }
			});
			
			txtfldVariazioneSAL.setFieldLabel("Variazione SAL");
			txtfldVariazioneSAL.setEnabled(true);
			txtfldVariazioneSAL.setAllowBlank(false);
			txtfldVariazioneSAL.setValue("0.00");
			txtfldVariazioneSAL.setRegex("[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[-]{1}[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|[-]{1}[0-9]+[.]{1}[0]{1}|0.00|0.0");
			txtfldVariazioneSAL.getMessages().setRegexText("Deve essere un numero nel formato 99.59 o -99.59");
			txtfldVariazioneSAL.addKeyListener(new KeyListener(){
				 public void componentKeyUp(ComponentEvent event) {
			    	  	if(hasValue(txtfldVariazioneSAL)&&txtfldVariazioneSAL.getValue()!=null){
			    	  		txtfldVariazioneSAL.clearInvalid();
			    	  		String scaricate= new String();
			    	  		String delta= new String();
			    	  		String variazionePCL= new String();
			    	  		String totaleEuro= new String();
			    	  		
			    	  		NumberFormat number = NumberFormat.getFormat("0.00");
			    	  		
			    	  		txtSalTotale.setText("0.00");
			    	  		
			    	  		variazionePCL=number.format(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
			    	  		txtfldOreScaricate.setValue(scaricate);
			    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreScaricate.getValue().toString()));
			    	  		txtOreScaricate.setText("("+totaleEuro+")");
			    	  		
			    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
			    	  		txtfldDiffScaricateEseguite.setValue(delta);
			    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldDiffScaricateEseguite.getValue().toString()));
			    	  		txtMargine.setText("("+totaleEuro+")");
			    	  		
			    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldVariazioneSAL.getValue().toString()));
			    	  		txtVariazioneSal.setText("("+totaleEuro+")");
			    	  		
			    	  		txtSalTotale.setText(ClientUtility.aggiornaTotGenerale(txtfldSALIniziale.getValue().toString(), txtfldVariazioneSAL.getValue().toString()));
			    	  	}
			      }	
				 
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldVariazioneSAL.getValue()==null)
								txtfldVariazioneSAL.setValue("0.00");
							else{
								String valore= txtfldVariazioneSAL.getValue().toString();
														
								if(valore.compareTo("")==0)
									valore ="0.00";
								else
									if(valore.indexOf(".")==-1)
										valore=valore+".00";
									else{
										int index=valore.indexOf(".");
										int length=valore.length();
										
										if(valore.substring(index+1, length).length()==1)
											valore=valore+"0";		
										else if(valore.substring(index+1, length).length()==0)
											valore=valore+"00";
									}
								txtfldVariazioneSAL.setValue(valore);
							}
							
							if(hasValue(txtfldVariazioneSAL)&&txtfldVariazioneSAL.getValue()!=null){
				    	  		txtfldVariazioneSAL.clearInvalid();
				    	  		String scaricate= new String();
				    	  		String delta= new String();
				    	  		String variazionePCL= new String();
				    	  		String totaleEuro= new String();
				    	  		
				    	  		NumberFormat number = NumberFormat.getFormat("0.00");
				    	  		
				    	  		txtSalTotale.setText("0.00");
				    	  		
				    	  		variazionePCL=number.format(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
				    	  		txtfldOreScaricate.setValue(scaricate);
				    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreScaricate.getValue().toString()));
				    	  		txtOreScaricate.setText("("+totaleEuro+")");
				    	  		
				    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
				    	  		txtfldDiffScaricateEseguite.setValue(delta);
				    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldDiffScaricateEseguite.getValue().toString()));
				    	  		txtMargine.setText("("+totaleEuro+")");
				    	  		
				    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldVariazioneSAL.getValue().toString()));
				    	  		txtVariazioneSal.setText("("+totaleEuro+")");
				    	  		
				    	  		txtSalTotale.setText(ClientUtility.aggiornaTotGenerale(txtfldSALIniziale.getValue().toString(), txtfldVariazioneSAL.getValue().toString()));
				    	  	}
						}	    		
				      }
				 
			});
			
			txtfldVariazionePCL.setFieldLabel("Variazione PCL");
			txtfldVariazionePCL.setEnabled(true);
			txtfldVariazionePCL.setAllowBlank(false);
			txtfldVariazionePCL.setValue("0.00");
			txtfldVariazionePCL.setRegex("[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[-]{1}[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|[-]{1}[0-9]+[.]{1}[0]{1}|0.00|0.0");
			txtfldVariazionePCL.getMessages().setRegexText("Deve essere un numero nel formato 99.59 o -99.59");
			txtfldVariazionePCL.addKeyListener(new KeyListener(){
				 public void componentKeyUp(ComponentEvent event) {
			    	  	if(hasValue(txtfldVariazioneSAL)&&txtfldVariazionePCL.getValue()!=null){
			    	  		txtfldVariazionePCL.clearInvalid();
			    	  		String scaricate= new String();
			    	  		String delta= new String();
			    	  		String variazionePCL= new String();
			    	  		String totaleEuro=new String();
			    	  		String decimali= new String();
			    	  		
			    	  		txtPclTotale.setText("0.00");
			    	  		NumberFormat number = NumberFormat.getFormat("0.00");
			    	  		//number=NumberFormat.getCurrencyFormat("EUR");
			    	  		
			    	  		//variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
			    	  		variazionePCL=number.format(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
			    	  					    	  		
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
			    	  		txtfldOreScaricate.setValue(scaricate);
			    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreScaricate.getValue().toString()));
			    	  		txtOreScaricate.setText("("+totaleEuro+")");
			    	  		
			    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
			    	  		txtfldDiffScaricateEseguite.setValue(delta);
			    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldDiffScaricateEseguite.getValue().toString()));
			    	  		txtMargine.setText("("+totaleEuro+")");
			    	  		
			    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldVariazionePCL.getValue().toString()));
			    	  		txtVariazionePcl.setText("("+totaleEuro+")");
			    	  		
			    	  		txtPclTotale.setText(ClientUtility.aggiornaTotGenerale(txtfldPCLIniziale.getValue().toString(), txtfldVariazionePCL.getValue().toString()));
			    	  	}
			      }	
				 
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldVariazionePCL.getValue()==null)
								txtfldVariazionePCL.setValue("0.00");
							else{
								String valore= txtfldVariazionePCL.getValue().toString();
														
								if(valore.compareTo("")==0)
									valore ="0.00";
								else
									if(valore.indexOf(".")==-1)
										valore=valore+".00";
									else{
										int index=valore.indexOf(".");
										int length=valore.length();
										
										if(valore.substring(index+1, length).length()==1)
											valore=valore+"0";		
										else if(valore.substring(index+1, length).length()==0)
											valore=valore+"00";
									}
								txtfldVariazionePCL.setValue(valore);
							}
							
							if(hasValue(txtfldVariazioneSAL)&&txtfldVariazionePCL.getValue()!=null){
				    	  		txtfldVariazionePCL.clearInvalid();
				    	  		String scaricate= new String();
				    	  		String delta= new String();
				    	  		String variazionePCL= new String();
				    	  		String totaleEuro=new String();
				    	  		String decimali= new String();
				    	  		
				    	  		txtPclTotale.setText("0.00");
				    	  		NumberFormat number = NumberFormat.getFormat("0.00");
				    	  		//number=NumberFormat.getCurrencyFormat("EUR");
				    	  		
				    	  		//variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
				    	  		variazionePCL=number.format(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
				    	  					    	  		
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
				    	  		txtfldOreScaricate.setValue(scaricate);
				    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreScaricate.getValue().toString()));
				    	  		txtOreScaricate.setText("("+totaleEuro+")");
				    	  		
				    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
				    	  		txtfldDiffScaricateEseguite.setValue(delta);
				    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldDiffScaricateEseguite.getValue().toString()));
				    	  		txtMargine.setText("("+totaleEuro+")");
				    	  		
				    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldVariazionePCL.getValue().toString()));
				    	  		txtVariazionePcl.setText("("+totaleEuro+")");
				    	  		
				    	  		txtPclTotale.setText(ClientUtility.aggiornaTotGenerale(txtfldPCLIniziale.getValue().toString(), txtfldVariazionePCL.getValue().toString()));
				    	  	}
						}	    		
				      }
			});
			
			txtfldOreScaricate.setFieldLabel("Ore Scaricate");
			txtfldOreScaricate.setEnabled(false);
			
			txtfldDiffScaricateEseguite.setFieldLabel("Diff. Scar./Eseg.");
			txtfldDiffScaricateEseguite.setEnabled(false);
			txtfldDiffScaricateEseguite.setValue("0.00");
			
			txtfldTotFatturato.setFieldLabel("Totale(Euro)");
			txtfldTotFatturato.setEnabled(false);
			
			txtaNote.setFieldLabel("Note per la fatturazione");
			txtaNote.setHeight(85);
			txtaNote.setMaxLength(500);
			
			RowData data1 = new RowData(.20, 1);
			data.setMargins(new Margins(5));
			
			LayoutContainer layoutColumn=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn.setLayout(layout);
			layoutColumn.add(txtfldOreDaFatturare, new FormData("46%"));
			layoutColumn.add(txtOreDaFatturare);
			cp1.add(layoutColumn, data1);
			
			LayoutContainer layoutColumn1=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn1.setLayout(layout);
			layoutColumn1.add(txtfldVariazioneSAL, new FormData("46%"));
			layoutColumn1.add(txtVariazioneSal);
			cp1.add(layoutColumn1, data1);
			
			LayoutContainer layoutColumn2=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn2.setLayout(layout);
			layoutColumn2.add(txtfldVariazionePCL, new FormData("46%"));
			layoutColumn2.add(txtVariazionePcl);
			cp1.add(layoutColumn2, data1);
			
			LayoutContainer layoutColumn3=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn3.setLayout(layout);
			layoutColumn3.add(txtfldOreScaricate, new FormData("46%"));
			layoutColumn3.add(txtOreScaricate);
			cp1.add(layoutColumn3, data1);
			
			LayoutContainer layoutColumn4=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn4.setLayout(layout);
			layoutColumn4.add(txtfldDiffScaricateEseguite, new FormData("46%"));
			layoutColumn4.add(txtMargine);
			cp1.add(layoutColumn4, data1);
			
			fldsetFattura.add(cp1);
			
			ContentPanel cp2=new ContentPanel();
			cp2.setHeaderVisible(false);
			cp2.setSize(575, 120);
			cp2.setBorders(false);
			cp2.setBodyBorder(false);
			cp2.setFrame(false);
			
			cp2.setLayout(new RowLayout(Orientation.HORIZONTAL));
			
			RowData data2 = new RowData(.55, 1);
						
			LayoutContainer layoutColumnTxtArea=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(150);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumnTxtArea.setLayout(layout);
			layoutColumnTxtArea.add(txtaNote, new FormData("90%"));
			cp2.add(layoutColumnTxtArea, data2);
			
			LayoutContainer layoutColumn5=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn5.setLayout(layout);
			layoutColumn5.add(txtfldTotFatturato, new FormData("65%"));
			cp2.add(layoutColumn5, data1);	
			
			fldsetFattura.add(cp2);
			
			add(vp);
			layout();		
		}
		
		private void caricaTabellaDati() {
			String data= new String(); //Formato Feb2013
			data=smplcmbxMese.getRawValue().toString().substring(0,3)+smplcmbxAnno.getRawValue().toString();
			
	    	AdministrationService.Util.getInstance().getElencoCommesseSuFoglioFatturazione(numCommessa, numEstensione,  data, new AsyncCallback<List<RiepilogoOreTotaliCommesse>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getElencoCommesseSuFoglioFatturazione();");
    				caught.printStackTrace();
				}

				@Override
				public void onSuccess(List<RiepilogoOreTotaliCommesse> result) {
					if(result==null)
		    			Window.alert("error: Impossibile efettuare il caricamento dei dati Ordini in tabella.");
		    		else	
		    			loadTable(result);					
				}
			});
		}
		
		private void loadTable(List<RiepilogoOreTotaliCommesse> lista) {
			try {
				ListStore<RiepilogoOreTotaliCommesse>store = new ListStore<RiepilogoOreTotaliCommesse>();
				store.add(lista);
				store.setDefaultSort("numeroCommessa", SortDir.ASC);
				gridOrdine.reconfigure(store, cmOrdine);
				
			} catch (NullPointerException e) {
					e.printStackTrace();
			}	
		}
		

		private List<ColumnConfig> createColumns() {
			
			List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			final NumberFormat number = NumberFormat.getFormat("0.00");
			
			ColumnConfig column=new ColumnConfig();		
		    column.setId("numeroCommessa");  
		    column.setHeader("Commessa");  
		    column.setWidth(75);  
		    column.setRowHeader(true);  
		    configs.add(column); 
		    
		    column=new ColumnConfig();		
		    column.setId("estensione");  
		    column.setHeader("Estens.");  
		    column.setWidth(50);  
		    column.setRowHeader(true);  
		    configs.add(column);
		    
		    column=new ColumnConfig();		
		    column.setId("sal");  
		    column.setHeader("Var.SAL");  
		    column.setWidth(60);  
		    column.setRowHeader(true); 
		    column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {

				@Override
				public Object render(RiepilogoOreTotaliCommesse model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<RiepilogoOreTotaliCommesse> store, Grid<RiepilogoOreTotaliCommesse> grid) {
					
					Float n=model.get(property);
					return number.format(n);
				}
			});
		    configs.add(column); 
		    
		    column=new ColumnConfig();		
		    column.setId("pcl");  
		    column.setHeader("Var.PCL");  
		    column.setWidth(60);  
		    column.setRowHeader(true); 
		    column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {

				@Override
				public Object render(RiepilogoOreTotaliCommesse model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<RiepilogoOreTotaliCommesse> store, Grid<RiepilogoOreTotaliCommesse> grid) {
					
					Float n=model.get(property);
					return number.format(n);
				}
			});
		    configs.add(column); 
		    
			 column=new ColumnConfig();		
		    column.setId("numeroOrdine");  
		    column.setHeader("Ordine");  
		    column.setWidth(80);  
		    column.setRowHeader(true);  
		    configs.add(column);  
		    
		    column=new ColumnConfig();		
		    column.setId("oreOrdine");
		    column.setHeader("Ore Eseg.");  
		    column.setWidth(65);  
		    column.setRowHeader(true);  
		    configs.add(column);
		    
		    column=new ColumnConfig();		
		    column.setId("compilato");//flag per indicare se è già presente il foglio ore del mese  
		    column.setHeader("");  
		    column.setWidth(20);  
		    column.setRowHeader(true);  
		    column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {

				@Override
				public Object render(RiepilogoOreTotaliCommesse model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<RiepilogoOreTotaliCommesse> store, Grid<RiepilogoOreTotaliCommesse> grid) {
					
					String numeroCommessa=model.get("numeroCommessa");
					if(numeroCommessa.compareTo("TOTALE")!=0)
					{
						String color = "#F08080";
						String flag=model.get("compilato");
						if(flag.compareTo("Si")==0)
							color = "#90EE90";                    
						config.style = config.style + ";background-color:" + color + ";";									
					}
					else{
						config.style = config.style + ";background-color:" + "#FFFFFF" + ";";
					}
					return "";
				}
			});
		    configs.add(column);	    
		    return configs;
		}
		
		
		private void caricaDatiFatturazioneOrdine(String numeroCommessa) {	
			String meseR= new String();
			String anno= new String();
			String data= new String();
			
			final String commessa=numeroCommessa;
			
			if(smplcmbxMese.isValid()&&smplcmbxAnno.isValid()){
				anno=smplcmbxAnno.getRawValue().toString();
				meseR=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
				data=meseR+anno;
				AdministrationService.Util.getInstance().getDatiFatturazionePerOrdine(commessa, data, new AsyncCallback<FoglioFatturazioneModel>() {
					@Override
					public void onSuccess(FoglioFatturazioneModel result) {
						if(result==null)
							Window.alert("error: Problemi durante l'accesso ai dati di fatturazione dell'ordine selezionato.");
						else	
							if(result.getStato().compareTo("0")==0) {
								nuovo=true;
								loadFormFatturazione(result, commessa);
							}							
							else{
								nuovo=false;
								loadFormFatturazione(result, commessa);
							}							
					}
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on getDatiFatturazionePerOrdine();");
						caught.printStackTrace();		
					}
				}); //AsyncCallback	  		
			}else{Window.alert("error: Dati selezionati non corretti.");}
		}
		
		
		protected boolean hasValue(TextField<String> field) {
		    return field.getValue() != null && field.isValid();
		}
		
		
		private void loadFormFatturazione(FoglioFatturazioneModel result, String numeroCommessa) {			
					
			ListStore<RiepilogoOreDipFatturazione> store= new ListStore<RiepilogoOreDipFatturazione>();
			List<RiepilogoOreDipFatturazione> lista= new ArrayList<RiepilogoOreDipFatturazione>();
			String ncommessa= new String();
			String totOre=new String();
			String delta= new String();
			String scaricate= new String();
			String totaleEuro= new String();
			String variazionePCL=new String();
				
			NumberFormat number= NumberFormat.getFormat("0.00");
						
			HorizontalPanel hp= new HorizontalPanel();
			CntpnlRiepilogoOreDipFatturazione cp;
					
			try {
				totOre= "0.0";			
				hp=(HorizontalPanel) getParent();//se non è presente una lista faccio comunque apparire gli ordini da "fatturare" in modo tale da permettere l'inserimento di ore di sal
				cp=(CntpnlRiepilogoOreDipFatturazione) hp.getItemByItemId("panelRiepilogoOre");
				store=cp.gridRiepilogo.getStore();
				
				//prelevo la lista di 
				lista.addAll(store.getModels());
					
				//prendo il numero di ore eseguite dalla commessa selezionata
				for(RiepilogoOreDipFatturazione riep: lista){
					ncommessa=riep.getNumeroCommessa().substring(0,riep.getNumeroCommessa().indexOf("(")-1);			
					if(ncommessa.compareTo(numeroCommessa)==0 &&
							(ncommessa.substring(ncommessa.length()-2, ncommessa.length()).compareTo("pa")!=0) &&
								(riep.getDipendente().compareTo(".TOTALE")==0)){
						String numeroFormattato= new String();
						numeroFormattato=number.format(riep.getOreTotali());
						totOre=	ClientUtility.aggiornaTotGenerale(totOre, numeroFormattato);
					}					
				}
				if(!nuovo){
					
	    	  		variazionePCL=number.format(Float.valueOf(result.getVariazionePcl())*(-1));
	    	  		
	
					scaricate=ClientUtility.aggiornaTotGenerale(result.getOreFatturate(), result.getVariazioneSal());
					scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
									    
					totaleEuro=number.format(result.getTariffaOraria()*Float.valueOf(result.getOreFatturate()));
						
					txtSalTotale.setText(ClientUtility.aggiornaTotGenerale(result.getsalAttuale(), result.getVariazioneSal()));
					txtPclTotale.setText(ClientUtility.aggiornaTotGenerale(result.getPclAttuale(), result.getVariazionePcl()));
					
					txtfldOreOrdine.setValue(result.getOreOrdine());
					txtfldOreResiduoOrdine.setValue(result.getResiduoOre());
					txtfldCostoOrario.setValue(number.format(result.getTariffaOraria()));
					txtfldSALIniziale.setValue(result.getsalAttuale());
					txtfldPCLIniziale.setValue(result.getPclAttuale());
					txtfldOreDaFatturare.setValue(result.getOreFatturate());
					txtfldVariazioneSAL.setValue(result.getVariazioneSal());
					txtfldVariazionePCL.setValue(result.getVariazionePcl());
					txtaNote.setValue(result.getNote());
						
					txtfldOreEseguiteRegistrate.setValue(result.getOreEseguiteRegistrate());
					
					txtfldTotFatturato.setValue(totaleEuro);
					
					delta=ClientUtility.calcoloDelta(scaricate, result.getOreEseguiteRegistrate());//se uso l'inserimento manuale
					txtfldDiffScaricateEseguite.setValue(delta);
					
					txtfldOreScaricate.setValue(scaricate);
					txtfldOreDaFatturare.setValue(result.getOreFatturate());
	    	  		
					totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreDaFatturare.getValue().toString()));
	    	  		txtOreDaFatturare.setText("("+totaleEuro+")");
					
	    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreScaricate.getValue().toString()));
	    	  		txtOreScaricate.setText("("+totaleEuro+")");
	    	  		
	    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldDiffScaricateEseguite.getValue().toString()));
	    	  		txtMargine.setText("("+totaleEuro+")");
	    	  		
	    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldVariazioneSAL.getValue().toString()));
	    	  		txtVariazioneSal.setText("("+totaleEuro+")");
	    	  		
	    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldVariazionePCL.getValue().toString()));
	    	  		txtVariazionePcl.setText("("+totaleEuro+")");
					
				}else{
									
					txtSalTotale.setText(ClientUtility.aggiornaTotGenerale(result.getsalAttuale(), result.getVariazioneSal()));
					txtPclTotale.setText(ClientUtility.aggiornaTotGenerale(result.getPclAttuale(), result.getVariazionePcl()));
					
					txtfldOreOrdine.setValue(result.getOreOrdine());
					txtfldOreResiduoOrdine.setValue(result.getResiduoOre());
					txtfldCostoOrario.setValue(number.format(result.getTariffaOraria()));
					txtfldSALIniziale.setValue(result.getsalAttuale());
					txtfldPCLIniziale.setValue(result.getPclAttuale());
					txtfldOreDaFatturare.setValue(result.getOreFatturate());
					txtfldVariazioneSAL.setValue(result.getVariazioneSal());
					txtfldVariazionePCL.setValue(result.getVariazionePcl());
					txtfldOreScaricate.setValue("0.00");
					
					txtfldOreEseguiteRegistrate.setValue(totOre);
					//txtfldOreEseguiteRegistrate.setValue(result.getOreEseguiteRegistrate());
					
					txtfldTotFatturato.setValue("0.00");
					txtfldDiffScaricateEseguite.setValue("0.00");
					txtfldOreScaricate.setValue("0.00");
					txtfldOreDaFatturare.setValue("0.00");
					
					totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreDaFatturare.getValue().toString()));
	    	  		txtOreDaFatturare.setText("("+totaleEuro+")");
					
	    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreScaricate.getValue().toString()));
	    	  		txtOreScaricate.setText("("+totaleEuro+")");
	    	  		
	    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldDiffScaricateEseguite.getValue().toString()));
	    	  		txtMargine.setText("("+totaleEuro+")");
	    	  		
	    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldVariazioneSAL.getValue().toString()));
	    	  		txtVariazioneSal.setText("("+totaleEuro+")");
	    	  		
	    	  		totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldVariazionePCL.getValue().toString()));
	    	  		txtVariazionePcl.setText("("+totaleEuro+")");				
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("errore load FormFatturazione.");
			}
			
		}
	}
}
