package org.smartregister.kip.fragment;

import android.text.TextUtils;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.child.fragment.BaseAdvancedSearchFragment;
import org.smartregister.child.presenter.BaseChildAdvancedSearchPresenter;
import org.smartregister.kip.presenter.AdvancedSearchPresenter;
import org.smartregister.kip.util.DBQueryHelper;
import org.smartregister.kip.util.KipConstants;
import org.smartregister.view.activity.BaseRegisterActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ndegwamartin on 08/03/2019.
 */
public class AdvancedSearchFragment extends BaseAdvancedSearchFragment {
    protected MaterialEditText openSrpId;
    protected MaterialEditText motherGuardianLastName;
    protected MaterialEditText motherGuardianFirstName;
    protected MaterialEditText motherGuardianNid;
    protected MaterialEditText motherGuardianPhoneNumber;
    private AdvancedSearchPresenter presenter;
    private MaterialEditText firstName;
    private MaterialEditText lastName;

    @Override
    protected BaseChildAdvancedSearchPresenter getPresenter() {
        if (presenter == null) {
            String viewConfigurationIdentifier = ((BaseRegisterActivity) getActivity()).getViewIdentifiers().get(0);
            presenter = new AdvancedSearchPresenter(this, viewConfigurationIdentifier);
        }

        return presenter;
    }

    @Override
    protected String getMainCondition() {
        return DBQueryHelper.getHomeRegisterCondition();
    }

    @Override
    protected Map<String, String> getSearchMap(boolean searchFlag) {
        Map<String, String> searchParams = new HashMap<>();

        String firstName = this.firstName.getText().toString();
        String lastName = this.lastName.getText().toString();


        String motherGuardianFirstNameString = motherGuardianFirstName.getText().toString();
        String motherGuardianLastNameString = motherGuardianLastName.getText().toString();
        String motherGuardianNidString = motherGuardianNid.getText().toString();
        String motherGuardianPhoneNumberString = motherGuardianPhoneNumber.getText().toString();
        String merId = openSrpId.getText().toString();


        if (StringUtils.isNotBlank(motherGuardianFirstNameString)) {
            searchParams.put(KipConstants.KEY.MOTHER_FIRST_NAME, motherGuardianFirstNameString);
        }

        if (StringUtils.isNotBlank(motherGuardianLastNameString)) {
            searchParams.put(KipConstants.KEY.MOTHER_FIRST_NAME, motherGuardianLastNameString);
        }

        if (StringUtils.isNotBlank(motherGuardianNidString)) {
            searchParams.put(KipConstants.KEY.MOTHER_NRC_NUMBER, motherGuardianNidString);
        }

        if (StringUtils.isNotBlank(motherGuardianPhoneNumberString)) {
            searchParams.put(KipConstants.KEY.MOTHER_GUARDIAN_NUMBER, motherGuardianPhoneNumberString);
        }


        if (!TextUtils.isEmpty(firstName)) {
            searchParams.put(KipConstants.KEY.FIRST_NAME, firstName);
        }

        if (!TextUtils.isEmpty(lastName)) {
            searchParams.put(KipConstants.KEY.LAST_NAME, lastName);
        }

        if (!TextUtils.isEmpty(merId)) {
            searchParams.put(KipConstants.KEY.ZEIR_ID, merId);
        }

        return searchParams;
    }

    @Override
    public void assignedValuesBeforeBarcode() {
        if (searchFormData.size() > 0) {
            firstName.setText(searchFormData.get(KipConstants.KEY.FIRST_NAME));
            lastName.setText(searchFormData.get(KipConstants.KEY.LAST_NAME));
            motherGuardianFirstName.setText(searchFormData.get(KipConstants.KEY.MOTHER_FIRST_NAME));
            motherGuardianLastName.setText(searchFormData.get(KipConstants.KEY.MOTHER_LAST_NAME));
            motherGuardianNid.setText(searchFormData.get(KipConstants.KEY.MOTHER_NRC_NUMBER));
            motherGuardianPhoneNumber.setText(searchFormData.get(KipConstants.KEY.MOTHER_GUARDIAN_NUMBER));
            openSrpId.setText(searchFormData.get(KipConstants.KEY.ZEIR_ID));
        }
    }

