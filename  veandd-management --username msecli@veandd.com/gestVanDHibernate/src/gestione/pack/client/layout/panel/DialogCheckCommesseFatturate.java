package gestione.pack.client.layout.panel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class DialogCheckCommesseFatturate  extends Dialog{
	
	public DialogCheckCommesseFatturate(){
		
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
	
		
		
	}

}
