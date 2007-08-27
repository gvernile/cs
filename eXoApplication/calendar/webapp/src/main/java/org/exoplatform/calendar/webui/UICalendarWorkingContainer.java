/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.calendar.webui;

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;

/**
 * Created by The eXo Platform SARL
 * Author : Hung Nguyen
 *          hung.nguyen@exoplatform.com
 * Aus 01, 2007 2:48:18 PM 
 */

@ComponentConfig(
    //lifecycle = UIContainerLifecycle.class,  
    template =  "app:/templates/calendar/webui/UICalendarWorkingContainer.gtmpl"

)
public class UICalendarWorkingContainer extends UIContainer  {
  public UICalendarWorkingContainer() throws Exception {
    addChild(UICalendarContainer.class, null, null).setRendered(true) ;
    addChild(UICalendarViewContainer.class, null, null).setRendered(true) ;
  }
  
}
