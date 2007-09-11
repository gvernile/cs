/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.contact.webui.popup;

import org.exoplatform.contact.service.Contact;
import org.exoplatform.contact.service.ContactService;
import org.exoplatform.contact.webui.UIContactPortlet;
import org.exoplatform.contact.webui.UIContactPreview;
import org.exoplatform.contact.webui.UIContacts;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.impl.GroupImpl;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIFormCheckBoxInput;
import org.exoplatform.webui.form.UIFormInputWithActions;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.UIFormTabPane;
import org.exoplatform.webui.form.UIFormTextAreaInput;

/**
 * Created by The eXo Platform SARL
 * Author : Hung Nguyen
 *          hung.nguyen@exoplatform.com
 * Aus 01, 2007 2:48:18 PM 
 */
@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template = "system:/groovy/webui/form/UIFormTabPane.gtmpl",
    events = {
      @EventConfig(listeners = UIContactForm.AddCategoryActionListener.class),
      @EventConfig(listeners = UIContactForm.SaveActionListener.class),      
      @EventConfig(listeners = UIContactForm.CancelActionListener.class)
    }
)
public class UIContactForm extends UIFormTabPane implements UIPopupComponent {
  public static String contactId_ ;
  public static boolean isNew_ = true;
  public static final String FIELD_WORKADDRESS_INPUT = "workAddress";
  public static final String FIELD_WORKCITY_INPUT = "workCity";
  public static final String FIELD_WORKSTATE_INPUT = "workState_province";
  public static final String FIELD_WORKPOSTALCODE_INPUT = "workPostalCode";
  public static final String FIELD_WORKCOUNTRY_INPUT = "workCountry";
  public static final String FIELD_WORKPHONE1_INPUT = "workPhone1";
  public static final String FIELD_WORKPHONE2_INPUT = "workPhone2";
  public static final String FIELD_WORKFAX_INPUT = "workFax";
  public static final String FIELD_WORKMOBILEPHONE_INPUT = "mobilePhone";
  public static final String FIELD_WORKWEBPAGE_INPUT = "webPage";
  
  public static final String FIELD_EXOCHAT_INPUT = "exoChat";
  public static final String FIELD_GOOGLE_INPUT = "google";
  public static final String FIELD_MSN_INPUT = "msn";
  public static final String FIELD_AOLAIM_INPUT = "aolAim";
  public static final String FIELD_YAHOO_INPUT = "yahoo";
  public static final String FIELD_ICR_INPUT = "icr";
  public static final String FIELD_SKYPE_INPUT = "skype";
  public static final String FIELD_ICQ_INPUT = "icq";
  
  public static final String FIELD_HOMEADDRESS_INPUT = "homeAddress";
  public static final String FIELD_HOMECITY_INPUT = "homeCity";
  public static final String FIELD_HOMESTATE_INPUT = "homeState_province";
  public static final String FIELD_HOMEPOSTALCODE_INPUT = "homePostalCode";
  public static final String FIELD_HOMECOUNTRY_INPUT = "homeCountry";
  public static final String FIELD_HOMEPHONE1_INPUT = "homePhone1";
  public static final String FIELD_HOMEPHONE2_INPUT = "homePhone2";
  public static final String FIELD_HOMEFAX_INPUT = "homeFax";
  public static final String FIELD_PERSONALSITE_INPUT = "personalSite";
  public static final String FIELD_NOTE_INPUT = "note";
  
  public static String[] FIELD_SHAREDCONTACT_BOX = null;
  public static String FIELD_ISPUBLIC_BOX = "isPublic";
  public static String FIELD_EDITPERMISSION_INPUT = "editPermission";  
  
