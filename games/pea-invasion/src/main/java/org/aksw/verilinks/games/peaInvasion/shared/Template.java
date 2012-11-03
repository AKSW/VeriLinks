package org.aksw.verilinks.games.peaInvasion.shared;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Template implements IsSerializable{

  /**
  * HashMap that will always contain strings for both keys and values
  * @gwt.typeArgs <java.lang.String, java.lang.String>
  */
  private HashMap<String, String> template;
  
  public Template(){
    template= new HashMap<String, String>();
    template.put(PropertyConstants.NAME,"");
    template.put(PropertyConstants.ENDPOINT,"");
    template.put(PropertyConstants.PROP0,"");
    template.put(PropertyConstants.PROP1,"");
    template.put(PropertyConstants.PROP2,"");
    template.put(PropertyConstants.PROP3,"");
    template.put(PropertyConstants.IMAGE,"");
    template.put(PropertyConstants.TYPE,"");
    template.put(PropertyConstants.LATITUDE,"");
    template.put(PropertyConstants.LONGITUDE,"");
    template.put(PropertyConstants.ATTR_ZOOM,"");
  }
  
  public void setName(String name) {
    template.put(PropertyConstants.NAME,name);
  }
  
  public void setEndpoint(String endpoint) {
    template.put(PropertyConstants.ENDPOINT,endpoint);
  }
  
  public void setProp0(String prop0) {
    template.put(PropertyConstants.PROP0,prop0);
  }
  
  public void setProp1(String prop1) {
    template.put(PropertyConstants.PROP1,prop1);
  }
  
  public void setProp2(String prop2) {
    template.put(PropertyConstants.PROP2,prop2);
  }
  
  public void setProp3(String prop3) {
    template.put(PropertyConstants.PROP3,prop3);
  }
  
  public void setImage(String image) {
    template.put(PropertyConstants.IMAGE,image);
  }

	public void setType(String type) {
		template.put(PropertyConstants.TYPE, type);
		if(type.equals(PropertyConstants.TEMPLATE_TYPE_MAP)){
			setLat(PropertyConstants.LATITUDE_VALUE);
			setLong(PropertyConstants.LONGITUDE_VALUE);
			System.out.println("MapType set Lat and Long");
			System.out.println(PropertyConstants.LATITUDE_VALUE);
			System.out.println(PropertyConstants.LONGITUDE_VALUE);
		}
	}
  
	public void setLat(String lat) {
		template.put(PropertyConstants.LATITUDE, lat);
	}
	
	public void setLong(String longitude) {
		template.put(PropertyConstants.LONGITUDE, longitude);
	}
	
	public void setZoom(String zoom) {
		template.put(PropertyConstants.ATTR_ZOOM, zoom);
	}
	
	
  public String getName() {
    return (String) template.get(PropertyConstants.NAME);
  }
  
  public String getEndpoint() {
    return (String) template.get(PropertyConstants.ENDPOINT);
  }
  
  public String getProp0() {
    return (String) template.get(PropertyConstants.PROP0);
  }
  
  public String getProp1() {
    return (String) template.get(PropertyConstants.PROP1);
  }
  
  public String getProp2() {
    return (String) template.get(PropertyConstants.PROP2);
  }
  
  public String getProp3() {
    return (String) template.get(PropertyConstants.PROP3);
  }
  
  public String getImage() {
    return (String) template.get(PropertyConstants.IMAGE);
  }
  
  public int getCountPoperties() {
    return this.template.size();
  }

  public String getType(){
  	return (String) template.get(PropertyConstants.TYPE);
  }
  
  public String getLat() {
  	return (String) template.get(PropertyConstants.LATITUDE);
	}
  
  public String getLong() {
  	return (String) template.get(PropertyConstants.LONGITUDE);
	}
  
  public String getZoom() {
  	return (String) template.get(PropertyConstants.ATTR_ZOOM);
	}
  
  public HashMap<String, String> getTemplateMap(){
    return this.template;
  }

	

  

}
