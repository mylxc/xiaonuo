package com.jackli.common.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TreeOperateUtil {
    public static <T,R>  void getChildrensId(T parent, List<R> lists, Function<? super R, ? extends T> getParentFunc, Function<? super R, ? extends T> getOwnFunc, List<T> listIds)
    {
        lists.forEach(item->{
            T parentId=(T)getParentFunc.apply(item);//获取当前节点的父id
            if(parentId!=null&&parentId.equals(parent))
            {
                T id=(T)getOwnFunc.apply(item);//获取当前节点的id
                listIds.add(id);//放到list里
                getChildrensId(id,lists,getParentFunc,getOwnFunc,listIds);//递归查找
            }
        });
    }

    //通过反射构造树形结构
    public static  <T> List<T> getTreeNodes(List<T> list, String idFieldName, String parentFieldName, String childFieldName,Object parentIdVal) {
        List<T> treeNodes = new ArrayList<>();
        for (T obj : list) {
            if(ObjectUtil.isNotNull(obj)&&ObjectUtil.isNotNull(idFieldName)&&ObjectUtil.isNotNull(parentFieldName)&&ObjectUtil.isNotNull(childFieldName)&&
                    ReflectUtil.hasField(obj.getClass(),idFieldName)&&ReflectUtil.hasField(obj.getClass(),parentFieldName)) {
                Object parentId = ReflectUtil.getFieldValue(obj, parentFieldName);
                Object id = ReflectUtil.getFieldValue(obj, idFieldName);
                if (ObjectUtil.equal(parentId, parentIdVal)) {
                    List<T> childrens = getTreeNodes(list, idFieldName, parentFieldName, childFieldName, id);
                    ReflectUtil.setFieldValue(obj, childFieldName, childrens);
                    treeNodes.add(obj);
                }
            }
        }
        return treeNodes;
    }
}
