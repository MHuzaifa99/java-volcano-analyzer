import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    // add methods here to meet the requirements in README.md

    public static void main(String args[]) throws IOException, URISyntaxException {
        VolcanoAnalyzer a = new VolcanoAnalyzer();
        // a.loadVolcanoes(Optional.empty());
        a.eruptedInEighties();
        // a.numbVolcanoes();
    }

}
