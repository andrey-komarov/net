import AssemblyKeys._ // put this at the top of the file

assemblySettings

name := "lab3"

libraryDependencies += "org.scala-lang" %% "scala-pickling" % "0.8.0-SNAPSHOT"

libraryDependencies +=
  "org.scalacheck" %% "scalacheck" % "1.11.3" % "test"

resolvers += Resolver.sonatypeRepo("snapshots")

mainClass in (Compile, packageBin) := Some("ru.ifmo.ctddev.komarov.net.lab3.main.Test2")
