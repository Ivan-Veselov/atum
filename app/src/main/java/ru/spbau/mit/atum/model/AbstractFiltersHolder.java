package ru.spbau.mit.atum.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Абстрактный класс, описывающий объекты, которые состоят из набора фильтров. Типичный пример
 * такого объекта - задача.
 */
public abstract class AbstractFiltersHolder implements Parcelable {
    private final String name;

    private final String description;

    private final FilterSet filterSet;

    /**
     * Конструктор абстрактного объекта, содержащего набор фильтров.
     *
     * @param name имя объекта.
     * @param description описание объекта.
     * @param filterList набор фильтров.
     */
    public AbstractFiltersHolder(@NonNull String name, @NonNull String description,
                                 @NonNull List<TimeFilter> filterList) {
        this.name = name;
        this.description = description;
        this.filterSet = new FilterSet(filterList);
    }

    /**
     * @return имя объекта.
     */
    public @NonNull String getName() {
        return name;
    }

    /**
     * @return описание объекта.
     */
    public @NonNull String getDescription() {
        return description;
    }

    /**
     * @return набор фильтров.
     */
    public @NonNull FilterSet getFilterSet() {
        return filterSet;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(description);
        out.writeParcelable(filterSet, flags);
    }

    protected AbstractFiltersHolder(Parcel in) {
        name = in.readString();
        description = in.readString();
        filterSet = in.readParcelable(getClass().getClassLoader());
    }
}
