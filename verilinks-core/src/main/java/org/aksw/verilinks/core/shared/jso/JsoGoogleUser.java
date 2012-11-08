package org.aksw.verilinks.core.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoGoogleUser  extends JavaScriptObject{
  protected JsoGoogleUser(){}
  
  public final native String getId() /*-{
    return this.id;
  }-*/;

  public final native String getName() /*-{
    return this.name;
  }-*/;
}
