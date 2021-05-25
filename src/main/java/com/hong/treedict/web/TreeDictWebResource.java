package com.hong.treedict.web;

import com.baomidou.mybatisplus.extension.api.R;
import com.hong.treedict.po.TreeDictPO;
import com.hong.treedict.service.TreeDictService;
import com.hong.treedict.service.bo.TreeDictOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 在父节点下第一个位置插入节点
     *
     * @param operation 树节点操作对象
     * @return 统一响应对象
     */
    public R<?> insertPH(TreeDictOperation operation) {
        try {
            treeDictService.insertPH(operation);
            return R.ok(null);
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("插入节点失败：" + ex.getMessage());
        }
    }

    /**
     * 在父节点下最后一个位置插入节点
     *
     * @param operation 树节点操作对象
     * @return 统一响应对象
     */
    public R<?> insertPT(TreeDictOperation operation) {
        try {
            treeDictService.insertPT(operation);
            return R.ok(null);
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("插入节点失败：" + ex.getMessage());
        }
    }

    /**
     * 根据左边旁系定位插入节点
     *
     * @param operation 树节点操作对象
     * @return 统一响应对象
     */
    public R<?> insertL(TreeDictOperation operation) {
        try {
            treeDictService.insertL(operation);
            return R.ok(null);
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("插入节点失败：" + ex.getMessage());
        }
    }

    /**
     * 根据右边旁系定位插入节点
     *
     * @param operation 树节点操作对象
     * @return 统一响应对象
     */
    public R<?> insertR(TreeDictOperation operation) {
        try {
            treeDictService.insertR(operation);
            return R.ok(null);
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("插入节点失败：" + ex.getMessage());
        }
    }

    /**
     * 根据父节点和左边旁系定位插入节点
     *
     * @param operation 树节点操作对象
     * @return 统一响应对象
     */
    public R<?> insertPL(TreeDictOperation operation) {
        try {
            treeDictService.insertPL(operation);
            return R.ok(null);
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("插入节点失败：" + ex.getMessage());
        }
    }

    /**
     * 根据父节点和右边旁系定位插入节点
     *
     * @param operation 树节点操作对象
     * @return 统一响应对象
     */
    public R<?> insertPR(TreeDictOperation operation) {
        try {
            treeDictService.insertPR(operation);
            return R.ok(null);
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("插入节点失败：" + ex.getMessage());
        }
    }

    /**
     * 根据主键查找节点
     *
     * @param id 主键
     * @return 查找结果
     */
    @GetMapping("/find/{id}")
    public R<TreeDictPO> findById(@PathVariable("id") String id) {
        try {
            return R.ok(treeDictService.findById(id));
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("查询节点失败：" + ex.getMessage());
        }
    }

    /**
     * 根据码值和命名空间查找节点
     *
     * @param code      码值
     * @param namespace 命名空间
     * @return 查找结果
     */
    @GetMapping("/find/{code}/{namespace}")
    public R<TreeDictPO> findByCode(@PathVariable("code") String code, @PathVariable("namespace") String namespace) {
        try {
            return R.ok(treeDictService.findByCode(code, namespace));
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("查询节点失败：" + ex.getMessage());
        }
    }

    /**
     * 根据命名空间查找根节点
     *
     * @param namespace 命名空间
     * @return 根节点
     */
    @GetMapping("/root/{namespace}")
    public R<TreeDictPO> findRootByNamespace(@PathVariable("namespace") String namespace) {
        try {
            return R.ok(treeDictService.findRootByNamespace(namespace));
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("查询根节点失败：" + ex.getMessage());
        }
    }

    /**
     * 根据左、右值筛选子孙节点（不包含自己）
     *
     * @param leftValue  左值
     * @param rightValue 右值
     * @return 子孙节点集合
     */
    @GetMapping("/children/range/{left}/{right}")
    public R<List<TreeDictPO>> findChildren(@PathVariable("left") Long leftValue, @PathVariable("right") Long rightValue) {
        try {
            return R.ok(treeDictService.findChildren(leftValue, rightValue));
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("查询子节点失败：" + ex.getMessage());
        }
    }

    /**
     * 根据码值和命名空间查找子孙节点（不包含自己）
     *
     * @param code      码值
     * @param namespace 命名空间
     * @return 子孙节点集合
     */
    @GetMapping("/children/find/{code}/{namespace}")
    public R<List<TreeDictPO>> findChildren(@PathVariable("code") String code, @PathVariable("namespace") String namespace) {
        try {
            return R.ok(treeDictService.findChildren(code, namespace));
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("查询子节点失败：" + ex.getMessage());
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{code}/{namespace}")
    public R<?> delete(@PathVariable("code") String code, @PathVariable("namespace") String namespace) {
        try {
            treeDictService.deleteByCode(code, namespace);
            return R.ok(null);
        } catch (Exception ex) {
            // Handle exception.
            log.error(ex.getMessage(), ex);
            return R.failed("删除节点失败：" + ex.getMessage());
        }
    }

}
