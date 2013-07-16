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
package gestione.pack.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SessionManagementService")
public interface SessionManagementService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static SessionManagementServiceAsync instance;
		public static SessionManagementServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(SessionManagementService.class);
			}
			return instance;
		}
	}
	
	public void setUserName(String userName)throws IllegalArgumentException;
	
	String getUserName()throws IllegalArgumentException;

	public String getRuolo()throws IllegalArgumentException;

	
	public String login(String username, String password)throws IllegalArgumentException;

	void logOut()throws IllegalArgumentException;

	boolean setDataInSession(String mese, String sede, String username, String operazione);

	boolean setDataInSession(String dataRif, String username, String operazione);

	String getSede();
	
	
}
