package ca.bcit.wester;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.TextView;

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
        TextView name = (TextView) contentView.findViewById(R.id.name);
        TextView cater = (TextView) contentView.findViewById(R.id.cater);
        TextView desc = (TextView) contentView.findViewById(R.id.desc);
        TextView hours = (TextView) contentView.findViewById(R.id.hours);
        TextView address = (TextView) contentView.findViewById(R.id.address);
        TextView phone = (TextView) contentView.findViewById(R.id.phone);
        TextView email = (TextView) contentView.findViewById(R.id.email);
        TextView website = (TextView) contentView.findViewById(R.id.website);

        //Set all the text view with the service information
        name.setText(service.getName());
        cater.setText(service.getCategory());
        desc.setText(service.getDescription());
        hours.setText(service.getHours());
        address.setText(service.getAddress());
        phone.setText(service.getPhone());
        email.setText(service.getEmail());
        website.setText(service.getWebsite());

    }

}
