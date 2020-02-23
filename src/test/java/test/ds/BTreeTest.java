package test.ds;

import com.yang.ds.datastruct.tree.btree.BPlusTree;
import com.yang.ds.datastruct.tree.btree.BTree;
import com.yang.ds.datastruct.tree.btree.impl.BPlusTreeImpl;
import com.yang.ds.datastruct.tree.btree.impl.BTreeImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * B树测试类
 * */
public class BTreeTest {

    /**b+tree*/
    BPlusTree<Integer, String> bPlusTree;

    /**b-tree*/
    BTree<Integer, String> bTree;

    @Before
    public void init() {
        bPlusTree = new BPlusTreeImpl<>(3);
        bTree = new BTreeImpl<>(3);
    }
    /**
     * b-tree测试
     *
     * */
    @Test
    public void bTreeTest() {
        System.out.println("叶子、非叶子、根分裂-->数据初始化");
        bTree.insert(1, "1v");
        bTree.insert(3, "3v");
        bTree.insert(5, "5v");
        bTree.insert(7, "7v");
        bTree.insert(9, "9v");
        bTree.insert(11, "11v");
        bTree.insert(13, "13v");
        bTree.insert(15, "15v");
        bTree.insert(17, "17v");
        bTree.insert(14, "14v");
        bTree.insert(4, "4v");
        System.out.println(bTree.treeToString());

        // 删除叶子右旋
        System.out.println("叶子右旋-->叶子删除17");
        bTree.delete(17);
        System.out.println(bTree.treeToString());

        // 叶子左旋
        System.out.println("叶子左旋-->叶子删除1");
        bTree.delete(1);
        System.out.println(bTree.treeToString());

        // 叶子合并，非叶子左旋
        System.out.println("叶子合并(无左兄弟,（自己作为左兄弟))/非叶子左旋-->叶子删除3");
        bTree.delete(3);
        System.out.println(bTree.treeToString());

        // 插入
        System.out.println("插入3");
        bTree.insert(3, "3v");
        System.out.println(bTree.treeToString());

        // 合并/非叶子右旋
        System.out.println("叶子合并(有左兄弟)/非叶子右旋-->叶子删除15");
        bTree.delete(15);
        System.out.println(bTree.treeToString());

        // 删除非叶子节点，后继key替换，更新根,与app可能效果不一样，取决于，找后继节点时候，如果相等从值域右侧找还是左侧找
        System.out.println("非叶子合并/更新根-->非叶子删除4");
        bTree.delete(4);
        System.out.println(bTree.treeToString());

        // 查找
        System.out.println("查找15: " + bTree.search(15));
        System.out.println("查找11: " + bTree.search(11));

    }

    /**
     * b+tree 测试
     * 与B-tree基本一致，
     * */
    @Test
    public void bPlusTreeTest() {
        bPlusTree.insert(1, "1v");
        bPlusTree.insert(3, "3v");
        bPlusTree.insert(5, "5v");
        bPlusTree.insert(7, "7v");
        bPlusTree.insert(9, "9v");
        bPlusTree.insert(11, "11v");
        bPlusTree.insert(13, "13v");
        bPlusTree.insert(15, "15v");
        bPlusTree.insert(17, "17v");
        bPlusTree.insert(19, "19v");
        bPlusTree.insert(21, "21v");
        bPlusTree.insert(23, "23v");
        bPlusTree.insert(25, "25v");
        System.out.println(bPlusTree.treeToString());
        bPlusTree.delete(17);
        System.out.println(bPlusTree.treeToString());
        List<String> rangeList = bPlusTree.searchRange(1, BPlusTree.RangePolicy.INCLUSIVE, 5, BPlusTree.RangePolicy.INCLUSIVE);
        System.out.println(Arrays.toString(rangeList.toArray(new String[]{})));
    }
}
