import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class VolcanoAnalyzer {
    private List<Volcano> volcanos;

    public void loadVolcanoes(Optional<String> pathOpt) throws IOException, URISyntaxException {
        try {
            String path = pathOpt.orElse("volcano.json");
            URL url = this.getClass().getClassLoader().getResource(path);
            String jsonString = new String(Files.readAllBytes(Paths.get(url.toURI())));
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            volcanos = objectMapper.readValue(jsonString,
                    typeFactory.constructCollectionType(List.class, Volcano.class));
        } catch (Exception e) {
            throw (e);
        }
    }

    public Integer numbVolcanoes() {
        System.out.println(volcanos.size());
        return volcanos.size();
    }

    public List<Volcano> eruptedInEighties() {
        List<Volcano> data = volcanos.stream().filter(n -> {
            int a = n.getYear();
            return a > 1979 && a < 1990;
        }).collect(Collectors.toList());
        System.out.println(data);
        return data;
    }

    public String[] highVEI() {
        return volcanos.stream().filter(n -> n.getVEI() >= 6).map(n -> n.getName())// .collect(Collectors.toList())
                .toArray(String[]::new);
        // return data;
    }

    public Volcano mostDeadly() {
        Optional<Volcano> data = volcanos.stream()
                .filter(n -> !n.getDEATHS().isEmpty())
                .max(Comparator.comparingInt(a -> Integer.parseInt(a.getDEATHS())));
        return data.orElse(null);
    }

    public double causedTsunami() {
        return volcanos.stream().filter(v -> !v.getTsu().isEmpty()).count() * 100 / volcanos.size();
    }

    public String mostCommonType() {
        // HashMap <String,Integer> str = new HashMap<String, Integer>();
        // Stream<Volcano> data = volcanos.stream();
        // str.put(data.getType(), 1);
        // data.forEach(n -> n.getType());
        // return "";
        return volcanos.stream()
                .collect(Collectors.groupingBy(Volcano::getType, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

    }

    public int eruptionsByCountry(String country) {

        return (int) volcanos.stream()
                .filter(n -> n.getCountry().equalsIgnoreCase(country)).count();
    }

    public double averageElevation() {
        return volcanos.stream().mapToDouble(Volcano::getElevation).average().orElse(0);
    }

    public String[] volcanoTypes() {
        return volcanos.stream().map(Volcano::getType).distinct().toArray(String[]::new);
    }
    // add methods here to meet the requirements in README.md

    public double percentNorth() {
        double value = volcanos.stream().filter(n -> n.getLatitude() > 0).count();
        return value / volcanos.size() * 100;
    }

    public String[] manyFilters() {
        return volcanos.stream()
                .filter(n -> n.getYear() > 1800).filter(n -> n.getTsu().isEmpty()).filter(n -> n.getLatitude() < 0)
                .filter(n -> n.getVEI() == 5).map(Volcano::getName).toArray(String[]::new);
    }

    public String[] elevatedVolcanoes(int ele) {
        return volcanos.stream().filter(n -> n.getElevation() >= ele).map(Volcano::getName).toArray(String[]::new);
    }

    public String[] topAgentsOfDeath() {
        return volcanos.stream().filter(v -> !v.getDEATHS().isEmpty())
                .sorted((i, j) -> Integer.parseInt(j.getDEATHS()) - Integer.parseInt(i.getDEATHS()))
                .limit(10).filter(v -> !v.getAgent().isEmpty())
                .map(v -> Arrays.asList(v.getAgent().split(","))).flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    // public String[] topAgentsOfDeath(){
    // // return
    // String data = volcanos.stream().filter(n -> !n.getDEATHS().isEmpty())
    // .max(Comparator.comparingInt(a -> Integer.parseInt(a.getDEATHS())))
    // .map(Volcano::getAgent).orElse(null);
    // // .toArray(String[]::new);

    // // toArray(String[]::new);
    // }
    // public static void main(String args[]) throws IOException, URISyntaxException
    // {
    // VolcanoAnalyzer a = new VolcanoAnalyzer();
    // // a.loadVolcanoes(Optional.empty());
    // a.eruptedInEighties();
    // // a.numbVolcanoes();
    // }
}
