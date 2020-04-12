/*************************************************************************************
 *Title: grid                                                      
 *Description:表格 分为分页和不分页两种
 *date: 2018/11/26
 **************************************************************************************/
;(function($) {
  function Grid(el, opts){
    this.$el = el;
    this.opts = opts;
    this.params = this.opts;
  }

  Grid.VERSION = '3.1.1';

  Grid.prototype.init = function(){
    this.clearGrid();
    _defineVariable.call(this);
    _defineDom.call(this);
    _loadDom.call(this);
    _handleOpts.call(this);
    _bindEvents.call(this);
    return this.$el;
  };


  function _defineVariable(){
    this.bNotLoad = false; // 是否初始化加载时不加载数据 
    this.cmParentNodeName; // 表头cm的父节点名称
    this.aThs = [];        // 所有包含dataIndex的表头元素的数组
    this.bHasParent = false;// 是否在cm中通过parentName设置父表头名称
    this.oBar = null;      // 分页工具
    this.oOldTrData = {};  // 编辑前的数据
    this.oNewTrData = {};  // 编辑后的数据
    this.aDelTrData = [];  // 删除的数据
    this.addData = [];     // 新增的数据
    this.iWindowBindTimes = 0; // 是否已经给window添加了resize事件
    this.clickTimeout;     // 行点击事件的定时器，用于区别单击与双击
    this.clickCount = 0;   // 用于区别单击与双击
  };


  // 初始化并缓存dom
  function _defineDom(){
    // .common_grid_scroll 表格最外层容器，用于约束大小，显示滚动条
    this.$gridScroll = $('<div class="common_grid_scroll" style=""></div>'); 
    // .common_grid_div 存放表格的容器
    this.$gridDiv = $('<div class="common_grid_div"></div></div>');
    // .common_table 存放加载数据表格的容器
    this.$dataTableDiv = $('<div class="common_table"></div>');
    // .common_table>.common_grid_table 用于加载数据的表格
    this.$dataTable = $('<table class="common_grid_table">' +
          '<thead ><tr data-level="0"></tr></thead>' +
          '<tbody></tbody>' +
          '<tfoot></tfoot>' +
          '</table>');
    // 加载数据的表格的thead
    this.$dataTHead = this.$dataTable.find('thead');
    // 加载数据的表格的tbody
    this.$dataTBody = this.$dataTable.find('tbody');
    // .common_head 只加载表头的表格的容器
    this.$headTableDiv = $('<div class="common_head" style="position:absolute;top: 0;"></div>');
    // .common_head>.common_grid_table 只加载表头的表格
    this.$headTable = $('<table style="position:absolute; top:0" class="common_grid_table"><thead ></thead></table>');
    // 只加载表头的表格的thead
    this.$headTHead = this.$headTable.find('thead');
  };

  // 清空表格
  Grid.prototype.clearGrid = function(){
    this.$el.empty();
    return this.$el;
  };

  //设置没有toolbar的grid的查询参数
  Grid.prototype.setParams = function(data) {
    this.searchPara = data;
    return this.$el;
  };

  //设置表格加载的参数 data.name --- data.value
  Grid.prototype.setOpts = function(data) {
    var _this = this;
    $.each(data,function(k,v){
      _this.opts[k] = v;
    });
    return this.$el;
  };

  // 不建议使用
  Grid.prototype.getAddData = function(){
    var _this = this,
      addData  = [];
    $.each(_this.$dataTBody.find('tr[data-isnewdata]'),function(idx,val){
      addData.push(_this.aData[$(val).data('index')]);
    });
    return addData;
  };

  Grid.prototype.getDelData = function(){
    return this.aDelTrData;
  };

  // 获取表格所有值
  Grid.prototype.getData = function(){
    return this.aData;
  };

  //表格自身的查询方法
  Grid.prototype.search = function(oSearchParams){
    var _this = this,
      opts = this.opts;
    if(!_this.$el.find('.common_loadingMask').length){
      _this.loadMask();
      if(opts.url){
        var oDatas = {};
        if (opts.baseForm) {
          oDatas = opts.baseForm.data('cw.formSubmit').getParams();
        }
        if (opts.data) {
          $.each(opts.data, function(name, value) {
            oDatas[name] = value;
          });
        }
        if (_this.searchPara) {
          $.each(_this.searchPara, function(name, value) {
            oDatas[name] = value;
          });
        }
        if (oSearchParams) {
          $.each(oSearchParams, function(name, value) {
            oDatas[name] = value;
            if (name == 'sort') {
              _this.sort = value;
            } else if (name == 'dir') {
              _this.dir = value;
            }
          });
        }
        oDatas.sort = _this.sort;
        oDatas.dir = _this.dir;
        var ajaxPara = { //查询数据
          type: 'post',
          data: oDatas,
          url: opts.url,
          dataType: 'json',
          error: function() {},
          success: function(response) {
            _this.refreshData.call(_this,response);
          },
          complete: function() {}
        };
        if (opts.traditional) {
          ajaxPara['traditional'] = true;
        }
        $.ajax(ajaxPara);
      }
    }
    return _this.$el;
  };

   //加载数据
  Grid.prototype.refreshData = function(response, isAll, page){
    var _this = this,
      opts = this.opts;
    if(!isAll){
      _this.$dataTBody.find('tr').remove();
    }
    if(response.root){
      if(isAll && page != 1){
        _this.aData = _this.aData.concat(response.root);
      }else{
        _this.aData = response.root;
      }
    }

    var addTr = [];
    for (var i = 0; i < response.root.length; i++) { // 根据返回数据的多少，添加对应数量的tr
      var oTr = $('<tr data-have-value="true"></tr>');
      _this.$dataTBody.append(oTr);
      oTr.attr({
        'data-index': isAll ? (page-1)*50 + i : i
      });
      addTr.push(oTr);
      // oTr = null;
    }

    if(opts.height){
      var iTableHeight = _this.$dataTable.outerHeight();
      if(_this.$gridScroll.height() > iTableHeight){// 如果没有tbar则补充相应高度的tr
        var iAddLength = parseInt((_this.$gridScroll.height() - iTableHeight) / _this.$dataTBody.find('tr').height());
        addBlankTr(iAddLength);
      }
    }else if(_this.oBar){
      if(_this.oBar.getLimit() > response.root.length) { //如果返回数据量不足tbar的limt的数量，补充
        addBlankTr(_this.oBar.getLimit() - response.root.length);
      }
    }
    // 补充tr公共方法
    function addBlankTr(length){
      for (var i = 0; i < iAddLength; i++) {
        var oTr = $('<tr></tr>');
        _this.$dataTBody.append(oTr);
        $.each(_this.aThs, function(idx, val) {
          oTr.append('<td></td>');
          if ($(val).css('display') === 'none') {
            oTr.find('td').last().css('display', 'none');
          }
        });
        oTr.attr({
          'data-index': response.root.length + i,
        });
        addTr.push(oTr);
      }
    }

    //如果表格可编辑，则默认在tr的最前面添加含有可编辑按钮的td
    if (opts.editAble) { 
      $.each(addTr, function(idx, val) {
        var oTrThis = val;
        if (oTrThis.data('haveValue') === true) {
          var index = oTrThis.data('index');
          if(_this.oBar){
              if (opts.editBtn2){
                  oTrThis.prepend('<td style="padding:0;">'+
                              '<div class="common_editContent"  style="width:100%">'+
                                '<div title="编辑当前行" data-index=' + index + ' class="common_gridEdit common_editBtn fa fa-pencil"></div>'+
                                '<div title="删除" data-index=' + index + ' class="common_editBtn fa fa-trash"></div>' +
                                '<div title="保存" data-index=' + index + ' class="common_gridSave common_editBtn fa fa-check"></div>'+
                                '<div title="取消" data-index=' + index + ' class="common_gridCancle common_editBtn fa fa-close"></div>'+
                              '</div></td>');
              }else{
                  oTrThis.prepend('<td style="padding:0;">'+
                      '<div class="common_editContent"  style="width:100%">'+
                      '<div title="编辑当前行" data-index=' + index + ' class="common_gridEdit common_editBtn fa fa-pencil"></div>'+
                      '<div title="保存" data-index=' + index + ' class="common_gridSave common_editBtn fa fa-check"></div>'+
                      '<div title="取消" data-index=' + index + ' class="common_gridCancle common_editBtn fa fa-close"></div>'+
                      '</div></td>');
              }
          } else {
            oTrThis.prepend('<td style="padding:0;">'+
                              '<div class="common_editContent" style="width:100%;">'+
                                '<div title="编辑当前行" data-index=' + index + ' class="common_gridEdit common_editBtn fa fa-pencil"></div>'+
                                '<div title="新增" data-index=' + index + ' class="common_editBtn common_add fa fa-plus"></div>'+
                                '<div title="删除" data-index=' + index + ' class="common_editBtn fa fa-trash"></div>'+
                                '<div title="保存" data-index=' + index + ' class="common_gridSave common_editBtn fa fa-check"></div>'+
                                '<div title="取消" data-index=' + index + ' class="common_gridCancle common_editBtn fa fa-close"></div>'+
                              '</div></td>');
          }
        } else {
          oTrThis.prepend('<td style="padding:0;"><div class="common_editContent"  style="width:100%;"></div></td>');
        }
      });
      _this.$dataTBody.find('.common_gridEdit').tooltip();
      _this.$dataTBody.find('.fa-plus').tooltip();
      _this.$dataTBody.find('.fa-trash').tooltip();
      _this.$dataTBody.find('.common_gridSave').tooltip();
      _this.$dataTBody.find('.common_gridCancle').tooltip();
    }

    // 如果有复选框
    if(opts.sm){
      switch(opts.sm){
        case 'checkbox': 
          $.each(addTr,function(idx,val){
            var oTrThis = val;
            if (oTrThis.data('haveValue') === true) {
              var id = 'check'+Math.random().toString(36).substr(2);
              var oCheckTd = $('<td style="padding:0;">'+
                                '<div class="common_checkbox_container">'+
                                  '<input id="'+id+'" class="common_head_checkbox" style="margin:0;" type="checkbox">'+
                                  '<label class="common_grid_check_label fa" for="'+id+'"></label>'+
                                '</div></td>');
              oTrThis.prepend(oCheckTd);
              oCheckTd.find('label').on('click',function(e){
                var $this = $(this);
                if( !$this.parent().parent().parent().hasClass('common_trSel')){
                  $this.parent().parent().parent().addClass('common_trSel').find('input').prop('checked',true);
                }else{
                  $this.parent().parent().parent().removeClass('common_trSel').find('input').prop('checked',false);
                  // _this.element.find('thead .common_head_checkbox').prop('checked',false);
                }
                return false;
              });
            }else {
              oTrThis.prepend('<td  style="padding:0;"><div></div></td>');
            }
          });
        break;
      }
    }

    // 添加数据td
    if(){

    }
    $.each(response.root, function(idx, val) {
      $.each(_this.aThs, function(thidx, thval) {
        var tdValue = (val[$(thval).data('dataindex')]==0||val[$(thval).data('dataindex')])?val[$(thval).data('dataindex')]:''; //表头对应列的值
        var oldTdValue = tdValue;
        var index = isAll ? (page-1)*50 +idx : idx;
        //var oCm = opts.cm[thidx];
        //如果表头有renderer属性，则调用对应的renderer方法。获取表头对应的值
        if (thval.data('renderer')) { 
          tdValue = thval.data('renderer').call(this, tdValue, val, index, $(thval).data('dataindex'));
        }
        var oTd = $('<td ><div>' + tdValue + '</div></td>'); //新建td
        if (thval.data('hidden')) {
          oTd.css('display', 'none');
        }

        oTd.attr({ //td的参数。备用
          'data-td-attr': thval.data('dataindex'),
          'data-tdValue': oldTdValue,
          'data-edittype': thval.data('edittype'),
          'data-datefmt': thval.data('datefmt'),
          'data-editWidth': thval.data('editWidth'),
          'data-index': index
        });
        if(oldTdValue){
          oTd.tooltip({ title: oldTdValue }); //给td添加提示框
        }
        _this.$dataTBody.find('tr').eq(index).append(oTd);
      });
      if(val.isNewData){
        _this.$dataTBody.find('tr').eq(index).attr('data-isNewData',true);
      }
    });

    // 设置td的宽度和text-align
    $.each(_this.aThs, function(thidx, thval) {
      _this.$dataTBody.find('td[data-td-attr=' + thval.data('dataindex') + ']>div').css({
        'text-align': thval.data('align')
      });
    });

    // 设置表格边框
    if (_this.$gridScroll.height() <= _this.$gridDiv.height()
          ||_this.$gridScroll.width() <= _this.$gridDiv.width()) {
      _this.$gridScroll.css('border-style','solid');
    }

    // 给可点击cell添加样式
    if (opts.cellRander) {
      $.each(opts.cellRander, function(idx, val) {
        _this.$dataTBody.find('td[data-td-attr=' + val + ']  div').addClass('common_click_td').attr('data-dataIndex', val);
      });
    }

    // 数据加载完毕的回调
    if (opts.callback) {
      opts.callback.call(_this);
    }
    if (opts.callBack) {
      opts.callBack.call(_this);
    }

    if (_this.oBar) {
      _this.oBar.oTbarContent.find('.common_tbarMask ').remove();
    } else if(opts.editAble === true){
       if(!_this.aData.length){
          _this.aData.push({});
          _this.refreshData({root:_this.aData});
        } 
    }
    _this.$el.find('.common_loadingMask').remove();

    //默认选中第一行
    if ((opts.clicked && !_this.oBar) || (opts.clicked && _this.oBar && _this.oBar.iPage == 1 && page===1)) { 
      opts.clicked = false;
      _this.$dataTBody.find('tr').first().trigger('click');
    }
    return _this.$el;
  };

  // 获取初始化是否加载
  Grid.prototype.getNoLoad = function(){
    return this.bNotLoad;
  };

  // 获取选中行的数据
  Grid.prototype.getSelectData = function(){
    var datas= [],
        _this = this;
    $.each(_this.$dataTBody.find('.common_trSel'),function(idx,val){
      datas.push(_this.aData[parseInt($(val).data('index'))]);
    });
    return datas;
  };

  // 加载中蒙版
  Grid.prototype.loadMask = function(){
    var _this = this;
    var oMask = $('<div class="common_loadingMask"><div class="fa fa-spinner fa-pulse fa-fw" style="font-size: 30px;"></div></div>');
    _this.$gridScroll.append(oMask);
    oMask.css('top', _this.$gridScroll.scrollTop())
         .css('left', _this.$gridScroll.scrollLeft());
    _this.$gridScroll.on('scroll', function() { //表格上下拖动时，表头表的位置始终保持不变
      oMask.css('top', _this.$gridScroll.scrollTop())
           .css('left', _this.$gridScroll.scrollLeft());
    });
    return _this.$el;
  };
  // 加载弹框
  Grid.prototype.confirm = function(params){
      var _this = this;
      var oThis = {};
      oThis.oWin = $('<div class="grid-common_win_small" style="opacity:0"></div>');
      oThis.oTitle = $('<div class="common_win_small_title"></div>');
      oThis.oMsg = $('<div class="common_win_small_msg"></div>');
      oThis.oBtnContent = $('<div class="common_winSmallBtnContent"></div>');
      var oYBtn = $('<button class="common_win_small_btn" style="margin-right:10px;">确定</button>');
      var oNBtn = $('<button class="common_win_small_btn" >取消</button>');
      oThis.oWin.append(oThis.oTitle);
      oThis.oWin.append(oThis.oMsg);
      oThis.oWin.append(oThis.oBtnContent);
      oThis.oBtnContent.append(oYBtn).append(oNBtn);
      var oMask = null;

      oYBtn.on('click', function() {
          oThis.oWin.fadeOut('normal', function() {
              this.remove();
              if (params) {
                  if (params.callback) {
                      params.callback.call();
                  }
              }
          });
          // oMask.hide();
          _this.$el.find('.common_loadingMask').remove();
      });
      oNBtn.on('click', function() {
          oThis.oWin.fadeOut('normal', function() {
              this.remove();
          });
          // oMask.hide();
          _this.$el.find('.common_loadingMask').remove();
      });

      if (params) {
          if (params.parentDoc) {
              if (!params.maskRander) {
                  // oMask = _this.loadMask({ randerTo: $('body', params.parentDoc) });
                  oMask = _this.loadMask();
              }
              // $('body', params.parentDoc).append(oThis.oWin);
              $('body').append(oThis.oWin);
              oThis.oWin.css({
                  top: $(params.parentWin).height() / 2 + $(params.parentDoc).scrollTop() - 75 + 'px',
                  left: $(params.parentWin).width() / 2 + $(params.parentDoc).scrollLeft() - 150 + 'px'
              });
          } else {
              oMask = _this.loadMask();
              $('body').append(oThis.oWin);
              // alert();
              oThis.oWin.css({
                  top: $(window).height() / 2 + $(document).scrollTop() - 75 + 'px',
                  left: $(window).width() / 2 + $(document).scrollLeft() - 150 + 'px'
              });
          }
          oMask.show();
          oThis.oWin.fadeTo('normal', 0.9);
          oThis.oWin.fadeTo(1, 1);


          if ($.isPlainObject(params)) {

              oThis.oWin.fadeTo('normal', 0.9);
              oThis.oWin.fadeTo(1, 1);

              if (params.title) {
                  oThis.oTitle.text(params.title);
              } else {
                  oThis.oTitle.outerHeight(0);
              }
              if (params.msg) {
                  oThis.oMsg.html(params.msg);
              }
          } else {
              oThis.oMsg.text(params);
              oThis.oWin.fadeTo('normal', 0.9);
              oThis.oWin.fadeTo(1, 1);
              oThis.oTitle.outerHeight(0);
          }
      }

      oThis.closeWindow = function() {
          oThis.oWin.fadeOut('normal', function() {
              this.remove();
          });
          // oMask.hide();
          _this.$el.find('.common_loadingMask').remove();
      };
      return oThis;
  };

  // 装载dom
  function _loadDom(){
    var _this = this;
    _this.$el.css({
      'display': 'flex',
      'flex-direction': 'column',
//    'overflow': 'hidden',
    });
    _this.$el.append(
      _this.$gridScroll.append(
        _this.$gridDiv.append(
          _this.$dataTableDiv.append(_this.$dataTable)).append(
          _this.$headTableDiv.append(_this.$headTable))));
  };

  //加载表头
  function _loadHead(cm) {
    var _this = this,
      iThCount = 0,
      bHasParent = false,
      opts = this.opts;
      if(typeof opts.sm === 'string'){
        switch(opts.sm){
          case 'checkbox': 
            var id = 'thCheck'+Math.random().toString(36).substr(2);
            var  oCheckTh = $('<th class="common_checkbox_th" style="padding:0;">'+
                                '<div class="common_checkbox_container">'+
                                  '<input id="'+id+'" class="common_head_checkbox" style="margin:0;" type="checkbox">'+
                                  '<label for="'+id+'" class="common_grid_check_label fa"></label>'+
                                '</div></th>');
            _this.$dataTHead.find('tr').append(oCheckTh); // 表头中添加个全选的checkbox
            oCheckTh = null;
            break;
        }
      }
      if(typeof opts.editAble === 'boolean' && opts.editAble === true){
        //如果表格可编辑，则添加个编辑按钮的行
        _this.$dataTHead.find('tr').append('<th class="editAbleTh" style="width:'+((opts.tbar || opts.bbar )?'0.6':'0.9')+'rem;padding:0;"><div  style="justify-content:center;"></div></th>');
      }
      

      _handleCm.call(_this, cm);

      var trLength = _this.$dataTHead.find('tr').length;
      if(trLength > 1 && _this.bHasParent === false){  //多级表头加工设置rowspan和colspan
          var oTr = _this.$dataTHead.find('tr').last(),
            row = 1; // 设置rowspan 
          $('th[data-children]').attr('colspan',0);
          while(oTr.length) {  // 循环设置每一行的tr中的th
            $.each(oTr.find('th'),function(){
              var $this = $(this);
              var pNodeName = $this.data('pnodename');
              if($this.data('colspan')){
                $this.attr('colspan', $this.data('colspan'));
              }
              if(pNodeName) {
                var pNode = _this.$dataTHead.find('th[data-header="'+pNodeName+'"]');
                if(!pNode.data('colspan')){
                  var colspan = 0;
                  $.each(_this.$dataTHead.find('th[data-pnodename="'+pNodeName+'"]'),function(){
                    colspan+=parseInt($(this).attr('colspan'));
                  });
                  pNode.attr('colspan',colspan);
                }
              }
              if($this.data('rowspan')){
                $this.attr('rowspan', $this.data('rowspan'));
              }else {
                if(!$this.data('children')){
                  $this.attr('rowspan',row);
                }
              }
              
            });
            row++;
            oTr = oTr.prev();
          }
      } 
      if(_this.bHasParent === true) { 
        //设置通过parentName设置的多级表头没有父节点的th的rowspan为2
        _this.$dataTHead.find('th:not(.grid_parent)').attr('rowspan', 2); 
      }
      //设置固定表头的表的表头和实际表的表头相同
      _this.$headTHead.html('').append(_this.$dataTHead.html()); 
      if (opts.sm) {
        switch(opts.sm){
          case 'checkbox':
            var id ="gridHeadTop"+_this.$headTHead.find('.common_head_checkbox').attr('id');
            _this.$headTHead.find('.common_head_checkbox').attr('id',id);
            _this.$headTHead.find('label').attr('for',id);
            _this.$headTHead.find('.common_head_checkbox').on('click',function(){
              if($(this).is(':checked')){
                _this.$dataTBody.find('tr[data-have-value]').addClass('common_trSel').find('.common_head_checkbox').prop('checked',true);
              }else{
                _this.$dataTBody.find('tr[data-have-value]').removeClass('common_trSel').find('.common_head_checkbox').prop('checked',false);
              }
            });
            break;
          case 'radio':
            break;
        }
      }

      var headHeight = _this.$headTableDiv.position().top;
      _this.$gridScroll.off('scroll.sw.grid');
      //表格上下拖动时，表头表的位置始终保持不变
      _this.$gridScroll.on('scroll.sw.grid',function(){
        _this.$headTableDiv.css('top', _this.$gridScroll.scrollTop() + headHeight);
      });
     
  }

  // 处理表头数据cm
  function _handleCm(cm, pNodeName){
    var _this = this;
    $.each(cm, function(idx,val){
      if(pNodeName){
        val.pNodeName = pNodeName;
      }
      console.log(val);
      var oTh = null;
      oTh = $('<th rowspan="1" colspan="1"><div >' + val.header + '</div></th>');

      if(val.width){
        oTh.width(val.width);
      }
      if(val.hidden){
        oTh.css('display', 'none');
      }

      //将定义表头的obj传入对应的th中
      $.each(val, function(idx,val) {
        oTh.attr('data-' + idx, val);
        oTh.data(idx, val);
      });
      oTh.attr('data-thIndex', idx);

      if (val.dataIndex ){
        _this.aThs.push(oTh);
      }
      if(!pNodeName){
        if( !val.parentName ){ 
          _this.$dataTHead.find('tr:first').append(oTh);
        } else { // 如果是同过parentName设置的父节点
          _this.bHasParent = true;
          if( _this.$dataTHead.find('tr').length <=1 ){ //如果只有一个tr则新增一个tr
            _this.$dataTHead.append('<tr></tr>');
          }
          if( !_this.$dataTHead.find('th[data-pName="'+val.parentName+'"]').length) { //如果父节点不存在则添加父节点
            _this.$dataTHead.find('tr:first').append('<th class="grid_parent" colspan="1" data-pName="' + val.parentName + '"><div>' + val.parentName + '</div></th>');
          } else {
            var pNode = _this.$dataTHead.find('th[data-pName = ' + val.parentName + ']');
            pNode.attr('colspan', parseInt(pNode.attr('colspan')) + 1);
          }
          _this.$dataTHead.find('tr:eq(1)').append(oTh);
        }
        
      } else {
        var pNode = _this.$dataTHead.find('th[data-header="'+pNodeName+'"]');
        var pTrLevel = pNode.parent().data('level');
        pTrLevel =  (pNode.data('rowspan') ? parseInt(pNode.data('rowspan')) : 0) + parseInt(pTrLevel) + 1;
        var levelBefore = pTrLevel;
        var aTr = [];
        var oTr;
        var hasTr = false;
        while(pTrLevel>=1){
          oTr = _this.$dataTHead.find('tr[data-level="'+pTrLevel+'"]');
          if(oTr.length === 0){
            oTr = $('<tr data-level="'+pTrLevel+'"></tr>');
            aTr.unshift(oTr);
          }else{
            if(levelBefore === pTrLevel){
              hasTr = true;
            }
            break;
          }
          pTrLevel --;
        }
        if(!hasTr){
          $.each(aTr,function(){
            oTr = this;
            _this.$dataTHead.append(this);
          });
        }
        oTr.append(oTh);
      }
      if (val.remoteSort) {
        oTh.addClass('common_sortHead').attr({
          'data-remoteSort': val.remoteSort,
          'data-sortValue': 'none',
        });
      }
      if(val.children) {
        // console.log('p_name: '+ val.header);
        _handleCm.call(_this, val.children, val.header);
      }
    });
  };

  // 处理传参
  function _handleOpts(){
    var opts = this.opts,
      _this = this;
    if(typeof opts.baseForm === 'object') {
      //如果传入表单。则把表格本身传入表单。表单查询时，自动调用表格的search方法。并把表单的参数传进去
      opts.baseForm.data('cw.formSubmit').setGrid(this);
    }
    if(opts.isNotLoad || opts.doLoad == false) {
      _this.bNotLoad = true;
    }
    if(typeof opts.cm === 'object'){
      _loadHead.call(_this, opts.cm);
    }
    if(opts.width){
      opts.width = typeof opts.width === 'number' ? opts.width + 'px' : opts.width;
      _this.$el.css('width', opts.width);
    }
    if(opts.height){
      opts.height = typeof opts.height === 'number' ? opts.height + 'px' : opts.height;
      _this.$el.css('height', opts.height);
    }
    if(typeof opts.tbar === 'object'){
      _this.oBar = opts.tbar;
      _this.oBar.init();
      _this.$gridScroll.before(_this.oBar.oTbarContent);
      _this.oBar.setGrid(_this);
    }
    if(typeof opts.bbar === 'object'){
      _this.oBar = opts.bbar;
      _this.oBar.init();
      _this.$gridScroll.after(_this.oBar.oTbarContent);
      _this.oBar.setGrid(_this);
    }
    if(!_this.bNotLoad && !_this.oBar) {
      _this.search();
    }
    if(opts.editAble === true){
      _loadEdit.call(_this);
    }
  }

  // 绑定事件
  function _bindEvents(){
    var _this = this,
        opts = this.opts;
    _this.iWindowBindTimes ++;
    if(_this.iWindowBindTimes === 1){
      // 页面大小改变时，修改边框的显示方式,绑定一次就行了
      $(window).on('resize.sw.grid', _windowResizeEvent.bind(_this));
    }
    
    // 行点击事件
    // if(opts.click){
     _this.$gridScroll.off('click.sw.grid', 'tbody tr')
        .on('click.sw.grid', 'tbody tr', _lineClickEvent.bind(_this));
    // }
    // 行双击事件
    if(opts.dbclick) {
      _this.$gridScroll.off('dblclick.sw.grid', 'tbody tr')
        .on('dblclick.sw.grid', 'tbody tr', _linedbClickEvent.bind(_this));
    }

    // 单元格点击事件
    if(opts.cellRander) {
      $.each(opts.cellRander, function(idx,val){
        _this.$gridScroll.off('click.sw.grid.cell', 'div[data-dataIndex=' + val + ']')
          .on('click.sw.grid.cell', 'div[data-dataIndex=' + val + ']', _cellClickEvent.bind(_this));
      });
    }

    // 表格排序事件
    _this.$gridScroll.off('click.sw.grid', '.common_sortHead')
      .on('click.sw.grid', '.common_sortHead', _sortGridEvent.bind(_this));
  };

  // 表格排序事件
  function _sortGridEvent(e){
    var _this = this,
        $thThis = $(e.currentTarget),
        $ascBtn = $thThis.find('.common_asc'),
        $descBtn = $thThis.find('.common_desc'),
        $sortContent = $thThis.find('.common_sort');

    _this.$headTHead.find('th[data-dataindex != "'+$thThis.data('dataindex')+'"]').find('.common_sort').remove();
    if (_this.oBar) {
      _this.$dataTBody.find('tr').remove();
      _this.oBar.page = 0;
    }
    if($sortContent.length){
      $sortContent.find('.common_sortSelected').removeClass('common_sortSelected').siblings().addClass('common_sortSelected');
    } else {
      $sortContent = $('<div class="common_sort" style="overflow:hidden;white-space:normal;"></div>');
      $thThis.append($sortContent);
      $sortContent.append('<i class="common_asc fa fa-caret-up common_sortSelected" data-type="asc"></i>')
                  .append('<i class="common_desc fa fa-caret-down" data-type="desc"></i>');
      
    }
    var dir = $sortContent.find('.common_sortSelected').data('type');
    if (_this.oBar) {
      _this.oBar.search({ sort: $thThis.data('remotesort'), dir: dir });
    } else {
      _this.search({ sort: $thThis.data('remotesort'), dir: dir });
    }
  }

  // 单元格点击事件
  function _cellClickEvent(e){
    var _this = this,
        opts = this.opts,
        $tdThis = $(e.currentTarget),
        dataIndex = $tdThis.parent().data('tdAttr');
    _this.$dataTable.find('td').removeClass('common_tdDivSel');
    $tdThis.parent().addClass('common_tdDivSel');
    if(opts.cellClick){
      if(typeof opts.cellClick[dataIndex] === 'function'){
        opts.cellClick[dataIndex].call($tdThis, $tdThis.parent().data('tdvalue'), _this.aData[$tdThis.parent().parent().data('index')]);
      }
    }
    return false;
  }

  // 行双击事件
  function _linedbClickEvent(e){
    var _this = this,
        opts = this.opts,
        $trThis = $(e.currentTarget);
    clearTimeout(_this.clickTimeout);
    if($trThis.find('td[data-td-attr]').length<=0){
      return;
    }
    _this.clickCount = 0;
    $trThis.siblings().find('.common_head_checkbox').prop('checked', false);
    $trThis.find('.common_head_checkbox').prop('checked', true);
    $trThis.addClass('common_trSel').siblings().removeClass('common_trSel');
    opts.dbclick.call(_this, _this.aData[parseInt($trThis.data('index'))]);
  }

  // 行单击事件
  function _lineClickEvent(e){
    var _this = this,
        opts = this.opts,
        $trThis = $(e.currentTarget);
    if(_this.clickCount === 0){
      if($trThis.find('td[data-td-attr]').length <= 0){
        return;
      }
      _this.clickTimeout = setTimeout(function() {
        $trThis.siblings().find('.common_head_checkbox').prop('checked', false);
        $trThis.find('.common_head_checkbox').prop('checked', true);
        $trThis.addClass('common_trSel').siblings().removeClass('common_trSel');
        _this.clickCount = 0;
        if(opts.click){
          opts.click.call(_this, _this.aData[parseInt($trThis.data('index'))]);
        }
      }, 250);
    }
    _this.clickCount++;
  }

  function _windowResizeEvent(e){
    var _this = this;
    if(_this.$gridScroll.length 
        && _this.$gridDiv.length 
        && (_this.$gridScroll.height() <= _this.$gridDiv.height() 
            || _this.$gridScroll.width() <= _this.$gridDiv.width() )) {
      _this.$gridScroll.css('border-style','solid');
    } else if ( _this.$gridScroll.length) {
      _this.$gridScroll.css('border-style','hidden');
    }
  }

  // 设置可编辑表格
  function _loadEdit(){
    var _this = this;
    _this.oOldTrData = {}; //编辑前的数据
    _this.oNewTrData = {}; //编辑后的数据
    _this.$gridScroll.off('click.sw.grid', '.common_editContent .fa-plus')
      .on('click.sw.grid', '.common_editContent .fa-plus', _plusBtnEvent.bind(_this))// 新增行
      .off('click.sw.grid', '.common_editContent .fa-trash')
      .on('click.sw.grid' , '.common_editContent .fa-trash', _trashBtnEvent.bind(_this))//删除行
      .off('click.sw.grid', '.common_gridEdit')
      .on('click.sw.grid' , '.common_gridEdit', _editBtnEvent.bind(_this))//编辑行
      .off('click.sw.grid', '.common_gridSave')
      .on('click.sw.grid' , '.common_gridSave', _saveBtnEvent.bind(_this))//保存行
      .off('click.sw.grid', '.common_gridCancle')
      .on('click.sw.grid' , '.common_gridCancle', _cancleBtnEvent.bind(_this))//取消编辑行
  };

  //取消编辑行的事件
  function _cancleBtnEvent(e){
    var _this = this;
    _this.confirm({
      msg:'确定要取消吗？',
      callback:function(){
        _this.refreshData({root:_this.aData});
      }
    });
    return false;
  }

  // 保存编辑行的事件
  function _saveBtnEvent(e){
    var _this = this,
        opts = this.opts,
        $thisBtn = $(e.target),
        $thisTr = $thisBtn.parent().parent().parent().addClass('common_trSel'),
        trIndex = parseInt($thisBtn.data('index'));
    $thisTr.find('.common_editTd').css('overflow', 'hidden').removeClass('common_editTd');
    // 获取编辑后的值
    $.each($thisBtn.parent().parent().siblings(), function(index, value) {
      var $thisTd = $(value);
      if ($thisTd.find('input').length > 0) {
        if($thisTd.data('tdAttr')){
          _this.oNewTrData['td' + trIndex][$thisTd.data('tdAttr')] = $thisTd.find('input').val();
        }
      } else if ($thisTd.find('textarea').length > 0) {
        _this.oNewTrData['td' + trIndex][$thisTd.data('tdAttr')] = $thisTd.find('textarea').val();
      }
    });
    if (opts.afterEdit) {
      var returnPara = opts.afterEdit.call(_this, $thisTr);
      if(returnPara){
        $.each(returnPara,function(idx,val){
          _this.oNewTrData['td' + trIndex][idx]=val;
        });
      }
    }
    if (opts.saveValid) {
      var errArr = opts.saveValid.call(_this, _this.oNewTrData['td' + trIndex]);
      if (errArr) {
        $.each(errArr, function(index, value) {
          _this.oNewTrData['td' + trIndex][value] = _this.oOldTrData['td' + trIndex][value];
        });
      }
    }
    if (opts.submitUrl||opts.editUrl) {
      var url = opts.submitUrl?opts.submitUrl:opts.editUrl;
      $.ajax({
        type: 'post',
        data: _this.oNewTrData['td' + trIndex],
        url: url,
        error: function() {},
        success: function(response) {
          if (opts.submitCallBack) {
            opts.submitCallBack.call(_this, response);
          }
        },
        complete: function() {}
      });
    }else{
      // console.log( _this.aData);
      $thisBtn.trigger('mouseleave');
      _this.refreshData({root:_this.aData});
    }
    return false;
  }

  //根据type添加不同类型的编辑控件
  function _addInput(type, value){
    var $tdThis = $(value),
        $contentDiv = $tdThis.children().first();
    $contentDiv.css('overflow', 'visible')
              .addClass('common_editTd')
              .html('')
              .append('<div class="common_editDiv"></div>');
    switch(type){
      case 'TEXT':
        $contentDiv.find('.common_editDiv').textField({
            width: '100%',
            name: 'gridInput',
        }).textField('setValue',$(value).data('tdvalue'));
        break;
      case 'TEXTAREA':
        $contentDiv.find('.common_editDiv').textArea({
            name: 'gridTextArea',
            width: '100%',
            height: '0.8rem',
        }).textArea('setValue',$(value).data('tdvalue'));
        $contentDiv.find('.common_editDiv').find('textarea').css('resize', 'both');
      break;
    } 
    $tdThis.find('.common_tdDiv').css('text-decoration', 'inherit');
    $tdThis.find('input,textarea').on('click', function() {
        return false;
    });
  }

  // 编辑行按钮点击事件
  function _editBtnEvent(e){
    var _this = this,
        opts = this.opts,
        $thisBtn = $(e.target),
        $thisTr = $thisBtn.parent().parent().parent().addClass('common_trSel'),
        trIndex = parseInt($thisBtn.data('index'));
    $thisBtn.parent().find('.common_editBtn:visible').hide();
    $thisBtn.parent().find('.common_gridSave,.common_gridCancle').show().css('display','flex');
    _this.oOldTrData['td' + trIndex] = {};
    _this.oNewTrData['td' + trIndex] = _this.aData[trIndex];
    // 设置该行原值，以便还原
    $.each(_this.aData[trIndex], function(name, value) {
      _this.oOldTrData['td' + trIndex][name] = value;
    });
    // 给其他行添加编辑框
    $.each($thisBtn.parent().parent().siblings(), function(index, value) {
      if($(value).data('edittype')){
        _addInput.call(this,$(value).data('edittype').toUpperCase(),value);
      }
    });
    if (opts.beforEdit) {
      $.each($thisTr.find('td[data-td-attr]>div'),function(i,v){
        var $thisTd = $(v).parent();
        opts.beforEdit.call($thisTd,$thisTd, _this.oNewTrData['td' + trIndex]);
      });
    }
    if (opts.loadEdit) {
      opts.loadEdit.call(_this, $thisTr);
    }
    return false;
  }

  // 删除行按钮点击事件
  function _trashBtnEvent(e){
    var _this = this,
        opts = this.opts,
        $thisBtn = $(e.target),
        $thisTr = $thisBtn.parent().parent().parent().addClass('common_trSel');
    _this.confirm({
      msg: '确定要删除所选数据吗？',
      callback: function() {
        if(opts.delectRows){
          opts.delectRows.call(_this,_this.getSelectData());
          return;
        }
        if (opts.removeUrl) {
          $.ajax({
            type: 'post',
            data: _this.getSelectData(),
            url: opts.removeUrl,
            error: function() {

            },
            success: function(response) {
              if (opts.submitCallBack) {
                opts.submitCallBack.call(_this, response);
              }
            },
            complete: function() {}
          });
        }else{
          _this.aDelTrData = _this.aDelTrData.concat(_this.getSelectData());
          var $delRows = _this.$dataTBody.find('.common_trSel');
          for (var i = $delRows.length - 1; i >= 0; i--) {
            console.log($delRows.eq(i).data('index'));
            _this.aData.splice($delRows.eq(i).data('index'),1);
          }
          _this.refreshData({root:_this.aData});
        }
      }
    });
    e.stopPropagation();

  };
  // 添加行按钮点击事件
  function _plusBtnEvent(e){
    var _this = this,
        opts = this.opts,
        $thisBtn = $(e.target),
        $thisTr = $thisBtn.parent().parent().parent();
    _this.$dataTBody.find('.common_newTr').remove();
    var $newTr = $('<tr class="common_newTr" data-index="Add"></tr>');
    $thisTr.after($newTr);
    if(opts.sm){
      $newTr.append('<td ><div></div></td>');
    }
    for (var i = 0; i < _this.aThs.length; i++) {
      _this.oNewTrData[_this.aThs[i].data('dataindex')] = '';
    }
    $newTr.append('<td ><div class="common_editContent">'+
                          '<div title="保存" class="common_editBtn fa fa-check"></div>'+
                          '<div title="取消" class="common_editBtn fa fa-close"></div>'+
                        '</div></td>');
    $newTr.find('.fa-check').tooltip();
    $newTr.find('.fa-close').tooltip();
    $.each(_this.aThs, function(idx, val) {
      var $td = $('<td data-td-attr=' + val.data('dataindex') + ' data-edittype=' + val.data('edittype') + '><div></div></td>');
      $newTr.append($td);
      if ($(val).css('display') == 'none') {
        $td.css('display', 'none');
      }
      $td.find('div').css('overflow', 'visible').addClass('common_editTd').append('<div class="common_editDiv"></div>');
      switch (val.data('edittype')) {
        case 'text':
          $td.find('.common_editDiv').textField({
              width: '100%',
              name: 'gridInput',
            });
          break;
        case 'textArea':
          $td.find('.common_editDiv').textArea({
              width: '100%',
              height: 80,
              name: 'gridTextArea',
            });
            break;
      }
      $td.find('div').css('text-decoration', 'inherit');
      $td.find('.common_editDiv').find('input,textarea').on('click', function() {
        return false;
      });
      if (opts.beforEdit) {
        opts.beforEdit.call(_this, $td, oNewTrData);
      }
    });

    if(opts.loadEdit){
      opts.loadEdit.call(_this, $newTr)
    }

    $newTr.find('.fa-check').on('click', _newTrCheckBtnEvent.bind(_this));// 确认按钮
    $newTr.find('.fa-close').on('click', _newTrCancelBtnEvent.bind(_this));// 取消按钮
  };

  // 新增行中确定按钮点击事件
  function _newTrCheckBtnEvent(e){
    var _this = this,
        opts = this.opts,
        $thisBtn = $(e.target),
        $newTr = $thisBtn.parent().parent().parent(),
        addIndex = $newTr.data('index');

    // 获取所有的td中的值
    $.each($thisBtn.parent().parent().siblings(),function(idx,val){
      var $thisTd = $(val);
      if($thisTd.data('edittype')){
        if($thisTd.find('input').length >0 ){
          _this.oNewTrData[$thisTd.data('tdAttr')] = $thisTd.find('input').val();
        } else if ($thisTd.find('textarea').length > 0) {
          _this.oNewTrData[$thisTd.data('tdAttr')] = $thisTd.find('textarea').val();
        }
      }
    });
    // 如果有afterEdit方法，执行并将返回值赋到结果集中
    if(opts.afterEdit) {
      $.each(opts.afterEdit.call(_this, $newTr),function(idx,val){
        _this.oNewTrData[idx] = val;
      });
    } 

    if(opts.addUrl){
      $.ajax({
        type: 'post',
        data: _this.oNewTrData,
        error: function() {
        },
        success: function(response) {
          if (opts.submitCallBack) {
            opts.submitCallBack.call(_this, response);
          }
        },
        complete: function() {}
      });
    }else{
      var newData = {};
      $.each(_this.oNewTrData, function(idx,val){
        newData[idx] = val;
      });
      newData.isNewData = true;
      _this.addData.push(newData);
      _this.aData.splice(addIndex+1,1,newData);
      $thisBtn.trigger('mouseleave');
      _this.refreshData({root:_this.aData});
    }
    e.stopPropagation();
  }


  // 新增行中取消按钮点击事件
  function _newTrCancelBtnEvent(e){
    var _this = this;
    var $thisBtn = $(e.target),
        $newTr = $thisBtn.parent().parent().parent();
    _this.confirm({
      msg:'确定要取消吗？',
      callback:function(){
        $newTr.remove();
      }
    });
    e.stopPropagation();
  }

  function Plugin(option, methdOpt) {
      var data = this.data('sw.grid');
      if (typeof option == 'string') {
          return data[option].call(data, methdOpt);
      }
      return this.each(function () {
          var $this = $(this);
          var data = $this.data('sw.grid');
          var options = typeof option == 'object' && option;
          if (!data) {
              $this.data('sw.grid', (data = new Grid($this, options)));
              data.init();
          }
      });
  };

  var old = $.fn.grid;
  $.fn.grid = Plugin;
  $.fn.grid.Constructor = Grid;

  //解决冲突
  $.fn.grid.noConflect = function() {
    $.fn.grid = old;
    return this;
  };
})(jQuery);