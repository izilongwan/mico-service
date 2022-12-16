if redis == nil then
    package.path = package.path .. ';/usr/local/share/lua/5.4/redis.lua'
    local red = require('redis')
    redis = red.connect('127.0.0.1', 6379)
    KEYS = {'test:order|888', 'test:order|888:user|1002'}
    ARGV = {'1670744290', '1002', '888', '0984324', 'stream:orders'}
end

local streamName = 'stream:orders'
local order_key = KEYS[1]

local list = redis.call('HMGET', order_key, 'start_time', 'end_time', 'store')

if list == nil then
    return 1
end

local start_time, end_time, ts = tonumber(list[1]), tonumber(list[2]), tonumber(ARGV[1])

if ts < start_time and ts > end_time then
    return 2
end

local u_id, user_key = ARGV[2], KEYS[2]

if redis.call('SISMEMBER', user_key, u_id) == 1 then
    return 3
end

local store = tonumber(list[3])

if store <= 0 then
    return 4
end

redis.call('HINCRBY', order_key, 'store', -1)
redis.call('SADD', user_key, u_id)
redis.call('EXPIRE', order_key, 60 * 60 * 24 * 30)
redis.call('EXPIRE', user_key, 60 * 60 * 24 * 30)
redis.call('XADD', streamName, '*', 'user_id', u_id, 'goods_id', ARGV[3], 'order_id', ARGV[4])

return 0
