package com.cyient.iot.ambulance;

/*
 * Created by Administrator
 * on 27-10-2017.
 */

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "dnr_vehicle_details")
public class DNRVehicleDetails {
    private String vehicle_no;
    private String accident;
    private String bypass;
    private String latitude;
    private String longitude;
    private String theft;
    private String time;

    @DynamoDBHashKey(attributeName = "vehicle_no")
    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    @DynamoDBHashKey(attributeName = "accident")
    public String getAccident() {
        return accident;
    }

    public void setAccident(String accident) {
        this.accident = accident;
    }

    @DynamoDBHashKey(attributeName = "bypass")
    public String getBypass() {
        return bypass;
    }

    public void setBypass(String bypass) {
        this.bypass = bypass;
    }

    @DynamoDBHashKey(attributeName = "latitude")
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @DynamoDBHashKey(attributeName = "longitude")
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @DynamoDBHashKey(attributeName = "theft")
    public String getTheft() {
        return theft;
    }

    public void setTheft(String theft) {
        this.theft = theft;
    }

    @DynamoDBHashKey(attributeName = "time")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}