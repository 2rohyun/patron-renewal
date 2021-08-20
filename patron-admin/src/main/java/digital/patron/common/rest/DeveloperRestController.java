package digital.patron.common.rest;

import digital.patron.common.domain.Users;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/users")
public class DeveloperRestController {

    private List<Users> USERS = Stream.of(
            new Users(1L, "FirstName1", "LastName1"),
            new Users(2L, "FirstName2", "LastName2"),
            new Users(3L, "FirstName3", "LastName3")
    ).collect(Collectors.toList());

    @GetMapping
    public List<Users> getAll() {
        return USERS;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('users:read')")
    public Users getByEmail(Long id) {
        return USERS.stream().filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('users:write')")
    public Users create(@RequestBody Users users) {
        this.USERS.add(users);
        return users;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public void deleteById(Long id) {
        this.USERS.removeIf(users -> users.getId().equals(id));
    }
}
