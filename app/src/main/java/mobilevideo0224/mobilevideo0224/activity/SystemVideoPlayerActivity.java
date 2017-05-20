package mobilevideo0224.mobilevideo0224.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mobilevideo0224.mobilevideo0224.R;

public class SystemVideoPlayerActivity extends AppCompatActivity {

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

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        ButterKnife.inject(this);
        vv = (VideoView) findViewById(R.id.vv);

        //得到播放地址
        uri = getIntent().getData();
        //设置播放器的三个监听:播放准备好的监听,播放完成的监听,播放出错的监听
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            //底层准备播放完成的时候回调
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv.start();
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
                Toast.makeText(SystemVideoPlayerActivity.this, "视频播放完成", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //设置播放的地址
        vv.setVideoURI(uri);

        //设置控制面板
        vv.setMediaController(new MediaController(this));
    }

    @OnClick({R.id.btn_voice, R.id.btn_swiche_player, R.id.btn_exit, R.id.btn_pre, R.id.btn_start_pause, R.id.btn_next, R.id.btn_swich_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_voice:
                break;
            case R.id.btn_swiche_player:
                break;
            case R.id.btn_exit:
                break;
            case R.id.btn_pre:
                break;
            case R.id.btn_start_pause:
                break;
            case R.id.btn_next:
                break;
            case R.id.btn_swich_screen:
                break;
        }
    }
}
