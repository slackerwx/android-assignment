package slackerwx.com.br.androidassignment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import slackerwx.com.br.androidassignment.R;
import slackerwx.com.br.androidassignment.db.domain.OfflinePost;
import slackerwx.com.br.androidassignment.utils.ImageUtils;

/**
 * Created by slackerwx on 06/07/15.
 */
public class ImageAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<OfflinePost> items = new ArrayList<>();
    private LayoutInflater inflater;

    public ImageAdapter(Context context, ArrayList<OfflinePost> imagens) {
        this.context = context;
        this.items = imagens;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OfflinePost post = items.get(position);

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.preencher(post, context);


        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.image_thumbnail) ImageView imageView;
        @Bind(R.id.poster_name) TextView posterName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void preencher(OfflinePost post, Context context) {
            String url = post.getImageThumbnailUrl();
            Glide.with(context).load(url).into(imageView);

            ImageUtils.imageViewToGrayScale(imageView);

            String userName = post.getUsername();
            posterName.setText(userName);
        }


    }
}
