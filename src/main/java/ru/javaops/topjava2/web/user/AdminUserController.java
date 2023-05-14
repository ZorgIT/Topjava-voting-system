package ru.javaops.topjava2.web.user;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.User;
import ru.javaops.topjava2.to.AdminUserTo;
import ru.javaops.topjava2.to.UserTo;
import ru.javaops.topjava2.util.UsersUtil;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminUserController.REST_URL,
        produces = MediaType.APPLICATION_JSON_VALUE)
// TODO: cache only most requested, seldom changed data!
public class AdminUserController extends AbstractUserController {

    static final String REST_URL = "/api/admin/users";


    @GetMapping("/{userId}")
    public AdminUserTo getAdminUser(@PathVariable int userId) {
        return UsersUtil.asAdminUserTo(super.get(userId));
    }

    @Override
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int userId) {
        super.delete(userId);
    }

    @GetMapping
    public List<AdminUserTo> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC,
                        "name", "email")).stream()
                .map(UsersUtil::asAdminUserTo)
                .collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminUserTo> createWithLocation(
            @Valid @RequestBody UserTo user) {
        log.info("create {}", user);
        checkNew(user);
        AdminUserTo created = UsersUtil.asAdminUserTo(repository.prepareAndSave(UsersUtil.createNewFromTo(user)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @PathVariable int userId) {
        log.info("update {} with id={}", user, userId);
        assureIdConsistent(user, userId);
        repository.prepareAndSave(user);
    }

    @GetMapping("/by-email")
    public AdminUserTo getByEmail(@RequestParam String email) {
        log.info("getByEmail {}", email);
        return UsersUtil.asAdminUserTo(repository.getExistedByEmail(email));
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void enable(@PathVariable int userId, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", userId);
        User user = repository.getExisted(userId);
        user.setEnabled(enabled);
    }
}