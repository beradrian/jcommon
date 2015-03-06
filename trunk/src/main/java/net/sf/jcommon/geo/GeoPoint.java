package net.sf.jcommon.geo;

public class GeoPoint {

	private static final String SEPARATOR = ",";
	private double latitude, longitude, altitude;

	public GeoPoint() {
	}

	public GeoPoint(double latitude, double longitude) {
		this(latitude, longitude, 0);
	}

	public GeoPoint(double latitude, double longitude, double altitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	@Override
	public String toString() {
		return latitude + SEPARATOR + longitude + SEPARATOR + altitude;
	}

}
