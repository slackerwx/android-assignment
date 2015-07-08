package slackerwx.com.br.androidassignment.adapters;

/**
 * Created by slackerwx on 07/07/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class Adapter<T> extends BaseAdapter {

    protected static Context context;
    protected ArrayList<T> items;
    protected LayoutInflater inflater;

    public Adapter(Context context, ArrayList<T> listItems) {
        this.context = context;
        this.items = listItems;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items != null ? items.get(position) : null;
    }

    public ArrayList<T> getAllItems() {
        return items;
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    public void emptyList() {
        this.items.clear();
    }


}
