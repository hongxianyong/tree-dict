package com.hong.treedict;

import com.hong.treedict.po.TreeDictPO;
import com.hong.treedict.service.TreeDictService;
import com.hong.treedict.service.bo.TreeDictOperation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class TreeDictApplicationTests {

    @Resource
    TreeDictService treeDictService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateRoot() {
        TreeDictOperation root = new TreeDictOperation();
        root.setCode("XHD");
        root.setDescription("夏侯惇");
        root.setNameSpace("Honor of Kings");
        treeDictService.createRoot(root);
    }

    @Test
    public void testInsertPH() {
        TreeDictOperation head = new TreeDictOperation();
        head.setCode("LB");
        head.setDescription("刘备");
        head.setParentId("1396834145414234113");
        head.setNameSpace("Honor of Kings");
        treeDictService.insertPH(head);
    }

    @Test
    public void testInsertPT() {
        TreeDictOperation tail = new TreeDictOperation();
        tail.setCode("LS");
        tail.setDescription("刘禅");
        tail.setParentId("1396834406459355138");
        tail.setNameSpace("Honor of Kings");
        treeDictService.insertPT(tail);
    }

    @Test
    public void testInsertPL() {
        TreeDictOperation pl = new TreeDictOperation();
        pl.setCode("SSX");
        pl.setDescription("孙尚香");
        pl.setParentId("1396834145414234113");
        pl.setLeftCollateralId("1396834406459355138");
        pl.setNameSpace("Honor of Kings");
        treeDictService.insertPL(pl);
    }

    @Test
    public void testInsertPR() {
        TreeDictOperation pr = new TreeDictOperation();
        pr.setCode("ZY");
        pr.setDescription("赵云");
        pr.setParentId("1396834406459355138");
        pr.setRightCollateralId("1396834608872263682");
        pr.setNameSpace("Honor of Kings");
        treeDictService.insertPR(pr);
    }

    @Test
    public void testInsertR() {
        TreeDictOperation r = new TreeDictOperation();
        r.setCode("CC");
        r.setDescription("曹操");
        r.setRightCollateralId("1396834406459355138");
        r.setNameSpace("Honor of Kings");
        treeDictService.insertR(r);
    }

    @Test
    public void testInsertL() {
        TreeDictOperation l = new TreeDictOperation();
        l.setCode("SQ");
        l.setDescription("孙权");
        l.setRightCollateralId("1396835784661504001");
        l.setNameSpace("Honor of Kings");
        treeDictService.insertL(l);
    }

    @Test
    public void testFindByCode() {
        TreeDictPO cc = treeDictService.findByCode("CC", "Honor of Kings");
        System.out.println(cc);
    }

    @Test
    public void testFindById() {
        TreeDictPO cc = treeDictService.findById("1396835784661504001");
        System.out.println(cc);
    }

    @Test
    public void testFindChildrenByCode() {
        List<TreeDictPO> children = treeDictService.findChildren("XHD", "Honor of Kings");
        children.forEach(System.out::println);
    }

    @Test
    public void testFindChildrenById() {
        List<TreeDictPO> children = treeDictService.findChildren("1396834406459355138");
        children.forEach(System.out::println);
    }


    //    @Test
    public void testDeleteByCode() {
        treeDictService.deleteByCode("XHD", "Honor of Kings");
//        treeDictService.deleteByCode("LS", "Honor of Kings");
    }

}
