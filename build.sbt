name := "log4scalafugue"

version := "2.0"

scalaVersion := "2.9.1"

autoCompilerPlugins := true

resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/" 

addCompilerPlugin("org.scala-tools.subcut" % "subcut_2.9.1" % "1.0")

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "junit" % "junit" % "4.5" % "test"

libraryDependencies += "com.mongodb.casbah" % "casbah_2.9.0-1" % "2.1.5.0"

libraryDependencies += "org.mongodb" % "mongo-java-driver" % "1.4"

libraryDependencies += "org.scala-tools.subcut" % "subcut_2.9.1" % "1.0"

libraryDependencies += "joda-time" % "joda-time" % "1.6"

libraryDependencies += "log4j" % "log4j" % "1.2.16"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.9.1"

libraryDependencies <<= (scalaVersion, libraryDependencies) { (ver, deps) =>
  deps :+ "org.scala-lang" % "scala-compiler" % ver
}
