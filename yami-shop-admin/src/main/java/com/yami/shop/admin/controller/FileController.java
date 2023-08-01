/*

 */

package com.yami.shop.admin.controller;

import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.AttachFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传 controller
 * @author lgh
 *
 */
@RestController
@RequestMapping("/admin/file")
public class FileController {

	@Autowired
	private AttachFileService attachFileService;

	@PostMapping("/upload/element")
	public ServerResponseEntity<String> uploadElementFile(@RequestParam("file") MultipartFile file) throws IOException{
		if(file.isEmpty()){
            return ServerResponseEntity.success();
        }
		String fileName = attachFileService.uploadFile(file);
        return ServerResponseEntity.success(fileName);
	}

}
