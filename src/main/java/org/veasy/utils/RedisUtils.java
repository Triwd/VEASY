package org.veasy.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Service
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *  即刻报名版本
     */

    //获取活动剩余名额
    public Integer getRestNum(Integer activityId) {
        Integer result = 0;
        synchronized (this) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(activityId))) {
                try {
                    result = (Integer) redisTemplate.boundHashOps(activityId).get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return 0;
        }
        return result;
    }

    //(普通选拔)将一条学生报名活动记录添加到缓存中
    public boolean addStudentCacheInNormalMode(Integer activityId, Integer studentId) {
        synchronized (this) {
            boolean result = false;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(activityId))
                    && Boolean.FALSE.equals(redisTemplate.boundHashOps(activityId).hasKey(studentId))) {
                Integer restnum = (Integer) redisTemplate.boundHashOps(activityId).get(0);
                if (restnum != null && restnum > 0) {
                    try {
                        redisTemplate.boundHashOps(activityId).put(studentId, new Date());
                        restnum--;
                        redisTemplate.boundHashOps(activityId).put(0, restnum);
                        result = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }
    }

    //(多目标选拔)将一条学生报名活动记录添加到缓存中
    public boolean addStudentCacheInMOPMode(Integer activityId, Integer studentId) {
        synchronized (this) {
            boolean result = false;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(activityId))
                    && Boolean.FALSE.equals(redisTemplate.boundHashOps(activityId).hasKey(studentId))) {
                try {
                    redisTemplate.boundHashOps(activityId).put(studentId, new Date());
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }

    //(普通选拔)将一条学生报名活动记录从缓存中移除
    public boolean removeStudentCacheInNormalMode(Integer activityId, Integer studentId) {
        synchronized (this) {
            boolean result = false;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(activityId))
                    && Boolean.TRUE.equals(redisTemplate.boundHashOps(activityId).hasKey(studentId))) {
                Integer restNum = (Integer) redisTemplate.boundHashOps(activityId).get(0);
                try {
                    redisTemplate.boundHashOps(activityId).delete(studentId);
                    restNum++;
                    redisTemplate.boundHashOps(activityId).put(0, restNum);
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

    }

    //(多目标选拔)将一条学生报名活动记录从缓存中移除
    public boolean removeStudentCacheInMOPMode(Integer activityId, Integer studentId) {
        synchronized (this) {
            boolean result = false;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(activityId))
                    && Boolean.TRUE.equals(redisTemplate.boundHashOps(activityId).hasKey(studentId))) {
                try {
                    redisTemplate.boundHashOps(activityId).delete(studentId);
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }

    //将一项活动添加到缓存中（开启报名）
    public boolean addActivityCache(Integer activityId, Integer restNum) {
        boolean result = false;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(activityId))) {
            try {
                redisTemplate.boundHashOps(activityId).put(0, restNum);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //将一项活动从缓存中移除(关闭报名或取消活动)
    public boolean removeActivityCache(Integer activityId) {
        boolean result = false;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(activityId))) {
            try {
                redisTemplate.delete(activityId);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //根据id从缓存中获取活动志愿者id
    public Set<Integer> getAllStudentId(Integer activityId) {
        return redisTemplate.boundHashOps(activityId).keys();
    }

}

