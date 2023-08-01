/*

 */

package com.yami.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.common.util.ImgUploadUtil;
import com.yami.shop.dao.AttachFileMapper;
import com.yami.shop.service.AttachFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * @author lanhai
 */
@Service
public class AttachFileServiceImpl extends ServiceImpl<AttachFileMapper, AttachFile> implements AttachFileService {

    @Autowired
    private AttachFileMapper attachFileMapper;
	@Autowired
	private ImgUploadUtil imgUploadUtil;
    public final static String NORM_MONTH_PATTERN = "yyyy/MM/";

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String uploadFile(MultipartFile file) throws IOException {
		String extName = FileUtil.extName(file.getOriginalFilename());
		String fileName =DateUtil.format(new Date(), NORM_MONTH_PATTERN)+ IdUtil.simpleUUID() + "." + extName;
		AttachFile attachFile = new AttachFile();
		attachFile.setFilePath(fileName);
		attachFile.setFileSize(file.getBytes().length);
		attachFile.setFileType(extName);
		attachFile.setUploadTime(new Date());
		if (Objects.equals(imgUploadUtil.getUploadType(), 1)) {
			// 本地文件上传
			attachFileMapper.insert(attachFile);
			return imgUploadUtil.upload(file, fileName);
		}
		return null;
	}

	@Override
	public void deleteFile(String fileName){
		attachFileMapper.delete(new LambdaQueryWrapper<AttachFile>().eq(AttachFile::getFilePath,fileName));
		if (Objects.equals(imgUploadUtil.getUploadType(), 1)) {
			imgUploadUtil.delete(fileName);
		}
	}
}
