plugins {
    id("maven-publish")
    id("earth.terrarium.cloche") version "0.11.4"
}



repositories {
    cloche.librariesMinecraft()

    mavenCentral()

    cloche {
        mavenNeoforgedMeta()
        mavenNeoforged()
    }

    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
}


cloche {
    minecraftVersion = "1.21.1"

    metadata {
        modId = "exclusions_lib"
        name = "Exclusions Lib"
        version = "1.0.2-NEO"
        license = "CC-BY-4.0"
        description = "Nothing Besides Remains."
        author("Athebyne")
        url = "https://modrinth.com/mod/exclusions-lib"
        sources = "https://github.com/f-raz0r/exclusionslib"
        issues = "https://github.com/f-raz0r/exclusionslib/issues"
        icon = "assets/exclusions_lib/icon.png"
    }
    singleTarget {
        neoforge {
            loaderVersion = "21.1.172"

            data()

            runs {
                server()
                client()
                data()
            }
        }
    }

}
