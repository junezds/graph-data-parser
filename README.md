# DOM4J 解析XML、SVG文件

> 参考site：
>
> -  https://dom4j.github.io/#string-conversion
> - https://juejin.cn/post/6967175965659103240#heading-12

## DOM4J 工具介绍

### 注意：XPATH解析SVG特殊

这种元素比较特殊，需要通过 name 属性来进行定位。

写法如下：

`//*[name()="svg"]//*[name()="image"]`

如果要同时需要该元素的其它属性可以用 and 的方式来进行定位。

写法如下：

`//*[name()="svg" and @version="1.1"]//*[name()="image"]`

## XML结构

### 整体结构

描述：根节点下所有子节点类型

获取方法：`com/power/build/Main.java`下的静态方法`public Set<String> getAllSonNodeName(Stream<Path> xmlFilesStream)`

```json
[
    "FullModel", //记录整张图的元信息，包括静态拓扑图创建时间、描述、场景时间、版本
    
    "PSRType", //记录该图所含有的设备类型
    
    "GeographicalRegion", //地理区域，即一般为国网南京供电公司
    
    "SubGeographicalRegion", //子地理区域，即子部门，即一般为设备管理部
    
    "Asset", //（干啥的？）财富？资产？
    
    "Substation", //子站，如环网柜、变电站等(属性有PowerSystemResource.PSRType、Substation.NormalEnergizingFeeder、Substation.Region、PowerSystemResource.Assets、Substation.NormalEnergizingFeeder)
    
    "BaseVoltage", //该图含有的基础电压
    
    "Breaker", //断路器（属性有ConductingEquipment.BaseVoltage、PowerSystemResource.Assets、Equipment.EquipmentContainer等）
    
    "ConnectivityNode", //连接点（干啥的？）
    
    "Terminal", //（干啥的？）(终端？似乎会和EnergyConsumer的ID重叠)(站外-电缆段？导线段？站外-电缆终端头？柱上变压器？柱上-负荷开关？站外-中压用户接入点？母线？)
    
    "AssetDeployment",  //（干啥的？和Asset什么关系？）
    
    "Feeder",  // 馈线，例如 10kV青岛路线144（属性有Feeder.IsCurrentFeeder、PowerSystemResource.PSRType、Feeder.NormalEnergizingSubstation、Feeder.NormalHeadTerminal）
    
    "ACLineSegment",  // 交流线段，包括 站外-电缆段、导线段（属性有Equipment.EquipmentContainer、PowerSystemResource.Assets、ConductingEquipment.BaseVoltage）
    
    "Junction",  // (干啥的？包括站外-电缆终端头，还有环网柜内三角形的玩意儿)（属性有PowerSystemResource.PSRType、PowerSystemResource.Assets、Equipment.EquipmentContainer、Equipment.DCEquipmentContainer、ConductingEquipment.BaseVoltage）
    
    "BusbarSection", //母线（Equipment.EquipmentContainer、PowerSystemResource.Assets）
    
    "CompositeSwitch",  // 混合开关/接地开关，即站内-三工位负荷开关（PoweDMS_SECTION_DEVICE143rSystemResource.Assets、Equipment.EquipmentContainer）
    
    "LoadBreakSwitch",  // 负荷开关，包括柱上-负荷开关、站内-ConnectivityNode负荷开关等（站内负荷开关，附属于CompositeSwitch)
    
    "GroundDisconnector",  //接地刀闸（附属于混合开关？）（Switch.CompositeSwitch、Equipment.EquipmentContainer、PowerSystemResource.Assets）
    
    "EnergyConsumer",  // 电力能源消费者J
    
    "UsagePoint",  // 用户连接点
    
    "VoltageLevel", // 电压水平
    
    "Organisation",  //组织
    
    "AssetUser",  // 责任人
    
    "ParentOrganization", //父组织
    
    "Maintainer", // 责任组织
    
    "PowerTransformer",  // 变电站（属性基本同Junction）
    
    "PowerTransformerEnd",  // 变电站End终端（猜测是变电站的出节点，进入下一层次（PowerTransformerEnd.PowerTransformer、TransformerEnd.Terminal）
    
    "Fuse",  //（干啥的？）包括柱上-跌落式熔断器、压变柜
    
    "PoleCode",   // 杆塔节点，包括耐张杆塔、直线杆塔（Equipment.EquipmentContainer、PoleCode.Terminal两个）
    
    "Disconnector"  // 站内隔离开关（附属于Switch.CompositeSwitch）
]
```

