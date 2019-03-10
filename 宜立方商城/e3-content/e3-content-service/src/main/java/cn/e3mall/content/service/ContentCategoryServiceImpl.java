package cn.e3mall.content.service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCatList(long parentId) {
        //根据parentId查询子节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> catList =  contentCategoryMapper.selectByExample(example);
        //转换成EasyTreeNode
        List<EasyUITreeNode> nodes = new ArrayList<>();
        for (TbContentCategory category:catList){
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(category.getId());
            node.setText(category.getName());
            node.setState(category.getIsParent()?"closed":"open");
            nodes.add(node);
        }

        return nodes;
    }

    @Override
    public E3Result addContentCategory(long parentId, String name) {
        //创建一个tb——content——category表对应的pojo对象
        TbContentCategory category = new TbContentCategory();
        //设置pojo属性
        category.setParentId(parentId);
        category.setName(name);
        //1.正常 ，2.删除
        category.setStatus(1);
        //默认为1
        category.setSortOrder(1);
        category.setCreated(new Date());
        category.setUpdated(new Date());
        //新添加的节点一定是叶子节点
        category.setIsParent(false);
        //插入到数据库
        contentCategoryMapper.insert(category);
        //判断父节点的isparent属性，如果不是true改为true
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            //更新到数据库中
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果
        return E3Result.ok(category);
    }

    /**
     * 删除一个字节点
     * @param id
     */
    @Override
    public void deleteContentCategory(long id) {
        TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);
        long parentId = category.getParentId();
        contentCategoryMapper.deleteByPrimaryKey(id);
        //判断他的父节点还有没有子节点
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(parentId);
        int count = contentCategoryMapper.countByExample(example);
        if(count == 0){
            TbContentCategory parent = new TbContentCategory();
            parent.setId(parentId);
            parent.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKeySelective(parent);
        }
    }

    /**
     * 重命名
     * @param id
     * @param name
     */
    @Override
    public void updateContentCategory(long id, String name) {
        TbContentCategory category = new TbContentCategory();
        category.setId(id);
        category.setName(name);
        category.setUpdated(new Date());
        contentCategoryMapper.updateByPrimaryKeySelective(category);
    }
}
