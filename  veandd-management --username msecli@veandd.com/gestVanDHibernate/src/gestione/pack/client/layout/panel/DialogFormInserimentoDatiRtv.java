package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.model.RiferimentiRtvModel;
import gestione.pack.client.model.RtvModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;
import gestione.pack.shared.RiferimentiRtv;

import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.DataField;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class DialogFormInserimentoDatiRtv extends Dialog{

	private TextField<String> txtfldNumeroOrdine;
	private TextField<String> txtfldNumeroRtv;
	private TextField<String> txtfldCodiceFornitore;
	private TextArea txtarAttivita;
	private TextField<String> txtfldImporto;
	private DateField dtfldDataOrdine;
	private DateField dtfldDataEmissione;
	private DateField dtfldDataInizioAttivita;
	private DateField dtfldDataFineAttivita;
	private TextField<String> txtfldCdcRichiedente;
	private TextField<String> txtfldCommessaCliente;
	private TextField<String> txtfldEnte;
	
	//scelta del referente
	private ComboBox<RiferimentiRtvModel> cmbxReferente;
	private ComboBox<RtvModel> cmbxRtv;
	
	private SimpleComboBox<String> smplcmbxTipoModulo;
	final NumberFormat num= NumberFormat.getFormat("0.00");
	private Button btnPrintToRTF;

	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	public DialogFormInserimentoDatiRtv(final String numeroOrdine, GroupingStore<RtvModel> storeRtv){
  
	    final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		ContentPanel cntpnlLayout=new ContentPanel();
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setBorders(false);
		cntpnlLayout.setFrame(true);
		cntpnlLayout.setHeight(520);
		cntpnlLayout.setWidth(600);
		cntpnlLayout.setScrollMode(Scroll.NONE);
				
		ContentPanel cntpnlDatiAggiuntivi= new ContentPanel();
		cntpnlDatiAggiuntivi.setHeaderVisible(false);
		cntpnlDatiAggiuntivi.setLayout(new FitLayout());
		cntpnlDatiAggiuntivi.setBorders(false);
		cntpnlDatiAggiuntivi.setFrame(true);
		cntpnlDatiAggiuntivi.setHeight(490);
		cntpnlDatiAggiuntivi.setWidth(590);
		cntpnlDatiAggiuntivi.setScrollMode(Scroll.NONE);
		cntpnlDatiAggiuntivi.setLayout(new RowLayout(Orientation.HORIZONTAL));
		
		LayoutContainer layoutCol1= new LayoutContainer();		
		FormLayout layout= new FormLayout();
		layout.setLabelWidth(160);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol1.setLayout(layout);
					
		cmbxRtv= new ComboBox<RtvModel>();
		cmbxRtv.setStore(storeRtv);
		cmbxRtv.setFieldLabel("Rtv Precedenti");
		cmbxRtv.setName("rtv");
		cmbxRtv.setEmptyText("Rtv...");
		cmbxRtv.setAllowBlank(false);
		cmbxRtv.setTriggerAction(TriggerAction.ALL);
		cmbxRtv.setDisplayField("numeroRtv");
		cmbxRtv.addListener(Events.SelectionChange, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				RtvModel rtvM= cmbxRtv.getValue();
				txtarAttivita.setValue((String)rtvM.get("attivita"));
				txtfldCdcRichiedente.setValue((String)rtvM.get("cdcRichiedente"));
				txtfldCodiceFornitore.setValue((String)rtvM.get("codiceFornitore"));
				txtfldCommessaCliente.setValue((String)rtvM.get("commessaCliente"));
				txtfldEnte.setValue((String)rtvM.get("ente"));
				txtfldNumeroRtv.setValue((String)rtvM.get("numeroRtv"));
				dtfldDataOrdine.setValue((Date)rtvM.get("dataOrdine"));
			}		
		});		
		layoutCol1.add(cmbxRtv, new FormData("80%"));
		
		smplcmbxTipoModulo= new SimpleComboBox<String>();
		smplcmbxTipoModulo.setFieldLabel("Tipo Modulo");
		smplcmbxTipoModulo.setName("tipoModulo");
		smplcmbxTipoModulo.setEmptyText("Tipo Modulo...");
		smplcmbxTipoModulo.setAllowBlank(false);
		smplcmbxTipoModulo.add("Tipo1(FGA)");
		smplcmbxTipoModulo.add("Tipo2(FGA-Rid)");
		smplcmbxTipoModulo.add("Tipo3(FPT)");
		smplcmbxTipoModulo.setTriggerAction(TriggerAction.ALL);
		layoutCol1.add(smplcmbxTipoModulo, new FormData("80%"));		
		
		ListStore<RiferimentiRtvModel> listaR= new ListStore<RiferimentiRtvModel>();
		cmbxReferente= new ComboBox<RiferimentiRtvModel>();
		cmbxReferente.setStore(listaR);
		cmbxReferente.setFieldLabel("Referente");
		cmbxReferente.setName("referente");
		cmbxReferente.setEmptyText("Referente...");
		cmbxReferente.setAllowBlank(false);
		cmbxReferente.setTriggerAction(TriggerAction.ALL);
		cmbxReferente.setDisplayField("referente");
		cmbxReferente.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {					
					caricaDatiReferenti();					
			}		
		});		
		layoutCol1.add(cmbxReferente, new FormData("80%"));
				
		txtfldNumeroOrdine= new TextField<String>();
		txtfldNumeroOrdine.setEnabled(false);
		txtfldNumeroOrdine.setFieldLabel("Numero Ordine");
		txtfldNumeroOrdine.setValue(numeroOrdine);
		layoutCol1.add(txtfldNumeroOrdine, new FormData("80%"));
		
		dtfldDataOrdine= new DateField();
		dtfldDataOrdine.setFieldLabel("Data Ordine");
		layoutCol1.add(dtfldDataOrdine, new FormData("80%"));
		
		txtfldNumeroRtv= new TextField<String>();
		txtfldNumeroRtv.setFieldLabel("Numero RTV");
		txtfldNumeroRtv.setAllowBlank(false);
		layoutCol1.add(txtfldNumeroRtv, new FormData("80%"));
		
		txtfldCodiceFornitore= new TextField<String>();
		txtfldCodiceFornitore.setFieldLabel("Codice Fornitore");
		layoutCol1.add(txtfldCodiceFornitore, new FormData("80%"));
		
		txtfldEnte= new TextField<String>();
		txtfldEnte.setFieldLabel("Ente");
		layoutCol1.add(txtfldEnte, new FormData("80%"));
		
		dtfldDataEmissione=new DateField();
		dtfldDataEmissione.setValue(new Date());
		dtfldDataEmissione.setFieldLabel("Data Emissione RTV");
		layoutCol1.add(dtfldDataEmissione, new FormData("80%"));
			
		txtfldImporto= new TextField<String>();
		txtfldImporto.setEnabled(true);
		txtfldImporto.setFieldLabel("Importo");
		txtfldImporto.setAllowBlank(false);
		//TODO espressione regolare
		layoutCol1.add(txtfldImporto, new FormData("80%"));
		
		dtfldDataInizioAttivita= new DateField();
		dtfldDataInizioAttivita.setFieldLabel("Data Inizio Attivita");
		layoutCol1.add(dtfldDataInizioAttivita, new FormData("80%"));
		
		dtfldDataFineAttivita= new DateField();
		dtfldDataFineAttivita.setFieldLabel("Data Fine Attivita");
		layoutCol1.add(dtfldDataFineAttivita, new FormData("80%"));
		
		txtfldCdcRichiedente= new TextField<String>();
		txtfldCdcRichiedente.setFieldLabel("Cdc Richiedente");
		layoutCol1.add(txtfldCdcRichiedente,new FormData("80%"));
		
		txtfldCommessaCliente= new TextField<String>();
		txtfldCommessaCliente.setFieldLabel("Commessa Cliente");
		layoutCol1.add(txtfldCommessaCliente, new FormData("80%"));
		
		txtarAttivita= new TextArea();
		txtarAttivita.setFieldLabel("Attivita'");
		txtarAttivita.setEmptyText("Descrizione Attivita'...");
		txtarAttivita.setHeight(100);
		layoutCol1.add(txtarAttivita);							
		
		btnPrintToRTF = new Button();
		btnPrintToRTF.setToolTip("Stampa RTV");
		btnPrintToRTF.setHeight(80);
		btnPrintToRTF.setWidth(80);
		btnPrintToRTF.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print64()));
		btnPrintToRTF.setIconAlign(IconAlign.BOTTOM);
		btnPrintToRTF.setStyleAttribute("padding-left", "10px");	
		btnPrintToRTF.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {			
				
				if(checkDati()){	
								
					int idRtv=0;
					String numeroRtv="";
					String codiceFornitore="";
					String nomeResponsabile="";
					Date dataOrdine=null;
					Date dataEmissione=null;
					String importo= "0.00";
					String importoOrdine="";
					String importoAvanzamenti="";
					String meseRiferimento="";
					String attivita="";
					String statoLavori="";
					String cdcRichiedente="";
					String commessaCliente="";
					String ente="";
					Date dataInizioAttivita=null;
					Date dataFineAttivita=null;			
					
					RiferimentiRtvModel rifModel= cmbxReferente.getValue();
										
					if(!txtfldNumeroRtv.getRawValue().isEmpty())numeroRtv=txtfldNumeroRtv.getValue().toString();
					if(!txtfldCodiceFornitore.getRawValue().isEmpty())codiceFornitore=txtfldCodiceFornitore.getValue().toString();
					if(!txtfldImporto.getRawValue().isEmpty())importo=txtfldImporto.getValue().toString();
					if(!txtfldCdcRichiedente.getRawValue().isEmpty())cdcRichiedente=txtfldCdcRichiedente.getValue().toString();
					if(!txtfldCommessaCliente.getRawValue().isEmpty())commessaCliente=txtfldCommessaCliente.getValue().toString();
					if(!txtfldEnte.getRawValue().isEmpty())ente=txtfldEnte.getValue().toString();
					if(!txtarAttivita.getRawValue().isEmpty())attivita=txtarAttivita.getValue().toString();		
					
					if(!dtfldDataOrdine.getRawValue().isEmpty())dataOrdine=dtfldDataOrdine.getValue();
					if(!dtfldDataEmissione.getRawValue().isEmpty())dataEmissione=dtfldDataEmissione.getValue();
					if(!dtfldDataInizioAttivita.getRawValue().isEmpty())dataInizioAttivita=dtfldDataInizioAttivita.getValue();
					if(!dtfldDataFineAttivita.getRawValue().isEmpty())dataFineAttivita=dtfldDataFineAttivita.getValue();
					
					RtvModel rtv= new RtvModel(idRtv, 0, numeroOrdine, numeroRtv, codiceFornitore, nomeResponsabile, dataOrdine,
							dataEmissione, Float.valueOf(importo), importoOrdine, importoAvanzamenti, meseRiferimento, attivita, statoLavori, 
							cdcRichiedente, commessaCliente, ente, dataInizioAttivita, dataFineAttivita);
					
					String tipoModulo=smplcmbxTipoModulo.getRawValue().toString();
					tipoModulo=tipoModulo.substring(0, tipoModulo.indexOf("("));
					
					SessionManagementService.Util.getInstance().setDataRtv(rtv, rifModel,
							tipoModulo, "STAMPARTV",	new AsyncCallback<Boolean>() {

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

		RowData data = new RowData(.70, 1);
		data.setMargins(new Margins(5));
		cntpnlDatiAggiuntivi.add(layoutCol1, data);
		cntpnlDatiAggiuntivi.add(cp, new RowData(.20, .65, new Margins(5)));	
		
		VerticalPanel vp= new VerticalPanel();
		vp.setSpacing(5);
		
		vp.add(cntpnlDatiAggiuntivi);
				
		cntpnlLayout.add(vp);
		
		layoutContainer.add(cntpnlLayout, new FitData(3, 3, 3, 3));
		add(layoutContainer);	
	}
	

	private void caricaDatiReferenti() {
		
		AdministrationService.Util.getInstance().getDatiReferenti(new AsyncCallback<List<RiferimentiRtvModel>>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problemi di connessione on getDatiReferenti();");
			}

			@Override
			public void onSuccess(List<RiferimentiRtvModel> result) {
				if(result!=null){
					ListStore<RiferimentiRtvModel> lista= new ListStore<RiferimentiRtvModel>();
					lista.setStoreSorter(new StoreSorter<RiferimentiRtvModel>());  
					lista.setDefaultSort("referente", SortDir.ASC);
					
					lista.add(result);				
					cmbxReferente.clear();
					cmbxReferente.setStore(lista);
				}					
			}			
		});
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
		if(smplcmbxTipoModulo.isValid() &&
				txtfldNumeroRtv.isValid()&&
				txtfldImporto.isValid()&&
				cmbxReferente.isValid())
			return true;
		else
			return false;
	}
	
}
