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

@Mojo(name = "camunda-mock-service", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class MockMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    private static final Thread thread = new Thread(() -> {
        SpringApplication.run(PluginApplication.class, new String[] {});
    });

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {


        getLog().info("Starting Spring Context");
        thread.start();

        getLog().info("Sleeping 15s...");
        try {
            Thread.sleep(15000);
        } catch (final Exception e) {
        }
        getLog().info("Awake !");

    }

}
