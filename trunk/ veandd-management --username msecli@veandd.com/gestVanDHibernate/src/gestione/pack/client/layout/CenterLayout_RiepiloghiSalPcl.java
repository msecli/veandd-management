package gestione.pack.client.layout;

import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import gestione.pack.client.layout.panel.PanelRiepilogoSalPclMese;
import gestione.pack.client.layout.panel.PanelRiepilogoSalPclRiassunto;
import gestione.pack.client.model.RiepilogoSALPCLModel;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class CenterLayout_RiepiloghiSalPcl extends LayoutContainer{

	public CenterLayout_RiepiloghiSalPcl(){
		
	}
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private TabItem tbSal = new TabItem("SAL");
	private TabItem tbPcl = new TabItem("PCL");
	private TabItem tbRiassunto= new TabItem("Riassunto");
	private TabPanel tabpnlRiepSalPcl; 
	private ContentPanel cpnlContainTab;
	private Button btnSelect;	
	
	//private SimpleComboBox<String> smplcmbxPM;
	private SimpleComboBox<String> smplcmbxAnno;
	private SimpleComboBox<String> smplcmbxMese;
	
	private String tabSelected;
	private String data= new String();
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
				
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
			
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);	
					
		cpnlContainTab= new ContentPanel();
		cpnlContainTab.setHeaderVisible(false);
		cpnlContainTab.setBorders(false);
		cpnlContainTab.setFrame(true);
		cpnlContainTab.setSize(w-180, h-95);
		//cpnlContainTab.setPosition(3, 3);
		//cpnlContainTab.setScrollMode(Scroll.AUTO);
		cpnlContainTab.setLayout(new FitLayout());
		/*cpnlContainTab.addListener(Events.Resize, new Listener<ComponentEvent>() {

			@Override
			public void handleEvent(ComponentEvent be) {			
				tabpnlRiepSalPcl.setWidth(cpnlContainTab.getWidth()-15);
				tabpnlRiepSalPcl.setHeight(cpnlContainTab.getHeight()-20);
				
			}
						
		});
		*/
		Resizable r=new Resizable(cpnlContainTab);
	    r.setMinWidth(w-200);
	    	
	    ToolBar tlbScelte=new ToolBar();
	    tlbScelte.setBorders(false);
	    tlbScelte.setSpacing(5); 
	    /*
	    smplcmbxPM= new SimpleComboBox<String>();
	    tlbScelte.add(smplcmbxPM);
	    */
	    
	    Date d= new Date();
		String dt= d.toString();
		String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(dt.substring(4, 7)));
		String anno= dt.substring(dt.length()-4, dt.length());
	    
	    smplcmbxAnno= new SimpleComboBox<String>();
	    smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setAllowBlank(false);
		 for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);}
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setSimpleValue(anno);
	    tlbScelte.add(smplcmbxAnno);
	    
	    smplcmbxMese= new SimpleComboBox<String>();
	    smplcmbxMese.setFieldLabel("Mese");
		smplcmbxMese.setName("mese");
		smplcmbxMese.setEmptyText("Mese..");
		smplcmbxMese.setAllowBlank(false);
		 for(String l : DatiComboBox.getMese()){
			 smplcmbxMese.add(l);}
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		smplcmbxMese.setSimpleValue(mese);
	    tlbScelte.add(smplcmbxMese);
	    
	    Button btnPrint= new Button();
	    btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
	    btnPrint.setIconAlign(IconAlign.TOP);
	    btnPrint.setToolTip("Stampa");
	    //tlbScelte.add(btnPrint);
	    
	    btnSelect= new Button();
		btnSelect.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.reload()));
		btnSelect.setToolTip("Load");
		btnSelect.setIconAlign(IconAlign.TOP);
		btnSelect.setSize(26, 26);
		btnSelect.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {		
				if(smplcmbxMese.isValid()&&smplcmbxAnno.isValid()){			
					String meseRif= new String(); 
			    	String anno= new String();
			    				    				    	
			    	anno= smplcmbxAnno.getRawValue().toString();
					meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
					data=meseRif+anno;
								
					if(tabSelected.compareTo("sal")==0){
						tbSal.removeAll();
						tbSal.add(new PanelRiepilogoSalPclMese(tabSelected, data));
						tbSal.layout(true);
						cpnlContainTab.layout();
					}else
						if(tabSelected.compareTo("pcl")==0){
							tbPcl.removeAll();
							tbPcl.add(new PanelRiepilogoSalPclMese(tabSelected, data));
							tbPcl.layout(true);
							//cpnlContainTab.layout();							
						}else{
							tbRiassunto.removeAll();
							tbRiassunto.add(new PanelRiepilogoSalPclRiassunto("", data));
							tbRiassunto.layout(true);							
						}
					
				}else Window.alert("Controllare i dati selezionati!");
			}
		});		
		
		tlbScelte.add(btnSelect);
	   
	    tabpnlRiepSalPcl= new TabPanel();
	    tabpnlRiepSalPcl.setPlain(false);
	    tabpnlRiepSalPcl.setSize(w-220, h-95);
	   	    	  
	    tbSal.setTitle("SAL");
	    tbSal.setScrollMode(Scroll.AUTO);
	    tbSal.addListener(Events.Select, new Listener<ComponentEvent>() {  
	        public void handleEvent(ComponentEvent be) {  
	        	String meseRif= new String(); 
		    	String anno= new String();
		    				    				    	
		    	anno= smplcmbxAnno.getRawValue().toString();
				meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
				data=meseRif+anno;
				
	        	tabSelected="sal";
	        	tbSal.removeAll();
	        	tbSal.add(new PanelRiepilogoSalPclMese(tabSelected, data));
	        	tbSal.layout(true);
	          }  
	    });  
	    tabpnlRiepSalPcl.add(tbSal);  
	   
	    tbPcl.setTitle("PCL");
	    tbPcl.setScrollMode(Scroll.AUTO);
	    tbPcl.addListener(Events.Select, new Listener<ComponentEvent>() {  
	        public void handleEvent(ComponentEvent be) {  
	        	String meseRif= new String(); 
		    	String anno= new String();
		    				    				    	
		    	anno= smplcmbxAnno.getRawValue().toString();
				meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
				data=meseRif+anno;
				
	        	tabSelected="pcl";
	        	tbPcl.removeAll();
	        	tbPcl.add(new PanelRiepilogoSalPclMese(tabSelected, data));
	        	tbPcl.layout(true);
	          }  
	    });  
	    tabpnlRiepSalPcl.add(tbPcl);
	    
	    tbRiassunto.setTitle("Riassunto");
	    tbRiassunto.setScrollMode(Scroll.AUTO);
	    tbRiassunto.addListener(Events.Select, new Listener<ComponentEvent>() {  
	        public void handleEvent(ComponentEvent be) {  
	        	String meseRif= new String(); 
		    	String anno= new String();
		    				    				    	
		    	anno= smplcmbxAnno.getRawValue().toString();
				meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
				data=meseRif+anno;
				
	        	tabSelected="riassunto";
	        	tbRiassunto.removeAll();
	        	tbRiassunto.add(new PanelRiepilogoSalPclRiassunto("", data));
	        	tbRiassunto.layout(true);
	        }  
	    });  
	    tabpnlRiepSalPcl.add(tbRiassunto);
	    
	    cpnlContainTab.setTopComponent(tlbScelte);
	    cpnlContainTab.setBottomComponent(tabpnlRiepSalPcl);
	    	    	    
		layoutContainer.add(cpnlContainTab, new FitData(3, 3, 3, 3));
		add(layoutContainer);
	}		
}
