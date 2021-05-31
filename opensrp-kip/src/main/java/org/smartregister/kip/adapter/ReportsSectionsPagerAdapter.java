package org.smartregister.kip.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.smartregister.kip.R;
import org.smartregister.kip.activity.HIA2ReportsActivity;
import org.smartregister.kip.fragment.DailyTalliesFragment;
import org.smartregister.kip.fragment.DraftMonthlyFragment;
import org.smartregister.kip.fragment.SentMonthlyFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ReportsSectionsPagerAdapter extends FragmentPagerAdapter {

    private HIA2ReportsActivity hia2ReportsActivity;

    private SentMonthlyFragment sentMonthlyFragment;

    public ReportsSectionsPagerAdapter(HIA2ReportsActivity hia2ReportsActivity, FragmentManager fm) {
        super(fm);
        this.hia2ReportsActivity = hia2ReportsActivity;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return DailyTalliesFragment.newInstance(hia2ReportsActivity.getReportGrouping());
            case 1:
                return DraftMonthlyFragment.newInstance(hia2ReportsActivity.getReportGrouping());
            case 2:
                return getSentMonthlyFragment();
            default:
                break;
        }
        return null;
    }

    public SentMonthlyFragment getSentMonthlyFragment(){
        if(sentMonthlyFragment == null){
            sentMonthlyFragment = SentMonthlyFragment.newInstance(hia2ReportsActivity.getReportGrouping());
        }
        return sentMonthlyFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return hia2ReportsActivity.getString(R.string.hia2_daily_tallies);
            case 1:
                return hia2ReportsActivity.getString(R.string.hia2_draft_monthly);
            case 2:
                return hia2ReportsActivity.getString(R.string.hia2_sent_monthly);
            default:
                break;
        }
        return null;
    }
}
