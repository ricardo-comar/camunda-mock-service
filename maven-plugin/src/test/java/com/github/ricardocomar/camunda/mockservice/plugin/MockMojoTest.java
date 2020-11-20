package com.github.ricardocomar.camunda.mockservice.plugin;

import java.io.File;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class MockMojoTest extends AbstractMojoTestCase {
    
    public void testSomething()
        throws Exception
    {
        File pom = getTestFile( "../sample-project/pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        // MockMojo myMojo = (MockMojo) lookupMojo( "camunda-mock-service-maven-plugin", pom );
        // assertNotNull( myMojo );
        // myMojo.execute();

    }
}

