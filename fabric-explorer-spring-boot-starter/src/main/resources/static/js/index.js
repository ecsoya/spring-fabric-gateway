var basePath = $('#baseURL').val();

$(document).ready(function() {

	initDataTable();

	queryBlockchainInfo();

	loadMore();

	$('#query').bind('keypress keydown keyup', function(e) {
		if (e.keyCode == 13) {
			e.preventDefault();
			performSearch();
		}
	});

	$('[data-toggle="tooltip"]').tooltip();
});

function queryBlockchainInfo() {
	$.ajax({
		url : basePath + "queryfabricledger"
	}).then(function success(res) {
		if (res.status > 0) {
			$('#ledgerHeight').html(res.data.height);
			$('#peerInfo').html(res.data.peers);
			$('#orgInfo').html(res.data.orgs.length);
			$('#orgInfo').attr("title", res.data.orgs.toString());
			$('#orgInfo').attr("data-original-title", res.data.orgs.toString());
			$('#commonInfo').html(res.data.channel);
			$('#chaincodeInfo').html(res.data.chaincode);
			$('#chaincodeInfo').attr("title", res.data.chaincodeName);
			$('#chaincodeInfo').attr("data-original-title", res.data.chaincodeName);
			
			$('[data-toggle="tooltip"]').tooltip();
		}
	}, function fail(data, status) {

	});
}

function initDataTable() {
	$('#dataTable')
			.DataTable(
					{
						// "ajax" : function(data, callback) {
						// reloadDatas(callback);
						// },
						"paging" : false,
						"ordering" : false,
						"info" : false,
						"searching" : false,
						"processing" : true,
						"serverSide" : false,
						"scrollY" : '50vh',
						"scrollCollapse" : true,
						"scroller" : {
							"loadingIndicator" : true
						},
						"language" : {
							"emptyTable" : "空",
							"processing" : "正在加载……"
						},
						"columns" : [
								{
									"name" : "blockNumber",
									"data" : function(row) {
										return '#' + row.blockNumber;
									}
								},
								{
									"name" : "currentHash",
									"data" : function(row) {
										return '<div data-toggle="tooltip" title="'+row.currentHash+'">' + row.currentHash.substring(0, 40) + '...</div>';
									}
								},
								{
									"name" : "dataHash",
									"data" : function(row) {
										return '<div data-toggle="tooltip" title="'+row.dataHash+'">' + row.dataHash.substring(0, 40) + '...</div>';
									}
								},
								{
									"name" : "transactionCount",
									"data" : function(row) {
										return '<a href="'
												+ basePath
												+ 'block?height='
												+ row.blockNumber
												+ '" data-toggle="tooltip" title="点击查看所有的交易">'
												+ row.transactionCount + '</a>';
									}
								} ]
					});

};

var refreshDataTable = function() {
	$('#dataTable').DataTable().ajax.reload();
};

var bookmark;

function reloadDatas(callback) {
	var query = {}
	query.pageSize = 5;
	query.bookmark = bookmark;
	$.ajax({
		url : basePath + "queryblocks",
		type : 'POST',
		cache : false,
		data : JSON.stringify(query),
		contentType : "application/json;charset=utf-8",
	}).then(function success(res) {
		bookmark = res.bookmark;
		callback(res);
	}, function fail(data, status) {

	});
}

function loadMore() {
	// disable button
	$('#loadMoreButton').prop("disabled", true);
	// add spinner to button
	$($('#loadMoreButton'))
			.html(
					'<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> 加载中……');
	reloadDatas(function(res) {
		var table = $('#dataTable').DataTable();
		table.rows.add(res.data);
		table.draw();

		// disable button
		$('#loadMoreButton').prop("disabled", false);
		// add spinner to button
		$($('#loadMoreButton')).html('加载更多');

		$('[data-toggle="tooltip"]').tooltip();
	});
}

function performSearch() {
	var query = $('#query').val();
	if (isNaN(query)) {
		window.location.href = basePath + 'tx?txid=' + query;
	} else {
		window.location.href = basePath + 'block?height=' + query;
	}
}