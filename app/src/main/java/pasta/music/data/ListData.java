package pasta.music.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by aniket on 9/15/16.
 */
public class ListData {
    public class ViewHolder {
        View view;
        public ViewHolder(View view)
        {
            this.view=view;
        }

    }

    public void bindViewHolder(Context context,ListData.ViewHolder viewHolder,int position)
    {

    }

    public ListData.ViewHolder getViewHolder(LayoutInflater inflater,int position)
    {
        return null;
    }
}
