package gestione.pack.client.layout.panel;

import gestione.pack.client.layout.CenterLayout_FoglioOreGiornalieroAutoTimb.FldsetGiustificativi;

import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
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
	
	public TextField<String> txtfldTotOreLavoro = new TextField<String>();
	public TextField<String> txtfldTotOreViaggio = new TextField<String>();
	public Text txtDescrizione = new Text();
	
	private Button btnAssegnaOre;
	private Button btnAzzeraOre;
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
	    btnAssegnaOre.setSize(15, 15);
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
	    btnAzzeraOre.setSize(15,15);
	    btnAzzeraOre.setStyleAttribute("padding-left", "3px");
	    btnAzzeraOre.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				txtfldOreIntervallo.setValue("0.00");
				txtfldOreViaggio.setValue("0.00");		
			}
		});
		
		txtfldNumeroCommessa.setFieldLabel("Commessa");
		txtfldNumeroCommessa.setItemId("numeroCommessa");
		txtfldNumeroCommessa.setEnabled(false);

		txtfldOreIntervallo.setFieldLabel("Ore Lavoro");
		txtfldOreIntervallo.setMaxLength(7);
		txtfldOreIntervallo.setItemId("oreLavoro");
		txtfldOreIntervallo.setAllowBlank(false);
		//txtfldOreIntervallo.setRegex("[0-9]*[.]?[0-5]{1}[0-9]{1}|0.0|0.00");
		txtfldOreIntervallo.setRegex("^([0-9]+).(0|00|15|30|45)$");
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
		
		txtfldOreViaggio.setFieldLabel("Ore Viaggio");
		txtfldOreViaggio.setMaxLength(7);
		txtfldOreViaggio.setItemId("oreViaggio");
		txtfldOreViaggio.setAllowBlank(false);
		txtfldOreViaggio.setRegex("^([0-9]+).(0|00|15|30|45)$");
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
		
		txtfldTotOreLavoro.setFieldLabel("Tot.Mese");
		txtfldTotOreLavoro.setMaxLength(10);
		txtfldTotOreLavoro.setItemId("totOreLavoro");
		txtfldTotOreLavoro.setEnabled(false);
		
		txtfldTotOreViaggio.setFieldLabel("Tot.Mese");
		txtfldTotOreViaggio.setMaxLength(10);
		txtfldTotOreViaggio.setItemId("totOreViaggio");
		txtfldTotOreViaggio.setEnabled(false);
		//txtfldTotOreViaggio.setStyleAttribute("padding-top", "10px");
		
		txtDescrizione.setItemId("descrizione");
		
		//Per aggiungere il button assegna ore direttamente devo anche considerare un livello in più per 
		//poi accedere al dato dal foglio ore
		
		ContentPanel cpTxtBtn= new ContentPanel();
		cpTxtBtn.setHeaderVisible(false);
		cpTxtBtn.setSize(70,26);
		cpTxtBtn.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cpTxtBtn.add(btnAssegnaOre,new RowData(.16, 2));
		cpTxtBtn.add(btnAzzeraOre,new RowData(.16, 2));
		//cpTxtBtn.setStyleAttribute("float", "right");
		
		ContentPanel cpVuoto= new ContentPanel();
		cpVuoto.setHeaderVisible(false);
		cpVuoto.setSize(70,26);
		
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(75);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol1.setLayout(layout);
		layoutCol1.setItemId("lc1");

		layout = new FormLayout();
		layout.setLabelWidth(75);
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
		cp.setSize(580, 85);
		cp.setBorders(false);
		cp.setBodyBorder(false);
		cp.setFrame(false);
		cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cp.setStyleAttribute("padding-top", "10px");
		cp.setItemId("cp");		

		layoutCol1.add(txtfldNumeroCommessa, new FormData("80%"));
		layoutCol1.add(txtDescrizione, new FormData("100%"));

		if(tipoParent.compareTo("1")==0){
			layoutCol2.add(cpTxtBtn, new FormData("74%"));
			layoutCol2.add(txtfldOreIntervallo, new FormData("74%"));
			layoutCol2.add(txtfldOreViaggio, new FormData("74%"));
		
			layoutCol3.add(cpVuoto, new FormData("74%"));
			layoutCol3.add(txtfldTotOreLavoro, new FormData("74%"));
			layoutCol3.add(txtfldTotOreViaggio,new FormData("74%"));
		}else{
			
			layoutCol2.add(txtfldOreIntervallo, new FormData("74%"));
			layoutCol2.add(txtfldOreViaggio, new FormData("74%"));
					
			layoutCol3.add(txtfldTotOreLavoro, new FormData("74%"));
			layoutCol3.add(txtfldTotOreViaggio,new FormData("74%"));
		}
			
		cp.add(layoutCol1, new RowData(.40, 1));
		cp.add(layoutCol2, new RowData(.30, 1));
		cp.add(layoutCol3, new RowData(.30, 1));
		add(cp);

	}

}
