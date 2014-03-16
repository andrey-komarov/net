import sbtassembly.Plugin.AssemblyKeys
import AssemblyKeys._

assemblySettings

name := "lab3"

libraryDependencies +=
  "org.scalacheck" %% "scalacheck" % "1.11.3" % "test"

resolvers += Resolver.sonatypeRepo("snapshots")

mainClass in (Compile, packageBin) := Some("ru.ifmo.ctddev.komarov.net.lab3.main.Test4")
