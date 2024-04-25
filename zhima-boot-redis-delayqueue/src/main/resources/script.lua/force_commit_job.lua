-- 强制删除
local jobDetailKey = KEYS[1];
local jobIndexKey = KEYS[2];
local jobBackupKey = KEYS[3];
local id = ARGV[1];

redis.call('ZREM', jobBackupKey, id);
redis.call('ZREM', jobIndexKey, id);
redis.call('HDEL', jobDetailKey, id);







