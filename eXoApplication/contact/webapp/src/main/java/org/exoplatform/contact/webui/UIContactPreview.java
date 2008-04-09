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
package org.exoplatform.contact.webui;

import java.util.List;

import org.exoplatform.contact.ContactUtils;
import org.exoplatform.contact.service.Contact;
import org.exoplatform.contact.webui.popup.UIComposeForm;
import org.exoplatform.contact.webui.popup.UIPopupAction;
import org.exoplatform.download.DownloadService;
import org.exoplatform.mail.service.Account;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

/**
 * Created by The eXo Platform SARL
 * Author : Hung Nguyen
 *          hung.nguyen@exoplatform.com
 * Aus 01, 2007 2:48:18 PM 
 */

@ComponentConfig(
    template =  "app:/templates/contact/webui/UIContactPreview.gtmpl",
    events = {
        @EventConfig(listeners = UIContactPreview.SendEmailActionListener.class)
    }
)
public class UIContactPreview extends UIComponent  {
  private Contact contact_ = null ; 
  public UIContactPreview() throws Exception { }
  
  public void setContact(Contact c) { contact_ = c; }
  public Contact getContact() { return contact_; }

  public String getImageSource() throws Exception {
    return ContactUtils.getImageSource(contact_, getApplicationComponent(DownloadService.class)) ; 
  }

  static public class SendEmailActionListener extends EventListener<UIContactPreview> {
    public void execute(Event<UIContactPreview> event) throws Exception {
      UIContactPreview uiContactPreview = event.getSource() ;
      String email = event.getRequestContext().getRequestParameter(OBJECTID);
      if (!ContactUtils.isEmpty(email)) {
        UIContactPortlet contactPortlet = uiContactPreview.getAncestorOfType(UIContactPortlet.class) ;
        UIPopupAction popupAction = contactPortlet.getChild(UIPopupAction.class) ;
         
        List<Account> acc = ContactUtils.getAccounts() ;
        if (acc == null || acc.size() < 1) {
          UIApplication uiApp = uiContactPreview.getAncestorOfType(UIApplication.class) ;
          uiApp.addMessage(new ApplicationMessage("UIComposeForm.msg.invalidAcc", null,
              ApplicationMessage.WARNING)) ;
          return ;
        }
        UIComposeForm uiComposeForm = popupAction.activate(UIComposeForm.class, 850) ; 
        uiComposeForm.init(acc,email) ;
        event.getRequestContext().addUIComponentToUpdateByAjax(popupAction) ;  
        event.getRequestContext().addUIComponentToUpdateByAjax(uiContactPreview.getParent()) ;        
      }
    }
  }
}
