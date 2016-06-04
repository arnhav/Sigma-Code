package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.RobotLog;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.net.SocketException;

public class RobocolDatagramSocket {
    private final byte[] f496a;
    private DatagramSocket f497b;
    private final DatagramPacket f498c;
    private final RobocolDatagram f499d;
    private volatile State f500e;

    public enum State {
        LISTENING,
        CLOSED,
        ERROR
    }

    public RobocolDatagramSocket() {
        this.f496a = new byte[RobocolConfig.MAX_PACKET_SIZE];
        this.f498c = new DatagramPacket(this.f496a, this.f496a.length);
        this.f499d = new RobocolDatagram();
        this.f500e = State.CLOSED;
    }

    public void listen(InetAddress destAddress) throws SocketException {
        bind(new InetSocketAddress(RobocolConfig.determineBindAddress(destAddress), RobocolConfig.PORT_NUMBER));
    }

    public void bind(InetSocketAddress bindAddress) throws SocketException {
        if (this.f500e != State.CLOSED) {
            close();
        }
        this.f500e = State.LISTENING;
        RobotLog.m327d("RobocolDatagramSocket binding to " + bindAddress.toString());
        this.f497b = new DatagramSocket(bindAddress);
    }

    public void connect(InetAddress connectAddress) throws SocketException {
        SocketAddress inetSocketAddress = new InetSocketAddress(connectAddress, RobocolConfig.PORT_NUMBER);
        RobotLog.m327d("RobocolDatagramSocket connected to " + inetSocketAddress.toString());
        this.f497b.connect(inetSocketAddress);
    }

    public void close() {
        this.f500e = State.CLOSED;
        if (this.f497b != null) {
            this.f497b.close();
        }
        RobotLog.m327d("RobocolDatagramSocket is closed");
    }

    public void send(RobocolDatagram message) {
        try {
            this.f497b.send(message.getPacket());
        } catch (IllegalArgumentException e) {
            RobotLog.m331w("Unable to send RobocolDatagram: " + e.toString());
            RobotLog.m331w("               " + message.toString());
        } catch (IOException e2) {
            RobotLog.m331w("Unable to send RobocolDatagram: " + e2.toString());
            RobotLog.m331w("               " + message.toString());
        } catch (NullPointerException e3) {
            RobotLog.m331w("Unable to send RobocolDatagram: " + e3.toString());
            RobotLog.m331w("               " + message.toString());
        }
    }

    public RobocolDatagram recv() {
        try {
            this.f497b.receive(this.f498c);
        } catch (PortUnreachableException e) {
            RobotLog.m327d("RobocolDatagramSocket receive error: remote port unreachable");
            return null;
        } catch (IOException e2) {
            RobotLog.m327d("RobocolDatagramSocket receive error: " + e2.toString());
            return null;
        } catch (NullPointerException e3) {
            RobotLog.m327d("RobocolDatagramSocket receive error: " + e3.toString());
        }
        this.f499d.setPacket(this.f498c);
        return this.f499d;
    }

    public State getState() {
        return this.f500e;
    }

    public InetAddress getInetAddress() {
        if (this.f497b == null) {
            return null;
        }
        return this.f497b.getInetAddress();
    }

    public InetAddress getLocalAddress() {
        if (this.f497b == null) {
            return null;
        }
        return this.f497b.getLocalAddress();
    }

    public boolean isRunning() {
        return this.f500e == State.LISTENING;
    }

    public boolean isClosed() {
        return this.f500e == State.CLOSED;
    }
}
