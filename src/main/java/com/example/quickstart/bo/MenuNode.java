package com.example.quickstart.bo;

import com.example.quickstart.entity.SystemPermission;

import java.util.List;

/**
 * 菜单节点类
 *
 * @author zxy
 */
public class MenuNode {


    /**
     * 节点图标
     */
    private String icon;

    /**
     * 节点标题
     */
    private String title;

    /**
     * 节点唯一索引值
     */
    private Integer id;

    /**
     * 子节点
     */
    private List<MenuNode> children;

    /**
     * 节点是否初始展开
     */
    private boolean spread;

    /**
     * 点击节点弹出新窗口对应的 url
     */
    private String href;

    /**
     * 节点是否初始为选中状态
     */
    private boolean checked;

    /**
     * 节点是否为禁用状态
     */
    private boolean disabled;
    private String field;

    private boolean parent;
    private boolean show;

    //关于layui 第三方插件的 treeSelect 数据格式
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点是否为打开状态
     */
    private boolean open;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MenuNode> getChildren() {
        return children;
    }

    public void setChildren(List<MenuNode> children) {
        this.children = children;
    }

    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public MenuNode(SystemPermission permission) {
        this.id = permission.getPermissionId();
        this.title = permission.getPermissionName();
        this.spread = true;
        this.field = permission.getPermissionName();
        this.parent = permission.getParentno() == null;
        this.show = permission.isExhibition();
        this.href = permission.getUrl();
        this.name = permission.getPermissionName();
        this.open = false;
    }

}
