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
package org.exoplatform.contact.service ;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by The eXo Platform SARL
 * Author : Hung Nguyen
 *          hung.nguyen@exoplatform.com
 * July 2, 2007  
 * 
 */

public class Utils {
  final public static String SPLIT = "::".intern();
  final public static String SEMI_COLON = ";".intern();
  final public static String contactTempId = "ContacttempId" ;
  public static int limitExport = 150 ;
  
  public static String formatDate(String format, Date date) {
    Format formatter = new SimpleDateFormat(format);
    return formatter.format(date);
  }
  
  public static boolean isEmpty(String s) {
    if (s == null || s.trim().length() == 0) return true ;
    return false ;    
  }
  
}