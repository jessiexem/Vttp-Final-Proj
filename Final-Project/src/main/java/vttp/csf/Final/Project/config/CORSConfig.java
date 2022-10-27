package vttp.csf.Final.Project.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CORSConfig implements WebMvcConfigurer {

    private String path;
    private String origin;

    public CORSConfig (String p, String o) {
        path = p;
        origin = o;
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry
                .addMapping(path)
                .allowedOrigins(origin)
                .allowedMethods("*")
                .maxAge(3600L)
                .allowedHeaders("*")
                .exposedHeaders("Authorization");


        //model ans
//                .allowCredentials(true);
//                .addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("*")
//                .maxAge(3600L)
//                .allowedHeaders("*")
//                .exposedHeaders("Authorization")
//                .allowCredentials(true);
    }
}
