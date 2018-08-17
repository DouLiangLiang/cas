package org.apereo.cas.consent;

import org.apereo.cas.config.CasConsentCoreConfiguration;

import org.junit.AfterClass;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

/**
 * This is {@link JsonConsentRepositoryTests}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@SpringBootTest(classes = {
    CasConsentCoreConfiguration.class,
    RefreshAutoConfiguration.class
})
@TestPropertySource(properties = {
    "cas.consent.json.location=classpath:/ConsentRepository.json"
})
public class JsonConsentRepositoryTests {
    private static final ClassPathResource JSON_FILE = new ClassPathResource("ConsentRepository.json");

    @AfterClass
    public static void shutdown() throws IOException {
        JSON_FILE.getFile().delete();
    }
}
