package ca.bcit.wester;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.wester.controllers.ServiceController;
import ca.bcit.wester.models.Service;

/**
 * Created by Chonjou on 2017-11-21.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private ServiceController dbHandler;
    private static List<Service> services = new ArrayList<>();
    private static ArrayList<String> filterNames = new ArrayList<String>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextTitle;
        public TextView mTextDesc;
        public View _v;
        public ViewHolder(View v) {
            super(v);
            _v = v;
            mTextTitle = v.findViewById(R.id.info_title);
            mTextDesc = v.findViewById(R.id.info_text);
        }
        public View getView() {
            return _v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardAdapter(Context context) {
        dbHandler = new ServiceController(context);
        services = ServiceController.getServiceList();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final int servicePosition = position;
        holder.mTextTitle.setText(services.get(position).getName());
        holder.mTextDesc.setText(services.get(position).getCategory());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper((v.getContext()), R.style.CustomAlertDialog)).create();
                alertDialog.setTitle(services.get(servicePosition).getName());
                alertDialog.setMessage(
                        services.get(servicePosition).getDescription() + "\n\n" +
                        "Hours: " + services.get(servicePosition).getHours() + "\n" +
                        "Address: " + services.get(servicePosition).getAddress() + "\n" +
                        "Postal Code: " + services.get(servicePosition).getPostalCode() + "\n" +
                        "Phone: " + services.get(servicePosition).getPhone() + "\n" +
                        "Email: " + services.get(servicePosition).getEmail() + "\n" +
                        "Website: " + services.get(servicePosition).getWebsite() + "\n"
                );
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return services.size();
    }
}