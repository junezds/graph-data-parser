package com.power.enums;

public enum LayerType {
    BackGround_Layer("BackGround_Layer"),
    Text_Layer("Text_Layer"),
    Other_Layer("Other_Layer"),
    Substation_Layer("Substation_Layer"),
    ConnLine_Layer("ConnLine_Layer"),
    ACLineSegment_Layer("ACLineSegment_Layer"),
    BusbarSection_Layer("BusbarSection_Layer"),
    Junction_Layer("Junction_Layer"),
    Breaker_Layer("Breaker_Layer"),
    CompositeSwitch_Layer("CompositeSwitch_Layer"),
    EnergyConsumer_Layer("EnergyConsumer_Layer"),
    PowerTransformer_Layer("PowerTransformer_Layer"),
    LoadBreakSwitch_Layer("LoadBreakSwitch_Layer"),
    PoleCode_Layer("PoleCode_Layer"),
    Fuse_Layer("Fuse_Layer"),
    Disconnector_Layer("Disconnector_Layer");

    private String name;
    LayerType(String layerName) {
        this.name = layerName;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return name;
    }
}
