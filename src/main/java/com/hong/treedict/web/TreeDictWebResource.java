package com.hong.treedict.web;

import com.baomidou.mybatisplus.extension.api.R;
import com.hong.treedict.service.TreeDictService;
import com.hong.treedict.service.bo.TreeDictOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Hongxy
 * @description web 接口资源
 * @since 2021/5/24
 */
@Slf4j
@RequestMapping("/tree-dict")
@RestController
public class TreeDictWebResource {

    @Resource
    private TreeDictService treeDictService;

    /**
     * 创建根节点
     *
     * @param operation 节点操作对象
     * @return 统一响应对象
     */
    @PostMapping("/create/root")
    public R<?> createRoot(@RequestBody TreeDictOperation operation) {
        try {
            treeDictService.createRoot(operation);
            return R.ok(null);
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("创建根节点失败：" + ex.getMessage());
        }
    }
}
