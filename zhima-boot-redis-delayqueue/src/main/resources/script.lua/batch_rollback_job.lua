local jobDetailKey = KEYS[1];
local jobIndexKey = KEYS[2];
local jobBackupKey = KEYS[3];
local jobDeadLetterKey = KEYS[4];
local messageIds = cjson.decode(ARGV[1]);
local maxRetryTimes = tonumber(ARGV[2]);

for _, messageId in pairs(messageIds) do
    local messageJson = redis.call('HGET', jobDetailKey, messageId);
    if messageJson then
        local message = cjson.decode(messageJson);
        message['retryTimes'] = tonumber(message['retryTimes']) + 1;
        local shouldRunTime = tonumber(message['shouldRunTime']);

        if tonumber(message['retryTimes']) > maxRetryTimes then
            redis.call('ZREM', jobBackupKey, messageId);
            redis.call('ZADD', jobDeadLetterKey, shouldRunTime, messageId);
        else
            redis.call('ZREM', jobBackupKey, messageId);
            redis.call('ZADD', jobIndexKey, shouldRunTime, messageId);
        end
        redis.call("HSET", jobDetailKey, messageId, cjson.encode(message));
    end
end




