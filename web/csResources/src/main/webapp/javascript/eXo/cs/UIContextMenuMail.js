function UIContextMenuMail(){
	this.menus = new Array,
	this.attachedElement = null ;
	this.menuElement = null ;
	this.preventDefault = true ;
	this.preventForms = true ;
}

UIContextMenuMail.prototype.getCallback = function(menuId) {
	var menus = document.getElementById(menuId) ;
  if(!menus) return ;
	var callback = menus.getAttribute("eXoCallback") ;
	return callback ;
} ;
UIContextMenuMail.prototype.getPortlet = function(portletid) {
	this.portletName = portletid ;
} ;
UIContextMenuMail.prototype.init = function(conf) {
	var UIContextMenuMail = eXo.webui.UIContextMenuMail ;
	if ( document.all && document.getElementById && !window.opera ) {
		UIContextMenuMail.IE = true;
	}

	if ( !document.all && document.getElementById && !window.opera ) {
		UIContextMenuMail.FF = true;
	}

	if ( document.all && document.getElementById && window.opera ) {
		UIContextMenuMail.OP = true;
	}

	if ( UIContextMenuMail.IE || UIContextMenuMail.FF ) {

		if (conf && typeof(conf.preventDefault) != "undefined") {
			UIContextMenuMail.preventDefault = conf.preventDefault;
		}

		if (conf && typeof(conf.preventForms) != "undefined") {
			UIContextMenuMail.preventForms = conf.preventForms;
		}
		document.getElementById(UIContextMenuMail.portletName).oncontextmenu = UIContextMenuMail.show;
	}
} ;

UIContextMenuMail.prototype.attach = function(classNames, menuId) {
	var UIContextMenuMail = eXo.webui.UIContextMenuMail ;
	if (typeof(classNames) == "string") {
		UIContextMenuMail.menus[classNames] = menuId;
	}

	if (typeof(classNames) == "object") {
		for (x = 0; x < classNames.length; x++) {
			UIContextMenuMail.menus[classNames[x]] = menuId ;
		}
	}
} ;

UIContextMenuMail.prototype.getMenuElementId = function(evt) {
	var _e = window.event || evt ;
	var UIContextMenuMail = eXo.webui.UIContextMenuMail ;
	if (UIContextMenuMail.IE) {
		UIContextMenuMail.attachedElement = _e.srcElement;
	} else {
		UIContextMenuMail.attachedElement = _e.target;
	}

	while(UIContextMenuMail.attachedElement != null) {
		var className = UIContextMenuMail.attachedElement.className;

		if (typeof(className) != "undefined") {
			className = className.replace(/^\s+/g, "").replace(/\s+$/g, "")
			var classArray = className.split(/[ ]+/g);

			for (i = 0; i < classArray.length; i++) {
				if (UIContextMenuMail.menus[classArray[i]]) {
					return UIContextMenuMail.menus[classArray[i]];
				}
			}
		}

		if (UIContextMenuMail.IE) {
			UIContextMenuMail.attachedElement = UIContextMenuMail.attachedElement.parentElement;
		} else {
			UIContextMenuMail.attachedElement = UIContextMenuMail.attachedElement.parentNode;
		}
	}

	return null;
} ;

