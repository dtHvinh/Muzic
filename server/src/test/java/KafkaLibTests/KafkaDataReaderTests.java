package KafkaLibTests;

import com.dthvinh.Server.Lib.Kafka.Services.KafkaDataReader;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.util.List;
import java.util.Map;

public class KafkaDataReaderTests {
    public record KafkaGroupsConfig(
            @JsonProperty("group")
            Map<String, GroupConfig> groups
    ) {
        public record GroupConfig(
                String[] topics,
                @JsonProperty("consumer-count") int consumerCount
        ) {
        }
    }

    public static List<KafkaDataReader.KafkaConfiguration> loadKafkaConfigs(String yamlFilePath) {
        YAMLMapper mapper = new YAMLMapper();

        KafkaGroupsConfig config = mapper.readValue(new File(yamlFilePath), KafkaGroupsConfig.class);

        return config.groups.entrySet().stream()
                .map(entry -> new KafkaDataReader.KafkaConfiguration(
                        entry.getKey(),           // muzic-consumer-group-1
                        entry.getValue().topics(),
                        entry.getValue().consumerCount()
                ))
                .toList();
    }

    @Test
    void testLoadConfigs() {
        List<KafkaDataReader.KafkaConfiguration> configs = loadKafkaConfigs("consumer.yaml");

        Assertions.assertEquals(2, configs.size());
        Assertions.assertEquals(2, configs.stream().findFirst().get().topics.length);
    }
}
