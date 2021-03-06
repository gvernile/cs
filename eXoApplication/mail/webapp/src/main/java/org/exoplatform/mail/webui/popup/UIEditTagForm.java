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
package org.exoplatform.mail.webui.popup;

import java.util.List;

import javax.jcr.PathNotFoundException;

import org.exoplatform.cs.common.webui.UIPopupAction;
import org.exoplatform.cs.common.webui.UIPopupComponent;
import org.exoplatform.mail.MailUtils;
import org.exoplatform.mail.service.MailService;
import org.exoplatform.mail.service.Tag;
import org.exoplatform.mail.webui.UIMailPortlet;
import org.exoplatform.mail.webui.UIMessageArea;
import org.exoplatform.mail.webui.UIMessageList;
import org.exoplatform.mail.webui.UISelectAccount;
import org.exoplatform.mail.webui.UITagContainer;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.UIFormTextAreaInput;
import org.exoplatform.webui.form.ext.UIFormColorPicker;
import org.exoplatform.webui.form.validator.MandatoryValidator;
import org.exoplatform.webui.form.validator.SpecialCharacterValidator;


/**
 * Created by The eXo Platform SARL
 * Author : Phung Nam <phunghainam@gmail.com>
 * Aus 01, 2007 2:48:18 PM 
 */
@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template = "system:/groovy/webui/form/UIForm.gtmpl",
    events = {
      @EventConfig(listeners = UIEditTagForm.SaveActionListener.class), 
      @EventConfig(listeners = UIEditTagForm.CancelActionListener.class, phase = Phase.DECODE)
    }
)
public class UIEditTagForm extends UIForm implements UIPopupComponent {

  final public static String NEW_TAG_NAME = "newTagName" ;
  final public static String DESCRIPTION = "description" ;
  final public static String COLOR = "color" ;
  
  private String tagId;
  
  public UIEditTagForm() throws Exception {       
    addUIFormInput(new UIFormStringInput(NEW_TAG_NAME, NEW_TAG_NAME, null).addValidator(MandatoryValidator.class).addValidator(SpecialCharacterValidator.class)) ;
    addUIFormInput(new UIFormColorPicker(COLOR, COLOR)) ;
    addUIFormInput(new UIFormTextAreaInput(DESCRIPTION,DESCRIPTION,null)) ;    
  }
  
  public String getSelectedColor() {
    return getChild(UIFormColorPicker.class).getValue() ;
  }
  public void setSelectedColor(String value) {
    getChild(UIFormColorPicker.class).setValue(value) ;
  }
  public String getTagId() throws Exception { return tagId; }
  
  public void setTag(String tagId) throws Exception {
    this.tagId = tagId;
    
    MailService mailSrv = getApplicationComponent(MailService.class);
    String username = MailUtils.getCurrentUser();
    String accountId = getAncestorOfType(UIMailPortlet.class).findFirstComponentOfType(UISelectAccount.class).getSelectedValue();
    if(MailUtils.isDelegated(accountId)) {
      username = mailSrv.getDelegatedAccount(username, accountId).getDelegateFrom();
    }
    List<Tag> tagList= mailSrv.getTags(username, accountId);
    
    if (tagList.isEmpty()) return;   
    
    for (Tag tag : tagList) {      
      if (tag.getId().equals(tagId)){
        getUIStringInput(NEW_TAG_NAME).setValue(tag.getName()); 
        getUIFormTextAreaInput(DESCRIPTION).setValue(tag.getDescription());
        getChild(UIFormColorPicker.class).setValue(tag.getColor()) ;       
      }
    }
  }
  
  public void activate() throws Exception {}
  public void deActivate() throws Exception{}
 