其中有PSRID的子节点：

```json
[
        "Substation", //子站，如环网柜、变电站等
        "Feeder",  // 馈线，例如 10kV青岛路线144【svg无】
        "Breaker", //断路器
        "ACLineSegment",  // 交流线段，包括 站外-电缆段、导线段
        "Junction",  // (干啥的？包括站外-电缆终端头，还有环网柜内三角形的玩意儿)
        "PowerTransformer",  // 变电站
        "PowerTransformerEnd",  // 变电站End终端（猜测是变电站的出节点，进入下一层次【svg无】
        "EnergyConsumer",  // 电力能源消费者J
        "PoleCode",   // 杆塔节点，包括耐张杆塔、直线杆塔
        "BusbarSection", //母线
        "CompositeSwitch",  // 混合开关/接地开关，即站内-三工位负荷开关
    	"GroundDisconnector",  //接地刀闸（附属于混合开关？）【svg无】
        "Disconnector"  // 站内隔离开关【svg几乎无】
        "LoadBreakSwitch",  // 负荷开关，包括柱上-负荷开关、站内-负荷开关等【svg几乎无】
        "Fuse",  //（干啥的？）包括柱上-跌落式熔断器、柱上-负荷式熔断器、压变柜
]
```

### PSRType

描述：xml文件中所有的设备类型，即PSRType子节点的所有类型

获取方法：`com/power/build/Main.java`下的方法`public Map<String, String> getAllPSRTypeFromXmlFiles(Stream<Path> xmlFilesStream)`

```json
{
    "10000100": "馈线",
    "30000005": "配电站",
    "30200002": "配电变压器-双绕组",
    "30500000": "站内-断路器",
    "30000004": "开关站",
    "10200000": "直线-运行杆塔",
    "20200000": "站外-电缆终端头",
    "10200001": "耐张-运行杆塔",
    "11300000": "柱上-隔离开关",
    "32400000": "环网柜",
    "37000000": "站外-中压用户接入点",
    "20100000": "站外-电缆段",
    "10100000": "导线段",
    "30700002": "站内-三工位负荷开关",
    "11200000": "柱上-负荷开关",
    "20400000": "站房-电缆分支箱",
    "32300000": "箱式变电站",
    "30700000": "站内-负荷开关",
    "30500099": "配电-站内三工位断路器",
    "11500001": "柱上-负荷式熔断器",
    "11500000": "柱上-跌落式熔断器",
    "11000000": "柱上变压器",
    "11000001": "柱上-用户变压器",
    "30600000": "站内-隔离开关",
    "30900000": "站内-熔断器",
    "30300000": "站内-所用变",
    "30000000": "变电站",
    "31100000": "母线",
    "30600007": "站内-三工位隔离刀闸",
    "30600006": "站内-T型开关",
    "20300000": "站外-电缆终端头",
    "11100000": "柱上-断路器",
    "11400000": "柱上-重合器",
    "30600002": "站内-V型开关",
    "30600001": "站内-接地刀闸"
}
```

### Terminal

导电设备的端点，应该可以解析出连接关系

属性：

- ConductingEquipment：即端点所属的设备id
- ConnectivityNode：每个端点对应的连接点的id

比如

