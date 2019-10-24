package org.smartregister.kip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.smartregister.child.activity.BaseChildRegisterActivity;
import org.smartregister.child.enums.LocationHierarchy;
import org.smartregister.child.model.BaseChildRegisterModel;
import org.smartregister.child.presenter.BaseChildRegisterPresenter;
import org.smartregister.child.util.Constants;
import org.smartregister.child.util.JsonFormUtils;
import org.smartregister.child.util.Utils;
import org.smartregister.kip.R;
import org.smartregister.kip.event.LoginEvent;
import org.smartregister.kip.fragment.AdvancedSearchFragment;
import org.smartregister.kip.fragment.ChildRegisterFragment;
import org.smartregister.kip.util.KipConstants;
import org.smartregister.kip.util.KipUtils;
import org.smartregister.kip.view.NavigationMenu;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.lang.ref.WeakReference;

public class ChildRegisterActivity extends BaseChildRegisterActivity {

    @Override
    protected void attachBaseContext(android.content.Context base) {
        // get language from prefs
        String lang = KipUtils.getLanguage(base.getApplicationContext());
        super.attachBaseContext(KipUtils.setAppLocale(base, lang));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void registerBottomNavigation() {
        super.registerBottomNavigation();

        MenuItem clients = bottomNavigationView.getMenu().findItem(org.smartregister.child.R.id.action_clients);
        if (clients != null) {
            clients.setTitle(getString(org.smartregister.child.R.string.header_children));
        }
        bottomNavigationView.getMenu().removeItem(R.id.action_scan_qr);
        bottomNavigationView.getMenu().removeItem(R.id.action_scan_card);
    }

    @Override
    protected Fragment[] getOtherFragments() {
        ADVANCED_SEARCH_POSITION = 1;

        Fragment[] fragments = new Fragment[1];
        fragments[ADVANCED_SEARCH_POSITION - 1] = new AdvancedSearchFragment();

        return fragments;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public String getRegistrationForm() {
        return KipConstants.JSON_FORM.CHILD_ENROLLMENT;
    }

    @Override
    public void startNFCCardScanner() {
        // Todo
    }

    @Override
    protected void initializePresenter() {
        presenter = new BaseChildRegisterPresenter(this, new BaseChildRegisterModel());
    }

    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        WeakReference<ChildRegisterFragment> childRegisterFragmentWeakReference = new WeakReference<>(
                new ChildRegisterFragment());

        return childRegisterFragmentWeakReference.get();
    }

    @Override
    protected void onResumption() {
        super.onResumption();
        openDrawer();
    }

    public void openDrawer() {
        NavigationMenu navigationMenu = NavigationMenu.getInstance(this, null, null);
        navigationMenu.getNavigationAdapter().setSelectedView(KipConstants.DrawerMenu.CHILD_CLIENTS);
        navigationMenu.runRegisterCount();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void showNfcNotInstalledDialog(LoginEvent event) {
        if (event != null) {
            KipUtils.removeStickyEvent(event);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showNfcDialog();
                }
            });

        }
    }

    private void showNfcDialog() {
        KipUtils.showDialogMessage(this, R.string.nfc_sdk_missing, R.string.please_install_nfc_sdk);
    }

    public void refresh() {
        Intent intent = new Intent(ChildRegisterActivity.this, ChildRegisterActivity.class);
        getApplicationContext()
                .startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, Utils.metadata().childFormActivity);
        if (jsonForm.has(KipConstants.KEY.ENCOUNTER_TYPE) && jsonForm.optString(KipConstants.KEY.ENCOUNTER_TYPE).equals(
                KipConstants.KEY.BIRTH_REGISTRATION)) {
            JsonFormUtils.addChildRegLocHierarchyQuestions(jsonForm, KipConstants.KEY.REGISTRATION_HOME_ADDRESS, LocationHierarchy.ENTIRE_TREE);
        }
        intent.putExtra(Constants.INTENT_KEY.JSON, jsonForm.toString());

        Form form = new Form();
        form.setWizard(false);
        form.setHideSaveLabel(true);
        form.setNextLabel("");

        intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, form);
        startActivityForResult(intent, JsonFormUtils.REQUEST_CODE_GET_JSON);
    }
}
