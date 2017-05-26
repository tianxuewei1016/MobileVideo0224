package mobilevideo0224.mobilevideo0224.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mobilevideo0224.mobilevideo0224.IMusicPlayService;
import mobilevideo0224.mobilevideo0224.R;
import mobilevideo0224.mobilevideo0224.service.MusicPlayService;

public class SystemAudioPlayerActivity extends AppCompatActivity {

    @InjectView(R.id.iv_icon)
    ImageView ivIcon;
    @InjectView(R.id.tv_artist)
    TextView tvArtist;
    @InjectView(R.id.tv_audioname)
    TextView tvAudioname;
    @InjectView(R.id.rl_top)
    RelativeLayout rlTop;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.seekbar_audio)
    SeekBar seekbarAudio;
    @InjectView(R.id.btn_playmode)
    Button btnPlaymode;
    @InjectView(R.id.btn_pre)
    Button btnPre;
    @InjectView(R.id.btn_start_pause)
    Button btnStartPause;
    @InjectView(R.id.btn_next)
    Button btnNext;
    @InjectView(R.id.btn_lyric)
    Button btnLyric;
    @InjectView(R.id.ll_bottom)
    LinearLayout llBottom;
    /**
     * 这个就是IMusicPlayService.Stub的实例
     */
    private IMusicPlayService service;
    /**
     * 音乐的下标的位置
     */
    private int position;

    private ServiceConnection conon = new ServiceConnection() {
        /**
         * 当绑定服务成功后的回调
         * @param name
         * @param iBinder
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            //这个就是stub，stub包含很多方法，这些方法调用服务的方法
            service = IMusicPlayService.Stub.asInterface(iBinder);
            if (service != null) {
                try {
                    service.openAudio(position);//打开播放第0个音频
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 当断开连接的时候回调
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_audio_player);
        ButterKnife.inject(this);

        ivIcon.setBackgroundResource(R.drawable.animation_bg);
        AnimationDrawable drawable = (AnimationDrawable) ivIcon.getBackground();
        drawable.start();
        getData();
        startAndBindService();
    }

    private void getData() {
        position = getIntent().getIntExtra("position", 0);
    }

    private void startAndBindService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        //绑定-得到服务的操作对象-IMusicPlayService service
        bindService(intent,conon, Context.BIND_AUTO_CREATE);
        //防止多次实例化Service
        startService(intent);
    }

    @OnClick({R.id.btn_playmode, R.id.btn_pre, R.id.btn_start_pause, R.id.btn_next, R.id.btn_lyric})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_playmode:
                break;
            case R.id.btn_pre:
                break;
            case R.id.btn_start_pause:
                try {
                    if (service.isPlaying()) {
                        //暂停
                        service.pause();
                        //按钮状态--播放
                        btnStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                    } else {
                        //播放
                        service.start();
                        //按钮状态--暂停
                        btnStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_next:
                break;
            case R.id.btn_lyric:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conon != null) {
            unbindService(conon);
            conon = null;
        }
    }
}
