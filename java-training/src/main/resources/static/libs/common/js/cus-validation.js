/*************************************************************************************
 *Title: tooltip  cTooltip
 *Description: 鼠标移入提示框
 *params:zIndex:设置z-index值，isLeft：是否左边显示
 *author: zhoujumbo
 *date: 2018/8/22
 *PS： 此部分代码为工具控件代码  与业务实现逻辑无关  请自动忽略
 **************************************************************************************/
;(function($){
    'use strict';
    var Tooltip = function(el,opts){
        this.element = el;
        this.sTitle = '';
        this.opts = opts?opts:{};
        this.oTooltip;
        this.bShow=false;

    };
    Tooltip.prototype.init = function(){
        var _this = this,
            _el = this.element,
            _opt = this.opts;

        if(_el.attr('title')){
            _this.sTitle = _el.attr('title');
            _el.attr('title', '');
        }
        if(_opt.title){
            _this.sTitle = _opt.title;
        }
        _el.off('mouseover.zh.tooltip mouseout.zh.tooltip mousemove.zh.tooltip');
        _el.on({
            'mouseover.zh.tooltip': function(e) {
                var iTop = e.pageY + 20;
                var iLeft = e.pageX + 20;

                if(!_this.oTooltip){
                    _this.oTooltip = $('<div class="common_tooltip" ><div></div></div>').appendTo('body');
                }
                if(_opt.zIndex){
                    _this.oTooltip.css("z-index", _opt.zIndex);
                }
                _this.oTooltip.children().empty().html(_this.sTitle);
                _this.oTooltip.show();
                if (_this.oTooltip.outerWidth() + iLeft >= $('body').width()) {
                    iLeft = $('body').width() - _this.oTooltip.outerWidth() - 50;
                }
                if (_this.oTooltip.outerHeight() + iTop >= $('body').height()) {
                    iTop = $('body').height() - _this.oTooltip.outerHeight() - 50;
                }
                _this.oTooltip.hide();
                _this.bShow = true;
                _this.oTooltip.siblings('.common_tooltip').slideUp('fast'); // 隐藏其他tooltip
                setTimeout(function() {
                    if(_this.bShow){
                        _this.oTooltip.css({
                            left: iLeft,
                            top: iTop,
                        });
                        _this.oTooltip.slideDown('fast');
                    }
                }, 500);
            },
            'mouseout.zh.tooltip': function() {
                _this.bShow = false;
                if (_this.oTooltip) {
                    _this.oTooltip.slideUp('fast');
                }
            },
            'mousemove.zh.tooltip': function(e) {
                var iTop = e.pageY + 20;
                var iLeft = e.pageX + 20;

                if (_opt.isLeft) {
                    iLeft = iLeft - _this.oTooltip.outerWidth() - 50;
                }

                if (_this.oTooltip && _this.oTooltip.outerWidth() + iLeft >= $('body').width()) {
                    iLeft = $('body').width() - _this.oTooltip.outerWidth() - 50;
                }
                if (_this.oTooltip && _this.oTooltip.outerHeight() + iTop >= $('body').height()) {
                    iTop = $('body').height() - _this.oTooltip.outerHeight() - 50;
                }

                if (_this.oTooltip) {
                    _this.oTooltip.css({
                        left: iLeft,
                        top: iTop,
                    });
                }

            },
        });
    };

    function Plugin(option,methdOpt){
        var data =  $(this).data('zh.tooltip');
        if(typeof option == 'string') {
            return data[option].call(data,methdOpt);
        }
        return this.each(function(){
            var _this = $(this);
            var data = _this.data('zh.tooltip');
            var options = typeof option == 'object' && option;
            if(!data){
                _this.data('zh.tooltip',(data = new Tooltip(_this,options)));
                data.init();
            } else {
                data.opts = options?options:{};
                data.init();
            }
        });
    }
    var old = $.fn.ctooltip;
    $.fn.ctooltip = Plugin;
    $.fn.ctooltip.Constructor = Tooltip;

    //解决冲突
    $.fn.ctooltip.noConflect = function(){
        $.fn.ctooltip = old;
        return this;
    };
})(jQuery);
/******************************************************************
 *Title: validation
 *Description: 页面校验
 *params:
 *author: zhoujumbo
 *date: 2018/10/22
 ******************************************************************/
