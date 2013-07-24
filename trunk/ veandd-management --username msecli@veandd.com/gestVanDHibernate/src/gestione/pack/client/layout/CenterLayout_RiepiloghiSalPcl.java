package gestione.pack.client.layout;

import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class CenterLayout_RiepiloghiSalPcl extends LayoutContainer{

	public CenterLayout_RiepiloghiSalPcl(){
		
	}
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private TabItem tbSal = new TabItem("SAL");
	private TabItem tbPcl = new TabItem("PCL");  	
	private TabPanel tabpnlRiepSalPcl; 
	private ContentPanel cpnlContainTab;
	
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
		cpnlContainTab.setSize(w-180, h-75);
		cpnlContainTab.setPosition(3, 3);
		cpnlContainTab.setScrollMode(Scroll.AUTO);
		cpnlContainTab.setLayout(new FitLayout());
		cpnlContainTab.addListener(Events.Resize, new Listener<ComponentEvent>() {

			@Override
			public void handleEvent(ComponentEvent be) {			
				tabpnlRiepSalPcl.setWidth(cpnlContainTab.getWidth()-15);
				tabpnlRiepSalPcl.setHeight(cpnlContainTab.getHeight()-20);
				
			}
						
		});
		
		Resizable r=new Resizable(cpnlContainTab);
	    r.setMinWidth(w-200);
	    	
	    ToolBar tlbScelte=new ToolBar();
	    tlbScelte.setBorders(false);
	    tlbScelte.setSpacing(5); 
	    
	    SimpleComboBox<String> smplcmbxPM= new SimpleComboBox<String>();
	    tlbScelte.add(smplcmbxPM);
	    
	    SimpleComboBox<String> smplcmbxAnno= new SimpleComboBox<String>();
	    tlbScelte.add(smplcmbxAnno);
	    
	    SimpleComboBox<String> smplcmbxMese= new SimpleComboBox<String>();
	    tlbScelte.add(smplcmbxMese);
	    
	    Button btnPrint= new Button();
	    btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
	    btnPrint.setIconAlign(IconAlign.TOP);
	    btnPrint.setToolTip("Stampa");
	    tlbScelte.add(btnPrint);
	   
	    tabpnlRiepSalPcl= new TabPanel();
	    tabpnlRiepSalPcl.setPlain(false);
	    tabpnlRiepSalPcl.setSize(w-220, h-95);
	   	    	    	    
	    tabpnlRiepSalPcl.add(tbSal);  
	   
	    tabpnlRiepSalPcl.add(tbPcl);
	    
	    cpnlContainTab.setTopComponent(tlbScelte);
	    cpnlContainTab.setBottomComponent(tabpnlRiepSalPcl);
	    	    	    
		layoutContainer.add(cpnlContainTab, new FitData(3, 3, 3, 3));
		add(layoutContainer);
	}
}
