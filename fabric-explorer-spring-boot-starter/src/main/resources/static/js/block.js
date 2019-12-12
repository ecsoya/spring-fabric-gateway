const basePath = $('#baseURL').val();
const blockNumber = $('#height').val();
const viewTitle = $('#viewTitle').val();
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
	$('#blockHeight').html(blockNumber);
	$.ajax({
		url : basePath + "query/block/" + blockNumber,
	}).then(function success(res) {
		if (res.status > 0) {
			$('#previousHash').html(res.data.previousHash);
			$('#dataHash').html(res.data.dataHash);
		}
		$('[data-toggle="tooltip"]').tooltip()
	}, function fail(data, status) {

	});
}

function initDataTable() {
	var table = $('#dataTable').DataTable(
			{
				"ajax" : function(data, callback) {
					var height = $('#height').val();
					$.ajax(
							{
								url : basePath
										+ "query/transactions/"
										+ height,
							}).then(function success(res) {
						callback(res);
						$('[data-toggle="tooltip"]').tooltip()
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
					"emptyTable" : "-",
					"processing" : "Loading…"
				},
				"columns" : [
						{
							"name" : 'index',
							"data" : function(row) {
								return row.index + 1;
							}
						},
						{
							"name" : "txId",
							"data" : function(row) {
								return '<a href="' + basePath + 'tx?txid='
										+ row.txId
										+ '" data-toggle="tooltip" title="'
										+ viewTitle + '">' + row.txId + '</a>';
							}
						}, {
							"name" : "date",
							"data" : function(row) {
								return format(row.date);
							}
						} ]
			});
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
