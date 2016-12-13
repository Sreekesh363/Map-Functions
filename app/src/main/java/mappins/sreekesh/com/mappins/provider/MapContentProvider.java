package mappins.sreekesh.com.mappins.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import mappins.sreekesh.com.mappins.model.Contract;
import mappins.sreekesh.com.mappins.utils.DBHelper;

public class MapContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int MAP_DATA = 100;

    private DBHelper dbHelper;

    public MapContentProvider() {
    }

    private static UriMatcher buildUriMatcher(){

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, Contract.PATH_MAP_DATA, MAP_DATA);
        return uriMatcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int row_num;
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){

            case MAP_DATA:
                row_num = db.delete(Contract.MapDataEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unable to perform delete on uri: "+uri);
        }

        if(selection == null || row_num != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return row_num;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MAP_DATA:
                return Contract.MapDataEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unsupported Uri: "+ uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final Uri retUri;
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){

            case MAP_DATA: {
                long _id = db.insert(
                        Contract.MapDataEntry.TABLE_NAME,
                        null,
                        values
                );
                if (_id > 0) {
                    retUri = Contract.MapDataEntry.buildMapDataUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into:" + uri);
                }
            }
            break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor  cursor;
        switch (sUriMatcher.match(uri)){
            case MAP_DATA:
                cursor = dbHelper.getReadableDatabase().query(
                        Contract.MapDataEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final int row_num;
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){

            case MAP_DATA:
                row_num = db.update(Contract.MapDataEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unable to perform update on uri: "+uri);
        }

        if(row_num != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return row_num;
    }
}
