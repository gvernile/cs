/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.contact.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.exoplatform.contact.service.Contact;
import org.exoplatform.contact.service.ContactGroup;
import org.exoplatform.contact.service.ContactService;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.access.AccessControlEntry;
import org.exoplatform.services.jcr.access.PermissionType;
import org.exoplatform.services.jcr.core.ExtendedNode;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.impl.GroupImpl;

/**
 * Created by The eXo Platform SARL
 * Author : Hung Nguyen Quang
 *          hung.nguyen@exoplatform.com
 * Nov 23, 2007 3:09:21 PM
 */
public class NewUserListener extends UserEventListener {
  private ContactService cservice_ ;
  private NodeHierarchyCreator nodeHierarchyCreator_ ;
  public static String DEFAULTGROUP = "default".intern() ;
  public static String DEFAULTGROUPNAME = "My contacts".intern() ;
  public NewUserListener(ContactService cservice, NodeHierarchyCreator nodeHierarchyCreator) throws Exception {
  	cservice_ = cservice ;
  	nodeHierarchyCreator_ = nodeHierarchyCreator ;  	
  }
  
  public void postSave(User user, boolean isNew) throws Exception {  	
  	if(isNew) {
  		ContactGroup group = new ContactGroup() ;
  		group.setId(DEFAULTGROUP+user.getUserName()) ;
    	group.setName(DEFAULTGROUPNAME) ;
    	group.setDescription("Default address book") ;
    	SessionProvider sysProvider = SessionProvider.createSystemProvider() ;
    	cservice_.saveGroup(sysProvider, user.getUserName(), group, true) ;
    	Contact contact = new Contact() ;
    	contact.setId(user.getUserName()) ;
    	contact.setFullName(user.getFirstName() + " " + user.getLastName()) ;
    	contact.setFirstName(user.getFirstName()) ;
    	contact.setLastName(user.getLastName()) ;
      
      
    	contact.setEmailAddress(user.getEmail()) ;
    	Calendar cal = new GregorianCalendar() ;
    	contact.setLastUpdated(cal.getTime()) ;
    	List<String> groupIds = new ArrayList<String>()  ;
    	groupIds.add(group.getId()) ;
      
      OrganizationService organizationService = 
        (OrganizationService)PortalContainer.getComponent(OrganizationService.class) ;
      Object[] objGroupIds = organizationService.getGroupHandler().findGroupsOfUser(user.getUserName()).toArray() ;
      for (Object object : objGroupIds) {
        groupIds.add(((GroupImpl)object).getId()) ;
      }
    	contact.setAddressBook(groupIds.toArray(new String[]{})) ;
    	contact.setOwner(true) ;
    	contact.setOwnerId(user.getUserName()) ;
    	cservice_.saveContact(sysProvider, user.getUserName(), contact, true) ;
    	
      
      // added 23-4
      JCRDataStorage storage_ = new JCRDataStorage(nodeHierarchyCreator_) ;
      Node publicContactHome = storage_.getPublicContactHome(SessionProvider.createSystemProvider()) ;      
      String usersPath = nodeHierarchyCreator_.getJcrPath(JCRDataStorage.USERS_PATH) ;
      QueryManager qm = publicContactHome.getSession().getWorkspace().getQueryManager();
      List<String> recievedUser = new ArrayList<String>() ;
      recievedUser.add(user.getUserName()) ;
      
      for (Object object : objGroupIds) {  
        String groupId = ((GroupImpl)object).getId() ;
        StringBuffer queryString = new StringBuffer("/jcr:root" + usersPath 
            + "//element(*,exo:contactGroup)[@exo:viewPermissionGroups='").append(groupId + "']") ;        
        Query query = qm.createQuery(queryString.toString(), Query.XPATH);
        QueryResult result = query.execute();
        NodeIterator nodes = result.getNodes() ;
        while (nodes.hasNext()) {
          Node address = nodes.nextNode() ;
          storage_.shareAddressBook(SessionProvider.createSystemProvider(), address.getProperty("exo:sharedUserId")
              .getString(), address.getProperty("exo:id").getString(),recievedUser) ;
        }
        
        /*// lookup shared contacts
        queryString = new StringBuffer("/jcr:root" + usersPath 
            + "//element(*,exo:contact)[@exo:viewPermissionGroups='").append(groupId + "']") ;        
        query = qm.createQuery(queryString.toString(), Query.XPATH);
        result = query.execute();
        nodes = result.getNodes() ;
        while (nodes.hasNext()) {
          Node contactNode = nodes.nextNode() ;
          String split = "/" ;
          String temp = contactNode.getPath().split(usersPath)[1] ;
          String userId = temp.split(split)[1] ;
          storage_.shareContact(SessionProvider.createSystemProvider(), userId,
              new String[] {contactNode.getProperty("exo:id").getString()}, recievedUser) ;
        }*/
      }

      
      
      Node userApp = nodeHierarchyCreator_.getUserApplicationNode(SessionProvider.createSystemProvider(), user.getUserName()) ;
      //reparePermissions(userApp, user.getUserName()) ;
      //reparePermissions(userApp.getNode("ContactApplication"), user.getUserName()) ;
      //reparePermissions(userApp.getNode("ContactApplication/contactGroup"), user.getUserName()) ;
      //reparePermissions(userApp.getNode("ContactApplication/contactGroup/" + group.getId()), user.getUserName()) ;
      userApp.getSession().save() ;   
      
      sysProvider.close();
  	} else {
     //System.out.println("===============> edit users"); 
    }
  }

  
  private void reparePermissions(Node node, String owner) throws Exception {
  	ExtendedNode extNode = (ExtendedNode)node ;
  	if (extNode.canAddMixin("exo:privilegeable")) extNode.addMixin("exo:privilegeable");
    String[] arrayPers = {PermissionType.READ, PermissionType.ADD_NODE, PermissionType.SET_PROPERTY, PermissionType.REMOVE} ;
    extNode.setPermission(owner, arrayPers) ;
    List<AccessControlEntry> permsList = extNode.getACL().getPermissionEntries() ;    
    for(AccessControlEntry accessControlEntry : permsList) {
      extNode.setPermission(accessControlEntry.getIdentity(), arrayPers) ;      
    } 
    extNode.removePermission("any") ;
    
  }
  public void preDelete(User user) throws Exception {
    
  }
}