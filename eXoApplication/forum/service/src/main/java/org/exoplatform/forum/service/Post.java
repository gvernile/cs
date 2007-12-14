/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL				 All rights reserved.	*
 * Please look at license.txt in info directory for more license detail.	 *
 **************************************************************************/
package org.exoplatform.forum.service ;

import java.util.Date;
import java.util.List;

import org.exoplatform.services.jcr.util.IdGenerator;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *					tuan.nguyen@exoplatform.com
 * Jul 2, 2007	
 * Editer by Vu Duy Tu
 * 				tu.duy@exoplatform.com
 * July 16, 2007
 */
public class Post { 
	private String id;
	private String owner;
	private String path ;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String subject;
	private String message;
	private String remoteAddr;
	private String icon; 
	private boolean isApproved = false ;
	private List<ForumAttachment> attachments ;
	
	
	public Post() {
		id = "post" + IdGenerator.generate() ;
	}
	
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	/**
	 * This method should calculate the id of the topic base on the id of the post
	 * @return
	 */
	public String getTopicId() { return null; }
	/**
	 * This method should calculate the id of the forum base on the id of the post
	 * @return
	 */
	public String getForumId() { return null ;}

	public String getOwner(){return owner;}
	public void setOwner(String owner){this.owner = owner;}
	
	public String getPath() {return path; }
	public void setPath( String path) { this.path = path;}
	
	public Date getCreatedDate(){return createdDate;}
	public void setCreatedDate(Date createdDate){this.createdDate = createdDate;}
	
	public String getModifiedBy(){return modifiedBy;}
	public void setModifiedBy(String modifiedBy){this.modifiedBy = modifiedBy;}
	
	public Date getModifiedDate(){return modifiedDate;}
	public void setModifiedDate(Date modifiedDate){this.modifiedDate = modifiedDate;}
	
	public String getSubject(){return subject;}
	public void setSubject(String subject){this.subject = subject;}
	
	public String getMessage(){return message;}
	public void setMessage(String message){this.message = message;}
	
	public String getRemoteAddr(){return remoteAddr;}
	public void setRemoteAddr(String remoteAddr){this.remoteAddr = remoteAddr;}
	
	public String getIcon() { return icon;}
	public void setIcon( String icon) { this.icon = icon;}
	
	public boolean getIsApproved() { return isApproved;}
	public void setIsApproved(boolean isApproved) { this.isApproved = isApproved;}

	public List<ForumAttachment> getAttachments() { return this.attachments;}
	public void setAttachments( List<ForumAttachment> attachments) { this.attachments = attachments;}
}
