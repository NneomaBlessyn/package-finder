package com.nneomablessyn.packagefinder.commons.docs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("app.info")
@Component
@Data
public class ApiInfo {
    private String title;
    private String description;
    private String version;
    private String contactName;
    private String contactUrl;
    private String contactEmail;
}
