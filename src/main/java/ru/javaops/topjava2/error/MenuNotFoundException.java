package ru.javaops.topjava2.error;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException(){
        super("Menu not found for the given restaurant and date");
    }
}
