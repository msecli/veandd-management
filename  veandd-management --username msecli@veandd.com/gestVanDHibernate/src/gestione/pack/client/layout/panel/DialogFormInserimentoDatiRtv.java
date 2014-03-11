package gestione.pack.client.layout.panel;

import gestione.pack.client.SessionManagementService;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import java.util.Date;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class DialogFormInserimentoDatiRtv extends Dialog{

	private TextField<String> txtfldNumeroOrdine;
	private TextField<String> txtfldImporto;
	private SimpleComboBox<String> smplcmbxMese;
	private SimpleComboBox<String> smplcmbxAnno;
	private SimpleComboBox<String> smplcmbxTipoModulo;
	
	private Button btnPrintToRTF;

	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	public DialogFormInserimentoDatiRtv(final String numeroOrdine){
  
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
		
		LayoutContainer layoutCol1= new LayoutContainer();		
		FormLayout layout= new FormLayout();
		layout.setLabelWidth(160);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol1.setLayout(layout);
		
		txtfldNumeroOrdine= new TextField<String>();
		txtfldNumeroOrdine.setEnabled(false);
		txtfldNumeroOrdine.setFieldLabel("Numero Ordine");
		txtfldNumeroOrdine.setValue(numeroOrdine);
		layoutCol1.add(txtfldNumeroOrdine, new FormData("80%"));
		
		txtfldImporto= new TextField<String>();
		txtfldImporto.setEnabled(true);
		txtfldImporto.setFieldLabel("Importo");
		//TODO espressione regolare
		layoutCol1.add(txtfldImporto, new FormData("80%"));
	
		Date d= new Date();
		String dt= d.toString();
		final String anno= dt.substring(dt.length()-4, dt.length());
		final String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(dt.substring(4, 7)));
		
		smplcmbxAnno= new SimpleComboBox<String>();
		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setAllowBlank(false);
		smplcmbxAnno.setEnabled(true);
		for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);}
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setSimpleValue(anno);
		smplcmbxAnno.setWidth(100);
		layoutCol1.add(smplcmbxAnno, new FormData("60%"));
		
		smplcmbxMese= new SimpleComboBox<String>();
		smplcmbxMese.setFieldLabel("Mese");
		smplcmbxMese.setName("mese");
		smplcmbxMese.setEmptyText("Mese..");
		smplcmbxMese.setAllowBlank(false);
		 for(String l : DatiComboBox.getMese()){
			 smplcmbxMese.add(l);}
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		smplcmbxMese.setSimpleValue(mese);
		layoutCol1.add(smplcmbxMese, new FormData("60%"));
		
		smplcmbxTipoModulo= new SimpleComboBox<String>();
		smplcmbxTipoModulo.setFieldLabel("Tipo Modulo");
		smplcmbxTipoModulo.setName("tipoModulo");
		smplcmbxTipoModulo.setEmptyText("Tipo Modulo...");
		smplcmbxTipoModulo.setAllowBlank(false);
		smplcmbxTipoModulo.add("Tipo 1");
		smplcmbxTipoModulo.add("Tipo 2");
		smplcmbxTipoModulo.add("Tipo 3");
		smplcmbxTipoModulo.setTriggerAction(TriggerAction.ALL);
		layoutCol1.add(smplcmbxTipoModulo, new FormData("80%"));		
				
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
					String meseRif=smplcmbxMese.getRawValue().toString().substring(0, 3)+smplcmbxAnno.getRawValue().toString();
									
					SessionManagementService.Util.getInstance().setDataRtv(numeroOrdine, meseRif, txtfldImporto.getValue().toString(),
							smplcmbxTipoModulo.getRawValue().toString(), "STAMPARTV",	new AsyncCallback<Boolean>() {

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
		
		if(smplcmbxAnno.isValid())
			if(smplcmbxMese.isValid())
				if(smplcmbxTipoModulo.isValid())
					return true;
		
			return false;
		
	}
	
}
