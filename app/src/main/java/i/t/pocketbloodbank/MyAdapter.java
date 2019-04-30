package i.t.pocketbloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//import com.example.plabon.myapplication.EventInfo;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<donor> {

    public MyAdapter(Context context, ArrayList<donor> array) {

        super(context, 0, array);

    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        donor event = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);

        }

        // Lookup view for data population

       // TextView needName = (TextView) convertView.findViewById(R.id.Name);

        TextView needcontact = (TextView) convertView.findViewById(R.id.contact);

        // Populate the data into the template view using the data object

        needcontact.setText(event.getContact());


        // Return the completed view to render on screen

        return convertView;

    }

}