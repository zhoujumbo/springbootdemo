;(function ($) {
    /**工具方法扩展***/
    /**
     * 时间对象的格式化;
     */
    Date.prototype.format = function (format) { //给日期添加format原型
        /*
         * 使用例子:format="yyyy-MM-dd hh:mm:ss";
         */
        var o = {
            "M+": this.getMonth() + 1, // month
            "d+": this.getDate(), // day
            "h+": this.getHours(), // hour
            "m+": this.getMinutes(), // minute
            "s+": this.getSeconds(), // second
            "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
            "S": this.getMilliseconds()
            // millisecond
        };

        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        }

        for (var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;
    };

    /**
     * 获取字符串的长度
     * @param font
     */
    String.prototype.strWidth = function (font) {
        var f = font || '12px arial',
            o = $('<div>' + this + '</div>')
                .css({
                    'position': 'absolute',
                    'float': 'left',
                    'white-space': 'nowrap',
                    'visibility': 'hidden',
                    'font': f
                }).appendTo($('body')),
            w = o.width();

        o.remove();
        return w;
    };

    /***
     *Title: isHeadInclude
     *Description: 删除head里面是否引用某js文件或者css文件
     *author: zhoujumbo
     *date: 2018/10/03
     */
    $.isHeadRemove = function (name) {
        var js = /js$/i.test(name);
        var es = document.getElementsByTagName(js ? 'script' : 'link');
        for (var i = 0; i < es.length; i++) {
            if (es[i][js ? 'src' : 'href'].indexOf(name) != -1) {
                es[i].remove();
            }
        }
        return false;
    };

    /***
     * $.extend
     * Description：jquery扩展对象
     */
    $.extend({

        /*************************************************************************************
         * Title: baseUrl
         * Description：获取网页当前url的基础部分
         **************************************************************************************/
        'baseUrl': function () {
            return _urlCommon;
        },

        /*************************************************************************************
         * Title: reoladBySize
         * Description：获取网页当前url的基础部分
         **************************************************************************************/
        'reoladBySize': function (func) {
            $(window).on('resize', function () {
                // console.log(_cur_common_fontsize + '   ' + parseInt($('#common_baseFont').css('font-size')));
                if (_cur_common_fontsize != parseInt($('#common_baseFont').css('font-size'))) {
                    _cur_common_fontsize = parseInt($('#common_baseFont').css('font-size'));
                    func.call();
                }
            });
        },

        /*************************************************************************************
         *Title: getQueryStr
         *Description: 获取url传参中某个参数的值
         **************************************************************************************/
        'getQueryStr': function (str) {
            var reg = new RegExp("(^|&)" + str + "=([^&]*)(&|$)");
            var r = decodeURIComponent(window.location.search).substr(1).match(reg);
            return r != null ? unescape(r[2]) : null;
        },

        /*************************************************************************************
         *Title: getIndex
         *Description: 获取一个字符串 str 中 第 len 个字符 char 的位置
         **************************************************************************************/
        'getIndex': function (str, char, len) {
            var index = 0;
            for (var i = 0; i < len; i++) {
                index = str.indexOf(char, index + 1);
            }
            return index;
        },

        /*************************************************************************************
         * Title: myBrowser
         * Description: 获取浏览器类型并返回
         * Return： String ：Opera、FF、Chrome、Safari、iPad、iPhone、Android、IE
         **************************************************************************************/
        'myBrowser': function () {
            if (!!window.ActiveXObject || "ActiveXObject" in window) {
                return "IE";
            } else {
                var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
                if (userAgent.indexOf("Opera") > -1) { //判断是否Opera浏览器
                    return "Opera"
                }
                ;
                if (userAgent.indexOf("Firefox") > -1) { //判断是否Firefox浏览器
                    return "FF";
                }
                ;
                if (userAgent.indexOf("Chrome") > -1) { //google
                    return "Chrome";
                }
                ;
                if (userAgent.indexOf("Safari") > -1) { //判断是否Safari浏览器
                    return "Safari";
                }
                ;
                if (userAgent.indexOf("iPad") > -1) { //iPad
                    return "iPad";
                }
                ;
                if (userAgent.indexOf("iPhone") > -1) { //iPhone
                    return "iPhone";
                }
                ;
                if (userAgent.indexOf("Android") > -1) { //Android
                    return "Android";
                }
                ;
                if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) { //判断是否IE浏览器
                    return "IE";
                }
                ;
            }
        },


    });


    $.fn.extend({});

    var _urlCommon = window.location.href;
    _urlCommon = _urlCommon.substring(0, $.getIndex(_urlCommon, '/', 4) + 1);
})(jQuery);

// var _urlCommon = window.location.href;
// _urlCommon = _urlCommon.substring(0, $.getIndex(_urlCommon, '/', 4) + 1);


