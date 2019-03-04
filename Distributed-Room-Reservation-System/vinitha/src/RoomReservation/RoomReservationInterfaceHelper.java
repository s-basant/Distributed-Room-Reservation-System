package RoomReservation;

/** 
 * Helper class for : RoomReservationInterface
 *  
 * @author OpenORB Compiler
 */ 
public class RoomReservationInterfaceHelper
{
    /**
     * Insert RoomReservationInterface into an any
     * @param a an any
     * @param t RoomReservationInterface value
     */
    public static void insert(org.omg.CORBA.Any a, RoomReservation.RoomReservationInterface t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract RoomReservationInterface from an any
     *
     * @param a an any
     * @return the extracted RoomReservationInterface value
     */
    public static RoomReservation.RoomReservationInterface extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return RoomReservation.RoomReservationInterfaceHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the RoomReservationInterface TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "RoomReservationInterface" );
        }
        return _tc;
    }

    /**
     * Return the RoomReservationInterface IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:RoomReservation/RoomReservationInterface:1.0";

    /**
     * Read RoomReservationInterface from a marshalled stream
     * @param istream the input stream
     * @return the readed RoomReservationInterface value
     */
    public static RoomReservation.RoomReservationInterface read(org.omg.CORBA.portable.InputStream istream)
    {
        return(RoomReservation.RoomReservationInterface)istream.read_Object(RoomReservation._RoomReservationInterfaceStub.class);
    }

    /**
     * Write RoomReservationInterface into a marshalled stream
     * @param ostream the output stream
     * @param value RoomReservationInterface value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, RoomReservation.RoomReservationInterface value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to RoomReservationInterface
     * @param obj the CORBA Object
     * @return RoomReservationInterface Object
     */
    public static RoomReservationInterface narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof RoomReservationInterface)
            return (RoomReservationInterface)obj;

        if (obj._is_a(id()))
        {
            _RoomReservationInterfaceStub stub = new _RoomReservationInterfaceStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to RoomReservationInterface
     * @param obj the CORBA Object
     * @return RoomReservationInterface Object
     */
    public static RoomReservationInterface unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof RoomReservationInterface)
            return (RoomReservationInterface)obj;

        _RoomReservationInterfaceStub stub = new _RoomReservationInterfaceStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
