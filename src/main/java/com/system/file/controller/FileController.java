package com.system.file.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.file.entity.FsFile;
import com.system.file.service.FileService;
import com.web.basic.service.AppService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * 文件管理
 * @author fyx
 *
 */
@Api(description = "Qms文件管理")
@RestController
@RequestMapping(value="/file")
public class FileController extends WebController {
	@Autowired
	private FileService fileService;
	
	@Autowired
	 private AppService appService;

	@ApiOperation(value="上传文件", notes="上传文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "file", value = "附件", dataType = "MultipartFile", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ApiResponseResult upload(MultipartFile file) {
		try {
			FsFile fsFile = new FsFile();
			return fileService.upload(fsFile, file);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ApiResponseResult.failure(e.getMessage());
		}
	}


	@ApiOperation(value="业务上传文件", notes="业务上传文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "file", value = "附件", dataType = "MultipartFile", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/uploadByBs", method = RequestMethod.POST)
	public ApiResponseResult uploadByBs(MultipartFile file,Long bsId,String bsType) {
		try {
			FsFile fsFile = new FsFile();
			return fileService.uploadByBs(fsFile, file,bsId,bsType);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ApiResponseResult.failure(e.getMessage());
		}
	}

	@ApiOperation(value="业务文件列表", notes="业务文件列表")
	@RequestMapping(value = "/getListByBs", method = RequestMethod.GET)
	public ApiResponseResult getListByBs(Long bsId,String bsType) {
		try {
			return fileService.getListByBs(bsId, bsType,super.getPageRequest(Sort.unsorted()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ApiResponseResult.failure(e.getMessage());
		}
	}

	@ApiOperation(value = "删除", notes = "删除",hidden = true)
	@RequestMapping(value = "/deleteBsFile", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult delete(@RequestBody Map<String, Object> params){
		try{
			String ids = params.get("id").toString() ;
			ApiResponseResult result = fileService.deleteBsFile(ids);
			return result;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("删除失败！", e);
			return ApiResponseResult.failure("删除失败！");
		}
	}


	@ApiOperation(value="上传图片", notes="上传图片")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "file", value = "图片", dataType = "MultipartFile", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
	public ApiResponseResult uploadImg(MultipartFile file) {
		try {
			FsFile fsFile = new FsFile();
			return fileService.upload(fsFile, file);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ApiResponseResult.failure(e.getMessage());
		}
	}

	@ApiOperation(value="下载文件", notes="下载文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fsFileId", value = "文件ID", required = true, dataType = "Long", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public void get(@RequestParam(value = "fsFileId", required = true) Long fsFileId) {
		try {
			fileService.get(fsFileId, getResponse());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@ApiOperation(value="图片在线预览", notes="图片在线预览")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fsFileId", value = "文件ID", required = true, dataType = "Long", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public void view(@RequestParam(value = "fsFileId", required = true) Long fsFileId) {
		try {
			fileService.onlineView(fsFileId, getResponse());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@ApiOperation(value="图片在线预览", notes="图片在线预览")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fsFileId", value = "文件ID", required = true, dataType = "Long", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/viewByUrl", method = RequestMethod.GET)
	public void viewByUrl(@RequestParam(value = "url", required = true) String url) {
		try {
			if(!StringUtils.isEmpty(url)){
				String [] sz=url.split("/");
				String fileName = sz[sz.length-1];
				fileService.viewByUrl("/url", fileName, getResponse());
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

    @ApiOperation(value="删除文件", notes="删除文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fsFileId", value = "文件ID", required = true, dataType = "Long", paramType="query",defaultValue=""),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ApiResponseResult delete(@RequestParam(value = "fsFileId", required = true) Long fsFileId){
	    try{
	        return fileService.delete(fsFileId);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ApiResponseResult.failure(e.getMessage());
        }
    }
    
    @ApiOperation(value="上传文件", notes="上传文件")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "file", value = "附件", dataType = "MultipartFile", paramType="query",defaultValue=""),
	})
	@RequestMapping(value = "/uploadAppFile", method = RequestMethod.POST)
	public ApiResponseResult uploadAppFile(MultipartFile file) {
		try {
			return appService.upload(file);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ApiResponseResult.failure(e.getMessage());
		}
	}
}
