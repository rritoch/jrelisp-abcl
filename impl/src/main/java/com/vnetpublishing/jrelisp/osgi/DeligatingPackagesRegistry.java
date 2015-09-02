package com.vnetpublishing.jrelisp.osgi;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


import org.armedbear.lisp.Package;
import org.armedbear.lisp.Packages;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Version;
import org.osgi.framework.wiring.BundleWiring;

public class DeligatingPackagesRegistry extends ConcurrentHashMap<String, Package> 
	implements BundleListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5269309431626927020L;
	private static final Logger logger = Logger.getLogger(DeligatingPackagesRegistry.class.getName());
	 

	protected static boolean active = false;
	protected static Object origPackages = null;
	protected static DeligatingPackagesRegistry INSTANCE = null;
	protected static final ThreadLocal<ClassLoader> PackageClassLoader = new ThreadLocal<ClassLoader>();

	private ThreadLocal<ConcurrentHashMap<String, Package>> registryOverride = new ThreadLocal<ConcurrentHashMap<String, Package>>();
	
	protected ConcurrentHashMap<ClassLoader,ConcurrentHashMap<String, Package>> registrations = new ConcurrentHashMap<ClassLoader,ConcurrentHashMap<String, Package>>();
	protected Map<OSGIDependency,Package>  provided = new HashMap<OSGIDependency,Package>();
	protected Map<ClassLoader,List<OSGIDependency>> providers = new HashMap<ClassLoader,List<OSGIDependency>>();
	
	
	
	protected  ConcurrentHashMap<String, Package> getActivePackages() 
	{
		
		if (registryOverride.get() != null) {
			return registryOverride.get();
		}
		
		ConcurrentHashMap<String, Package> ret = null; 
		ClassLoader scl = ClassLoader.getSystemClassLoader();
		//ClassLoader cl = Thread.currentThread().getContextClassLoader();
		ClassLoader cl = PackageClassLoader.get(); 
		
		if (cl == null) {
			//TODO: Set to ThreadLocal Classloader?
			//cl = RT.baseLoader();
		}
		while (ret == null && cl != null && cl != scl) {
			ret = registrations.get(cl);
			cl = cl.getParent();
		}
		if (ret == null) {
			if (cl == ClassLoader.getSystemClassLoader()) {
				ret = registrations.get(cl);
			}
			if (ret == null) {
				ret = new ConcurrentHashMap<String, Package>();
			}
		}
		return ret;
	}
	
	@Override 
	public void clear() {
		getActivePackages().clear();
	}
	
	@Override
	public boolean contains(Object value) {
		return getActivePackages().contains(value);
	}
	
	@Override
	public boolean containsKey(Object value) {
		return getActivePackages().containsKey(value);
	}
	
	@Override
	public boolean containsValue(Object value) {
		return getActivePackages().containsValue(value);
	}
	
	
	@Override
	public Enumeration<Package> elements() 
	{
		return getActivePackages().elements();
	}
	
	@Override
	public Set<Entry<String,Package>> entrySet() 
	{
		return getActivePackages().entrySet();
	}	
	
	
	@Override 
	public Package get(Object key) 
	{
		return getActivePackages().get(key);
	}
	
	@Override
	public boolean isEmpty() 
	{
		return getActivePackages().isEmpty();
	}
	
	@Override
	public Enumeration<String> keys() 
	{
		
		return getActivePackages().keys();
	}
	
	@Override
	public KeySetView<String,Package> keySet() {
		return getActivePackages().keySet();
	}

	@Override
	public Package put(String key,Package value) {
		return getActivePackages().put(key, value);
	}
	
	@Override
	public void putAll(Map<? extends String, ? extends Package> m) 
	{
		getActivePackages().putAll(m);
	}
	
	@Override
	public Package putIfAbsent(String k, Package v) 
	{
		return getActivePackages().putIfAbsent(k, v);
	}
	
	@Override
	public Package remove(Object key) {
		return  getActivePackages().remove(key);
	}
	
	@Override
	public boolean remove(Object key, Object value) 
	{
		return getActivePackages().remove(key, value);
	}
	
	@Override
	public Package replace(String k, Package v) 
	{
		return getActivePackages().replace(k, v);
	}
	
	
	@Override
	public boolean replace(String k, Package oldValue, Package newValue) 
	{
		return getActivePackages().replace(k, oldValue,newValue);
	}
	
	@Override
	public int size() 
	{
		return getActivePackages().size();
	}
	
	@Override
	public Collection<Package> values() {
		return getActivePackages().values();
	}
	
	protected OSGIDependency findImport(String nsimport) {
		List<OSGIDependency> avail = new ArrayList<OSGIDependency>();
		avail.addAll(provided.keySet());
		return PackageUtil.findImport(nsimport,avail);
	}
	
	protected synchronized void register(ClassLoader cl, List<String> exports, List<String> imports) {
		
		ConcurrentHashMap<String, Package> reg = registrations.get(cl);
		if (reg == null) {
			reg = new ConcurrentHashMap<String, Package>();
		}
		
		List<OSGIDependency> myExports = new ArrayList<OSGIDependency>();
		List<OSGIDependency> myImports = new ArrayList<OSGIDependency>();
		
		// Populate export list
		
		if (exports != null) {
			for (String export : exports) {
				String versionStr = PackageUtil.parseDefinedAttributes(export).get("version");
				if (versionStr == null || versionStr.length() < 1) {
					myExports.add(new OSGIDependency(PackageUtil.parseDefinedSymbol(export),null));
				} else {
					myExports.add(new OSGIDependency(PackageUtil.parseDefinedSymbol(export),new Version(versionStr)));
				}
			}
		}
		
		// Grab all imports, exception if import is not an export
		
		if (imports != null) {
			for (String nsimport : imports) {
				OSGIDependency i = findImport(nsimport);
			
				if (i == null) {
					i = PackageUtil.findImport(nsimport,myExports);
				}
			
				if (i == null) {
					throw new RuntimeException("Missing import");
				}
			
				myImports.add(i);
			}
		}
		
		// Generate all exports, they should exist eventually
		
		registryOverride.set(reg); // Temporarily activate runtime
		for (OSGIDependency export : myExports) {
			Package ns = Packages.findOrCreate(export.getName());
			reg.put(ns.getName(),ns);
		}
		registryOverride.set(null);
		
		// Process imports
		
		for (OSGIDependency myImport : myImports) {
			Package ns = reg.get(myImport.getName());
			Package nsin = myImport.getPackage();
			if (ns == null) {
				if (nsin == null) {
					nsin = provided.get(myImport);
					myImport.setPackage(nsin);
				}
				reg.put(myImport.getName(), nsin);
			} else {
				//TODO: Copy symbols & functions into package
			}
		}
		
		// Finally register exports
		for (OSGIDependency export : myExports) {
			provided.put(export, reg.get(export.getName()));
		}
		
		providers.put(cl,myExports);
		
		registrations.put(cl,reg);
		
		// Pre-load export Package, note Packages aren't usually thread safe
		if (myExports != null) {
			final List<OSGIDependency> preloadExports = myExports;
			
			ClassLoader ccl = Thread.currentThread().getContextClassLoader();
			final ClassLoader bcl = cl == ClassLoader.getSystemClassLoader()  ? ccl : cl;
			Thread.currentThread().setContextClassLoader(bcl);
			try {
				withVoidPackageClassLoader(bcl,new Runnable() {
					@Override
					public void run() {
						for (OSGIDependency export : preloadExports) {
							//TODO: Load named export
						}
					}
				});
			} finally {
				Thread.currentThread().setContextClassLoader(ccl);
			}
		}
	}
	
	protected void unregister(ClassLoader cl) 
	{
		ConcurrentHashMap<String, Package> reg = registrations.get(cl);
		
		if (reg == null) {
			return; // Nothing to do!
		}
		
		List<OSGIDependency> pExports = providers.get(cl);
		
		if (pExports != null) {
			for (OSGIDependency export : pExports) {
				provided.remove(export, reg.get(export.getName()));
			}
			providers.remove(cl);
		}
		
		registrations.remove(cl);
	}
	
	public static boolean startFramework(BundleContext bundleContext, List<String> exports) 
	{
		if (isActive()) {
			logger.warning("Refusing to start Lisp OSGI framework, it is already active");
			return false;
		}
		
		active = true;
		DeligatingPackagesRegistry r = new DeligatingPackagesRegistry();
		r.register(ClassLoader.getSystemClassLoader(), exports, null);
		
		Field Packages;
		try {
			Packages = Package.class.getDeclaredField("Packages");
			Packages.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(Packages, Packages.getModifiers() & ~Modifier.FINAL);
			origPackages = Packages.get(null);
			Packages.set(null, r);
		} catch (NoSuchFieldException e) {
			active = false;
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			active = false;
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			active = false;
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			active = false;
			throw new RuntimeException(e);
		}
		
		r.start(bundleContext);
		INSTANCE = r;
		return true;
	}

	public static boolean stopFramework(BundleContext bundleContext) 
	{
		if (!isActive() || origPackages == null) {
			return false;
		}

		//OSGI Framework SHOULD stop dependencies before us, so we shouldn't need to stop bundles 
		
		Field Packages;
		try {
			Packages = Package.class.getDeclaredField("Packages");
			Packages.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(Packages, Packages.getModifiers() & ~Modifier.FINAL);
			Packages.set(null, origPackages);
		} catch (NoSuchFieldException e) {
			active = false;
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			active = false;
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			active = false;
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			active = false;
			throw new RuntimeException(e);
		}

		origPackages = null;
		active = false;
		INSTANCE = null;
		return true;
	}
	
	protected void start(BundleContext bundleContext) 
	{
		bundleContext.addBundleListener(this);
		List<Bundle> bundles = Arrays.asList(bundleContext.getBundles());
		for(Bundle bundle : bundles) {
			int bstate = bundle.getState();
			if (Bundle.RESOLVED == bstate) { 
				init(bundle);
			}
		}
	}
	
	public boolean isLispBundle(Bundle bundle) 
	{
		logger.fine(String.format("isLispBundle(%s)",bundle.getSymbolicName()));
		Dictionary<String,String> headers = bundle.getHeaders();
		
		String lisp_imports = headers.get("CL-Imports"); 
		logger.fine(String.format("CL-Imports: %s",String.valueOf(lisp_imports)));
		String lisp_exports = headers.get("CL-Exports");
		logger.fine(String.format("CL-Exports: %s",String.valueOf(lisp_exports)));		
		String lisp_enable = headers.get("CL-Enable");
		logger.fine(String.format("CL-Enable: %s",String.valueOf(lisp_enable)));
		
		String lisp_activator_Package = headers.get("CL-Activator");
		logger.fine(String.format("CL-Activator: %s",String.valueOf(lisp_activator_Package)));
		
		if (null != lisp_imports) {
			return true;
		}

		if (null != lisp_exports) {
			return true;
		}

		if (null != lisp_activator_Package) {
			return true;
		}
		
		if (null != lisp_enable) {
			if ("true".equals(lisp_enable.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	protected void init(Bundle bundle) 
	{
		
		if (registrations == null) return; // this shouldn't really happen
		int state = bundle.getState();
		
		logger.info(String.format("Initializing bundle %s has state %s",bundle.getSymbolicName(),PackageUtil.bundleStateName(state)));
		if (state == Bundle.STARTING || state == Bundle.INSTALLED || state == Bundle.RESOLVED || state == Bundle.ACTIVE) {
			
			if (isLispBundle(bundle)) {
			
				ClassLoader cl = bundle.adapt(BundleWiring.class).getClassLoader();
				if (registrations.get(cl) != null) {
					logger.warning(String.format("Classloader for %s has already been registered",bundle.getSymbolicName()));
					return; // Dejavu!
				}
			
				Dictionary<String,String> headers = bundle.getHeaders();
				
				String importsRaw = headers.get("CL-Imports");
				List<String> imports = null;
				if (importsRaw != null) {
					imports =  Arrays.asList(importsRaw.split(","));
				}
				
				String exportsRaw = headers.get("CL-Exports");
				List<String> exports = null;
				if (exportsRaw != null) {
					exports = Arrays.asList(exportsRaw.split(","));
				}
				
				register(cl,exports,imports);
				
				logger.info(String.format("Lisp bundle %s has been registered",bundle.getSymbolicName()));
			}
		} else {
			if (isLispBundle(bundle)) {
				logger.warning(String.format("Lisp bundle %s missed it's chance to be registered",bundle.getSymbolicName()));
			}
		}
	}
	
	
	protected static ClassLoader getBundleClassLoader(Bundle bundle) 
	{
		ClassLoader cl = null;
		if (bundle != null) {
			cl = bundle.adapt(BundleWiring.class).getClassLoader();
		}
		return cl;
	}
	
	protected void uninit(Bundle bundle) 
	{
		if (registrations == null) return; // this shouldn't really happen
		int state = bundle.getState();
		
		logger.info(String.format("Initializing bundle %s has state %s",bundle.getSymbolicName(),PackageUtil.bundleStateName(state)));
		if (state == Bundle.RESOLVED || state == Bundle.ACTIVE) {
			
			if (isLispBundle(bundle)) {
			
				ClassLoader cl = getBundleClassLoader(bundle);
				if (registrations.get(cl) == null) {
					logger.warning(String.format("Classloader for %s isn't registered",bundle.getSymbolicName()));
					return; // Dejavu!
				}
			
				unregister(cl);
				logger.info(String.format("Lisp bundle %s has been unregistered",bundle.getSymbolicName()));
			}
		} else {
			if (isLispBundle(bundle)) {
				logger.warning(String.format("Lisp bundle %s missed it's chance to be registered",bundle.getSymbolicName()));
			}
		}
	}
	
	protected void withVoidPackageClassLoader(ClassLoader cl, Runnable proc) 
	{
		ClassLoader ncl = PackageClassLoader.get();
		PackageClassLoader.set(cl);
		try {
			proc.run();
		} finally {
			PackageClassLoader.set(ncl);
		}
	}
	
	protected void callStart(Bundle bundle) 
	{
		if (isLispBundle(bundle)) {
			Dictionary<String,String> headers = bundle.getHeaders();
			final String lisp_activator_Package = headers.get("CL-Activator");
			if (lisp_activator_Package != null) {
				
				ClassLoader ccl = Thread.currentThread().getContextClassLoader();
				ClassLoader bcl = getBundleClassLoader(bundle);
				Thread.currentThread().setContextClassLoader(bcl);
				try {
					final Bundle bnd = bundle;
					withVoidPackageClassLoader(bcl,new Runnable() {
						@Override
						public void run() {
							//TODO: Load lisp_activator_Package (if not loaded?)
							//TODO: Call start function with arg0=bnd.getBundleContext() 
						}
					});
				} finally {
					Thread.currentThread().setContextClassLoader(ccl);
				}
			}
		}
	}
	
	protected void callStop(Bundle bundle) 
	{
		if (isLispBundle(bundle)) {
			Dictionary<String,String> headers = bundle.getHeaders();
			final String lisp_activator_Package = headers.get("CL-Activator");
			if (lisp_activator_Package != null) {
				
				ClassLoader ccl = Thread.currentThread().getContextClassLoader();
				ClassLoader bcl = getBundleClassLoader(bundle);
				Thread.currentThread().setContextClassLoader(bcl);
				try {
					final Bundle bnd = bundle;
					withVoidPackageClassLoader(bcl,new Runnable() {
						@Override
						public void run() {
							//TODO: Invoke stop method with bundle as argument
						}
					});
				} finally {
					Thread.currentThread().setContextClassLoader(ccl);
				}
			}
		}
	}
	
	@Override
	public void bundleChanged(BundleEvent event) 
	{
		int event_type = event.getType();
		Bundle bundle = event.getBundle();
		logger.fine(String.format("bundleChanged: %s BundleEvent.type is %s",bundle.getSymbolicName(), PackageUtil.bundleEventTypeName(event_type)));
		
		switch(event_type) {

			case BundleEvent.RESOLVED:
				init(bundle);
				break;
			case BundleEvent.UNRESOLVED:
				uninit(bundle);
				break;
				
			case BundleEvent.STARTING:
				logger.warning("***** CAUGHT STARTING EVENT: OSGI container may not be compatible with clj-osgi-namepsaces");
				break;
				
			case BundleEvent.STOPPING:
				callStop(bundle);
				break;

			case BundleEvent.STARTED:
				callStart(bundle);
				break;
			case BundleEvent.STOPPED:
				break;
			case BundleEvent.INSTALLED:
			
			case BundleEvent.UNINSTALLED:
			case BundleEvent.UPDATED:
			default:
				break;
		}
	}
	
	public static boolean isActive() 
	{
		return active;
	}
}


