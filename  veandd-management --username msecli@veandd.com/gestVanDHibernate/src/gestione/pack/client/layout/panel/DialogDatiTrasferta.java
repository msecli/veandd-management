package gestione.pack.client.layout.panel;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class DialogDatiTrasferta extends Dialog {

	public DialogDatiTrasferta(){
		
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
		
		
		
		layoutContainer.add(cntpnlLayout, new FitData(3, 3, 3, 3));
		add(layoutContainer);
		
	}
	
}
