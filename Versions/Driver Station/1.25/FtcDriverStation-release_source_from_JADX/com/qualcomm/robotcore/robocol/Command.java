package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolParsable.MsgType;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Comparator;

public class Command implements RobocolParsable, Comparable<Command>, Comparator<Command> {
    public static final int MAX_COMMAND_LENGTH = 256;
    private static final Charset f701h;
    String f702a;
    String f703b;
    byte[] f704c;
    byte[] f705d;
    long f706e;
    boolean f707f;
    byte f708g;

    static {
        f701h = Charset.forName("UTF-8");
    }

    public Command(String name) {
        this(name, BuildConfig.VERSION_NAME);
    }

    public Command(String name, String extra) {
        this.f707f = false;
        this.f708g = (byte) 0;
        this.f702a = name;
        this.f703b = extra;
        this.f704c = TypeConversion.stringToUtf8(this.f702a);
        this.f705d = TypeConversion.stringToUtf8(this.f703b);
        this.f706e = generateTimestamp();
        if (this.f704c.length > MAX_COMMAND_LENGTH) {
            throw new IllegalArgumentException(String.format("command name length is too long (MAX: %d)", new Object[]{Integer.valueOf(MAX_COMMAND_LENGTH)}));
        } else if (this.f705d.length > MAX_COMMAND_LENGTH) {
            throw new IllegalArgumentException(String.format("command extra data length is too long (MAX: %d)", new Object[]{Integer.valueOf(MAX_COMMAND_LENGTH)}));
        }
    }

    public Command(byte[] byteArray) throws RobotCoreException {
        this.f707f = false;
        this.f708g = (byte) 0;
        fromByteArray(byteArray);
    }

    public void acknowledge() {
        this.f707f = true;
    }

    public boolean isAcknowledged() {
        return this.f707f;
    }

    public String getName() {
        return this.f702a;
    }

    public String getExtra() {
        return this.f703b;
    }

    public byte getAttempts() {
        return this.f708g;
    }

    public long getTimestamp() {
        return this.f706e;
    }

    public MsgType getRobocolMsgType() {
        return MsgType.COMMAND;
    }

    public byte[] toByteArray() throws RobotCoreException {
        if (this.f708g != 127) {
            this.f708g = (byte) (this.f708g + 1);
        }
        short length = (short) ((this.f704c.length + 11) + this.f705d.length);
        ByteBuffer allocate = ByteBuffer.allocate(length + 3);
        try {
            allocate.put(getRobocolMsgType().asByte());
            allocate.putShort(length);
            allocate.putLong(this.f706e);
            allocate.put((byte) (this.f707f ? 1 : 0));
            allocate.put((byte) this.f704c.length);
            allocate.put(this.f704c);
            allocate.put((byte) this.f705d.length);
            allocate.put(this.f705d);
        } catch (Exception e) {
            RobotLog.logStacktrace(e);
        }
        return allocate.array();
    }

    public void fromByteArray(byte[] byteArray) throws RobotCoreException {
        boolean z = true;
        ByteBuffer wrap = ByteBuffer.wrap(byteArray, 3, byteArray.length - 3);
        this.f706e = wrap.getLong();
        if (wrap.get() != (byte) 1) {
            z = false;
        }
        this.f707f = z;
        this.f704c = new byte[TypeConversion.unsignedByteToInt(wrap.get())];
        wrap.get(this.f704c);
        this.f702a = TypeConversion.utf8ToString(this.f704c);
        this.f705d = new byte[TypeConversion.unsignedByteToInt(wrap.get())];
        wrap.get(this.f705d);
        this.f703b = TypeConversion.utf8ToString(this.f705d);
    }

    public String toString() {
        return String.format("command: %20d %5s %s", new Object[]{Long.valueOf(this.f706e), Boolean.valueOf(this.f707f), this.f702a});
    }

    public boolean equals(Object o) {
        if (o instanceof Command) {
            Command command = (Command) o;
            if (this.f702a.equals(command.f702a) && this.f706e == command.f706e) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return (int) (((long) this.f702a.hashCode()) & this.f706e);
    }

    public int compareTo(Command another) {
        int compareTo = this.f702a.compareTo(another.f702a);
        if (compareTo != 0) {
            return compareTo;
        }
        if (this.f706e < another.f706e) {
            return -1;
        }
        if (this.f706e > another.f706e) {
            return 1;
        }
        return 0;
    }

    public int compare(Command c1, Command c2) {
        return c1.compareTo(c2);
    }

    public static long generateTimestamp() {
        return System.nanoTime();
    }
}
