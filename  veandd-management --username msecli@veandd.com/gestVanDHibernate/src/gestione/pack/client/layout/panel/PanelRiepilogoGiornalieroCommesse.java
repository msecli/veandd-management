package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.RiepilogoOreDipCommesseGiornaliero;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class PanelRiepilogoGiornalieroCommesse extends LayoutContainer{	
	
	private GroupingStore<RiepilogoOreDipCommesseGiornaliero>store = new GroupingStore<RiepilogoOreDipCommesseGiornaliero>();
	private Grid<RiepilogoOreDipCommesseGiornaliero> gridRiepilogo;
	private ColumnModel cmCommessa;
	private String username= "";
	private Date data;
	private Button btnPrint= new Button();
	private Button btnViewFoglioOre= new Button();
	private int idDip;
	private String tipoL="0"; 
	private TextField<String> txtOreTotaliIntCommesse;
	private TextField<String> txtOreTotaliIntIU;
	private Text txtCheck;
	
	//com.google.gwt.user.client.ui.Button btnPrint1 = new com.google.gwt.user.client.ui.Button("Stampa");
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	private String dataSel= new String();
	private String dipSel= new String();
	
	public PanelRiepilogoGiornalieroCommesse(String user, Date dataRiferimento){
		username=user;
		data=dataRiferimento;	
	}
			
	public PanelRiepilogoGiornalieroCommesse(int id, String anno, String mese, String tipo) {//tipo 1
		idDip=id;
		
		mese=mese.substring(0,1).toLowerCase()+mese.substring(1,mese.length());
		mese=ClientUtility.traduciMeseToNumber(mese);
		
		Date retVal = null;
	        try
	        {
	            retVal = DateTimeFormat.getFormat( "MM-yyyy" ).parse(mese+"-"+anno);
	        }
	        catch ( Exception e )
	        {
	            retVal = null;
	        }
		
	    data=retVal;
		tipoL=tipo;
	}

	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    setItemId("pnlRiepilogo");
	    
	    final FitLayout fl= new FitLayout();
	    LayoutContainer layoutContainer= new LayoutContainer();
	    layoutContainer.setBorders(false);
	    layoutContainer.setLayout(fl);	
	  		
	    LayoutContainer bodyContainer = new LayoutContainer();
	    bodyContainer.setLayout(new FlowLayout());
	  	bodyContainer.setBorders(false);
	  	  	  	
	  	ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setBodyBorder(false);         
		cntpnlGrid.setLayout(new FitLayout());  
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setWidth(500);
		cntpnlGrid.setHeight(605);
		cntpnlGrid.setScrollMode(Scroll.AUTOY);
		
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
	  	
	   	btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
	   	btnPrint.setIconAlign(IconAlign.TOP);
	   	btnPrint.setSize(26, 26);
		btnPrint.setToolTip("Stampa");
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				
				String dataRif=data.toString();
				String mese=dataRif.substring(4, 7);
				
				String anno=dataRif.substring(dataRif.length()-4,dataRif.length());
			    String totOreCommesse= txtOreTotaliIntCommesse.getValue().toString();
			    String totOreIU= txtOreTotaliIntIU.getValue().toString();
				
				mese=ClientUtility.traduciMeseToIt(mese);
				
			    dataRif=mese+anno;
			    
				SessionManagementService.Util.getInstance().setDataInSession(dataRif, username, "COMM", totOreCommesse,
						totOreIU, new AsyncCallback<Boolean>() {

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
		
		btnViewFoglioOre.setStyleAttribute("padding-left", "2px");
	  	btnViewFoglioOre.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
	  	btnViewFoglioOre.setIconAlign(IconAlign.TOP);
	  	btnViewFoglioOre.setToolTip("Modifica dati giornalieri.");
	  	btnViewFoglioOre.setSize(26, 26);
	  	btnViewFoglioOre.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				  String giorno=dataSel.substring(0, 2);
				  String mese=dataSel.substring(3,6);
				  String anno= dataSel.substring(7,dataSel.length());
				 // mese=mese.substring(0, 1).toUpperCase()+mese.substring(1, mese.length());
				  mese=ClientUtility.traduciMeseToNumber(mese);
				  Date retVal = null;
			      
				  try
			        {
			            retVal = DateTimeFormat.getFormat( "dd-MM-yyyy" ).parse( giorno+"-"+mese+"-"+anno );
			        }
			      catch ( Exception e )
			        {
			            retVal = null;
			        }	    						
					Dialog d =new  DialogRilevazionePresenze(retVal,dipSel);
					d.setModal(false);
					d.show();
								
					d.addListener(Events.Hide, new Listener<ComponentEvent>() {			     
						@Override
						public void handleEvent(ComponentEvent be) {
								
						}
					});	
				}
		});
		
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
		
		txtCheck= new Text("!!!E' Presente un'incongruenza tra i dati inseriti nel mese!!!");
		txtCheck.setStyleAttribute("padding-top", "10px");
		txtCheck.setStyleAttribute("font-size", "15px");
		txtCheck.setStyleAttribute("color", "red");
		txtCheck.setVisible(false);
		
		cntpnlTxtField.add(txtOreTotaliIntIU);
		cntpnlTxtField.add(txtOreTotaliIntCommesse);
		cntpnlTxtField.add(txtCheck);
		
		if(tipoL.compareTo("0")==0)
			fp.add(btnPrint);
		else
			fp.add(btnViewFoglioOre);
		fp.setMethod(FormPanel.METHOD_POST);
	    fp.setAction(url);
	    fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
	   					
		caricaTabellaDati();
	    
	    try {	    
	    		cmCommessa = new ColumnModel(createColumns());
	    	} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	    
	    store.groupBy("numeroCommessa");
	    store.setSortField("giorno");    
	          
	    GroupSummaryView summary = new GroupSummaryView();  
	    summary.setForceFit(true);  
	    summary.setShowGroupedColumn(false);
	    summary.setStartCollapsed(false);
		    
	    gridRiepilogo= new Grid<RiepilogoOreDipCommesseGiornaliero>(store, cmCommessa);  
	    gridRiepilogo.setItemId("grid");
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setView(summary);  
	    gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);
	    gridRiepilogo.getView().setShowDirtyCells(false);
	    gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	    	    
	    gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<RiepilogoOreDipCommesseGiornaliero>>() {  
	          public void handleEvent(SelectionChangedEvent<RiepilogoOreDipCommesseGiornaliero> be) {  
	        	
	            if (be.getSelection().size() > 0) {      
	            	String d=be.getSelectedItem().get("giorno");
	            	dataSel= d;
					dipSel= be.getSelectedItem().get("username");										
	            		            		               		            	
	            } else {  
	                
	           }
	         }
	    });
	    gridRiepilogo.getView().setViewConfig(new GridViewConfig(){
	    	@Override
	        public String getRowStyle(ModelData model, int rowIndex, ListStore<ModelData> ds) {
	    		if (model != null) {	    
	            	String stato= new String();
	            	stato= model.get("compilato");
	                if (stato.compareTo("N")==0) 
	                    return "red-row";               
	                else if (model.get("compilato").toString().compareTo("S")==0) 
	                    return "green-row";
	            }
				return "";            
	    	}    	
	    });
	    
	
	    cntpnlGrid.add(gridRiepilogo);	   
	    
	    ContentPanel cntpnlLayout= new ContentPanel();
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setCollapsible(false);
		cntpnlLayout.setExpanded(true);
		cntpnlLayout.setHeading("Riepilogo Giornaliero.");
		cntpnlLayout.setSize(515, 755);
		cntpnlLayout.setFrame(true);
		cntpnlLayout.add(fp);
		cntpnlLayout.add(cntpnlGrid);
		cntpnlLayout.add(cntpnlTxtField);
	    
		bodyContainer.add(cntpnlLayout);    
		
		layoutContainer.add(bodyContainer, new FitData(3, 3, 3, 3));
		add(layoutContainer);    
	}


	private void caricaTabellaDati() {
		
		AdministrationService.Util.getInstance().getRiepilogoGiornalieroCommesse(idDip, username, data,  new AsyncCallback<List<RiepilogoOreDipCommesseGiornaliero>>() {	
			@Override
			public void onSuccess(List<RiepilogoOreDipCommesseGiornaliero> result) {
				if(result==null)
					Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
				else	
					if(result.size()==0){
						//Window.alert("Nessun dato rilevato in base ai criteri di ricerca selezionati.");				
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
	
	
	private void loadTable(List<RiepilogoOreDipCommesseGiornaliero> result) {
		try {
			final NumberFormat number = NumberFormat.getFormat("0.00");
			RiepilogoOreDipCommesseGiornaliero recordTotali= new RiepilogoOreDipCommesseGiornaliero();
			recordTotali=result.remove(result.size()-1);
			
			txtOreTotaliIntCommesse.setValue(number.format(recordTotali.getTotOre()));
			txtOreTotaliIntIU.setValue(number.format(recordTotali.getOreViaggio()));
			
			if(number.format(recordTotali.getTotOre()).compareTo(number.format(recordTotali.getOreViaggio()))!=0)
				txtCheck.setVisible(true);
			else
				txtCheck.setVisible(false);
			
			store.removeAll();
			store.add(result);
			gridRiepilogo.reconfigure(store, cmCommessa);				
		} catch (NullPointerException e) {
			Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
				e.printStackTrace();
		}
	}

	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number= NumberFormat.getFormat("0.00");
		       
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("numeroCommessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(85);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("giorno");  
	    column.setHeader("Giorno");  
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    //column.setRenderer(renderer);
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("letteraGiorno");  
	    column.setHeader("");  
	    column.setWidth(15);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	       	    	    
	    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
	    columnOreLavoro.setId("oreLavoro");  
	    columnOreLavoro.setHeader("Ore Lavoro");  
	    columnOreLavoro.setWidth(60);    
	    columnOreLavoro.setRowHeader(true);  
	    columnOreLavoro.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreLavoro.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesseGiornaliero>() {
			@Override
			public Object render(RiepilogoOreDipCommesseGiornaliero model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreDipCommesseGiornaliero> store,
					Grid<RiepilogoOreDipCommesseGiornaliero> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	   
	    configs.add(columnOreLavoro); 	
	    
	    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
	    columnOreViaggio.setId("oreViaggio");  
	    columnOreViaggio.setHeader("Ore Viaggio");  
	    columnOreViaggio.setWidth(60);    
	    columnOreViaggio.setRowHeader(true);  
	    columnOreViaggio.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreViaggio.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesseGiornaliero>() {
			@Override
			public Object render(RiepilogoOreDipCommesseGiornaliero model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreDipCommesseGiornaliero> store,
					Grid<RiepilogoOreDipCommesseGiornaliero> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	        
	    configs.add(columnOreViaggio); 	
	    
	    SummaryColumnConfig<Double> columnOreTotali=new SummaryColumnConfig<Double>();		
	    columnOreTotali.setId("totOre");  
	    columnOreTotali.setHeader("Totale");  
	    columnOreTotali.setWidth(60);    
	    columnOreTotali.setRowHeader(true);   
	    columnOreTotali.setAlignment(HorizontalAlignment.RIGHT);   
	    columnOreTotali.setStyle("color:#e71d2b;");
	    columnOreTotali.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesseGiornaliero>() {
			@Override
			public Object render(RiepilogoOreDipCommesseGiornaliero model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreDipCommesseGiornaliero> store,
					Grid<RiepilogoOreDipCommesseGiornaliero> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});	       
	    configs.add(columnOreTotali); 		
		return configs;
	}
	
	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {
		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			 	
			String nome, cognome, nomeFile;
			
			nome=username.substring(0, username.indexOf("."));
			nome=nome.substring(0, 1).toUpperCase()+nome.substring(1,nome.length());
			
			cognome=username.substring(username.indexOf(".")+1, username.length());
			cognome=cognome.substring(0, 1).toUpperCase()+cognome.substring(1,cognome.length());
			
			nomeFile=cognome+nome+"_Report.pdf";
			
			Window.open("/FileStorage/RiepiloghiCommesse/"+nomeFile, "_blank", "1");			
		}
	}
}
