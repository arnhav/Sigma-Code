package com.qualcomm.robotcore.robocol;

import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolParsable.MsgType;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteBuffer;

public class PeerDiscovery implements RobocolParsable {
    public static final short BUFFER_SIZE = (short) 13;
    public static final short PAYLOAD_SIZE = (short) 10;
    public static final byte ROBOCOL_VERSION = (byte) 1;
    private PeerType f713a;

    public enum PeerType {
        NOT_SET(0),
        PEER(1),
        GROUP_OWNER(2);
        
        private static final PeerType[] f485a;
        private int f487b;

        static {
            f485a = values();
        }

        public static PeerType fromByte(byte b) {
            PeerType peerType = NOT_SET;
            try {
                return f485a[b];
            } catch (ArrayIndexOutOfBoundsException e) {
                RobotLog.m331w(String.format("Cannot convert %d to Peer: %s", new Object[]{Byte.valueOf(b), e.toString()}));
                return peerType;
            }
        }

        private PeerType(int type) {
            this.f487b = type;
        }

        public byte asByte() {
            return (byte) this.f487b;
        }
    }

    public PeerDiscovery(PeerType peerType) {
        this.f713a = peerType;
    }

    public PeerType getPeerType() {
        return this.f713a;
    }

    public MsgType getRobocolMsgType() {
        return MsgType.PEER_DISCOVERY;
    }

    public byte[] toByteArray() throws RobotCoreException {
        ByteBuffer allocate = ByteBuffer.allocate(13);
        try {
            allocate.put(getRobocolMsgType().asByte());
            allocate.putShort(PAYLOAD_SIZE);
            allocate.put(ROBOCOL_VERSION);
            allocate.put(this.f713a.asByte());
        } catch (Exception e) {
            RobotLog.logStacktrace(e);
        }
        return allocate.array();
    }

    public void fromByteArray(byte[] byteArray) throws RobotCoreException {
        if (byteArray.length < 13) {
            throw new RobotCoreException("Expected buffer of at least 13 bytes, received " + byteArray.length);
        }
        ByteBuffer wrap = ByteBuffer.wrap(byteArray, 3, 10);
        switch (wrap.get()) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                this.f713a = PeerType.fromByte(wrap.get());
            default:
        }
    }

    public String toString() {
        return String.format("Peer Discovery - peer type: %s", new Object[]{this.f713a.name()});
    }
}
