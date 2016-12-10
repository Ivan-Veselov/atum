package ru.spbau.mit.atum;

import android.content.Context;
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
import java.util.List;

/**
 * Даные пользователя, которые могут быть сохранены на Google Drive и прочитаны с него обратно.
 *
 * TODO: сделать интерфейс с callback методом
 * TODO: javadocs
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
                         @NonNull final GoogleApiClient googleApiClient) {
        runHandler(context, googleApiClient,
            new FileHandler() {
                @Override
                public void handle(@NonNull Context context,
                                   @NonNull GoogleApiClient googleApiClient,
                                   @NonNull DriveFile file) {
                    save(context, googleApiClient, file);
                }
            }
        );
    }

    public void loadData(@NonNull final Context context,
                         @NonNull final GoogleApiClient googleApiClient) {
        runHandler(context, googleApiClient,
            new FileHandler() {
                @Override
                public void handle(@NonNull Context context,
                                   @NonNull GoogleApiClient googleApiClient,
                                   @NonNull DriveFile file) {
                    load(context, googleApiClient, file);
                }
            }
        );
    }

    private void runHandler(@NonNull final Context context,
                            @NonNull final GoogleApiClient googleApiClient,
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

                        save(context, googleApiClient, result.getDriveFile());
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
                                            mdResultSet.get(0).getDriveId().asDriveFile());

                         }
                     }
                 });
    }

    private void saveToOutputStream(@NonNull OutputStream stream) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(stream);

        outputStream.writeObject(tasks);
        outputStream.writeObject(blockers);
    }

    private void loadFromInputStream(@NonNull InputStream stream)
            throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(stream);
        tasks = (List<UserDefinedTask>) inputStream.readObject();
        blockers = (List<UserDefinedTimeBlocker>) inputStream.readObject();
    }

    private void save(@NonNull final Context context,
                      @NonNull final GoogleApiClient googleApiClient,
                      @NonNull DriveFile file) {
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
                                Toast.makeText(context,
                                        "Data uploaded",
                                        Toast.LENGTH_LONG).show();

                                googleApiClient.disconnect();
                            }
                        }
                    );
                }
            }
        );
    }

    private void load(@NonNull final Context context,
                      @NonNull final GoogleApiClient googleApiClient,
                      @NonNull DriveFile file) {
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
                    Toast.makeText(context, "Data downloaded", Toast.LENGTH_LONG).show();
                    googleApiClient.disconnect();
                }
            }
        );
    }

    private UserSynchronisableData() {
    }

    private interface FileHandler {
        void handle(@NonNull final Context context,
                    @NonNull final GoogleApiClient googleApiClient,
                    @NonNull DriveFile file);
    }
}
