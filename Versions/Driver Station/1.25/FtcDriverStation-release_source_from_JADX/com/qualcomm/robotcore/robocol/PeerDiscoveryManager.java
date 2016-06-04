package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.PeerDiscovery.PeerType;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PeerDiscoveryManager {
    private InetAddress f489a;
    private final RobocolDatagramSocket f490b;
    private ScheduledExecutorService f491c;
    private ScheduledFuture<?> f492d;
    private final PeerDiscovery f493e;

    /* renamed from: com.qualcomm.robotcore.robocol.PeerDiscoveryManager.a */
    private class C0112a implements Runnable {
        final /* synthetic */ PeerDiscoveryManager f488a;

        private C0112a(PeerDiscoveryManager peerDiscoveryManager) {
            this.f488a = peerDiscoveryManager;
        }

        public void run() {
            try {
                RobotLog.m330v("Sending peer discovery packet");
                RobocolDatagram robocolDatagram = new RobocolDatagram(this.f488a.f493e);
                if (this.f488a.f490b.getInetAddress() == null) {
                    robocolDatagram.setAddress(this.f488a.f489a);
                }
                this.f488a.f490b.send(robocolDatagram);
            } catch (RobotCoreException e) {
                RobotLog.m327d("Unable to send peer discovery packet: " + e.toString());
            }
        }
    }

    public PeerDiscoveryManager(RobocolDatagramSocket socket) {
        this.f493e = new PeerDiscovery(PeerType.PEER);
        this.f490b = socket;
    }

    public InetAddress getPeerDiscoveryDevice() {
        return this.f489a;
    }

    public void start(InetAddress peerDiscoveryDevice) {
        RobotLog.m330v("Starting peer discovery");
        if (peerDiscoveryDevice == this.f490b.getLocalAddress()) {
            RobotLog.m330v("No need for peer discovery, we are the peer discovery device");
            return;
        }
        if (this.f492d != null) {
            this.f492d.cancel(true);
        }
        this.f489a = peerDiscoveryDevice;
        this.f491c = Executors.newSingleThreadScheduledExecutor();
        this.f492d = this.f491c.scheduleAtFixedRate(new C0112a(), 1, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        RobotLog.m330v("Stopping peer discovery");
        if (this.f492d != null) {
            this.f492d.cancel(true);
        }
    }
}
