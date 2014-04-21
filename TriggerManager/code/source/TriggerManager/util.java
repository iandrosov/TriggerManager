package TriggerManager;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2005-06-13 15:58:07 JST
// -----( ON-HOST: xiandros-c640

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.util.*;
import com.wm.app.b2b.server.ns.Namespace;
import com.wm.lang.ns.*;
import com.wm.util.coder.XMLCoder;
import com.wm.app.b2b.server.*;
import com.wm.app.b2b.server.dispatcher.*;
// --- <<IS-END-IMPORTS>> ---

public final class util

{
	// ---( internal utility methods )---

	final static util _instance = new util();

	static util _newInstance() { return new util(); }

	static util _cast(Object o) { return (util)o; }

	// ---( server methods )---




	public static final void addTriggerName (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(addTriggerName)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required name
		// [o] field:0:required message
		IDataHashCursor idc = pipeline.getHashCursor();	
			
			String property_name = prop_name;
			idc.first("name");
			String property_value = (String)idc.getValue();
			if (property_value == null)
				return;
		
			try
			{
				String str = getProperty(property_name);
				str += ";" + property_value;
				property_value = str;
				
					// Get server properties
					Properties config = new Properties();
					InputStream in_stream = (InputStream) new FileInputStream(file_name);
					config.load(in_stream);
					config.setProperty(property_name, property_value);
					// Stor properties to file
					OutputStream out = (OutputStream) new FileOutputStream(file_name);
					config.store(out, "Server property");
		
					out.flush();
					out.close();
					in_stream.close();
		
					idc.first();
					idc.insertAfter( "message", "Set property - " + property_value );
			}
			catch (Exception e)
			{
				idc.first();
				idc.insertAfter( "message", "Error setting property - " + e.getMessage());
			}
			idc.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void deleteTriggerName (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(deleteTriggerName)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required name
		// [o] field:0:required message
		IDataHashCursor idc = pipeline.getHashCursor();	
			
			String property_name = prop_name;
			idc.first("name");
			String property_value = (String)idc.getValue();
			if (property_value == null)
				return;
		
			if (removeValue(property_value))
			{
					idc.first();
					idc.insertAfter( "message", "Remove property value - " + property_value );
			}
			else
			{
					idc.first();
					idc.insertAfter( "message", "ERROR! Remove property value - " + property_value );
			}
		idc.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void getTriggerList (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getTriggerList)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [o] field:0:required value
		// [o] field:1:required value_list
		IDataHashCursor idc = pipeline.getHashCursor();	
			
			String property_name = prop_name;
			
			try 
			{
				String str = getProperty(property_name);
				idc.insertAfter("value",str);
		
				String[] list = getNameList(str);
				idc.insertAfter("value_list",list);
			}
			catch(Exception e)
			{
				throw new ServiceException(e.getMessage());
			}
			idc.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void getTriggersFromPackage (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getTriggersFromPackage)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required package
		// [o] field:1:required trigger_list
		// [o] record:1:required trigger_info
		// [o] - field:0:required trigger_name
		// [o] - field:0:required status
		// [o] - field:0:required package
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	package_1 = IDataUtil.getString( pipelineCursor, "package" );
		pipelineCursor.destroy();
		
		com.wm.app.b2b.server.Package pkg = com.wm.app.b2b.server.PackageManager.getPackage(package_1);
		String[]	trigger_list = new String[1];
		
		IData trg_data = null;
		String node_dir ="";
		File f_node = null;
		String tmp = "";
		boolean reload = false;
		String trigger_enabled = "";
		
		IData trg_info = null;
		IData trg_info_list[] = null;
		try
		{
			// Get loaded nodes based on namespace
			Vector vc = com.wm.app.b2b.server.PackageManager.getNodes(pkg);
			Enumeration enum = vc.elements();
			ArrayList al = new ArrayList();
		    ArrayList trg_l = new ArrayList();
			while (enum.hasMoreElements())
			{
				NSNode loadedNode =  (NSNode)enum.nextElement();
				NSName ns = loadedNode.getNSName();
				// Get type of node
				NSType node_type = loadedNode.getNodeTypeObj();
				String str_type = node_type.toString();
			
				if (str_type.equals("webMethods/trigger"))
				{			
					al.add(ns.toString());		
		
					trg_data = loadedNode.getAsData();
					// Find trigger data in node and update it
					IDataCursor idc = trg_data.getCursor();
					idc.first("trigger");
					IData node_data = (IData)idc.getValue();
					IDataCursor idc_node = node_data.getCursor();
					if (idc_node.first("docsPrefetchMax"))
					{
						tmp = (String)idc_node.getValue();
						int docsMax = Integer.parseInt(tmp);
						if (docsMax > 0)
						{
							trigger_enabled = "true";
							trg_l.add(trigger_enabled);
						}
						else
						{
							trigger_enabled = "false";
							trg_l.add(trigger_enabled);
						}
					}
		
		
				}
			} // end while loop
			// Save list to array and return 
			trigger_list = new String[al.size()];
			trg_info_list = new IData[al.size()];
			for (int i = 0; i < trigger_list.length; i++)
			{
				trigger_list[i] = (String)al.get(i);
				// Setup trigger info structure
				trg_info = IDataFactory.create();
				IDataCursor dc = trg_info.getCursor();
				IDataUtil.put( dc, "trigger_name", trigger_list[i] );
				IDataUtil.put( dc, "status", (String)trg_l.get(i) );
				IDataUtil.put( dc, "package", package_1 );
				trg_info_list[i] = trg_info;
				dc.destroy();
			}
			
		}
		catch(Exception e)
		{
			throw new ServiceException(e.getMessage());
		}
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		
		//trigger_list[0] = "trigger_list";
		
		IDataUtil.put( pipelineCursor_1, "trigger_list", trigger_list );
		IDataUtil.put( pipelineCursor_1, "trigger_info", trg_info_list );
		pipelineCursor_1.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void setTrigger (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(setTrigger)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required package
		// [i] field:0:required trigger_flag {"true","false"}
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	package_1 = IDataUtil.getString( pipelineCursor, "package" );
			String	trigger_flag = IDataUtil.getString( pipelineCursor, "trigger_flag" );
		pipelineCursor.destroy();
		
		// Check if package is system package! All system packages must be ignored by this service
		if (isSystemPackage(package_1))
			return;
		
		if (trigger_flag == null || trigger_flag.length() == 0)
			trigger_flag = "true"; // Default enable trigger
		
		com.wm.app.b2b.server.Package pkg = com.wm.app.b2b.server.PackageManager.getPackage(package_1);
		
		IData trg_data = null;
		String node_dir ="";
		File f_node = null;
		String tmp = "";
		boolean reload = false;
		try
		{
			// Get loaded nodes based on namespace
			Vector vc = com.wm.app.b2b.server.PackageManager.getNodes(pkg);
			Enumeration enum = vc.elements();
			
			while (enum.hasMoreElements())
			{
				NSNode loadedNode =  (NSNode)enum.nextElement();
				NSName ns = loadedNode.getNSName();
			
		
				// Get type of node
				NSType node_type = loadedNode.getNodeTypeObj();
				String str_type = node_type.toString();
				
				if (str_type.equals("webMethods/trigger"))
				{			
					trg_data = loadedNode.getAsData();
					// Find trigger data in node and update it
					IDataCursor idc = trg_data.getCursor();
					idc.first("trigger");
					IData node_data = (IData)idc.getValue();
					IDataCursor idc_node = node_data.getCursor();
					if (idc_node.first("deliverEnabled"))
					{
						tmp = (String)idc_node.getValue();
						if (!tmp.equals(trigger_flag))
						{
							IDataUtil.put(idc_node,"deliverEnabled",trigger_flag);
							reload = true;
						}
					}
					if (idc_node.first("executeEnabled"))
					{
						tmp = (String)idc_node.getValue();
						if (!tmp.equals(trigger_flag))
						{
							IDataUtil.put(idc_node,"executeEnabled",trigger_flag);
							reload = true;
						}
					}
		
					if (reload)
					{
						// Get subdirectory of node file
						f_node = pkg.getNodeSubDir(ns);
						node_dir = f_node.getCanonicalPath();
						node_dir += File.separator + "node.ndf";
		
			
						// Update node file data
						writeNodeToFile(node_dir, trg_data);
		
						// Load Node into Server
						loadedNode.setFromData(trg_data);
						Namespace.current().putNode(loadedNode, false, pkg);
					}
				}
			} // end while loop
		}
		catch(Exception e)
		{
			throw new ServiceException(e.getMessage());
		}
		
		
		
		// --- <<IS-END>> ---

                
	}



	public static final void start (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(start)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		try
		{
			InboundBrokerDocDispatcher _inboundBrokerDocDispatcher = InboundBrokerDocDispatcher.getDefault();
			_inboundBrokerDocDispatcher.start();
		}
		catch(Exception e)
		{
		throw new ServiceException(e.getMessage());
		}
		// --- <<IS-END>> ---

                
	}



	public static final void startTrigger (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(startTrigger)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required package
		// [i] field:0:required triggername
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	package_1 = IDataUtil.getString( pipelineCursor, "package" );
			String	triggername = IDataUtil.getString( pipelineCursor, "triggername" );
		pipelineCursor.destroy();
		
		com.wm.app.b2b.server.Package pkg = com.wm.app.b2b.server.PackageManager.getPackage(package_1);
		
		IData trg_data = null;
		String node_dir ="";
		File f_node = null;
		String tmp = "";
		boolean reload = false;
		try
		{
			// Get loaded nodes based on namespace
			Vector vc = com.wm.app.b2b.server.PackageManager.getNodes(pkg);
			Enumeration enum = vc.elements();
		
			while (enum.hasMoreElements())
			{
				NSNode loadedNode =  (NSNode)enum.nextElement();
				NSName ns = loadedNode.getNSName();
			
		
				// Get type of node
				NSType node_type = loadedNode.getNodeTypeObj();
				String str_type = node_type.toString();
				
				if (str_type.equals("webMethods/trigger") && ns.toString().equals(triggername))
				{
					trg_data = loadedNode.getAsData();
					// Find trigger data in node and update it
					IDataCursor idc = trg_data.getCursor();
					idc.first("trigger");
					IData node_data = (IData)idc.getValue();
					IDataCursor idc_node = node_data.getCursor();
		
					if (idc_node.first("processingSuspended"))
					{
						IDataUtil.put(idc_node,"processingSuspended","false");
						reload = true;
					}
					if (idc_node.first("retrievalSuspended"))
					{
						IDataUtil.put(idc_node,"retrievalSuspended","false");
						reload = true;
					}
					
					if (idc_node.first("docsPrefetchMax"))
					{
						tmp = (String)idc_node.getValue();
						int docsMax = Integer.parseInt(tmp);
						//if (docsMax == 0)
						//{
							tmp = (String)trg_obj.get(triggername);
							IDataUtil.put(idc_node,"docsPrefetchMax",tmp);
							reload = true;
						//}
					}
					
					if (reload)
					{
						// Get subdirectory of node file
						f_node = pkg.getNodeSubDir(ns);
						node_dir = f_node.getCanonicalPath();
						node_dir += File.separator + "node.ndf";
		
						// Update node file data
						writeNodeToFile(node_dir, trg_data);
		
						// Load Node into Server
						loadedNode.setFromData(trg_data);
						Namespace.current().putNode(loadedNode, false, pkg);
					}
				}
			} // end while loop
			//if (reload)
				//com.wm.app.b2b.server.PackageManager.loadPackage(package_1,true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		// --- <<IS-END>> ---

                
	}



	public static final void stop (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stop)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		try
		{
		InboundBrokerDocDispatcher _inboundBrokerDocDispatcher = InboundBrokerDocDispatcher.getDefault();
		_inboundBrokerDocDispatcher.stop();
		}
		catch(Exception e)
		{
			throw new ServiceException(e.getMessage());
		}
		// --- <<IS-END>> ---

                
	}



	public static final void stopTrigger (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stopTrigger)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required package
		// [i] field:0:required triggername
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	package_1 = IDataUtil.getString( pipelineCursor, "package" );
			String	triggername = IDataUtil.getString( pipelineCursor, "triggername" );
		pipelineCursor.destroy();
		
		com.wm.app.b2b.server.Package pkg = com.wm.app.b2b.server.PackageManager.getPackage(package_1);
		
		IData trg_data = null;
		String node_dir ="";
		File f_node = null;
		String tmp = "";
		boolean reload = false;
		try
		{
			// Get loaded nodes based on namespace
			Vector vc = com.wm.app.b2b.server.PackageManager.getNodes(pkg);
			Enumeration enum = vc.elements();
		
			while (enum.hasMoreElements())
			{
				NSNode loadedNode =  (NSNode)enum.nextElement();
				NSName ns = loadedNode.getNSName();
			
				// Get type of node
				NSType node_type = loadedNode.getNodeTypeObj();
				String str_type = node_type.toString();
				//System.out.println(ns.toString() + " Type - " + str_type);
				if (str_type.equals("webMethods/trigger") && ns.toString().equals(triggername))
				{
					trg_data = loadedNode.getAsData();
					// Find trigger data in node and update it
					IDataCursor idc = trg_data.getCursor();
					idc.first("trigger");
					IData node_data = (IData)idc.getValue();
					IDataCursor idc_node = node_data.getCursor();
					if (idc_node.first("processingSuspended"))
					{
						tmp = (String)idc_node.getValue();
						IDataUtil.put(idc_node,"processingSuspended","true");
						reload = true;
					}
					if (idc_node.first("retrievalSuspended"))
					{
						tmp = (String)idc_node.getValue();
						IDataUtil.put(idc_node,"retrievalSuspended","true");
						reload = true;
					}
					
					if (idc_node.first("docsPrefetchMax"))
					{
						tmp = (String)idc_node.getValue();
						int docsMax = Integer.parseInt(tmp);
						if (docsMax > 0)
						{
							trg_obj.put(triggername,tmp);
						}
							IDataUtil.put(idc_node,"docsPrefetchMax","0");
							reload = true;
						//}
					}
					
					if (reload)
					{
						// Get subdirectory of node file
						f_node = pkg.getNodeSubDir(ns);
						node_dir = f_node.getCanonicalPath();
						node_dir += File.separator + "node.ndf";
		
						// Update node file data
						writeNodeToFile(node_dir, trg_data);
		
						// Load Node into Server
						loadedNode.setFromData(trg_data);
						Namespace.current().putNode(loadedNode, true, pkg);
					}
				}
			} // end while loop
			if (reload)
				com.wm.app.b2b.server.PackageManager.loadPackage(package_1,true);
		}
		catch(Exception e)
		{
			throw new ServiceException(e.getMessage());
		}
		
		
		// --- <<IS-END>> ---

                
	}



	public static final void test (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(test)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		
		
		com.wm.app.b2b.server.Package pkg = com.wm.app.b2b.server.PackageManager.getPackage("Test");
		
		IData trg_data = null;
		String node_dir ="";
		File f_node = null;
		String tmp = "";
		boolean reload = false;
		try
		{
			// Get loaded nodes based on namespace
			Vector vc = com.wm.app.b2b.server.PackageManager.getNodes(pkg);
			Enumeration enum = vc.elements();
			System.out.println("### Start ###");
			while (enum.hasMoreElements())
			{
				NSNode loadedNode =  (NSNode)enum.nextElement();
				NSName ns = loadedNode.getNSName();
			
		
				// Get type of node
				NSType node_type = loadedNode.getNodeTypeObj();
				String str_type = node_type.toString();
				
				if (str_type.equals("webMethods/trigger"))
				{
					System.out.println(ns.toString() + " Type - " + str_type);
		
					trg_data = loadedNode.getAsData();
					// Find trigger data in node and update it
					IDataCursor idc = trg_data.getCursor();
					idc.first("trigger");
					IData node_data = (IData)idc.getValue();
					IDataCursor idc_node = node_data.getCursor();
					if (idc_node.first("docsPrefetchMax"))
					{
						//tmp = (String)idc_node.getValue();
						//if (tmp.equals("true"))
						//{
							IDataUtil.put(idc_node,"docsPrefetchMax","0");
							reload = true;
						//}
					}
					if (idc_node.first("docsPrefetchMin"))
					{
						//tmp = (String)idc_node.getValue();
						//if (tmp.equals("true"))
						//{
							IDataUtil.put(idc_node,"docsPrefetchMin","0");
							reload = true;
						//}
					}
		
					if (reload)
					{
						// Get subdirectory of node file
						f_node = pkg.getNodeSubDir(ns);
						node_dir = f_node.getCanonicalPath();
						node_dir += File.separator + "node.ndf";
		
						System.out.println("#### node file - " + node_dir);
		
						// Update node file data
						writeNodeToFile(node_dir, trg_data);
		
						// Load Node into Server
						loadedNode.setFromData(trg_data);
						Namespace.current().putNode(loadedNode, false, pkg);
					}
				}
			} // end while loop
			//if (reload)
				//com.wm.app.b2b.server.PackageManager.loadPackage("Test",true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	private static String file_name = "packages/TriggerManager/config/trigger.txt";
	private static String prop_name = "trigger.nameList";
	private static int docsPrefetchMin = 0;
	private static int docsPrefetchMax = 0;
	private static Hashtable trg_obj = new Hashtable();
	
	public static void writeNodeToFile(String file, IData idata)
	{
		// Write node file
		try
		{
			XMLCoder coder = new XMLCoder();
			FileOutputStream ostream = new FileOutputStream(file);
			coder.encode(ostream,Values.use(idata));
			ostream.close();
			ostream = null;
			coder = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean isSystemPackage(String pkg)
	{
		boolean ret = false;
		String str = pkg.substring(0,2);
		if (str.equals("Wm") || pkg.equals("TriggerManager") || pkg.equals("Default"))
			ret = true;
	
		return ret;
	}
	
	private static void setProperty(String property_name, String property_value)
	{
		try
		{
				// Get server properties
				Properties config = new Properties();
				InputStream in_stream = (InputStream) new FileInputStream(file_name);
				config.load(in_stream);
				config.setProperty(property_name, property_value);
				// Stor properties to file
				OutputStream out = (OutputStream) new FileOutputStream(file_name);
				config.store(out, "Server property");
				out.flush();
				out.close();
				in_stream.close();
		}
		catch(Exception e)
		{}
	}
	
	private static String getProperty(String property_name)
	{
		String str = "";
		
		try 
		{
	
			Properties config = new Properties();
			InputStream in_stream = (InputStream) new FileInputStream(file_name);
			config.load(in_stream);
			in_stream.close();
			str = config.getProperty(property_name);
			// clean up
			config = null;
			in_stream = null;
			
		}
		catch(Exception e)
		{
	
		}
		return str;
	}
	
	private static String[] getNameList(String str)
	{
		String[] ret = new String[0];
		StringTokenizer st = new StringTokenizer(str,";");
		ret = new String[st.countTokens()];
		int i = 0;
		while(st.hasMoreTokens())
		{
			ret[i] = st.nextToken();
			i++;
		}
		return ret;
	}
	
	private static boolean removeValue(String name)
	{
		StringBuffer tmp = new StringBuffer();
		boolean rc = true;
		String str = getProperty(prop_name);
		String[] list = getNameList(str);
		for (int i = 0; i < list.length; i++)
		{
			if (!name.equals(list[i]))
			{
				tmp.append(list[i]);
				tmp.append(";")	;	
			}
		}
		setProperty(prop_name, tmp.toString());
	
		return rc;
	}
	// --- <<IS-END-SHARED>> ---
}

