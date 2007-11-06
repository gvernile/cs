/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.calendar.webui;

import java.io.InputStream;

import org.exoplatform.calendar.CalendarUtils;
import org.exoplatform.calendar.service.Attachment;
import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.CalendarService;
import org.exoplatform.calendar.webui.popup.UIPopupComponent;
import org.exoplatform.download.DownloadService;
import org.exoplatform.download.InputStreamDownloadResource;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

/**
 * Created by The eXo Platform SARL
 * Author : Hung Nguyen
 *          hung.nguyen@exoplatform.com
 * Aus 01, 2007 2:48:18 PM 
 */
@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    events = {
      @EventConfig(listeners = UIPreview.ViewActionListener.class),  
      @EventConfig(listeners = UICalendarView.EditActionListener.class),  
      @EventConfig(listeners = UICalendarView.DeleteActionListener.class)

    }
)
public class UIPreview extends UICalendarView implements UIPopupComponent {
  private CalendarEvent event_ = null ;
  private boolean isShowPopup_ = false ;

  public UIPreview() throws Exception {}

  public String getTemplate(){
    if(event_ == null) return "app:/templates/calendar/webui/UIDefaultPreview.gtmpl" ;
    if(event_.getEventType().equals(CalendarEvent.TYPE_EVENT))
      return "app:/templates/calendar/webui/UIEventPreview.gtmpl" ;
    return "app:/templates/calendar/webui/UITaskPreview.gtmpl" ;    
  }

  public CalendarEvent getEvent(){ return event_ ; }
  public void setEvent(CalendarEvent event) { event_ = event ; }

  public void refresh() throws Exception {
    /*if(event_ != null) {
      CalendarService calService = getApplicationComponent(CalendarService.class) ;
      String username = Util.getPortalRequestContext().getRemoteUser() ;
      CalendarEvent event = null ;
      if(CalendarUtils.PUBLIC_TYPE.equals(event_.getCalType())) {
        event = calService.getGroupEvent(event_.getCalendarId(), event_.getId()) ;
      } else {
        event = calService.getUserEvent(username, event_.getCalendarId(), event_.getId()) ;
      }
      if( event != null) event_ = event ;
    } */
  }

  public void activate() throws Exception {
    // TODO Auto-generated method stub

  }

  public void deActivate() throws Exception {
    // TODO Auto-generated method stub

  }

  public void setShowPopup(boolean isShow) {
    this.isShowPopup_ = isShow;
  }

  public boolean isShowPopup() {
    return isShowPopup_;
  }

  public String getImage(Attachment att) throws Exception {
    DownloadService dservice = getApplicationComponent(DownloadService.class) ;
    InputStreamDownloadResource dresource ;
    InputStream input = att.getInputStream() ;
    dresource = new InputStreamDownloadResource(input, att.getName()) ;
    dresource.setDownloadName(att.getName()) ;
    return dservice.getDownloadLink(dservice.addDownloadResource(dresource)) ;
  }

  public String getDownloadLink(Attachment att) throws Exception {
    DownloadService dservice = getApplicationComponent(DownloadService.class) ;
    InputStreamDownloadResource dresource ;
    InputStream input = att.getInputStream() ;
    dresource = new InputStreamDownloadResource(input, att.getName()) ;
    dresource.setDownloadName(att.getName()) ;
    return dservice.getDownloadLink(dservice.addDownloadResource(dresource)) ;
  }

