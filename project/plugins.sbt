resolvers += Classpaths.typesafeReleases

resolvers += Classpaths.sbtPluginReleases

resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("org.scoverage" %% "sbt-scoverage" % "0.99.7.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.7.1")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.6")

addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.11")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.6.0")