package com.dthvinh.Server.Lib.Kafka.Services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class KafkaDataReader {
    @AllArgsConstructor
    public static class KafkaConfiguration {
        public String groupId;
        public String[] topics;
        public int consumerCount;
    }

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

    public static List<KafkaDataReader.KafkaConfiguration> loadKafkaConfigs() throws FileNotFoundException {
        YAMLMapper mapper = new YAMLMapper();

        KafkaGroupsConfig config = mapper.readValue(resolveConsumerFile(false), KafkaGroupsConfig.class);

        return config.groups.entrySet().stream()
                .map(entry -> new KafkaDataReader.KafkaConfiguration(
                        entry.getKey(),           // muzic-consumer-group-1
                        entry.getValue().topics(),
                        entry.getValue().consumerCount()
                ))
                .toList();
    }

    private static File resolveConsumerFile(boolean isSilent) throws FileNotFoundException {
        File val = new File("consumer.yaml");
        if (val.exists())
            return val;

        if (!isSilent)
            throw new FileNotFoundException("Can not find the file name \"consumer.yaml\"");

        return null;
    }
}
