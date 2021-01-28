package org.smartregister.kip.fragment;

import org.smartregister.opd.utils.ConfigurationInstancesHelper;
import org.smartregister.kip.configuration.AllClientsRegisterQueryProvider;


public class AllClientsRegisterFragment extends OpdRegisterFragment {

    public AllClientsRegisterFragment() {
        super();
        setOpdRegisterQueryProvider(ConfigurationInstancesHelper.newInstance(AllClientsRegisterQueryProvider.class));
    }
}