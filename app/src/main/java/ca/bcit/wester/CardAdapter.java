package ca.bcit.wester;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
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
                final AlertDialog.Builder alertAdd = new AlertDialog.Builder(new ContextThemeWrapper((v.getContext()), R.style.CustomAlertDialog));
//                AlertDialog alertDialog = alertAdd.create();
                //  LayoutInflater inflater = (LayoutInflater)v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//                alertDialog.setContentView(R.layout.dialog_laylout);
                alertAdd.setTitle("");
                LayoutInflater factory = LayoutInflater.from(v.getContext());
                final View contentView = factory.inflate(R.layout.dialog_laylout, null);

                //get all the views
                TextView nameView = (TextView) contentView.findViewById(R.id.name);
                TextView caterView = (TextView) contentView.findViewById(R.id.cater);
                TextView descView = (TextView) contentView.findViewById(R.id.desc);
                TextView hoursView = (TextView) contentView.findViewById(R.id.hours);
                TextView addressView = (TextView) contentView.findViewById(R.id.address);
                TextView postCodeView = (TextView) contentView.findViewById(R.id.postocode);
                TextView phoneView = (TextView) contentView.findViewById(R.id.phone);
                TextView emailView = (TextView) contentView.findViewById(R.id.email);
                TextView websiteView = (TextView) contentView.findViewById(R.id.website);
                ImageView caterImg = (ImageView) contentView.findViewById(R.id.CaterImage);
                Button returnBtn = (Button) contentView.findViewById(R.id.dialog_return);

                //set up the string for display
                String name = services.get(servicePosition).getName();
                String category = services.get(servicePosition).getCategory();
                String description =  services.get(servicePosition).getDescription();
                String hours = "Hours: " + services.get(servicePosition).getHours();
                String address = "Address: " + services.get(servicePosition).getAddress();
                String postCode = "PostCode: " + services.get(servicePosition).getPostalCode();
                String phone = "Phone: " + services.get(servicePosition).getPhone();
                String email = "Email: " + services.get(servicePosition).getEmail();
                String website = "Website: " + services.get(servicePosition).getWebsite();
                makeImage(services.get(servicePosition).getCategory(), caterImg);

                //Set all the text view with the service information
                nameView.setText(name);
                caterView.setText(category);
                descView.setText(description);
                hoursView.setText(hours);
                addressView.setText(address);
                postCodeView.setText(postCode);
                phoneView.setText(phone);
                emailView.setText(email);
                websiteView.setText(website);

                alertAdd.setView(contentView);
                final AlertDialog dialog = alertAdd.create();
                returnBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return services.size();
    }

    /**
     * Sets the image to a resource depending on which title
     * @param caterTitle the title of the category
     * @param caterImg the imageview that changes
     */
    private void makeImage(String caterTitle, ImageView caterImg){

        switch(caterTitle) {
            case "Drop-In Centre" :
                caterImg.setImageResource(R.drawable.dropin);
                break;

            case "Education, Language and Literacy" :
                caterImg.setImageResource(R.drawable.education);
                break;

            case "Emergency,Transitional and Supported Housing" :
                caterImg.setImageResource(R.drawable.emergency);
                break;

            case "Employment and Job Training" :
                caterImg.setImageResource(R.drawable.employment);
                break;

            case "Family and General Support Programs" :
                caterImg.setImageResource(R.drawable.family);
                break;

            case "Food Programs and Services" :
                caterImg.setImageResource(R.drawable.foodprogram);
                break;

            case "Government and Justice Services" :
                caterImg.setImageResource(R.drawable.governmentndustice);
                break;

            case "Health, Mental Health & Addictions Services" :
                caterImg.setImageResource(R.drawable.healthmental);
                break;

            case "Housing Outreach, Advocacy and Referral" :
                caterImg.setImageResource(R.drawable.housingoutreach);
                break;

            case "Non-Market and Co-op Housing" :
                caterImg.setImageResource(R.drawable.nonmarket);
                break;

            case "Parks, Recreation and Community School" :
                caterImg.setImageResource(R.drawable.parkrecreating);
                break;

            case "Seniors Services" :
                caterImg.setImageResource(R.drawable.seniorsservics);
                break;

            case "Child Care, Child Development and Early Learning Programs" :
                caterImg.setImageResource(R.drawable.childcare);
                break;

            case "Settlement Service" :
                caterImg.setImageResource(R.drawable.settement);
                break;

            default :
                caterImg.setImageResource(R.drawable.newwestminster);
        }
    }
}