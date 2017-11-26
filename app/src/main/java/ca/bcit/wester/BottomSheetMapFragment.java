package ca.bcit.wester;

import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.TextView;

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
    }

}
