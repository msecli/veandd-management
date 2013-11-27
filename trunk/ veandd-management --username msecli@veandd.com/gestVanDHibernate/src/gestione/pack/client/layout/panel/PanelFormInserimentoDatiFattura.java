package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import sun.text.resources.FormatData;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.AttivitaFatturateModel;
import gestione.pack.client.model.CostingRisorsaModel;
import gestione.pack.client.model.FatturaModel;
import gestione.pack.client.model.TariffaOrdineModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.MyImages;
import gestione.pack.server.ServerUtility;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelFormInserimentoDatiFattura extends LayoutContainer{

	private TextField<String> txtfldTotaleImponibile= new TextField<String>();
	private TextField<String> txtfldTotaleIva= new TextField<String>();
	private TextField<String> txtfldTotaleImporto= new TextField<String>();
	private TextField<String> txtfldDataFattura= new TextField<String>();
	private TextField<String> txtfldNumeroFattura= new TextField<String>();
	private TextField<String> txtfldCondizioniPagamento= new TextField<String>();
	private TextField<String> txtfldAliquotaIva= new TextField<String>();
	private TextField<String> txtfldDescrizione;
	private TextField<String> txtfldImporto;
	private EditorGrid<AttivitaFatturateModel> gridAttivitaFatturate;
	private ListStore<AttivitaFatturateModel> storeAttivita= new ListStore<AttivitaFatturateModel>();
	private ColumnModel cm;
	private CellSelectionModel<AttivitaFatturateModel> cs;
	
	private Button btnAddAttivita;
	private Button btnDelAttivita;
	private Button btnPrintToPDF;
	private Button btnPrintToRTF;
	
	private String numeroOrdine;
	private int idFoglioFatturazione;
	private FatturaModel fM;
	
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	public PanelFormInserimentoDatiFattura(String numeroOrdine, int idFoglioFatturazione, FatturaModel fM){
		this.numeroOrdine=numeroOrdine;
		this.idFoglioFatturazione=idFoglioFatturazione;
		this.fM=fM;		
	}
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	    
	    final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		ContentPanel cntpnlLayout=new ContentPanel();
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setBorders(false);
		cntpnlLayout.setFrame(true);
		cntpnlLayout.setHeight(480);
		cntpnlLayout.setWidth(600);
		cntpnlLayout.setScrollMode(Scroll.NONE);
				
		ContentPanel cntpnlDatiAggiuntivi= new ContentPanel();
		cntpnlDatiAggiuntivi.setHeaderVisible(false);
		cntpnlDatiAggiuntivi.setLayout(new FitLayout());
		cntpnlDatiAggiuntivi.setBorders(false);
		cntpnlDatiAggiuntivi.setFrame(true);
		cntpnlDatiAggiuntivi.setHeight(160);
		cntpnlDatiAggiuntivi.setWidth(590);
		cntpnlDatiAggiuntivi.setScrollMode(Scroll.NONE);
		cntpnlDatiAggiuntivi.setLayout(new RowLayout(Orientation.HORIZONTAL));
		
		ContentPanel cntpnlDatiRiepilogo= new ContentPanel();
		cntpnlDatiRiepilogo.setHeaderVisible(false);
		cntpnlDatiRiepilogo.setLayout(new FitLayout());
		cntpnlDatiRiepilogo.setBorders(false);
		cntpnlDatiRiepilogo.setFrame(true);
		cntpnlDatiRiepilogo.setHeight(80);
		cntpnlDatiRiepilogo.setWidth(590);
		cntpnlDatiRiepilogo.setScrollMode(Scroll.NONE);
		cntpnlDatiRiepilogo.setLayout(new RowLayout(Orientation.HORIZONTAL));
		
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setLayout(new FitLayout());
		cntpnlGrid.setBorders(false);
		cntpnlGrid.setFrame(true);
		cntpnlGrid.setHeight(200);
		cntpnlGrid.setWidth(590);
		cntpnlGrid.setScrollMode(Scroll.NONE);
		
		LayoutContainer layoutCol1= new LayoutContainer();		
		FormLayout layout= new FormLayout();
		layout.setLabelWidth(160);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol1.setLayout(layout);
		
		LayoutContainer layoutCol2= new LayoutContainer();		
		layout= new FormLayout();
		layout.setLabelWidth(100);
		layout.setLabelAlign(LabelAlign.TOP);
		layoutCol2.setLayout(layout);
		
		LayoutContainer layoutCol3= new LayoutContainer();		
		layout= new FormLayout();
		layout.setLabelWidth(100);
		layout.setLabelAlign(LabelAlign.TOP);
		layoutCol3.setLayout(layout);
		
		LayoutContainer layoutCol4= new LayoutContainer();		
		layout= new FormLayout();
		layout.setLabelWidth(100);
		layout.setLabelAlign(LabelAlign.TOP);
		layoutCol4.setLayout(layout);
		
		txtfldNumeroFattura.setAllowBlank(false);
		txtfldNumeroFattura.setFieldLabel("Numero Fattura");
		txtfldNumeroFattura.setValue((String) fM.get("numeroFattura"));
		layoutCol1.add(txtfldNumeroFattura, new FormData("80%"));
		
		txtfldDataFattura.setAllowBlank(false);
		txtfldDataFattura.setFieldLabel("Data Fattura");
		txtfldDataFattura.setValue((String) fM.get("dataFattura"));
		layoutCol1.add(txtfldDataFattura, new FormData("80%"));
		
		txtfldAliquotaIva.setAllowBlank(false);
		txtfldAliquotaIva.setFieldLabel("IVA (%)");
		txtfldAliquotaIva.setValue((String) fM.get("iva"));
		layoutCol1.add(txtfldAliquotaIva, new FormData("65%"));
		
		txtfldCondizioniPagamento.setAllowBlank(false);
		txtfldCondizioniPagamento.setFieldLabel("Condizioni di pagamento");
		txtfldCondizioniPagamento.setValue((String) fM.get("condizioni"));
		layoutCol1.add(txtfldCondizioniPagamento, new FormData("80%"));		
		
		txtfldTotaleImponibile.setAllowBlank(false);
		txtfldTotaleImponibile.setFieldLabel("Totale Imponibile");
		txtfldTotaleImponibile.setEnabled(false);
		txtfldTotaleImponibile.setValue((String) fM.get("imponibile"));
		layoutCol2.add(txtfldTotaleImponibile, new FormData("100%"));
		
		txtfldTotaleIva.setAllowBlank(false);
		txtfldTotaleIva.setFieldLabel("Totale Iva");
		txtfldTotaleIva.setEnabled(false);
		txtfldTotaleIva.setValue((String) fM.get("totaleIva"));
		layoutCol3.add(txtfldTotaleIva, new FormData("100%"));
		
		txtfldTotaleImporto.setAllowBlank(false);
		txtfldTotaleImporto.setFieldLabel("Importo totale");
		txtfldTotaleImporto.setEnabled(false);
		txtfldTotaleImporto.setValue((String) fM.get("totaleImporto"));
		layoutCol4.add(txtfldTotaleImporto, new FormData("100%"));
		
		btnPrintToRTF = new Button();
		btnPrintToRTF.setToolTip("Stampa Fattura");
		btnPrintToRTF.setHeight(80);
		btnPrintToRTF.setWidth(80);
		btnPrintToRTF.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print64()));
		btnPrintToRTF.setIconAlign(IconAlign.BOTTOM);
		btnPrintToRTF.setStyleAttribute("padding-left", "10px");	
		btnPrintToRTF.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {			
								
				List<AttivitaFatturateModel> listaAM= storeAttivita.getModels();
				fM.set("listaAttivita", listaAM);
				
				SessionManagementService.Util.getInstance().setDataFattura(numeroOrdine, idFoglioFatturazione, fM, "STAMPAFATTURA",
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
		
		fp.setMethod(com.google.gwt.user.client.ui.FormPanel.METHOD_POST);
		fp.setAction(url);
		//fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
		fp.add(btnPrintToRTF);
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(fp);
		
		cs=new CellSelectionModel<AttivitaFatturateModel>();
	    cs.setSelectionMode(SelectionMode.SIMPLE);
   
	    cm=new ColumnModel(createColumns());
		gridAttivitaFatturate=new EditorGrid<AttivitaFatturateModel>(storeAttivita, cm);
		gridAttivitaFatturate.setBorders(false);  
		gridAttivitaFatturate.setItemId("grid");
		gridAttivitaFatturate.setColumnLines(true);
		gridAttivitaFatturate.setStripeRows(true);
		gridAttivitaFatturate.getView().setShowDirtyCells(false);
		gridAttivitaFatturate.setSelectionModel(cs);
				
		btnAddAttivita= new Button();
		btnAddAttivita.setSize(26, 26);
		btnAddAttivita.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.add()));
		btnAddAttivita.setIconAlign(IconAlign.BOTTOM);
		btnAddAttivita.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					AttivitaFatturateModel tm=new AttivitaFatturateModel();
					gridAttivitaFatturate.stopEditing();
				    storeAttivita.insert(tm, 0);//inserisco una riga vuota per l'inserimento
				    gridAttivitaFatturate.startEditing(storeAttivita.indexOf(tm), 0);
				}
		});
		    
		btnDelAttivita= new Button();
		btnDelAttivita.setSize(26, 26);
		btnDelAttivita.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.respingi()));
		btnDelAttivita.setIconAlign(IconAlign.BOTTOM);
		btnDelAttivita.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					AttivitaFatturateModel trf=	cs.getSelectedItem();
					storeAttivita.remove(trf);					
				}
		});
		
		ToolBar tlbGrid= new ToolBar();
		tlbGrid.add(btnAddAttivita);
		tlbGrid.add(new SeparatorToolItem());
		tlbGrid.add(btnDelAttivita);
		tlbGrid.add(new SeparatorToolItem());
		
		RowData data = new RowData(.70, 1);
		data.setMargins(new Margins(5));
		cntpnlDatiAggiuntivi.add(layoutCol1, data);
		cntpnlDatiAggiuntivi.add(btnPrintToRTF, new RowData(.20, .55, new Margins(5)));	
		
		RowData data1 = new RowData(.32, .5);
		data1.setMargins(new Margins(5));
		cntpnlDatiRiepilogo.add(layoutCol2,data1);
		cntpnlDatiRiepilogo.add(layoutCol3,data1);
		cntpnlDatiRiepilogo.add(layoutCol4,data1);
		
		cntpnlGrid.setTopComponent(tlbGrid);
		cntpnlGrid.add(gridAttivitaFatturate);
		
		VerticalPanel vp= new VerticalPanel();
		vp.setSpacing(5);
		
		vp.add(cntpnlDatiAggiuntivi);
		vp.add(cntpnlDatiRiepilogo);
		vp.add(cntpnlGrid);
		
		cntpnlLayout.add(vp);
		
		layoutContainer.add(cntpnlLayout, new FitData(3, 3, 3, 3));
		add(layoutContainer);	
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		CellEditor editorTxt;
		
		ColumnConfig column = new ColumnConfig();  
	    column.setId("descrizione");  
	    column.setHeader("Descrizione");  
	    column.setWidth(450);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);	    
	    txtfldDescrizione= new TextField<String>();
	    txtfldDescrizione.setAllowBlank(false);
	    editorTxt= new CellEditor(txtfldDescrizione){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column);
	    
	    
	    column = new ColumnConfig();  
	    column.setId("importo");  
	    column.setHeader("Importo");  
	    column.setWidth(100);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    txtfldImporto= new TextField<String>();
	    txtfldImporto.setAllowBlank(false);
	    txtfldImporto.setRegex("[0-9]+[.][0-9]+|[0-9]+");
		txtfldImporto.getMessages().setRegexText("Deve essere un numero, eventualmente nel formato 99.99");		
		txtfldImporto.addKeyListener(new KeyListener(){
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){		
							final NumberFormat number = NumberFormat.getFormat("0.00");
			    	  		String imponibile="0.00";
			    	  		String importoIva="0.00";
			    	  		String totaleIva="0.00";
			    	  		String totaleImporto="0.00";
			    	  		List<AttivitaFatturateModel> lista=storeAttivita.getModels();
			    	  		for(AttivitaFatturateModel f:lista){
			    	  			imponibile=ClientUtility.aggiornaTotGenerale(imponibile,(String) f.get("importo"));
								importoIva=number.format(Float.valueOf(txtfldAliquotaIva.getValue().toString())*Float.valueOf((String) f.get("importo"))/100);
								totaleIva=ClientUtility.aggiornaTotGenerale(totaleIva, importoIva);
								totaleImporto=ClientUtility.aggiornaTotGenerale(totaleImporto, ClientUtility.aggiornaTotGenerale((String) f.get("importo"), importoIva));			    	  					    	  			
			    	  		}
			    	  		txtfldTotaleImponibile.setValue(imponibile);
			    	  		txtfldTotaleIva.setValue(totaleIva);
			    	  		txtfldTotaleImporto.setValue(totaleImporto);
						}
				 }
		});
		
	    editorTxt= new CellEditor(txtfldImporto){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column);
		
	    return configs;
	}
	
	protected boolean hasValue(TextField<String> field) {
	    return field.getValue() != null && field.isValid();
	}
	
}
