name := "sealed-enumerations"

scalacOptions ++= Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
  "com.netaporter"      %%  "scala-uri"  %  "0.4.11"
, "com.typesafe.play"   %%  "play"       %  "2.4.6"
, "com.typesafe.slick"  %%  "slick"      %  "3.0.3"
)
