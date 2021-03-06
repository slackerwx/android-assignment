package slackerwx.com.br.androidassignment.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import slackerwx.com.br.androidassignment.R;
import slackerwx.com.br.androidassignment.db.domain.OfflinePost;
import slackerwx.com.br.androidassignment.utils.DateUtils;
import slackerwx.com.br.androidassignment.utils.SharedPreferencesUtils;
import slackerwx.com.br.androidassignment.utils.Utils;

public class DetalhesActivity extends ActionBarActivity {

    @Bind(R.id.imagem_detalhe) ImageView mImageView;
    @Bind(R.id.tv_location_lat_lng) TextView tvLocationLatLng;
    @Bind(R.id.tv_location_name) TextView tvLocationName;
    @Bind(R.id.tv_tags) TextView tvTags;
    @Bind(R.id.tv_user_full_name) TextView tvUserFullName;
    @Bind(R.id.tv_user_id) TextView tvUserId;
    @Bind(R.id.tv_likes) TextView tvLikes;
    @Bind(R.id.tv_data_criacao) TextView tvDataCriacao;
    @Bind(R.id.tv_distancia) TextView tvDistancia;
    private OfflinePost post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        ButterKnife.bind(this);

        EventBus.getDefault().registerSticky(this);

    }

    public void onEvent(OnMediaClickEvent event) {
        post = event.getPost();

        Glide.with(this).load(post.getImageUrl()).into(mImageView);

        String locationName = post.getLocationName();
        tvLocationName.setText(locationName);

        final String latLng = post.getLatLng();
        tvLocationLatLng.setText(latLng);

        final String userFullName = post.getUserFullName();
        tvUserFullName.setText(userFullName);

        final String userId = post.getUserId();
        tvUserId.setText(userId);

        final String allTags = post.getTags();
        tvTags.setText(allTags);

        final String likesCount = String.valueOf(post.getLikesCount());
        tvLikes.setText(likesCount);

        final String createdTime = DateUtils.timestampToStringDate(post.getCreatedTime());
        tvDataCriacao.setText(createdTime);

        final String distancia = getDistancia();
        tvDistancia.setText(distancia);

    }

    private String getDistancia() {
        String latLngAtual = SharedPreferencesUtils.getLatLngAtual();

        String[] split = latLngAtual.split(" ");
        double mLatAtual = Double.valueOf(split[0]);
        double mLongAtual = Double.valueOf(split[1]);


        final double distancia = Utils.getDistanceFromLocation(mLatAtual, mLongAtual, post.getLatitude(), post.getLongitude(), 'K');

        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(Math.round(distancia)));
        sb.append(" km");

        return sb.toString();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
