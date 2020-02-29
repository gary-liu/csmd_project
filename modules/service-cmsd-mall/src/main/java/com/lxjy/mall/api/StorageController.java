package com.lxjy.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.lxjy.mall.entity.CsmdStorage;
import com.lxjy.mall.service.CsmdStorageService;
import com.lxjy.mall.storage.StorageService;
import com.lxjy.mall.util.CharUtil;
import com.lxjy.mall.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * create by gary 2020/2/27
 * 技术交流请加QQ:498982703
 * 对象存储服务
 */
@RestController
@Validated
@RequestMapping("/app/storage")
@Slf4j
public class StorageController {


    @Autowired
    private StorageService storageService;

    @Autowired
    private CsmdStorageService csmdStorageService;

    @SuppressWarnings("unused")
    private String generateKey(String originalFilename) {
        int index = originalFilename.lastIndexOf('.');
        String suffix = originalFilename.substring(index);

        String key = null;
        CsmdStorage storageInfo = null;

        do {
            key = CharUtil.getRandomString(20) + suffix;
            storageInfo = csmdStorageService.getBykey(key);

        } while (storageInfo != null);

        return key;
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public CommonResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("【请求开始】上传文件,请求参数,file:{}", file.getOriginalFilename());

        String originalFilename = file.getOriginalFilename();
        String url = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(),
                originalFilename);

        Map<String, Object> data = new HashMap<>();
        data.put("url", url);

        log.info("【请求结束】上传文件,响应结果:{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }

    /**
     * 访问存储对象
     *
     * @param key
     *            存储对象key
     * @return
     */
    @GetMapping("/fetch/{key:.+}")
    public ResponseEntity<Resource> fetch(@PathVariable String key) {
        // log.info("【请求开始】访问存储对象,请求参数,key:{}", key);

        CsmdStorage csmdStorage = csmdStorageService.getBykey(key);
        if (key == null) {
            return ResponseEntity.notFound().build();
        }
        if (key.contains("../")) {
            return ResponseEntity.badRequest().build();
        }
        String type = csmdStorage.getType();
        MediaType mediaType = MediaType.parseMediaType(type);

        Resource file = storageService.loadAsResource(key);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        // log.info("【请求结束】访问存储对象,响应结果:{}","成功");
        return ResponseEntity.ok().contentType(mediaType).body(file);
    }

    /**
     * 访问存储对象
     *
     * @param key
     *            存储对象key
     * @return
     */
    @GetMapping("/download/{key:.+}")
    public ResponseEntity<Resource> download(@PathVariable String key) {
        // log.info("【请求开始】访问存储对象,请求参数,key:{}", key);
        CsmdStorage CsmdStorage = csmdStorageService.getBykey(key);
        if (key == null) {
            return ResponseEntity.notFound().build();
        }
        if (key.contains("../")) {
            return ResponseEntity.badRequest().build();
        }

        String type = CsmdStorage.getType();
        MediaType mediaType = MediaType.parseMediaType(type);

        Resource file = storageService.loadAsResource(key);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        // log.info("【请求结束】访问存储对象,响应结果:{}","成功");
        return ResponseEntity.ok().contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


}
