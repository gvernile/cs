/*
 * Copyright (C) 2003-2010 eXo Platform SAS.
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
package org.exoplatform.cs.ext.impl;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.contact.service.AddressBook;
import org.exoplatform.contact.service.ContactService;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.social.core.space.SpaceListenerPlugin;
import org.exoplatform.social.core.space.impl.SpaceServiceImpl;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 7, 2010  
 */
public class ContactDataInitialize extends SpaceListenerPlugin {

  private static final Log log = ExoLogger.getLogger(ContactDataInitialize.class);

  @Override
  public void applicationActivated(SpaceLifeCycleEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void applicationAdded(SpaceLifeCycleEvent event) {
    try {
      String firstMem = null;
      Space space = event.getSpace();

      /* --- start organization service ---*/
      PortalContainer manager = PortalContainer.getInstance() ;
      OrganizationService oService = (OrganizationService)ExoContainerContext
      .getCurrentContainer().getComponentInstanceOfType(OrganizationService.class);
      ((ComponentRequestLifecycle)oService).startRequest(manager);
      /* --- end --- */
      List<String> receivedUsers = new ArrayList<String>();
      for(User u : oService.getUserHandler().findUsersByGroup(space.getGroupId()).getAll())  {
        Membership m = oService.getMembershipHandler().findMembershipByUserGroupAndType(u.getUserName(), space.getGroupId(),SpaceServiceImpl.MANAGER);
        if(m != null && firstMem == null) {
          firstMem = u.getUserName();
        } else {
          receivedUsers.add(u.getUserName());
        }
      }
      /* --- stop organization service --- */
      ((ComponentRequestLifecycle)oService).endRequest(manager);
      /* --- end --- */

      ContactService contactService = (ContactService) PortalContainer.getComponent(ContactService.class);
      String addrBookId = "ContactGroup" + space.getId();
      AddressBook book = null;
      try {
        book = contactService.getPersonalAddressBook(firstMem, addrBookId);
      } catch (Exception e) {
        book = null;
      }

      if (book == null) {
        book = new AddressBook();
        book.setId(addrBookId);
        book.setName(space.getName() + " [sharing for Space]");
        book.setDescription("AddressBook for Social Space: " + space.getName());
        book.setEditPermissionGroups(new String[]{space.getGroupId()});
        book.setViewPermissionGroups(new String[] {space.getGroupId()});
        contactService.saveAddressBook(firstMem, book, true);
        contactService.shareAddressBook(firstMem, addrBookId, receivedUsers);
      }
    } catch (Exception e) {
      log.error("could initialize space adressbook to group: " + event.getSpace().getGroupId(), e);
    }
  }

  @Override
  public void applicationDeactivated(SpaceLifeCycleEvent event) {
  }

  @Override
  public void applicationRemoved(SpaceLifeCycleEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void grantedLead(SpaceLifeCycleEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void joined(SpaceLifeCycleEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void left(SpaceLifeCycleEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void revokedLead(SpaceLifeCycleEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void spaceCreated(SpaceLifeCycleEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void spaceRemoved(SpaceLifeCycleEvent event) {
    // TODO Auto-generated method stub

  }

}