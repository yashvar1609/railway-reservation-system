package com.yash.railway.reservation.entities;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {

    @JsonProperty("train_id")
    private String trainId;

    @JsonProperty("train_no")
    private String trainNo;

    @JsonProperty("seats")
    private List<List<Integer>> seats;

    @JsonProperty("station_name_with_date")
    private Map<String, String> stationNameWithDate;

    @JsonProperty("stations_name")
    private List<String> stationsName;

    // REQUIRED for Jackson
    public Train() {
    }

    public Train(String trainId,
                 String trainNo,
                 List<List<Integer>> seats,
                 Map<String, String> stationNameWithDate,
                 List<String> stationsName) {

        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationNameWithDate = stationNameWithDate;
        this.stationsName = stationsName;
    }

    // ================= GETTERS & SETTERS =================

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public Map<String, String> getStationNameWithDate() {
        return stationNameWithDate;
    }

    public void setStationNameWithDate(Map<String, String> stationNameWithDate) {
        this.stationNameWithDate = stationNameWithDate;
    }

    public List<String> getStationsName() {
        return stationsName;
    }

    public void setStationsName(List<String> stationsName) {
        this.stationsName = stationsName;
    }

    // ================= DISPLAY METHOD =================

    @JsonIgnore
    public String getTraininfo() {
        return String.format(
                "Train ID: %s | Train No: %s",
                trainId,
                trainNo
        );
    }
    //Train ID: --> normal text
    //%s --> placeholder for trainId
    // | --> just a separator for readability
    // Train No --> normal text
    // %s --> placeholder for trainNo
    // String.format() --> is used to create a clean sentence by inserting values into fixed placeholders.
}

