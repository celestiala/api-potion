package com.tmoncorp.apipotion.spring.version;

import com.tmoncorp.apipotion.core.version.BuildInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@Service
public class ManifestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManifestService.class);

    @Autowired
    ServletContext context;

    public BuildInfo getBuildInfo() {

        InputStream resourceAsStream = context.getResourceAsStream("/META-INF/MANIFEST.MF");
        Manifest mf = new Manifest();
        try {
            mf.read(resourceAsStream);
        } catch (IOException e) {
            LOGGER.debug("",e);
            return null;
        }

        Attributes atts = mf.getMainAttributes();
        BuildInfo info = new BuildInfo();
        info.setTitle(atts.getValue("Implementation-Title"));
        info.setVersion(atts.getValue("Implementation-Version"));
        info.setVendor(atts.getValue("Implementation-Vendor-Id"));
        info.setBuildJDK(atts.getValue("Build-Jdk"));
        info.setBuildBy(atts.getValue("Built-By"));
        info.setGitShaHash(atts.getValue("git-SHA-1"));

        return info;
    }
}
