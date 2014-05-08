package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.DettaglioTrasfertaModel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DatePickerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
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
	private TextField<String> txtfldCostoKmAutoPropria;
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
	private Text txtTotCostoKmAutoPropria;
	
	private TextField<String> txtfldNumeroNotti;
	private TextField<String> txtfldCostoDiaria;
	private TextField<String> txtfldCostoAlbergo;
	private TextField<String> txtfldCostoPranzo;
	private TextField<String> txtfldCostoCena;
	private TextField<String> txtfldNoleggioAuto;
	private TextField<String> txtfldTrasportoLocale;
	
	private Text txtVuota3= new Text();
	private Text txtVuota4= new Text();
	private Text txtTotCostoDiaria;
	private Text txtTotCostoAlbergo;
	private Text txtTotCostoPranzo;
	private Text txtTotCostoCena;
	private Text txtTotNoleggioAuto;
	private Text txtTotTrasportoLocale;
	
	private TextField<String> txtfldTotCostiViaggio= new TextField<String>();
	private TextField<String> txtfldTotCostiSoggiorno= new TextField<String>();
	
	private Button btnConferma;
	
	private int idRisorsaSelected;
	private int idCostingSelected;
	
	NumberFormat number= NumberFormat.getFormat("0.00");
	
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
		
		layout= new FormLayout();
		layout.setLabelWidth(120);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol3.setLayout(layout);
		
		layout= new FormLayout();
		layout.setLabelWidth(80);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol4.setLayout(layout);
		
		
		//--------------LAYOUTCOL1
		txtfldNumeroViaggi= new TextField<String>();
		txtfldNumeroViaggi.setFieldLabel("Numero Viaggi");
		txtfldNumeroViaggi.setValue("0");
		txtfldNumeroViaggi.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldNumeroViaggi.getMessages().setRegexText("Deve essere un numero!");
		
		txtfldOreViaggio= new TextField<String>();
		txtfldOreViaggio.setFieldLabel("Ore Viaggio A/R");
		txtfldOreViaggio.setValue("0.00");
		txtfldOreViaggio.setAllowBlank(false);
		txtfldOreViaggio.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldOreViaggio.getMessages().setRegexText("Deve essere un numero!");
		txtfldOreViaggio.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldOreViaggio)&&txtfldOreViaggio.getValue()!=null){
					 Float totale= (float)0.00;
					 String oreSingoloViaggio=txtfldOreViaggio.getRawValue().toString();
					 String numeroViaggi="0";
					 if(hasValue(txtfldNumeroViaggi)){
						 numeroViaggi=txtfldNumeroViaggi.getRawValue().toString();
						 totale=Float.valueOf(oreSingoloViaggio)* Float.valueOf(numeroViaggi);					 
						 txtTotOreViaggio.setText("Ore Totali: "+number.format(totale));
					 }					 
					 calcolaTotaleViaggi();					
		    	 }
			 }
		});
		
		txtfldKmStradali= new TextField<String>();
		txtfldKmStradali.setFieldLabel("Km stradali");
		txtfldKmStradali.setValue("0.00");
		txtfldKmStradali.setAllowBlank(false);
		txtfldKmStradali.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldKmStradali.getMessages().setRegexText("Deve essere un numero!");
		txtfldKmStradali.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldKmStradali)&&txtfldKmStradali.getValue()!=null){
					 Float totale= (float)0.00;
					 String kmViaggio=txtfldKmStradali.getRawValue().toString();
					 String numeroViaggi="0";
					 if(hasValue(txtfldNumeroViaggi)){
						 numeroViaggi=txtfldNumeroViaggi.getRawValue().toString();
						 totale=Float.valueOf(kmViaggio)* Float.valueOf(numeroViaggi);					 
						 txtTotKmStradali.setText("Km Totali: "+number.format(totale));
					 }				 
					 calcolaTotaleViaggi();
				 }
			 }
		});

		txtfldCarburante= new TextField<String>();
		txtfldCarburante.setFieldLabel("Costo carburante");
		txtfldCarburante.setValue("0.00");
		txtfldCarburante.setAllowBlank(false);
		txtfldCarburante.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldCarburante.getMessages().setRegexText("Deve essere un numero!");
		txtfldCarburante.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldCarburante)&&txtfldCarburante.getValue()!=null){
					 Float totale= (float)0.00;
					 String totCarburante=txtfldCarburante.getRawValue().toString();
					 String numeroViaggi="0";
					 String kmStradali="0.0";
					 
					 if(hasValue(txtfldNumeroViaggi)){
						 numeroViaggi=txtfldNumeroViaggi.getRawValue().toString();
						 kmStradali=txtfldKmStradali.getRawValue().toString();
						 totale=Float.valueOf(totCarburante)* Float.valueOf(numeroViaggi)*Float.valueOf(kmStradali);					 
						 txtTotCarburante.setText("Costo Totale: "+number.format(totale));
					 }
					 calcolaTotaleViaggi();
		    	 }
			 }
		});
		
		txtfldAutostrada= new TextField<String>();
		txtfldAutostrada.setFieldLabel("Costo Autostrada");
		txtfldAutostrada.setValue("0.00");
		txtfldAutostrada.setAllowBlank(false);
		txtfldAutostrada.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldAutostrada.getMessages().setRegexText("Deve essere un numero!");
		txtfldAutostrada.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldAutostrada)&&txtfldAutostrada.getValue()!=null){
					 Float totale= (float)0.00;
					 String totAutostrada=txtfldAutostrada.getRawValue().toString();
					 String numeroViaggi="0";
					 if(hasValue(txtfldNumeroViaggi)){
						 numeroViaggi=txtfldNumeroViaggi.getRawValue().toString();
						 totale=Float.valueOf(totAutostrada)* Float.valueOf(numeroViaggi);					 
						 txtTotAutostrada.setText("Costo Totale: "+number.format(totale));
					 }
					 calcolaTotaleViaggi();
		    	 }			 
			 }
		});
		
		chbxAutoPropria=new CheckBox();
		chbxAutoPropria.setFieldLabel("Uso auto propria");
		chbxAutoPropria.addListener(Events.Change, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				if(chbxAutoPropria.getValue())
					txtfldCostoKmAutoPropria.enable();
				else{
					txtfldCostoKmAutoPropria.disable();
					txtfldCostoKmAutoPropria.setValue("0.00");
				}
			}
		});
		
		
		txtfldCostoKmAutoPropria= new TextField<String>();
		txtfldCostoKmAutoPropria.setFieldLabel("Rimborso Km (Auto)");
		txtfldCostoKmAutoPropria.setValue("0.00");
		txtfldCostoKmAutoPropria.setAllowBlank(false);
		txtfldCostoKmAutoPropria.disable();
		txtfldCostoKmAutoPropria.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldCostoKmAutoPropria.getMessages().setRegexText("Deve essere un numero!");
		txtfldCostoKmAutoPropria.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldCostoKmAutoPropria)&&txtfldCostoKmAutoPropria.getValue()!=null){
					 Float totale= (float)0.00;
					 String totRimborso=txtfldCostoKmAutoPropria.getRawValue().toString();
					 String numeroKm="0";
					 if(hasValue(txtfldNumeroViaggi)){
						 numeroKm=txtfldKmStradali.getRawValue().toString();
						 totale=Float.valueOf(totRimborso)* Float.valueOf(numeroKm);					 
						 txtTotCostoKmAutoPropria.setText("Costo Totale: "+number.format(totale));
					 }
					 calcolaTotaleViaggi();
		    	 }
			 }
		});
				
		txtfldCostoTreno= new TextField<String>();
		txtfldCostoTreno.setFieldLabel("Costo treno");
		txtfldCostoTreno.setValue("0.00");
		txtfldCostoTreno.setAllowBlank(false);
		txtfldCostoTreno.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldCostoTreno.getMessages().setRegexText("Deve essere un numero!");
		txtfldCostoTreno.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldCostoTreno)&&txtfldCostoTreno.getValue()!=null){
					 Float totale= (float)0.00;
					 String totTreno=txtfldCostoTreno.getRawValue().toString();
					 String numeroViaggi="0";
					 if(hasValue(txtfldNumeroViaggi)){
						 numeroViaggi=txtfldNumeroViaggi.getRawValue().toString();
						 totale=Float.valueOf(totTreno)* Float.valueOf(numeroViaggi);					 
						 txtTotCostoTreno.setText("Costo Totale: "+number.format(totale));
					 }
					 calcolaTotaleViaggi();
		    	 }			 
			 }
		});
		
		txtfldCostoAereo= new TextField<String>();
		txtfldCostoAereo.setFieldLabel("Costo aereo");
		txtfldCostoAereo.setValue("0.00");
		txtfldCostoAereo.setAllowBlank(false);
		txtfldCostoAereo.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldCostoAereo.getMessages().setRegexText("Deve essere un numero!");
		txtfldCostoAereo.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldCostoAereo)&&txtfldCostoAereo.getValue()!=null){
					 Float totale= (float)0.00;
					 String totAereo=txtfldCostoAereo.getRawValue().toString();
					 String numeroViaggi="0";
					 if(hasValue(txtfldNumeroViaggi)){
						 numeroViaggi=txtfldNumeroViaggi.getRawValue().toString();
						 totale=Float.valueOf(totAereo)* Float.valueOf(numeroViaggi);					 
						 txtTotCostoAereo.setText("Costo Totale: "+number.format(totale));
					 }
					 calcolaTotaleViaggi();
		    	 }			 
			 }
		});
		
		txtfldCostiVari= new TextField<String>();
		txtfldCostiVari.setFieldLabel("Costi vari");
		txtfldCostiVari.setValue("0.00");
		txtfldCostiVari.setAllowBlank(false);
		txtfldCostiVari.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldCostiVari.getMessages().setRegexText("Deve essere un numero!");
		txtfldCostiVari.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldCostiVari)&&txtfldCostiVari.getValue()!=null){
					 Float totale= (float)0.00;
					 String totVari=txtfldCostiVari.getRawValue().toString();
					 String numeroViaggi="0";
					 if(hasValue(txtfldNumeroViaggi)){
						 numeroViaggi=txtfldNumeroViaggi.getRawValue().toString();
						 totale=Float.valueOf(totVari)* Float.valueOf(numeroViaggi);					 
						 txtTotCostiVari.setText("Costo Totale: "+number.format(totale));
					 }
					 calcolaTotaleViaggi();
		    	 }			 
			 }
		});
		//-------------- 
		
		
		//-------------- LAYOUTCOL 2
		txtVuota1.setStyleAttribute("padding-top", "6px");		
		
		txtTotOreViaggio= new Text();
		txtTotOreViaggio.setStyleAttribute("padding-top", "6px");
		
		txtTotKmStradali= new Text();
		txtTotKmStradali.setStyleAttribute("padding-top", "6px");
		
		txtTotCostoKmAutoPropria= new Text();
		txtTotCostoKmAutoPropria.setStyleAttribute("padding-top", "18px");
				
		txtTotCarburante=new Text();
		txtTotCarburante.setStyleAttribute("padding-top", "6px");
		
		txtTotAutostrada= new Text();
		txtTotAutostrada.setStyleAttribute("padding-top", "6px");
		
		txtTotCostoTreno= new Text();
		txtTotCostoTreno.setStyleAttribute("padding-top", "10px");
		
		txtTotCostoAereo= new Text();
		txtTotCostoAereo.setStyleAttribute("padding-top", "6px");
		
		txtTotCostiVari=new Text();
		txtTotCostiVari.setStyleAttribute("padding-top", "6px");
		
		//----------------
		
		
		//---------------- LAYOUTCOL 3
		txtfldNumeroGiorni= new TextField<String>();
		txtfldNumeroGiorni.setFieldLabel("Numero giorni");
		txtfldNumeroGiorni.setValue("0");
		
		txtfldNumeroNotti= new TextField<String>();
		txtfldNumeroNotti.setFieldLabel("Numeri notti");
		txtfldNumeroNotti.setValue("0");
		
		txtfldCostoDiaria= new TextField<String>();
		txtfldCostoDiaria.setFieldLabel("Costo diaria");
		txtfldCostoDiaria.setValue("0.00");
		txtfldCostoDiaria.setAllowBlank(false);
		txtfldCostoDiaria.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldCostoDiaria.getMessages().setRegexText("Deve essere un numero!");
		txtfldCostoDiaria.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldCostoDiaria)&&txtfldCostoDiaria.getValue()!=null){
					 Float totale= (float)0.00;
					 String totAlbergo=txtfldCostoDiaria.getRawValue().toString();
					 String numeroViaggi="0";
					 if(hasValue(txtfldNumeroGiorni)){
						 numeroViaggi=txtfldNumeroGiorni.getRawValue().toString();
						 totale=Float.valueOf(totAlbergo)* Float.valueOf(numeroViaggi);					 
						 txtTotCostoDiaria.setText("Costo Totale: "+number.format(totale));
					 }
					 
					 calcolaTotaleSoggiorno();
		    	 }			 
			 }			
		});
				
		txtfldCostoAlbergo= new TextField<String>();
		txtfldCostoAlbergo.setFieldLabel("Costo albergo");
		txtfldCostoAlbergo.setValue("0.00");
		txtfldCostoAlbergo.setAllowBlank(false);
		txtfldCostoAlbergo.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldCostoAlbergo.getMessages().setRegexText("Deve essere un numero!");
		txtfldCostoAlbergo.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldCostoAlbergo)&&txtfldCostoAlbergo.getValue()!=null){
					 Float totale= (float)0.00;
					 String totAlbergo=txtfldCostoAlbergo.getRawValue().toString();
					 String numeroNotti="0";
					 if(hasValue(txtfldNumeroNotti)){
						 numeroNotti=txtfldNumeroNotti.getRawValue().toString();
						 totale=Float.valueOf(totAlbergo)* Float.valueOf(numeroNotti);					 
						 txtTotCostoAlbergo.setText("Costo Totale: "+number.format(totale));
					 }					 
					 calcolaTotaleSoggiorno();
		    	 }			 
			 }		
		});
		
		txtfldCostoPranzo= new TextField<String>();
		txtfldCostoPranzo.setFieldLabel("Costo pranzo");
		txtfldCostoPranzo.setValue("0.00");
		txtfldCostoPranzo.setAllowBlank(false);
		txtfldCostoPranzo.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldCostoPranzo.getMessages().setRegexText("Deve essere un numero!");
		txtfldCostoPranzo.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldCostoPranzo)&&txtfldCostoPranzo.getValue()!=null){
					 Float totale= (float)0.00;
					 String totPranzo=txtfldCostoPranzo.getRawValue().toString();
					 String numeroViaggi="0";
					 if(hasValue(txtfldNumeroGiorni)){
						 numeroViaggi=txtfldNumeroGiorni.getRawValue().toString();
						 totale=Float.valueOf(totPranzo)* Float.valueOf(numeroViaggi);					 
						 txtTotCostoPranzo.setText("Costo Totale: "+number.format(totale));
					 }
					 calcolaTotaleSoggiorno();
		    	 }			 
			 }
		});
		
		txtfldCostoCena= new TextField<String>();
		txtfldCostoCena.setFieldLabel("Costo cena");
		txtfldCostoCena.setValue("0.00");
		txtfldCostoCena.setAllowBlank(false);
		txtfldCostoCena.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldCostoCena.getMessages().setRegexText("Deve essere un numero!");
		txtfldCostoCena.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldCostoCena)&&txtfldCostoCena.getValue()!=null){
					 Float totale= (float)0.00;
					 String totCena=txtfldCostoCena.getRawValue().toString();
					 String numeroViaggi="0";
					 if(hasValue(txtfldNumeroGiorni)){
						 numeroViaggi=txtfldNumeroGiorni.getRawValue().toString();
						 totale=Float.valueOf(totCena)* Float.valueOf(numeroViaggi);					 
						 txtTotCostoCena.setText("Costo Totale: "+number.format(totale));
					 }
					 calcolaTotaleSoggiorno();
		    	 }			 
			 }
		});
		
		txtfldNoleggioAuto= new TextField<String>();
		txtfldNoleggioAuto.setFieldLabel("Noleggio auto");
		txtfldNoleggioAuto.setValue("0.00");
		txtfldNoleggioAuto.setAllowBlank(false);
		txtfldNoleggioAuto.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldNoleggioAuto.getMessages().setRegexText("Deve essere un numero!");
		txtfldNoleggioAuto.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldNoleggioAuto)&&txtfldNoleggioAuto.getValue()!=null){
					 Float totale= (float)0.00;
					 String noleggioA=txtfldNoleggioAuto.getRawValue().toString();
					 String numeroViaggi="0";
					 if(hasValue(txtfldNumeroGiorni)){
						 numeroViaggi=txtfldNumeroGiorni.getRawValue().toString();
						 totale=Float.valueOf(noleggioA)* Float.valueOf(numeroViaggi);					 
						 txtTotNoleggioAuto.setText("Costo Totale: "+number.format(totale));
					 }
					 calcolaTotaleSoggiorno();
		    	 }
			 }
		});
		
		txtfldTrasportoLocale= new TextField<String>();
		txtfldTrasportoLocale.setFieldLabel("Trasporto locale");
		txtfldTrasportoLocale.setValue("0.00");
		txtfldTrasportoLocale.setAllowBlank(false);
		txtfldTrasportoLocale.setRegex("[0-9]+[.]{1}[0-9]+|[0-9]+");
		txtfldTrasportoLocale.getMessages().setRegexText("Deve essere un numero!");
		txtfldTrasportoLocale.addKeyListener(new KeyListener() {
			 public void componentKeyUp(ComponentEvent event) {
				 if(hasValue(txtfldTrasportoLocale)&&txtfldTrasportoLocale.getValue()!=null){
					 Float totale= (float)0.00;
					 String traspLocale=txtfldTrasportoLocale.getRawValue().toString();
					 String numeroViaggi="0";
					 if(hasValue(txtfldNumeroGiorni)){
						 numeroViaggi=txtfldNumeroGiorni.getRawValue().toString();
						 totale=Float.valueOf(traspLocale)* Float.valueOf(numeroViaggi);					 
						 txtTotTrasportoLocale.setText("Costo Totale: "+number.format(totale));
					 }
					 calcolaTotaleSoggiorno();
		    	 }
			 }
		});
		//------------------
		
		
		//------------------ LAYOUTCOL 4	
		txtVuota3.setStyleAttribute("padding-top", "6px");
		
		txtVuota4.setStyleAttribute("padding-top", "6px");
		
		txtTotCostoDiaria= new Text();
		txtTotCostoDiaria.setStyleAttribute("padding-top", "6px");
		
		txtTotCostoAlbergo= new Text();
		txtTotCostoAlbergo.setStyleAttribute("padding-top", "7px");
				
		txtTotCostoPranzo= new Text();
		txtTotCostoPranzo.setStyleAttribute("padding-top", "7px");
		
		txtTotCostoCena= new Text();
		txtTotCostoCena.setStyleAttribute("padding-top", "7px");
		
		txtTotNoleggioAuto= new Text();
		txtTotNoleggioAuto.setStyleAttribute("padding-top", "7px");
		
		txtTotTrasportoLocale= new Text();
		txtTotTrasportoLocale.setStyleAttribute("padding-top", "7px");
		//------------------
		
		
		btnConferma= new Button("Conferma");
		btnConferma.setWidth(80);
		btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				boolean checkbox= chbxAutoPropria.getValue();
				
				AdministrationService.Util.getInstance().saveDatiTrasfertaUtente(idRisorsaSelected, idCostingSelected, txtfldNumeroViaggi.getRawValue().toString(),
						txtfldOreViaggio.getRawValue().toString(), txtfldKmStradali.getRawValue().toString(),
						txtfldCarburante.getRawValue().toString(), txtfldAutostrada.getRawValue().toString(), checkbox, txtfldCostoTreno.getRawValue().toString(),
						txtfldCostoAereo.getRawValue().toString(), txtfldCostiVari.getRawValue().toString(), txtfldNumeroGiorni.getRawValue().toString(), txtfldNumeroNotti.getRawValue().toString(),
						txtfldCostoDiaria.getRawValue().toString(), txtfldCostoAlbergo.getRawValue().toString(), txtfldCostoPranzo.getRawValue().toString(), txtfldCostoCena.getRawValue().toString(), 
						txtfldNoleggioAuto.getRawValue().toString(), txtfldTrasportoLocale.getRawValue().toString(), new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore di connessione on saveDatiTrasfertaUtente();");
							}

							@Override
							public void onSuccess(Boolean result) {
								if(result)
									Window.alert("Inserimento dati avvenuto");
								else
									Window.alert("Impossibile inserire i dati!");								
							}
				});				
			}
		});
		
		txtfldTotCostiViaggio.setValue("0.00");
		txtfldTotCostiViaggio.setWidth(100);
		txtfldTotCostiViaggio.setFieldLabel("TOTALE COSTI VIAGGIO");
		txtfldTotCostiViaggio.setEnabled(false);
		
		txtfldTotCostiSoggiorno.setValue("0.00");
		txtfldTotCostiSoggiorno.setWidth(100);
		txtfldTotCostiSoggiorno.setEnabled(false);
		txtfldTotCostiSoggiorno.setFieldLabel("TOTALE COSTI SOGGIORNO");
		txtfldTotCostiSoggiorno.setStyleAttribute("padding-top", "6px");
		
		layoutCol1.add(txtfldNumeroViaggi,new FormData("85%"));
		layoutCol1.add(txtfldOreViaggio,new FormData("85%"));
		layoutCol1.add(txtfldKmStradali,new FormData("85%"));
		layoutCol1.add(txtfldCarburante,new FormData("85%"));
		layoutCol1.add(txtfldAutostrada,new FormData("85%"));
		layoutCol1.add(chbxAutoPropria,new FormData("85%"));
		layoutCol1.add(txtfldCostoKmAutoPropria, new FormData("85%"));
		layoutCol1.add(txtfldCostoTreno,new FormData("85%"));
		layoutCol1.add(txtfldCostoAereo,new FormData("85%"));
		layoutCol1.add(txtfldCostiVari,new FormData("85%"));
		
		layoutCol2.add(txtVuota1);
		layoutCol2.add(txtTotOreViaggio);
		layoutCol2.add(txtTotKmStradali);
		layoutCol2.add(txtTotCarburante);
		layoutCol2.add(txtTotAutostrada);
		layoutCol2.add(txtVuota2);
		layoutCol2.add(txtTotCostoKmAutoPropria);
		layoutCol2.add(txtTotCostoTreno);
		layoutCol2.add(txtTotCostoAereo);
		layoutCol2.add(txtTotCostiVari);
		
		layoutCol3.add(txtfldNumeroGiorni,new FormData("85%"));
		layoutCol3.add(txtfldNumeroNotti, new FormData("85%"));
		layoutCol3.add(txtfldCostoDiaria,new FormData("85%"));
		layoutCol3.add(txtfldCostoAlbergo,new FormData("85%"));
		layoutCol3.add(txtfldCostoPranzo,new FormData("85%"));
		layoutCol3.add(txtfldCostoCena,new FormData("85%"));
		layoutCol3.add(txtfldNoleggioAuto,new FormData("85%"));
		layoutCol3.add(txtfldTrasportoLocale,new FormData("85%"));
		
		layoutCol4.add(txtVuota3);
		layoutCol4.add(txtVuota4);
		layoutCol4.add(txtTotCostoDiaria);
		layoutCol4.add(txtTotCostoAlbergo);
		layoutCol4.add(txtTotCostoPranzo);
		layoutCol4.add(txtTotCostoCena);
		layoutCol4.add(txtTotNoleggioAuto);
		layoutCol4.add(txtTotTrasportoLocale);
		
		RowData data = new RowData(.55, 1);
		data.setMargins(new Margins(3));
		
		RowData data1 = new RowData(.45, 1);
		data1.setMargins(new Margins(3));
		
		cp1.add(layoutCol1,data);
		cp1.add(layoutCol2,data1);
		cp1.setBottomComponent(txtfldTotCostiViaggio);
		
		cp2.add(layoutCol3,data);
		cp2.add(layoutCol4,data1);
		cp2.setBottomComponent(txtfldTotCostiSoggiorno);
		
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
		
		loadDataForm();
		
		layoutContainer.add(cntpnlLayout, new FitData(3, 3, 3, 3));
		add(layoutContainer);
		
	}
	
	private void loadDataForm() {		
		AdministrationService.Util.getInstance().loadDataTrasferta(idCostingSelected, new AsyncCallback<DettaglioTrasfertaModel>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problemi di connessione on loadDataTrasferta()");
			}

			@Override
			public void onSuccess(DettaglioTrasfertaModel result) {
				if(result!=null)
					loadData(result);
			}
		});
	}
	
	private void loadData(DettaglioTrasfertaModel result) {
		txtfldNumeroViaggi.setValue(String.valueOf((Float) result.get("numViaggi")));
		txtfldOreViaggio.setValue(String.valueOf((Float)  result.get("oreViaggio")));
		txtfldKmStradali.setValue(String.valueOf((Float) result.get("kmStradali")));
		txtfldCarburante.setValue(String.valueOf((Float) result.get("costoCarburante")));
		txtfldAutostrada.setValue(String.valueOf((Float) result.get("costoAutostrada")));		
		chbxAutoPropria.setValue((Boolean) result.get("usoVetturaPropria"));
		txtfldCostoTreno.setValue(String.valueOf((Float) result.get("costoTreno")));
		txtfldCostoAereo.setValue(String.valueOf((Float)  result.get("costoAereo")));
		txtfldCostiVari.setValue(String.valueOf((Float)  result.get("costiVari")));
		
		txtfldNumeroGiorni.setValue(String.valueOf((Float)  result.get("numGiorni")));
		txtfldCostoDiaria.setValue(String.valueOf((Float)  result.get("diariaGiorno")));
		txtfldCostoAlbergo.setValue(String.valueOf((Float)  result.get("costoAlbergo")));
		txtfldCostoPranzo.setValue(String.valueOf((Float)  result.get("costoPranzo")));
		txtfldCostoCena.setValue(String.valueOf((Float)  result.get("costoCena")));
		txtfldNoleggioAuto.setValue(String.valueOf((Float)  result.get("costoNoleggioAuto")));
		txtfldTrasportoLocale.setValue(String.valueOf((Float)  result.get("costoTrasportiLocali")));
		
		txtfldCarburante.fireEvent(Events.KeyUp);
		txtfldCostoDiaria.fireEvent(Events.KeyUp);
		txtfldAutostrada.fireEvent(Events.KeyUp);
		txtfldCostoTreno.fireEvent(Events.KeyUp);
		txtfldCostoAereo.fireEvent(Events.KeyUp);
		txtfldCostiVari.fireEvent(Events.KeyUp);
		txtfldCostoAlbergo.fireEvent(Events.KeyUp);
		txtfldCostoPranzo.fireEvent(Events.KeyUp);
		txtfldCostoCena.fireEvent(Events.KeyUp);
		txtfldNoleggioAuto.fireEvent(Events.KeyUp);
		txtfldTrasportoLocale.fireEvent(Events.KeyUp);
		
		calcolaTotaleSoggiorno();
		calcolaTotaleViaggi();
	}

	private void calcolaTotaleSoggiorno() {
		 Float totaleC= (float)0.00;
		 Float numeroGiorni= Float.valueOf(txtfldNumeroGiorni.getRawValue().toString());
		
		 totaleC=/*(Float.valueOf(txtfldOreViaggio.getRawValue().toString()))+(Float.valueOf(txtfldKmStradali.getRawValue().toLowerCase()))+*/
				 (Float.valueOf(txtfldCostoAlbergo.getRawValue().toString()))+(Float.valueOf(txtfldCostoPranzo.getRawValue().toString()))+
				 (Float.valueOf(txtfldCostoCena.getRawValue().toString()))+(Float.valueOf(txtfldNoleggioAuto.getRawValue().toString()))+
				 (Float.valueOf(txtfldTrasportoLocale.getRawValue().toString()))+(Float.valueOf(txtfldCostoDiaria.getRawValue().toString()));
		 
		 totaleC=totaleC*numeroGiorni;
		 
		 txtfldTotCostiSoggiorno.setValue(number.format(totaleC));
	}
	
	private void calcolaTotaleViaggi() {
		 Float totaleC= (float)0.00;
		 Float numeroViaggi= Float.valueOf(txtfldNumeroViaggi.getRawValue().toString());
		
		 totaleC=/*(Float.valueOf(txtfldOreViaggio.getRawValue().toString()))+(Float.valueOf(txtfldKmStradali.getRawValue().toLowerCase()))+*/
				 (Float.valueOf(txtfldCarburante.getRawValue().toString()))+(Float.valueOf(txtfldAutostrada.getRawValue().toString()))+
				 (Float.valueOf(txtfldCostoTreno.getRawValue().toString()))+(Float.valueOf(txtfldCostoAereo.getRawValue().toString()))+
				 (Float.valueOf(txtfldCostiVari.getRawValue().toString()));
		 
		 totaleC=totaleC*numeroViaggi;
		 
		 txtfldTotCostiViaggio.setValue(number.format(totaleC));
	}
	
	private boolean hasValue(TextField<String> field) {
		return field.getValue() != null && field.isValid();
	}
}
