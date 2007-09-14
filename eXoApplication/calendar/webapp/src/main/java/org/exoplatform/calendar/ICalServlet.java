/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.calendar;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.calendar.service.CalendarService;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.jcr.RepositoryService;
/**
 * Created by The eXo Platform SARL        .
 * @author: Nguyen Quang Hung
 * @email: nguyenkequanghung@yahoo.com
 */
@SuppressWarnings({"serial","unused"})
public class ICalServlet extends HttpServlet {
  public void init(ServletConfig config) throws ServletException {}  
	public void service(HttpServletRequest request, HttpServletResponse response) 
          throws ServletException, IOException {
    response.setHeader("Cache-Control", "private max-age=600, s-maxage=120");
    String pathInfo = request.getPathInfo() ;
    String[] arrayInfo = pathInfo.toString().split("/") ;
    Session session = null ;
    try{
      if(arrayInfo.length < 5) throw new Exception("Invalid URL");
      String portalName = arrayInfo[1] ;
      String wsName = arrayInfo[2] ;
      String userName = arrayInfo[3] ;
      StringBuffer fileName = new StringBuffer() ;
      for(int i = 4; i < arrayInfo.length ; i ++) {
        if(fileName.length() > 0) fileName.append("/") ;
        fileName.append(arrayInfo[i]) ;
      }
      PortalContainer pcontainer = getPortalContainer(portalName) ;
      PortalContainer.setInstance(pcontainer) ;
      RepositoryService repositoryService = 
        (RepositoryService)pcontainer.getComponentInstanceOfType(RepositoryService.class) ;
      
      session = repositoryService.getDefaultRepository().getSystemSession(wsName) ;
      CalendarService calService = 
        (CalendarService)pcontainer.getComponentInstanceOfType(CalendarService.class) ;
      
      Node node = calService.getRssHome(userName).getNode(fileName.toString()) ;
      if (node == null) throw new Exception("Node " + fileName + " not found. ");
      Node content;
      if(node.isNodeType("exo:rssData")) {
        response.setContentType("text/xml") ;
        InputStream is = node.getProperty("exo:content").getStream();
        byte[] buf = new byte[is.available()];
        is.read(buf);
        ServletOutputStream os = response.getOutputStream();
        os.write(buf);
      }else if(node.isNodeType("exo:iCalData")) {
        response.setContentType("text/calendar") ;
        InputStream is = node.getProperty("exo:data").getStream();
        byte[] buf = new byte[is.available()];
        is.read(buf);
        ServletOutputStream os = response.getOutputStream();
        os.write(buf);
      } else throw new Exception("Invalid node type, expected exo:rssData or exo:iCalData type");
    }catch(Exception e) {
      e.printStackTrace() ;
      throw new ServletException(e) ;
    }finally{
      if(session != null) {
        session.logout() ;
      }
    }    		
	}  
  
  private  PortalContainer getPortalContainer(String portalName) {
    PortalContainer pcontainer =  RootContainer.getInstance().getPortalContainer(portalName) ;
    return pcontainer ;
  }
}
