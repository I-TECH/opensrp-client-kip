package org.smartregister.kip.util;

import android.content.Context;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.child.ChildLibrary;
import org.smartregister.child.util.ChildJsonFormUtils;
import org.smartregister.child.util.Constants;
import org.smartregister.child.util.Utils;
import org.smartregister.kip.context.AllSettings;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.FormUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static org.smartregister.kip.util.KipConstants.FormTitleUtil.UPDATE_CHILD_FORM;
import static org.smartregister.login.task.RemoteLoginTask.getOpenSRPContext;

public class KipJsonFormUtils extends ChildJsonFormUtils {

//    private static CommonPersonObjectClient detailmaps;
    private static Map<String, String> detailmaps;


    public static String getMetadataForEditForm(Context context, Map<String, String> childDetails, List<String> nonEditableFields) {
        try {
            JSONObject birthRegistrationForm = FormUtils.getInstance(context)
                    .getFormJson(Utils.metadata().childRegister.formName);
            updateRegistrationEventType(birthRegistrationForm);
            ChildJsonFormUtils.addRegistrationFormLocationHierarchyQuestions(birthRegistrationForm);
            KipLocationUtility.addChildRegLocHierarchyQuestions(birthRegistrationForm, getOpenSRPContext());
            KipJsonFormUtils.addRelationshipTypesQuestions(birthRegistrationForm);

            if (birthRegistrationForm != null) {
                birthRegistrationForm.put(ChildJsonFormUtils.ENTITY_ID, childDetails.get(Constants.KEY.BASE_ENTITY_ID));
                birthRegistrationForm.put(ChildJsonFormUtils.ENCOUNTER_TYPE, Utils.metadata().childRegister.updateEventType);
                birthRegistrationForm.put(ChildJsonFormUtils.RELATIONAL_ID, childDetails.get(RELATIONAL_ID));
                birthRegistrationForm.put(ChildJsonFormUtils.CURRENT_ZEIR_ID, Utils.getValue(childDetails, KipConstants.KEY.MALAWI_ID, true).replace("-", ""));
                birthRegistrationForm.put(ChildJsonFormUtils.CURRENT_OPENSRP_ID,
                        Utils.getValue(childDetails, Constants.JSON_FORM_KEY.UNIQUE_ID, false));

                JSONObject metadata = birthRegistrationForm.getJSONObject(ChildJsonFormUtils.METADATA);
                metadata.put(ChildJsonFormUtils.ENCOUNTER_LOCATION,
                        ChildLibrary.getInstance().getLocationPickerView(context).getSelectedItem());

                //inject zeir id into the birthRegistrationForm
                JSONObject stepOne = birthRegistrationForm.getJSONObject(ChildJsonFormUtils.STEP1);
                JSONArray jsonArray = stepOne.getJSONArray(ChildJsonFormUtils.FIELDS);
                updateFormDetailsForEdit(childDetails, jsonArray, nonEditableFields);
                return birthRegistrationForm.toString();
            }
        } catch (Exception e) {
            Timber.e(e, "KipJsonFormUtils --> getMetadataForEditForm");
        }

        return "";
    }
    private static void updateFormDetailsForEdit(Map<String, String> childDetails, JSONArray jsonArray, List<String> nonEditableFields)
            throws JSONException {
        String prefix;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            prefix = getPrefix(jsonObject);

            if (jsonObject.getString(ChildJsonFormUtils.KEY).equalsIgnoreCase(Constants.KEY.PHOTO)) {
                processPhoto(childDetails.get(Constants.KEY.BASE_ENTITY_ID), jsonObject);
            } else if (jsonObject.getString(ChildJsonFormUtils.KEY).equalsIgnoreCase("dob_unknown")) {
                getDobUnknown(childDetails, jsonObject);
            } else if (jsonObject.getString(ChildJsonFormUtils.KEY).equalsIgnoreCase(Constants.JSON_FORM_KEY.AGE)) {
                processAge(Utils.getValue(childDetails, "dob", false), jsonObject);
            } else if (jsonObject.getString(JsonFormConstants.TYPE).equalsIgnoreCase(JsonFormConstants.DATE_PICKER)) {
                processDate(childDetails, prefix, jsonObject);
            } else if (jsonObject.getString(ChildJsonFormUtils.OPENMRS_ENTITY).equalsIgnoreCase(ChildJsonFormUtils.PERSON_INDENTIFIER)) {
                jsonObject.put(ChildJsonFormUtils.VALUE, Utils.getValue(childDetails,
                        jsonObject.getString(ChildJsonFormUtils.OPENMRS_ENTITY_ID).toLowerCase(), true).replace("-", ""));
            } else if (jsonObject.getString(ChildJsonFormUtils.OPENMRS_ENTITY).equalsIgnoreCase(ChildJsonFormUtils.CONCEPT)) {
                jsonObject.put(ChildJsonFormUtils.VALUE,
                        getMappedValue(jsonObject.getString(ChildJsonFormUtils.KEY), childDetails));
            } else {
                jsonObject.put(ChildJsonFormUtils.VALUE,
                        getMappedValue(prefix + jsonObject.getString(ChildJsonFormUtils.OPENMRS_ENTITY_ID),
                                childDetails));
            }

            processLocationTree(childDetails, nonEditableFields, jsonObject);

            if (jsonObject.getString(ChildJsonFormUtils.KEY).equalsIgnoreCase(KipConstants.KEY.MIDDLE_NAME)) {
                String middleName = Utils.getValue(childDetails, KipConstants.KEY.MIDDLE_NAME, true);
                jsonObject.put(ChildJsonFormUtils.VALUE, middleName);
            }
            if (jsonObject.getString(ChildJsonFormUtils.KEY).equalsIgnoreCase(KipConstants.KEY.MOTHER_NRC_NUMBER)) {
                String nidNumber = Utils.getValue(childDetails, KipConstants.KEY.MOTHER_NRC_NUMBER, true);
                jsonObject.put(ChildJsonFormUtils.VALUE, nidNumber);
            }
            if (jsonObject.getString(ChildJsonFormUtils.KEY).equalsIgnoreCase(KipConstants.KEY.MOTHER_SECOND_PHONE_NUMBER)) {
                String secondaryNumber = Utils.getValue(childDetails, KipConstants.KEY.MOTHER_SECOND_PHONE_NUMBER, true);
                jsonObject.put(ChildJsonFormUtils.VALUE, secondaryNumber);
            }

        }
    }

    private static void getDobUnknown(Map<String, String> childDetails, JSONObject jsonObject) throws JSONException {
        JSONObject optionsObject = jsonObject.getJSONArray(Constants.JSON_FORM_KEY.OPTIONS).getJSONObject(0);
        optionsObject.put(ChildJsonFormUtils.VALUE,
                Utils.getValue(childDetails, "dob_unknown", false));
    }

    @NotNull
    private static String getPrefix(JSONObject jsonObject) throws JSONException {
        String prefix;
        prefix = jsonObject.has(ChildJsonFormUtils.ENTITY_ID) && jsonObject.getString(ChildJsonFormUtils.ENTITY_ID)
                .equalsIgnoreCase(KipConstants.KEY.MOTHER) ? KipConstants.KEY.MOTHER_ : "";
        return prefix;
    }

    private static void processLocationTree(Map<String, String> childDetails, List<String> nonEditableFields, JSONObject jsonObject) throws JSONException {
        updateBirthFacilityHierarchy(childDetails, jsonObject);
        if (jsonObject.getString(ChildJsonFormUtils.KEY).equalsIgnoreCase(KipConstants.KEY.BIRTH_FACILITY_NAME_OTHER)) {
            jsonObject
                    .put(ChildJsonFormUtils.VALUE, Utils.getValue(childDetails,
                            KipConstants.KEY.BIRTH_FACILITY_NAME_OTHER, false));
            jsonObject.put(ChildJsonFormUtils.READ_ONLY, true);
        }
        updateResidentialAreaHierarchy(childDetails, jsonObject);
        updateHomeFacilityHierarchy(childDetails, jsonObject);
        addNonEditableFields(nonEditableFields, jsonObject);
    }

    private static void updateBirthFacilityHierarchy(Map<String, String> childDetails, JSONObject jsonObject) throws JSONException {
        if (jsonObject.getString(ChildJsonFormUtils.KEY).equalsIgnoreCase(KipConstants.KEY.BIRTH_FACILITY_NAME)) {
            jsonObject.put(ChildJsonFormUtils.READ_ONLY, true);
            List<String> birthFacilityHierarchy = null;
            String birthFacilityName = Utils.getValue(childDetails, KipConstants.KEY.BIRTH_FACILITY_NAME, false);
            if (birthFacilityName != null) {
                if (birthFacilityName.equalsIgnoreCase(KipConstants.KEY.OTHER)) {
                    birthFacilityHierarchy = new ArrayList<>();
                    birthFacilityHierarchy.add(birthFacilityName);
                } else {
                    birthFacilityHierarchy = LocationHelper.getInstance()
                            .getOpenMrsLocationHierarchy(birthFacilityName, true);
                }
            }

            String birthFacilityHierarchyString = AssetHandler
                    .javaToJsonString(birthFacilityHierarchy, new TypeToken<List<String>>() {
                    }.getType());
            if (StringUtils.isNotBlank(birthFacilityHierarchyString)) {
                jsonObject.put(ChildJsonFormUtils.VALUE, birthFacilityHierarchyString);
            }
        }
    }

    private static void updateResidentialAreaHierarchy(Map<String, String> childDetails, JSONObject jsonObject) throws JSONException {
        if (jsonObject.getString(ChildJsonFormUtils.KEY).equalsIgnoreCase(KipConstants.KEY.RESIDENTIAL_AREA)) {
            List<String> residentialAreaHierarchy;
            String address3 = Utils.getValue(childDetails, KipConstants.KEY.ADDRESS_3, false);
            if (address3 != null && address3.equalsIgnoreCase(KipConstants.KEY.OTHER)) {
                residentialAreaHierarchy = new ArrayList<>();
                residentialAreaHierarchy.add(address3);
            } else {
                residentialAreaHierarchy = LocationHelper.getInstance()
                        .getOpenMrsLocationHierarchy(address3, true);
            }

            String residentialAreaHierarchyString = AssetHandler
                    .javaToJsonString(residentialAreaHierarchy, new TypeToken<List<String>>() {
                    }.getType());
            if (StringUtils.isNotBlank(residentialAreaHierarchyString)) {
                jsonObject.put(ChildJsonFormUtils.VALUE, residentialAreaHierarchyString);
            }
        }
    }

    private static void updateHomeFacilityHierarchy(Map<String, String> childDetails, JSONObject jsonObject) throws JSONException {
        if (jsonObject.getString(ChildJsonFormUtils.KEY).equalsIgnoreCase(KipConstants.KEY.HOME_FACILITY)) {
            List<String> homeFacilityHierarchy = LocationHelper.getInstance()
                    .getOpenMrsLocationHierarchy(Utils.getValue(childDetails,
                            KipConstants.KEY.HOME_FACILITY, false), true);
            String homeFacilityHierarchyString = AssetHandler
                    .javaToJsonString(homeFacilityHierarchy, new TypeToken<List<String>>() {
                    }.getType());
            if (StringUtils.isNotBlank(homeFacilityHierarchyString)) {
                jsonObject.put(ChildJsonFormUtils.VALUE, homeFacilityHierarchyString);
            }
        }
    }

    private static void addNonEditableFields(List<String> nonEditableFields, JSONObject jsonObject) throws JSONException {
        jsonObject.put(ChildJsonFormUtils.READ_ONLY,
                nonEditableFields.contains(jsonObject.getString(ChildJsonFormUtils.KEY)));
    }

    private static void updateRegistrationEventType(JSONObject form) throws JSONException {
        if (form.has(ChildJsonFormUtils.ENCOUNTER_TYPE) && form.getString(ChildJsonFormUtils.ENCOUNTER_TYPE)
                .equals(Constants.EventType.BITRH_REGISTRATION)) {
            form.put(ChildJsonFormUtils.ENCOUNTER_TYPE, Constants.EventType.UPDATE_BITRH_REGISTRATION);
        }

        if (form.has(ChildJsonFormUtils.STEP1) && form.getJSONObject(ChildJsonFormUtils.STEP1).has(KipConstants.KEY.TITLE) && form.getJSONObject(ChildJsonFormUtils.STEP1).getString(KipConstants.KEY.TITLE)
                .equals(Constants.EventType.BITRH_REGISTRATION)) {
            form.getJSONObject(ChildJsonFormUtils.STEP1).put(KipConstants.KEY.TITLE, UPDATE_CHILD_FORM);
        }
    }
    public static String getJsonString(JSONObject jsonObject, String field) {
        try {
            if (jsonObject != null && jsonObject.has(field)) {
                String string = jsonObject.getString(field);
                if (StringUtils.isBlank(string)) {
                    return "";
                }

                return string;
            }
        } catch (JSONException e) {
            Timber.e(e);
        }

        return "";
    }

    public static JSONObject getJsonObject(JSONObject jsonObject, String field) {
        try {
            if (jsonObject != null && jsonObject.has(field)) {
                return jsonObject.getJSONObject(field);
            }
        } catch (JSONException e) {
            Timber.e(e);
        }

        return null;
    }
    public static void addRelationshipTypesQuestions(JSONObject form) {
        String UNIVERSAL_OPENMRS_RELATIONSHIP_TYPE_UUID = "8d91a210-c2cc-11de-8d13-0010c6dffd0f";

        try {
            JSONArray questions = form.getJSONObject("step1").getJSONArray("fields");
            JSONArray relationshipTypes = new JSONObject(AllSettings.fetchRelationshipTypes() ).getJSONArray("relationshipTypes");

            for (int i = 0; i < questions.length(); i++) {
                if (questions.getJSONObject(i).getString("key").equals("Mother_Guardian_Relationship")
                        || questions.getJSONObject(i).getString("key").equals("Father_Guardian_Relationship")) {

                    JSONArray values = new JSONArray();
                    String value = "";

                    if (relationshipTypes != null && relationshipTypes.length() > 0) {
                        for (int n = 0; n < relationshipTypes.length(); n++) {
                            JSONObject rType = new JSONObject(relationshipTypes.getString(n));
                            values.put(rType.getString("name"));
                            if (rType.has("key") && rType.get("key").equals(UNIVERSAL_OPENMRS_RELATIONSHIP_TYPE_UUID)) {
                                value = rType.getString("name");
                            }
                        }
                    }

                    questions.getJSONObject(i).remove(ChildJsonFormUtils.VALUES);
                    questions.getJSONObject(i).put(ChildJsonFormUtils.VALUES, values);
                    // Set the default relationship type.
                    questions.getJSONObject(i).remove(ChildJsonFormUtils.VALUE);
                    questions.getJSONObject(i).put(ChildJsonFormUtils.VALUE, value);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
}