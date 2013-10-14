
package com.socix.util.dbf;

import java.io.*;


public class DBFReader
{

    public DBFReader(String s)
        throws DBFException
    {
        stream = null;
        fields = null;
        nextRecord = null;
        try
        {
            init(new FileInputStream(s));
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            throw new DBFException(filenotfoundexception);
        }
    }

    public DBFReader(InputStream inputstream)
        throws DBFException
    {
        stream = null;
        fields = null;
        nextRecord = null;
        init(inputstream);
    }

    private void init(InputStream inputstream)
        throws DBFException
    {
        try
        {
            stream = new DataInputStream(inputstream);
            int i = readHeader();
            fields = new DBFField[i];
            int j = 1;
            for(int k = 0; k < i; k++)
            {
                fields[k] = readFieldHeader();
                j += fields[k].getLength();
            }

            if(stream.read() < 1)
                throw new DBFException("Unexpected end of file reached.");
            nextRecord = new byte[j];
            try
            {
                stream.readFully(nextRecord);
            }
            catch(EOFException eofexception)
            {
                nextRecord = null;
                stream.close();
            }
        }
        catch(IOException ioexception)
        {
            throw new DBFException(ioexception);
        }
    }

    private int readHeader()
        throws IOException, DBFException
    {
        byte abyte0[] = new byte[16];
        try
        {
            stream.readFully(abyte0);
        }
        catch(EOFException eofexception)
        {
            throw new DBFException("Unexpected end of file reached.");
        }
        int i = abyte0[8];
        if(i < 0)
            i += 256;
        i += 256 * abyte0[9];
        i = --i / 32;
        i--;
        try
        {
            stream.readFully(abyte0);
        }
        catch(EOFException eofexception1)
        {
            throw new DBFException("Unexpected end of file reached.");
        }
        return i;
    }

    private DBFField readFieldHeader()
        throws IOException, DBFException
    {
        byte abyte0[] = new byte[16];
        try
        {
            stream.readFully(abyte0);
        }
        catch(EOFException eofexception)
        {
            throw new DBFException("Unexpected end of file reached.");
        }
        StringBuffer stringbuffer = new StringBuffer(10);
        for(int i = 0; i < 10; i++)
        {
            if(abyte0[i] == 0)
                break;
            stringbuffer.append((char)abyte0[i]);
        }

        char c = (char)abyte0[11];
        try
        {
            stream.readFully(abyte0);
        }
        catch(EOFException eofexception1)
        {
            throw new DBFException("Unexpected end of file reached.");
        }
        int j = abyte0[0];
        int k = abyte0[1];
        if(j < 0)
            j += 256;
        if(k < 0)
            k += 256;
        return new DBFField(stringbuffer.toString(), c, j, k);
    }

    public int getFieldCount()
    {
        return fields.length;
    }

    public DBFField getField(int i)
    {
        return fields[i];
    }

    public boolean hasNextRecord()
    {
        return nextRecord != null;
    }

    public Object[] nextRecord()
        throws DBFException
    {
        if(!hasNextRecord())
            throw new DBFException("No more records available.");
        Object aobj[] = new Object[fields.length];
        int i = 1;
        for(int j = 0; j < aobj.length; j++)
        {
            int k = fields[j].getLength();
            StringBuffer stringbuffer = new StringBuffer(k);
            stringbuffer.append(new String(nextRecord, i, k));
            aobj[j] = fields[j].parse(stringbuffer.toString());
            i += fields[j].getLength();
        }

        try
        {
            stream.readFully(nextRecord);
        }
        catch(EOFException eofexception)
        {
            nextRecord = null;
        }
        catch(IOException ioexception)
        {
            throw new DBFException(ioexception);
        }
        return aobj;
    }

    public void close()
        throws DBFException
    {
        nextRecord = null;
        try
        {
            stream.close();
        }
        catch(IOException ioexception)
        {
            throw new DBFException(ioexception);
        }
    }

    private DataInputStream stream;
    private DBFField fields[];
    private byte nextRecord[];
}
