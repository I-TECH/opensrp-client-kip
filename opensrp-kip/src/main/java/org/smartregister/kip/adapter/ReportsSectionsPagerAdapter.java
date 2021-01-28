package org.smartregister.kip.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
//            case 2:
//                return SentMonthlyFragment.newInstance(hia2ReportsActivity.getReportGrouping());
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return hia2ReportsActivity.getString(R.string.moh_daily_tally);
            case 1:
                return hia2ReportsActivity.getString(R.string.moh_monthly_report);
            default:
                break;
        }
        return null;
    }
}
