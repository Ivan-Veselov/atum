package ru.spbau.mit.atum;

import android.content.Context;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Даные пользователя, которые могут быть сохранены на Google Drive и прочитаны с него обратно.
 *
 * TODO: javadocs
 * TODO: Internal data leak within a DataBuffer object detected!
 */
public class UserSynchronisableData extends UserData {
    private static UserSynchronisableData instance = null;

    private final String fileName = "atum";

    public static boolean isInstantiated() {
        return instance != null;
    }

    public static UserSynchronisableData getInstance() {
        if (instance == null) {
            instance = new UserSynchronisableData();
        }

        return instance;
    }

    public void saveData(@NonNull final Context context,
                         @NonNull final GoogleApiClient googleApiClient,
                         @NonNull final Callback callback) {
        runHandler(context, googleApiClient, callback,
            new FileHandler() {
                @Override
                public void handle(@NonNull Context context,
                                   @NonNull GoogleApiClient googleApiClient,
                                   @NonNull DriveFile file,
                                   @NonNull Callback callback) {
                    save(context, googleApiClient, file, callback);
                }
            }
        );
    }

    public void loadData(@NonNull final Context context,
                         @NonNull final GoogleApiClient googleApiClient,
                         @NonNull final Callback callback) {
        runHandler(context, googleApiClient, callback,
            new FileHandler() {
                @Override
                public void handle(@NonNull Context context,
                                   @NonNull GoogleApiClient googleApiClient,
                                   @NonNull DriveFile file,
                                   @NonNull Callback callback) {
                    load(context, googleApiClient, file, callback);
                }
            }
        );
    }

    private void runHandler(@NonNull final Context context,
                            @NonNull final GoogleApiClient googleApiClient,
                            @NonNull final Callback callback,
                            @NonNull final FileHandler handler) {
        final DriveFolder appFolder = Drive.DriveApi.getAppFolder(googleApiClient);

        final ResultCallback<DriveFolder.DriveFileResult> driveFileCallback = new
                ResultCallback<DriveFolder.DriveFileResult>() {
                    @Override
                    public void onResult(@NonNull DriveFolder.DriveFileResult result) {
                        if (!result.getStatus().isSuccess()) {
                            // TODO: Handle error
                            return;
                        }

                        save(context, googleApiClient, result.getDriveFile(), callback);
                    }
                };

        final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new
                ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            // TODO: Handle error
                            return;
                        }

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle(fileName)
                                .build();

                        appFolder.createFile(googleApiClient,
                                             changeSet,
                                             result.getDriveContents())
                                 .setResultCallback(driveFileCallback);
                    }
                };

        Query query = new Query.Builder()
                               .addFilter(Filters.eq(SearchableField.TITLE, fileName))
                               .build();

        appFolder.queryChildren(googleApiClient, query)
                 .setResultCallback(
                 new ResultCallback<DriveApi.MetadataBufferResult>() {
                     @Override
                     public void onResult(DriveApi.MetadataBufferResult result) {
                         MetadataBuffer mdResultSet = result.getMetadataBuffer();
                         int resultCount = mdResultSet.getCount();

                         if (resultCount == 0) {
                             Drive.DriveApi.newDriveContents(googleApiClient)
                                     .setResultCallback(driveContentsCallback);
                         } else {
                             // TODO: Handle ambiguity
                             handler.handle(context,
                                            googleApiClient,
                                            mdResultSet.get(0).getDriveId().asDriveFile(),
                                            callback);

                         }
                     }
                 });
    }

    private void saveToOutputStream(@NonNull OutputStream stream) throws IOException {
        Parcel parcel = Parcel.obtain();

        parcel.writeList(tasks);
        parcel.writeList(blockers);

        stream.write(parcel.marshall());
        parcel.recycle();
    }

    private void loadFromInputStream(@NonNull InputStream stream)
            throws IOException, ClassNotFoundException {
        byte[] data = new byte[stream.available()];
        stream.read(data);

        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(data, 0, data.length);
        parcel.setDataPosition(0);

        tasks = new ArrayList<> ();
        blockers = new ArrayList<> ();

        parcel.readList(tasks, UserDefinedTask.class.getClassLoader());
        parcel.readList(blockers, UserDefinedTimeBlocker.class.getClassLoader());

        parcel.recycle();
    }

    private void save(@NonNull final Context context,
                      @NonNull final GoogleApiClient googleApiClient,
                      @NonNull DriveFile file,
                      @NonNull final Callback callback) {
        file.open(googleApiClient, DriveFile.MODE_WRITE_ONLY, null).setResultCallback(
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        // TODO: handle error
                        return;
                    }

                    DriveContents contents = result.getDriveContents();

                    try {
                        saveToOutputStream(contents.getOutputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    contents.commit(googleApiClient, null).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status result) {
                                callback.call();
                            }
                        }
                    );
                }
            }
        );
    }

    private void load(@NonNull final Context context,
                      @NonNull final GoogleApiClient googleApiClient,
                      @NonNull DriveFile file,
                      @NonNull final Callback callback) {
        file.open(googleApiClient, DriveFile.MODE_READ_ONLY, null).setResultCallback(
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        // TODO: handle error
                        return;
                    }

                    DriveContents contents = result.getDriveContents();

                    try {
                        loadFromInputStream(contents.getInputStream());
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    contents.discard(googleApiClient);
                    callback.call();
                }
            }
        );
    }

    private UserSynchronisableData() {
    }

    public interface Callback {
        void call();
    }

    private interface FileHandler {
        void handle(@NonNull final Context context,
                    @NonNull final GoogleApiClient googleApiClient,
                    @NonNull DriveFile file,
                    @NonNull final Callback callback);
    }
}
