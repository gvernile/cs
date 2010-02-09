/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.exoplatform.calendar.service;

import java.net.URL;
import java.util.List;

import org.exoplatform.calendar.service.impl.CalendarServiceImpl;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * May 14, 2009  
 */
public class AutoGeneratePeriodJobImp  implements Job {
  

  public void execute(JobExecutionContext jContext) throws JobExecutionException {
    PortalContainer container = Utils.getPortalContainer(jContext);
    if(container == null) return;
    ExoContainer oldContainer = ExoContainerContext.getCurrentContainer();
    ExoContainerContext.setCurrentContainer(container);
    JobDataMap jdatamap = jContext.getJobDetail().getJobDataMap();    
    String numberLimited = jdatamap.getString("event_number") ;
    CalendarService calSvr = (CalendarService) container.getComponentInstanceOfType(CalendarService.class) ;
    try {
      if (calSvr == null) {
        System.out.println("\n\n Can not get calendar service");
        return ;
      }
    	List<FeedData>  data  = calSvr.getFeeds(null) ;
      for(FeedData d : data) {
        URL feedUrl = new URL(d.getUrl());
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedUrl)); 
        if (feed.getEntries() == null || feed.getEntries().size() == 0) continue ;
        SyndEntry entry = (SyndEntry)feed.getEntries().get(0);
        String calId = entry.getLink().substring(entry.getLink().lastIndexOf("/")+1) ;
        calSvr.updateRss(entry.getAuthor(), calId, calSvr.getCalendarImportExports(CalendarServiceImpl.ICALENDAR),Integer.parseInt(numberLimited)) ;
        calSvr.updateCalDav(entry.getAuthor(), calId, calSvr.getCalendarImportExports(CalendarServiceImpl.ICALENDAR),Integer.parseInt(numberLimited)) ;
      }
    } catch (Exception e) {
      e.printStackTrace() ;
    }
    finally{
      ExoContainerContext.setCurrentContainer(oldContainer);
    }

  }

}
