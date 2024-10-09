package it.saimao.pakpicalendar.utils;

public enum CalendarType {
    NORMAL(0), PAKPI(1);
    private    int value;

    CalendarType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
