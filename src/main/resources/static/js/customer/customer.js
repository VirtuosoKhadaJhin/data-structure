$(function () {
	var countryId, cityId, districtId, userId, mchId, mchName, startDate, endDate;
	var _page = 1, _size = 40;

	init();

	function init() {
		initPage();
		initEvent();
		initData();
	}

	function initPage() { }

	function initEvent() { }

	function initData() {
		loadList();
	}

	function loadList() {
		var queryParams = {
			countryId: countryId,
			cityId: cityId,
			districtId: districtId,
			userId: userId,
			mchId: mchId,
			mchName: mchName,
			startDate: startDate,
			endDate: endDate
		};

		$("#table").bootstrapTable({
			url: '../follow/list?page='+ _page +'&size=' + _size,
			method: 'post',
			queryParams: queryParams,
			height: 200,
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
					field: 'followTime',
				},{
					title: '使用时间',
					field: 'followTime'
				},{
					title: '总价（本地）',
					field: 'followTime'
				},{
					title: '总价（RMB）',
					field: 'userName',
				},{
					title: '订单状态',
					field: 'content',
					width: '30%'
				},{
					title: '订单类型',
					field: 'imgs.length'
				},{
					title: '订单编号和流水',
					field: 'content'
				}
			],
			responseHandler: function(result){
				if (result.code === 0) {
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
				url: '../follow/list?page='+ number +'&size=' + size,
			});
		});
	}
});
