addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")
//addSbtPlugin("com.github.shivawu" % "sbt-maven-plugin" % "0.1.2")
classpathTypes += "maven-plugin"

// javacpp `Loader` is used to determine `platform` classifier in the project`s `build.sbt`
// We define dependency here (in folder `project`) since it is used by the build itself.
libraryDependencies += "org.bytedeco" % "javacpp" % "0.11"
