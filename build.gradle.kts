plugins {
    id("java")
}

group = "ru.vsu.cs.ereshkin_a_v"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jfree:org.jfree.svg:5.0.5")
    implementation("com.intellij:forms_rt:7.0.3")

}

tasks.test {
    useJUnitPlatform()
}