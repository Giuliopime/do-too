# DoToo API
those are the APIs for the [DoToo application](https://github.com/Giuliopime/do-too)!

# stack
- [kotlin](https://kotlinlang.org): as the main language
- [gradle](https://gradle.org): building and dependency management
- [ktor](https://ktor.io): web server
- [postgres](https://www.postgresql.org): db
- [redis](https://redis.io): cache
- [brevo](https://www.brevo.com/products/transactional-email): transactional email
- [docker](https://docker.com): for building the API docker image
- [docker compose](https://docs.docker.com/compose): for setting up the development environment

# features
- typed environment variables loader (`/src/main/kotlin/app/dotoo/config`)
- typed IDs (`/src/main/kotlin/app/dotoo/core/logic/typedId`)
- dependency injection using [koin](https://insert-koin.io)
- postgres client for storing data in a db
- redis client for caching
- email (with verification) and password user authentication with session management
- serialization, deserialization, request validation

## articles
| title                                    |      |   |
|------------------------------------------|------------|---|
| managing environment variables in Kotlin | [Medium](https://medium.com/@giuliopime/managing-environment-variables-in-kotlin-with-ease-and-type-safety-dotoo-backend-42882a1371ff) | [Blog](https://blog.giuliopime.dev/posts/env-variables-kotlin/#sources-and-mentions) |
