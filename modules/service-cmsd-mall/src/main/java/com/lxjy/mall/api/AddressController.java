package com.lxjy.mall.api;

import com.lxjy.mall.entity.CsmdAddress;
import com.lxjy.mall.entity.CsmdRegion;
import com.lxjy.mall.service.CsmdAddressService;
import com.lxjy.mall.service.CsmdRegionService;
import com.lxjy.mall.util.CommonResult;
import com.lxjy.mall.util.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * create by gary 2020/2/27
 * 技术交流请加QQ:498982703
 */
@RestController
@RequestMapping("/app/address")
@Validated
@Slf4j
public class AddressController {

    @Autowired
    private CsmdAddressService addressService;

    @Autowired
    private CsmdRegionService regionService;

    private final static ArrayBlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(6);

    private final static RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

    private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(3, 6, 1000, TimeUnit.MILLISECONDS,
            WORK_QUEUE, HANDLER);

    /**
     * 用户收获地址列表
     * @param userId  用户ID
     * @return  收货地址列表
     */
    @GetMapping("list")
    public CommonResult list(Integer userId) {
        log.info("【请求开始】获取收货地址列表,请求参数,userId：{}", userId);
        if (userId == null) {
            return CommonResult.unlogin();
        }

        List<CsmdAddress> addressList = addressService.queryByUserId(userId);
        List<Map<String, Object>> addressVoList = new ArrayList<>(addressList.size());
        List<CsmdRegion> regionList = regionService.getDtsRegions();
        for (CsmdAddress address : addressList) {
            Map<String, Object> addressVo = new HashMap<>();
            addressVo.put("id", address.getId());
            addressVo.put("name", address.getName());
            addressVo.put("mobile", address.getMobile());
            addressVo.put("isDefault", address.getIsDefault());
            Callable<String> provinceCallable = () -> regionList.stream()
                    .filter(region -> region.getId().equals(address.getProvinceId())).findAny().orElse(null).getName();
            Callable<String> cityCallable = () -> regionList.stream()
                    .filter(region -> region.getId().equals(address.getCityId())).findAny().orElse(null).getName();
            Callable<String> areaCallable = () -> regionList.stream()
                    .filter(region -> region.getId().equals(address.getAreaId())).findAny().orElse(null).getName();
            FutureTask<String> provinceNameCallableTask = new FutureTask<>(provinceCallable);
            FutureTask<String> cityNameCallableTask = new FutureTask<>(cityCallable);
            FutureTask<String> areaNameCallableTask = new FutureTask<>(areaCallable);
            executorService.submit(provinceNameCallableTask);
            executorService.submit(cityNameCallableTask);
            executorService.submit(areaNameCallableTask);
            String detailedAddress = "";
            try {
                String province = provinceNameCallableTask.get();
                String city = cityNameCallableTask.get();
                String area = areaNameCallableTask.get();
                String addr = address.getAddress();
                detailedAddress = province + city + area + " " + addr;
            } catch (Exception e) {
                log.error("【行政区域获取出错】获取收货地址列表错误！关键参数：{}", userId);
                e.printStackTrace();
            }
            addressVo.put("detailedAddress", detailedAddress);

            addressVoList.add(addressVo);
        }
        return CommonResult.success(addressVoList);



    }

    /**
     * 收货地址详情
     *
     * @param userId
     *            用户ID
     * @param id
     *            收货地址ID
     * @return 收货地址详情
     */
    @GetMapping("detail")
    public CommonResult detail( Integer userId, @NotNull Integer id) {
        if (userId == null) {
            return CommonResult.unlogin();
        }

        CsmdAddress address = addressService.getById(id);
        if (address == null) {
            return CommonResult.badArgumentValue();
        }

        Map<Object, Object> data = new HashMap<Object, Object>();
        data.put("id", address.getId());
        data.put("name", address.getName());
        data.put("provinceId", address.getProvinceId());
        data.put("cityId", address.getCityId());
        data.put("areaId", address.getAreaId());
        data.put("mobile", address.getMobile());
        data.put("address", address.getAddress());
        data.put("isDefault", address.getIsDefault());
        String pname = regionService.getById(address.getProvinceId()).getName();
        data.put("provinceName", pname);
        String cname = regionService.getById(address.getCityId()).getName();
        data.put("cityName", cname);
        String dname = regionService.getById(address.getAreaId()).getName();
        data.put("areaName", dname);

        return CommonResult.success(data);

    }

    private CommonResult validate(CsmdAddress address) {
        if (address == null) {
            return CommonResult.badArgument();
        }
        String name = address.getName();
        if (StringUtils.isEmpty(name)) {
            return CommonResult.badArgumentValue();
        }
        String mobile = address.getMobile();
        if(StringUtils.isEmpty(mobile))
            return CommonResult.badArgumentValue();
        if (!RegexUtil.isMobileExact(mobile)) {
            return CommonResult.badArgumentValue();
        }


        Integer pid = address.getProvinceId();
        if (pid == null) {
            return CommonResult.badArgument();
        }
        if (regionService.getById(pid) == null) {
            return CommonResult.badArgumentValue();
        }

        Integer cid = address.getCityId();
        if (cid == null) {
            return CommonResult.badArgument();
        }

        if (regionService.getById(cid) == null) {
            return CommonResult.badArgumentValue();
        }

        Integer aid = address.getAreaId();
        if (aid == null) {
            return CommonResult.badArgument();
        }
        if (regionService.getById(aid) == null) {
            return CommonResult.badArgumentValue();
        }

        String detailedAddress = address.getAddress();
        if (StringUtils.isEmpty(detailedAddress)) {
            return CommonResult.badArgument();
        }

        Boolean isDefault = address.getIsDefault();
        if (isDefault == null) {
            return CommonResult.badArgument();
        }
        return null;
    }

    /**
     * 添加或更新收货地址
     *
     * @param address
     *            用户收货地址
     * @return 添加或更新操作结果
     */
    @PostMapping("save")
    public CommonResult save( Integer userId, @RequestBody CsmdAddress address) {
        CommonResult result = validate(address);
        if (result != null) {
            return result;
        }
        if (address.getIsDefault()) {
            addressService.resetDefault(userId);
        }
        address.setUserId(userId);
        if (address.insertOrUpdate()) {
            result = CommonResult.success();
        }
        result = CommonResult.error();

        return result;

    }


    /**
     * 添加或更新收货地址

     * @param address
     *            用户收货地址
     * @return 添加或更新操作结果
     */
    @PostMapping("delete")
    public CommonResult delete( @RequestBody CsmdAddress address) {
        if (address == null) {
            return CommonResult.badArgumentValue();
        }
        address.deleteById();
        return CommonResult.success();
    }








}
