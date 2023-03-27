package com.power.enums;

public enum XmlSonNodeType {

    FullModel("FullModel"),
    PSRType("PSRType"),
    GeographicalRegion("GeographicalRegion"),
    SubGeographicalRegion("SubGeographicalRegion"),
    Asset("Asset"),
    Substation("Substation"),
    BaseVoltage("BaseVoltage"),
    Breaker("Breaker"),
    ConnectivityNode("ConnectivityNode"),
    Terminal("Terminal"),
    AssetDeployment("AssetDeployment"),
    Feeder("Feeder"),
    ACLineSegment("ACLineSegment"),
    Junction("Junction"),
    BusbarSection("BusbarSection"),
    CompositeSwitch("CompositeSwitch"),
    LoadBreakSwitch("LoadBreakSwitch"),
    GroundDisconnector("GroundDisconnector"),
    EnergyConsumer("EnergyConsumer"),
    UsagePoint("UsagePoint"),
    VoltageLevel("VoltageLevel"),
    Organisation("Organisation"),
    AssetUser("AssetUser"),
    ParentOrganization("ParentOrganization"),
    Maintainer("Maintainer"),
    PowerTransformer("PowerTransformer"),
    PowerTransformerEnd("PowerTransformerEnd"),
    Fuse("Fuse"),
    PoleCode("PoleCode"),
    Disconnector("Disconnector");

    private String sonNodeName;

    XmlSonNodeType(String sonNodeName) {
        this.sonNodeName = sonNodeName;
    }

    public String getName() {
        return sonNodeName;
    }

    public String getValue() {
        return sonNodeName;
    }

    public String getFullNameWithNameSpace() {
        String result;
        if (sonNodeName.equals("FullModel")) {
            result = "md:" + sonNodeName;
        } else if (sonNodeName.equals("PoleCode")) {
            result = "sgcim:" + sonNodeName;
        } else {
            result = "cim:" + sonNodeName;
        }
        return result;
    }
}
