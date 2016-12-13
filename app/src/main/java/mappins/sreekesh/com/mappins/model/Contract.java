package mappins.sreekesh.com.mappins.model;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sree on 12/12/16.
 */

public class Contract {
    public static final String CONTENT_AUTHORITY = "mappins.sreekesh.com.mappins";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_MAP_DATA = "map_data";

    public static final class MapDataEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MAP_DATA).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MAP_DATA;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_MAP_DATA;

        public static final String TABLE_NAME = "map_data";
        public static final String COLUMN_MAP_DATA_ID = "map_data_id";
        public static final String COLUMN_MAP_DATA_NAME = "map_data_name";
        public static final String COLUMN_MAP_DATA_LATITUDE = "map_data_latitude";
        public static final String COLUMN_MAP_DATA_LONGITUDE = "map_data_longitude";
        public static final String COLUMN_MAP_DATA_ADDRESS = "map_data_address";

        public static final Uri buildMapDataUri(long _id){
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
    }

}
