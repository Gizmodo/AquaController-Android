package ru.esp8266.aqua.Model;

public class LampItem {
    private Boolean Enabled;
    private Boolean State;
    private Integer Pin;
    private String On;
    private String Off;
    private String Position;

    public Boolean getEnabled() {
        return Enabled;
    }

    public void setEnabled(Boolean enabled) {
        Enabled = enabled;
    }

    public Boolean getState() {
        return State;
    }

    public void setState(Boolean state) {
        State = state;
    }

    public Integer getPin() {
        return Pin;
    }

    public void setPin(Integer pin) {
        Pin = pin;
    }

    public String getOn() {
        return On;
    }

    public void setOn(String on) {
        On = on;
    }

    public String getOff() {
        return Off;
    }

    public void setOff(String off) {
        Off = off;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public LampItem() {
    }

    public LampItem(Boolean enabled, Boolean state, Integer pin, String on, String off, String position) {
        Enabled = enabled;
        State = state;
        Pin = pin;
        On = on;
        Off = off;
        Position = position;
    }
}
