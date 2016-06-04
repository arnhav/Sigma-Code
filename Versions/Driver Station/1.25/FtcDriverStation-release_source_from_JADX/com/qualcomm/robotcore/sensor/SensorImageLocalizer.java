package com.qualcomm.robotcore.sensor;

import android.util.Log;
import com.qualcomm.robotcore.util.MatrixD;
import com.qualcomm.robotcore.util.Pose;
import com.qualcomm.robotcore.util.PoseUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorImageLocalizer extends SensorBase<Pose> implements SensorListener<List<TrackedTargetInfo>> {
    private final boolean f719a;
    private final String f720b;
    private final Map<String, TargetInfo> f721c;
    private Pose f722d;
    private final HashMap<String, C0114a> f723e;
    private C0114a f724f;

    /* renamed from: com.qualcomm.robotcore.sensor.SensorImageLocalizer.a */
    private class C0114a {
        public long f507a;
        public long f508b;
        public int f509c;
        public String f510d;
        public double f511e;
        final /* synthetic */ SensorImageLocalizer f512f;

        private C0114a(SensorImageLocalizer sensorImageLocalizer) {
            this.f512f = sensorImageLocalizer;
        }
    }

    public SensorImageLocalizer(List<SensorListener<Pose>> l) {
        super(l);
        this.f719a = false;
        this.f720b = "SensorImageLocalizer";
        this.f723e = new HashMap();
        this.f721c = new HashMap();
    }

    public boolean initialize() {
        return true;
    }

    public boolean shutdown() {
        return true;
    }

    public boolean resume() {
        return true;
    }

    public boolean pause() {
        return true;
    }

    public void AddListener(SensorListener<Pose> l) {
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(l)) {
                this.mListeners.add(l);
            }
        }
    }

    public void RemoveListener(SensorListener<Pose> l) {
        synchronized (this.mListeners) {
            if (this.mListeners.contains(l)) {
                this.mListeners.remove(l);
            }
        }
    }

    public boolean addTargetReference(String targetName, double xTrans, double yTrans, double zTrans, double angle, double longSideTransFromCenterToVertex, double shortSideTransFromCenterToVertex) {
        if (targetName == null) {
            throw new IllegalArgumentException("Null targetInfoWorldRef");
        } else if (this.f721c.containsKey(targetName)) {
            return false;
        } else {
            MatrixD makeRotationY = Pose.makeRotationY(Math.toRadians(angle));
            MatrixD matrixD = new MatrixD(3, 4);
            matrixD.setSubmatrix(makeRotationY, 3, 3, 0, 0);
            matrixD.data()[0][3] = yTrans;
            matrixD.data()[1][3] = zTrans;
            matrixD.data()[2][3] = xTrans;
            Pose pose = new Pose(matrixD);
            Log.d("SensorImageLocalizer", "Target Pose \n" + matrixD);
            this.f721c.put(targetName, new TargetInfo(targetName, pose, new TargetSize(targetName, longSideTransFromCenterToVertex, shortSideTransFromCenterToVertex)));
            return true;
        }
    }

    public boolean addRobotToCameraRef(double length, double width, double height, double angle) {
        MatrixD matrixD = new MatrixD(3, 3);
        MatrixD makeRotationY = Pose.makeRotationY(-angle);
        matrixD = new MatrixD(3, 4);
        matrixD.setSubmatrix(makeRotationY, 3, 3, 0, 0);
        matrixD.data()[0][3] = width;
        matrixD.data()[1][3] = -height;
        matrixD.data()[2][3] = length;
        this.f722d = new Pose(matrixD);
        return true;
    }

    public boolean removeTargetReference(String targetName) {
        if (targetName == null) {
            throw new IllegalArgumentException("Null targetName");
        } else if (!this.f721c.containsKey(targetName)) {
            return false;
        } else {
            this.f721c.remove(targetName);
            return true;
        }
    }

    private boolean m452a(TrackedTargetInfo trackedTargetInfo) {
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        C0114a c0114a;
        if (this.f723e.containsKey(trackedTargetInfo.mTargetInfo.mTargetName)) {
            c0114a = (C0114a) this.f723e.get(trackedTargetInfo.mTargetInfo.mTargetName);
            c0114a.f508b = trackedTargetInfo.mTimeTracked;
            c0114a.f511e = trackedTargetInfo.mConfidence;
            if (currentTimeMillis - c0114a.f508b > 120) {
                c0114a.f509c = 1;
            } else {
                c0114a.f509c++;
            }
        } else {
            c0114a = new C0114a();
            c0114a.f511e = trackedTargetInfo.mConfidence;
            c0114a.f510d = trackedTargetInfo.mTargetInfo.mTargetName;
            c0114a.f508b = trackedTargetInfo.mTimeTracked;
            c0114a.f509c = 1;
            this.f723e.put(trackedTargetInfo.mTargetInfo.mTargetName, c0114a);
        }
        if (this.f724f == null || this.f724f.f510d == r0.f510d || currentTimeMillis - this.f724f.f507a >= 10) {
            return true;
        }
        Log.d("SensorImageLocalizer", "Ignoring target " + trackedTargetInfo.mTargetInfo.mTargetName + " Time diff " + (currentTimeMillis - this.f724f.f507a));
        return false;
    }

    public void onUpdate(List<TrackedTargetInfo> targetPoses) {
        Log.d("SensorImageLocalizer", "SensorImageLocalizer onUpdate");
        if (targetPoses == null || targetPoses.size() < 1) {
            Log.d("SensorImageLocalizer", "SensorImageLocalizer onUpdate NULL");
            update(null);
            return;
        }
        Object obj = null;
        double d = Double.MIN_VALUE;
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        TrackedTargetInfo trackedTargetInfo = null;
        C0114a c0114a = null;
        for (TrackedTargetInfo trackedTargetInfo2 : targetPoses) {
            double d2;
            Object obj2;
            double d3;
            if (this.f721c.containsKey(trackedTargetInfo2.mTargetInfo.mTargetName)) {
                if (!m452a(trackedTargetInfo2) || trackedTargetInfo2.mConfidence <= d) {
                    Log.d("SensorImageLocalizer", "Ignoring target " + trackedTargetInfo2.mTargetInfo.mTargetName + " Confidence " + trackedTargetInfo2.mConfidence);
                } else {
                    c0114a = (C0114a) this.f723e.get(trackedTargetInfo2.mTargetInfo.mTargetName);
                    d2 = trackedTargetInfo2.mConfidence;
                    obj2 = 1;
                    Log.d("SensorImageLocalizer", "Potential target " + trackedTargetInfo2.mTargetInfo.mTargetName + " Confidence " + trackedTargetInfo2.mConfidence);
                    d3 = d2;
                    trackedTargetInfo = trackedTargetInfo2;
                    obj = obj2;
                    d = d3;
                }
            }
            TrackedTargetInfo trackedTargetInfo22 = trackedTargetInfo;
            d3 = d;
            obj2 = obj;
            d2 = d3;
            d3 = d2;
            trackedTargetInfo = trackedTargetInfo22;
            obj = obj2;
            d = d3;
        }
        if (obj == null) {
            update(null);
            return;
        }
        TargetInfo targetInfo = (TargetInfo) this.f721c.get(trackedTargetInfo.mTargetInfo.mTargetName);
        c0114a.f507a = currentTimeMillis;
        this.f724f = c0114a;
        Log.d("SensorImageLocalizer", "Selected target " + trackedTargetInfo.mTargetInfo.mTargetName + " time " + currentTimeMillis);
        MatrixD matrixD = null;
        if (this.f722d != null) {
            matrixD = this.f722d.poseMatrix.submatrix(3, 3, 0, 0);
        }
        MatrixD transpose = trackedTargetInfo.mTargetInfo.mTargetPose.poseMatrix.submatrix(3, 3, 0, 0).transpose();
        MatrixD submatrix = targetInfo.mTargetPose.poseMatrix.submatrix(3, 3, 0, 0);
        MatrixD times = Pose.makeRotationX(Math.toRadians(90.0d)).times(Pose.makeRotationY(Math.toRadians(90.0d)));
        MatrixD times2 = times.times(submatrix).times(transpose);
        if (matrixD != null) {
            matrixD = times2.times(matrixD);
        } else {
            matrixD = times2;
        }
        times2 = new MatrixD(3, 1);
        times2.data()[0][0] = targetInfo.mTargetSize.mLongSide;
        times2.data()[1][0] = targetInfo.mTargetSize.mShortSide;
        times2.data()[2][0] = 0.0d;
        MatrixD times3 = transpose.times(trackedTargetInfo.mTargetInfo.mTargetPose.getTranslationMatrix());
        MatrixD matrixD2 = new MatrixD(3, 1);
        if (this.f722d != null) {
            matrixD2 = this.f722d.getTranslationMatrix();
        }
        times = times.times(targetInfo.mTargetPose.getTranslationMatrix().subtract(submatrix.times(times3.add(transpose.times(matrixD2)).add(times2))));
        MatrixD matrixD3 = new MatrixD(3, 4);
        matrixD3.setSubmatrix(matrixD, 3, 3, 0, 0);
        matrixD3.setSubmatrix(times, 3, 1, 0, 3);
        Pose pose = new Pose(matrixD3);
        double[] anglesAroundZ = PoseUtils.getAnglesAroundZ(pose);
        Log.d("SensorImageLocalizer", String.format("POSE_HEADING: x %8.4f z %8.4f up %8.4f", new Object[]{Double.valueOf(anglesAroundZ[0]), Double.valueOf(anglesAroundZ[1]), Double.valueOf(anglesAroundZ[2])}));
        matrixD3 = pose.getTranslationMatrix();
        Log.d("SensorImageLocalizer", String.format("POSE_TRANS: x %8.4f y %8.4f z %8.4f", new Object[]{Double.valueOf(matrixD3.data()[0][0]), Double.valueOf(matrixD3.data()[1][0]), Double.valueOf(matrixD3.data()[2][0])}));
        update(pose);
    }
}