  public static final String INPUT_PROFILETAB = "protileTab" ;
  public static final String INPUT_IMCONTACTTAB = "imContactTab" ;
  public static final String INPUT_HOMETAB = "homeTab" ;
  public static final String INPUT_WORKTAB = "workTab" ;
  public static final String INPUT_NODETAB = "noteTab" ;
  public static final String INPUT_SHARETAB =  "shareContactTab" ;
 
  
  public UIContactForm() throws Exception {
    super("UIContactForm", false);
    UIProfileInputSet ProfileTab = new UIProfileInputSet(INPUT_PROFILETAB) ;
    
    UIFormInputWithActions IMContactTab = new UIFormInputWithActions(INPUT_IMCONTACTTAB) ;
    UIFormInputWithActions HomeTab = new UIFormInputWithActions(INPUT_HOMETAB) ;
    UIFormInputWithActions WorkTab = new UIFormInputWithActions(INPUT_WORKTAB) ;
    UIFormInputWithActions NoteTab = new UIFormInputWithActions(INPUT_NODETAB) ;
    UIFormInputWithActions ShareTab = new UIFormInputWithActions(INPUT_SHARETAB) ;

    WorkTab.addUIFormInput(new UIFormStringInput(FIELD_WORKADDRESS_INPUT, FIELD_WORKADDRESS_INPUT, null));
    WorkTab.addUIFormInput(new UIFormStringInput(FIELD_WORKCITY_INPUT, FIELD_WORKCITY_INPUT, null));
    WorkTab.addUIFormInput(new UIFormStringInput(FIELD_WORKSTATE_INPUT, FIELD_WORKSTATE_INPUT, null));
    WorkTab.addUIFormInput(new UIFormStringInput(FIELD_WORKPOSTALCODE_INPUT, FIELD_WORKPOSTALCODE_INPUT, null));
    WorkTab.addUIFormInput(new UIFormStringInput(FIELD_WORKCOUNTRY_INPUT, FIELD_WORKCOUNTRY_INPUT, null));
    WorkTab.addUIFormInput(new UIFormStringInput(FIELD_WORKPHONE1_INPUT, FIELD_WORKPHONE1_INPUT, null));
    WorkTab.addUIFormInput(new UIFormStringInput(FIELD_WORKPHONE2_INPUT, FIELD_WORKPHONE2_INPUT, null));
    WorkTab.addUIFormInput(new UIFormStringInput(FIELD_WORKFAX_INPUT, FIELD_WORKFAX_INPUT, null));
    WorkTab.addUIFormInput(new UIFormStringInput(FIELD_WORKMOBILEPHONE_INPUT, FIELD_WORKMOBILEPHONE_INPUT, null));
    WorkTab.addUIFormInput(new UIFormStringInput(FIELD_WORKWEBPAGE_INPUT, FIELD_WORKWEBPAGE_INPUT, null));

    IMContactTab.addUIFormInput(new UIFormStringInput(FIELD_EXOCHAT_INPUT, FIELD_EXOCHAT_INPUT, null));
    IMContactTab.addUIFormInput(new UIFormStringInput(FIELD_GOOGLE_INPUT, FIELD_GOOGLE_INPUT, null));
    IMContactTab.addUIFormInput(new UIFormStringInput(FIELD_MSN_INPUT, FIELD_MSN_INPUT, null));
    IMContactTab.addUIFormInput(new UIFormStringInput(FIELD_AOLAIM_INPUT, FIELD_AOLAIM_INPUT, null));
    IMContactTab.addUIFormInput(new UIFormStringInput(FIELD_YAHOO_INPUT, FIELD_YAHOO_INPUT, null));
    IMContactTab.addUIFormInput(new UIFormStringInput(FIELD_ICR_INPUT, FIELD_ICR_INPUT, null));
    IMContactTab.addUIFormInput(new UIFormStringInput(FIELD_SKYPE_INPUT, FIELD_SKYPE_INPUT, null));
    IMContactTab.addUIFormInput(new UIFormStringInput(FIELD_ICQ_INPUT, FIELD_ICQ_INPUT, null));

    HomeTab.addUIFormInput(new UIFormStringInput(FIELD_HOMEADDRESS_INPUT, FIELD_HOMEADDRESS_INPUT, null));
    HomeTab.addUIFormInput(new UIFormStringInput(FIELD_HOMECITY_INPUT, FIELD_HOMECITY_INPUT, null));
    HomeTab.addUIFormInput(new UIFormStringInput(FIELD_HOMESTATE_INPUT, FIELD_HOMESTATE_INPUT, null));
    HomeTab.addUIFormInput(new UIFormStringInput(FIELD_HOMEPOSTALCODE_INPUT, FIELD_HOMEPOSTALCODE_INPUT, null));
    HomeTab.addUIFormInput(new UIFormStringInput(FIELD_HOMECOUNTRY_INPUT, FIELD_HOMECOUNTRY_INPUT, null));
    HomeTab.addUIFormInput(new UIFormStringInput(FIELD_HOMEPHONE1_INPUT, FIELD_HOMEPHONE1_INPUT, null));
    HomeTab.addUIFormInput(new UIFormStringInput(FIELD_HOMEPHONE2_INPUT, FIELD_HOMEPHONE2_INPUT, null));
    HomeTab.addUIFormInput(new UIFormStringInput(FIELD_HOMEFAX_INPUT, FIELD_HOMEFAX_INPUT, null));
    HomeTab.addUIFormInput(new UIFormStringInput(FIELD_PERSONALSITE_INPUT, FIELD_PERSONALSITE_INPUT, null));
    
    NoteTab.addUIFormInput(new UIFormTextAreaInput(FIELD_NOTE_INPUT, FIELD_NOTE_INPUT, null));

    ShareTab.addUIFormInput(new UIFormCheckBoxInput<Boolean>(FIELD_ISPUBLIC_BOX, FIELD_ISPUBLIC_BOX, false));
    OrganizationService organizationService = (OrganizationService)PortalContainer.getComponent(OrganizationService.class) ;
    Object[] groups = organizationService.getGroupHandler().getAllGroups().toArray() ;
    getChildren().clear() ;
    FIELD_SHAREDCONTACT_BOX = new String[groups.length];
    for(int i = 0; i < groups.length; i ++) {
      FIELD_SHAREDCONTACT_BOX[i] = ((GroupImpl)groups[i]).getId() ;
      ShareTab.addUIFormInput(new UIFormCheckBoxInput<Boolean>(FIELD_SHAREDCONTACT_BOX[i], FIELD_SHAREDCONTACT_BOX[i], false));
    }
    ShareTab.addUIFormInput(new UIFormStringInput(FIELD_EDITPERMISSION_INPUT, FIELD_EDITPERMISSION_INPUT, null));
    
    addUIFormInput(ProfileTab) ;
    addUIFormInput(WorkTab) ;
    addUIFormInput(IMContactTab) ;    
    addUIFormInput(HomeTab) ;
    addUIFormInput(NoteTab) ;
    addUIFormInput(ShareTab) ;
    setRenderedChild(UIProfileInputSet.class) ;  
  }
  
