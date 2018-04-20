package cadena.cadenareactive.com.cadenareactive.model;

import java.util.ArrayList;
import java.util.List;

public class LocationServiceResponse {
	private long processingTime;
	private List<DeviceLocations> deviceLocationses = new ArrayList<>();

	public void setProcessingTime(long processingTime) {
		this.processingTime = processingTime;
	}

	 public LocationServiceResponse deviceLocationses(
             List<DeviceLocations> deviceLocationses) {
      this.deviceLocationses = deviceLocationses;
      return this;
  }

	public LocationServiceResponse(List<DeviceLocations> deviceLocationses, long processingTime) {
		this.processingTime = processingTime;
		this.deviceLocationses = deviceLocationses;
	}

	public List<DeviceLocations> getDeviceLocationses() {
		return deviceLocationses;
	}

	public long getProcessingTime() {
		return processingTime;
	}
	 

}
