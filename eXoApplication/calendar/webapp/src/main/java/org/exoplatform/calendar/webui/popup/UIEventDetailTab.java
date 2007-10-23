/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.calendar.webui.popup;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exoplatform.calendar.CalendarUtils;
import org.exoplatform.calendar.service.Attachment;
import org.exoplatform.calendar.service.Calendar;
import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.CalendarService;
import org.exoplatform.calendar.service.CalendarSetting;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormCheckBoxInput;
import org.exoplatform.webui.form.UIFormDateTimeInput;
import org.exoplatform.webui.form.UIFormInputInfo;
import org.exoplatform.webui.form.UIFormInputWithActions;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.UIFormTextAreaInput;

/**
 * Created by The eXo Platform SARL
 * Author : Pham Tuan
 *          tuan.pham@exoplatform.com
 * Aug 29, 2007  
 */

@ComponentConfig(
    template = "app:/templates/calendar/webui/UIPopup/UIEventDetailTab.gtmpl"
) 
public class UIEventDetailTab extends UIFormInputWithActions {

  final public static String FIELD_EVENT = "eventName".intern() ;
  final public static String FIELD_CALENDAR = "calendar".intern() ;
  final public static String FIELD_CATEGORY = "category".intern() ;
  final public static String FIELD_FROM = "from".intern() ;
  final public static String FIELD_TO = "to".intern() ;
  final public static String FIELD_FROM_TIME = "fromTime".intern() ;
  final public static String FIELD_TO_TIME = "toTime".intern() ;

  final public static String FIELD_CHECKALL = "allDay".intern() ;
  final public static String FIELD_REPEAT = "repeat".intern() ;
  final public static String FIELD_PLACE = "place".intern() ;
  
  final public static String FIELD_PRIORITY = "priority".intern() ; 
  final public static String FIELD_DESCRIPTION = "description".intern() ;

  final static public String FIELD_ATTACHMENTS = "attachments".intern() ;

  protected List<Attachment> attachments_ = new ArrayList<Attachment>() ;
  private Map<String, List<ActionData>> actionField_ ;
  private int timeInterval_  ;
  private String timeFormat_ ;
  
  public UIEventDetailTab(String arg0) throws Exception {
    super(arg0);
    setComponentConfig(getClass(), null) ;
    applySetting() ;
    actionField_ = new HashMap<String, List<ActionData>>() ;
    addUIFormInput(new UIFormStringInput(FIELD_EVENT, FIELD_EVENT, null)) ;
    addUIFormInput(new UIFormTextAreaInput(FIELD_DESCRIPTION, FIELD_DESCRIPTION, null)) ;
    addUIFormInput(new UIFormSelectBox(FIELD_CALENDAR, FIELD_CALENDAR, getCalendar())) ;
    addUIFormInput(new UIFormSelectBox(FIELD_CATEGORY, FIELD_CATEGORY, UIEventForm.getCategory())) ;
    
    ActionData addCategoryAction = new ActionData() ;
    addCategoryAction.setActionType(ActionData.TYPE_ICON) ;
    addCategoryAction.setActionName(UIEventForm.ACT_ADDCATEGORY) ;
    addCategoryAction.setActionListener(UIEventForm.ACT_ADDCATEGORY) ;
    List<ActionData> addCategoryActions = new ArrayList<ActionData>() ;
    addCategoryActions.add(addCategoryAction) ;
    setActionField(FIELD_CATEGORY, addCategoryActions) ;

    addUIFormInput(new UIFormInputInfo(FIELD_ATTACHMENTS, FIELD_ATTACHMENTS, null)) ;
    setActionField(FIELD_ATTACHMENTS, getUploadFileList()) ;

    addUIFormInput(new UIFormDateTimeInput(FIELD_FROM, FIELD_FROM, new Date(), false));
    addUIFormInput(new UIFormSelectBox(FIELD_FROM_TIME, FIELD_FROM_TIME, CalendarUtils.getTimesSelectBoxOptions(timeFormat_, timeInterval_)));
    addUIFormInput(new UIFormDateTimeInput(FIELD_TO, FIELD_TO, new Date(), false));
    addUIFormInput(new UIFormSelectBox(FIELD_TO_TIME, FIELD_TO_TIME,  CalendarUtils.getTimesSelectBoxOptions(timeFormat_, timeInterval_)));
    addUIFormInput(new UIFormCheckBoxInput<Boolean>(FIELD_CHECKALL, FIELD_CHECKALL, null));
    addUIFormInput(new UIFormStringInput(FIELD_PLACE, FIELD_PLACE, null));
    addUIFormInput(new UIFormSelectBox(FIELD_REPEAT, FIELD_REPEAT, getRepeater())) ;
    addUIFormInput(new UIFormSelectBox(FIELD_PRIORITY, FIELD_PRIORITY, getPriority())) ;
   
    ActionData addEmailAddress = new ActionData() ;
    addEmailAddress.setActionType(ActionData.TYPE_ICON) ;
    addEmailAddress.setActionName(UIEventForm.ACT_ADDEMAIL) ;
    addEmailAddress.setActionListener(UIEventForm.ACT_ADDEMAIL) ;
    
    List<ActionData> addMailActions = new ArrayList<ActionData>() ;
    addMailActions.add(addEmailAddress) ;
    
  }
  public void applySetting() throws Exception {
    CalendarService calService = getApplicationComponent(CalendarService.class) ;
    String username = Util.getPortalRequestContext().getRemoteUser() ;
    CalendarSetting calSetting = calService.getCalendarSetting(username) ;
    timeInterval_ = (int)calSetting.getTimeInterval() ;
    timeFormat_ = calSetting.getTimeFormat() ;
  }
  
