package com.hong.treedict.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hong.treedict.dal.TreeDictMapper;
import com.hong.treedict.po.TreeDictPO;
import com.hong.treedict.service.TreeDictService;
import com.hong.treedict.service.bo.TreeDictOperation;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * tree-dict 服务实现类
 * 满足树形字典的一般维护场景增、删、改、查、向下查询子树、父节点回溯查询等
 * 一般情况下树的层级关系可用高度和深度来衡量，考虑到树型字典属于增加和查询
 * 场景比删除和平移场景更多，高度在每次增加和删除的时候都需要动态更新，深度
 * 在增加的时候只需要在父节点深度上+1即可，删除的时候不需要变动父节点，所以
 * 使用深度来衡量层级关系。其实直接使用
 * select count(id) as depth from tree_dict s
 * where s.left_value <= #{leftValue} and s.right_value >= #{rightValue}
 * 也可以查出深度，但是树形字典查询的场景更多，所以为了查询的方便还是考虑增
 * 加深度字段。
 * </p>
 *
 * @author Hongxy
 * @since 2021-05-24
 */
@Service
public class TreeDictServiceImpl extends ServiceImpl<TreeDictMapper, TreeDictPO> implements TreeDictService {

    /**
     * 创建树根节点
     * 创建树根节点的时候，其父节点主键默认为 0
     *
     * @param root 根节点属性
     */
    @Override
    public void createRoot(TreeDictOperation root) {
        // 根节点唯一性校验
        TreeDictPO exist = findRootByNamespace(root.getNameSpace());
        if (Objects.nonNull(exist)) {
            throw new IllegalArgumentException("ROOT_HAS_BEEN_EXIST");
        }
        TreeDictPO treeDict = buildTreeDict(null, 1L, root.getCode(), root.getDescription(), 1L, 2L, root.getNameSpace());
        saveWithCatchDuplicateKeyException(treeDict);
    }

