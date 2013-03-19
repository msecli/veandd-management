package gestione.pack.client;

import gestione.pack.client.layout.BodyLayout_Administration;
import gestione.pack.client.layout.BodyLayout_Dipendente;
import gestione.pack.client.layout.BodyLayout_Direzione;
import gestione.pack.client.layout.BodyLayout_PersonalManager;
import gestione.pack.client.layout.BodyLayout_UffAmministrazione;
import gestione.pack.client.layout.BodyLayout_GestionePersonale;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;

/*
 * Gestione del login tramite accesso controllato. In base all'utente che accede verr� restituito un layout con 
 * operazioni differenti.
 * */
public class GestVanDHibernate implements EntryPoint {
	
		
	
	public void onModuleLoad() {		
		LoginDialog d = new LoginDialog();
		d.show();
		}
	}
	
   	class LoginDialog extends Dialog {

	  protected TextField<String> userName;
	  protected TextField<String> password;
	  protected Button reset;
	  protected Button login;
	  protected Status status;
	  

	  public LoginDialog() {
	    FormLayout layout = new FormLayout();
	    layout.setLabelWidth(90);
	    layout.setDefaultWidth(155);
	    setLayout(layout);
	    
	    setButtonAlign(HorizontalAlignment.LEFT);
	    setButtons("");
	    //setIcon(IconHelper.createStyle("user"));
	    setHeading("Login");
	    setModal(true);
	    setBodyBorder(true);
	    setBodyStyle("padding: 8px; background: none");
	    setWidth(320);
	    setResizable(false);
	    setClosable(false);

	    KeyListener keyListener = new KeyListener() {
	      
	    public void componentKeyUp(ComponentEvent event) {
	        validate();
	      }
	   
	    @Override
	      public void componentKeyPress(ComponentEvent event) { 	  
	    	int keyCode=event.getKeyCode();
			if(keyCode==13){			
				onSubmit();
			}	    		
	      }    
	    };

	    userName = new TextField<String>();
	    userName.setMinLength(4);
	    userName.setFieldLabel("Username");
	    userName.addKeyListener(keyListener);
	    add(userName);

	    password = new TextField<String>();
	    password.setMinLength(4);
	    password.setPassword(true);
	    password.setFieldLabel("Password");
	    password.addKeyListener(keyListener);
	    add(password);
	   
	    setFocusWidget(userName);
	  }

	  @Override
	  protected void createButtons() {
	    super.createButtons();
	    status = new Status();
	    status.setBusy("please wait...");
	    status.hide();
	    status.setAutoWidth(true);
	    getButtonBar().add(status);
	    
	    getButtonBar().add(new FillToolItem());
	    
	    reset = new Button("Reset");
	    reset.addSelectionListener(new SelectionListener<ButtonEvent>() {
	      public void componentSelected(ButtonEvent ce) {
	        userName.reset();
	        password.reset();
	        validate();
	        userName.focus();
	        status.hide();
	      }

	    });

	    login = new Button("Login");
	    login.disable();
	    login.addSelectionListener(new SelectionListener<ButtonEvent>() {
	      public void componentSelected(ButtonEvent ce) {
	        onSubmit();
	      }
	    });
	    addButton(reset);
	    addButton(login);    
	  }

	  
	  protected void onSubmit() {
		status.setBusy("please wait...");
	    status.show();
	    getButtonBar().disable();
	  	    
	    Timer t = new Timer() {
	      @Override
	      public void run() {
	    	  
	    	  SessionManagementService.Util.getInstance().login(userName.getValue(), password.getValue(), new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					
					status.setText("Error on login();");
				}
				
				@Override
				public void onSuccess(String result) {
					
					if(result!=null){
						callLayout(result);
						
					}
					
					else{
							status.setText("Dati non corretti!");
							reset.setEnabled(true);
							login.setEnabled(true);
							getButtonBar().enable();
						}								
				}

	  		}); //AsyncCallback  
	    	    	          
	      }// run();

	    };//Timer();
	    t.schedule(1500);
	  }
	  

	  protected boolean hasValue(TextField<String> field) {
	    return field.getValue() != null && field.getValue().length() > 0;
	  }

	  protected void validate() {
	    login.setEnabled(hasValue(userName) && hasValue(password)
	        && password.getValue().length() > 0);
	  }
	   
	  
	  private void callLayout(String result) {
		  	
		  if(result.equals("UA")){
			  BodyLayout_UffAmministrazione bl= new BodyLayout_UffAmministrazione();
			  bl.txtfldUsername.setValue(userName.getValue().toString());
			  bl.txtfldRuolo.setValue(result);		  	  
			  RootPanel.get().add(bl);
			  LoginDialog.this.hide();
		  	}
		  
		  if(result.equals("UG")){
			  BodyLayout_GestionePersonale bl= new BodyLayout_GestionePersonale();
			  bl.txtfldUsername.setValue(userName.getValue().toString());
			  bl.txtfldRuolo.setValue(result);
			  RootPanel.get().add(bl);
			  LoginDialog.this.hide();  
		   }
		  
		  if(result.equals("AMM")){		  
			  BodyLayout_Administration bl=new BodyLayout_Administration();
			  bl.txtfldUsername.setValue(userName.getValue().toString());
			  bl.txtfldRuolo.setValue(result);
			  RootPanel.get().add(bl); 
			  LoginDialog.this.hide();  
		   }
		  
		  if(result.equals("DIR")){		  
			  BodyLayout_Direzione bl=new BodyLayout_Direzione();
			  bl.txtfldUsername.setValue(userName.getValue().toString());
			  bl.txtfldRuolo.setValue(result);
			  RootPanel.get().add(bl); 
			  LoginDialog.this.hide();  
		   }
		  
		  if(result.equals("PM")){
			  BodyLayout_PersonalManager bl= new BodyLayout_PersonalManager();
			  bl.txtfldUsername.setValue(userName.getValue().toString());
			  bl.txtfldRuolo.setValue(result);
			  RootPanel.get().add(bl);
			  LoginDialog.this.hide();  
		  }

		  if(result.equals("DIP")){
			  BodyLayout_Dipendente bl= new BodyLayout_Dipendente();
			  bl.txtfldUsername.setValue(userName.getValue().toString());
			  bl.txtfldRuolo.setValue(result);
			  RootPanel.get().add(bl);
			  LoginDialog.this.hide();  
			  
		  }
		  
	  }	 

	}

