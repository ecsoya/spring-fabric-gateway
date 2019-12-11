var basePath = $('#baseURL').val();

var key = $('#key').val();
var type = $('#type').val();

$(document).ready(function() {
	queryBlockInfo();
	initDataTable();

	$('#query').bind('keypress keydown keyup', function(e) {
		if (e.keyCode == 13) {
			e.preventDefault();
			performSearch();
		}
	});

	$('[data-toggle="tooltip"]').tooltip()
});

function queryBlockInfo() {
	$('#keyValue').html(key);
}

function initDataTable() {
	var table = $('#dataTable').DataTable(
			{
				"ajax" : function(data, callback) {
					$.ajax(
							{
								url : basePath + "queryhistories?type=" + type
										+ "&key=" + key,
							}).then(function success(res) {
						if (res.data) {
							for (var i = 0; i < res.data.length; i++) {
								res.data[i].index = i + 1;
							}
						}
						callback(res);
						$('[data-toggle="tooltip"]').tooltip();
					}, function fail(data, status) {

					});
				},
				"paging" : false,
				"ordering" : false,
				"info" : false,
				"searching" : false,
				"processing" : true,
				"serverSide" : true,
				"language" : {
					"emptyTable" : "空",
					"processing" : "正在加载……"
				},
				"columns" : [
						{
							"name" : 'index',
							"data" : 'index'
						},
						{
							"name" : "txId",
							"data" : function(row) {
								return '<a href="' + basePath + 'tx?txid='
										+ row.txId + '" data-toggle="tooltip" title="点击查看交易内容">' + row.txId + '</a>';
							}
						}, {
							"name" : "timestamp",
							"data" : function(row){
								return format(row.timestamp);
							}
						}, {
							"name" : "delete",
							"data" : function(row) {
								if (row.isDelete) {
									return '是';
								}
								return '否';
							}
						} ]
			});
	table.on('order.dt search.dt', function() {
		table.column(0, {
			search : 'applied',
			order : 'applied'
		}).nodes().each(function(cell, i) {
			cell.innerHTML = i + 1;
		});
	}).draw();
};

var refreshDataTable = function() {
	$('#dataTable').DataTable().ajax.reload();
};

function performSearch() {
	var query = $('#query').val();
	if (isNaN(query)) {
		window.location.href = basePath + 'tx?txid=' + query;
	} else {
		window.location.href = basePath + 'block?height=' + query;
	}
}
