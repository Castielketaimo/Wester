package ca.bcit.wester;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

import ca.bcit.wester.controllers.ServiceController;
import ca.bcit.wester.models.Service;

/**
 * Created by casti on 2017-11-25.
 */

public class BottomSheetMapFragment extends BottomSheetDialogFragment {

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.fragment_map_bottomsheet, null);
        dialog.setContentView(contentView);
        Bundle args = getArguments();
        int serviceId = args.getInt("serviceId", 0);
        ServiceController s = new ServiceController(getContext());
        Service service = s.readSingleRecordById(serviceId);

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

        //set up the string for display
        String name = service.getName();
        String category = service.getCategory();
        String description =  service.getDescription();
        String hours = "Hours: " + service.getHours();
        String address = "Address: " + service.getAddress();
        String postCode = "PostCode: " + service.getPostalCode();
        String phone = "Phone: " + service.getPhone();
        String email = "Email: " + service.getEmail();
        String website = "Website: " + service.getWebsite();
        makeImage(service.getCategory(), caterImg);

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
    }

    private void makeImage(String caterTitle, ImageView caterImg){

        switch(caterTitle) {
            case "Drop-In Centre" :
                caterImg.setImageResource(R.drawable.dropin);
                break;

            case "Education, Language and Literacy" :
                caterImg.setImageResource(R.drawable.education);
                break;

            case "Emergency, Transitional and Supported Housing" :
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

            case "Parks, Recreation and Community School Programming" :
                caterImg.setImageResource(R.drawable.parkrecreating);
                break;

            case "Seniors Services" :
                caterImg.setImageResource(R.drawable.seniorsservics);
                break;

            case "Child Care, Child Development and Early Learning Programs" :
                caterImg.setImageResource(R.drawable.childcare);
                break;

            default :
                caterImg.setImageResource(R.drawable.newwestminster);
        }
    }

}