- ACLineSegment 交流线段，包括 站外-电缆段201、导线段101，的两端，二比一
- Breaker，包括站内断路器305的两端，柱上-断路器111二比一
- Junction，站外-电缆终端头202、203的节点，一比一
- PowerTransformer，包括柱上变压器110的两端，配电变压器-双绕组30200002的两端、站内-所用变303两端，柱上-用户变压器11000001，二比一
- LoadBreakSwitch，包括柱上-负荷开关112的两端，站内-负荷开关(附属于CompositeSwitcher)307的两端，二比一
- EnergyConsumer，包括站外-中压用户接入点370的节点，一比一
- BusbarSection，包括母线311的节点，一比一
- GroundDisconnector，包括站内接地刀闸(附属于CompositeSwicher)30600001的两端，二比一
- Disconnector，柱上-隔离开关113的两端，柱上-重合器114，站内-隔离开关306，二比一
- Fuse，柱上-负荷式熔断器11500001的两端，柱上-跌落式熔断器115的两端，站内-熔断器309的两端，二比一

### ConnectivityNode

连在同一个ConnectivityNode上的Terminal表示彼此相连

## SVG结构

### LayerName

```json
[
    "BackGround_Layer",  //背景
    "Text_Layer",  //文字
    "Other_Layer",  //其他
    
    "Substation_Layer", //变电站、环网柜等子站【同 XML Substation】
    "ConnLine_Layer",  //连接线（xml里几乎不含有）
    "ACLineSegment_Layer",  //交流线段/馈线段（包括 站外-电缆段、导线段）【大部分同XML，小部分比XML多】(会连到别的单线图上)
    "BusbarSection_Layer",  //母线【同 XML】
    "Junction_Layer",  //（根据xml，包括站外-电缆终端头，还有**环网柜内三角形的玩意儿**）【全部比XML多】（因为XML不含有32000000，也不知道是个啥设备）
    "Breaker_Layer",  //断路器【绝大部分同xml，少部分比xml少很多】（有些是故障的后接上的、备用的）（多得那部分是组合于CompositeSwicher的）
    "CompositeSwitch_Layer",  //混合开关，即站内-三工位负荷开关【同 xml CompositeSwitch】
    "EnergyConsumer_Layer",  //J圆圈，负荷/能源消耗者，一般为站外-中压用户接入点【同xml】
    "PowerTransformer_Layer",  //变电站【同xml】
    "LoadBreakSwitch_Layer",  //负荷开关，（根据xml，包括柱上-负荷开关、站内-负荷开关）【xml多，svg几乎没有】
    "PoleCode_Layer",  //杆塔节点，包括耐张杆塔、直线杆塔【svg普遍比xml多】(有可能会连到别的单线图上)
    "Fuse_Layer",  //（根据xml，包括柱上-跌落式熔断器）【同xml】
    "Disconnector_Layer"  //隔离开关【XML多，svg几乎不显示】
]
```

- 看看ACLineSegment_Layer svg多了哪些东西，是否和别的图右连接关系，怎么连得**（单线图之间到底怎么连得）**包括polecode
- 看xml里面说了和什么相连，但是svg里面没展现出这个设备，反之同理



- Junction

## 节点node与关系edge

数据源：node与edge全部基于xml文件

目标：导出，增量式写入数据库，保证新的图或者旧图的更新，可以不重复的写入、更新图数据库

### 节点

xml里拥有psrid的设备作为node

        "Substation", //子站，如环网柜、变电站等
        "Feeder",  // 馈线，例如 10kV青岛路线144【svg无】
        "Breaker", //断路器
        "ACLineSegment",  // 交流线段，包括 站外-电缆段、导线段
        "Junction",  // (干啥的？包括站外-电缆终端头，还有环网柜内三角形的玩意儿)
        "PowerTransformer",  // 变电站
        "PowerTransformerEnd",  // 变电站End终端（猜测是变电站的出节点，进入下一层次【svg无】
        "EnergyConsumer",  // 电力能源消费者J
        "PoleCode",   // 杆塔节点，包括耐张杆塔、直线杆塔
        "BusbarSection", //母线
        "CompositeSwitch",  // 混合开关/接地开关，即站内-三工位负荷开关
    	"GroundDisconnector",  //接地刀闸（附属于混合开关？）【svg无】
        "Disconnector"  // 站内隔离开关【svg几乎无】
        "LoadBreakSwitch",  // 负荷开关，包括柱上-负荷开关、站内-负荷开关等【svg几乎无】
        "Fuse",  //（干啥的？）包括柱上-跌落式熔断器、压变柜

