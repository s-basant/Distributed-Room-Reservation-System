package admin;


/**
* admin/ListHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from AdminIDL.idl
* Friday, December 1, 2017 1:25:00 PM EST
*/

public final class ListHolder implements org.omg.CORBA.portable.Streamable
{
  public String value[] = null;

  public ListHolder ()
  {
  }

  public ListHolder (String[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = admin.ListHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    admin.ListHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return admin.ListHelper.type ();
  }

}