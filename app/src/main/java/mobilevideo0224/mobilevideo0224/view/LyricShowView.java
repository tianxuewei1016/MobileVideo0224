package mobilevideo0224.mobilevideo0224.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;

import mobilevideo0224.mobilevideo0224.base.Lyric;

/**
 * 作者：田学伟 on 2017/5/26 13:43
 * QQ：93226539
 * 作用：自定义显示歌词的控件
 */

public class LyricShowView extends TextView {
    private Paint paint;
    private int width;
    private int height;
    private ArrayList<Lyric> lyrics;

    public LyricShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView() {
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setTextSize(16);
        //设置居中
        paint.setTextAlign(Paint.Align.CENTER);

        //准备歌词
        lyrics = new ArrayList<>();
        Lyric lyric = new Lyric();
        for (int i = 0; i < 1000; i++) {
            //不同歌词
            lyric.setContent("aaaaaaa_" + i);
            lyric.setSleepTime(2000);
            lyric.setTimePoint(2000 * i);
            //添加到集合
            lyrics.add(lyric);
            //重新创建新对象
            lyric = new Lyric();
        }
    }

    /**
     * 绘制歌词
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("没有找到歌词..", width / 2, height / 2, paint);
    }
}
