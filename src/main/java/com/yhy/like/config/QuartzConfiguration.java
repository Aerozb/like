package com.yhy.like.config;

import com.yhy.like.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 凌晨三点定时从redis取数据到数据库
 */
@Configuration
public class QuartzConfiguration {

    @Autowired
    private LikeService likeService;

    @Autowired
    private MethodInvokingJobDetailFactoryBean jobDetailFactoryBean;

    @Autowired
    private CronTriggerFactoryBean cronTriggerFactoryBean;

    /**
     * 任务细节工厂
     */
    @Bean
    public MethodInvokingJobDetailFactoryBean createJobDetail() {
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        jobDetailFactoryBean.setName("like_save_daily_job");
        jobDetailFactoryBean.setGroup("like_save_daily_group");
        //禁止任务并行执行，即一个任务完成以后另一个任务开始
        jobDetailFactoryBean.setConcurrent(false);
        jobDetailFactoryBean.setTargetObject(likeService);
        jobDetailFactoryBean.setTargetMethod("saveOrDeleteAll");
        return jobDetailFactoryBean;
    }

    /**
     * 时间触发器工厂
     */
    @Bean
    public CronTriggerFactoryBean createJobTrigger() {
        CronTriggerFactoryBean jobTrigger = new CronTriggerFactoryBean();
        jobTrigger.setName("product_sell_daily_trigger");
        jobTrigger.setGroup("like_save_daily_group");
        jobTrigger.setJobDetail(jobDetailFactoryBean.getObject());
        jobTrigger.setCronExpression("0 0 3 * * ? ");
        return jobTrigger;
    }

    /**
     * 创建调度工厂
     */
    @Bean
    public SchedulerFactoryBean createScheduler() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(cronTriggerFactoryBean.getObject());
        return scheduler;
    }

}
