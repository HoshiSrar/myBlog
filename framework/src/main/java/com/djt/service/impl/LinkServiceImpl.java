package com.djt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djt.constants.SystemConstants;
import com.djt.domain.ResponseResult;
import com.djt.domain.entity.Link;
import com.djt.domain.vo.LinkVo;
import com.djt.domain.vo.PageVo;
import com.djt.mapper.LinkMapper;
import com.djt.service.LinkService;
import com.djt.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-03-05 15:27:13
 */
@Service("LinkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的友链
        LambdaQueryWrapper<Link> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(QueryWrapper);
//        VO
        List<LinkVo> listVo = BeanCopyUtils.copyBeanList(links, LinkVo.class);
//        封装返回
        return ResponseResult.okResult(listVo);
    }

    /**
     * 分页查询友链列表，模糊查询和状态查询。
     * @param link
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(link.getName()), Link::getName,link.getName());
        queryWrapper.eq(Objects.nonNull(link.getStatus()),Link::getStatus, link.getStatus());

        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<Link> categories = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(categories);
        return pageVo;
    }

    @Override
    public void deleteLinkById(Long id) {
        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Link::getId, id);
        updateWrapper.set(Link::getDelFlag,SystemConstants.LINK_STATUS_FAIL);
        update(updateWrapper);
    }

}
