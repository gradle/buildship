package Buildship

import java.util.EnumSet

enum class Jdk(val majorVersion: String, val vendor: String, val availableOn: EnumSet<OS> = EnumSet.allOf(OS::class.java)) {
    ORACLE_JDK_8("8", "oracle"),
    ORACLE_JDK_9("9", "oracle", EnumSet.of(OS.LINUX)),
    OPEN_JDK_10("10", "oracle", EnumSet.of(OS.LINUX)),
    OPEN_JDK_11("11", "openjdk"),
    OPEN_JDK_12("12", "openjdk", EnumSet.of(OS.LINUX)),
    OPEN_JDK_13("13", "openjdk", EnumSet.of(OS.LINUX)),
    OPEN_JDK_14("14", "openjdk", EnumSet.of(OS.LINUX)),
    OPEN_JDK_15("15", "openjdk"),
    OPEN_JDK_16("16", "openjdk"),
    OPEN_JDK_17("17", "openjdk"),
    OPEN_JDK_18("18", "openjdk"),
    OPEN_JDK_19("19", "openjdk"),
    OPEN_JDK_20("20", "openjdk"),
    OPEN_JDK_21("21", "openjdk"),
    OPEN_JDK_22("22", "openjdk"),
    OPEN_JDK_23("23", "openjdk"),
    OPEN_JDK_24("24", "openjdk");

    fun getJavaHomePath(os: OS) = if (availableOn.contains(os)) "%${os.name.toLowerCase()}.java${majorVersion}.${vendor}.64bit%" else throw RuntimeException("$name not available on ${os.name}")

    companion object {
        fun javaInstallationPathsProperty(os: OS) = "\"-Porg.gradle.java.installations.paths=${ Jdk.values().filter { it.availableOn.contains(os) }.joinToString(",") { it.getJavaHomePath(os) } }\""
    }
}
