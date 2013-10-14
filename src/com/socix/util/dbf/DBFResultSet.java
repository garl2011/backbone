package com.socix.util.dbf;


import java.util.HashMap;
import java.util.Map;


public class DBFResultSet
{
	private Object[] result;
	private DBFReader reader;
	private Map<String, Integer> map;
	
	public DBFResultSet(String name)
	{
		try
        {
			this.reader=new DBFReader(name);
			this.map=new HashMap<String, Integer>();
			int fieldCount=this.reader.getFieldCount();
			for(int i=0;i<fieldCount;i++)
			{
				this.map.put(reader.getField(i).getName(), new Integer(i));
			}
        }catch (DBFException e)
	    {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } 
	}
	
	public void close()
	{
		try
        {
	        this.reader.close();
        } catch (DBFException e)
        {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
	}
	
	public boolean next()
	{
		if(reader.hasNextRecord())
		{
			try
            {
	            result=reader.nextRecord();
	            //System.out.println(result.toString());
            } catch (DBFException e)
            {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            return false;
            }
			return true;
		}
		return false;
	}
	
	/************* get method **************/
	
	public String getString(String columnLabel)
	{
		return result[map.get(columnLabel)].toString();
	}
	
	public String getString(int columnIndex)
	{
		return result[columnIndex].toString();
	}
	
	public int getInt(String columnLabel)
	{
		return Integer.parseInt(result[map.get(columnLabel)].toString());
	}
	
	public int getInt(int columnIndex)
	{
		return Integer.parseInt(result[columnIndex].toString());
	}
	
	public double getDouble(String columnLabel)
	{
		return Double.parseDouble(result[map.get(columnLabel)].toString());
	}
	
	public double getDouble(int columnIndex)
	{
		return Double.parseDouble(result[columnIndex].toString());
	}
	
	public boolean getBoolean(String columnLabel)
	{
		return Boolean.parseBoolean(result[map.get(columnLabel)].toString());
	}
	
	public boolean getBoolean(int columnIndex)
	{
		return Boolean.parseBoolean(result[columnIndex].toString());
	}
	
	/****************** end *******************/
	
	public static void main(String[] args)
	{
		long start=System.currentTimeMillis();
		//DBFResultSet rs=new DBFResultSet("N:/REMOTE/DBF/SHOW2003.DBF");
		DBFResultSet rs=new DBFResultSet("d:/TMP_DB/SHOW2003.DBF");
		System.out.println(System.currentTimeMillis()-start);
		String time="";
		Double point=0.0;
		start=System.currentTimeMillis();
		int i=0;
	    while(rs.next())
	    {
	    	i++;
	    	
//	    	if(rs.getString("S1").equals("000000"))
//	    	{
//	    		time=rs.getString("S2");
//	    	}
//	    	//System.out.println(rs.getString(0));
//	    	if(rs.getString("S1").equals("600036"))
//	    	{
//	    		//System.out.println("ok");
//	    		point=rs.getDouble("S8");
//	    	}
	    }
	    rs.close();
	    System.out.println("时间:"+time+" 沪深300指数:"+point);
	    System.out.println(System.currentTimeMillis()-start+"毫秒");
	}
}
