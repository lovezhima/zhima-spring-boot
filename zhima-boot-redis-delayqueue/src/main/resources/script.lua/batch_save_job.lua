-- 批量添加一个延迟任务(支持id去重)
-- 1.校验数据
-- 2.添加数据

local jobDetailKey = KEYS[1];
local jobIndexKey = KEYS[2];
local content = cjson.decode(ARGV[1]);
local compareFlag = ARGV[2];

for _, message in pairs(content) do
    local topicId = message["id"];
    local score = tonumber(message["shouldRunTime"]);

    local compare = false;
    if compareFlag and "Y" == compareFlag then
        local oldScore = redis.call('ZSCORE', jobIndexKey, topicId);
        compare = oldScore and tonumber(oldScore) > score;
    end

    -- 比较时间戳
    if not compare then
        local contentJson = cjson.encode(message);
        redis.call('HSET', jobDetailKey, topicId, contentJson);
        redis.call('ZADD', jobIndexKey, score, topicId);
    end
end
return 'true';
