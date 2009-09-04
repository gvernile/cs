/**
 * @author Uoc Nguyen
 *  email uoc.nguyen@exoplatform.com
 *
 *  This is an UI component use to manage UICreateNewRoomPopupWindow popup window
 */
function UICreateNewRoomPopupWindow() {
}

/**
 * Initializing method
 *
 * @param {HTMLElement} rootNode
 * @param {UIMainChatWindow} UIMainChatWindow
 */
UICreateNewRoomPopupWindow.prototype.init = function(rootNode, UIMainChatWindow) {
  this.rootNode = rootNode;
  this.UIMainChatWindow = UIMainChatWindow;
  var fieldList = this.rootNode.getElementsByTagName('input');
  for (var i=0; i<fieldList.length; i++) {
    if (fieldList[i].name == 'roomName') {
      this.roomNameField = fieldList[i];
      continue;
    }
  }
};

/**
 * Use to make component visible or not
 *
 * @param {Boolean} visible
 */
UICreateNewRoomPopupWindow.prototype.setVisible = function(visible) {
  if (!this.UIMainChatWindow.userStatus ||
      this.UIMainChatWindow.userStatus == this.UIMainChatWindow.OFFLINE_STATUS) {
    return;
  }
  if (visible) {
    if (this.rootNode.style.display != 'block') {
      this.rootNode.style.display = 'block'; 
    }
    this.roomNameField.focus();
    this.UIPopupManager.focusEventFire(this);
  } else {
    if (this.rootNode.style.display != 'none') {
      this.rootNode.style.display = 'none'; 
    }
    this.roomNameField.value = '';
  }
};

/**
 * Use to call service to create a new room
 */
UICreateNewRoomPopupWindow.prototype.createNewRoomAction = function() {
  var roomName = this.roomNameField.value;
  if (roomName.indexOf(' ') != -1 ||
      roomName == '') {
    window.alert('Room name is invalid.');
    this.roomNameField.focus();
    return;
  }
  this.setVisible(false);
  this.UIMainChatWindow.newestRoomName = roomName;
  this.UIMainChatWindow.createRoomChat({id: (new Date()).getTime(), name:roomName});
};

eXo.communication.chatbar.webui.UICreateNewRoomPopupWindow = new UICreateNewRoomPopupWindow();
