package org.smartregister.kip.repository;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.anc.library.repository.RegisterQueryProvider;

public class KipAncRegisterQueryProvider extends RegisterQueryProvider {

    @Override
    public String getObjectIdsQuery(String tempMainCondition, String tempFilters) {
        String mainCondition = tempMainCondition;
        String filters = tempFilters;
        if (!filters.isEmpty()) {
            filters = String.format(" and ec_client_search.phrase MATCH '*%s*'", filters);
        }

        if (!StringUtils.isBlank(mainCondition)) {
            mainCondition = " and " + mainCondition;
        }

        return "select ec_client_search.object_id from ec_client_search " +
                "join ec_mother_details on ec_client_search.object_id =  ec_mother_details.id " +
                "join client_register_type on ec_client_search.object_id=client_register_type.base_entity_id " +
                "where register_type='anc' " + mainCondition + filters;
    }

    public String getCountExecuteQuery(String tempMainCondition, String tempFilters) {
        String filters = tempFilters;
        String mainCondition = tempMainCondition;
        if (!filters.isEmpty()) {
            filters = String.format(" and ec_client_search.phrase MATCH '*%s*'", filters);
        }

        if (!StringUtils.isBlank(mainCondition)) {
            mainCondition = " and " + mainCondition;
        }

        return "select count(ec_client_search.object_id) from ec_client_search " +
                "join ec_mother_details on ec_client_search.object_id =  ec_mother_details.id " +
                "join client_register_type on ec_client_search.object_id=client_register_type.base_entity_id " +
                "where register_type='anc' " + mainCondition + filters;
    }

}
