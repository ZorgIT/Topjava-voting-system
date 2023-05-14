package ru.javaops.topjava2.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.dto.MenuDto;
import ru.javaops.topjava2.dto.MenuWithoutDateDto;
import ru.javaops.topjava2.error.IllegalRequestDataException;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.service.MenuService;

import java.util.List;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController {
    static final String REST_URL = "/api/admin/restaurants";
    private final MenuService menuService;

    @Autowired
    public AdminMenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/{restaurantId}/menus")
    public ResponseEntity<Menu> createMenu(@PathVariable Long restaurantId,
                                           @Valid @RequestBody MenuWithoutDateDto menu,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalRequestDataException("Incorrect input data" + bindingResult);
        }
        Menu createdMenu = menuService.createMenu(restaurantId, menu);
        return ResponseEntity.ok(createdMenu);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{restaurantId}/menus")
    public ResponseEntity<List<Menu>> getMenuForRestaurant(@PathVariable Long restaurantId) {
        List<Menu> menu = menuService.getMenuForRestaurant(restaurantId);
        return ResponseEntity.ok(menu);
    }


    @Secured("ROLE_ADMIN")
    @PutMapping("/{restaurantId}/menus/{menuId}")
    public ResponseEntity<Menu> updateMenu(@PathVariable Long restaurantId,
                                           @PathVariable Long menuId,
                                           @RequestBody MenuDto updatedMenu) {
        Menu menu = menuService.updateMenu(restaurantId, menuId, updatedMenu);
        return ResponseEntity.ok(menu);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{restaurantId}/menus/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        menuService.deleteMenu(restaurantId, menuId);
        return ResponseEntity.noContent().build();
    }
}
