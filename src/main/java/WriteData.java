import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WriteData {

    List<String[]> dataLines = new ArrayList<>();

    public WriteData() {/*
        dataLines.add(new String[]
                {"One", "Two", "Thrree", "Four.One\nFour.Two"});
        dataLines.add(new String[]
                {"Uno", "Due, Tre", "Quatrooo", "Cinque\"quote\""});*/
    }

    public WriteData(String[] stringArray) {
        dataLines.add(stringArray);
    }

    public void fillArray(String[] strings, double[][] double2D) {
        setCaption(strings);
        for (double[] line : double2D) {
            addLine(line);
        }
    }

    public void setCaption(String[] strings) {
        dataLines.add(strings);
    }

    public void addLine(double[] doubles) {
        String[] strings = convertDoubleToString(doubles);
        dataLines.add(strings);
    }


    public void writeArrayToCSV(String pathname) throws IOException {
        File csvOutputFile = new File(pathname);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertStringToCSV)
                    .forEach(pw::println);
        }
        assert (csvOutputFile.exists());
    }

    private String[] convertDoubleToString(double[] doubleValues) {
        return Arrays.stream(doubleValues)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);
    }

    private String convertStringToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}