  static  public class ViewActionListener extends EventListener<UIPreview> {
    public void execute(Event<UIPreview> event) throws Exception {
      UIPreview uiView = event.getSource() ;
      System.out.println("\n\n ViewActionListener");
      /* CalendarEvent eventCalendar = null ;
      String username = event.getRequestContext().getRemoteUser() ;
      String calendarId = event.getRequestContext().getRequestParameter(CALENDARID) ;
      String eventId = event.getRequestContext().getRequestParameter(OBJECTID) ;
      try {
        CalendarService calService = uiView.getApplicationComponent(CalendarService.class) ;
        eventCalendar = calService.getUserEvent(username, calendarId, eventId) ;
      } catch (Exception e){
        e.printStackTrace() ;
      }
       */
    }
  }
  /* static  public class EditActionListener extends EventListener<UIPreview> {
    public void execute(Event<UIPreview> event) throws Exception {
      System.out.println("EditEventActionListener");
      UIPreview uiView = event.getSource() ;
      UICalendarPortlet uiPortlet = uiView.getAncestorOfType(UICalendarPortlet.class) ;
      UIPopupAction uiPopupAction = uiPortlet.getChild(UIPopupAction.class) ;
      UIPopupContainer uiPopupContainer = uiPopupAction.activate(UIPopupContainer.class, 700) ;
      CalendarEvent eventCalendar = null ;
      String username = event.getRequestContext().getRemoteUser() ;
      String calendarId = event.getRequestContext().getRequestParameter(CALENDARID) ;
      String calType = event.getRequestContext().getRequestParameter(CALTYPE) ;
      String eventId = event.getRequestContext().getRequestParameter(OBJECTID) ;
      try {
        CalendarService calService = uiView.getApplicationComponent(CalendarService.class) ;
        eventCalendar = calService.getUserEvent(username, calendarId, eventId) ;
      } catch (Exception e){
        e.printStackTrace() ;
      }
      if(CalendarEvent.TYPE_EVENT.equals(eventCalendar.getEventType())) {
        uiPopupContainer.setId(UIPopupContainer.UIEVENTPOPUP) ;
        UIEventForm uiEventForm = uiPopupContainer.createUIComponent(UIEventForm.class, null, null) ;
        uiEventForm.initForm(uiPortlet.getCalendarSetting(), eventCalendar) ;
        if(calType.equals("0")) {
          uiEventForm.update(calType, null) ;
          uiEventForm.setSelectedCalendarId(calendarId) ;
        } else {
          List<SelectItemOption<String>> options = new ArrayList<SelectItemOption<String>>() ;
          //options.add(new SelectItemOption<String>(calendarName, calendarId)) ;
          uiEventForm.update(calType, options) ;
        }    
        uiPopupContainer.addChild(uiEventForm) ;
        event.getRequestContext().addUIComponentToUpdateByAjax(uiPopupAction) ;
        event.getRequestContext().addUIComponentToUpdateByAjax(uiView.getParent()) ;
      } else if(CalendarEvent.TYPE_TASK.equals(eventCalendar.getEventType())) {
        uiPopupContainer.setId(UIPopupContainer.UITASKPOPUP) ;
        UITaskForm uiTaskForm = uiPopupContainer.createUIComponent(UITaskForm.class, null, null) ;
        uiTaskForm.initForm(eventCalendar) ;
        uiPopupContainer.addChild(uiTaskForm) ;
        event.getRequestContext().addUIComponentToUpdateByAjax(uiPopupAction) ;
        event.getRequestContext().addUIComponentToUpdateByAjax(uiView.getParent()) ;
      } else {
        System.out.println("\n\n event type is not supported !");
      }
    }
  }*/
  /*static  public class DeleteActionListener extends EventListener<UIPreview> {
    public void execute(Event<UIPreview> event) throws Exception {
      UIPreview uiView = event.getSource() ;
      System.out.println("\n\n QuickDeleteEventActionListener");
      String eventId = event.getRequestContext().getRequestParameter(OBJECTID) ;
      String calendarId = event.getRequestContext().getRequestParameter(CALENDARID) ;
      UICalendarViewContainer uiContainer = uiView.getAncestorOfType(UICalendarViewContainer.class) ;
      UICalendarPortlet uiPortlet = uiView.getAncestorOfType(UICalendarPortlet.class) ;
      UIMiniCalendar uiMiniCalendar = uiPortlet.findFirstComponentOfType(UIMiniCalendar.class) ;
      try {
        CalendarService calService = uiView.getApplicationComponent(CalendarService.class) ;
        String username = event.getRequestContext().getRemoteUser() ;
        calService.removeUserEvent(username, calendarId, eventId) ;
        uiView.setEvent(null) ;
        uiMiniCalendar.refresh() ;
        uiContainer.refresh() ;
        event.getRequestContext().addUIComponentToUpdateByAjax(uiMiniCalendar) ;
        event.getRequestContext().addUIComponentToUpdateByAjax(uiContainer) ;
      } catch (Exception e) {
        e.printStackTrace() ;
      }
    }
  }*/
}
