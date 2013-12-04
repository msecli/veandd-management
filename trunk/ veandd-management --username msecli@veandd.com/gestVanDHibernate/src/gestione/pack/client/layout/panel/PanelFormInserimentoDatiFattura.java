package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.AttivitaFatturateModel;
import gestione.pack.client.model.FatturaModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class PanelFormInserimentoDatiFattura extends Dialog{

	private TextField<String> txtfldTotaleImponibile= new TextField<String>();
	private TextField<String> txtfldTotaleIva= new TextField<String>();
	private TextField<String> txtfldTotaleImporto= new TextField<String>();
	private TextField<String> txtfldNumeroFattura= new TextField<String>();
	private TextField<String> txtfldCondizioniPagamento= new TextField<String>();
	private TextField<String> txtfldAliquotaIva= new TextField<String>();
	private TextField<String> txtfldDescrizione;
	private TextField<String> txtfldImporto;
	private DateField dtfldDataFattura;
	private EditorGrid<AttivitaFatturateModel> gridAttivitaFatturate;
	private ListStore<AttivitaFatturateModel> storeAttivita= new ListStore<AttivitaFatturateModel>();
	private ColumnModel cm;
	private CellSelectionModel<AttivitaFatturateModel> cs;
	
	private Button btnAddAttivita;
	private Button btnDelAttivita;
	
	private Button btnPrintToRTF;

	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	public PanelFormInserimentoDatiFattura(final String numeroOrdine, final int idFoglioFatturazione, final FatturaModel fM){
  
	    final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		ContentPanel cntpnlLayout=new ContentPanel();
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setBorders(false);
		cntpnlLayout.setFrame(true);
		cntpnlLayout.setHeight(400);
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
		
		/*txtfldDataFattura.setAllowBlank(false);
		txtfldDataFattura.setFieldLabel("Data Fattura");
		txtfldDataFattura.setValue((String) fM.get("dataFattura"));*/
		//Date dataFattura=null;
		//dtfldDataFattura.setValue(new Date());
		//dataFattura=DateTimeFormat.getFormat("yyyy-MM-dd").parse((String) fM.get("dataFattura"));
		dtfldDataFattura= new DateField();
		dtfldDataFattura.setAllowBlank(false);
		dtfldDataFattura.setFieldLabel("Data Fattura");
		dtfldDataFattura.setValue((Date) fM.get("dataFattura"));
		layoutCol1.add(dtfldDataFattura, new FormData("80%"));
		
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
				
				if(checkDati()){
					List<AttivitaFatturateModel> listaAM= storeAttivita.getModels();
					fM.set("listaAttivita", null);
					fM.set("numeroFattura", txtfldNumeroFattura.getValue().toString());
					fM.set("dataFattura",dtfldDataFattura.getValue());
					fM.set("condizioni", txtfldCondizioniPagamento.getValue().toString());
					fM.set("iva", txtfldAliquotaIva.getValue().toString());
				
					String nordine=numeroOrdine;
					int idFF=idFoglioFatturazione;
				
					SessionManagementService.Util.getInstance().setDataFattura(nordine, idFF, fM, listaAM, "STAMPAFATTURA", 
						new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Error on setDataInSession()");					
						}

						@Override
						public void onSuccess(Boolean result) {
							if(result){
								fp.submit();							
							}
							else
								Window.alert("Problemi durante il settaggio dei parametri in Sessione (http)");
						}
		
					});
				}else
					Window.alert("Controllare i dati inseriti!");
			}

		});
		
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setAction(url);
		fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler()); 
		fp.add(btnPrintToRTF);
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.setHeight(130);
		cp.add(fp);
		
		cs=new CellSelectionModel<AttivitaFatturateModel>();
	    cs.setSelectionMode(SelectionMode.SIMPLE);
   
	    cm=new ColumnModel(createColumns());
	    storeAttivita.add((List<AttivitaFatturateModel>) fM.getListaAttF());
	    //storeAttivita.insert(new AttivitaFatturateModel((String)fM.get("descrizione"), (String)fM.get("imponibile")), 0);
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
		cntpnlDatiAggiuntivi.add(cp, new RowData(.20, .65, new Margins(5)));	
		
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
		//vp.add(cntpnlDatiRiepilogo);
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
	    txtfldImporto.setRegex("[0-9]+[.]{1}[0-9]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00");
		txtfldImporto.getMessages().setRegexText("Deve essere un numero, eventualmente nel formato 99.99");		
				
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
	
	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {

		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			closeDialog();
		}	
	}
	
	private void closeDialog() {
		this.hide();
	}
	
	protected boolean hasValue(TextField<String> field) {
	    return field.getValue() != null && field.isValid();
	}
	
	private boolean checkDati() {
		
		if(txtfldAliquotaIva.isValid())
			if(txtfldCondizioniPagamento.isValid())
				if(dtfldDataFattura.isValid())
					if(txtfldImporto.isValid())
						if (txtfldNumeroFattura.isValid())
						return true;
		
			return false;
		
	}
	
}
