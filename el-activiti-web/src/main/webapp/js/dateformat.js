/**
 * Created by xvshu on 2017/11/22.
 */
/**
 * date('Y-m-d','1350052653');//很方便的将时间戳转换成了2012-10-11
 * date('Y-m-d H:i:s','1350052653');//得到的结果是2012-10-12 22:37:33
 * 和PHP一样的时间戳格式化函数
 * @param  {string} format    格式
 * @param  {int}    timestamp 要格式化的时间 默认为当前时间
 * @return {string}           格式化的时间字符串
 */
function dateUnixFormat(time) {
    var unixtime = time
    var unixTimestamp = new Date(unixtime * 1000)
    var Y = unixTimestamp.getFullYear()
    var M = ((unixTimestamp.getMonth() + 1) > 10 ? (unixTimestamp.getMonth() + 1) : '0' + (unixTimestamp.getMonth() + 1))
    var D = (unixTimestamp.getDate() > 10 ? unixTimestamp.getDate() : '0' + unixTimestamp.getDate())
    var toDay = Y + '-' + M + '-' + D
    return toDay
}
