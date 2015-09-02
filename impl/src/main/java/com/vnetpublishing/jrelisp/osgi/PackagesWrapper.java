package com.vnetpublishing.jrelisp.osgi;

import org.armedbear.lisp.Packages;
import org.armedbear.lisp.Package;
import java.util.Map;

public class PackagesWrapper extends Packages {

	Map<String,Package> old;
	
	public void set(DeligatingPackagesRegistry reg) {
		old = getData();
		setData(reg);
	}
	
	public void unset() {
		super.setData(old);
	}
}
