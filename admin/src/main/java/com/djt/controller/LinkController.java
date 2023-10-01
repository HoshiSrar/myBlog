package com.djt.controller;

import com.djt.domain.ResponseResult;
import com.djt.domain.entity.Link;
import com.djt.domain.vo.PageVo;
import com.djt.service.LinkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Resource
    LinkService linkService;
    //查询友链列表
    @GetMapping("/list")
    public ResponseResult linkList(Link link,Integer pageNum,Integer pageSize){
        PageVo pageVo = linkService.selectLinkPage(link,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }
    //新增友链
    @PostMapping
    public ResponseResult addLink(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }
    //获取选择的友链id的信息
    @GetMapping("/{id}")
    public ResponseResult LinkInfo(@PathVariable("id") Long id){
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }
    @PutMapping
    public ResponseResult editLinkInfo(@RequestBody Link link){
        linkService.updateById(link);
    return ResponseResult.okResult();
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteLink(@PathVariable("id") Long id){
        linkService.deleteLinkById(id);
        return ResponseResult.okResult();
    }
    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }
}
