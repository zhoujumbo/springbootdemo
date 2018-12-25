;(function($) {

    /***
     * $.extend
     * Description：jquery扩展对象
     */
  $.extend({

  });


  $.fn.extend({

  });

})(jQuery);


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
                _this.oTooltip.siblings('.common_tooltip').slideUp('fast');
                setTimeout(function() {
                    if(_this.bShow){
                        _this.oTooltip.slideDown('fast');
                    }
                }, 100);
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
    var old = $.fn.tooltip;
    $.fn.tooltip = Plugin;
    $.fn.tooltip.Constructor = Tooltip;

    //解决冲突
    $.fn.tooltip.noConflect = function(){
        $.fn.tooltip = old;
        return this;
    };
})(jQuery);

