package cadena.cadenareactive.com.cadenareactive.model;

import java.util.Date;

public class Location extends DomainEntity{

	private String nmeaGps;

	private String gMapGps;

	public Location(Date dateTime, String description, Long deviceId, String gMapGps, Long idLong, String messageId, String nmeaGps) {
		super.setDescription(description);
		super.setIdLong(idLong);
		this.nmeaGps = nmeaGps;
		this.gMapGps = gMapGps;
		this.dateTime = dateTime;
		this.messageId = messageId;
		this.deviceId = deviceId;
	}

	private Date dateTime;
	
	private String messageId ;
	
	private Long deviceId ;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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
