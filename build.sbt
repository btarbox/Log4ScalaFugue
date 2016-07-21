name := "log4scalafugue"

version := "2.0"

scalaVersion := "2.11.7"

autoCompilerPlugins := true

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0-M14"

libraryDependencies += "junit" % "junit" % "4.5" % "test"

libraryDependencies += "org.mongodb" % "mongo-java-driver" % "1.4"

libraryDependencies += "org.scala-tools.subcut" % "subcut_2.9.1" % "1.0"

libraryDependencies += "joda-time" % "joda-time" % "1.6"

libraryDependencies += "log4j" % "log4j" % "1.2.16"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.7"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.4.7"

libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.4.7"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.4.7"

libraryDependencies += "com.typesafe.akka" %% "akka-http-core" % "2.4.7"

libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7"

libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "2.4.7"

libraryDependencies <<= (scalaVersion, libraryDependencies) { (ver, deps) =>
  deps :+ "org.scala-lang" % "scala-compiler" % ver
}
