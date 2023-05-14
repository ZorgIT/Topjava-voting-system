package ru.javaops.topjava2.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.javaops.topjava2.dto.MenuDto;
import ru.javaops.topjava2.dto.MenuWithoutDateDto;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.service.MenuService;
import ru.javaops.topjava2.service.RestaurantService;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminMenuControllerTest {
    private static final String REST_URL = AdminMenuController.REST_URL + "/{restaurantId}/menus";
    private static final String RESTAURANT_ID = "1";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private Restaurant testRestaurant;

    private MenuWithoutDateDto testMenu;

    private MenuDto createdMenuDto;

    @BeforeEach
    public void setUp() throws Exception {
        testRestaurant = restaurantService.createRestaurant(
                new Restaurant(null, "Test Restaurant"));
        testMenu =
                new MenuWithoutDateDto("Test Dish", new BigDecimal("10.00"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void createMenuTest() throws Exception {
        String requestJson = objectMapper.writeValueAsString(testMenu);
        MvcResult mvcResult = mockMvc.perform(post(REST_URL, RESTAURANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        MenuDto createdMenuDto = objectMapper.readValue(responseJson, MenuDto.class);

        assertEquals(createdMenuDto.getDish(), testMenu.getDish());
        assertEquals(createdMenuDto.getPrice(), testMenu.getPrice());
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (createdMenuDto != null) {
            menuService.deleteMenu(testRestaurant.getId(), createdMenuDto.getId());
        }
        restaurantService.deleteRestaurant(testRestaurant.getId());
    }




}


