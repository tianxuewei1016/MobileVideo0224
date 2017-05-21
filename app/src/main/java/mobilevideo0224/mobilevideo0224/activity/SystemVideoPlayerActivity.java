package mobilevideo0224.mobilevideo0224.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mobilevideo0224.mobilevideo0224.R;
import mobilevideo0224.mobilevideo0224.bean.MediaItem;
import mobilevideo0224.mobilevideo0224.utils.Utils;

public class SystemVideoPlayerActivity extends AppCompatActivity {

    /**
     * 视频进度更新
     */
    private static final int PROGRESS = 0;
    /**
     * 隐藏控制面板
     */
    private static final int HIDE_MEDIACONTROLLER = 1;

    @InjectView(R.id.vv)
    VideoView vv;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.iv_battery)
    ImageView ivBattery;
    @InjectView(R.id.tv_systetime)
    TextView tvSystetime;
    @InjectView(R.id.btn_voice)
    Button btnVoice;
    @InjectView(R.id.seekbar_voice)
    SeekBar seekbarVoice;
    @InjectView(R.id.btn_swiche_player)
    Button btnSwichePlayer;
    @InjectView(R.id.ll_top)
    LinearLayout llTop;
    @InjectView(R.id.tv_currenttime)
    TextView tvCurrenttime;
    @InjectView(R.id.seekbar_video)
    SeekBar seekbarVideo;
    @InjectView(R.id.tv_duration)
    TextView tvDuration;
    @InjectView(R.id.btn_exit)
    Button btnExit;
    @InjectView(R.id.btn_pre)
    Button btnPre;
    @InjectView(R.id.btn_start_pause)
    Button btnStartPause;
    @InjectView(R.id.btn_next)
    Button btnNext;
    @InjectView(R.id.btn_swich_screen)
    Button btnSwichScreen;
    @InjectView(R.id.ll_bottom)
    LinearLayout llBottom;
    @InjectView(R.id.activity_system_video_player)
    RelativeLayout activitySystemVideoPlayer;

    private Utils utils;
    private MyBroadCastReceiver receiver;

    private Uri uri;
    private ArrayList<MediaItem> mediaItems;
    /**
     * 视频列表的显示
     */
    private int position;
    /**
     * 手势识别器
     */
    private GestureDetector detector;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    //得到当前的进度
                    int currentPosition = vv.getCurrentPosition();
                    //让SeekBar进度更新
                    seekbarVideo.setProgress(currentPosition);

                    //设置文本的播放速度
                    tvCurrenttime.setText(utils.stringForTime(currentPosition));

                    //得到系统的时间
                    tvSystetime.setText(getSystemTime());

                    //循环发消息
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;

                case HIDE_MEDIACONTROLLER://隐藏控制面板
                    hideMediaController();
                    break;

            }
        }
    };

    /**
     * 得到系统的时间
     *
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        ButterKnife.inject(this);
        vv = (VideoView) findViewById(R.id.vv);

        initData();

        setListener();

        getData();

        setData();


        //设置控制面板
        //vv.setMediaController(new MediaController(this));
    }

    private void setData() {
        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());
            vv.setVideoPath(mediaItem.getData());
        } else if (uri != null) {
            //设置播放的地址
            vv.setVideoURI(uri);
        }

        setButtonStatus();

    }

    private void setButtonStatus() {
        if (mediaItems != null && mediaItems.size() > 0) {
            //有视频播放
            setEnable(true);

            if (position == 0) {
                btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnPre.setEnabled(false);
            }

            if (position == mediaItems.size() - 1) {
                btnNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnNext.setEnabled(false);
            }
        } else if (uri != null) {
            //上一个和下一个不可用点击
            setEnable(false);
        }
    }

    private void setEnable(boolean b) {
        if (b) {
            //上一个和下一个都可以点击
            btnPre.setBackgroundResource(R.drawable.btn_pre_selector);
            btnNext.setBackgroundResource(R.drawable.btn_next_selector);
        } else {
            //上一个和下一个灰色，并且不可用点击
            btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnNext.setBackgroundResource(R.drawable.btn_next_gray);
        }
        btnPre.setEnabled(b);
        btnNext.setEnabled(b);
    }

    private void getData() {
        //得到播放地址
        uri = getIntent().getData();
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);

    }

    private void initData() {
        utils = new Utils();

        //注册监听点亮变化的广播
        receiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //监听点亮变化
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, intentFilter);

        //实例化手势识别器
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            /**
             * 长按的
             * @param e
             */
            @Override
            public void onLongPress(MotionEvent e) {
                setStartOrPause();
                super.onLongPress(e);
            }

            /**
             * 双击的
             * @param e
             * @return
             */
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(SystemVideoPlayerActivity.this, "双击了", Toast.LENGTH_SHORT).show();
                return super.onDoubleTap(e);
            }

            /**
             * 单击的
             * @param e
             * @return
             */
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if(isShowMediaController) {
                    hideMediaController();
                    handler.removeMessages(HIDE_MEDIACONTROLLER);
                }else{
                    showMediaController();
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
                }
                return super.onSingleTapConfirmed(e);
            }
        });
    }

    /**
     * 触摸事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //把事件交给手势识别器去解析
        detector.onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }

    /**
     * 是否显示控制面板
     */
    private  boolean isShowMediaController = false;

    /**
     * 隐藏控制面板
     */
    private void hideMediaController(){
        llBottom.setVisibility(View.INVISIBLE);
        llTop.setVisibility(View.GONE);
        isShowMediaController = false;
    }

    private void showMediaController(){
        llBottom.setVisibility(View.VISIBLE);
        llTop.setVisibility(View.VISIBLE);
        isShowMediaController = true;
    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);//主线程
            setBatteryView(level);
        }
    }

    /**
     * 设置点亮的量化,对应的都是点亮的图片
     *
     * @param level
     */
    private void setBatteryView(int level) {
        if (level <= 0) {
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    private void setListener() {
        //设置播放器的三个监听:播放准备好的监听,播放完成的监听,播放出错的监听
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            //底层准备播放完成的时候回调
            @Override
            public void onPrepared(MediaPlayer mp) {
                //得到视频的总时间=长
                int duration = vv.getDuration();
                seekbarVideo.setMax(duration);

                //设置文本总时间
                tvDuration.setText(utils.stringForTime(duration));

                vv.start();

                //发消息开始更新播放速度
                handler.sendEmptyMessage(PROGRESS);

                //默认隐藏
                hideMediaController();
            }
        });

        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemVideoPlayerActivity.this, "播放出错了...", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            //如果是列表就播放下一个,如果是最后一个就退出当前页面
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Toast.makeText(SystemVideoPlayerActivity.this, "视频播放完成", Toast.LENGTH_SHORT).show();
                //finish();
                setNextVideo();
            }
        });

        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 状态变化的时候时候回调
             * @param seekBar
             * @param progress 当前改变的进度-要拖动到的位置
             * @param fromUser 用户导致的改变true,否则false
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    vv.seekTo(progress);
                }
            }

            /**
             * 当手指按下的时候回调
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                handler.removeMessages(HIDE_MEDIACONTROLLER);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);

            }
        });
    }

    private void setPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position--;
            if (position > 0) {
                //还是在列表范围的内容
                MediaItem mediaItem = mediaItems.get(position);
                vv.setVideoPath(mediaItem.getData());
                tvName.setText(mediaItem.getName());

                //设置电池的状态
                setButtonStatus();
            }
        }
    }

    /**
     * 播放下一个视频
     */
    private void setNextVideo() {
        //1.判断一下列表
        if (mediaItems != null && mediaItems.size() > 0) {
            position++;
            if (position < mediaItems.size()) {
                MediaItem mediaItem = mediaItems.get(position);
                vv.setVideoPath(mediaItem.getData());
                tvName.setText(mediaItem.getName());

                //设置按钮的状态
                setButtonStatus();
            } else {
                Toast.makeText(this, "退出播放器", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @OnClick({R.id.btn_voice, R.id.btn_swiche_player, R.id.btn_exit, R.id.btn_pre, R.id.btn_start_pause, R.id.btn_next, R.id.btn_swich_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_voice:
                break;
            case R.id.btn_swiche_player:
                break;
            case R.id.btn_exit:
                finish();
                break;
            case R.id.btn_pre:
                setPreVideo();
                break;
            case R.id.btn_start_pause:
                setStartOrPause();

                break;
            case R.id.btn_next:
                setNextVideo();
                break;
            case R.id.btn_swich_screen:
                break;
        }
        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
    }

    private void setStartOrPause() {
        if (vv.isPlaying()) {
            //暂停
            vv.pause();
            //按钮状态--播放
            btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
        } else {
            //播放
            vv.start();
            //按钮状态--暂停
            btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
        }
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            //把所有消息移除
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        //取消注册
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }
}