  protected UIForm getParentFrom() {
    return (UIForm)getParent() ;
  }
  
  public List<ActionData> getUploadFileList() { 
    List<ActionData> uploadedFiles = new ArrayList<ActionData>() ;
    for(Attachment attachdata : attachments_) {
      ActionData fileUpload = new ActionData() ;
      fileUpload.setActionListener("") ;
      fileUpload.setActionType(ActionData.TYPE_ICON) ;
      fileUpload.setCssIconClass("AttachmentIcon ZipFileIcon") ;
      fileUpload.setActionName(attachdata.getName() + " (" + attachdata.getSize() + " Kb)" ) ;
      fileUpload.setShowLabel(true) ;
      uploadedFiles.add(fileUpload) ;
      ActionData removeAction = new ActionData() ;
      removeAction.setActionListener(UIEventForm.ACT_REMOVE) ;
      removeAction.setActionName(UIEventForm.ACT_REMOVE);
      removeAction.setActionParameter(attachdata.getId());
      removeAction.setActionType(ActionData.TYPE_LINK) ;
      removeAction.setBreakLine(true) ;
      uploadedFiles.add(removeAction) ;
    }
    return uploadedFiles ;
  }
  
  public void addToUploadFileList(Attachment attachfile) {
    attachments_.add(attachfile) ;
  }
  public void removeFromUploadFileList(Attachment attachfile) {
    attachments_.remove(attachfile);
  }  
  public void refreshUploadFileList() throws Exception {
    setActionField(FIELD_ATTACHMENTS, getUploadFileList()) ;
  }
  protected List<Attachment> getAttachments() { 
    return attachments_ ;
  }
  protected void setAttachments(List<Attachment> attachment) { 
    attachments_ = attachment ;
  }
  
  private List<SelectItemOption<String>> getCalendar() throws Exception {
    List<SelectItemOption<String>> options = new ArrayList<SelectItemOption<String>>() ;
    CalendarService calendarService = CalendarUtils.getCalendarService() ;
    String username = Util.getPortalRequestContext().getRemoteUser() ;
    List<Calendar> calendars = calendarService.getUserCalendars(username) ;
    for(Calendar c : calendars) {
      options.add(new SelectItemOption<String>(c.getName(), c.getId())) ;
    }
    return options ;
  }
  private List<SelectItemOption<String>> getPriority() throws Exception {
    List<SelectItemOption<String>> options = new ArrayList<SelectItemOption<String>>() ;
    options.add(new SelectItemOption<String>("high", "1")) ;
    options.add(new SelectItemOption<String>("normal", "2")) ;
    options.add(new SelectItemOption<String>("low", "3")) ;
    return options ;
  }
  private List<SelectItemOption<String>> getRepeater() {
    List<SelectItemOption<String>> options = new ArrayList<SelectItemOption<String>>() ;
    for(String s : CalendarEvent.REPEATTYPES) {
      options.add(new SelectItemOption<String>(s,s)) ;
    }
    return options ;
  }
  /*private List<SelectItemOption<String>> getReminder() {
    List<SelectItemOption<String>> options = new ArrayList<SelectItemOption<String>>() ;
    for(String rmdType : Reminder.REMINDER_TYPES) {
      options.add(new SelectItemOption<String>(rmdType, rmdType)) ;
    }
    return options ;
  }*/
  public void setActionField(String fieldName, List<ActionData> actions) throws Exception {
    actionField_.put(fieldName, actions) ;
  }
  public List<ActionData> getActionField(String fieldName) {return actionField_.get(fieldName) ;}
  @Override
  public void processRender(WebuiRequestContext arg0) throws Exception {
    super.processRender(arg0);
  }
  protected void setTimeInterval(int timeInterval_) {
    this.timeInterval_ = timeInterval_;
  }
  protected int getTimeInterval() {
    return timeInterval_;
  }
  protected void setTimeFormat(String timeFormat_) {
    this.timeFormat_ = timeFormat_;
  }
  protected String getTimeFormat() {
    return timeFormat_;
  }


}
