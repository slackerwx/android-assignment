package slackerwx.com.br.androidassignment.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import slackerwx.com.br.androidassignment.Constants;
import slackerwx.com.br.androidassignment.R;
import slackerwx.com.br.androidassignment.activities.DetalhesActivity;
import slackerwx.com.br.androidassignment.activities.OnMediaClickEvent;
import slackerwx.com.br.androidassignment.adapters.ImageAdapter;
import slackerwx.com.br.androidassignment.db.bo.OfflinePostBo;
import slackerwx.com.br.androidassignment.db.domain.Image;
import slackerwx.com.br.androidassignment.db.domain.Images;
import slackerwx.com.br.androidassignment.db.domain.Location;
import slackerwx.com.br.androidassignment.db.domain.Media;
import slackerwx.com.br.androidassignment.db.domain.OfflinePost;
import slackerwx.com.br.androidassignment.db.domain.RecentByTag;
import slackerwx.com.br.androidassignment.rest.ServiceGenerator;
import slackerwx.com.br.androidassignment.rest.Transacao;
import slackerwx.com.br.androidassignment.rest.TransacaoTask;
import slackerwx.com.br.androidassignment.rest.endpoints.TagEndpoints;
import slackerwx.com.br.androidassignment.utils.DateUtils;

/**
 * Created by slackerwx on 07/07/15.
 */
public class FragmentHome extends Fragment implements Transacao {

    private static final String MODO_EXIBICAO = "modoExibicao";

    @Bind(R.id.gridview) GridView mGridView;
    @Bind(R.id.loading_circular) ProgressBar mProgressBar;

    private ImageAdapter mImageAdapter;
    private ArrayList<OfflinePost> posts = new ArrayList<>();
    private String tipoExibicao;

    public FragmentHome() {

    }

    public static Fragment newInstance(String modoExibicao) {
        FragmentHome f = new FragmentHome();

        Bundle args = new Bundle();
        args.putString(MODO_EXIBICAO, modoExibicao);
        f.setArguments(args);

        return f;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle arguments = getArguments();
        tipoExibicao = arguments.getString(MODO_EXIBICAO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid_images, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        TransacaoTask task = new TransacaoTask(getActivity(), this, mProgressBar);
        task.execute();
    }


    @OnItemClick(R.id.gridview)
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), DetalhesActivity.class);
        startActivity(intent);

        OfflinePost post = (OfflinePost) mImageAdapter.getItem(position);

        EventBus.getDefault().postSticky(new OnMediaClickEvent(post));

        OfflinePostBo.getInstance().criarOuAtualizar(post);
    }

    private OfflinePost getOfflinePost(Media media) {
        Images images = media.getImages();
        Image standardResolution = images.getStandardResolution();
        final String imageUrl = standardResolution.getUrl();


        Location location = media.getLocation();

        String latLng = "";
        String locationName = "";
        if (location != null) {
            locationName = location.getName();

            Double lat = location.getLatitude();
            Double longi = location.getLongitude();

            final String latitude =  lat != null ? Double.toString(lat) : "";
            final String longitude = longi !=null ? Double.toString(longi) : "";
            latLng = latitude  + " " + longitude;
        }


        List<String> tags = media.getTags();

        StringBuilder sb = new StringBuilder();
        for (String tag : tags) {
            sb.append("#");
            sb.append(tag);
            sb.append(" ");
        }

        final String userFullName = media.getUser().getUsername();
        final String userId = media.getUser().getId();
        final String allTags = sb.toString();
        final String likesCount = String.valueOf(media.getLikes().getCount());
        final String createdTime = DateUtils.timestampToStringDate(media.getCreatedTime());
        final String imageThumbnailUrl = media.getImages().getThumbnail().getUrl();
        final String username = media.getUser().getUsername();
        final String id = media.getId();

        return new OfflinePost(id, imageUrl, userFullName, userId, allTags, createdTime, latLng, locationName, likesCount,
                imageThumbnailUrl, username);
    }


    @Override
    public void executar() throws Exception {

        switch (tipoExibicao) {
            case Constants.ONLINE:
                TagEndpoints client = ServiceGenerator.createService(TagEndpoints.class, Constants.APIURL);

                String clientId = getResources().getString(R.string.instagram_client_id);
//                Long minTimestamp = null;
//                Long maxTimestamp = null;
//                Integer distanceInKm = 1000;
//                Double latitude = -23.5626773;
//                Double longitude = -46.6551163;

                String tagName = "saopaulocity";
                RecentByTag search = client.getRecent(tagName, clientId, null, null);

                ArrayList<Media> mediaList = (ArrayList<Media>) search.getMediaList();

                posts = mediaListToOfflinePosts(mediaList);
                break;

            case Constants.OFFLINE:
                posts = OfflinePostBo.getInstance().buscarTodos();
                break;
        }


        mImageAdapter = new ImageAdapter(getActivity(), posts);

    }

    private ArrayList<OfflinePost> mediaListToOfflinePosts(ArrayList<Media> mediaList) {

        ArrayList<OfflinePost> posts = new ArrayList<>();

        for (Media media : mediaList) {
            OfflinePost post = getOfflinePost(media);
            posts.add(post);
        }

        return posts;

    }

    @Override
    public void atualizarView() {
        mGridView.setAdapter(mImageAdapter);
        mImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filtro_data) {
            Collections.sort(posts, new Comparator<OfflinePost>() {
                @Override
                public int compare(OfflinePost lhs, OfflinePost rhs) {
                    return rhs.getCreatedTime().compareTo(lhs.getCreatedTime());
                }
            });
            mImageAdapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.action_filtro_distancia) {
            return true;
        } else if (id == R.id.action_filtro_likes) {
            Collections.sort(posts, new Comparator<OfflinePost>() {
                @Override
                public int compare(OfflinePost lhs, OfflinePost rhs) {
                    return rhs.getLikesCount().compareTo(lhs.getLikesCount());
                }
            });
            mImageAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
