package com.lovezhima.boot.redis.delayqueue;

/**
 * Redis延迟队列Lua脚本常量
 *
 * @author king
 * @date 2023/8/13
 */
public interface RedisDelayQueueConstant {

    interface LuaScript {

        /**
         * 添加JOB
         */
        String ADD_JOB_REDIS_SCRIPT_LUA_PATH = "script/lua/add_job.lua";

        /**
         * 批量保存
         */
        String BATCH_ADD_JOB_REDIS_SCRIPT_LUA_PATH = "script/lua/batch_add_job.lua";

        /**
         * 保存JOB
         */
        String BATCH_SAVE_JOB_REDIS_SCRIPT_LUA_PATH = "script/lua/save_job.lua";

        /**
         * 强制提交JOB
         */
        String FORCE_COMMIT_JOB_REDIS_SCRIPT_LUA_PATH = "script/lua/force_commit_job.lua";

        /**
         * 提交JOB
         */
        String COMMIT_JOB_REDIS_SCRIPT_LUA_PATH = "script/lua/commit_job.lua";

        /**
         * 批量提交JOB
         */
        String BATCH_COMMIT_JOB_REDIS_SCRIPT_LUA_PATH = "script/lua/batch_commit_job.lua";

        /**
         * 回滚JOB
         */
        String ROLLBACK_JOB_REDIS_SCRIPT_LUA_PATH = "script/lua/rollback_job.lua";

        /**
         * 批次回滚job
         */
        String BATCH_ROLLBACK_JOB_REDIS_SCRIPT_LUA_PATH = "script/lua/batch_rollback_job.lua";

        /**
         * POP JOB
         */
        String POP_JOB_REDIS_SCRIPT_LUA_PATH = "script/lua/pop_job.lua";
    }

    class DelayQueue {
        private static final String DQ_JOB_DETAIL = "dq:%s:{%s}_job_detail";

        private static final String DQ_JOB_INDEX = "dq:%s:{%s}_job_index";

        private static final String DQ_JOB_BACKUP = "dq:%s:{%s}_job_backup";

        public static final String DQ_JOB_DEAD = "dq:%s:{%s}_job_dead";

        private static String hashTag;

        public static String getDqJobDetailKey(String topic) {
            return String.format(DQ_JOB_DETAIL, hashTag, topic);
        }

        public static String getDqJobIndexKey(String topic) {
            return String.format(DQ_JOB_INDEX, hashTag, topic);
        }

        public static String getDqJobBackupKey(String topic) {
            return String.format(DQ_JOB_BACKUP, hashTag, topic);
        }

        public static String getDqJobDead(String topic) {
            return String.format(DQ_JOB_DEAD, hashTag, topic);
        }
    }
}
