package pers.cy.iris.commons.util.bootstrap;

/**
 * @Author:cy
 * @Date:Created in 09:44 17/3/23
 * @Destription: 目前支持的server的类型
 */
public enum ServerType {
	Coordinator("coordinator"),
	Broker("broker");

	private final String typeDescription;
	private String propertiesPath;

	ServerType(String typeDescription) {
		this.typeDescription = typeDescription;
		this.propertiesPath = typeDescription + ".properties";
	}

	public String nameKey(){
		return typeDescription+".name";
	}

	public String portKey(){
		return typeDescription+".port";
	}

	public String propertiesPath(){
		return propertiesPath;
	}

	public void setPropertiesPath(String propertiesPath) {
		this.propertiesPath = propertiesPath;
	}
}
