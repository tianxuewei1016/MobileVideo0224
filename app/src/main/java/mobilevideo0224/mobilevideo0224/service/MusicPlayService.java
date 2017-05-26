package mobilevideo0224.mobilevideo0224.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import mobilevideo0224.mobilevideo0224.IMusicPlayService;
import mobilevideo0224.mobilevideo0224.bean.MediaItem;

public class MusicPlayService extends Service {

    private IMusicPlayService.Stub stub = new IMusicPlayService.Stub() {
        MusicPlayService service = MusicPlayService.this;

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public String getArtistName() throws RemoteException {
            return service.getArtistName();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return service.getAudioName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mediaPlayer.isPlaying();
        }
    };

    public MusicPlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private ArrayList<MediaItem> mediaItems;

    private MediaPlayer mediaPlayer;

    private int position;

    private MediaItem mediaItem;

    public static final String OPEN_COMPLETE = "com.atguigu.mobileplayer.OPEN_COMPLETE";

    @Override
    public void onCreate() {
        super.onCreate();
        //加载列表数据
        getData();
    }

    /**
     * 得到数据
     */
    private void getData() {
        new Thread() {
            public void run() {
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//视频在sdcard上的名称
                        MediaStore.Audio.Media.DURATION,//视频时长
                        MediaStore.Audio.Media.SIZE,//视频文件的大小
                        MediaStore.Audio.Media.DATA,//视频播放地址
                        MediaStore.Audio.Media.ARTIST//艺术家
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        Log.e("TAG", "name==" + name + ",duration==" + duration + ",data===" + data + ",artist==" + artist);

                        if (duration > 10 * 1000) {
                            mediaItems.add(new MediaItem(name, duration, size, data, artist));
                        }
                    }

                    cursor.close();
                }
            }
        }.start();
    }

    /**
     * 根据位置播放一个音频
     *
     * @param position
     */
    private void openAudio(int position) {
        this.position = position;
        if (mediaItems != null && mediaItems.size() > 0) {

            if (position < mediaItems.size()) {
                mediaItem = mediaItems.get(position);

                //如果不为空释放之前的播放音频的资源
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    mediaPlayer = null;
                }
                try {
                    mediaPlayer = new MediaPlayer();
                    //设置播放地址
                    mediaPlayer.setDataSource(mediaItem.getData());
                    //设置最基本的三个监听：准备完成，播放出错，播放完成
                    mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                    mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                    mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                    //准备
                    mediaPlayer.prepareAsync();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(MusicPlayService.this, "音频还没有加载完成", Toast.LENGTH_SHORT).show();
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            //发广播
            notifyChange(OPEN_COMPLETE);
            start();
        }
    }

    /**
     * 发送广播
     * @param action
     */
    private void notifyChange(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    /**
     * 播放音频
     */
    private void start() {
        //开始播放
        mediaPlayer.start();
    }

    /**
     * 暂停音频
     */
    private void pause() {
        mediaPlayer.pause();
    }

    /**
     * 得到演唱者
     *
     * @return
     */
    private String getArtistName() {
        return mediaItem.getArtist();
    }

    /**
     * 得到歌曲名
     *
     * @return
     */
    private String getAudioName() {
        return mediaItem.getName();
    }


    /**
     * 得到歌曲路径
     *
     * @return
     */
    private String getAudioPath() {
        return "";
    }

    /**
     * 得到总时长
     *
     * @return
     */
    private int getDuration() {
        return mediaPlayer.getDuration();
    }


    /**
     * 得到当前播放进度
     *
     * @return
     */
    private int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * +     * 音频拖动
     * +     *
     * +     * @param position
     * +
     */
    private void seekTo(int position) {
    }

    /**
     * 播放下一个
     */
    private void next() {
    }

    /**
     * 播放上一个
     */
    private void pre() {
    }
}