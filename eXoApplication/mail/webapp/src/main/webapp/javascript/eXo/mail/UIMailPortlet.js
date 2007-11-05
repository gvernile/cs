eXo.require('eXo.webui.UIContextMenu') ;

function UIMailPortlet(){
};

UIMailPortlet.prototype.showContextMenu = function() {
	
	var UIContextMenu = eXo.webui.UIContextMenu ;//eXo.contact.ContextMenu ;
	var config = {
		'preventDefault':false, 
		'preventForms':false
	} ;	
	UIContextMenu.init(config) ;
	UIContextMenu.attach('MessageItem', 'UIMessagePopupMenu') ;
	UIContextMenu.attach('IconFolder', 'UIFolderListPopupMenu') ;
	UIContextMenu.attach('IconTagHolder', 'UITagListPopupMenu') ;
	UIContextMenu.attach('InboxIcon', 'UIDefaultFolderPopupMenu') ;
	UIContextMenu.attach('DraftsIcon', 'UIDefaultFolderPopupMenu') ;
	UIContextMenu.attach('SentIcon', 'UIDefaultFolderPopupMenu') ;
	UIContextMenu.attach('SpamIcon', 'UIDefaultFolderPopupMenu') ;
	UIContextMenu.attach('TrashIcon', 'UITrashFolderPopupMenu') ;
} ;

UIMailPortlet.prototype.msgPopupMenuCallback = function(evt) {
	var UIContextMenu = eXo.webui.UIContextMenu ;
	var _e = window.event || evt ;
	//_e.cancelBubble = true ;
	var src = null ;
	if (UIContextMenu.IE) {
		src = _e.srcElement;
	} else {
		src = _e.target;
	}
	if (src.nodeName != "tr")
		src = src.parentNode;
		
	id = src.getAttribute("msgId");
	eXo.webui.UIContextMenu.changeAction(UIContextMenu.menuElement, id) ;
} ;

UIMailPortlet.prototype.folderListPopupMenuCallback = function(evt) {
	var UIContextMenu = eXo.webui.UIContextMenu ;
	var _e = window.event || evt ;
	//_e.cancelBubble = true ;
	var src = null ;
	if (UIContextMenu.IE) {
		src = _e.srcElement;
	} else {
		src = _e.target;
	}
	if (src.nodeName != "A")
		src = src.parentNode;
		
	folderName = src.getAttribute("folderName");
	eXo.webui.UIContextMenu.changeAction(UIContextMenu.menuElement, folderName) ;
} ;

UIMailPortlet.prototype.defaultFolderPopupMenuCallback = function(evt) {
	var UIContextMenu = eXo.webui.UIContextMenu ;
	var _e = window.event || evt ;
	//_e.cancelBubble = true ;
	var src = null ;
	if (UIContextMenu.IE) {
		src = _e.srcElement;
	} else {
		src = _e.target;
	}
	if (src.nodeName != "A")
		src = src.parentNode;
		
	folder = src.getAttribute("folder");
	eXo.webui.UIContextMenu.changeAction(UIContextMenu.menuElement, folder) ;
} ;

UIMailPortlet.prototype.tagListPopupMenuCallback = function(evt) {
	var UIContextMenu = eXo.webui.UIContextMenu ;
	var _e = window.event || evt ;
	//_e.cancelBubble = true ;
	var src = null ;
	if (UIContextMenu.IE) {
		src = _e.srcElement;
	} else {
		src = _e.target;
	}
	if (src.nodeName != "A")
		src = src.parentNode;
		
	tagName = src.getAttribute("tagId");
	eXo.webui.UIContextMenu.changeAction(UIContextMenu.menuElement, tagName) ;
} 

UIMailPortlet.prototype.selectItem = function(obj) {
	var DOMUtil = eXo.core.DOMUtil ;
  obj = DOMUtil.findFirstDescendantByClass(obj, "input", "checkbox");
	var tr = DOMUtil.findAncestorByTagName(obj, "tr") ;
	if(obj.checked) {
		if (!tr.getAttribute("tmpClass")) {			
			tr.setAttribute("tmpClass", tr.className) ;
			tr.className = "SelectedItem" ;
		}
	} else {
		if (tr.getAttribute("tmpClass")) {			
			tr.className = tr.getAttribute("tmpClass") ;
			tr.removeAttribute("tmpClass") ;
		}
	}
	
	var table = DOMUtil.findAncestorByTagName(tr, "table") ;
  var trs = DOMUtil.findDescendantsByTagName(table, "tr");
	for (var i = 1; i < trs.length; i++) {
		var input = DOMUtil.findFirstDescendantByClass(trs[i], "input", "checkbox");
		if (!input.checked) {
			if (trs[i].className == "SelectedItem")
				trs[i].className = "NormalItem";
		}
	}
}
UIMailPortlet.prototype.checkAll = function(obj) {
	var DOMUtil = eXo.core.DOMUtil ;
	var thead = DOMUtil.findAncestorByTagName(obj, "thead") ;
	var tbody = DOMUtil.findNextElementByTagName(thead, "tbody") ;
	var checkboxes = DOMUtil.findDescendantsByClass(tbody, "input", "checkbox") ;
	var len = checkboxes.length ;
	if (obj.checked){
		for(var i = 0 ; i < len ; i++) {
			checkboxes[i].checked = true ;
			var obj = DOMUtil.findAncestorByTagName(checkboxes[i], "td");
			this.selectItem(obj) ;
		}
	} else {
		for(var i = 0 ; i < len ; i++) {
			checkboxes[i].checked = false ;
			var obj = DOMUtil.findAncestorByTagName(checkboxes[i], "td");
			this.selectItem(obj) ;
		}
	}
} ;

UIMailPortlet.prototype.readMessage = function() {} ;

eXo.mail.UIMailPortlet = new UIMailPortlet();