package cadena.cadenareactive.com.cadenareactive.model;

import java.util.List;

public class DeviceLocations {
	private List<Location> locations;
	private Device device;

	public DeviceLocations(Device device) {
		this.device = device;
	}

	public DeviceLocations setLocations(final List<Location> locations) {
		this.locations = locations;
		return this;
	}

	public DeviceLocations(Device device, List<Location> locations) {
		this.locations = locations;
		this.device = device;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public List<Location> getLocations() {
		return locations;
	}

}
