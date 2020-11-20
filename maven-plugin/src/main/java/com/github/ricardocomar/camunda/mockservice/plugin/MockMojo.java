package com.github.ricardocomar.camunda.mockservice.plugin;

import com.github.ricardocomar.camunda.mockservice.PluginApplication;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.springframework.boot.SpringApplication;

@Mojo(name = "camunda-mock-service", defaultPhase = LifecyclePhase.PROCESS_TEST_CLASSES,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class MockMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "scenariosFolder",
            defaultValue = "${project.build.testOutputDirectory}/scenarios")
    private String scenariosFolder;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info("Starting Spring Context");
        SpringApplication.run(PluginApplication.class,
                new String[] {"--scenariosFolder=" + scenariosFolder});
        getLog().info("started!");

    }

}
