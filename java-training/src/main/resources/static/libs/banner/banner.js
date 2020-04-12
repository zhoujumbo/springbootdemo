;(function(){
    'use strict';
    var Banner = function(el,opts){
        this.el = el;
        this.opts = opts?opts:{};

    };

    Banner.VERSION = '1.0.0';

    Banner.prototype.init = function(){
        _defineValue.call(this);
        _defineDom.call(this);
        _loadDom.call(this);
        _drawBanner.call(this);
        _bindEvents.call(this);
    };

    function _defineValue (){
        var _this = this;
        _this.params = { //
            el: _this.el,
            ajaxPram: null,
            jsonPram: null,
            data: [],  // 初始化数据
            timerLenth:3000,
            style: {
                banner: {
                    width: '100%',
                    height: '150px',
                },
                pointBtn: null,
                bannerPrev:null,
                bannerNext:null,
            },

        };
        _this.params = $.extend(true, _this.params, _this.opts);

        _this.i = 0;
        _this.timer = null;
        _this.timerLenth = _this.params.timerLenth;
        _this.data = [];
        _this.imgNum = _this.params.ajaxPram?0:_this.params.data.length;

    };

    function _defineDom(){
        var _this = this;
        _this.$banner = '<div class="banner"></div>';
        _this.$ul_img = '<ul class="banner-img"></ul>';
        _this.$li = '<li></li>';
        _this.$a = '<a href="#"></a>';
        _this.$img = '<img src="" alt="">';
        _this.$ul_point = '<ul class="banner-point"></ul>';
        _this.$div = '<div class="banner-btn"></div>';
        _this.$span_prev = '<span class="banner-prev">\<</span>';
        _this.$span_next = '<span class="banner-next">\></span>';
    };
// _this.params.style.pointBtn /bannerPrev / bannerNext  /banner
    function _loadDom(){
        var _this = this;

        var $ulImg = $(_this.$ul_img);
        var $ulPoint = $(_this.$ul_point);
        var $divBtn = $(_this.$div);
        var $spanPrev = $(_this.$span_prev);
        var $spanNext = $(_this.$span_next);
        // 重设样式
        if(_this.params.style.bannerPrev){
            $spanPrev.css(_this.params.style.bannerPrev);
        }
        if(_this.params.style.bannerNext){
            $spanNext.css(_this.params.style.bannerNext);
        }

        $divBtn.append($spanPrev).append($spanNext);
        _this.banner = $(_this.$banner).append($ulImg)
            .append($ulPoint).append($divBtn);

        if(_this.params.style.banner){
            _this.banner.css(_this.params.style.banner);
        }
        // _this.banner.width(_this.params.style.banner.width)
        //     .height(_this.params.style.banner.height);
        $(_this.el).append(_this.banner);
        // 取得banner的宽度
        _this.bannerWidth = $('.banner')[0].offsetWidth;
        _this.bannerHeight = $('.banner')[0].offsetHeight;
    };

    function _bindEvents(){
        var _this = this;

        //根据光标的影响控制按钮的显示和隐藏
        $('.banner').hover(function(){
            $('.banner-btn').show();
        },function(){
            $('.banner-btn').hide();
        });

        // 添加定时器
        //定时器自动轮播
        _this.timer=setInterval(function(){
            _this.i++;
            if (_this.i==$('.banner-img li').length) {
                _this.i=1;
                $('.banner-img').css({left:0});//保证无缝轮播，设置left值
            }
            //进行下一张图片
            $('.banner-img').stop().animate({left:-_this.i*_this.bannerWidth},500);
            //圆点跟着变化
            if (_this.i==$('.banner-img li').length-1) {
                $('.banner-point li').eq(0).addClass('active').siblings().removeClass('active');
            }else{
                $('.banner-point li').eq(_this.i).addClass('active').siblings().removeClass('active');
            }
        },_this.timerLenth);

        //鼠标移入，暂停自动播放，移出，开始自动播放
        $('.banner').hover(function(){
            clearInterval(_this.timer);
        },function(){
            _this.timer=setInterval(function(){
                _this.i++;
                if (_this.i==$('.banner-img li').length) {
                    _this.i=1;
                    $('.banner-img').css({left:0});
                };
                //进行下一张图片
                $('.banner-img').stop().animate({left:-_this.i*_this.bannerWidth},500);
                //圆点跟着变化
                if (_this.i==$('.banner-img li').length-1) {
                    $('.banner-point li').eq(0).addClass('active').siblings().removeClass('active');
                }else{
                    $('.banner-point li').eq(_this.i).addClass('active').siblings().removeClass('active');
                }
            },_this.timerLenth)
        });

        //上一个按钮
        $('.banner-prev').click(function(){
            _this.i--;
            if (_this.i==-1) {
                _this.i=$('.banner-img li').length-2;
                $('.banner-img').css({left:-($('.banner-img li').length-1)*_this.bannerWidth});
            }
            $('.banner-img').stop().animate({left:-_this.i*_this.bannerWidth},500);
            $('.banner-point li').eq(_this.i).addClass('active').siblings().removeClass('active');
        });

        // 下一个按钮
        $('.banner-next').click(function(){
            _this.i++;
            if (_this.i==$('.banner-img li').length) {
                _this.i=1; //这里不是i=0
                $('.banner-img').css({left:0});
            };
            $('.banner-img').stop().animate({left:-_this.i*_this.bannerWidth},500);
            if (_this.i==$('.banner-img li').length-1) { //设置小圆点指示
                $('.banner-point li').eq(0).addClass('active').siblings().removeClass('active');
            }else{
                $('.banner-point li').eq(_this.i).addClass('active').siblings().removeClass('active');
            };

        });

        //鼠标划入圆点
        $('.banner-point li').mouseover(function(){
            var _index=$(this).index();
            //维持i变量控制的对应关系不变
            _this.i = _index;
            $('.banner-img').stop().animate({left:-_index*_this.bannerWidth},300);
            $('.banner-point li').eq(_index).addClass('active').siblings().removeClass('active');
        });

    };

    function _getAjaxData(){
        var _this = this;
        var bannerData = [];
        var ajaxPram = {
            type: 'post',
            async:false,
            url: '',
            data: {},
            //contentType:"application/json ",
            cache:false,
            dataType: 'json',
            success: function(response) {
                if(response){
                    _this.imgNum = response.length;
                    bannerData = response;
                }
            },
            error: function(response) {
                console.error("reqAjax请求ERROR:"+response);
            },
            complete: function(XMLHttpRequest) {}
        };
        if(_this.params.ajaxPram){
            ajaxPram = $.extend(true, ajaxPram, _this.params.ajaxPram);
        }
        $.ajax(ajaxPram);
        return bannerData;
        // 给_this.imgNum 赋值
    };

    function _drawBanner (){
        var _this = this;
        // if(!_this.params.ajaxPram && !_this.params.ajaxPram && _this.params.data.length==0){
        //     console.error("banner:Data is undefined");
        //     return;
        // }
        if(!_this.params.jsonPram && _this.params.ajaxPram && _this.params.data.length==0){  // 必须同步获取数据
            _this.data = _getAjaxData.call(_this);
        }else if(_this.params.jsonPram && !_this.params.ajaxPram && _this.params.data.length==0){
            _this.data = _getJsonData.call(_this);
        }else if(!_this.params.jsonPram && !_this.params.ajaxPram && _this.params.data.length!=0){
            _this.data = _this.params.data;
        }else{
            console.error("banner:Data is undefined");
            return;
        }

        // 给li添加数据 并添加到ul中
        if(_this.data.length>0){
            $.each(_this.data, function(idx, item){
                var $li = $(_this.$li);
                var $a = $(_this.$a);
                $a.prop('href',item.url);
                $a.html(item.title);
                $a.appendTo($li);
                $li.width(_this.bannerWidth).height(_this.bannerHeight);
                $li.css({
                    'background': 'url('+item['imgSrc']+') no-repeat center center',
                    'background-size': '100% 100%'
                });
                $li.data('url',item['url']);
                $('.banner-img').append($li);
            });
        }else{
            console.error("banner:Data is undefined");
            return;
        }

        // 根据图片的张数动态添加圆点个数
        if(_this.imgNum>0){
            for (var j = 0; j < _this.imgNum; j++) {
                $('.banner-point').append(_this.$li);
            }
            //默认情况下的第一个圆点进行背景设计
            $('.banner-point li').first().addClass('active');
        }

        //将第一张图片复制并添加到img部分下与前图片相接
        var firstimg=$('.banner-img li').first().clone(); //复制第一张图片
        $('.banner-img').append(firstimg).width($('.banner-img li').length*($('.banner-img li').width()));
        // $('.banner-img').append(firstimg);


    };

    function _getJsonData(){
        var _this = this;
        var bannerData = [];
        $.ajaxSetup({async:false})
        $.getJSON(_this.params.jsonPram, "", function(data) {
            if(data){
                _this.imgNum = data.length;
                bannerData = data;
            }else{
                _this.imgNum = 0;
            }
        });
        $.ajaxSetup({async:true});
        return bannerData;
    };

    function Plugin(option,methdOpt){
        var data =  $(this).data('zh.cus.banner');
        if(typeof option == 'string') {
            return data[option].call(data,methdOpt);
        }
        return this.each(function(){
            var _this = $(this);
            var data = _this.data('zh.cus.banner');
            var options = typeof option == 'object' && option;
            if(!data){
                _this.data('zh.cus.banner',(data = new Banner(_this,options)));
                data.init();
            } else {
                data.opts = options?options:{};
                data.init();
            }
        });
    }
    var old = $.fn.banner;
    $.fn.banner = Plugin;
    $.fn.banner.Constructor = Banner;

    //解决冲突
    $.fn.banner.noConflect = function(){
        $.fn.banner = old;
        return this;
    };
})();
