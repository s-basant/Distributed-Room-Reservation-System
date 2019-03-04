package RoomReservation;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

/**
 * Interface definition: RoomReservationInterface.
 * 
 * @author OpenORB Compiler
 */
public class _RoomReservationInterfaceStub extends org.omg.CORBA.portable.ObjectImpl
        implements RoomReservationInterface
{
    static final String[] _ids_list =
    {
        "IDL:RoomReservation/RoomReservationInterface:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = RoomReservation.RoomReservationInterfaceOperations.class;

    /**
     * Operation fnBookRoom
     */
    public String fnBookRoom(String StudentId, String campus, String dRoomNumber, String dtDate, String dtToTimeSlott)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("fnBookRoom",true);
                    _output.write_string(StudentId);
                    _output.write_string(campus);
                    _output.write_string(dRoomNumber);
                    _output.write_string(dtDate);
                    _output.write_string(dtToTimeSlott);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("fnBookRoom",_opsClass);
                if (_so == null)
                   continue;
                RoomReservation.RoomReservationInterfaceOperations _self = (RoomReservation.RoomReservationInterfaceOperations) _so.servant;
                try
                {
                    return _self.fnBookRoom( StudentId,  campus,  dRoomNumber,  dtDate,  dtToTimeSlott);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation fnDeleteRoom
     */
    public boolean fnDeleteRoom(String adminId, String date, String roomNumber, String timeSlots)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("fnDeleteRoom",true);
                    _output.write_string(adminId);
                    _output.write_string(date);
                    _output.write_string(roomNumber);
                    _output.write_string(timeSlots);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("fnDeleteRoom",_opsClass);
                if (_so == null)
                   continue;
                RoomReservation.RoomReservationInterfaceOperations _self = (RoomReservation.RoomReservationInterfaceOperations) _so.servant;
                try
                {
                    return _self.fnDeleteRoom( adminId,  date,  roomNumber,  timeSlots);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation fnGetAvailableTimeSlot
     * @throws IOException 
     * @throws UnknownHostException 
     * @throws RemoteException 
     */
    public String fnGetAvailableTimeSlot(String campus, String dtdate) throws RemoteException, UnknownHostException, IOException
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("fnGetAvailableTimeSlot",true);
                    _output.write_string(campus);
                    _output.write_string(dtdate);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("fnGetAvailableTimeSlot",_opsClass);
                if (_so == null)
                   continue;
                RoomReservation.RoomReservationInterfaceOperations _self = (RoomReservation.RoomReservationInterfaceOperations) _so.servant;
                try
                {
                    return _self.fnGetAvailableTimeSlot( campus,  dtdate);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation fnCancelBooking
     */
    public boolean fnCancelBooking(String strBookingId, String StudentId)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("fnCancelBooking",true);
                    _output.write_string(strBookingId);
                    _output.write_string(StudentId);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("fnCancelBooking",_opsClass);
                if (_so == null)
                   continue;
                RoomReservation.RoomReservationInterfaceOperations _self = (RoomReservation.RoomReservationInterfaceOperations) _so.servant;
                try
                {
                    return _self.fnCancelBooking( strBookingId,  StudentId);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation fnCreateRoom
     */
    public boolean fnCreateRoom(String adminId, String date, String roomNumber, String timeSlots)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("fnCreateRoom",true);
                    _output.write_string(adminId);
                    _output.write_string(date);
                    _output.write_string(roomNumber);
                    _output.write_string(timeSlots);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("fnCreateRoom",_opsClass);
                if (_so == null)
                   continue;
                RoomReservation.RoomReservationInterfaceOperations _self = (RoomReservation.RoomReservationInterfaceOperations) _so.servant;
                try
                {
                    return _self.fnCreateRoom( adminId,  date,  roomNumber,  timeSlots);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation fnTransferRoomDetails
     */
    public String fnTransferRoomDetails(String strBookingId, String strStudentId, String strDate, String strCampusName, String strNewCampusName, String strRoomNumber, String strTimeSlot)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("fnTransferRoomDetails",true);
                    _output.write_string(strBookingId);
                    _output.write_string(strStudentId);
                    _output.write_string(strDate);
                    _output.write_string(strCampusName);
                    _output.write_string(strNewCampusName);
                    _output.write_string(strRoomNumber);
                    _output.write_string(strTimeSlot);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("fnTransferRoomDetails",_opsClass);
                if (_so == null)
                   continue;
                RoomReservation.RoomReservationInterfaceOperations _self = (RoomReservation.RoomReservationInterfaceOperations) _so.servant;
                try
                {
                    return _self.fnTransferRoomDetails( strBookingId,  strStudentId,  strDate,  strCampusName,  strNewCampusName,  strRoomNumber,  strTimeSlot);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
