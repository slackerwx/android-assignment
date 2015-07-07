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
import slackerwx.com.br.androidassignment.db.domain.Media;
import slackerwx.com.br.androidassignment.db.domain.OfflinePost;
import slackerwx.com.br.androidassignment.db.domain.RecentByTag;
import slackerwx.com.br.androidassignment.rest.ServiceGenerator;
import slackerwx.com.br.androidassignment.rest.Transacao;
import slackerwx.com.br.androidassignment.rest.TransacaoTask;
import slackerwx.com.br.androidassignment.rest.endpoints.TagEndpoints;

/**
 * Created by slackerwx on 07/07/15.
 */
public class FragmentHome extends Fragment implements Transacao {

    private static final String MODO_EXIBICAO = "modoExibicao";
    private static final int ORDENAR_LIKES = 0;
    private static final int ORDENAR_DATA = 1;
    private static final int ORDENAR_DISTANCIA = 2;
    @Bind(R.id.gridview) GridView mGridView;
    @Bind(R.id.loading_circular) ProgressBar mProgressBar;
    private int tipoOrdenacao = ORDENAR_LIKES;
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


    @Override
    public void executar() throws Exception {

        switch (tipoExibicao) {
            case Constants.ONLINE:
                TagEndpoints client = ServiceGenerator.createService(TagEndpoints.class, Constants.APIURL);

                String clientId = getResources().getString(R.string.instagram_client_id);

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
        aplicarFiltroOrdenacao(posts, tipoOrdenacao);

    }


    private ArrayList<OfflinePost> mediaListToOfflinePosts(ArrayList<Media> mediaList) {

        ArrayList<OfflinePost> posts = new ArrayList<>();

        for (Media media : mediaList) {
            OfflinePost post = OfflinePostMapping.getInstance().getOfflinePost(media);
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

        }

        mImageAdapter.notifyDataSetChanged();

    }

}
