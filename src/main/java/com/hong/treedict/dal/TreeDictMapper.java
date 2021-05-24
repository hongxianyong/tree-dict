package com.hong.treedict.dal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hong.treedict.po.TreeDictPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  tree-dict Mapper 接口
 * </p>
 *
 * @author Hongxy
 * @since 2021-05-24
 */
public interface TreeDictMapper extends BaseMapper<TreeDictPO> {

    @Update("update tree_dict s set s.right_value = s.right_value + 2 where s.right_value > #{compareVal}")
    int updateRightIncreaseTwoWhenRightGreaterThan(@Param("compareVal") Long compareVal);

    @Update("update tree_dict s set s.left_value = s.left_value + 2 where s.left_value > #{compareVal}")
    int updateLeftIncreaseTwoWhenLeftGreaterThan(@Param("compareVal") Long compareVal);

    @Update("update tree_dict s set s.right_value = s.right_value - #{gap} where s.right_value > #{compareVal}")
    int updateRightDecreaseGapWhenRightGreaterThen(@Param("compareVal") Long compareVal, @Param("gap") Long gap);

    @Update("update tree_dict s set s.left_value = s.left_value - #{gap} where s.left_value > #{compareVal}")
    int updateLeftDecreaseGapWhenLeftGreaterThan(@Param("compareVal") Long compareVal, @Param("gap") Long gap);

}
