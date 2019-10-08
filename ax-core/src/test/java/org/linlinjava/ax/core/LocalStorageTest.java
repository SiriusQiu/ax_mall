package org.linlinjava.ax.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linlinjava.ax.core.storage.LocalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LocalStorageTest {

    private final Log logger = LogFactory.getLog(LocalStorageTest.class);
    @Autowired
    private LocalStorage localStorage;

    @Test
    public void test() throws IOException {
        String test = getClass().getClassLoader().getResource("ax.png").getFile();
        File testFile = new File(test);
        localStorage.store(new FileInputStream(test), testFile.length(), "image/png", "ax.png");
        Resource resource = localStorage.loadAsResource("ax.png");
        String url = localStorage.generateUrl("ax.png");
        logger.info("test file " + test);
        logger.info("store file " + resource.getURI());
        logger.info("generate url " + url);
    }

}