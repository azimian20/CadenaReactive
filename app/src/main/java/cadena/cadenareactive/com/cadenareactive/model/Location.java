package cadena.cadenareactive.com.cadenareactive.model;

import java.util.Date;

import cadena.cadenareactive.util.DateStringFormatter;

public class Location extends DomainEntity {

    private String nmeaGps;

    private String gMapGps;

    public Location(String dateTime, String description, Long deviceId, String gMapGps, Long idLong, String messageId, String nmeaGps) {
        System.out.println("---------Location constructor");
        super.setDescription(description);
        super.setIdLong(idLong);
        this.nmeaGps = nmeaGps;
        this.gMapGps = gMapGps;
        this.dateTime = dateTime;//DateStringFormatter.format(dateTime);
        this.messageId = messageId;
        this.deviceId = deviceId;
    }

    private String dateTime;

    private String messageId;

    private Long deviceId;

    public String getDateTime() {
        System.out.println("-----------Location dateTime getter");
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;//DateStringFormatter.format(dateTime);
    }

    public String getNmeaGps() {
        return nmeaGps;
    }

    public void setNmeaGps(String nmeaGps) {
        this.nmeaGps = nmeaGps;
    }

    public String getgMapGps() {
        return gMapGps;
    }

    public void setgMapGps(String gMapGps) {
        this.gMapGps = gMapGps;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }


}
