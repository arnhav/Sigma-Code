package com.qualcomm.robotcore.robocol;

import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolParsable.MsgType;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteBuffer;

public class Heartbeat implements RobocolParsable {
    public static final short BUFFER_SIZE = (short) 14;
    public static final short MAX_SEQUENCE_NUMBER = (short) 10000;
    public static final short PAYLOAD_SIZE = (short) 11;
    private static short f709a;
    private long f710b;
    private short f711c;
    private RobotState f712d;

    /* renamed from: com.qualcomm.robotcore.robocol.Heartbeat.1 */
    static /* synthetic */ class C01101 {
        static final /* synthetic */ int[] f483a;

        static {
            f483a = new int[Token.values().length];
            try {
                f483a[Token.EMPTY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    public enum Token {
        EMPTY
    }

    static {
        f709a = (short) 0;
    }

    public Heartbeat() {
        this.f711c = m450a();
        this.f710b = System.nanoTime();
        this.f712d = RobotState.NOT_STARTED;
    }

    public Heartbeat(Token token) {
        switch (C01101.f483a[token.ordinal()]) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                this.f711c = (short) 0;
                this.f710b = 0;
                this.f712d = RobotState.NOT_STARTED;
            default:
        }
    }

    public long getTimestamp() {
        return this.f710b;
    }

    public double getElapsedTime() {
        return ((double) (System.nanoTime() - this.f710b)) / 1.0E9d;
    }

    public short getSequenceNumber() {
        return this.f711c;
    }

    public MsgType getRobocolMsgType() {
        return MsgType.HEARTBEAT;
    }

    public byte getRobotState() {
        return this.f712d.asByte();
    }

    public void setRobotState(RobotState state) {
        this.f712d = state;
    }

    public byte[] toByteArray() throws RobotCoreException {
        ByteBuffer allocate = ByteBuffer.allocate(14);
        try {
            allocate.put(getRobocolMsgType().asByte());
            allocate.putShort(PAYLOAD_SIZE);
            allocate.putShort(this.f711c);
            allocate.putLong(this.f710b);
            allocate.put(this.f712d.asByte());
        } catch (Exception e) {
            RobotLog.logStacktrace(e);
        }
        return allocate.array();
    }

    public void fromByteArray(byte[] byteArray) throws RobotCoreException {
        if (byteArray.length < 14) {
            throw new RobotCoreException("Expected buffer of at least 14 bytes, received " + byteArray.length);
        }
        ByteBuffer wrap = ByteBuffer.wrap(byteArray, 3, 11);
        this.f711c = wrap.getShort();
        this.f710b = wrap.getLong();
        this.f712d = RobotState.fromByte(wrap.get());
    }

    public String toString() {
        return String.format("Heartbeat - seq: %4d, time: %d", new Object[]{Short.valueOf(this.f711c), Long.valueOf(this.f710b)});
    }

    private static synchronized short m450a() {
        short s;
        synchronized (Heartbeat.class) {
            s = f709a;
            f709a = (short) (f709a + 1);
            if (f709a > MAX_SEQUENCE_NUMBER) {
                f709a = (short) 0;
            }
        }
        return s;
    }
}
