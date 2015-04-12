package org.vas.notification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import org.vas.domain.repository.Address;
import org.vas.notification.domain.repository.impl.NotificationRepositoryImpl;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "notifications", daoClass = NotificationRepositoryImpl.class)
public class Notification {

  @DatabaseField(generatedId = true)
  public int id;

  @DatabaseField(columnName = "kind", dataType = DataType.STRING)
  public String type = "mail";

  @DatabaseField
  public int hour;

  @DatabaseField
  public int min = 00;

  @DatabaseField
  public int dist = 500; // Default to 500m

  @DatabaseField(foreign = true, canBeNull = false)
  public Address address;

  public boolean isValidTime() {
    return (hour >= 0 && hour < 24) && (min >= 0 && min < 60);
  }

  public boolean isTime(LocalDateTime time) {
    int theHour = time.get(ChronoField.HOUR_OF_DAY);
    int theMin = time.get(ChronoField.MINUTE_OF_HOUR);

    return hour == theHour && min == theMin;
  }

  @Override
  public String toString() {
    return "Notification [id=" + id + ", type=" + type + ", hour=" + hour + ", min=" + min + ", dist=" + dist
      + ", address=" + address.id + "]";
  }
}
