package org.armedbear.lisp.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

public class InputStreamFacade extends InputStream {

	InputStream is = null;
	URL url;
	protected final UUID uuid = UUID.randomUUID();
	
	Throwable ctrace = null;
	
	public static boolean debug = false;
	
	public InputStreamFacade(IPathname path) {
		
		if (debug) {
			try {
				throw new Exception("Trace");
			} catch (Throwable t) {
				ctrace = t;
			}
		}
		
		url = path.toURL();
		
		try {
			if ("file".equalsIgnoreCase(url.toURI().getScheme())) {
				is = new FileInputStream(path.toFile());
			} else {
				is = url.openStream();
			}
			
			//logDebug("OPENED");
			
		} catch (URISyntaxException e) {
			// Must be a bug somewhere
			e.printStackTrace();
			is = null;
		} catch (FileNotFoundException e) {
			is = null;
		} catch (IOException e) {
			is = null;
		}
	}
	
	public InputStreamFacade(URL url) {
		
		try {
			throw new Exception("Trace");
		} catch (Throwable t) {
			ctrace = t;
		}
		
		this.url = url;
		
		try {
			if ("file".equals(url.toURI().getScheme().toLowerCase())) {
				is = new FileInputStream(new File(url.toURI()));
			} else {
				is = url.openStream();
			}
			//logDebug("OPENED");
		} catch (URISyntaxException e) {
			// Must be a bug somewhere
			e.printStackTrace();
			is = null;
		} catch (FileNotFoundException e) {
			is = null;
		} catch (IOException e) {
			is = null;
		}
		
	}
	
	private void logDebug(String msg) 
	{
		System.err.println(String.format("InputStreamFacade %s#%s: %s",url.toString(),uuid.toString(),msg));
	}
	
	@Override
	public int read() throws IOException {
		if (is == null) {
			throw new IOException("InputStream unavailable");
		}
		
		return is.read();
	}
	
	public void close() throws IOException {
		if (is != null) {
			//logDebug("CLOSED");
			is.close();
			is = null;
		}
	}
	
	public void finalize() throws Throwable {
		if (is != null) {
			close();
			if (debug) {
				logDebug("AutoClosed");
				if (ctrace != null) {
					ctrace.printStackTrace();
				}
			}
		}
		super.finalize();
	}

	public InputStream getInputStream() {
		return is;
	}
}
