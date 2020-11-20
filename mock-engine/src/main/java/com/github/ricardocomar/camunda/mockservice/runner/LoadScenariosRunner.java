package com.github.ricardocomar.camunda.mockservice.runner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import com.github.ricardocomar.camunda.mockservice.usecase.RegisterOnTopicUseCase;
import com.github.ricardocomar.camunda.mockservice.usecase.SaveScenarioUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class LoadScenariosRunner implements ApplicationRunner {
    private static final String SCENARIOS_FOLDER = "scenariosFolder";
    private static final String ROOT_FOLDER = "scenarios";
    private static final String FILES_PATTERN = ".json";
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadScenariosRunner.class);

    @Autowired
    private SaveScenarioUseCase saveUC;

    @Autowired
    private RegisterOnTopicUseCase regTopic;

    private final ObjectMapper mapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("Application started with option arguments: {}", args.getOptionNames());

        String filesFolder = ROOT_FOLDER;
        if (args.getOptionNames().contains(SCENARIOS_FOLDER)) {
            filesFolder = args.getOptionValues(SCENARIOS_FOLDER).get(0);
            LOGGER.info("Custom folder {}", filesFolder);
        }

        final File rootFolder = new File(filesFolder);
        LOGGER.info("Searching for \"{}\" files in {} folder", FILES_PATTERN,
                rootFolder.toPath().toAbsolutePath());
        if (rootFolder.exists() && rootFolder.isDirectory()) {
            LOGGER.info("Folder {} found, searching for files {}", rootFolder, FILES_PATTERN);

            try (Stream<Path> paths = Files.walk(rootFolder.toPath())) {
                paths
                        // .filter(file -> file.getFileName().endsWith(FILES_PATTERN))
                        .filter(Files::isRegularFile).forEach(this::loadFile);
            }
        }
    }

    private void loadFile(Path jsonFile) {
        try {
            Scenario scenario = mapper.readValue(jsonFile.toFile(), Scenario.class);
            saveUC.save(scenario);

            if (!regTopic.isTopicRegistred(scenario.getTopicName())) {
                regTopic.registerTopic(scenario.getTopicName());
            }

            LOGGER.info("Scenario for topic {} and priority {} registred!!",
                    scenario.getTopicName(), scenario.getPriority());

        } catch (Exception e) {
            LOGGER.error("Error reading file " + jsonFile.getFileName(), e);
            return;
        }
    }
}
