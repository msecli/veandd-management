package gestione.pack.client.layout;

import gestione.pack.client.layout.panel.PanelCommessa;

import com.extjs.gxt.ui.client.Style.Scroll;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

public class CenterLayout_GestioneCommessa extends LayoutContainer{
	public CenterLayout_GestioneCommessa() {
	}
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
		
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);

		final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		layoutContainer.setWidth(w-225);
		layoutContainer.setHeight(h-54);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);		
								
	/*	ContentPanel cntpnlCommessa = new ContentPanel();
		cntpnlCommessa.setHeading("Gestione Commessa");
		cntpnlCommessa.setHeaderVisible(true);
		cntpnlCommessa.setCollapsible(false);
		cntpnlCommessa.setBorders(false);
		cntpnlCommessa.setWidth(w-215);
		cntpnlCommessa.setHeight(h-55);
		cntpnlCommessa.setScrollMode(Scroll.AUTO);
			
		cntpnlCommessa.add(new PanelCommessa());*/
		
		bodyContainer.add(new PanelCommessa());
		
		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);
	}
	
}

	
