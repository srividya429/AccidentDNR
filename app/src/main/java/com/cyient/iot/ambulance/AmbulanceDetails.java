package com.cyient.iot.ambulance;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by Administrator
 * on 24-10-2017.
 */
@DynamoDBTable(tableName = "ambulance_details")
public class AmbulanceDetails {

    private String vehicle_no;
    private String ambulance_type;
    private String email;
    private boolean logged_in;
    private String mobile_no;
    private String name;
    private String password;
    private String service_provider;

    @DynamoDBHashKey(attributeName = "vehicle_no")
    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    @DynamoDBAttribute(attributeName = "ambulance_type")
    public String getAmbulance_type() {
        return ambulance_type;
    }

    public void setAmbulance_type(String ambulance_type) {
        this.ambulance_type = ambulance_type;
    }

    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "logged_in")
    public boolean isLogged_in() {
        return logged_in;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }

    @DynamoDBAttribute(attributeName = "mobile_no")
    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDBAttribute(attributeName = "service_provider")
    public String getService_provider() {
        return service_provider;
    }

    public void setService_provider(String service_provider) {
        this.service_provider = service_provider;
    }
}
