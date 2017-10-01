package util;

import org.smartregister.AllConstants;
import org.smartregister.kip.BuildConfig;
import org.smartregister.kip.application.KipApplication;

/**
 * Created by coder on 2/14/17.
 */
public class KipConstants extends AllConstants {
    private static final String OPENMRS_URL = BuildConfig.OPENMRS_URL;
    public static final int DATABASE_VERSION = BuildConfig.DATABASE_VERSION;

    public static final String OPENMRS_IDGEN_URL = BuildConfig.OPENMRS_IDGEN_URL;
    public static final int OPENMRS_UNIQUE_ID_INITIAL_BATCH_SIZE = BuildConfig.OPENMRS_UNIQUE_ID_INITIAL_BATCH_SIZE;
    public static final int OPENMRS_UNIQUE_ID_BATCH_SIZE = BuildConfig.OPENMRS_UNIQUE_ID_BATCH_SIZE;
    public static final int OPENMRS_UNIQUE_ID_SOURCE = BuildConfig.OPENMRS_UNIQUE_ID_SOURCE;
    public static final long MAX_SERVER_TIME_DIFFERENCE = BuildConfig.MAX_SERVER_TIME_DIFFERENCE;
    public static final boolean TIME_CHECK = BuildConfig.TIME_CHECK;

    public static final String CHILD_TABLE_NAME = "ec_child";
    public static final String MOTHER_TABLE_NAME = "ec_mother";
    public static final String CURRENT_LOCATION_ID = "CURRENT_LOCATION_ID";

    public static final String DEFAULT_DATE_STRING = "1970-1-1";

    public static String openmrsUrl() {
        String baseUrl = KipApplication.getInstance().context().allSharedPreferences().fetchBaseURL("");
        int lastIndex = baseUrl.lastIndexOf("/");
        baseUrl = baseUrl.substring(0, lastIndex) + "/openmrs";
        return OPENMRS_URL.isEmpty() || OPENMRS_URL == null ? baseUrl : OPENMRS_URL;
    }

    public static final class ServiceType {
        public static final int DATA_SYNCHRONIZATION = 1;
        public static final int DAILY_TALLIES_GENERATION = 2;
        public static final int MONTHLY_TALLIES_GENERATION = 3;
        public static final int PULL_UNIQUE_IDS = 4;
        public static final int VACCINE_SYNC_PROCESSING = 5;
        public static final int WEIGHT_SYNC_PROCESSING = 6;
        public static final int RECURRING_SERVICES_SYNC_PROCESSING = 7;
    }


    public static final class EventType {
        public static final String DEATH = "Death";
    }

    public static final class EntityType {
        public static final String CHILD = "child";
    }

    public static final class EC_CHILD_TABLE {
        public static final String DOD = "dod";
        public static final String GENDER = "gender";
        public static final String DUE_DATE = "due_date";
        public static final String CHW_NAME = "chw_name";
        public static final String CHW_PHONE_NUMBER = "chw_phone_number";
    }

    public static final String CHILD_ENROLLMENT = "Child Enrollment";
}
