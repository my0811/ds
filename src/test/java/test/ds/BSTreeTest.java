package test.ds;

import com.yang.ds.datastruct.tree.bstree.BSTree;
import com.yang.ds.datastruct.tree.bstree.impl.BSTreeImpl;
import com.yang.ds.datastruct.tree.bstree.impl.RBTreeImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * 二叉树测试
 * */
public class BSTreeTest {

    /**二叉树*/
    BSTree<Integer, String> bsTree;
    /**红黑树*/
    BSTree<Integer, String> rbTree;


    @Before
    public void init() {
        bsTree = new BSTreeImpl();
        rbTree = new RBTreeImpl();
    }

    /**
     * 二叉树测试
     * */

    @Test
    public void bsTreeTest() {
        bsTree.add(50, "50V");
        bsTree.add(25, "25V");
        bsTree.add(75, "75V");
        bsTree.add(15, "15V");
        bsTree.add(30, "30V");
        bsTree.add(60, "60V");
        bsTree.add(80, "80V");
        bsTree.add(10, "10V");
        bsTree.add(20, "20V");
        bsTree.add(28, "28V");
        bsTree.add(35, "35V");
        bsTree.add(55, "55V");
        bsTree.add(65, "65V");
        bsTree.add(78, "78V");
        bsTree.add(85, "85VV");
        System.out.println(bsTree.toString());
        bsTree.delete(10);
        System.out.println("delete 10");
        System.out.println(bsTree);
        bsTree.delete(25);
        System.out.println("delete 25");
        System.out.println(bsTree);
        bsTree.delete(15);
        System.out.println("delete 15");
        System.out.println(bsTree);
        System.out.println("get 55:" + bsTree.get(55));
        System.out.println("getMin:" + bsTree.getMin());
        System.out.println("getMax:" + bsTree.getMax());
        System.out.println("中序遍历:" + Arrays.toString(bsTree.midErgodic().toArray()));
        System.out.println("前序遍历:" + Arrays.toString(bsTree.preErgodic().toArray()));
        System.out.println("后续遍历:" + Arrays.toString(bsTree.afterErgodic().toArray()));
    }

    @Test
    public void rbTreeTest() {
        // 50,25,10,80,85,90,87,45,48,40,86,43,88
        rbTree.add(50, "50");
        rbTree.add(25, "25");
        rbTree.add(10, "10");
        rbTree.add(80, "80");
        rbTree.add(85, "85");
        rbTree.add(90, "90");
        rbTree.add(87, "87");
        rbTree.add(45, "45");
        rbTree.add(48, "48");
        rbTree.add(40, "40");
        rbTree.add(86, "86");
        rbTree.add(43, "43");
        rbTree.add(88, "88");
        System.out.println(rbTree.toString());
        // 叶子------------case1/case3-------------------
        System.out.println("叶子删除 case1 删除45");
        rbTree.delete(45);
        System.out.println(rbTree);

        System.out.println("叶子删除 case3 删除10");
        rbTree.delete(10);
        System.out.println(rbTree);

        //一个子树case2-------------------------------
        System.out.println("存在一个孩子 case2 删除85");
        rbTree.delete(85);
        System.out.println(rbTree);

        // 两个子树，出发平衡修复case1,case2,case3
        System.out.println("删除48,触发平衡修复逻辑 case1,case2,case3");
        rbTree.delete(48);
        System.out.println(rbTree);
        System.out.println("删除48");

        System.out.println("删除25,触发平衡修复逻辑 case5");
        rbTree.delete(25);
        System.out.println(rbTree);

        System.out.println("删除86，88，80,触发平衡修复逻辑 case4");
        rbTree.delete(86);
        rbTree.delete(88);
        rbTree.delete(80);
        System.out.println(rbTree);

        System.out.println("删除86，88，80,触发平衡修复逻辑 case4");

        System.out.println("感谢所有TV所有case走完.......");

    }
}
