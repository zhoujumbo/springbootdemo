/*************************************************************************************
*Title:toolBar,tbar                                              
*Description:grid分页工具
*params: position(left,center,right)：控件的位置.
*    baseParams:基本参数
*    baseForm:过滤表单，传入通过formSubmit()方法获取的form，通过点击表单中的提交按钮自动获取表单参数并刷新该表格
          formSubmit()使用方法详见该方法定义        
     btns（json数组）：添加按钮，默认封装成idxButton，json格式：text：默认按钮的内容，handler：按钮响应事件，type：默认不填，若填type:'html',则按钮就是text中封装的html格式控件
     url：查询数据路径
*method: search(para)表格刷新   
*       setParams(data):设置tbar查询时的参数                                  
 *date: 2018/11/26
**************************************************************************************/

;(function($){
  'use strict';
  var ToolBar = function(params){
  	this._params = params;
  };
  ToolBar.VERSION = '3.1.1';
  /**
   * init()
   * */
  ToolBar.prototype.init = function(){
  	//------初始
  	_defineValue.call(this);//参数-初始化
  	
  	_defineDom.call(this);//缓存dom
  	
  	_loadDom.call(this);//加载dom
  	
  	//------设置
  	_setSearchForm.call(this);//设置查询表单
  	
  	_setControlsPosition.call(this);//设置控件位置
  	
  	_setBtn.call(this);//添加按钮
  	
  	//------事件
  	_bindEvents.call(this);
  	
  };
  /**
   * setOpts()
   * */
  ToolBar.prototype.setOpts = function(data) {
  	var _this = this;
  	
    $.each(data,function(k,v){
      _this.opts[k] = v;
    });
    return _this;
  };
  /**
   * setParams()
   * */
  ToolBar.prototype.setParams = function(data) {
  	var _this = this;
  	
    _this.searchPara = data;
  };
  /**
   * search()
   * 数据刷新方法，oSearchParams：过滤条件，flag：如果不为true则分页从头开始
   * */
  ToolBar.prototype.search = function(oSearchParams, flag, isAll, noMask) {
		var _this = this;
		
    if(!noMask){
      if (!_this.oTbarContent.find('.common_tbarMask').get(0)) {
        _this.oTbarContent.append('<div class="common_tbarMask"></div>');
        _this.grid.loadMask.call(_this.grid);
      } 
    }
   
    _this.onSearch = true;
    if (_this.opts.baseForm) {
      _this.oParams = _this.opts.baseForm.data('cw.formSubmit').getParams(); //获取表单参数
    }

    if (_this.opts.baseParams) {
      $.each(_this.opts.baseParams, function(name, value) {
        _this.oParams[name] = value; //获取定义tbar的参数数据
      });
    }

    if (_this.searchPara) {
      $.each(_this.searchPara, function(name, value) {
        _this.oParams[name] = value;
      });
    }

    if (oSearchParams) {
      _this.oSearchData = oSearchParams; //将参数传给_this.oSearchData，之后分页时使用该参数
      $.each(oSearchParams, function(name, value) {
        _this.oParams[name] = value; //获取saarch的参数
        if (name == 'sort') {
          _this.sort = value;
        } else if (name == 'dir') {
          _this.dir = value;
        }
      });
    } else {
      _this.oSearchData = {}; //如果没有传参，则置为初始值

    }

    _this.oParams.sort = _this.sort;
    _this.oParams.dir = _this.dir;

    if (!flag) {
      _this.iStart = 0;
      _this.iPage = 1;
    }

    if(typeof flag === 'object' && flag.curePage){
       _this.iStart -= _this.iLimit;
    }

    _this.oParams.start = _this.iStart;
    _this.oParams.limit = _this.iLimit;

    _this.iStart += _this.iLimit; //查询后将start置为下一页起始位置
    if(_this.isAll&&isAll){
      _this.page ++;
    }else if(_this.isAll&&typeof isAll === 'undefined'){
  	_this.page = 1;
  	_this.grid.$dataTBody.find('tr').remove();
    }
    $.ajax({ //查询数据
      type: 'post',
      data: _this.oParams,
      url: _this.opts.url,
      dataType: 'json',
      error: function() {

      },
      success: function(response) {
        _this.iTotal = parseInt(response.totalProperty); //获取总数量
        _this.iPageSize = Math.ceil(_this.iTotal / _this.iLimit); //页码数
        _this.oPageInput.val(_this.iPage); //设置当前页值
        _this.oPageCount.text(_this.iPageSize);
        _this.oDataCount.html(response.totalProperty + '&nbsp;条');
        _this.grid.refreshData(response, _this.isAll, _this.page); //调用表格的刷新数据方法
        _this.onSearch = false;
      },
      complete: function() {}
    });
  };
  /**
   * refreshData()
   * */
  ToolBar.prototype.refreshData = function(oData){
  	var _this = this;
  	
    _this.grid.refreshData(oData); //调用表格的刷新数据方法
    _this.iTotal = parseInt(oData.totalProperty); //获取总数量
    _this.iPageSize = Math.ceil(_this.iTotal / _this.iLimit); //页码数
    _this.oPageInput.val(1); //设置当前页值
    _this.oPageCount.text(_this.iPageSize);
    _this.oDataCount.html(oData.totalProperty + '&nbsp;条');
  };
  /**
   * setGrid()
   * */
  ToolBar.prototype.setGrid = function(grid) {
  	var _this = this;
  	
    _this.grid = grid;
    if (_this.opts.url) {
      if (!grid.getNoLoad()) {
        _this.search();
      }
    }
    _this.grid.$gridScroll.on('mousewheel scroll',function(e){

      if( _this.oTypeBtn.data('flag') == 'hide'){
        var grid = _this.grid;
        var scrollTop =  grid.$gridScroll.scrollTop();
        var gridHeight = grid.$gridScroll.height();
        var innerHeight = grid.$gridDiv.height();
        if(!_this.onSearch && (innerHeight-scrollTop-gridHeight < 200)){
          _this.search(_this.oSearchData, true, true, true);
        }
      }
      e.stopPropagation();
    });
  };
  /**
   * getLimit()
   * 获取limit值
   * */
  ToolBar.prototype.getLimit = function() { 
  	var _this = this;
    return _this.iLimit;
  };
  
  //参数-初始化
  function _defineValue(){
  	var _this = this;
  	
  	_this.iStart = 0;
    _this.opts = this._params;
    _this.iLimit = _this.opts.limit;
    _this.iTotal = 0;
    _this.iPage = 1;
    _this.oParams = {};
    _this.iPageSize = 0;
    _this.iOldPage = '1';
  	
  	_this.oSearchData = {}; //search方法传入的参数过滤条件
    _this.onSearch = false;
    _this.sort = null;
    _this.dir = null;
  };
  
  //dom 缓存
  function _defineDom(){
  		var _this = this;
  		
  		if(_this.oTbarContent){
      	_this.oTbarContent.empty();
      }
      _this.oTbarContent = $('<div class="common_tbar_content"></div>'); //分页工具的容器

      _this.oSy = $('<div class="common_tb_hide common_tbBtn common_sy fa fa-angle-double-left  fa-fw" ></div>'); //首页按钮
      _this.oPre = $('<div class="common_tb_hide common_tbBtn common_pre fa fa-angle-left fa-fw" ></div>'); //前一页按钮
      _this.oNext = $('<div class="common_tb_hide common_tbBtn common_next fa fa-angle-right fa-fw" ></div>'); //下一页按钮
      _this.oWy = $('<div class="common_tb_hide common_tbBtn common_wy fa fa-angle-double-right fa-fw" ></div>'); //尾页按钮
      _this.oPageInput = $('<input type="text" class="common_tb_hide common_table_page_input" />'); //页面输入框
      _this.oPageCount = $('<div class="common_tb_hide common_toolbartext"></div>'); //总页面数
      _this.oDataCount = $('<div class="common_tb_hide common_toolbartext"></div>'); //总数据数
      _this.oTypeBtn = $('<button  type="button" data-flag="show"  style="margin-right:10px">显示全部</button>');
  	
  };
  
  //dom 加载
  function _loadDom(){
  	var _this = this;
  	
  	_this.oTbarContent.append(_this.oSy)
                          .append(_this.oPre)
                          .append(_this.oPageInput)
                          .append(_this.oNext)
                          .append(_this.oWy)
                          .append('<div class="common_tb_hide common_toolbartext">&nbsp;共&nbsp;</div>')
                          .append(_this.oPageCount)
                          .append('<div class="common_tb_hide common_toolbartext">&nbsp;页&nbsp;&nbsp;</div>')
                          .append(_this.oDataCount)
                          .append('<div class="common_tb_hide common_tbar_btns_line"></div>') //分割线
                          .append(_this.oTypeBtn);
  };
  
  //事件
  function _bindEvents(){
  	var _this = this;
  	
  	_this.oTypeBtn.off('click.cw.toolBar.typeBtn','').on('click.cw.toolBar.typeBtn',_oTypeBtnEvent.bind(_this));
  	
  	_this.oPageInput.off('keypress.cw.toolBar','').on('keypress.cw.toolBar',_oPageInputEvent.bind(_this));
  	//首页
  	_this.oSy.off('click.cw.toolBar.oSy','').on('click.cw.toolBar.oSy',_oSyEvent.bind(_this));
  	//上一页
    _this.oPre.off('click.cw.toolBar.oPre','').on('click.cw.toolBar.oPre',_oPreEvent.bind(_this));
		//下一页
    _this.oNext.off('click.cw.toolBar.oNext','').on('click.cw.toolBar.oNext',_oNextEvent.bind(_this));
		//尾页
    _this.oWy.off('click.cw.toolBar.oWy','').on('click.cw.toolBar.oWy',_oWyEvent.bind(_this));
  };
  
  //设置查询表单
  function _setSearchForm(){
  	var _this = this;
  	
  	if (_this.opts.baseForm) {
      _this.opts.baseForm.data('cw.formSubmit').setGrid(_this);
    }
  };
  
  //设置控件位置
  function _setControlsPosition(){
  	var _this = this;
  	
  	if (_this.opts.position) {
          if (_this.opts.position == 'left') {
            _this.oTbarContent.css('justify-content', 'flex-start'); //控件居左
          } else if (_this.opts.position == 'center') {
            _this.oTbarContent.css('justify-content', 'center'); //控件居中
          } else if (_this.opts.position == 'right') {
            _this.oTbarContent.css('justify-content', 'flex-end'); //控件居右
          }
    }
  };
  
  //添加按钮
  function _setBtn(){
  	var _this = this;
  	
  	if (_this.opts.btns) {
          $.each(_this.opts.btns, function(index, value) {
            if (!value.type) { //默认封装成按钮
              _this.oBtn = $('<button  type="button" style="margin-right:10px">' + value.text + '</button>');
              _this.oTbarContent.append(_this.oBtn);
              _this.oBtn.on('click', value.handler);
             

            } else if (value.type == 'html') { //如果type为html。则按钮类型为自定义的内容
              _this.oBtn = $(value.text);
              _this.oTbarContent.append(_this.oBtn);
              _this.oBtn.on('click', value.handler).css('margin-right', '10px');
            }
          });
        }
  };
  
  
  
  //typeBtnEvent
  function _oTypeBtnEvent(e){
  	var _this = this;//this element
  	var $this = $(e.currentTarget);
    if($this.data('flag') === 'show'){ 
      // 显示全部
      _this.isAll = true;
      $this.data('flag','hide').text('分页显示');
      _this.oTbarContent.find('.common_tb_hide').hide();
      _this.grid.$dataTBody.find('tr').remove();
      _this.start = 0;
      _this.oldLimit = _this.iLimit;
      _this.iLimit = 50;
      _this.page = 0;
      _this.search(_this.oSearchData, false, true);
    } else {
      // 分页展示
      _this.isAll = false;
      $this.data('flag','show').text('显示全部');
      _this.oTbarContent.find('.common_tb_hide').show();
      _this.start = 0;
      _this.iLimit = _this.oldLimit;
      _this.search(_this.oSearchData);
    }
  };
  
  //pageInputEvent
  function _oPageInputEvent(event) {
		var _this = this;
		
    if (event.keyCode == "13") { //页码输入框回车后的事件
      if (!isNaN(_this.oPageInput.val()) && _this.oPageInput.val() <= _this.iPageSize && _this.oPageInput.val() >= 1) {
        _this.oPageInput.val(Math.round(_this.oPageInput.val()));
        _this.iPage = _this.oPageInput.val();
      } else if (!isNaN(_this.oPageInput.val()) && _this.oPageInput.val() >= _this.iPageSize) {
        _this.oPageInput.val(_this.iPageSize);
        _this.iPage = _this.iPageSize;
      } else {
        _this.oPageInput.val(1);
        _this.iPage = 1;
      }
      if (_this.iOldPage != _this.iPage) {
        _this.iStart = (_this.iPage - 1) * _this.iLimit;
        _this.search(_this.oSearchData, true);
      }
    }
  };
  
  // 首页点击事件
  function _oSyEvent(){
  	var _this = this;
  	
  	if (!_this.onSearch) {
      if (_this.oPageInput.val() != '1') {
        _this.iStart = 0;
        _this.iPage = 1;
        _this.search(_this.oSearchData, true);
      }
    }
  };
  
  //上一页点击事件
  function _oPreEvent(){
  	var _this = this;
  	
  	if (!_this.onSearch) {
      if (_this.oPageInput.val() != '1') {
        _this.iPage -= 1;
        _this.iStart = (_this.iPage - 1) * _this.iLimit;
        _this.search(_this.oSearchData, true);
      }
    }
  };
  
  //下一页点击事件
  function _oNextEvent(){
  	var _this = this;
  	
  	if (!_this.onSearch) {
	    if (_this.oPageInput.val() != _this.iPageSize) {
	       //手动输入页码之后点击下一页会出现以字符串形式拼接的问题--luqian
	        if(typeof _this.iPage == 'string'){
	          _this.iPage = parseInt(_this.iPage);
	        }
	      _this.iPage += 1;
	      _this.iStart = (_this.iPage - 1) * _this.iLimit;
	      _this.search(_this.oSearchData, true);
	    }
	  }
  };
  
  //尾页事件
  function _oWyEvent(){
  	var _this = this;
  	
  	if (!_this.onSearch) {
      if (_this.oPageInput.val() != _this.iPageSize) {
        _this.iPage = _this.iPageSize;
        _this.iStart = (_this.iPage - 1) * _this.iLimit;
        _this.search(_this.oSearchData, true);
      }
    }
  };
  
	function Plugin(option){
		var data = new ToolBar(option);
		// data.init();
		return data;
	}
  
  var old = $.toolBar;

  var oldTbar = $.tbar;
  $.tbar = $.toolBar = Plugin;
  
  
  //解决冲突
  $.toolBar.noConflect = function(){
    $.toolBar = old;
  };
  
  $.tbar.noConflect = function(){
    $.tbar = oldTbar;
  };
})(jQuery);
