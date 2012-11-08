package org.aksw.verilinks.core.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoFacebookFriends extends JavaScriptObject{
  
  protected JsoFacebookFriends() {}
 

  public final native JsArray<JsoFacebookFriendsEntry> getEntries() /*-{
    return this.data;
  }-*/;

 
}
