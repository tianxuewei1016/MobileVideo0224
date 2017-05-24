package mobilevideo0224.mobilevideo0224.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xutils.image.ImageOptions;

import java.util.ArrayList;

import mobilevideo0224.mobilevideo0224.R;
import mobilevideo0224.mobilevideo0224.bean.MediaItem;

/**
 * 作者：田学伟 on 2017/5/22 20:07
 * QQ：93226539
 * 作用：网络视频的播放器
 */

public class NetVideoAdapter extends BaseAdapter{
    private final Context mContext;
    private final ArrayList<MediaItem> datas;

    private ImageOptions imageOptions;

    public NetVideoAdapter(Context mContext, ArrayList<MediaItem> mediaItems) {
        this.mContext = mContext;
        this.datas = mediaItems;
        imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setFailureDrawableId(R.drawable.video_default)
                .setLoadingDrawableId(R.drawable.video_default)
                .build();
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.item_net_video,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //根据位置得到对应的数据
        MediaItem trailersBean = datas.get(position);
        viewHolder.tv_name.setText(trailersBean.getName());
        viewHolder.tv_size.setText(trailersBean.getSize()+"秒");
        viewHolder.tv_duration.setText(trailersBean.getDesc());
        //x.image().bind(viewHolder.iv_icon, trailersBean.getImageUrl(),imageOptions);
//        使用Picasso请求图片
//        Picasso.with(mContext)
//                .load(trailersBean.getImageUrl())
//                .placeholder(R.drawable.video_default)
//                .error(R.drawable.video_default)
//                .into(viewHolder.iv_icon);
        //使用glide请求图片
        Glide.with(mContext)
                .load(trailersBean.getImageUrl())
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .into(viewHolder.iv_icon);
        return convertView;
    }
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;
    }
}