  static  public class SaveActionListener extends EventListener<UIEditTagForm> {
    public void execute(Event<UIEditTagForm> event) throws Exception {
      UIEditTagForm editTagForm  = event.getSource() ;
      UIMailPortlet uiPortlet = editTagForm.getAncestorOfType(UIMailPortlet.class);
      MailService mailService = editTagForm.getApplicationComponent(MailService.class);

      String username = uiPortlet.getCurrentUser() ;
      String accountId =  uiPortlet.findFirstComponentOfType(UISelectAccount.class).getSelectedValue() ;
      String tagId = editTagForm.getTagId();
      String newTagName = editTagForm.getUIStringInput(NEW_TAG_NAME).getValue().trim() ;
      
      if(MailUtils.isDelegated(accountId)) {
        username = mailService.getDelegatedAccount(username, accountId).getDelegateFrom();
      }
//    CS-3009
      newTagName = MailUtils.reduceSpace(newTagName) ;
      /*if (!MailUtils.isNameValid(newTagName, MailUtils.SIMPLECHARACTER)) {
        UIApplication uiApp = editTagForm.getAncestorOfType(UIApplication.class) ;
        uiApp.addMessage(new ApplicationMessage("UIEditTagForm.msg.tagname-invalid", MailUtils.SIMPLECHARACTER, ApplicationMessage.WARNING) ) ;
        event.getRequestContext().addUIComponentToUpdateByAjax(uiApp.getUIPopupMessages()) ;
        return;
      }
      */
      String description = editTagForm.getUIFormTextAreaInput(DESCRIPTION).getValue() ;
      String color = editTagForm.getSelectedColor(); 
      UIApplication uiApp = editTagForm.getAncestorOfType(UIApplication.class) ;
      List<Tag> tagList = null ;
      try {
        tagList = mailService.getTags(username, accountId);
      } catch (PathNotFoundException e) {
        uiPortlet.findFirstComponentOfType(UIMessageList.class).setMessagePageList(null) ;
        uiPortlet.findFirstComponentOfType(UISelectAccount.class).refreshItems();
        event.getRequestContext().addUIComponentToUpdateByAjax(uiPortlet); 
        
        uiApp.addMessage(new ApplicationMessage("UIMessageList.msg.deleted_account", null, ApplicationMessage.WARNING)) ;
        event.getRequestContext().addUIComponentToUpdateByAjax(uiApp.getUIPopupMessages()) ;
        return ;
      }
      for (Tag tag : tagList) {
        if(tag.getName().equals(newTagName) && !tag.getId().equals(tagId)) {
          uiApp.addMessage(new ApplicationMessage("UIEditTagForm.msg.tag-already-exists", new Object[]{newTagName})) ;
          event.getRequestContext().addUIComponentToUpdateByAjax(uiApp.getUIPopupMessages()) ;
          return ;
        }
      }
      
      if (tagId != null) {
        try {      
          editTagForm.setTag(tagId);        
          Tag tag =  mailService.getTag(username, accountId, tagId);
          if (tag != null) {
            tag.setName(newTagName);
            tag.setColor(color);
            tag.setDescription(description);
            mailService.updateTag(username, accountId, tag);
          }
        } catch (Exception e){
          uiApp.addMessage(new ApplicationMessage("UIRenameTagForm.msg.error-rename-tag", null)) ;
          e.printStackTrace() ;
        }
        UIMessageList uiMessageList = uiPortlet.findFirstComponentOfType(UIMessageList.class);
        uiMessageList.updateList();
        event.getRequestContext().addUIComponentToUpdateByAjax(uiMessageList.getAncestorOfType(UIMessageArea.class)) ;
      } else {
        Tag newTag = new Tag() ;
        newTag.setName(newTagName);
        newTag.setColor(color);
        newTag.setDescription(description);
        mailService.addTag(username, accountId, newTag);
        event.getRequestContext().addUIComponentToUpdateByAjax(uiPortlet.findFirstComponentOfType(UIMessageArea.class)) ;
      }
      event.getRequestContext().addUIComponentToUpdateByAjax(uiPortlet.findFirstComponentOfType(UITagContainer.class)) ;
      editTagForm.getAncestorOfType(UIPopupAction.class).deActivate() ;
      event.getRequestContext().addUIComponentToUpdateByAjax(editTagForm.getAncestorOfType(UIPopupAction.class)) ;
    }
  }
  
  static  public class CancelActionListener extends EventListener<UIEditTagForm> {
    public void execute(Event<UIEditTagForm> event) throws Exception {
      UIEditTagForm uiForm = event.getSource() ;
      uiForm.getAncestorOfType(UIPopupAction.class).deActivate() ;
      event.getRequestContext().addUIComponentToUpdateByAjax(uiForm.getAncestorOfType(UIPopupAction.class)) ;
    }
  }
}
