package cn.onestravel.kotlin.demo;

import cn.onestravel.library.kotlin.rxrequest.loader.OneLoader;
import cn.onestravel.library.kotlin.rxrequest.service.OneService;
import cn.onestravel.library.kotlin.rxrequest.service.RetrofitServiceManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author onestravel
 * @createTime 2019-08-03 10:47
 * @description TODO
 */
public abstract class BaseLoader<T extends OneService> extends OneLoader<T> {
    @NotNull
    @Override
    public RetrofitServiceManager createServiceManager() {
        return BaseServiceManager.INSTANCE;
    }




}
