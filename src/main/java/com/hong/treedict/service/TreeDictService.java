package com.hong.treedict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hong.treedict.po.TreeDictPO;
import com.hong.treedict.service.bo.TreeDictOperation;

import java.util.List;

/**
 * <p>
 * tree-dict 服务类
 * </p>
 *
 * @author Hongxy
 * @since 2021-05-24
 */
public interface TreeDictService extends IService<TreeDictPO> {
    /**
     * 查询
     */
    TreeDictPO findById(String id);

    TreeDictPO findByCode(String code, String namespace);

    TreeDictPO findRootByNamespace(String namespace);

    List<TreeDictPO> findChildren(Long leftValue, Long rightValue);

    List<TreeDictPO> findChildren(String code, String namespace);

    List<TreeDictPO> findChildren(String id);

    /**
     * 删除
     */
    void delete(TreeDictPO treeDict);

    void deleteByCode(String code, String namespace);

    void deleteSelfAndChildren(Long leftValue, Long rightValue);

    /**
     * 插入
     */
    void createRoot(TreeDictOperation root);

    void insertPH(TreeDictOperation operation);

    void insertPT(TreeDictOperation operation);

    void insertL(TreeDictOperation operation);

    void insertR(TreeDictOperation operation);

    void insertPL(TreeDictOperation operation);

    void insertPR(TreeDictOperation operation);

    /**
     * 修改 TODO
     * 修改场景就比较灵活了，有根据 code 修改的，也有根据 id 修改的，
     * 有修改 code 的，也有修改其他属性的，甚至还有节点树平移等操作，
     * 所以这些操作就留给使用者自行完善
     */

}
