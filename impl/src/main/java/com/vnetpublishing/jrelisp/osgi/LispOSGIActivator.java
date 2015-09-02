package com.vnetpublishing.jrelisp.osgi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;

public class LispOSGIActivator implements BundleActivator {
	
	private static final Logger logger = Logger.getLogger(LispOSGIActivator.class.getName());

	@Override
	public void start(BundleContext context) throws Exception 
	{
		ClassLoader ccl = Thread.currentThread().getContextClassLoader();
		
		ClassLoader wcl = context.getBundle().adapt(BundleWiring.class).getClassLoader();
		ClassLoader acl = LispOSGIActivator.class.getClassLoader();
		
		logger.info(String.format("Starting LispOSGIActivator: contextClassloader = %s with classname %s bundleClassLoader = %s with classname %s activatorClassLoader = %s with classname %s"
				,ccl.toString(),ccl.getClass().getName()
				,wcl.toString(),wcl.getClass().getName()
				,acl.toString(),acl.getClass().getName()
		));
		
		Thread.currentThread().setContextClassLoader(wcl);
		
		List<String> exports = new ArrayList<String>();
		exports.add("Lisp.core");
		exports.add("Lisp.osgi.namespaces");
		DeligatingPackagesRegistry.startFramework(context, exports);
		
		Thread.currentThread().setContextClassLoader(ccl);
		logger.info("LispOSGIActivator: Started!");
	}

	@Override
	public void stop(BundleContext context) throws Exception 
	{
		
		ClassLoader ccl = Thread.currentThread().getContextClassLoader();
		ClassLoader wcl = context.getBundle().adapt(BundleWiring.class).getClassLoader();
		
		logger.info("Stopping LispOSGIActivator");
		
		Thread.currentThread().setContextClassLoader(wcl);
		DeligatingPackagesRegistry.stopFramework(context);
		
		Thread.currentThread().setContextClassLoader(ccl);
		
		logger.info("LispOSGIActivator: Stopped!");
	}

}
