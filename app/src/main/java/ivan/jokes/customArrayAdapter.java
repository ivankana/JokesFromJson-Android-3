package ivan.jokes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ivan on 20.03.2016.
 * This class was used to create my customadapter. But It is not finshed yet
 */
public class customArrayAdapter extends ArrayAdapter<String> {

    private static class ViewHolder{
        TextView joke;
    }

    public customArrayAdapter(Context context, ArrayList<String> text) {
        super(context, R.layout.items, text);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String text = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.items, parent, false);
            viewHolder.joke = (TextView) convertView.findViewById(R.id.jokeText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.joke.setText(text);
        // Return the completed view to render on screen
        return convertView;
    }
}


