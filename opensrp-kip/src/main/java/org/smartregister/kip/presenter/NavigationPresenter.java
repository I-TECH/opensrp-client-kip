package org.smartregister.kip.presenter;

import android.app.Activity;

import org.smartregister.kip.contract.NavigationContract;
import org.smartregister.kip.interactor.NavigationInteractor;
import org.smartregister.kip.model.NavigationModel;
import org.smartregister.kip.model.NavigationOption;
import org.smartregister.kip.util.KipConstants;
import org.smartregister.growthmonitoring.job.HeightIntentServiceJob;
import org.smartregister.growthmonitoring.job.WeightIntentServiceJob;
import org.smartregister.growthmonitoring.job.ZScoreRefreshIntentServiceJob;
import org.smartregister.immunization.job.VaccineServiceJob;
import org.smartregister.job.ImageUploadServiceJob;
import org.smartregister.job.SyncServiceJob;
import org.smartregister.job.SyncSettingsServiceJob;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class NavigationPresenter implements NavigationContract.Presenter {

    private NavigationContract.Model mModel;
    private NavigationContract.Interactor mInteractor;
    private WeakReference<NavigationContract.View> mView;

    private HashMap<String, String> tableMap = new HashMap<>();

    public NavigationPresenter(NavigationContract.View view) {
        mView = new WeakReference<>(view);
        mInteractor = NavigationInteractor.getInstance();
        mModel = NavigationModel.getInstance();
        initialize();
    }

    private void initialize() {
        tableMap.put(KipConstants.DrawerMenu.ALL_FAMILIES, KipConstants.TABLE_NAME.FAMILY);
        tableMap.put(KipConstants.DrawerMenu.CHILD_CLIENTS, KipConstants.TABLE_NAME.CHILD);
        tableMap.put(KipConstants.DrawerMenu.ANC_CLIENTS, KipConstants.TABLE_NAME.MOTHER_TABLE_NAME);
        tableMap.put(KipConstants.DrawerMenu.ANC, KipConstants.TABLE_NAME.ANC_MEMBER);
        tableMap.put(KipConstants.DrawerMenu.ALL_CLIENTS, String.format("%s|%s", KipConstants.TABLE_NAME.CHILD, KipConstants.TABLE_NAME.MOTHER_TABLE_NAME));
    }

    @Override
    public NavigationContract.View getNavigationView() {
        return mView.get();
    }

    @Override
    public void refreshNavigationCount() {
        int navigationItems = 0;
        while (navigationItems < mModel.getNavigationItems().size()) {
            final int finalNavigationItems = navigationItems;
            String menuTitle = mModel.getNavigationItems().get(navigationItems).getMenuTitle();
            mInteractor.getRegisterCount(tableMap.get(menuTitle),
                    new NavigationContract.InteractorCallback<Integer>() {
                        @Override
                        public void onResult(Integer result) {
                            mModel.getNavigationItems().get(finalNavigationItems).setRegisterCount(result);
                            getNavigationView().refreshCount();
                        }

                        @Override
                        public void onError(Exception e) {
                            Timber.e(e, "Error retrieving count for %s",
                                    tableMap.get(mModel.getNavigationItems().get(finalNavigationItems).getMenuTitle()));
                        }
                    });
            navigationItems++;
        }

    }

    @Override
    public void refreshLastSync() {
        // get last sync date
        getNavigationView().refreshLastSync(mInteractor.sync());
    }

    @Override
    public void displayCurrentUser() {
        getNavigationView().refreshCurrentUser(mModel.getCurrentUser());
    }

    @Override
    public void sync(Activity activity) {
        ImageUploadServiceJob.scheduleJobImmediately(ImageUploadServiceJob.TAG);
        SyncServiceJob.scheduleJobImmediately(SyncServiceJob.TAG);
        SyncSettingsServiceJob.scheduleJobImmediately(SyncSettingsServiceJob.TAG);
        ZScoreRefreshIntentServiceJob.scheduleJobImmediately(ZScoreRefreshIntentServiceJob.TAG);
        WeightIntentServiceJob.scheduleJobImmediately(WeightIntentServiceJob.TAG);
        HeightIntentServiceJob.scheduleJobImmediately(HeightIntentServiceJob.TAG);
        VaccineServiceJob.scheduleJobImmediately(VaccineServiceJob.TAG);
    }

    @Override
    public List<NavigationOption> getOptions() {
        return mModel.getNavigationItems();
    }
}
