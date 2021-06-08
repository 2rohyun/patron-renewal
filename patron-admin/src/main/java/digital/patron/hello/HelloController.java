package digital.patron.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HelloController {

    @GetMapping("/admin/hello")
    public String forTest() {
        return "hello admin!";
    }
}
