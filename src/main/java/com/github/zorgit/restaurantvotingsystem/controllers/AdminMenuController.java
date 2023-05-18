package com.github.zorgit.restaurantvotingsystem.controllers;


import com.github.zorgit.restaurantvotingsystem.dto.MenuDto;
import com.github.zorgit.restaurantvotingsystem.dto.MenuWithoutDateDto;
import com.github.zorgit.restaurantvotingsystem.error.IllegalRequestDataException;
import com.github.zorgit.restaurantvotingsystem.model.Menu;
import com.github.zorgit.restaurantvotingsystem.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{restaurantId}/menus")
    public ResponseEntity<Menu> createMenu(@PathVariable Long restaurantId,
                                           @Valid @RequestBody MenuWithoutDateDto menu,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalRequestDataException("Incorrect input data" + bindingResult);
        }
        Menu createdMenu = menuService.saveOrUpdate(restaurantId, menu);
        return ResponseEntity.ok(createdMenu);
    }

    @GetMapping("/{restaurantId}/menus")
    public ResponseEntity<List<Menu>> getMenusForRestaurant(@PathVariable Long restaurantId) {
        List<Menu> menu = menuService.findAllByRestaurantId(restaurantId);
        return ResponseEntity.ok(menu);
    }

    @GetMapping("/{restaurantId}/menus/{menuId}")
    public ResponseEntity<Menu> getMenu(@PathVariable Long restaurantId,
                                        @PathVariable Long menuId) {
        Menu menu = menuService.getOneByIdAndRestaurantId(menuId, restaurantId);
        return ResponseEntity.ok(menu);
    }

    @PutMapping("/{restaurantId}/menus/{menuId}")
    public ResponseEntity<Menu> updateMenu(@PathVariable Long restaurantId,
                                           @PathVariable Long menuId,
                                           @RequestBody MenuDto updatedMenu) {
        Menu menu = menuService.update(restaurantId, menuId, updatedMenu);
        return ResponseEntity.ok(menu);
    }

    @DeleteMapping("/{restaurantId}/menus/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        menuService.delete(restaurantId, menuId);
        return ResponseEntity.noContent().build();
    }
}
