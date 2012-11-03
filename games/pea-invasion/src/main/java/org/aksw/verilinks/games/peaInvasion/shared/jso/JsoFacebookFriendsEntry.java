package org.aksw.verilinks.games.peaInvasion.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoFacebookFriendsEntry extends JavaScriptObject{

  protected JsoFacebookFriendsEntry() {}
  
  public final native String getName() /*-{
    return this.name;
  }-*/;

  public final native String getId() /*-{
    return this.id;
  }-*/;

}
