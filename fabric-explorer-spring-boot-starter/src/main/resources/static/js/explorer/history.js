const basePath = $('#baseURL').val() + '/' + $('#path').val() + '/';

const key = $('#key').val();
const type = $('#type').val();
const viewDetails = $('#viewDetails').val();

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
								url : basePath + "query/history?type=" + type
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
				"columns" : [
						{
							"name" : 'index',
							"data" : 'index'
						},
						{
							"name" : "txId",
							"data" : function(row) {
								return '<a href="' + basePath + 'tx?txid='
										+ row.txId
										+ '" data-toggle="tooltip" title="'
										+ viewDetails + '">' + row.txId
										+ '</a>';
							}
						}, {
							"name" : "timestamp",
							"data" : function(row) {
								if (!isNaN(row.timestamp)) {
									var time = new Date();
									time.setTime(row.timestamp);
									return format(time);
								}
								return format(row.timestamp);
							}
						}, {
							"name" : "delete",
							"data" : function(row) {
								if (row.isDelete) {
									return 'Yes';
								}
								return 'No';
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
