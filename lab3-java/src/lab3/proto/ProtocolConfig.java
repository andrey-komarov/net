package lab3.proto;

public class ProtocolConfig {
    public final static int UDP_PORT = 3012;
    public final static int TCP_PORT = 3013;

    public final static long HEARTBEAT_TIMEOUT = 10000;

    public final static byte GET_REVISION_LIST = 0x01;
    public final static byte GET_REVISION_FILES = 0x02;
    public final static byte GET_FILE = 0x03;

    public final static byte OK = 0x00;
    public final static byte DENIED = 0x01;
}
