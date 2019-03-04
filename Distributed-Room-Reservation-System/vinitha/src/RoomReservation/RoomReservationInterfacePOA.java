package RoomReservation;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import org.omg.CORBA.portable.OutputStream;

/**
 * Interface definition: RoomReservationInterface.
 * 
 * @author OpenORB Compiler
 */
public abstract class RoomReservationInterfacePOA extends org.omg.PortableServer.Servant
        implements RoomReservationInterfaceOperations, org.omg.CORBA.portable.InvokeHandler
{
    public RoomReservationInterface _this()
    {
        return RoomReservationInterfaceHelper.narrow(_this_object());
    }

    public RoomReservationInterface _this(org.omg.CORBA.ORB orb)
    {
        return RoomReservationInterfaceHelper.narrow(_this_object(orb));
    }

    private static String [] _ids_list =
    {
        "IDL:RoomReservation/RoomReservationInterface:1.0"
    };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte [] objectId)
    {
        return _ids_list;
    }

    public final  OutputStream _invoke(final String opName,
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler)
    {

        if (opName.equals("fnBookRoom")) {
                return _invoke_fnBookRoom(_is, handler);
        } else if (opName.equals("fnCancelBooking")) {
                return _invoke_fnCancelBooking(_is, handler);
        } else if (opName.equals("fnCreateRoom")) {
                return _invoke_fnCreateRoom(_is, handler);
        } else if (opName.equals("fnDeleteRoom")) {
                return _invoke_fnDeleteRoom(_is, handler);
        } else if (opName.equals("fnGetAvailableTimeSlot")) {
                try {
					return _invoke_fnGetAvailableTimeSlot(_is, handler);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        } else if (opName.equals("fnTransferRoomDetails")) {
                return _invoke_fnTransferRoomDetails(_is, handler);
        } else {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }
		return null;
    }

    // helper methods
    private org.omg.CORBA.portable.OutputStream _invoke_fnBookRoom(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();
        String arg3_in = _is.read_string();
        String arg4_in = _is.read_string();

        String _arg_result = fnBookRoom(arg0_in, arg1_in, arg2_in, arg3_in, arg4_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_fnDeleteRoom(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();
        String arg3_in = _is.read_string();

        boolean _arg_result = fnDeleteRoom(arg0_in, arg1_in, arg2_in, arg3_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_fnGetAvailableTimeSlot(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) throws RemoteException, UnknownHostException, IOException {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();

        String _arg_result = fnGetAvailableTimeSlot(arg0_in, arg1_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_fnCancelBooking(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();

        boolean _arg_result = fnCancelBooking(arg0_in, arg1_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_fnCreateRoom(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();
        String arg3_in = _is.read_string();

        boolean _arg_result = fnCreateRoom(arg0_in, arg1_in, arg2_in, arg3_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_fnTransferRoomDetails(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();
        String arg3_in = _is.read_string();
        String arg4_in = _is.read_string();
        String arg5_in = _is.read_string();
        String arg6_in = _is.read_string();

        String _arg_result = fnTransferRoomDetails(arg0_in, arg1_in, arg2_in, arg3_in, arg4_in, arg5_in, arg6_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

}
