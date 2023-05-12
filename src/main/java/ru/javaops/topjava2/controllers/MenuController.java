package ru.javaops.topjava2.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.service.MenuService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    static final String REST_URL = "/api/restaurants";
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{restaurantId}/menus")
    public ResponseEntity<List<Menu>> getMenuForRestaurantAndDate(
            @PathVariable Long restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Menu> menu = menuService.getMenuForRestaurantAdnDate(restaurantId, date);
        return ResponseEntity.ok(menu);
    }

    @PostMapping("/{restaurantId}/menus")
    public ResponseEntity<Menu> createMenu(@PathVariable Long restaurantId, @RequestBody Menu menu) {
        Menu createdMenu = menuService.createMenu(restaurantId, menu);
        return ResponseEntity.ok(createdMenu);
    }

    @PutMapping("/{restaurantId}/menus/{menuId}")
    public ResponseEntity<Menu> updateMenu(@PathVariable Long restaurantId,
                                           @PathVariable Long menuId,
                                           @RequestBody Menu updatedMenu) {
        Menu menu = menuService.updateMenu(restaurantId, menuId, updatedMenu);
        return ResponseEntity.ok(menu);
    }

    @DeleteMapping("/{restaurantId}/menus/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        menuService.deleteMenu(restaurantId, menuId);
        return ResponseEntity.noContent().build();
    }
}
