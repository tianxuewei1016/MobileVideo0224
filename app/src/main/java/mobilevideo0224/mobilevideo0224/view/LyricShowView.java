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
    private Paint paintGreen;
    private Paint paintWhite;
    private int width;
    private int height;
    private ArrayList<Lyric> lyrics;
    /**
     * 表示的是贺词列表中的哪一句
     */
    private int index;
    private float textHeight = 20;

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
        paintGreen = new Paint();
        paintGreen.setColor(Color.GREEN);
        paintGreen.setAntiAlias(true);
        paintGreen.setTextSize(16);
        //设置居中
        paintGreen.setTextAlign(Paint.Align.CENTER);

        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);
        paintWhite.setAntiAlias(true);
        paintWhite.setTextSize(16);
        //设置居中
        paintWhite.setTextAlign(Paint.Align.CENTER);
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
        if(lyrics!=null&&lyrics.size()>0) {
            //有歌词
            //当前-中心的那一句
            String currentContent = lyrics.get(index).getContent();
            canvas.drawText(currentContent, width / 2, height / 2, paintGreen);
            //得到中间句的坐标
            float tempY = height/2;
            //绘制前面部分
            for (int i = index - 1; i >= 0; i--) {
                //得到前一部分的歌词内容
                String preContent = lyrics.get(i).getContent();
                tempY = tempY - textHeight;
                if(tempY<0) {
                    break;
                }
                canvas.drawText(preContent, width / 2, tempY, paintWhite);
            }
            tempY = height/2;
            //绘制后面的部分
            for (int i = index +1;i < lyrics.size();i++){
                //得到后一部分的内容
                String nextContent = lyrics.get(i).getContent();
                tempY = tempY + textHeight;
                if(tempY > height) {
                    break;
                }
                //绘制内容
                canvas.drawText(nextContent,width / 2, tempY, paintWhite);
            }

        }else{
            canvas.drawText("没有找到歌词..", width / 2, height / 2, paintGreen);
        }
    }
}