package com.vnetpublishing.jrelisp.osgi;

import org.osgi.framework.Version;
import org.armedbear.lisp.Package;

public class OSGIDependency {

	protected Version version;
	protected String name;
	protected Package pkg = null;
	protected final int hash;
	
	public OSGIDependency(String pkgName, Version version) 
	{
		this.name = pkgName;
		this.version = version;
		int hash = pkgName.hashCode();
		
		if (version != null) {
			 hash = (hash >> 1) + (version.toString().hashCode() >> 1);
		}
		
		this.hash = hash;
	}
	
	public String getName() 
	{
		return name;
	}

	public Package getPackage() 
	{
		return pkg;
	}
	
	public Version getVersion() 
	{
		return version;
	}
	
	public void setPackage(Package pkg) 
	{
		this.pkg = pkg;
		
	}
	
	public int hashCode() 
	{
		return hash;
	}
	
}

