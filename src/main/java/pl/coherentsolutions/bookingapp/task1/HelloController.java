package pl.coherentsolutions.bookingapp.task1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello/{userName}")
    public String hello(@PathVariable String userName) {
        return String.format("Hello %s!", userName);
    }
}
