package com.qualcomm.robotcore.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Looper;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class WifiDirectAssistant {
    private static WifiDirectAssistant f596a;
    private final List<WifiP2pDevice> f597b;
    private Context f598c;
    private boolean f599d;
    private final IntentFilter f600e;
    private final Channel f601f;
    private final WifiP2pManager f602g;
    private C0134d f603h;
    private final C0131a f604i;
    private final C0133c f605j;
    private final C0132b f606k;
    private int f607l;
    private ConnectStatus f608m;
    private Event f609n;
    private String f610o;
    private String f611p;
    private InetAddress f612q;
    private String f613r;
    private String f614s;
    private String f615t;
    private boolean f616u;
    private int f617v;
    private WifiDirectAssistantCallback f618w;

    /* renamed from: com.qualcomm.robotcore.wifi.WifiDirectAssistant.1 */
    class C01281 implements ActionListener {
        final /* synthetic */ WifiDirectAssistant f587a;

        C01281(WifiDirectAssistant wifiDirectAssistant) {
            this.f587a = wifiDirectAssistant;
        }

        public void onSuccess() {
            this.f587a.m342a(Event.DISCOVERING_PEERS);
            RobotLog.m327d("Wifi Direct discovering peers");
        }

        public void onFailure(int reason) {
            String failureReasonToString = WifiDirectAssistant.failureReasonToString(reason);
            this.f587a.f607l = reason;
            RobotLog.m331w("Wifi Direct failure while trying to discover peers - reason: " + failureReasonToString);
            this.f587a.m342a(Event.ERROR);
        }
    }

    /* renamed from: com.qualcomm.robotcore.wifi.WifiDirectAssistant.2 */
    class C01292 implements ActionListener {
        final /* synthetic */ WifiDirectAssistant f588a;

        C01292(WifiDirectAssistant wifiDirectAssistant) {
            this.f588a = wifiDirectAssistant;
        }

        public void onSuccess() {
            this.f588a.f608m = ConnectStatus.GROUP_OWNER;
            this.f588a.m342a(Event.GROUP_CREATED);
            RobotLog.m327d("Wifi Direct created group");
        }

        public void onFailure(int reason) {
            if (reason == 2) {
                RobotLog.m327d("Wifi Direct cannot create group, does group already exist?");
                return;
            }
            String failureReasonToString = WifiDirectAssistant.failureReasonToString(reason);
            this.f588a.f607l = reason;
            RobotLog.m331w("Wifi Direct failure while trying to create group - reason: " + failureReasonToString);
            this.f588a.f608m = ConnectStatus.ERROR;
            this.f588a.m342a(Event.ERROR);
        }
    }

    /* renamed from: com.qualcomm.robotcore.wifi.WifiDirectAssistant.3 */
    class C01303 implements ActionListener {
        final /* synthetic */ WifiDirectAssistant f589a;

        C01303(WifiDirectAssistant wifiDirectAssistant) {
            this.f589a = wifiDirectAssistant;
        }

        public void onSuccess() {
            RobotLog.m327d("WifiDirect connect started");
            this.f589a.m342a(Event.CONNECTING);
        }

        public void onFailure(int reason) {
            String failureReasonToString = WifiDirectAssistant.failureReasonToString(reason);
            this.f589a.f607l = reason;
            RobotLog.m327d("WifiDirect connect cannot start - reason: " + failureReasonToString);
            this.f589a.m342a(Event.ERROR);
        }
    }

    public enum ConnectStatus {
        NOT_CONNECTED,
        CONNECTING,
        CONNECTED,
        GROUP_OWNER,
        ERROR
    }

    public enum Event {
        DISCOVERING_PEERS,
        PEERS_AVAILABLE,
        GROUP_CREATED,
        CONNECTING,
        CONNECTED_AS_PEER,
        CONNECTED_AS_GROUP_OWNER,
        DISCONNECTED,
        CONNECTION_INFO_AVAILABLE,
        ERROR
    }

    public interface WifiDirectAssistantCallback {
        void onWifiDirectEvent(Event event);
    }

    /* renamed from: com.qualcomm.robotcore.wifi.WifiDirectAssistant.a */
    private class C0131a implements ConnectionInfoListener {
        final /* synthetic */ WifiDirectAssistant f592a;

        private C0131a(WifiDirectAssistant wifiDirectAssistant) {
            this.f592a = wifiDirectAssistant;
        }

        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            this.f592a.f602g.requestGroupInfo(this.f592a.f601f, this.f592a.f606k);
            this.f592a.f612q = info.groupOwnerAddress;
            RobotLog.m327d("Group owners address: " + this.f592a.f612q.toString());
            if (info.groupFormed && info.isGroupOwner) {
                RobotLog.m327d("Wifi Direct group formed, this device is the group owner (GO)");
                this.f592a.f608m = ConnectStatus.GROUP_OWNER;
                this.f592a.m342a(Event.CONNECTED_AS_GROUP_OWNER);
            } else if (info.groupFormed) {
                RobotLog.m327d("Wifi Direct group formed, this device is a client");
                this.f592a.f608m = ConnectStatus.CONNECTED;
                this.f592a.m342a(Event.CONNECTED_AS_PEER);
            } else {
                RobotLog.m327d("Wifi Direct group NOT formed, ERROR: " + info.toString());
                this.f592a.f607l = 0;
                this.f592a.f608m = ConnectStatus.ERROR;
                this.f592a.m342a(Event.ERROR);
            }
        }
    }

    /* renamed from: com.qualcomm.robotcore.wifi.WifiDirectAssistant.b */
    private class C0132b implements GroupInfoListener {
        final /* synthetic */ WifiDirectAssistant f593a;

        private C0132b(WifiDirectAssistant wifiDirectAssistant) {
            this.f593a = wifiDirectAssistant;
        }

        public void onGroupInfoAvailable(WifiP2pGroup group) {
            if (group != null) {
                if (group.isGroupOwner()) {
                    this.f593a.f613r = this.f593a.f610o;
                    this.f593a.f614s = this.f593a.f611p;
                } else {
                    WifiP2pDevice owner = group.getOwner();
                    this.f593a.f613r = owner.deviceAddress;
                    this.f593a.f614s = owner.deviceName;
                }
                this.f593a.f615t = group.getPassphrase();
                this.f593a.f615t = this.f593a.f615t != null ? this.f593a.f615t : BuildConfig.VERSION_NAME;
                RobotLog.m330v("Wifi Direct connection information available");
                this.f593a.m342a(Event.CONNECTION_INFO_AVAILABLE);
            }
        }
    }

    /* renamed from: com.qualcomm.robotcore.wifi.WifiDirectAssistant.c */
    private class C0133c implements PeerListListener {
        final /* synthetic */ WifiDirectAssistant f594a;

        private C0133c(WifiDirectAssistant wifiDirectAssistant) {
            this.f594a = wifiDirectAssistant;
        }

        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            this.f594a.f597b.clear();
            this.f594a.f597b.addAll(peerList.getDeviceList());
            RobotLog.m330v("Wifi Direct peers found: " + this.f594a.f597b.size());
            for (WifiP2pDevice wifiP2pDevice : this.f594a.f597b) {
                RobotLog.m330v("    peer: " + wifiP2pDevice.deviceAddress + " " + wifiP2pDevice.deviceName);
            }
            this.f594a.m342a(Event.PEERS_AVAILABLE);
        }
    }

    /* renamed from: com.qualcomm.robotcore.wifi.WifiDirectAssistant.d */
    private class C0134d extends BroadcastReceiver {
        final /* synthetic */ WifiDirectAssistant f595a;

        private C0134d(WifiDirectAssistant wifiDirectAssistant) {
            this.f595a = wifiDirectAssistant;
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.wifi.p2p.STATE_CHANGED".equals(action)) {
                this.f595a.f599d = intent.getIntExtra("wifi_p2p_state", -1) == 2;
                RobotLog.m327d("Wifi Direct state - enabled: " + this.f595a.f599d);
            } else if ("android.net.wifi.p2p.PEERS_CHANGED".equals(action)) {
                RobotLog.m327d("Wifi Direct peers changed");
                this.f595a.f602g.requestPeers(this.f595a.f601f, this.f595a.f605j);
            } else if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(action)) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                WifiP2pInfo wifiP2pInfo = (WifiP2pInfo) intent.getParcelableExtra("wifiP2pInfo");
                RobotLog.m327d("Wifi Direct connection changed - connected: " + networkInfo.isConnected());
                if (networkInfo.isConnected()) {
                    this.f595a.f602g.requestConnectionInfo(this.f595a.f601f, this.f595a.f604i);
                    this.f595a.f602g.stopPeerDiscovery(this.f595a.f601f, null);
                    return;
                }
                this.f595a.f608m = ConnectStatus.NOT_CONNECTED;
                if (!this.f595a.f616u) {
                    this.f595a.discoverPeers();
                }
                if (this.f595a.isConnected()) {
                    this.f595a.m342a(Event.DISCONNECTED);
                }
                this.f595a.f616u = wifiP2pInfo.groupFormed;
            } else if ("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(action)) {
                RobotLog.m327d("Wifi Direct this device changed");
                this.f595a.m341a((WifiP2pDevice) intent.getParcelableExtra("wifiP2pDevice"));
            }
        }
    }

    static {
        f596a = null;
    }

    public static synchronized WifiDirectAssistant getWifiDirectAssistant(Context context) {
        WifiDirectAssistant wifiDirectAssistant;
        synchronized (WifiDirectAssistant.class) {
            if (f596a == null) {
                f596a = new WifiDirectAssistant(context);
            }
            wifiDirectAssistant = f596a;
        }
        return wifiDirectAssistant;
    }

    private WifiDirectAssistant(Context context) {
        this.f597b = new ArrayList();
        this.f598c = null;
        this.f599d = false;
        this.f607l = 0;
        this.f608m = ConnectStatus.NOT_CONNECTED;
        this.f609n = null;
        this.f610o = BuildConfig.VERSION_NAME;
        this.f611p = BuildConfig.VERSION_NAME;
        this.f612q = null;
        this.f613r = BuildConfig.VERSION_NAME;
        this.f614s = BuildConfig.VERSION_NAME;
        this.f615t = BuildConfig.VERSION_NAME;
        this.f616u = false;
        this.f617v = 0;
        this.f618w = null;
        this.f598c = context;
        this.f600e = new IntentFilter();
        this.f600e.addAction("android.net.wifi.p2p.STATE_CHANGED");
        this.f600e.addAction("android.net.wifi.p2p.PEERS_CHANGED");
        this.f600e.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
        this.f600e.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
        this.f602g = (WifiP2pManager) context.getSystemService("wifip2p");
        this.f601f = this.f602g.initialize(context, Looper.getMainLooper(), null);
        this.f603h = new C0134d();
        this.f604i = new C0131a();
        this.f605j = new C0133c();
        this.f606k = new C0132b();
    }

    public synchronized void enable() {
        this.f617v++;
        RobotLog.m330v("There are " + this.f617v + " Wifi Direct Assistant Clients (+)");
        if (this.f617v == 1) {
            RobotLog.m330v("Enabling Wifi Direct Assistant");
            if (this.f603h == null) {
                this.f603h = new C0134d();
            }
            this.f598c.registerReceiver(this.f603h, this.f600e);
        }
    }

    public synchronized void disable() {
        this.f617v--;
        RobotLog.m330v("There are " + this.f617v + " Wifi Direct Assistant Clients (-)");
        if (this.f617v == 0) {
            RobotLog.m330v("Disabling Wifi Direct Assistant");
            this.f602g.stopPeerDiscovery(this.f601f, null);
            this.f602g.cancelConnect(this.f601f, null);
            try {
                this.f598c.unregisterReceiver(this.f603h);
            } catch (IllegalArgumentException e) {
            }
            this.f609n = null;
        }
    }

    public synchronized boolean isEnabled() {
        return this.f617v > 0;
    }

    public ConnectStatus getConnectStatus() {
        return this.f608m;
    }

    public List<WifiP2pDevice> getPeers() {
        return new ArrayList(this.f597b);
    }

    public WifiDirectAssistantCallback getCallback() {
        return this.f618w;
    }

    public void setCallback(WifiDirectAssistantCallback callback) {
        this.f618w = callback;
    }

    public String getDeviceMacAddress() {
        return this.f610o;
    }

    public String getDeviceName() {
        return this.f611p;
    }

    public InetAddress getGroupOwnerAddress() {
        return this.f612q;
    }

    public String getGroupOwnerMacAddress() {
        return this.f613r;
    }

    public String getGroupOwnerName() {
        return this.f614s;
    }

    public String getPassphrase() {
        return this.f615t;
    }

    public boolean isWifiP2pEnabled() {
        return this.f599d;
    }

    public boolean isConnected() {
        return this.f608m == ConnectStatus.CONNECTED || this.f608m == ConnectStatus.GROUP_OWNER;
    }

    public boolean isGroupOwner() {
        return this.f608m == ConnectStatus.GROUP_OWNER;
    }

    public void discoverPeers() {
        this.f602g.discoverPeers(this.f601f, new C01281(this));
    }

    public void cancelDiscoverPeers() {
        RobotLog.m327d("Wifi Direct stop discovering peers");
        this.f602g.stopPeerDiscovery(this.f601f, null);
    }

    public void createGroup() {
        this.f602g.createGroup(this.f601f, new C01292(this));
    }

    public void removeGroup() {
        this.f602g.removeGroup(this.f601f, null);
    }

    public void connect(WifiP2pDevice peer) {
        if (this.f608m == ConnectStatus.CONNECTING || this.f608m == ConnectStatus.CONNECTED) {
            RobotLog.m327d("WifiDirect connection request to " + peer.deviceAddress + " ignored, already connected");
            return;
        }
        RobotLog.m327d("WifiDirect connecting to " + peer.deviceAddress);
        this.f608m = ConnectStatus.CONNECTING;
        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = peer.deviceAddress;
        wifiP2pConfig.wps.setup = 0;
        wifiP2pConfig.groupOwnerIntent = 1;
        this.f602g.connect(this.f601f, wifiP2pConfig, new C01303(this));
    }

    private void m341a(WifiP2pDevice wifiP2pDevice) {
        this.f611p = wifiP2pDevice.deviceName;
        this.f610o = wifiP2pDevice.deviceAddress;
        RobotLog.m330v("Wifi Direct device information: " + this.f611p + " " + this.f610o);
    }

    public String getFailureReason() {
        return failureReasonToString(this.f607l);
    }

    public static String failureReasonToString(int reason) {
        switch (reason) {
            case SpiSlaveResponseEvent.OK /*0*/:
                return "ERROR";
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                return "P2P_UNSUPPORTED";
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                return "BUSY";
            default:
                return "UNKNOWN (reason " + reason + ")";
        }
    }

    private void m342a(Event event) {
        if (this.f609n != event || this.f609n == Event.PEERS_AVAILABLE) {
            this.f609n = event;
            if (this.f618w != null) {
                this.f618w.onWifiDirectEvent(event);
            }
        }
    }
}
