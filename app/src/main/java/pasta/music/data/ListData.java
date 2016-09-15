package pasta.music.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by aniket on 9/15/16.
 */
public class ListData {

    public void bindViewHolder(Context context, ListData.ViewHolder holder, int position) {
    }

    public ViewHolder getViewHolder(LayoutInflater inflater, int position) {
        return null;
    }

    public class ViewHolder {
        View v;

        public ViewHolder(View v) {
            this.v = v;
        }
    }
}
