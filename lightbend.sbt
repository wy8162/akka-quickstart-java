resolvers in ThisBuild += "lightbend-commercial-mvn" at
  "https://repo.lightbend.com/pass/z5_Hk6iM1SAJgzjvgxlw9r4JmTOs7ub0W0yDEVpav8UFiXc1/commercial-releases"
resolvers in ThisBuild += Resolver.url("lightbend-commercial-ivy",
  url("https://repo.lightbend.com/pass/z5_Hk6iM1SAJgzjvgxlw9r4JmTOs7ub0W0yDEVpav8UFiXc1/commercial-releases"))(Resolver.ivyStylePatterns)