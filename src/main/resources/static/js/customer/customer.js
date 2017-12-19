$(function () {
	var countryId, cityId, districtId, userId, mchId, mchName, startDate, endDate;
	var _page = 1, _size = 40;
	var mchId = $('#mchid').val() || 0;
	var winheight;

	init();

	function init() {
		initPage();
		initEvent();
		initData();
	}

	function initPage() {
		if($('#table').offset()) {
            winheight = $(window).height() - $('#table').offset().top - 40;
		}
	}

	function initEvent() { }

	function initData() {
		loadList();
	}

	function loadList() {
		var queryParams = {};

		$("#table").bootstrapTable({
			url: '../merchant/orderList?mchId=' + mchId,
			method: 'get',
			height: winheight,
			queryParams: queryParams,
			striped: true,
			pagination: true,
			pageNumber: 1,
			pageSize: _size,
			sidePagination: 'server',
			paginationHAlign: 'center',
			paginationPreText: '«',
			paginationNextText: '»',  
			columns:[
				{
					title: '下单时间',
					field: 'createtime',
				},{
					title: '使用时间',
					field: 'usetime'
				},{
					title: '总价（本地）',
					field: 'kpprice'
				},{
					title: '总价（RMB）',
					field: 'price',
				},{
					title: '订单状态',
					field: 'ststus'
				},{
					title: '订单类型',
					field: 'type'
				},{
					title: '订单编号和流水',
					field: 'orderFormatter',
					formatter: orderFormatter
				}
			],
			responseHandler: function(result){
				if (result.code === 0) {
					for (var i = 0; i < result.data.list.length; i++) {
                        var orderFormatter = {
                            transactionid: result.data.list[i].transactionid,
                            ordersn: result.data.list[i].ordersn
                        }

                        result.data.list[i].orderFormatter = orderFormatter;
                    }

					var returnData = {
						rows: result.data.list,
						total: result.data.total
					};
					
					return returnData;
				}else{
					alert(result.msg)
				}
			}
		}).on("page-change.bs.table", function(e, number, size) {
			$("#table").bootstrapTable('refresh', {
				url:'../merchant/orderList?mchId=' + mchId + '&page='+ number +'&size=' + size,
			});
		});
	}

    function nullFormatter(value) {
        return value || "";
    }

	function orderFormatter(orderFormatter) {
		return [
			'<span>'+ nullFormatter(orderFormatter.transactionid) +'</span><span>'+ nullFormatter(orderFormatter.ordersn) +'</span>'
		].join('');
	}
});
