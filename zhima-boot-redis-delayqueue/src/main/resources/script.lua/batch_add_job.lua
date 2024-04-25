local jobDetailKey = KEYS[1];
local jobIndexKey = KEYS[2];

-- 将delayMessage json转成 lua对象
local delayMessages = cjson.decode(ARGV[1]);

-- 批量获取延迟消息，逐一获取score，如果存在则不添加，不存在则添加
for _, message in pairs(delayMessages) do
    local id = message["id"];
    if redis.call('ZSCORE', jobIndexKey, id) then
        return 'false';
    end
end

for _, delayMessage in pairs(delayMessages) do
    local id = delayMessage["id"];
    local shouldRunTime = tonumber(delayMessage["shouldRunTime"]);
    local delayMessageJson = cjson.encode(delayMessage);
    redis.call('HSET', jobDetailKey, id, delayMessageJson);
    redis.call('ZADD', jobIndexKey, shouldRunTime, id);
end
return 'true';