UIContextMenuMail.prototype.getReturnValue = function(evt) {
	var returnValue = true;
	var _e = window.event || evt;

	if (evt.button != 1) {
		if (evt.target) {
			var el = _e.target;
		} else if (_e.srcElement) {
			var el = _e.srcElement;
		}

		var tname = el.tagName.toLowerCase();

		if ((tname == "input" || tname == "textarea")) {
			if (!UIContextMenuMail.preventForms) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else {
			if (!UIContextMenuMail.preventDefault) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		}
	}

	return returnValue;
} ;

UIContextMenuMail.prototype.hasChild = function(root, obj) {
	if(typeof(obj) == "string") obj = document.getElementById(obj) ;
	var children = eXo.core.DOMUtil.findChildrenByClass(root, "div", "UIRightClickPopupMenu") ;
	var len = children.length ;
	if (len > 0) return children ;
	return false ;
} ;

UIContextMenuMail.prototype.show = function(evt) {
	var _e = window.event || evt
	var UIContextMenuMail = eXo.webui.UIContextMenuMail ;
	var menuElementId = UIContextMenuMail.getMenuElementId(_e) ;
	if (menuElementId) {
		UIContextMenuMail.menuElement = document.getElementById(menuElementId) ;
		var callback = UIContextMenuMail.getCallback(menuElementId) ;
		if(callback) {
			callback = callback + "(_e)" ;
			eval(callback) ;
		}
		var extraX = (document.getElementById("UIControlWorkspace")) ? document.getElementById("UIControlWorkspace").offsetWidth : 0 ;
		var extraY = 0 ;
		if (UIContextMenuMail.menuElement.offsetParent) {
			extraX -= eXo.core.Browser.findPosX(UIContextMenuMail.menuElement.offsetParent) ;
			extraY = eXo.core.Browser.findPosY(UIContextMenuMail.menuElement.offsetParent) ;
		}
		var top = eXo.core.Browser.findMouseYInPage(_e) - extraY ;
		var left = eXo.core.Browser.findMouseXInPage(_e) - extraX ;
		eXo.core.DOMUtil.listHideElements(UIContextMenuMail.menuElement) ;
		var ln = eXo.core.DOMUtil.hideElementList.length ;
		if (ln > 0) {
			for (var i = 0; i < ln; i++) {
				eXo.core.DOMUtil.hideElementList[i].style.display = "none" ;
			}
		}
    if (!UIContextMenuMail.IE && document.getElementById("UIPageDesktop")) {
      var uiWindow = eXo.core.DOMUtil.findAncestorByClass(document.getElementById(UIContextMenuMail.portletName), "UIWindow") ;
      top -= uiWindow.offsetTop ;
      left -= (uiWindow.offsetLeft)  ;
		}
		UIContextMenuMail.menuElement.style.left = left + "px" ;
		UIContextMenuMail.menuElement.style.top = top + "px" ;
		UIContextMenuMail.menuElement.style.display = 'block' ;
		eXo.core.DOMUtil.addClass(UIContextMenuMail.menuElement, UIContextMenuMail.portletName) ;
		UIContextMenuMail.menuElement.onmouseover = UIContextMenuMail.autoHide ;
		UIContextMenuMail.menuElement.onmouseout = UIContextMenuMail.autoHide ;		
//		if (!UIContextMenuMail.IE) {
//			/* Clean up cache menu in body */
//			try{				
//				if(UIContextMenuMail.hasChild(document.body, menuElementId)) {
//					var popup = UIContextMenuMail.hasChild(document.body, menuElementId) ;
//					var len = popup.length ;
//					for(var i = 0 ; i < len ; i++) {
//						document.body.removeChild(popup[i]) ;
//					}
//				}
//			} catch(e) {
//				alert(e.message) ;		
//			}
//			/* ---------------------- */
//
//			//document.body.appendChild(tmp) ;
//      //document.body.appendChild(UIContextMenuMail.menuElement) ;
//		}
		return false ;
	}
	return UIContextMenuMail.getReturnValue(_e) ;
} ;

UIContextMenuMail.prototype.autoHide = function(evt) {
	var _e = window.event || evt ;
	var eventType = _e.type ;	
	var UIContextMenuMail = eXo.webui.UIContextMenuMail ;
	if (eventType == 'mouseout') {
		UIContextMenuMail.timeout = setTimeout("eXo.webui.UIContextMenuMail.menuElement.style.display='none'", 5000) ;		
	} else {
		if (UIContextMenuMail.timeout) clearTimeout(UIContextMenuMail.timeout) ;		
	}
} ;

UIContextMenuMail.prototype.replaceall = function(string, obj) {			
	var p = new Array() ;
	var i = 0 ;
	for(var reg in obj){
		p.push(new RegExp(reg)) ;
		string = string.replace(p[i], obj[reg]) ;
		i++ ;
	}
	if (!string) alert("Not match") ;
	return string ;
} ;

UIContextMenuMail.prototype.changeAction = function(obj, id) {
	var actions = eXo.core.DOMUtil.findDescendantsByTagName(obj, "a") ;
	var len = actions.length ;
	var href = "" ;
	if (typeof(id) == "string") {		
		var pattern = /objectId\s*=\s*[A-Za-z0-9_]*(?=&|'|")/ ;
		for(var i = 0 ; i < len ; i++) {
			href = String(actions[i].href) ;
			if (!pattern.test(href)) continue ;
			actions[i].href = href.replace(pattern,"objectId="+id) ;
		}
	} else if (typeof(id) == "object") {
		for(var i = 0 ; i < len ; i++) {
			href = String(actions[i].href) ;			
			actions[i].href = eXo.webui.UIContextMenuMail.replaceall(href, id) ;
		}
	} else {
		return  ;
	}
	
} ;

UIContextMenuMail.prototype.hide = function() {
	var ln = eXo.core.DOMUtil.hideElementList.length ;
	if (ln > 0) {
		for (var i = 0; i < ln; i++) {
			eXo.core.DOMUtil.hideElementList[i].style.display = "none" ;
		}
	}
} ;

UIContextMenuMail.prototype.showHide = function(obj) {
	if (obj.style.display != "block") {
		eXo.webui.UIContextMenuMail.hide() ;
		obj.style.display = "block" ;
		eXo.core.DOMUtil.listHideElements(obj) ;
	} else {
		obj.style.display = "none" ;
	}
} ;

UIContextMenuMail.prototype.swapMenu = function(oldmenu, clickobj) {
	var UIContextMenuMail = eXo.webui.UIContextMenuMail ;
	var Browser = eXo.core.Browser ;
	var menuX = Browser.findPosX(clickobj) ;
	var menuY = Browser.findPosY(clickobj) + clickobj.offsetHeight ;
	if (arguments.length > 2) { // Customize position of menu with an object that have 2 properties x, y 
		menuX = arguments[2].x ;
		menuY = arguments[2].y ;
	}	
	if(document.getElementById("tmpMenuElement")) document.getElementById("UIPortalApplication").removeChild(document.getElementById("tmpMenuElement")) ;
	var tmpMenuElement = oldmenu.cloneNode(true) ;
	tmpMenuElement.setAttribute("id","tmpMenuElement") ;
	UIContextMenuMail.menuElement = tmpMenuElement ;
	document.getElementById("UIPortalApplication").appendChild(tmpMenuElement) ;
	UIContextMenuMail.menuElement.style.top = menuY + "px" ;
	UIContextMenuMail.menuElement.style.left = menuX + "px" ;	
	UIContextMenuMail.showHide(UIContextMenuMail.menuElement) ;
} ;

eXo.webui.UIContextMenuMail = new UIContextMenuMail() ;