;(function($,window){

    var Validation = function(){

        this.option = {
            el: 'body',
            rules: {
                // 校验文本框  非空必填
                'vali-required': {
                    'trigger': 'blur',  // 支持  blur change input click  触发方式
                    'tooltip': false,   // 提示方式  tooltip  或者 边框红色
                    'tip':'该字段必填！',
                    'warn':'该字段不能为空！',
                    'isRegx': true,
                    // 'regx':/^[\\s\\S]*.*[^\\s][\\s\\S]*$/,  // 正则
                    'regx':/\S/,  // 正则
                    'val': null,
                    'AllowEmpty':false, // 允许为空
                },
                // 校验日期格式
                'vali-date-ymd': {
                    'trigger': 'blur',  // 支持  blur change input click  触发方式
                    'tooltip': false,   // 提示方式  tooltip  或者 边框红色
                    'tip':'该字段必填！',
                    'warn':'日期为空或格式异常！',
                    'isRegx': true,
                    'regx':/^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/,  // 正则
                    'val': null,
                    'AllowEmpty':false, // 允许为空
                },
                // 校验电话  允许为空
                'vali-phone-req': {
                    'trigger': 'blur',  // 支持  blur change input click  触发方式
                    'tooltip': false,   // 提示方式  tooltip  或者 边框红色
                    'tip':'允许为空<br>填写则至少为7为以上数字！',
                    'warn':'电话号码格式不正确！',
                    'isRegx': true,
                    // 'regx':/^$|^(\d+|\-){7,}$/,  // 正则
                    'regx':/^$|^(\d+|\-){7,}$/,  // 正则
                    'val': null,
                    'AllowEmpty':true, // 允许为空
                },
                // 校验邮箱  允许为空
                'vali-email-req': {
                    'trigger': 'blur',  // 支持  blur change input click  触发方式
                    'tooltip': false,   // 提示方式  tooltip  或者 边框红色
                    'tip':'请填写正确的邮箱<br> *@*.*！',
                    'warn':'邮箱格式不正确！',
                    'isRegx': true,
                    'regx':/^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/,  // 正则
                    'val': null,
                    'AllowEmpty':true, // 允许为空
                },

            },

        }
    }
    /**
     * 插件版本号
     * @type {string}
     */
    Validation.prototype.version = "V 0.0.2";
    /**
     * 加载
     */
    Validation.prototype.init = function(params){
        this.defConstSet();// 初始化常量值
        this.cache(params);
        this.createDom();
        this.sheetStyle();
        this.bindEvent();

    };
    /**
     * 定义页面常量
     */
    Validation.prototype.CONST = {};
    Validation.prototype.defConstSet = function(){
        /**
         * 常量定义
         * freeze() 兼容性
         * 局限：  Feature	     Firefox (Gecko)	Chrome	Internet Explorer	Opera	Safari
         Basic support	 4.0 (2)	        6	    9	                12	    5.1
         * @type {{}}
         * @param
         */
        this.CONST.ICO_MARGINLIFE = 10;    // ico图标 左偏移量
        this.CONST.ICO_MARGINTOP = 10;    // ico图标 上偏移量
        Object.freeze(this.CONST);
    };
    // 缓存数据
    Validation.prototype.cache = function(params){

        this.option = $.extend(true,this.option, params)


    };

    // 缓存dom
    Validation.prototype.createDom = function(){
        // this.$em = $('<em class="cus-em" ></em>');
        this.$tipem = $('<em class="cus-em" style="\n' +
            '    border: 2px solid;\n' +
            '    position: absolute;\n' +
            '    width: 1rem;\n' +
            '    border-radius: 50%;\n' +
            '    line-height: 130%;\n' +
            '    font-size: 90%;\n' +
            '    font-weight: bold;\n' +
            '    color: #0088cc;\n' +
            '    display: none;\n' +
            '    text-align: center;\n' +
            '">!</em>');

        this.$warnem = $('<em class="cus-em" style="\n' +
            '    border: 2px solid;\n' +
            '    position: absolute;\n' +
            '    width: 1rem;\n' +
            '    border-radius: 50%;\n' +
            '    line-height: 130%;\n' +
            '    font-size: 90%;\n' +
            '    font-weight: bold;\n' +
            '    color: #E1282A;\n' +
            '    display: none;\n' +
            '    text-align: center;\n' +
            '">!</em>');

        this.$tipem.css('height',this.$tipem.css('width'));
        this.$warnem.css('height',this.$warnem.css('width'));

    };

    // 样式表设置
    Validation.prototype.sheetStyle = function(){
        // document.styleSheets[0].insertRule('.cus-em {position: absolute;\n' +
        //     '    width: 1rem;\n' +
        //     '    border-radius: 50%;\n' +
        //     '    line-height: 110%;\n' +
        //     '    font-size: 90%;\n' +
        //     '    right: -1.5rem;}', 0);


    }


    // 事件绑定
    Validation.prototype.bindEvent = function(){
        var _this = this;
        var opt = this.option;
        $.each(opt.rules, function(key, obj){
            _this.bindEventHandle[obj.trigger].call(_this,key,obj);
        });


    };
    // 事件handle
    Validation.prototype.bindEventHandle = {
        blur: function(key,obj){
            var _this = this;
            var opt = _this.option;
            _this.bindEventHandle['focus'].call(_this,key,obj);
            $(opt.el).off('blur.cus.validation'+key, '.'+key)
                .on('blur.cus.validation'+key, '.'+key, function(){
                    var ioffsetWidth = $(this)[0].offsetWidth;
                    var ioffsetHeight = $(this)[0].offsetHeight;
                    var ioffsetTop = _getTop($(this).get(0));
                    var ioffsetLeft = _getLeft($(this).get(0));

                    // 隐藏提示图标
                    $('#'+ioffsetTop+''+ioffsetLeft).hide();
                    var _thisVal = '',_valStatus;
                    if(obj.val){
                        _thisVal = obj.val($(this));
                    }else{
                        _thisVal = $(this).val().trim();
                    }
                    if(obj.isRegx){
                        _valStatus = _regxVal.call(_this, _thisVal, obj.regx, obj);
                    }else{
                        _valStatus = _thisVal;
                    }

                    if(!_valStatus){
                        if(obj.tooltip){  // 图标提示
                            var _$warnem = $(_this.$warnem).clone(true).get(0);
                            $(_$warnem).prop('id',ioffsetTop+''+ioffsetLeft+'W');
                            // 设置ico图标水平偏移量
                            if(obj.icoLeft){
                                $(_$warnem).css({
                                    'left':ioffsetLeft+ioffsetWidth+obj.icoLeft,
                                });
                            }else{
                                $(_$warnem).css({
                                    'left':ioffsetLeft+ioffsetWidth+(_this.CONST.ICO_MARGINLIFE),
                                });
                            }
                            // 设置ico图标垂直偏移量
                            if(obj.icoTop){
                                $(_$warnem).css({
                                    'top':ioffsetTop+ioffsetHeight/2-obj.icoTop
                                });
                            }else{
                                $(_$warnem).css({
                                    'top':ioffsetTop+ioffsetHeight/2-(_this.CONST.ICO_MARGINTOP),
                                });
                            }
                            $(_$warnem).ctooltip({title: obj.warn});
                            if($('#'+ioffsetTop+''+ioffsetLeft+'W').length===0){
                                $(_$warnem).appendTo('body');
                            }
                            $('#'+ioffsetTop+''+ioffsetLeft+'W').show();
                            $(this).parent().addClass('vali-warn-k');
                        }else{  // 非图标提示
                            $(this).parent().addClass('vali-warn');
                        }

                    }else{
                        if(obj.tooltip) {  // 图标提示
                            $('#'+ioffsetTop+''+ioffsetLeft+'W').hide();
                            $(this).parent().removeClass('vali-warn-k');
                        }else{
                            $(this).parent().removeClass('vali-warn');
                        }
                    }
            });
        },
        focus: function(key,obj){
            var _this = this;
            var opt = _this.option;
            $(opt.el).off('focus.cus.validation'+key, '.'+key)
                .on('focus.cus.validation'+key, '.'+key, function(e){
                    var ioffsetWidth = $(this)[0].offsetWidth;
                    var ioffsetHeight = $(this)[0].offsetHeight;
                    var ioffsetTop = _getTop($(this).get(0));
                    var ioffsetLeft = _getLeft($(this).get(0));
                    // var iTop = e.pageY + 20;
                    // var iLeft = e.pageX + 20;
                   if (obj.tooltip){
                       var _$tipem = $(_this.$tipem).clone(true).get(0);
                       $(_$tipem).prop('id',ioffsetTop+''+ioffsetLeft);
                       // $(_$tipem).css({
                       //     'left':ioffsetLeft+ioffsetWidth+20,
                       //      'top':ioffsetTop+ioffsetHeight/2-10
                       // });
                       // 设置ico图标水平偏移量
                       if(obj.icoLeft){
                           $(_$tipem).css({
                               'left':ioffsetLeft+ioffsetWidth+obj.icoLeft,
                           });
                       }else{
                           $(_$tipem).css({
                               'left':ioffsetLeft+ioffsetWidth+_this.CONST.ICO_MARGINLIFE,
                           });
                       }
                       // 设置ico图标垂直偏移量
                       if(obj.icoTop){
                           $(_$tipem).css({
                               'top':ioffsetTop+ioffsetHeight/2-obj.icoTop
                           });
                       }else{
                           $(_$tipem).css({
                               'top':ioffsetTop+ioffsetHeight/2-(_this.CONST.ICO_MARGINTOP),
                           });
                       }
                       $(_$tipem).ctooltip({title: obj.tip});
                       if($('#'+ioffsetTop+''+ioffsetLeft).length===0){
                            $(_$tipem).appendTo('body');
                       }else{
                           $('#'+ioffsetTop+''+ioffsetLeft).remove();
                           $(_$tipem).appendTo('body');
                       }
                       $('#'+ioffsetTop+''+ioffsetLeft+'W').hide();
                       $('#'+ioffsetTop+''+ioffsetLeft).show();

                   }
                });
        },
        change: function(key,obj){
            var _this = this;
            var opt = _this.option;
            // 获取焦点
            $(opt.el).off('focus.cus.validation'+key, '.'+key)
                .on('focus.cus.validation'+key, '.'+key, function(e){
                    var ioffsetWidth = $(this)[0].offsetWidth;
                    var ioffsetHeight = $(this)[0].offsetHeight;
                    var ioffsetTop = _getTop($(this).get(0));
                    var ioffsetLeft = _getLeft($(this).get(0));
                    // var iTop = e.pageY + 20;
                    // var iLeft = e.pageX + 20;
                    if (obj.tooltip){
                        var _$tipem = $(_this.$tipem).clone(true).get(0);
                        $(_$tipem).prop('id',ioffsetTop+''+ioffsetLeft);
                        // $(_$tipem).css({
                        //     'left':ioffsetLeft+ioffsetWidth+20,
                        //     'top':ioffsetTop+ioffsetHeight/2-10
                        // });
                        // 设置ico图标水平偏移量
                        if(obj.icoLeft){
                            $(_$tipem).css({
                                'left':ioffsetLeft+ioffsetWidth+obj.icoLeft,
                            });
                        }else{
                            $(_$tipem).css({
                                'left':ioffsetLeft+ioffsetWidth+_this.CONST.ICO_MARGINLIFE,
                            });
                        }
                        // 设置ico图标垂直偏移量
                        if(obj.icoTop){
                            $(_$tipem).css({
                                'top':ioffsetTop+ioffsetHeight/2-obj.icoTop
                            });
                        }else{
                            $(_$tipem).css({
                                'top':ioffsetTop+ioffsetHeight/2-(_this.CONST.ICO_MARGINTOP),
                            });
                        }
                        $(_$tipem).ctooltip({title: obj.tip});
                        if($('#'+ioffsetTop+''+ioffsetLeft).length===0){
                            $(_$tipem).appendTo('body');
                        }
                        $('#'+ioffsetTop+''+ioffsetLeft+'W').hide();
                        $('#'+ioffsetTop+''+ioffsetLeft).show();

                    }
                });

            $(opt.el).off('change.cus.validation'+key, '.'+key)
                .on('change.cus.validation'+key, '.'+key, function(e){
                    var ioffsetWidth = $(this)[0].offsetWidth;
                    var ioffsetHeight = $(this)[0].offsetHeight;
                    var ioffsetTop = _getTop($(this).get(0));
                    var ioffsetLeft = _getLeft($(this).get(0));

                    // 隐藏提示图标
                    $('#'+ioffsetTop+''+ioffsetLeft).hide();
                    var _thisVal = '',_valStatus;
                    if(obj.val){
                        _thisVal = obj.val($(this));
                    }else{
                        _thisVal = $(this).val().trim();
                    }
                    if(obj.isRegx){
                        _valStatus = _regxVal.call(_this, _thisVal, obj.regx, obj);
                    }else{
                        _valStatus = _thisVal;
                    }

                    if(!_valStatus){
                        if(obj.tooltip){  // 图标提示
                            var _$warnem = $(_this.$warnem).clone(true).get(0);
                            $(_$warnem).prop('id',ioffsetTop+''+ioffsetLeft+'W');
                            // $(_$warnem).css({
                            //     'left':ioffsetLeft+ioffsetWidth+20,
                            //     'top':ioffsetTop+ioffsetHeight/2-10
                            // });
                            // 设置ico图标水平偏移量
                            if(obj.icoLeft){
                                $(_$warnem).css({
                                    'left':ioffsetLeft+ioffsetWidth+obj.icoLeft,
                                });
                            }else{
                                $(_$warnem).css({
                                    'left':ioffsetLeft+ioffsetWidth+_this.CONST.ICO_MARGINLIFE,
                                });
                            }
                            // 设置ico图标垂直偏移量
                            if(obj.icoTop){
                                $(_$warnem).css({
                                    'top':ioffsetTop+ioffsetHeight/2-obj.icoTop
                                });
                            }else{
                                $(_$warnem).css({
                                    'top':ioffsetTop+ioffsetHeight/2-(_this.CONST.ICO_MARGINTOP),
                                });
                            }
                            $(_$warnem).ctooltip({title: obj.warn});
                            if($('#'+ioffsetTop+''+ioffsetLeft+'W').length===0){
                                $(_$warnem).appendTo('body');
                            }
                            $('#'+ioffsetTop+''+ioffsetLeft+'W').show();
                            $(this).parent().addClass('vali-warn-k');
                        }else{  // 非图标提示
                            $(this).parent().addClass('vali-warn');
                        }

                    }else{
                        if(obj.tooltip) {  // 图标提示
                            $('#'+ioffsetTop+''+ioffsetLeft+'W').hide();
                            $(this).parent().removeClass('vali-warn-k');
                        }else{
                            $(this).parent().removeClass('vali-warn');
                        }
                    }

                });


        },

    };

    /**
     * 获取当前页面校验状态  true OR false
     * @return {boolean}
     */
    Validation.prototype.checked = function(){
        _triggerAllCheck.call(this);
        var _warnItem = $('.vali-warn,.vali-warn-k');
        if(_warnItem.length>0){
            var _scrolltop = ($(_warnItem[0]).offset().top-20)<=0 ? 0 : ($(_warnItem[0]).offset().top-20);
            $('html, body').animate({
                scrollTop: _scrolltop
            }, 1000);
          return false;
        }
         return true;
    };


    Validation.prototype.resetValidation = function(){
        var opt = this.option;
        // 去除警告
        $('.vali-warn,.vali-warn-k').each(function(i, el){
            $(el).removeClass('vali-warn');
            $(el).removeClass('vali-warn-k');
        });
        // 移除 em
        $('.cus-em').remove();
        this.init(opt);
    };
    /**
     * 触发所有加入校验的控件
     * @private
     */
    function _triggerAllCheck() {
        var _this = this;
        var opt = _this.option;
        $.each(opt.rules, function(key, obj){
           $('.'+key).each(function(i, el){
               $(this).trigger(obj.trigger+'.cus.validation'+key);
           });
        });
    };

    // 计算
    //获取元素的纵坐标
    function _getTop(e){
        var offset=e.offsetTop;
        if(e.offsetParent!=null) offset+=_getTop(e.offsetParent);
        return offset;
    }
    //获取元素的横坐标
    function _getLeft(e){
        var offset=e.offsetLeft;
        if(e.offsetParent!=null) offset+=_getLeft(e.offsetParent);
        return offset;
    }

    //正则校验
    function _regxVal(val, regx, obj){
        var _this = this, opt = this.option;
        var str = $.trim(val);
        if(str) {
            // reg=/^([1-9]\d*)$/;
            var reg = regx;
            if (!reg.test(str)) {
                return false;
            }else{
                return true;
            }
        }
        if(obj.AllowEmpty){
            return true;
        }else{
            return false;
        }

    };

    $.fn.cValidation = new Validation();

})(jQuery,window);