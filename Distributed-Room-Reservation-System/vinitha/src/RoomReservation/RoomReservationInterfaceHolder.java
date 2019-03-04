package RoomReservation;

/**
 * Holder class for : RoomReservationInterface
 * 
 * @author OpenORB Compiler
 */
final public class RoomReservationInterfaceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal RoomReservationInterface value
     */
    public RoomReservation.RoomReservationInterface value;

    /**
     * Default constructor
     */
    public RoomReservationInterfaceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public RoomReservationInterfaceHolder(RoomReservation.RoomReservationInterface initial)
    {
        value = initial;
    }

    /**
     * Read RoomReservationInterface from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = RoomReservationInterfaceHelper.read(istream);
    }

    /**
     * Write RoomReservationInterface into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        RoomReservationInterfaceHelper.write(ostream,value);
    }

    /**
     * Return the RoomReservationInterface TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return RoomReservationInterfaceHelper.type();
    }

}
