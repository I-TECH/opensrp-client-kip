package org.smartregister.kip.provider;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.growthmonitoring.repository.WeightRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.kip.R;
import org.smartregister.kip.fragment.AdvancedSearchFragment;
import org.smartregister.service.AlertService;
import org.smartregister.view.contract.SmartRegisterClient;

import java.util.ArrayList;
import java.util.List;

import static org.smartregister.util.Utils.getValue;

/**
 * Created by Keyman on 06-Apr-17.
 */
public class AdvancedSearchClientsProvider extends ChildSmartClientsProvider {
    private final Context context;
    private final View.OnClickListener onClickListener;
    private final CommonRepository commonRepository;

    public AdvancedSearchClientsProvider(Context context, View.OnClickListener onClickListener,
                                         AlertService alertService, VaccineRepository vaccineRepository, WeightRepository weightRepository, CommonRepository commonRepository) {
        super(context, onClickListener, alertService, vaccineRepository, weightRepository);
        this.onClickListener = onClickListener;
        this.context = context;
        this.commonRepository = commonRepository;

    }

    public void getView(Cursor cursor, SmartRegisterClient client, View convertView) {
        super.getView(cursor, client, convertView);

        CommonPersonObjectClient pc = (CommonPersonObjectClient) client;

        //TODO check if record exists ...
        if (cursor instanceof AdvancedSearchFragment.AdvancedMatrixCursor && commonRepository != null) {
            CommonPersonObject commonPersonObject = commonRepository.findByBaseEntityId(pc.entityId());

            View recordVaccination = convertView.findViewById(R.id.record_vaccination);
            recordVaccination.setVisibility(View.VISIBLE);

            View moveToCatchment = convertView.findViewById(R.id.move_to_catchment);
            moveToCatchment.setVisibility(View.GONE);

            if (commonPersonObject == null) { //Out of area -- doesn't exist in local database
                TextView recordWeightText = (TextView) convertView.findViewById(R.id.record_weight_text);
                recordWeightText.setText("Record\nservice");

                String zeirId = getValue(pc.getColumnmaps(), "zeir_id", false);

                View recordWeight = convertView.findViewById(R.id.record_weight);
                recordWeight.setBackground(context.getResources().getDrawable(R.drawable.record_weight_bg));
                recordWeight.setTag(zeirId);
                recordWeight.setClickable(true);
                recordWeight.setEnabled(true);
                recordWeight.setOnClickListener(onClickListener);


                TextView moveToCatchmentText = (TextView) convertView.findViewById(R.id.move_to_catchment_text);
                moveToCatchmentText.setText("Move to my\ncatchment");

                String motherBaseEntityId = getValue(pc.getColumnmaps(), "mother_base_entity_id", false);
                String entityId = pc.entityId();

                List<String> ids = new ArrayList<>();
                ids.add(motherBaseEntityId);
                ids.add(entityId);

                moveToCatchment.setBackground(context.getResources().getDrawable(R.drawable.record_weight_bg));
                moveToCatchment.setTag(ids);
                moveToCatchment.setClickable(true);
                moveToCatchment.setEnabled(true);
                moveToCatchment.setOnClickListener(onClickListener);

                moveToCatchment.setVisibility(View.VISIBLE);
                recordVaccination.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public View inflatelayoutForCursorAdapter() {
        return (ViewGroup) inflater().inflate(R.layout.advanced_search_client, null);
    }
}
