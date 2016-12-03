package ru.spbau.mit.atum;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Даные пользователя, которые могут быть сохранены на диск и прочитаны с него обратно.
 */
public class UserSynchronisableData extends UserData {
    private final Context context;

    private final String fileName;

    /**
     * Создает новый набор данных. Данные загружаются из файла, имя которого передается в качестве
     * аргумента. В дальнейшем все данные будут сохраняться именно в этот файл.
     *
     * @param context контекст приложения.
     * @param fileName имя файла, который будет привязан к этому объекту.
     */
    public UserSynchronisableData(@NonNull Context context, @NonNull String fileName)
            throws IOException, ClassNotFoundException {
        this.context = context;
        this.fileName = fileName;

        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) {
            tasks = new ArrayList<>();
            blockers = new ArrayList<>();

            return;
        }

        ObjectInputStream inputStream = new ObjectInputStream(context.openFileInput(fileName));
        tasks = (List<UserDefinedTask>) inputStream.readObject();
        blockers = (List<UserDefinedTimeBlocker>) inputStream.readObject();
    }

    /**
     * Сохраняет данные в файл, который был привязан к данному объекту при конструировании.
     *
     * @throws IOException любое исключение ввода-вывода, которое может произойти во время
     * сериализации объектов в файл.
     */
    public void saveData() throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(
                context.openFileOutput(fileName, Context.MODE_PRIVATE));

        outputStream.writeObject(tasks);
        outputStream.writeObject(blockers);
    }
}
