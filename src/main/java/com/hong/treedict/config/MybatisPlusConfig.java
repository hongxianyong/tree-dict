package com.hong.treedict.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Hongxy
 * @description mybatis-plus 配置
 * @since 2021/5/24
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.hong.treedict.dal")
public class MybatisPlusConfig {
    /**
     * 分页插件
     * 乐观锁插件
     * 全表更新、删除阻断插件
     *
     * @return 拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    /**
     * 公共字段设置插件
     *
     * @return 元信息处理器
     */
    @Bean
    public MetaObjectHandler templateColumnsHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createdBy", String.class, "system");
                this.strictInsertFill(metaObject, "createdTime", String.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                this.strictInsertFill(metaObject, "lastModifiedBy", String.class, "system");
                this.strictInsertFill(metaObject, "lastModifiedTime", String.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                this.strictInsertFill(metaObject, "deletedFlag", Long.class, 0L);
                this.strictInsertFill(metaObject, "version", Long.class, 1L);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "lastModifiedBy", String.class, "system");
                this.strictInsertFill(metaObject, "lastModifiedTime", String.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
            }
        };
    }
}
