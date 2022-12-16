if (redis.call('GET', KEYS[1]) == ARGV[1]) then
    return redis.call('DEL', ARGV[1])
end

-- hgetall 获取成功返回ok
local v = redis.call('HMGET', 'test:order:hash', 'store', 1)

redis.call('expire', 'test:order:hash', 60 * 60 * 24)

return tonumber(v[1])
