import scalariform.formatter.preferences._

name := """akka-video-stream"""

version := "1.1"

scalaVersion := "2.11.7"
//classpathTypes += "maven-plugin"

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

//libraryDependencies ++= Seq("org.bytedeco" %% "javacv" % "0.8" )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-M5"
)

libraryDependencies +=
  "com.typesafe.akka" %% "akka-remote" % "2.3.7"

// Platform classifier for native library dependencies
lazy val platform = org.bytedeco.javacpp.Loader.getPlatform
val javacppVersion = "0.11"
// Some dependencies like `javacpp` are packaged with maven-plugin packaging
classpathTypes += "maven-plugin"
libraryDependencies ++= Seq(
  "org.bytedeco"                 % "javacpp"         % javacppVersion,
  "org.bytedeco"                 % "javacv"          % javacppVersion,
  "org.bytedeco.javacpp-presets" % "opencv" % ("2.4.11-" + javacppVersion) classifier "",
  "org.bytedeco.javacpp-presets" % "opencv" % ("2.4.11-" + javacppVersion) classifier platform,
  "org.scala-lang.modules"      %% "scala-swing"     % "1.0.1",
  "junit"                        % "junit"           % "4.12" % "test",
  "com.novocode"                 % "junit-interface" % "0.11" % "test"
)



//libraryDependencies ++= Seq(
//  "org.bytedeco" %% "javacv" % "0.8"
//)



//val javacv =
//  "com.googlecode.javacv" %
//    "javacv" %
//    "0.7" classifier "linux-x86_64" classifier "macosx-x86_64" classifier ""

//libraryDependencies += javacv
autoCompilerPlugins := true

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)

fork in run := true
