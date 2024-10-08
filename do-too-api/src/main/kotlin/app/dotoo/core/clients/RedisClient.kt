package app.dotoo.core.clients

import app.dotoo.config.RedisConfig
import app.dotoo.di.IClosableComponent
import org.koin.core.annotation.Single
import redis.clients.jedis.JedisPool

@Single(createdAtStart = true)
class RedisClient : IClosableComponent {
    val jedisPool = JedisPool(RedisConfig.connectionString)

    override suspend fun close() {
        jedisPool.close()
    }
}
