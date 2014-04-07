package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DialogDatiTrasferta extends Dialog {

	
	private TextField<String> txtfldNumeroGiorni;
	private TextField<String> txtfldNumeroViaggi;
		
	private TextField<String> txtfldOreViaggio;
	private TextField<String> txtfldKmStradali;
	private TextField<String> txtfldCarburante;
	private TextField<String> txtfldAutostrada;
	private TextField<String> txtfldCostoTreno;
	private TextField<String> txtfldCostoAereo;
	private TextField<String> txtfldCostiVari;
	private CheckBox chbxAutoPropria;
	
	private Text txtVuota1= new Text();
	private Text txtVuota2= new Text();
	private Text txtTotOreViaggio;
	private Text txtTotKmStradali;
	private Text txtTotCarburante;
	private Text txtTotAutostrada;
	private Text txtTotCostoTreno;
	private Text txtTotCostoAereo;
	private Text txtTotCostiVari;
	
	private TextField<String> txtfldCostoAlbergo;
	private TextField<String> txtfldCostoPranzo;
	private TextField<String> txtfldCostoCena;
	private TextField<String> txtfldNoleggioAuto;
	private TextField<String> txtfldTrasportoLocale;
	
	private Text txtVuota3= new Text();
	private Text txtTotCostoAlbergo;
	private Text txtTotCostoPranzo;
	private Text txtTotCostoCena;
	private Text txtTotNoleggioAuto;
	private Text txtTotTrasportoLocale;
	
	private Text txtTotCostiViaggio;
	private Text txtTotCostiSoggiorno;
	
	private Button btnConferma;
	
	private int idRisorsaSelected;
	private int idCostingSelected;
	
	public DialogDatiTrasferta(int idrisorsa, int idSelected){
		
		this.idRisorsaSelected= idrisorsa;
		this.idCostingSelected= idSelected;
		
	    final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		ContentPanel cntpnlLayout=new ContentPanel();
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setBorders(false);
		cntpnlLayout.setFrame(true);
		cntpnlLayout.setHeight(450);
		cntpnlLayout.setWidth(800);
		cntpnlLayout.setScrollMode(Scroll.NONE);
		cntpnlLayout.setButtonAlign(HorizontalAlignment.CENTER);
		cntpnlLayout.setLayout(new RowLayout(Orientation.HORIZONTAL));
		
		FieldSet fldsetDatiViaggio=new FieldSet();
		fldsetDatiViaggio.setCollapsible(false);
		fldsetDatiViaggio.setExpanded(true);
		fldsetDatiViaggio.setHeading("Costi del viaggio");
		
		FieldSet fldsetDatiSoggiorno=new FieldSet();
		fldsetDatiSoggiorno.setCollapsible(false);
		fldsetDatiSoggiorno.setExpanded(true);
		fldsetDatiSoggiorno.setHeading("Costi del soggiorno");
		fldsetDatiSoggiorno.setStyleAttribute("margin-left", "20px");
		
		ContentPanel cp1= new ContentPanel();
		cp1.setHeaderVisible(false);
		cp1.setSize(400, 340);
		cp1.setBorders(false);
		cp1.setBodyBorder(false);
		cp1.setFrame(false);
		cp1.setLayout(new RowLayout(Orientation.HORIZONTAL));
		
		ContentPanel cp2= new ContentPanel();
		cp2.setHeaderVisible(false);
		cp2.setSize(400, 340);
		cp2.setBorders(false);
		cp2.setBodyBorder(false);
		cp2.setFrame(false);
		cp2.setLayout(new RowLayout(Orientation.HORIZONTAL));
			
		LayoutContainer layoutCol1=new LayoutContainer();//contiene le caselle di testo per i dati viaggio
		LayoutContainer layoutCol2=new LayoutContainer();//contiene le txt per i totali dei dati viaggio
		LayoutContainer layoutCol3=new LayoutContainer();//contiene le txtfld per i dati soggiorno
		LayoutContainer layoutCol4=new LayoutContainer();//contiene le txt per i totali dei dati soggiorno
		
		FormLayout layout= new FormLayout();
		layout.setLabelWidth(120);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol1.setLayout(layout);
				
		layout= new FormLayout();
		layout.setLabelWidth(80);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol2.setLayout(layout);
		layoutCol2.setStyleAttribute("padding-left", "20px");
		
		layout= new FormLayout();
		layout.setLabelWidth(120);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol3.setLayout(layout);
		
		layout= new FormLayout();
		layout.setLabelWidth(80);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol4.setLayout(layout);
		
		txtfldOreViaggio= new TextField<String>();
		txtfldOreViaggio.setFieldLabel("Ore Viaggio");
		
		txtfldNumeroViaggi= new TextField<String>();
		txtfldNumeroViaggi.setFieldLabel("Numero Viaggi");
		
		txtfldKmStradali= new TextField<String>();
		txtfldKmStradali.setFieldLabel("Km stradali");
				
		txtfldCarburante= new TextField<String>();
		txtfldCarburante.setFieldLabel("Costo carburante");
		
		txtfldAutostrada= new TextField<String>();
		txtfldAutostrada.setFieldLabel("Costo Autostrada");
		
		chbxAutoPropria=new CheckBox();
		chbxAutoPropria.setFieldLabel("Uso auto propria");
		
		txtfldCostoTreno= new TextField<String>();
		txtfldCostoTreno.setFieldLabel("Costo treno");
		
		txtfldCostoAereo= new TextField<String>();
		txtfldCostoAereo.setFieldLabel("Costo aereo");
		
		txtfldCostiVari= new TextField<String>();
		txtfldCostiVari.setFieldLabel("Costi vari");
		
		txtVuota1.setText(".");
		txtVuota2.setText("");
		
		txtTotOreViaggio= new Text();
		
		txtTotKmStradali= new Text();
				
		txtTotCarburante=new Text();
		
		txtTotAutostrada= new Text();
		
		txtTotCostoTreno= new Text();
		
		txtTotCostoAereo= new Text();
		
		txtTotCostiVari=new Text();
		
		txtTotCostiViaggio= new Text();
		
		txtfldNumeroGiorni= new TextField<String>();
		txtfldNumeroGiorni.setFieldLabel("Numero giorni");
		
		txtfldCostoAlbergo= new TextField<String>();
		txtfldCostoAlbergo.setFieldLabel("Costo albergo");
		
		txtfldCostoPranzo= new TextField<String>();
		txtfldCostoPranzo.setFieldLabel("Costo pranzo");
		
		txtfldCostoCena= new TextField<String>();
		txtfldCostoCena.setFieldLabel("Costo cena");
		
		txtfldNoleggioAuto= new TextField<String>();
		txtfldNoleggioAuto.setFieldLabel("Noleggio auto");
		
		txtfldTrasportoLocale= new TextField<String>();
		txtfldTrasportoLocale.setFieldLabel("Trasporto locale");
		
		txtTotCostiSoggiorno= new Text();
		
		txtTotCostoAlbergo= new Text();
				
		txtTotCostoPranzo= new Text();
		
		txtTotCostoCena= new Text();
		
		txtTotNoleggioAuto= new Text();
		
		txtTotTrasportoLocale= new Text();
		
		btnConferma= new Button("Conferma");
		btnConferma.setWidth(80);
		btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				boolean checkbox= chbxAutoPropria.getValue();
				
				AdministrationService.Util.getInstance().saveDatiTrasfertaUtente(idRisorsaSelected, idCostingSelected, txtfldOreViaggio.getRawValue().toString(), txtfldKmStradali.getRawValue().toString(),
						txtfldCarburante.getRawValue().toString(), txtfldAutostrada.getRawValue().toString(), checkbox, txtfldCostoTreno.getRawValue().toString(),
						txtfldCostoAereo.getRawValue().toString(), txtfldCostiVari.getRawValue().toString(), txtfldNumeroGiorni.getRawValue().toString(), 
						txtfldCostoAlbergo.getRawValue().toString(), txtfldCostoPranzo.getRawValue().toString(), txtfldCostoCena.getRawValue().toString(), 
						txtfldNoleggioAuto.getRawValue().toString(), txtfldTrasportoLocale.getRawValue().toString(), new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onSuccess(Boolean result) {
								// TODO Auto-generated method stub
								
							}
				});
			}
		});
		
		layoutCol1.add(txtfldNumeroViaggi,new FormData("20%"));
		layoutCol1.add(txtfldOreViaggio,new FormData("20%"));
		layoutCol1.add(txtfldKmStradali,new FormData("20%"));
		layoutCol1.add(txtfldCarburante,new FormData("20%"));
		layoutCol1.add(txtfldAutostrada,new FormData("20%"));
		layoutCol1.add(chbxAutoPropria,new FormData("20%"));
		layoutCol1.add(txtfldCostoTreno,new FormData("20%"));
		layoutCol1.add(txtfldCostoAereo,new FormData("20%"));
		layoutCol1.add(txtfldCostiVari,new FormData("20%"));
		
		layoutCol2.add(txtVuota1);
		layoutCol2.add(txtTotOreViaggio);
		layoutCol2.add(txtTotKmStradali);
		layoutCol2.add(txtTotCarburante);
		layoutCol2.add(txtTotAutostrada);
		layoutCol2.add(txtVuota2);
		layoutCol2.add(txtTotCostoTreno);
		layoutCol2.add(txtTotCostoAereo);
		layoutCol2.add(txtTotCostiVari);
		
		layoutCol3.add(txtfldNumeroGiorni,new FormData("20%"));
		layoutCol3.add(txtfldCostoAlbergo,new FormData("20%"));
		layoutCol3.add(txtfldCostoPranzo,new FormData("20%"));
		layoutCol3.add(txtfldCostoCena,new FormData("20%"));
		layoutCol3.add(txtfldNoleggioAuto,new FormData("20%"));
		layoutCol3.add(txtfldTrasportoLocale,new FormData("20%"));
		
		layoutCol4.add(txtVuota3);
		layoutCol4.add(txtTotCostoAlbergo);
		layoutCol4.add(txtTotCostoPranzo);
		layoutCol4.add(txtTotCostoCena);
		layoutCol4.add(txtTotNoleggioAuto);
		layoutCol4.add(txtTotTrasportoLocale);
		
		cp1.add(layoutCol1);
		cp1.add(layoutCol2);
		cp1.setBottomComponent(txtTotCostiViaggio);
		
		cp2.add(layoutCol3);
		cp2.add(layoutCol4);
		cp2.setBottomComponent(txtTotCostiSoggiorno);
		
		HorizontalPanel hp= new HorizontalPanel();
		hp.setSpacing(5);
		HorizontalPanel hp1= new HorizontalPanel();
		hp1.setSpacing(5);
		
		fldsetDatiViaggio.add(cp1);
		fldsetDatiSoggiorno.add(cp2);
		
		hp.add(fldsetDatiViaggio);
		hp.add(fldsetDatiSoggiorno);
		
		hp1.add(btnConferma);
		
		cntpnlLayout.add(hp);
		cntpnlLayout.setBottomComponent(hp1);
		
		layoutContainer.add(cntpnlLayout, new FitData(3, 3, 3, 3));
		add(layoutContainer);
		
	}
}
