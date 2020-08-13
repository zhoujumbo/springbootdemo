/*************************************************************************************
 *Title: dynamicTable
 *Description: 动态表格模板
 *@params {int}
 *author: zhoujumbo
 *date: 2018/11/11
 **************************************************************************************/
;(function ($) {

    function DynamicTable(el, opts) {
        this.$el = el;
        this.opts = opts ? opts : {};
        this.params = this.opts;
    }

    DynamicTable.VERSION = '1.0.1';

    DynamicTable.prototype.init = function () {

        defCacheVal.call(this, this.params);
        defDomLoad.call(this);
        drawTemplate.call(this);
        defBindEvent.call(this);
    }


    function defCacheVal(params) {
        var _this = this;
        _this.options = {
            el: _this.$el,
            tempHeadEl: '',    // 头模板ID
            tempContainerEl: '', // 行模板ID
            btnShow: true,// 显示新增/移除按钮
            addtype: bottom, // 新增位置   待实现
            btnCopy: false, // 复制功能  待实现
            maxItem: 5, // 最多添加条数  必须设置btnShow为true
            defaultData: [], //默认数据  待实现
            ajaxParam: null, // 数据请求   待实现
            addItemCb: null, // 新增条目回调
            removeItemCb: null, // 移除条目回调
            onInitComponent: null, //初始化行  添加自定义控件
            onInitItem: null, // 回显数据处理
            callback: null,  //   回调
        }
        _this.options = $.extend(true, _this.options, params);
        var $container = $(_this.options.el);
        $container.data('itemSum', 0); // 父元素记录数据条数 ，初始化为0
    }

    function defDomLoad() {
        var _this = this;

        _this.$headHandleCell = $('<div class="cus-com-cell">\n' +
            '<div Class="cus-com-cell-content"><span>操作</span></div>\n' +
            '</div>');

        _this.$contHandleCell = $('<div class="cus-com-cell">\n' +
            '<div Class="cus-com-cell-content cusTbBtnContent">\n' +
            '<a href="javascript:void(0)" class="cusTbBtn cusAddItem" style="margin-right: 0.5rem">新增</a>\n' +
            '<a href="javascript:void(0)" class="cusTbBtn cusSubItem">移除</a>\n' +
            '</div>\n' +
            '</div>');

        _this.$cusAddBtn = $('<a href="javascript:void(0)" class="cusTbBtn cusAddItem" style="margin-right: 0.5rem">新增</a>');
        _this.$cusSubBtn = $('<a href="javascript:void(0)" class="cusTbBtn cusSubItem" >移除</a>');
    }

    function drawTemplate(data, callback) {
        var _this = this;
        var opt = _this.options;
        var $container = $(opt.el);
        var $headTemp = $('#' + opt.tempHeadEl).html();  // 头部模板
        var $contTemp = $('#' + opt.tempContainerEl).html();
        var formStrDataParam = {};
        // 判断并添加按钮
        if (opt && opt.btnShow) {
            // 添加头部
            $headTemp = $($headTemp).append(_this.$headHandleCell.clone(true).get(0));
            // 給行添加按钮区域
            $contTemp = $($contTemp).append(_this.$contHandleCell.clone(true).get(0));
        }

        if (!data) {  // 数据为空 默认添加一个空行
            $container.append($($headTemp)).append($($contTemp));
            resetTabMsg.call(_this); // 设置参数
            setBtns.call(_this);     // 设置btn
            // 回调执行
            opt.onInitComponent && opt.onInitComponent($('.cus-com-col-content', $container).get(0));
            // 控件加载完成调用回调
            opt.callback && opt.callback({status: 'init'});
        } else {
            var msg = typeof (data) !== 'object' ? '表格模板数据格式错误' : '';
            if (msg != '') {
                alert(msg);
                return;
            }
            // cus-com-col-content
            // 清空所有数据
            $container.empty();
            $container.append($($headTemp));
            var _$contTemp = null;
            if (data.length === 0) { // 明细数据为空 添加空行
                _$contTemp = $($contTemp).clone(true).get(0);
                // 回调执行 加载行内控件
                opt.onInitComponent && opt.onInitComponent(_$contTemp);
                $container.append(_$contTemp);
                resetTabMsg.call(_this); // 设置参数
                setBtns.call(_this);     // 设置btn
            } else {
                $.each(data, function (idx, obj) {
                    _$contTemp = $($contTemp).clone(true).get(0);
                    // 缓存数据

                    // 回调执行 加载行内控件
                    opt.onInitComponent && opt.onInitComponent(_$contTemp);
                    Object.keys(obj).forEach(function (key) {
                        // 回调执行 加载行数据
                        callback && callback(_$contTemp, obj, key);
                    });
                    $container.append(_$contTemp);
                    resetTabMsg.call(_this); // 设置参数
                    setBtns.call(_this);     // 设置btn
                });
            }
            opt.callback && opt.callback({status: 'ready'});
        }
    }

    /**
     * 事件绑定
     */
    function defBindEvent() {
        var _this = this;
        var opt = _this.options;
        var $container = $(opt.el);

        $($container).off('click.cusTbBtn.cusTb', '.cusTbBtn').on('click.cusTbBtn.cusTb', '.cusTbBtn', function () {
            var isAddBtn = $(this).hasClass('cusAddItem');
            var isSubBtn = $(this).hasClass('cusSubItem');
            var sn = $(this).closest('.cus-com-col-content').data('sn');
            // console.log('当前行编号'+sn)
            console.time("新增行性能测试");
            isAddBtn && cusAddItem.call(_this);
            console.timeEnd("新增行性能测试");
            console.time("删除行性能测试");
            isSubBtn && cusSubItem.call(_this, sn);
            console.timeEnd("删除行性能测试");
        });
    }

    /**
     * 事件执行函数
     * 增加条目
     */
    function cusAddItem() {
        var _this = this;
        addItem.call(_this);
        resetTabMsg.call(_this);
        setBtns.call(_this);
    }

    /**
     * 事件执行函数
     * 移除条目
     * @param sn
     */
    function cusSubItem(sn) {
        var _this = this;
        removeItem.call(_this, sn);
        resetTabMsg.call(_this);
        setBtns.call(_this);
    }

    /**
     * 增加一行
     */
    function addItem() {
        var _this = this;
        var opt = _this.options;
        var $container = $(opt.el);
        var $contTemp = $('#' + opt.tempContainerEl).html();
        // 判断并添加按钮
        if (opt && opt.btnShow) {
            // 給行添加按钮区域
            $contTemp = $($contTemp).append(_this.$contHandleCell.clone(true).get(0));
        }
        // 初始化行内控件

        opt.onInitComponent && opt.onInitComponent($($contTemp).get(0));
        $container.append($($contTemp));
        // 回调
        opt.addItemCb && opt.addItemCb($($contTemp).get(0), $container);
    }

    /**
     * 根据行编号移除数据
     */
    function removeItem(sn) {
        var _this = this;
        var opt = _this.options;
        var $container = $(opt.el);
        $.each($('.cus-com-col-content', $container), function (i, v) {
            if (i === sn) {
                $(v).remove();
            }
        });
        // 回调
        opt.removeItemCb && opt.removeItemCb($('.cus-com-col-content', $container).get(0));
    }

    /**
     * 格式化每行排序信息 和总数信息
     */
    function resetTabMsg() {
        var _this = this;
        var opt = _this.options;
        var $container = $(opt.el);
        $container.data('itemSum', 0);
        $.each($('.cus-com-col-content', $container), function (i, v) {
            $(v).data('sn', i);
            $container.data('itemSum', ($container.data('itemSum') + 1));
        });
    }

    /**
     * 控制按钮
     */
    function setBtns() {
        var _this = this;
        var opt = this.options;
        var $container = $(opt.el);// 行dom
        var Maxitem = opt.maxItem; // 允许添加的最大条数
        var nowItem = $container.data('itemSum');
        $.each($('.cus-com-col-content', $container), function (i, v) {
            $('.cusTbBtn', $(v)).remove();
            if (nowItem === 1) {
                $('.cusTbBtnContent', $(v)).append(_this.$cusAddBtn.clone(true).get(0));
            } else if (nowItem > 1 && nowItem < Maxitem) {
                if (i < nowItem - 1) {
                    $('.cusTbBtnContent', $(v)).append(_this.$cusSubBtn.clone(true).get(0));
                }
                if (i === nowItem - 1) {
                    $('.cusTbBtnContent', $(v)).append(_this.$cusAddBtn.clone(true).get(0));
                    $('.cusTbBtnContent', $(v)).append(_this.$cusSubBtn.clone(true).get(0));
                }
            } else if (nowItem === Maxitem) {
                $('.cusTbBtnContent', $(v)).append(_this.$cusSubBtn.clone(true).get(0));
            }


        });
    }

    /**
     * 回显数据
     * @param data
     * @return {boolean}
     */
    DynamicTable.prototype.setData = function (data) {
        var _this = this;
        var opt = this.options;
        // 校验数据
        if (!data || data == null || typeof data == 'undefined') {
            console.error('error：' + data + '!!!' + e);
            return false;
        }
        if (typeof data == 'string') {
            try {
                var _data = JSON.parse(data);
                // 根据数据渲染tb
                drawTemplate.call(_this, data, opt.onInitItem);
            } catch (e) {
                console.error('error：' + data + '!!!' + e);
                return false;
            }
        } else {
            try {
                // 根据数据渲染tb
                drawTemplate.call(_this, data, opt.onInitItem);
            } catch (e) {
                console.error('error：' + data + '!!!' + e);
                return false;
            }
        }
    }

    /**
     * Tools
     * 数据绑定工具 {{}}
     * @param str
     * @param data
     * @return {string|void|XML}
     */
    function formStrData(str, data) {
        return str.replace(/\{\{(\w+)\}\}/g, function (match, $1) {
            if (!data) {
                return;
            }
            if ($1 == '') {
                return;
            }
            return data[$1];
        });
    }

    /**
     * Tools
     * dom to str
     * @param $el
     * @return {string}
     */
    function domToHtmlStr($el) {
        var $d = document.createElement('div');
        $d.append($el);
        return $d.innerHTML;
    }

    function Plugin(option, methdOpt) {
        var data = $(this).data('sw.dytable');
        if (typeof option == 'string') {
            return data[option].call(data, methdOpt);
        }
        return this.each(function () {
            var _this = $(this);
            var data = _this.data('sw.dytable');
            var options = typeof option == 'object' && option;
            if (!data) {
                _this.data('sw.dytable', (data = new DynamicTable(_this, options)));
                data.init();
            } else {
                data.opts = options ? options : {};
                data.init();
            }
        });
    }

    var old = $.fn.dynamicTable;
    $.fn.dynamicTable = Plugin;
    $.fn.dynamicTable.Constructor = DynamicTable;

    //解决冲突
    $.fn.dynamicTable.noConflect = function () {
        $.fn.dynamicTable = old;
        return this;
    };

})(jQuery);