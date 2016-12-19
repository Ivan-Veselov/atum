package ru.spbau.mit.atum;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Объект этого класса представляет собой набор фильтров, которые задают некоторое подмножество
 * временной прямой. Это подмножество означает моменты времени, в которые пользователь не хочет
 * выполнять задания.
 */
public class UserDefinedTimeBlocker extends AbstractFiltersHolder {
    /**
     * @param name имя объекта.
     * @param description описание объекта.
     * @param filterList набор фильтров.
     */
    public UserDefinedTimeBlocker(@NonNull String name,
                                  @NonNull String description,
                                  @NonNull List<TimeFilter> filterList) {
        super(name, description, filterList);
    }

    public static final Parcelable.Creator<UserDefinedTimeBlocker> CREATOR
            = new Parcelable.Creator<UserDefinedTimeBlocker>() {
        public UserDefinedTimeBlocker createFromParcel(Parcel in) {
            return new UserDefinedTimeBlocker(in);
        }

        public UserDefinedTimeBlocker[] newArray(int size) {
            return new UserDefinedTimeBlocker[size];
        }
    };

    protected UserDefinedTimeBlocker(Parcel in) {
        super(in);
    }
}
