local jobIndexKey = KEYS[1];
local jobBackupKey = KEYS[2];

local count = tonumber(ARGV[1]);
local currentTimeMillis = tonumber(ARGV[2]);

local indexes = redis.call("ZRANGEBYSCORE", jobIndexKey, "-inf", currentTimeMillis, "LIMIT", 0, count);

if #indexes > 0 then
    for _, id in ipairs(indexes) do
        local shouldRunTime = redis.call('ZSCORE', jobIndexKey, id);
        redis.call("ZADD", jobBackupKey, shouldRunTime, id);
        redis.call("ZREM", jobIndexKey, id);
    end
    return cjson.encode(indexes);
end