  public String[] getActions() { return new String[] {"Save", "Cancel"} ; }
  public void activate() throws Exception {}
  public void deActivate() throws Exception {}
  
  public void setValues(String contactId) throws Exception {
    contactId_ = contactId ;
    ContactService contactService = getApplicationComponent(ContactService.class);
    String username = Util.getPortalRequestContext().getRemoteUser() ;
    Contact contact = contactService.getContact(username, contactId);
    if(contact == null) {
      contact = contactService.getSharedContact(contactId);
      getUIFormCheckBoxInput(FIELD_ISPUBLIC_BOX).setChecked(true);
      String[] categories = contact.getCategories();
      for (String category : categories) getUIFormCheckBoxInput(category).setChecked(true) ;
      String[] editPermission = contact.getEditPermission();
      StringBuffer editPermissionBuffer = new StringBuffer("");
      if (editPermission.length > 0) editPermissionBuffer.append(editPermission[0]);
      for (int i = 1; i < editPermission.length; i ++) editPermissionBuffer.append("," + editPermission[i]);
      getUIStringInput(FIELD_EDITPERMISSION_INPUT).setValue(editPermissionBuffer.toString());
    }    
    getUIFormCheckBoxInput(FIELD_ISPUBLIC_BOX).setEnable(false) ;
    for (String group : FIELD_SHAREDCONTACT_BOX) getUIFormCheckBoxInput(group).setEnable(false) ;
    getUIStringInput(FIELD_EDITPERMISSION_INPUT).setEditable(false) ;
    
    UIProfileInputSet profileTab = getChildById(INPUT_PROFILETAB) ;
    profileTab.setFieldFullNameValue(contact.getFullName());
    profileTab.setFieldFirstNameValue(contact.getFirstName());
    profileTab.setFieldMiddleNameValue(contact.getMiddleName());
    profileTab.setFieldLastNameValue(contact.getLastName());
    profileTab.setFieldNickNameValue(contact.getNickName());
    profileTab.setFieldGenderValue(contact.getGender());
    profileTab.setFieldBirthdayValue(contact.getBirthday());
    profileTab.setFieldJobNameValue(contact.getJobTitle());
    profileTab.setFieldEmailValue(contact.getEmailAddress());
    
    getUIStringInput(FIELD_WORKADDRESS_INPUT).setValue(contact.getWorkAddress());
    getUIStringInput(FIELD_WORKCITY_INPUT).setValue(contact.getWorkCity());
    getUIStringInput(FIELD_WORKSTATE_INPUT).setValue(contact.getWorkStateProvince());
    getUIStringInput(FIELD_WORKPOSTALCODE_INPUT).setValue(contact.getWorkPostalCode());
    getUIStringInput(FIELD_WORKCOUNTRY_INPUT).setValue(contact.getWorkCountry());
    getUIStringInput(FIELD_WORKPHONE1_INPUT).setValue(contact.getWorkPhone1());
    getUIStringInput(FIELD_WORKPHONE2_INPUT).setValue(contact.getWorkPhone2());
    getUIStringInput(FIELD_WORKFAX_INPUT).setValue(contact.getWorkFax());
    getUIStringInput(FIELD_WORKMOBILEPHONE_INPUT).setValue(contact.getMobilePhone());
    getUIStringInput(FIELD_WORKWEBPAGE_INPUT).setValue(contact.getWebPage());
    
    getUIStringInput(FIELD_EXOCHAT_INPUT).setValue(contact.getExoId());
    getUIStringInput(FIELD_GOOGLE_INPUT).setValue(contact.getGoogleId());
    getUIStringInput(FIELD_MSN_INPUT).setValue(contact.getMsnId());
    getUIStringInput(FIELD_AOLAIM_INPUT).setValue(contact.getAolId());
    getUIStringInput(FIELD_YAHOO_INPUT).setValue(contact.getYahooId());
    getUIStringInput(FIELD_ICR_INPUT).setValue(contact.getIcrId());
    getUIStringInput(FIELD_SKYPE_INPUT).setValue(contact.getSkypeId());
    getUIStringInput(FIELD_ICQ_INPUT).setValue(contact.getIcqId());
    
    getUIStringInput(FIELD_HOMEADDRESS_INPUT).setValue(contact.getHomeAddress());
    getUIStringInput(FIELD_HOMECITY_INPUT).setValue(contact.getHomeCity());
    getUIStringInput(FIELD_HOMESTATE_INPUT).setValue(contact.getHomeState_province());
    getUIStringInput(FIELD_HOMEPOSTALCODE_INPUT).setValue(contact.getHomePostalCode());
    getUIStringInput(FIELD_HOMECOUNTRY_INPUT).setValue(contact.getHomeCountry());
    getUIStringInput(FIELD_HOMEPHONE1_INPUT).setValue(contact.getHomePhone1());
    getUIStringInput(FIELD_HOMEPHONE2_INPUT).setValue(contact.getHomePhone2());
    getUIStringInput(FIELD_HOMEFAX_INPUT).setValue(contact.getHomeFax());
    getUIStringInput(FIELD_PERSONALSITE_INPUT).setValue(contact.getPersonalSite());
    getUIStringInput(FIELD_NOTE_INPUT).setValue(contact.getNote());
  }
  