    @Override
    public void populateSearchableFields(View view) {
        firstName = view.findViewById(org.smartregister.child.R.id.first_name);
        advancedFormSearchableFields.put(KipConstants.KEY.FIRST_NAME, firstName);

        lastName = view.findViewById(org.smartregister.child.R.id.last_name);
        advancedFormSearchableFields.put(KipConstants.KEY.LAST_NAME, lastName);

        openSrpId = view.findViewById(org.smartregister.child.R.id.opensrp_id);
        advancedFormSearchableFields.put(KipConstants.KEY.ZEIR_ID, openSrpId);

        motherGuardianFirstName = view.findViewById(org.smartregister.child.R.id.mother_guardian_first_name);
        advancedFormSearchableFields.put(KipConstants.KEY.MOTHER_FIRST_NAME, motherGuardianFirstName);

        motherGuardianLastName = view.findViewById(org.smartregister.child.R.id.mother_guardian_last_name);
        advancedFormSearchableFields.put(KipConstants.KEY.MOTHER_LAST_NAME, motherGuardianLastName);

        motherGuardianNid = view.findViewById(org.smartregister.child.R.id.mother_guardian_nrc);
        advancedFormSearchableFields.put(KipConstants.KEY.MOTHER_NRC_NUMBER, motherGuardianNid);

        motherGuardianPhoneNumber = view.findViewById(org.smartregister.child.R.id.mother_guardian_phone_number);
        advancedFormSearchableFields.put(KipConstants.KEY.MOTHER_GUARDIAN_NUMBER, motherGuardianPhoneNumber);


        firstName.addTextChangedListener(advancedSearchTextwatcher);
        lastName.addTextChangedListener(advancedSearchTextwatcher);
        openSrpId.addTextChangedListener(advancedSearchTextwatcher);
        motherGuardianFirstName.addTextChangedListener(advancedSearchTextwatcher);
        motherGuardianLastName.addTextChangedListener(advancedSearchTextwatcher);
        motherGuardianNid.addTextChangedListener(advancedSearchTextwatcher);
        motherGuardianPhoneNumber.addTextChangedListener(advancedSearchTextwatcher);
    }

    @Override
    protected HashMap<String, String> createSelectedFieldMap() {
        HashMap<String, String> fields = new HashMap<>();
        fields.put(KipConstants.KEY.FIRST_NAME, firstName.getText().toString());
        fields.put(KipConstants.KEY.LAST_NAME, lastName.getText().toString());
        fields.put(KipConstants.KEY.MOTHER_FIRST_NAME, motherGuardianFirstName.getText().toString());
        fields.put(KipConstants.KEY.MOTHER_LAST_NAME, motherGuardianLastName.getText().toString());
        fields.put(KipConstants.KEY.MOTHER_NRC_NUMBER, motherGuardianNid.getText().toString());
        fields.put(KipConstants.KEY.MOTHER_GUARDIAN_NUMBER, motherGuardianPhoneNumber.getText().toString());
        fields.put(KipConstants.KEY.ZEIR_ID, openSrpId.getText().toString());
        return fields;
    }

    @Override

    protected void clearFormFields() {
        super.clearFormFields();
        openSrpId.setText("");
        firstName.setText("");
        lastName.setText("");
        motherGuardianFirstName.setText("");
        motherGuardianLastName.setText("");
        motherGuardianNid.setText("");
        motherGuardianPhoneNumber.setText("");

    }

    @Override
    protected String getDefaultSortQuery() {
        return presenter.getDefaultSortQuery();
    }

    @Override
    protected String filterSelectionCondition(boolean urgentOnly) {
        return DBQueryHelper.getFilterSelectionCondition(urgentOnly);
    }

    @Override
    public void onClick(View view) {
        view.toString();
    }
}

