plugins {
    id("java")
    id("java-library")
    // shadow
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "cn.thymechen.xiaoming.plugin"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // 本地库
    compileOnly(fileTree("libs"))

    // 数据库
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("mysql:mysql-connector-java:8.0.28")

    // lombok
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor ("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.24")
    testCompileOnly ("org.projectlombok:lombok:1.18.24")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
    exclude("META-INF/LICENSE")

    archiveFileName.set(project.name + '-' + archiveVersion.get() + '.' + archiveExtension.get())
}
