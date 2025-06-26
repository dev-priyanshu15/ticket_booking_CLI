package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

    private List<Train> trainList;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAIN_DB_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";

    public TrainService() throws IOException {
        File trainFile = new File(TRAIN_DB_PATH);
        if (!trainFile.exists()) {
            trainList = new ArrayList<>();
        } else {
            trainList = objectMapper.readValue(trainFile, new TypeReference<List<Train>>() {});
        }
    }

    /**
     * Search for all valid trains from a given source to a destination.
     */
    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream()
                .filter(train -> validTrain(train, source, destination))
                .collect(Collectors.toList());
    }

    /**
     * Add a new train to the list, or update if it already exists.
     */
    public void addTrain(Train newTrain) {
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            updateTrain(newTrain);
        } else {
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    /**
     * Update an existing train in the list.
     */
    public void updateTrain(Train updatedTrain) {
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            // Fallback: Add as new if not found
            addTrain(updatedTrain);
        }
    }

    /**
     * Save the current train list to the JSON file.
     */
    private void saveTrainListToFile() {
        try {
            objectMapper.writeValue(new File(TRAIN_DB_PATH), trainList);
        } catch (IOException e) {
            System.out.println("Error saving train list: " + e.getMessage());
        }
    }

    /**
     * Checks whether a train is valid for the given source and destination in the correct order.
     */
    private boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }
}
