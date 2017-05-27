package mobilevideo0224.mobilevideo0224.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mobilevideo0224.mobilevideo0224.R;

public class SearchActivity extends AppCompatActivity {

    @InjectView(R.id.et_sousuo)
    EditText etSousuo;
    @InjectView(R.id.iv_voice)
    ImageView ivVoice;
    @InjectView(R.id.tv_go)
    TextView tvGo;
    @InjectView(R.id.lv)
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.iv_voice, R.id.tv_go})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_voice:
                break;
            case R.id.tv_go:
                break;
        }
    }
}
