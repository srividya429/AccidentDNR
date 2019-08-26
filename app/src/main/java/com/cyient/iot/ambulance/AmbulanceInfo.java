package com.cyient.iot.ambulance;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by Administrator
 * on 24-10-2017.
 */
@DynamoDBTable(tableName = "ambulance_info")
public class AmbulanceInfo {

    private String vehicle_no;
    private String gen_time;
    private String latitude;
    private String longitude;
    private String status;

    @DynamoDBHashKey(attributeName = "vehicle_no")
    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    @DynamoDBAttribute(attributeName = "gen_time")
    public String getGen_time() {
        return gen_time;
    }

    public void setGen_time(String gen_time) {
        this.gen_time = gen_time;
    }

    @DynamoDBAttribute(attributeName = "latitude")
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @DynamoDBAttribute(attributeName = "longitude")
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @DynamoDBAttribute(attributeName = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
