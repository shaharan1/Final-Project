package emranhss.com.Modern_Hospital_Management_System.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${image.upload.dir}")
    private String uploadDir;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Resolve the configured upload directory to an absolute "file:" URI so
        // uploaded images are always served correctly regardless of the working directory.
        String absolute = Paths.get(uploadDir).toAbsolutePath().toString();
        if (!absolute.endsWith("/")) {
            absolute += "/";
        }
        String location = Paths.get(absolute).toUri().toString();

        // Both /images/** (used by stored photo paths) and /uploads/** (used by the
        // Angular proxy / imgUrl convention) point at the same upload directory.
        registry.addResourceHandler("/images/**", "/uploads/**")
                .addResourceLocations(location);
    }

}

