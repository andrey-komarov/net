
assemblySettings

name := "lab3"

scalaVersion := "2.10.3"

libraryDependencies +=
  "org.scalacheck" %% "scalacheck" % "1.11.3" % "test"

resolvers += Resolver.sonatypeRepo("snapshots")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies +=
  "com.typesafe.akka" %% "akka-actor" % "2.3.0"

mainClass in(Compile, packageBin) := Some("ru.ifmo.ctddev.komarov.net.lab3.main.Test4")
