package model;


/**
 * Created by prasanth on 8/5/2017.
 */
public class ReplicaErrorInfo {

    public ServerErrorInfo DVLserverErrorInfo ;
    public ServerErrorInfo KKLserverErrorInfo ;
    public ServerErrorInfo WSTserverErrorInfo ;

    public ReplicaErrorInfo() {
        DVLserverErrorInfo = new ServerErrorInfo();
        KKLserverErrorInfo = new ServerErrorInfo();
        WSTserverErrorInfo = new ServerErrorInfo();
    }
}
