package gestione.pack.client.layout.panel;

import gestione.pack.client.layout.CenterLayout_FoglioOreGiornalieroAutoTimb.FldsetGiustificativi;

import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class FormInserimentoIntervalloCommessa extends LayoutContainer {

	public LayoutContainer layoutCol1 = new LayoutContainer();
	public LayoutContainer layoutCol2 = new LayoutContainer();
	public LayoutContainer layoutCol3 = new LayoutContainer();

	public TextField<String> txtfldNumeroCommessa= new TextField<String>();
	public TextField<String> txtfldOreIntervallo = new TextField<String>();
	public TextField<String> txtfldOreViaggio = new TextField<String>();
	public TextField<String> txtfldOreStrao = new TextField<String>();
	
	public TextField<String> txtfldTotOreLavoro = new TextField<String>();
	public TextField<String> txtfldTotOreViaggio = new TextField<String>();
	
	public Text txtDescrizione = new Text();
	public Text txtOreTotLavoro = new Text();
	public Text txtOreTotViaggio = new Text();
	public Text txtAbilitazioneStrao= new Text();
	
	private Button btnAssegnaOre;
	public Button btnAzzeraOre;
	public Button btnHelp= new Button();
	private String tipoParent;
		
	public FormInserimentoIntervalloCommessa(String tipoParent) {
		this.tipoParent=tipoParent;
	}

	protected void onRender(Element target, int index) {
		super.onRender(target, index);
				
	    btnAssegnaOre= new Button();
	    btnAssegnaOre.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.arrowdown()));
	    btnAssegnaOre.setToolTip("Assegna Totale Ore");
	    btnAssegnaOre.setStyleAttribute("padding-left", "2px");
	    btnAssegnaOre.setIconAlign(IconAlign.TOP);
	    btnAssegnaOre.setSize(20, 15);
	    btnAssegnaOre.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				String oreLavoro= new String();
				String oreViaggio= new String();
				
				LayoutContainer lc= new LayoutContainer(); 
			  	LayoutContainer right= new LayoutContainer();
				
			  	FldsetGiustificativi fldsetGiustificativo;
				lc=(LayoutContainer) getParent().getParent().getParent();
				right=(LayoutContainer) lc.getItemByItemId("right");
				fldsetGiustificativo=(FldsetGiustificativi) right.getItemByItemId("fldSetGiustificativi");
				oreLavoro=fldsetGiustificativo.txtfldTotGenerale.getValue().toString();
				oreViaggio=fldsetGiustificativo.txtfldOreViaggio.getValue().toString();
				
				txtfldOreIntervallo.setValue(oreLavoro);
				txtfldOreViaggio.setValue(oreViaggio);				
			}
		});
	    
	    btnAzzeraOre= new Button();
	    btnAzzeraOre.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.azzera()));
	    btnAzzeraOre.setIconAlign(IconAlign.TOP);
	    btnAzzeraOre.setToolTip("Azzera Ore");
	    btnAzzeraOre.setSize(20,15);
	    btnAzzeraOre.setStyleAttribute("padding-left", "5px");
	    btnAzzeraOre.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				txtfldOreIntervallo.setValue("0.00");
				txtfldOreViaggio.setValue("0.00");		
			}
		});
	    
	    //btnHelp= new Button();
	    btnHelp.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.idea()));
	    btnHelp.setIconAlign(IconAlign.TOP);
	    btnHelp.setToolTip("Indicare qui le ore straordinarie sulla commessa");
	    btnHelp.setSize(18,18);
	    //btnHelp.setEnabled(false);
	    btnHelp.setStyleAttribute("padding-left", "5px");
	   		
		txtfldNumeroCommessa.setFieldLabel("Commessa");
		txtfldNumeroCommessa.setItemId("numeroCommessa");
		txtfldNumeroCommessa.setEnabled(false);

		txtfldOreIntervallo.setFieldLabel("h/Lavoro Tot");
		txtfldOreIntervallo.setMaxLength(7);
		txtfldOreIntervallo.setItemId("oreLavoro");
		txtfldOreIntervallo.setAllowBlank(false);
		//txtfldOreIntervallo.setRegex("[0-9]*[.]?[0-5]{1}[0-9]{1}|0.0|0.00");
		txtfldOreIntervallo.setRegex("^([0-9]+)[.](0|00|15|30|45)$");
		txtfldOreIntervallo.getMessages().setRegexText("Deve essere un numero nel formato 99.59 ed espresso in sessantesimi");
		txtfldOreIntervallo.addKeyListener(new KeyListener(){
			 @Override
		      public void componentKeyDown(ComponentEvent event) { 	  
		    	int keyCode=event.getKeyCode();
				if(keyCode==9){			
					
					if(txtfldOreIntervallo.getValue()==null)
						txtfldOreIntervallo.setValue("0.00");
					else{
						String valore= txtfldOreIntervallo.getValue().toString();
												
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
						txtfldOreIntervallo.setValue(valore);
					}
				}	    		
		      }
		});
		
		txtfldOreViaggio.setFieldLabel("h/Viaggio");
		txtfldOreViaggio.setMaxLength(7);
		txtfldOreViaggio.setItemId("oreViaggio");
		txtfldOreViaggio.setAllowBlank(false);
		txtfldOreViaggio.setRegex("^([0-9]+)[.](0|00|15|30|45)$");
		txtfldOreViaggio.getMessages().setRegexText("Deve essere un numero nel formato 99.59 ed espresso in sessantesimi");
		txtfldOreViaggio.addKeyListener(new KeyListener(){
			 @Override
		      public void componentKeyDown(ComponentEvent event) { 	  
		    	int keyCode=event.getKeyCode();
				if(keyCode==9){			
					
					if(txtfldOreViaggio.getValue()==null)
						txtfldOreViaggio.setValue("0.00");
					else{
						String valore= txtfldOreViaggio.getValue().toString();
												
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
						txtfldOreViaggio.setValue(valore);
					}
				}	    		
		      }
		});
		
		txtfldOreStrao.setFieldLabel("h/Straord.");
		txtfldOreStrao.setMaxLength(7);
		txtfldOreStrao.setItemId("oreStraordinario");
		txtfldOreStrao.setAllowBlank(false);
		txtfldOreStrao.setRegex("^([0-9]+)[.](0|00|15|30|45)$");
		txtfldOreStrao.getMessages().setRegexText("Deve essere un numero nel formato 99.59 ed espresso in sessantesimi");
		txtfldOreStrao.addKeyListener(new KeyListener(){
			 @Override
		      public void componentKeyDown(ComponentEvent event) {
				 int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldOreStrao.getValue()==null)
							txtfldOreStrao.setValue("0.00");
						else{
							String valore= txtfldOreStrao.getValue().toString();
													
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
							txtfldOreStrao.setValue(valore);
						}
					}	    
		      }
		});
		
		txtAbilitazioneStrao.setVisible(true);
		txtAbilitazioneStrao.setStyleAttribute("color", "#d7dbdb");
		txtAbilitazioneStrao.setStyleAttribute(" font-style", "italic");
		txtAbilitazioneStrao.setStyleAttribute("padding-top", "4px");
		txtAbilitazioneStrao.setText("Straordinario non autorizzato.");
		
		/*
		txtfldTotOreLavoro.setFieldLabel("Tot.Mese");
		txtfldTotOreLavoro.setMaxLength(10);
		txtfldTotOreLavoro.setItemId("totOreLavoro");
		txtfldTotOreLavoro.setEnabled(false);
		
		txtfldTotOreViaggio.setFieldLabel("Tot.Mese");
		txtfldTotOreViaggio.setMaxLength(10);
		txtfldTotOreViaggio.setItemId("totOreViaggio");
		txtfldTotOreViaggio.setEnabled(false);*/
		//txtfldTotOreViaggio.setStyleAttribute("padding-top", "10px");
		
		txtDescrizione.setItemId("descrizione");
		txtDescrizione.setStyleAttribute("font-size","13px" );
		txtDescrizione.setStyleAttribute("font-weight","bold" );
		HorizontalPanel hp1= new HorizontalPanel();
		hp1.setSpacing(2);
		hp1.add(txtDescrizione);
		
		txtOreTotLavoro.setStyleAttribute("font-size","13px" );
		
		txtOreTotViaggio.setStyleAttribute("font-size","13px" );
		txtOreTotViaggio.setStyleAttribute("padding-top", "11px");
		
		//Per aggiungere il button assegna ore direttamente devo anche considerare un livello in più per 
		//poi accedere al dato dal foglio ore
		
		ContentPanel cpTxtBtn= new ContentPanel();
		cpTxtBtn.setHeaderVisible(false);
		cpTxtBtn.setSize(70,25);
		cpTxtBtn.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cpTxtBtn.add(btnAssegnaOre,new RowData(.30, 1));
		cpTxtBtn.add(btnAzzeraOre,new RowData(.30, 1));
		//cpTxtBtn.setStyleAttribute("float", "right");
		
		ContentPanel cpTxtBtn1= new ContentPanel();
		cpTxtBtn1.setHeaderVisible(false);
		cpTxtBtn1.setSize(70,25);
		cpTxtBtn1.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cpTxtBtn1.add(btnHelp,new RowData(.30, 1));
		cpTxtBtn1.setStyleAttribute("margin-top", "25px");
		
		ContentPanel cpVuoto= new ContentPanel();
		cpVuoto.setHeaderVisible(false);
		cpVuoto.setSize(70,26);
		
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(85);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol1.setLayout(layout);
		layoutCol1.setItemId("lc1");

		layout = new FormLayout();
		layout.setLabelWidth(85);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol2.setLayout(layout);
		layoutCol2.setItemId("lc2");
		
		layout = new FormLayout();
		layout.setLabelWidth(75);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol3.setLayout(layout);
		layoutCol3.setItemId("lc3");
	
		ContentPanel cp = new ContentPanel();
		cp.setHeaderVisible(false);
		cp.setSize(550, 112);
		cp.setBorders(false);
		cp.setBodyBorder(false);
		cp.setFrame(true);
		cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cp.setItemId("cp");		

		//TODO ore strao
		
		if(tipoParent.compareTo("1")==0){
			layoutCol1.add(cpTxtBtn, new FormData("100%"));
			layoutCol1.add(cpTxtBtn1, new FormData("100%"));
			layoutCol2.add(txtfldOreIntervallo, new FormData("100%"));
			layoutCol2.add(txtfldOreViaggio, new FormData("100%"));
			//layoutCol2.add(txtfldOreStrao, new FormData("100%"));
			//layoutCol2.add(txtAbilitazioneStrao, new FormData("100%"));
		
			//layoutCol3.add(cpVuoto, new FormData("74%"));
			layoutCol3.add(txtOreTotLavoro, new FormData("100%"));
			layoutCol3.add(txtOreTotViaggio,new FormData("100%"));
			//layoutCol3.add(txtAbilitazioneStrao, new FormData("100%"));
		}else{
			
			layoutCol2.add(txtfldOreIntervallo, new FormData("100%"));
			layoutCol2.add(txtfldOreViaggio, new FormData("100%"));
			//layoutCol2.add(txtfldOreStrao, new FormData("100%"));
					
			layoutCol3.add(txtOreTotLavoro, new FormData("100%"));
			layoutCol3.add(txtOreTotViaggio,new FormData("100%"));
		}			
		
		cp.setTopComponent(hp1);
				
		cp.add(layoutCol2, new RowData(.25, 1, new Margins(2)));
		cp.add(layoutCol1, new RowData(.15, 1, new Margins(2)));
		cp.add(layoutCol3, new RowData(.32, 1, new Margins(2)));
		add(cp, new MarginData(5));

	}

}