  static  public class SaveActionListener extends EventListener<UIContactForm> {
    public void execute(Event<UIContactForm> event) throws Exception {
      UIContactForm uiForm = event.getSource() ;
      ContactService contactService = uiForm.getApplicationComponent(ContactService.class);  
      String username = Util.getPortalRequestContext().getRemoteUser() ;
      Contact contact ;
      if (isNew_) contact = new Contact() ;
      else {
        contact = contactService.getContact(username, contactId_);
        if (contact == null) contact = contactService.getSharedContact(contactId_);
      }
      UIProfileInputSet profileTab = uiForm.getChildById(INPUT_PROFILETAB) ;
      if (profileTab.getFieldFullNameValue() == null || profileTab.getFieldFullNameValue().trim().length() == 0) {
        UIApplication uiApp = uiForm.getAncestorOfType(UIApplication.class) ;
        uiApp.addMessage(new ApplicationMessage("UIContactForm.msg.fullname-required", null)) ;
        event.getRequestContext().addUIComponentToUpdateByAjax(uiApp.getUIPopupMessages()) ;
        return ; 
      } 
      contact.setFullName(profileTab.getFieldFullNameValue());
      contact.setFirstName(profileTab.getFieldFirstNameValue());
      contact.setMiddleName(profileTab.getFieldMiddleNameValue());
      contact.setLastName(profileTab.getFieldLastNameValue());
      contact.setNickName(profileTab.getFieldNickNameValue());
      contact.setGender(profileTab.getFieldGenderValue()) ;
      contact.setBirthday(profileTab.getFieldBirthdayValue()) ;
      contact.setJobTitle(profileTab.getFieldJobNameValue());
      contact.setEmailAddress(profileTab.getFieldEmailValue());
      
      contact.setWorkAddress(uiForm.getUIStringInput(FIELD_WORKADDRESS_INPUT).getValue());
      contact.setWorkCity(uiForm.getUIStringInput(FIELD_WORKCITY_INPUT).getValue());
      contact.setWorkStateProvince(uiForm.getUIStringInput(FIELD_WORKSTATE_INPUT).getValue());
      contact.setWorkPostalCode(uiForm.getUIStringInput(FIELD_WORKPOSTALCODE_INPUT).getValue());
      contact.setWorkCountry(uiForm.getUIStringInput(FIELD_WORKCOUNTRY_INPUT).getValue());
      contact.setWorkPhone1(uiForm.getUIStringInput(FIELD_WORKPHONE1_INPUT).getValue());
      contact.setWorkPhone2(uiForm.getUIStringInput(FIELD_WORKPHONE2_INPUT).getValue());
      contact.setWorkFax(uiForm.getUIStringInput(FIELD_WORKFAX_INPUT).getValue());
      contact.setMobilePhone(uiForm.getUIStringInput(FIELD_WORKMOBILEPHONE_INPUT).getValue());
      contact.setWebPage(uiForm.getUIStringInput(FIELD_WORKWEBPAGE_INPUT).getValue());
    
      contact.setExoId(uiForm.getUIStringInput(FIELD_EXOCHAT_INPUT).getValue());
      contact.setGoogleId(uiForm.getUIStringInput(FIELD_GOOGLE_INPUT).getValue());
      contact.setMsnId(uiForm.getUIStringInput(FIELD_MSN_INPUT).getValue());
      contact.setAolId(uiForm.getUIStringInput(FIELD_AOLAIM_INPUT).getValue());
      contact.setYahooId(uiForm.getUIStringInput(FIELD_YAHOO_INPUT).getValue());
      contact.setIcrId(uiForm.getUIStringInput(FIELD_ICR_INPUT).getValue() );
      contact.setSkypeId(uiForm.getUIStringInput(FIELD_SKYPE_INPUT).getValue());
      contact.setIcqId(uiForm.getUIStringInput(FIELD_ICQ_INPUT).getValue());
      
      contact.setHomeAddress(uiForm.getUIStringInput(FIELD_HOMEADDRESS_INPUT).getValue());
      contact.setHomeCity(uiForm.getUIStringInput(FIELD_HOMECITY_INPUT).getValue());
      contact.setHomeState_province(uiForm.getUIStringInput(FIELD_HOMESTATE_INPUT).getValue());
      contact.setHomePostalCode(uiForm.getUIStringInput(FIELD_HOMEPOSTALCODE_INPUT).getValue());
      contact.setHomeCountry(uiForm.getUIStringInput(FIELD_HOMECOUNTRY_INPUT).getValue());
      contact.setHomePhone1(uiForm.getUIStringInput(FIELD_HOMEPHONE1_INPUT).getValue() );
      contact.setHomePhone2(uiForm.getUIStringInput(FIELD_HOMEPHONE2_INPUT).getValue());
      contact.setHomeFax(uiForm.getUIStringInput(FIELD_HOMEFAX_INPUT).getValue());
      contact.setPersonalSite(uiForm.getUIStringInput(FIELD_PERSONALSITE_INPUT).getValue());
      contact.setNote(uiForm.getUIFormTextAreaInput(FIELD_NOTE_INPUT).getValue());

      String categoryId = "";
      if (uiForm.getUIFormCheckBoxInput(FIELD_ISPUBLIC_BOX).isChecked()) {
        StringBuffer sharedGroups = new StringBuffer("");
        for (int i = 0; i < FIELD_SHAREDCONTACT_BOX.length; i ++) {
          if (uiForm.getUIFormCheckBoxInput(FIELD_SHAREDCONTACT_BOX[i]).isChecked())
            sharedGroups.append(FIELD_SHAREDCONTACT_BOX[i] + ",");
        }
        if (sharedGroups.toString().equals("")) {
          UIApplication uiApp = uiForm.getAncestorOfType(UIApplication.class) ;
          uiApp.addMessage(new ApplicationMessage("UIContactForm.msg.selectSharedGroups-required", null)) ;
          event.getRequestContext().addUIComponentToUpdateByAjax(uiApp.getUIPopupMessages()) ;
          return ; 
        }   
        if (uiForm.getUIStringInput(FIELD_EDITPERMISSION_INPUT).getValue() != null)
          contact.setEditPermission(uiForm.getUIStringInput(FIELD_EDITPERMISSION_INPUT).getValue().split(","));
        contact.setCategories(sharedGroups.toString().split(","));
        contactService.saveSharedContact(contact, isNew_);  
      } else {
        UIPopupContainer popupContainer = uiForm.getAncestorOfType(UIPopupContainer.class) ;
        UICategorySelect uiCategorySelect = popupContainer.getChild(UICategorySelect.class); 
        categoryId = uiCategorySelect.getSelectedCategory();
        contact.setCategories(new String[] { categoryId });
        contactService.saveContact(username, contact, isNew_);
      }
      UIContactPortlet uiContactPortlet = uiForm.getAncestorOfType(UIContactPortlet.class) ;
      UIContacts uicontacts = uiContactPortlet.findFirstComponentOfType(UIContacts.class) ;
      if (isNew_) {
        if (uicontacts.getGroupId().equals(categoryId))
          uicontacts.updateContact(contact, isNew_) ;
      } else 
          uicontacts.updateContact(contact, isNew_) ;
      UIContactPreview uiContactPreview = uiContactPortlet.findFirstComponentOfType(UIContactPreview.class) ;
      if (contact.getId().equals(uiContactPreview.getContact().getId())) uiContactPreview.setContact(contact) ;
      uiContactPortlet.cancelAction() ;
      event.getRequestContext().addUIComponentToUpdateByAjax(uiContactPortlet) ;
    }
  }
  
  static  public class AddCategoryActionListener extends EventListener<UIContactForm> {
    public void execute(Event<UIContactForm> event) throws Exception {
      UIContactForm uiForm = event.getSource() ;
      UIPopupContainer popupContainer = uiForm.getAncestorOfType(UIPopupContainer.class) ;
      UIPopupAction popupAction = popupContainer.getChild(UIPopupAction.class) ;
      popupAction.activate(UICategoryForm.class, 600) ;
      event.getRequestContext().addUIComponentToUpdateByAjax(popupAction) ;      
    }
  }
  
  static  public class CancelActionListener extends EventListener<UIContactForm> {
    public void execute(Event<UIContactForm> event) throws Exception {
      UIContactForm uiForm = event.getSource() ;
      UIContactPortlet contactPortlet = uiForm.getAncestorOfType(UIContactPortlet.class) ;
      contactPortlet.cancelAction() ;      
    }
  }  
}
