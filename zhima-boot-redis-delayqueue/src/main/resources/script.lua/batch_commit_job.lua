-- 批量提交、删除延迟任务
local jobDetailKey = KEYS[1];
local jobIndexKey = KEYS[2];
local jobBackupKey = KEYS[3];
local delayMessages = cjson.decode(ARGV[1]);

for _, delayMessage in pairs(delayMessages) do
    -- 当前消息执行时间
    local runTime = tonumber(delayMessage['shouldRunTime']);
    -- 当前消息id
    local id = delayMessage['id'];

    -- 查询延时队列最新消息
    local delayMessageJson = redis.call('HGET', jobDetailKey, id);

    if delayMessageJson then
        local lastMsg = cjson.decode(delayMessageJson);
        local shouldRunTime = tonumber(lastMsg['shouldRunTime']);

        if runTime >= shouldRunTime then
            redis.call('ZREM', jobBackupKey, id);
            redis.call('ZREM', jobIndexKey, id);
            redis.call('HDEL', jobDetailKey, id);
        else
            redis.call('ZREM', jobBackupKey, id);
        end
    end
end




