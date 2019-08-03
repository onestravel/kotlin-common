package cn.onestravel.kotlin.demo.model

import java.io.Serializable

/**
 * @name cn.onestravel.kotlin.base.mvp.user
 * @description //TODO
 * @createTime 2018/11/30 14:49
 * @author onestravel
 * @version 1.0.0
 */

class User:Serializable{
    var name:String =""
    var headUrl:String =""
    var age:Int =0
    var sex:String ="男"
}