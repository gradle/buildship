package Buildship

enum class EclipseVersion(val codeName: String, val versionNumber: String) {
    ECLIPSE4_8("Photon", "4.8"),
    ECLIPSE4_9("2018-09", "4.9"),
    ECLIPSE4_10("2018-12", "4.10"),
    ECLIPSE4_11("2019-03", "4.11"),
    ECLIPSE4_12("2019-06", "4.12"),
    ECLIPSE4_13("2019-09", "4.13"),
    ECLIPSE4_14("2019-12", "4.14"),
    ECLIPSE4_15("2020-03", "4.15"),
    ECLIPSE4_16("2020-06", "4.16"),
    ECLIPSE4_17("2020-09", "4.17"),
    ECLIPSE4_18("2020-12", "4.18"),
    ECLIPSE4_19("2021-03", "4.19"),
    ECLIPSE4_20("2021-06", "4.20"),
    ECLIPSE4_21("2021-09", "4.21"),
    ECLIPSE4_22("2021-12", "4.22"),
    ECLIPSE4_23("2022-03", "4.23"),
    ECLIPSE4_24("2022-06", "4.24"),
    ECLIPSE4_25("2022-09", "4.25"),
    ECLIPSE4_26("2022-12", "4.26"),
    ECLIPSE4_27("2023-03", "4.27"),
    ECLIPSE4_28("2023-06", "4.28"),
    ECLIPSE4_29("2023-09", "4.29"),
    ECLIPSE4_30("2023-12", "4.30"),
    ECLIPSE4_31("2024-03", "4.31"),
    ECLIPSE4_32("2024-06", "4.32"),
    ECLIPSE4_33("2024-09", "4.33"),
    ECLIPSE4_34("2024-12", "4.34"),
    ECLIPSE4_35("2025-03", "4.35"),
    ECLIPSE4_36("2025-06", "4.36");

    val updateSiteVersion: String
        get() = versionNumber.replace(".", "")

    val isLatest: Boolean
        get() = this == values().last()
}
