package com.yash.railway.reservation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.railway.reservation.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class TrainService {

    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();

    // ⚠ Make sure this path is correct relative to your project root
    private static final String TRAIN_DB_PATH = "data/localDb/trains.json";

    // ================= CONSTRUCTOR =================
    public TrainService() throws IOException {

        File trainsFile = new File(TRAIN_DB_PATH);

        if (trainsFile.exists()) {
            trainList = objectMapper.readValue(
                    trainsFile,
                    new TypeReference<List<Train>>() {}
            );
        } else {
            trainList = new ArrayList<>();
        }
    }



    // ================= SEARCH TRAINS =================
    public List<Train> searchTrains(String source, String destination) {

        if (trainList == null || trainList.isEmpty()) {
            System.out.println("No trains loaded from file.");
            return new ArrayList<>();
        }

        List<Train> result = new ArrayList<>();

        for (int i = 0; i < trainList.size(); i++) {

            Train train = trainList.get(i);

            if (validTrain(train, source, destination)) {
                result.add(train);
            }
        }

        if (result.isEmpty()) {
            System.out.println("No trains available between "
                    + source + " and " + destination);
        }

        return result;
    }

    // ================= VALID TRAIN LOGIC =================
    private boolean validTrain(Train train, String source, String destination) {

        List<String> stationOrder = train.getStationsName();

        if (stationOrder == null || stationOrder.isEmpty()) {
            return false;
        }

        int sourceIndex = -1;
        int destinationIndex = -1;

        for (int i = 0; i < stationOrder.size(); i++) {

            if (stationOrder.get(i).equalsIgnoreCase(source.trim())) {
                sourceIndex = i;
            }

            if (stationOrder.get(i).equalsIgnoreCase(destination.trim())) {
                destinationIndex = i;
            }
        }

        return sourceIndex != -1
                && destinationIndex != -1
                && sourceIndex < destinationIndex;
    }

    // ================= ADD TRAIN =================
    public void addTrain(Train newTrain) {

        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainId()
                        .equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            updateTrain(newTrain);
        } else {
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    // ================= UPDATE TRAIN =================
    public void updateTrain(Train updatedTrain) {

        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId()
                        .equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            addTrain(updatedTrain);
        }
    }

    // ================= SAVE FILE =================
    private void saveTrainListToFile() {
        try {
            objectMapper.writeValue(new File(TRAIN_DB_PATH), trainList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
