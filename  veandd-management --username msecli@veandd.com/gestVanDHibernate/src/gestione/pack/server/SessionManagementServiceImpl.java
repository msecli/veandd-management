/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package gestione.pack.server;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.gilead.gwt.PersistentRemoteService;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;

import org.hibernate.Session;
import org.hibernate.Transaction;

import gestione.pack.client.SessionManagementService;
import gestione.pack.shared.Personale;


public class SessionManagementServiceImpl extends PersistentRemoteService implements SessionManagementService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SessionManagementServiceImpl(){
		
		HibernateUtil gileadHibernateUtil = new HibernateUtil();
	    gileadHibernateUtil.setSessionFactory(MyHibernateUtil.getSessionFactory());

	    PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
	    persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
	    persistentBeanManager.setProxyStore(new StatelessProxyStore());

	    setBeanManager(persistentBeanManager);	
	}
	

	@Override
	public void setUserName(String userName) throws IllegalArgumentException {
		
		HttpSession httpSession = getThreadLocalRequest().getSession(true);  
	    httpSession.setAttribute("userName", userName);
	}

	@Override
	public String getUserName() throws IllegalArgumentException {
		
		HttpSession session = getThreadLocalRequest().getSession(true);
	    if (session.getAttribute("userName") != null)
	    {
	        return (String) session.getAttribute("userName");
	    }
	    else 
	    {
	        return "";
	    }		
	}
	
	public String getRuolo() throws IllegalArgumentException{
		
		HttpSession session=getThreadLocalRequest().getSession(true);
		 if (session.getAttribute("ruolo") != null)
		    {
		        return (String) session.getAttribute("ruolo");
		    }
		 else 
		    {
		        return "";
		    }
	}

	public String login(String username, String password){
	       
		   String ruolo= new String();
		   String descrizione= new String(); //contiene ruolo;tipoLavoratore
	       Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		   Transaction tx= null;
		   //Invalidare la sessione al momento del Login nel caso in cui ci fosse una sessione http ancora aperta 
		   HttpServletRequest request = this.getThreadLocalRequest();
	   	   HttpSession httpSession = request.getSession();	   
	   	   
	   	   if(!httpSession.isNew()){ 
	   		   
	   		   //httpSession.invalidate();  		  
	   	   }
	   	   
	       try{
	    	    tx=	session.beginTransaction();

				Personale p = new Personale();
				p=(Personale)session.createQuery("from Personale where username=:username and password=:password").setParameter("username", username).
						setParameter("password", password).uniqueResult();
				if(p!=null){
					descrizione=p.getRuolo()+";"+p.getTipologiaLavoratore();
					ruolo= p.getRuolo();
					httpSession.setAttribute("ruolo", ruolo);
					httpSession.setAttribute("userName", username);
					httpSession.setAttribute("idUtente", p.getId_PERSONALE());
				}else {ruolo=null;}
				          
				tx.commit();
				
	       }catch(Exception e){
	    	   ruolo = null;
	    	   if (tx!=null)
					tx.rollback();
	    	   e.printStackTrace();
	       }
	             
	       return descrizione;
	    }
	
	
	@Override
	public void logOut() {
		HttpServletRequest request = this.getThreadLocalRequest();
    	HttpSession session = request.getSession();
		session.invalidate();
	}


	@Override
	public boolean setDataInSession(String mese, String sede, String username, String operazione) {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("mese", mese);
		   	 httpSession.setAttribute("username", username);
		   	 httpSession.setAttribute("sede", sede);
		   	 httpSession.setAttribute("operazione", operazione);
		   	 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 		
	}


	@Override
	public boolean setDataInSession(String dataRif, String username, String operazione) {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("mese", dataRif);
		   	 httpSession.setAttribute("username", username);
		     httpSession.setAttribute("operazione", operazione);
		   	 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
	
}
