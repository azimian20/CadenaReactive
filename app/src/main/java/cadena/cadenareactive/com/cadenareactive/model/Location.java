package cadena.cadenareactive.com.cadenareactive.model;

import java.util.Date;

public class Location {

    private String dateTime;
    private String description;
    private String gMapGps;
    private Long idLong;
    private String nmeaGps;

    public Location(String dateTime, String description, String gMapGps, Long idLong, String nmeaGps) {
        this.dateTime = dateTime;
        this.description = description;
        this.gMapGps = gMapGps;
        this.idLong = idLong;
        this.nmeaGps = nmeaGps;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getgMapGps() {
        return gMapGps;
    }

    public void setgMapGps(String gMapGps) {
        this.gMapGps = gMapGps;
    }

    public Long getIdLong() {
        return idLong;
    }

    public void setIdLong(Long idLong) {
        this.idLong = idLong;
    }

    public String getNmeaGps() {
        return nmeaGps;
    }

    public void setNmeaGps(String nmeaGps) {
        this.nmeaGps = nmeaGps;
    }
}