    /**
     * 邻接父节点定位在第一个位置插入节点
     *
     * @param operation 树形字典操作值对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertPH(TreeDictOperation operation) {
        // 父节点
        String parentId = operation.getParentId();
        TreeDictPO parent = Objects.requireNonNull(this.findById(parentId), "Node [ " + parentId + " ] does not exist.");
        // 父节点左值
        Long pl = parent.getLeftValue();
        // 当右值 > pl 时，将右值 + 2
        this.baseMapper.updateRightIncreaseTwoWhenRightGreaterThan(pl);
        // 当左值 > pl 时，将左值 + 2
        this.baseMapper.updateLeftIncreaseTwoWhenLeftGreaterThan(pl);
        /*
         * 设置父节点
         * 设置深度为父节点深度+1
         * 设置左值为父节点左值+1
         * 设置右值为父节点左值+2
         */
        TreeDictPO newTreeDict = buildTreeDict(parent, parent.getDepth() + 1, operation.getCode(), operation.getDescription(), pl + 1, pl + 2, operation.getNameSpace());
        saveWithCatchDuplicateKeyException(newTreeDict);
    }

    /**
     * 父邻接节点定位在最后一个位置插入节点
     *
     * @param operation 树形字典操作值对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertPT(TreeDictOperation operation) {
        // 父节点
        String parentId = operation.getParentId();
        TreeDictPO parent = Objects.requireNonNull(this.findById(parentId), "Node [ " + parentId + " ] does not exist.");
        // 父节点右值
        Long pr = parent.getRightValue();
        // 当右值 > pr - 1 时，将右值 + 2
        this.baseMapper.updateRightIncreaseTwoWhenRightGreaterThan(pr - 1);
        // 当左值 > pr 时，将左值 + 2
        this.baseMapper.updateLeftIncreaseTwoWhenLeftGreaterThan(pr);
        /*
         * 设置父节点
         * 设置深度为父节点深度+1
         * 设置左值为父节点右值
         * 设置右值为父节点右值+1
         */
        TreeDictPO newTreeDict = buildTreeDict(parent, parent.getDepth() + 1, operation.getCode(), operation.getDescription(), pr, pr + 1, operation.getNameSpace());
        saveWithCatchDuplicateKeyException(newTreeDict);
    }

    /**
     * 邻接父节点 + 邻接左旁系定位插入
     *
     * @param operation 树形字典操作值对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertPL(TreeDictOperation operation) {
        // 父节点
        String parentId = operation.getParentId();
        TreeDictPO parent = Objects.requireNonNull(this.findById(parentId), "Node [ " + parentId + " ] does not exist.");
        // 左节点
        String leftId = operation.getLeftCollateralId();
        TreeDictPO left = Objects.requireNonNull(this.findById(leftId), "Node [ " + leftId + " ] does not exist.");
        insertPL(operation, parent, left);
    }

    /**
     * 邻接父节点 + 邻接右旁系定位插入
     *
     * @param operation 树形字典操作值对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertPR(TreeDictOperation operation) {
        // 父节点
        String parentId = operation.getParentId();
        TreeDictPO parent = Objects.requireNonNull(this.findById(parentId), "Node [ " + parentId + " ] does not exist.");
        // 右节点
        String rightId = operation.getRightCollateralId();
        TreeDictPO right = Objects.requireNonNull(this.findById(rightId), "Node [ " + rightId + " ] does not exist.");
        insertPR(operation, parent, right);
    }

    /**
     * 邻接左旁系定位插入
     *
     * @param operation 树形字典操作值对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertL(TreeDictOperation operation) {
        // 左节点
        String leftId = operation.getLeftCollateralId();
        TreeDictPO left = Objects.requireNonNull(this.findById(leftId), "Node [ " + leftId + " ] does not exist.");
        // 父节点
        String parentId = left.getParentId();
        TreeDictPO parent = Objects.requireNonNull(this.findById(parentId), "Node [ " + parentId + " ] does not exist.");
        insertPL(operation, parent, left);
    }

    /**
     * 邻接右旁系定位插入
     *
     * @param operation 树形字典操作值对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertR(TreeDictOperation operation) {
        // 右节点
        String rightId = operation.getRightCollateralId();
        TreeDictPO right = Objects.requireNonNull(this.findById(rightId), "Node [ " + rightId + " ] does not exist.");
        // 父节点
        String parentId = right.getParentId();
        TreeDictPO parent = Objects.requireNonNull(this.findById(parentId), "Node [ " + parentId + " ] does not exist.");
        insertPR(operation, parent, right);
    }

    /**
     * 根据字典码值删除（当然前提是得指定命名空间，因为一个字典表不可能只存一套字典，多套字典可能存在同样的码值，所以得用命名空间加以区分）
     *
     * @param code      字典码值（例如：Y - 是，N - 否：M - 男，F - 女）
     * @param namespace 命名空间中英文自己考虑，能区分就行（例如：布尔值；性别；gender）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByCode(String code, String namespace) {
        TreeDictPO treeDict = this.findByCode(code, namespace);
        if (Objects.nonNull(treeDict)) {
            delete(treeDict);
        }
    }

    /**
     * 指定节点删除
     *
     * @param treeDict 要删除的节点
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(TreeDictPO treeDict) {
        Long left = treeDict.getLeftValue();
        Long right = treeDict.getRightValue();
        // 删除自己和子孙节点
        this.deleteSelfAndChildren(left, right);
        // 末项 - 首项 + 1  = 项数
        long gap = right - left + 1;
        // 将右值 <= right 的节点右值减去 gap
        this.baseMapper.updateRightDecreaseGapWhenRightGreaterThen(right, gap);
        // 将左值 >= right  的节点左值减去 gap
        this.baseMapper.updateLeftDecreaseGapWhenLeftGreaterThan(right, gap);
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 树形字典项
     */
    @Override
    public TreeDictPO findById(String id) {
        return this.getById(id);
    }

    /**
     * 根据字典码值查询子孙节点
     *
     * @param code      字典码值（例如：Y - 是，N - 否：M - 男，F - 女）
     * @param namespace 命名空间中英文自己考虑，能区分就行（例如：布尔值；性别；gender）
     * @return 子孙节点集合
     */
    @Override
    public List<TreeDictPO> findChildren(String code, String namespace) {
        TreeDictPO treeDict = this.findByCode(code, namespace);
        return this.findChildren(treeDict.getLeftValue(), treeDict.getRightValue());
    }

    /**
     * 根据 id 查找子孙节点（不包含自己）
     *
     * @param id 主键
     * @return 子孙节点集合
     */
    @Override
    public List<TreeDictPO> findChildren(String id) {
        TreeDictPO treeDict = this.findById(id);
        return this.findChildren(treeDict.getLeftValue(), treeDict.getRightValue());
    }

    /**
     * 构建树形字典项必要的参数
     *
     * @param parent      父节点
     * @param depth       深度
     * @param code        字典码值
     * @param description 码值翻译
     * @param leftValue   左值
     * @param rightValue  右值
     * @param nameSpace   命名空间
     * @return 树形字典项
     */
    private TreeDictPO buildTreeDict(TreeDictPO parent, Long depth, String code, String description, Long leftValue, Long rightValue, String nameSpace) {
        TreeDictPO treeDictCreator = new TreeDictPO();
        boolean root = Objects.isNull(parent);
        // 设置父节点
        treeDictCreator.setParentId(root ? "0" : parent.getId());
        // 设置深度为父节点深度+1
        treeDictCreator.setDepth(depth);
        treeDictCreator.setPath(root ? code : parent.getPath() + "->" + code);
        treeDictCreator.setCode(code);
        treeDictCreator.setDescription(description);
        // 设置左值为父节点左值+1
        treeDictCreator.setLeftValue(leftValue);
        // 设置右值为父节点左值+2
        treeDictCreator.setRightValue(rightValue);
        treeDictCreator.setNamespace(nameSpace);
        return treeDictCreator;
    }

    /**
     * 插入并捕获唯一约束键重复异常
     *
     * @param treeDict 需要保存的节点
     */
    private void saveWithCatchDuplicateKeyException(TreeDictPO treeDict) {
        try {
            this.save(treeDict);
        } catch (DuplicateKeyException ex) {
            throw new IllegalArgumentException("DUPLICATE_KEY_EXCEPTION");
        }
    }

    private void insertPL(TreeDictOperation operation, TreeDictPO parent, TreeDictPO left) {
        Long lr = left.getRightValue();
        // 当右值 > lr 时，将右值 + 2
        this.baseMapper.updateRightIncreaseTwoWhenRightGreaterThan(lr);
        // 当左值 > lr 时，将左值 + 2
        this.baseMapper.updateLeftIncreaseTwoWhenLeftGreaterThan(lr);
        /*
         * 设置父节点
         * 设置深度为父节点深度+1
         * 设置左值为左旁系节点右值+1
         * 设置右值为左旁系节点右值+2
         */
        TreeDictPO newTreeDict = buildTreeDict(parent, parent.getDepth() + 1, operation.getCode(), operation.getDescription(), lr + 1, lr + 2, operation.getNameSpace());
        saveWithCatchDuplicateKeyException(newTreeDict);
    }

    private void insertPR(TreeDictOperation operation, TreeDictPO parent, TreeDictPO right) {
        Long rl = right.getLeftValue();
        // 当右值 > rl - 1 时，将右值 + 2
        this.baseMapper.updateRightIncreaseTwoWhenRightGreaterThan(rl - 1);
        // 当左值 > rl - 1 时，将左值 + 2
        this.baseMapper.updateLeftIncreaseTwoWhenLeftGreaterThan(rl - 1);
        /*
         * 设置父节点
         * 设置深度为父节点深度+1
         * 设置左值为右旁系节点左值
         * 设置右值为右旁系节点左值+1（即：右旁系节点右值）
         */
        TreeDictPO newTreeDict = buildTreeDict(parent, parent.getDepth() + 1, operation.getCode(), operation.getDescription(), rl, rl + 1, operation.getNameSpace());
        saveWithCatchDuplicateKeyException(newTreeDict);
    }

    @Override
    public void deleteSelfAndChildren(Long leftValue, Long rightValue) {
        this.remove(Wrappers.<TreeDictPO>lambdaQuery().ge(TreeDictPO::getLeftValue, leftValue).le(TreeDictPO::getRightValue, rightValue));
    }

    @Override
    public TreeDictPO findByCode(String code, String namespace) {
        return this.getOne(Wrappers.<TreeDictPO>lambdaQuery()
                .eq(TreeDictPO::getCode, code).eq(TreeDictPO::getNamespace, namespace));
    }

    @Override
    public List<TreeDictPO> findChildren(Long leftValue, Long rightValue) {
        return this.list(Wrappers.<TreeDictPO>lambdaQuery().gt(TreeDictPO::getLeftValue, leftValue).lt(TreeDictPO::getRightValue, rightValue));
    }

    /**
     * 根据命名空间查找根节点
     *
     * @param namespace 命名空间
     * @return 根节点
     */
    @Override
    public TreeDictPO findRootByNamespace(String namespace) {
        return this.getOne(Wrappers.<TreeDictPO>lambdaQuery().eq(TreeDictPO::getNamespace, namespace).eq(TreeDictPO::getDepth, 1L).eq(TreeDictPO::getParentId, "0"));
    }
}
