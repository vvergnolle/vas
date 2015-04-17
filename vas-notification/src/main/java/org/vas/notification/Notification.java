/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vincent Vergnolle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

  @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
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
