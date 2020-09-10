package com.java.liuyun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ShareFrame extends FrameLayout {
    public int width, height;
    public TextView title, summary;

    public ShareFrame(Context context) {
        super(context);
        this.width = 720;
        this.height = 720;
        View view = View.inflate(getContext(), R.layout.share_layout, this);
        this.title = (TextView) view.findViewById(R.id.share_title);
        this.summary = (TextView) view.findViewById(R.id.share_summary);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
    public void setSummary(String summary) {
        this.summary.setText(summary);
    }

    public Bitmap createImage() {

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        measure(widthMeasureSpec, heightMeasureSpec);
        layout(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        draw(canvas);

        return bitmap;
    }

}