#### Substation

### 关系

CONNECT_WITH

|                         | Substation | Feeder | Breaker | ACLineSegment | Junction | PowerTransformer | PowerTransformerEnd | EnergyConsumer | PoleCode | BusbarSection | CompositeSwitch | GroundDisconnector | Disconnector | LoadBreakSwitch | Fuse |
| ----------------------- | ---------- | ------ | ------- | ------------- | -------- | ---------------- | ------------------- | -------------- | -------- | ------------- | --------------- | ------------------ | ------------ | --------------- | ---- |
| **Substation**          |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **Feeder**              |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **Breaker**             |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **ACLineSegment**       |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **Junction**            |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **PowerTransformer**    |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **PowerTransformerEnd** |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **EnergyConsumer**      |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **PoleCode**            |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **BusbarSection**       |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **CompositeSwitch**     |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **GroundDisconnector**  |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **Disconnector**        |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **LoadBreakSwitch**     |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |
| **Fuse**                |            |        |         |               |          |                  |                     |                |          |               |                 |                    |              |                 |      |

#### 设备连接关系connect_with

- ACLineSegment 交流线段，包括 站外-电缆段201、导线段101，的两端，二比一
- Breaker，包括站内断路器305的两端，柱上-断路器111二比一
- Junction，站外-电缆终端头202、203的节点，一比一
- PowerTransformer，包括柱上变压器110的两端，配电变压器-双绕组30200002的两端、站内-所用变303两端，柱上-用户变压器11000001，二比一
- LoadBreakSwitch，包括柱上-负荷开关112的两端，站内-负荷开关(附属于CompositeSwitcher)307的两端，二比一
- EnergyConsumer，包括站外-中压用户接入点370的节点，一比一
- BusbarSection，包括母线311的节点，一比一
- GroundDisconnector，包括站内接地刀闸(附属于CompositeSwicher)30600001的两端，二比一
- Disconnector，柱上-隔离开关113的两端，柱上-重合器114，站内-隔离开关306，二比一
- Fuse，柱上-负荷式熔断器11500001的两端，柱上-跌落式熔断器115的两端，站内-熔断器309的两端，二比一

|                    | ACLineSegment | Junction | PowerTransformer | BusbarSection | LoadBreakSwitch | GroundDisconnector | EnergyConsumer | Breaker | Disconnector | Fuse |
| ------------------ | ------------- | -------- | ---------------- | ------------- | --------------- | ------------------ | -------------- | ------- | ------------ | ---- |
| ACLineSegment      |               | 1        |                  |               |                 |                    |                | 1       |              |      |
| Junction           | 1             |          |                  |               |                 |                    |                | 1       |              |      |
| PowerTransformer   |               |          |                  |               |                 |                    |                |         |              |      |
| BusbarSection      |               |          |                  |               |                 |                    |                |         |              |      |
| LoadBreakSwitch    |               |          |                  |               |                 |                    |                |         |              |      |
| GroundDisconnector |               |          |                  |               |                 |                    |                |         |              |      |
| EnergyConsumer     |               |          |                  |               |                 |                    |                |         |              |      |
| Breaker            | 1             | 1        |                  |               |                 |                    |                |         |              |      |
| Disconnector       |               |          |                  |               |                 |                    |                |         |              |      |
| Fuse               |               |          |                  |               |                 |                    |                |         |              |      |



#### 设备层次关系

