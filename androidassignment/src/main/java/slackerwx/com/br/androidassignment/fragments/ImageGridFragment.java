package slackerwx.com.br.androidassignment.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import slackerwx.com.br.androidassignment.Constants;
import slackerwx.com.br.androidassignment.R;
import slackerwx.com.br.androidassignment.activities.DetalhesActivity;
import slackerwx.com.br.androidassignment.activities.OnMediaClickEvent;
import slackerwx.com.br.androidassignment.adapters.ImageAdapter;
import slackerwx.com.br.androidassignment.db.OfflinePostMapping;
import slackerwx.com.br.androidassignment.db.bo.OfflinePostBo;
import slackerwx.com.br.androidassignment.db.domain.OfflinePost;
import slackerwx.com.br.androidassignment.rest.Pagination;
import slackerwx.com.br.androidassignment.rest.ServiceGenerator;
import slackerwx.com.br.androidassignment.rest.Transacao;
import slackerwx.com.br.androidassignment.rest.TransacaoTask;
import slackerwx.com.br.androidassignment.rest.endpoints.TagEndpoints;
import slackerwx.com.br.androidassignment.rest.model.Media;
import slackerwx.com.br.androidassignment.rest.model.RecentByTag;
import slackerwx.com.br.androidassignment.utils.LocationUtils;
import slackerwx.com.br.androidassignment.utils.SharedPreferencesUtils;

/**
 * Created by slackerwx on 07/07/15.
 */
public class ImageGridFragment extends Fragment implements Transacao, LocationListener {

    private static final String MODO_EXIBICAO = "modoExibicao";
    private static final int ORDENAR_LIKES = 0;
    private static final int ORDENAR_DATA = 1;
    private static final int ORDENAR_DISTANCIA = 2;

    private boolean POSSUI_LOCALIZACAO = false;

    @Bind(R.id.gridview) GridView mGridView;
    @Bind(R.id.loading_circular) ProgressBar mProgressBar;
    private int tipoOrdenacao = ORDENAR_LIKES;
    private ImageAdapter mImageAdapter;
    private ArrayList<OfflinePost> posts = new ArrayList<>();
    private String tipoExibicao;
    private RecentByTag mRecentByTagSearch;
    private TagEndpoints mTagEndpointsClient;
    private String minTagId;
    private String maxTagId;

    private LocationManager locationManager;


    public ImageGridFragment() {

    }

    public static Fragment newInstance(String modoExibicao) {
        ImageGridFragment f = new ImageGridFragment();

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

        mTagEndpointsClient = ServiceGenerator.createService(TagEndpoints.class, Constants.APIURL);


        buscarLocalizacaoAtual();

        if(POSSUI_LOCALIZACAO) {
            TransacaoTask task = new TransacaoTask(getActivity(), this, mProgressBar);
            task.execute();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid_images, container, false);

        ButterKnife.bind(this, rootView);

        mGridView.setOnScrollListener(new EndlessScrollListener() {

            public int totalItemsCount;

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                this.totalItemsCount = totalItemsCount;

                if(POSSUI_LOCALIZACAO) {
                    TransacaoTask task = new TransacaoTask(getActivity(), this);
                    task.execute();
                }
            }

            @Override
            public void executar() throws Exception {
                Pagination pagination = mRecentByTagSearch.getPagination();
                minTagId = pagination.getNextMinTagId();
                maxTagId = pagination.getNextMaxTagId();

                ArrayList<Media> moreMedias = getMedias(minTagId, maxTagId);
                ArrayList<OfflinePost> moreOfflinePosts = mediaListToOfflinePosts(moreMedias);

                posts.addAll(totalItemsCount, moreOfflinePosts);

            }

            @Override
            public void atualizarView() {
                mImageAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }


    @OnItemClick(R.id.gridview)
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), DetalhesActivity.class);
        startActivity(intent);

        OfflinePost post = (OfflinePost) mImageAdapter.getItem(position);

        EventBus.getDefault().postSticky(new OnMediaClickEvent(post));

        OfflinePostBo.getInstance().criarOuAtualizar(post);
    }


    @Override
    public void executar() throws Exception {

        switch (tipoExibicao) {
            case Constants.ONLINE:

                ArrayList<Media> mediaList = getMedias(minTagId, maxTagId);

                posts = mediaListToOfflinePosts(mediaList);
                break;

            case Constants.OFFLINE:
                posts = OfflinePostBo.getInstance().buscarTodos();
                break;
        }

        mImageAdapter = new ImageAdapter(getActivity(), posts);
        aplicarFiltroOrdenacao(posts, tipoOrdenacao);

    }

    private ArrayList<Media> getMedias(String minId, String maxId) {
        String clientId = getResources().getString(R.string.instagram_client_id);

        String tagName = "saopaulocity";
        mRecentByTagSearch = mTagEndpointsClient.getRecent(tagName, clientId, minId, maxId);

        ArrayList<Media> mediaList = (ArrayList<Media>) mRecentByTagSearch.getMediaList();

        return mediaList;
    }


    private ArrayList<OfflinePost> mediaListToOfflinePosts(ArrayList<Media> mediaList) {

        ArrayList<OfflinePost> posts = new ArrayList<>();

        for (Media media : mediaList) {
            OfflinePost post = OfflinePostMapping.getInstance().getOfflinePost(media);
            if(post!=null) posts.add(post);
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
            tipoOrdenacao = ORDENAR_DATA;

            aplicarFiltroOrdenacao(posts, tipoOrdenacao);

            return true;
        } else if (id == R.id.action_filtro_distancia) {
            tipoOrdenacao = ORDENAR_DISTANCIA;

            aplicarFiltroOrdenacao(posts, tipoOrdenacao);

            return true;
        } else if (id == R.id.action_filtro_likes) {
            tipoOrdenacao = ORDENAR_LIKES;

            aplicarFiltroOrdenacao(posts, tipoOrdenacao);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void aplicarFiltroOrdenacao(ArrayList<OfflinePost> posts, int tipoOrdenacao) {

        switch (tipoOrdenacao) {
            case ORDENAR_LIKES:
                Collections.sort(posts, OfflinePost.LIKES_ASC);
                break;
            case ORDENAR_DATA:
                Collections.sort(posts, OfflinePost.DATE_ASC);
                break;
            case ORDENAR_DISTANCIA:
                Collections.sort(posts, OfflinePost.DISTANCE_DESC);
                break;
        }

        mImageAdapter.notifyDataSetChanged();

    }

    private void buscarLocalizacaoAtual(){
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Location location = LocationUtils.getLatestLocation(getActivity());
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
            }

            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(location.getLatitude()));
            sb.append(" ");
            sb.append(String.valueOf(location.getLongitude()));

            SharedPreferencesUtils.putLatLngAtual(sb.toString());

            POSSUI_LOCALIZACAO = true;

        } else {
            ligarGPS();
            POSSUI_LOCALIZACAO = false;
        }
    }

    private void ligarGPS() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("O GPS está desligado, deseja ligar agora?").setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Intent para entrar nas configurações de localização
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                        getActivity().finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                getActivity().finish();
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
