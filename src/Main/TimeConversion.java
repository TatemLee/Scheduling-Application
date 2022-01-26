package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.Timestamp;
import java.time.*;


/**
 * This class holds methods that support time conversion features in the application.
 */
public class TimeConversion {



    /**
     * This method creates a ZonedDateTime based on user input and location
     * @param date user input date
     * @param hour user input hour
     * @param min user input min
     * @return user's ZonedDateTime
     */
   public static ZonedDateTime createUserZonedDateTime(LocalDate date, int hour, int min) {
       //establish user DateTime
       LocalTime userLocalTime = LocalTime.of(hour, min);
       LocalDate userLocalDate = date;
       LocalDateTime userLocalDateTime = LocalDateTime.of(userLocalDate, userLocalTime);
        //establish user ZoneId
       ZoneId userZoneId = ZoneId.systemDefault();
       //create Zoned Date Time
       ZonedDateTime userZonedDateTime = ZonedDateTime.of(userLocalDateTime, userZoneId);

       return userZonedDateTime;
   }

    /**
     * This method converts a given ZonedDateTime to UTC
     * @param inputZDT ZonedDateTime to be converted to UTC
     * @return ZonedDateTime in UTC
     */
   public static ZonedDateTime convertToUTC(ZonedDateTime inputZDT) {
       //this is a test line
       if(inputZDT.getZone() == ZoneId.of("UTC")) {
           return inputZDT;
       }

        //create reference for UTC timezone
       ZoneId utcReference = ZoneId.of("UTC");
       //convert input time to UTC
       ZonedDateTime outputZDT = ZonedDateTime.ofInstant(inputZDT.toInstant(), utcReference);

       return outputZDT;
   }

    /**
     * This method takes a timestamp recorded in UTC and converts it to as ZonedDateTime in the user's time zone
     * @param timestamp UTC timestamp
     * @return user ZonedDateTime
     */
    public static ZonedDateTime timestampToUserZonedDateTime(Timestamp timestamp) {
     ZoneId userZoneId = ZoneId.systemDefault();
     ZoneId utcZoneId = ZoneId.of("UTC");
     ZonedDateTime utcZDT = ZonedDateTime.of(timestamp.toLocalDateTime(), utcZoneId);
     ZonedDateTime userZonedDateTime = ZonedDateTime.ofInstant(utcZDT.toInstant(), userZoneId);
        ZonedDateTime outputZDT = ZonedDateTime.of(timestamp.toLocalDateTime(), userZoneId);

       return outputZDT;
    }



    /**
     * This method converts a ZonedDateTime to a timestamp
     * @param ZDT ZonedDateTime
     * @return TimeStamp
     */
    public static Timestamp ZonedDateTimeToTimeStamp(ZonedDateTime ZDT) {
       LocalDateTime temporaryPackage = ZDT.toLocalDateTime();
       Timestamp outputTimestamp = Timestamp.valueOf(temporaryPackage);
       return outputTimestamp;
    }


    /**
     * This method extracts a LocalDate from a TimeStamp
     * @param timestamp input timestamp
     * @return LocalDate
     */
    public static LocalDate TimeStampToLocalDate(java.sql.Timestamp timestamp) {
       LocalDate outputDate = timestamp.toLocalDateTime().toLocalDate();
       return outputDate;
    }

    /**
     * This method populates the list of TimeStamps in a user's local time that a user may choose from when adding/updating an appointment.
     * The TimeStamps are generated based on Business Hours in EST so that users cannot select a time out of Business Hours
     * @return all office hours in the user's timezone
     */
    public static ObservableList<LocalTime> populateTimeStampList() {
        ObservableList<LocalTime> allOfficeHours = FXCollections.observableArrayList();
        //establish office hours start time in UTC
        LocalTime estLocalTime = LocalTime.of(8, 00);
        LocalDate estLocalDate = LocalDate.of(2021, 12, 13);
        LocalDateTime utcDateTime = LocalDateTime.of(estLocalDate, estLocalTime);
        ZoneId estZoneID = ZoneId.of("America/New_York");
        ZonedDateTime estZDT = ZonedDateTime.of(utcDateTime, estZoneID);

        //establish local time zone
        ZoneId userZoneID = ZoneId.systemDefault();
        //convert UTC starting time to user timezone
        ZonedDateTime userZDT = ZonedDateTime.ofInstant(estZDT.toInstant(), userZoneID);

        //create timestamp to use in loop
        LocalTime loopTimeStamp = userZDT.toLocalTime();
        //add starting time
        allOfficeHours.add(loopTimeStamp);
        //loop to generate all potential appointment times. The value is hard coded because business hours in the scope of this project are final
        for (int i = 0; i < 168; i++) {
            loopTimeStamp = loopTimeStamp.plusMinutes(5);
            allOfficeHours.add(loopTimeStamp);
        }
        return allOfficeHours;
    }


    /**
     * This method finds the most recent Monday based on the input Timestamp
     * @param timestamp input timestamp
     * @return Timestamp most recent Monday
     */
    public static Timestamp identifyMonday (Timestamp timestamp) {
        int dayOfWeek = timestamp.toLocalDateTime().getDayOfWeek().getValue();
        LocalDateTime compLDT;
        if(dayOfWeek > 1) {
            dayOfWeek -= 1;
            compLDT = timestamp.toLocalDateTime().minusDays(dayOfWeek);
        }
        else {
            compLDT = timestamp.toLocalDateTime();
        }

       Timestamp outputTimestamp = Timestamp.valueOf(compLDT);
       return outputTimestamp;
    }

    /**
     * This method finds the soonest upcoming Sunday based on the input Timestamp
     * @param timestamp input timestamp
     * @return Timestamp soonest upcoming Sunday
     */
    public static Timestamp identifySunday (Timestamp timestamp) {
        int dayOfWeek = timestamp.toLocalDateTime().getDayOfWeek().getValue();
        LocalDateTime compLDT;
        if(dayOfWeek < 7) {
            dayOfWeek = 7 - dayOfWeek;
            compLDT = timestamp.toLocalDateTime().plusDays(dayOfWeek);
        }
        else {
            compLDT = timestamp.toLocalDateTime();
        }

        Timestamp outputTimestamp = Timestamp.valueOf(compLDT);
        return outputTimestamp;
    }



}
