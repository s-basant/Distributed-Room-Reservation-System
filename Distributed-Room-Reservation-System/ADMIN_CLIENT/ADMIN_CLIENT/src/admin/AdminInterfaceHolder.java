package admin;

/**
* admin/AdminInterfaceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from AdminIDL.idl
* Friday, December 1, 2017 1:25:00 PM EST
*/

public final class AdminInterfaceHolder implements org.omg.CORBA.portable.Streamable
{
  public admin.AdminInterface value = null;

  public AdminInterfaceHolder ()
  {
  }

  public AdminInterfaceHolder (admin.AdminInterface initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = admin.AdminInterfaceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    admin.AdminInterfaceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return admin.AdminInterfaceHelper.type ();
  }

}
