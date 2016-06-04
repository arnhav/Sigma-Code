package com.qualcomm.robotcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.util.HashMap;

public class MapView extends View {
    MapView f546a;
    private int f547b;
    private int f548c;
    private int f549d;
    private int f550e;
    private Paint f551f;
    private Canvas f552g;
    private Bitmap f553h;
    private boolean f554i;
    private boolean f555j;
    private int f556k;
    private float f557l;
    private float f558m;
    private BitmapDrawable f559n;
    private int f560o;
    private int f561p;
    private int f562q;
    private boolean f563r;
    private HashMap<Integer, C0122a> f564s;
    private Bitmap f565t;

    /* renamed from: com.qualcomm.robotcore.util.MapView.a */
    private class C0122a {
        public int f540a;
        public int f541b;
        public int f542c;
        public int f543d;
        public boolean f544e;
        final /* synthetic */ MapView f545f;

        public C0122a(MapView mapView, int i, int i2, int i3, int i4, boolean z) {
            this.f545f = mapView;
            this.f540a = i;
            this.f541b = i2;
            this.f542c = i3;
            this.f543d = i4;
            this.f544e = z;
        }
    }

    protected void onSizeChanged(int x, int y, int oldx, int oldy) {
        this.f557l = ((float) getWidth()) / ((float) this.f547b);
        this.f558m = ((float) getHeight()) / ((float) this.f548c);
        this.f555j = true;
        redraw();
        Log.e("MapView", "Size changed");
    }

    public MapView(Context context) {
        super(context);
        this.f554i = false;
        this.f555j = false;
        this.f556k = 1;
        this.f563r = false;
        m319a();
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.f554i = false;
        this.f555j = false;
        this.f556k = 1;
        this.f563r = false;
        m319a();
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.f554i = false;
        this.f555j = false;
        this.f556k = 1;
        this.f563r = false;
        m319a();
    }

    private void m319a() {
        this.f551f = new Paint();
        this.f551f.setColor(-16777216);
        this.f551f.setStrokeWidth(Dimmer.MAXIMUM_BRIGHTNESS);
        this.f551f.setAntiAlias(true);
        this.f546a = this;
        this.f564s = new HashMap();
    }

    private int m318a(int i) {
        return i % 2 == 0 ? i : i + 1;
    }

    public void setup(int xMax, int yMax, int numLinesX, int numLinesY, Bitmap robotIcon) {
        this.f547b = xMax * 2;
        this.f548c = yMax * 2;
        this.f549d = this.f547b / m318a(numLinesX);
        this.f550e = this.f548c / m318a(numLinesY);
        this.f565t = robotIcon;
        this.f554i = true;
    }

    private void m321b() {
        this.f553h = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        this.f552g = new Canvas(this.f553h);
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setAntiAlias(true);
        this.f552g.drawRect(0.0f, 0.0f, (float) this.f552g.getWidth(), (float) this.f552g.getHeight(), paint);
        int i = 0;
        while (i < this.f548c) {
            float f = this.f558m * ((float) i);
            this.f552g.drawLine(0.0f, f, (float) this.f552g.getWidth(), f, this.f551f);
            i = this.f550e + i;
        }
        int i2 = 0;
        while (i2 < this.f547b) {
            float f2 = this.f557l * ((float) i2);
            this.f552g.drawLine(f2, 0.0f, f2, (float) this.f552g.getHeight(), this.f551f);
            i2 += this.f549d;
        }
    }

    private float m320b(int i) {
        return (((float) i) * this.f557l) + ((float) (getWidth() / 2));
    }

    private float m322c(int i) {
        return ((float) (getHeight() / 2)) - (((float) i) * this.f558m);
    }

    private int m324d(int i) {
        return 360 - i;
    }

    public void setRobotLocation(int x, int y, int angle) {
        this.f560o = -x;
        this.f561p = y;
        this.f562q = angle;
        this.f563r = true;
    }

    public int addMarker(int x, int y, int color) {
        int i = this.f556k;
        this.f556k = i + 1;
        this.f564s.put(Integer.valueOf(i), new C0122a(this, i, -x, y, color, true));
        return i;
    }

    public boolean removeMarker(int id) {
        if (this.f564s.remove(Integer.valueOf(id)) == null) {
            return false;
        }
        return true;
    }

    public int addDrawable(int x, int y, int resource) {
        int i = this.f556k;
        this.f556k = i + 1;
        this.f564s.put(Integer.valueOf(i), new C0122a(this, i, -x, y, resource, false));
        return i;
    }

    private void m323c() {
        for (C0122a c0122a : this.f564s.values()) {
            float b = m320b(c0122a.f541b);
            float c = m322c(c0122a.f542c);
            if (c0122a.f544e) {
                Paint paint = new Paint();
                paint.setColor(c0122a.f543d);
                this.f552g.drawCircle(b, c, 5.0f, paint);
            } else {
                Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), c0122a.f543d);
                this.f552g.drawBitmap(decodeResource, b - ((float) (decodeResource.getWidth() / 2)), c - ((float) (decodeResource.getHeight() / 2)), new Paint());
            }
        }
    }

    private void m325d() {
        float b = m320b(this.f560o);
        float c = m322c(this.f561p);
        int d = m324d(this.f562q);
        Matrix matrix = new Matrix();
        matrix.postRotate((float) d);
        matrix.postScale(0.2f, 0.2f);
        Bitmap bitmap = this.f565t;
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        this.f552g.drawBitmap(bitmap, b - ((float) (bitmap.getWidth() / 2)), c - ((float) (bitmap.getHeight() / 2)), new Paint());
    }

    public void redraw() {
        if (this.f554i && this.f555j) {
            m321b();
            m323c();
            if (this.f563r) {
                m325d();
            }
        }
        this.f559n = new BitmapDrawable(getResources(), this.f553h);
        this.f546a.setBackgroundDrawable(this.f559n);
    }
}
