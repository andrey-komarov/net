package lab3.net;

import lab3.main.World;
import lab3.proto.ProtocolConfig;
import lab3.structs.RevisionList;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;

public class RequestRevisionList implements Runnable {
    private final InetSocketAddress addr;
    private final World world;

    public RequestRevisionList(InetSocketAddress addr, World world) {
        this.addr = addr;
        this.world = world;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket();
            socket.connect(addr);
            System.err.println("REQ REVISION_LIST from " + addr);
            socket.getOutputStream().write(ProtocolConfig.GET_REVISION_LIST);
            Optional<RevisionList> oRList = RevisionList.loadFrom(socket.getInputStream());
            if (oRList.isPresent()) {
                world.acceptRevisionList(oRList.get(), addr.getAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
